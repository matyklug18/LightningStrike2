package matyk.engine;

import matyk.engine.components.CLight;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.data.Window;
import matyk.engine.managers.*;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.render.DefaultRenderer;
import matyk.engine.render.IRenderer;
import matyk.engine.render.LightRenderer;
import matyk.engine.utils.OBJLoader;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {
    public static void init() {
        glfwMakeContextCurrent(WindowManager.windows.get(0).winID);
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        LightManager.renderers.get(0).init();
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
            LightManager.renderers.get(0).init();
            for(Window wnd:WindowManager.windows) {
                wnd.update();
                ArrayList<Node> nodes = NodeManager.iterate();
                for(Node node:nodes) {
                    if(node instanceof Spatial) {
                        try {
                            node.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        node.getComponent(CMaterial.class).material = new Material().init();
                    }
                }
                for (int i = 0; i < nodes.size(); i ++) {
                    Node node = nodes.get(i);
                    if (node instanceof Light)
                        LightManager.renderers.get(0).render((Light) node, wnd);
                }
                glViewport(0, 0, wnd.w, wnd.h);

                wnd.update();
                for (IRenderer rndr : RenderManager.renderers)
                    for (Node node : NodeManager.iterate())
                        if (node instanceof Spatial)
                            rndr.render((Spatial) node, wnd);

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
        SceneManager.add("default", new Node());
        SceneManager.setCurrScene("default");
        NodeRegistryManager.add("Light", Light.class);
        NodeRegistryManager.add("Spatial", Spatial.class);
        ComponentRegistryManager.add("CTransform", CTransform.class);
        ComponentRegistryManager.add("CLight", CLight.class);
        RenderManager.renderers.add(new DefaultRenderer());
        LightManager.renderers.add(new LightRenderer());
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        WindowManager.windows.add(new Window(300, 300).init());
        renderThread.start();
        physicsThread.start();
    }

    public static void end() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void addNode(Node node) {
        runAfter(() -> NodeManager.add(node));
    }

    public static void addNodes(Node ... nodes) {
        for(Node node:nodes)
            addNode(node);
    }

    public static void runBefore(Runnable runnable) {
        execBefore.add(runnable);
    }

    public static void runAfter(Runnable runnable) {
        execAfter.add(runnable);
    }
}
