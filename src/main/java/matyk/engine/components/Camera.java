package matyk.engine.components;

import matyk.engine.data.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

public class Camera {
    public Vector3f position = new Vector3f(), rotation = new Vector3f();

    private double lx, ly;

    public float hspeed = 5.0f, vspeed = 5.0f, rspeed = 10.0f;

    public void move(Window window, float delta) {

        float dx, dy;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer px = stack.mallocDouble(1);
            DoubleBuffer py = stack.mallocDouble(1);

            GLFW.glfwGetCursorPos(window.winID, px, py);

            double x = px.get(), y = py.get();

            dx = (float) ((x - lx) * delta);
            dy = (float) ((y - ly) * delta);

            lx = x;
            ly = y;

        }

        if(GLFW.glfwGetMouseButton(window.winID, GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_RELEASE) {
            rotation.x += dy * rspeed;
            rotation.y += dx * rspeed;

            rotation.x = clamp(-89.9f, rotation.x, 89.9f);
        }

        Vector2f vel = new Vector2f();
        if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_W) != GLFW.GLFW_RELEASE) {
            vel.x -= 1f;
        } if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_S) != GLFW.GLFW_RELEASE) {
            vel.x += 1f;
        } if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_A) != GLFW.GLFW_RELEASE) {
            vel.y += 1f;
        } if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_D) != GLFW.GLFW_RELEASE) {
            vel.y -= 1f;
        } if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_SPACE) != GLFW.GLFW_RELEASE) {
            position.y += vspeed * delta;
        } if(GLFW.glfwGetKey(window.winID, GLFW.GLFW_KEY_LEFT_SHIFT) != GLFW.GLFW_RELEASE) {
            position.y -= vspeed * delta;
        }

        if(vel.x == 0.0f && vel.y == 0.0f) {
            return;
        }

        vel = rotate(vel, 90.0f + rotation.y).normalize(hspeed * delta);
        position.x += vel.x;
        position.z += vel.y;
    }

    private float clamp(float min, float x, float max) {
        if(x <= min) return min;
        else return Math.min(x, max);
    }

    private Vector2f rotate(Vector2f vec2, float angle) {
        angle *= Math.PI / 180.0f;
        return new Vector2f(
                (float) (vec2.x * Math.cos(angle) - vec2.y * Math.sin(angle)),
                (float) (vec2.x * Math.sin(angle) + vec2.y * Math.cos(angle))
        );
    }
}
