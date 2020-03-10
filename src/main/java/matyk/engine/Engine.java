package matyk.engine;

import matyk.engine.data.Window;
import matyk.engine.managers.LightManager;
import matyk.engine.managers.NodeManager;
import matyk.engine.managers.WindowManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.render.DefaultRenderer;
import matyk.engine.render.IRenderer;
import matyk.engine.managers.RenderManager;
import matyk.engine.render.LightRenderer;
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

    private static ArrayList<Runnable> execBefore = new ArrayList<>();
    private static ArrayList<Runnable> execAfter = new ArrayList<>();

    static Thread renderThread = new Thread(() -> {
        init();
        while(!WindowManager.shouldClose()) {
            for(Runnable exec:execBefore) {
                exec.run();
            }
            execBefore.clear();
            for(Window wnd:WindowManager.windows) {
                wnd.update();
                for (IRenderer rndr : RenderManager.renderers) {
                    for (Node node : NodeManager.iterate())
                        if (node instanceof Light)
                            LightManager.renderers.get(0).render((Light) node, wnd);
                    for (Node node : NodeManager.iterate())
                        if (node instanceof Spatial)
                            rndr.render((Spatial) node, wnd);
                }

                wnd.swapBuffers();
            }
            for(Runnable exec:execAfter) {
                exec.run();
            }
            execAfter.clear();
        }
        end();
    }, "render");

    static Thread physicsThread = new Thread(() -> {

    }, "physics");

    public static void start() {
        RenderManager.renderers.add(new DefaultRenderer());
        LightManager.renderers.add(new LightRenderer());
        renderThread.start();
        physicsThread.start();
    }

    public static void end() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void runBefore(Runnable runnable) {
        execBefore.add(runnable);
    }

    public static void runAfter(Runnable runnable) {
        execAfter.add(runnable);
    }
}
