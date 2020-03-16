package matyk.engine;

import matyk.engine.components.Camera;
import matyk.engine.data.Window;
import matyk.engine.managers.*;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.render.DefaultRenderer;
import matyk.engine.render.IRenderer;
import matyk.engine.render.LightRenderer;
import matyk.engine.utils.CommonUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Engine {
    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        WindowManager.windows.add(new Window(720, 1280).init());
        LightManager.renderers.get(0).init();
        glfwSetKeyCallback(WindowManager.windows.get(0).winID, (window, key, scancode, action, mods) -> {
            if(key == GLFW.GLFW_KEY_X && action == GLFW_RELEASE) {
                camera = camera == mainCamera? lightCamera : mainCamera;
            }
        });
    }

    private static ArrayList<Runnable> execBefore = new ArrayList<>();
    private static ArrayList<Runnable> execAfter = new ArrayList<>();
    public static Camera mainCamera = new Camera();
    public static Camera lightCamera = new Camera();
    public static Camera camera = mainCamera;

    static Thread renderThread = new Thread(() -> {
        init();
        while(!WindowManager.shouldClose()) {
            execBefore.forEach(Runnable::run);
            execBefore.clear();
            for(Window wnd:WindowManager.windows) {
                camera.move(wnd, 1f/60f);
                wnd.update();
                for(LightRenderer lightRenderer : LightManager.renderers)
                    CommonUtils.cast(NodeManager.iterateStream(), Light.class)
                        .forEach(light -> lightRenderer.render(light, wnd));
                glViewport(0, 0, wnd.w, wnd.h);
                wnd.clear();
                for (IRenderer rndr : RenderManager.renderers)
                    CommonUtils.cast(NodeManager.iterateStream(), Spatial.class)
                        .forEach(spatial -> rndr.render(spatial, wnd));

                wnd.swapBuffers();
            }
            execAfter.forEach(Runnable::run);
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
