package matyk.engine.data;

import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    long winID;
    public int h, w;

    public Window(int h, int w) {
        this.h = h;
        this.w = w;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(winID);
    }

    public Window init() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        winID = glfwCreateWindow(w, h, "Hello World!", NULL, NULL);
        if (winID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(winID, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(winID, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    winID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwSetWindowSizeCallback(winID, new GLFWWindowSizeCallback() {
            public void invoke(long window, int w0, int h0) {
                glViewport(0, 0, w0, h0);
                w = w0;
                h = h0;
            }
        });

        glfwMakeContextCurrent(winID);
        glfwSwapInterval(1);

        glfwShowWindow(winID);

        createCapabilities();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);

        return this;
    }

    public void swapBuffers() {
        glfwSwapBuffers(winID);
    }

    public void end() {
        glfwFreeCallbacks(winID);
        glfwDestroyWindow(winID);
    }

    public void update() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glfwPollEvents();
    }
}
