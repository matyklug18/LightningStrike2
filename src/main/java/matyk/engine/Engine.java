package matyk.engine;

import matyk.engine.data.Window;
import matyk.engine.managers.NodeManager;
import matyk.engine.managers.WindowManager;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.render.DefaultRenderer;
import matyk.engine.render.IRenderer;
import matyk.engine.render.RenderManager;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {
    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        WindowManager.windows.add(new Window(300, 300).init());
    }

    static ArrayList<Runnable> execLater = new ArrayList<>();

    static Thread renderThread = new Thread(() -> {
        init();
        while(!WindowManager.shouldClose()) {
            for(Runnable exec:execLater) {
                exec.run();
            }
            execLater.clear();
            for(Window wnd:WindowManager.windows) {
                wnd.update();
                for (IRenderer rndr : RenderManager.renderers)
                    for (Node node : NodeManager.iterate())
                        if (node instanceof Spatial)
                            rndr.render((Spatial) node, wnd);
                wnd.swapBuffers();
            }
        }
        end();
    }, "render");

    static Thread physicsThread = new Thread(() -> {

    }, "physics");

    public static void start() {
        RenderManager.renderers.add(new DefaultRenderer());
        renderThread.start();
        physicsThread.start();
    }

    public static void end() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void runLater(Runnable runnable) {
        execLater.add(runnable);
    }
}
