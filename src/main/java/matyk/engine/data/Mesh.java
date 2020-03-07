package matyk.engine.data;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    int[] inds;
    Vertex[] verts;
    public int vao, pbo, ibo, tbo, nbo;
    public int indsCount;

    public Mesh init(Obj model) {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(model.getNumVertices() * 3);
        float[] positionData = new float[model.getNumVertices()* 3];
        for (int i = 0; i < model.getNumVertices(); i++) {
            positionData[i * 3] = model.getVertex(i).getX();
            positionData[i * 3 + 1] = model.getVertex(i).getY();
            positionData[i * 3 + 2] = model.getVertex(i).getZ();
        }
        positionBuffer.put(positionData).flip();

        pbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, pbo);
        GL15.glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ArrayList<Integer> inds = new ArrayList<>();

        for (int i = 0; i < model.getNumFaces(); i++) {
            for (int j = 0; j < model.getFace(i).getNumVertices(); j++) {
                inds.add(model.getFace(i).getVertexIndex(j));
                indsCount++;
            }
        }

        IntBuffer indsBuffer = MemoryUtil.memAllocInt(indsCount);
        indsBuffer.put(inds.stream().mapToInt(i -> i).toArray());
        indsBuffer.flip();

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indsBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        FloatBuffer textCoordsBuffer = MemoryUtil.memAllocFloat(model.getNumTexCoords()*2);

        textCoordsBuffer.put(ObjData.getTexCoordsArray(model, 2));

        textCoordsBuffer.flip();

        tbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        FloatBuffer normalBuffer = ObjData.getNormals(model);
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return this;
    }
}
