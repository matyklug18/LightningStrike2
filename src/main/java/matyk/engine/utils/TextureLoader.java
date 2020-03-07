package matyk.engine.utils;

import org.apache.commons.io.IOUtils;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class TextureLoader {
    public static int loadTexture(String fileName) throws Exception {
        int width;
        int height;
        ByteBuffer buf;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            InputStream inputStream = TextureLoader.class.getResourceAsStream("/" + fileName);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            ByteBuffer buffer = memAlloc(bytes.length+1);
            buffer.put(bytes);
            buffer.put((byte) 0);
            buffer.flip();
            buf = stbi_load_from_memory(buffer, w, h, channels, 4);
            memFree(buffer);

            if (buf == null) {
                throw new Exception("Image file [" + fileName + "] not loaded: " + stbi_failure_reason());
            }
            /* Get width and height of image */
            width = w.get();
            height = h.get();
        }

        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);
        //glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);
        stbi_image_free(buf);
        return textureId;
    }
}
