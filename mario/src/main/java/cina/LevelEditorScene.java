package cina;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main(){\n" +
            "    fColor=aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}\n";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main(){\n" +
            "    color=fColor;\n" +
            "}";
    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            //position              //color
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f,0.0f,1.0f, //bottom right 0
            -0.5f,0.5f,0.0f,        0.0f,1.0f,0.0f,1.0f, //top left 1
            0.5f,0.5f,0.0f,         0.0f,0.0f,1.0f,1.0f,  //top right 2
            -0.5f,-0.5f,0.0f,        1.0f,1.0f,0.0f,1.0f //bottom left 3
    };
    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, //top right triangle
            0, 1, 3 //bottom left triangle
    };
    private int vaoID,vboID,eboID;

    public LevelEditorScene(){
        Shader testShader = new Shader("assets/shaders/default.glsl");
    }

    @Override
    public void init(){
        //compile and link the shaders
        //============================
        //First load & compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass shader sourcecode to gpu
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);

        //Check for errors in compilation process
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if (success==GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        //First load & compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass shader sourcecode to gpu
        glShaderSource(fragmentID,fragmentShaderSrc);
        glCompileShader(fragmentID);

        //Check for errors in compilation process
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if (success==GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        //Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success==GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        //=================================
        //Generate our VAO,VBO and EBO buffer objects, and send to GPU
        //=================================
        vaoID=glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer=BufferUtils.createIntBuffer((elementArray.length));
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0,positionSize,GL_FLOAT, false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT,false,vertexSizeBytes,(positionSize*floatSizeBytes));
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //Bind shader program
        glUseProgram(shaderProgram);
        //Bind the VAO
        glBindVertexArray(vaoID);
        //Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}