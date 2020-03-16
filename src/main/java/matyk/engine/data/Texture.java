package matyk.engine.data;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
    private final int target;
    private final int handle;
    private final int slot;

    public Texture(int target, int handle, int slot) {
        this.target = target;
        this.handle = handle;
        this.slot = slot;
    }

    public void useTexture() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + slot);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, handle);
    }
}
