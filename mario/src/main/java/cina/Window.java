package cina;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;


public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;
    /**Builds a window object for our game engine*/
    private Window(){
        this.width=1920;
        this.height=1080;
        this.title = "Mario";
        r=1;
        g=1;
        b=1;
        a=1;
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

        //Free the memory once our loop has exited
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and then free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
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

        //Forwarding actions executed on the window to MouseListener class
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        //Same thing with KeyListener class
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallback);

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
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(fadeToBlack){
                r = Math.max(r-0.01f, 0);
                g = Math.max(g-0.01f, 0);
                b = Math.max(b-0.01f, 0);
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                fadeToBlack=true;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }
}
