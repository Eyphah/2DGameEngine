package util;
/** Used to implement notion of time which is useful for example for sprite animations */
public class Time {
    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted)* 1E-9); //converting from ns to s
    }
}
