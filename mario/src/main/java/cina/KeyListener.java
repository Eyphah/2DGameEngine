package cina;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener(){

    }

    //get method
    public static KeyListener get() {
        if(KeyListener.instance==null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    //Key callback
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action==GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action==GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    //checks if key is pressed
    public static boolean isKeyPressed(int keyCode){
        if(keyCode<get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else {
            return false;
        }
    }
}
