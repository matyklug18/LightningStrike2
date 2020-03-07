package matyk.engine.managers;

import matyk.engine.data.Window;

import java.util.ArrayList;

public class WindowManager {
    public static ArrayList<Window> windows = new ArrayList<>();

    public static boolean shouldClose() {
        for(Window wnd:windows)
            if(!wnd.shouldClose())
                return false;
        return true;
    }

    public static void swapBuffers() {
        for(Window wnd:windows)
            wnd.swapBuffers();
    }

    public static void update() {
        for(Window wnd:windows)
            wnd.update();
    }
}
