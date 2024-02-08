package cina;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;


public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    private static Window window = null;
    /**Builds a window object for our game engine*/
    private Window(){
        this.width=1920;
        this.height=1080;
        this.title = "Mario";
    }

    /**Returns window object*/
    public static Window get() {
        if (Window.window == null){
            window=new Window();
        }
        return Window.window;
    }

    /***/
    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();
    }

    /**Initializes a window using GLFW, only making it visible once it is created*/
    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        //initialize GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        //create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        //Make the openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);
        //Make the window visible
        glfwShowWindow(glfwWindow);
        /**This line is critical for LWJGL's interpretation with GLFW's OpenGL context, or any context that is managed externally.
         * LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and makes the OpenGL bindings available for use.*/
        GL.createCapabilities();
    }

    /***/
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll events
            glfwPollEvents();
            glClearColor(1.0f, 0.0f, 0.0f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(glfwWindow);
        }
    }
}
