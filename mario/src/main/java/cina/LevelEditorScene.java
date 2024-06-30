package cina;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            //position              //color                 //UV coords
            100f, 0f, 0.0f,      1.0f, 0.0f,0.0f,1.0f, 1,1, //bottom right 0
            0f,100f,0.0f,         0.0f,1.0f,0.0f,1.0f, 0,0, //top left 1
            100f,100f,0.0f,       0.0f,0.0f,1.0f,1.0f, 1,0,  //top right 2
            0f,0f,0.0f,           1.0f,1.0f,0.0f,1.0f,  0,1//bottom left 3
    };
    //IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, //top right triangle
            0, 1, 3 //bottom left triangle
    };
    private int vaoID,vboID,eboID;
    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;
    private boolean firstTime = false;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        System.out.println("Creating 'test object'");
        this.testObj=new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(-200,-300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");

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
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0,positionSize,GL_FLOAT, false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT,false,vertexSizeBytes,(positionSize*Float.BYTES));
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT,false,vertexSizeBytes,(positionSize+colorSize)*Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
//        camera.position.x -= dt * 50.0f;
//        camera.position.y -= dt * 20.0f;

        defaultShader.use();

        //Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
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

        defaultShader.detach();

        if (!firstTime) {
            System.out.println("Creating gameObject");
            GameObject go = new GameObject("Game Test 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
    }
}
