package matyk.engine.render;

import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.data.Shader;
import matyk.engine.data.Window;
import matyk.engine.managers.WindowManager;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.MatrixUtils;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class DefaultRenderer implements IRenderer {
    @Override
    public void render(Spatial spatial, Window wnd) {
        Mesh msh = ((CMesh) spatial.getComponent(CMesh.class)).mesh;
        Shader shader = ((CMaterial) spatial.getComponent(CMaterial.class)).material.shader;
        CTransform trans = (CTransform) spatial.getComponent(CTransform.class);

        glBindVertexArray(msh.vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, msh.ibo);

        glUseProgram(shader.PID);

        shader.setUniform("transform", MatrixUtils.transformationMatrix(trans.pos, trans.rot, trans.scale));
        shader.setUniform("project", MatrixUtils.projectionMatrix(70, (float) wnd.w/(float) wnd.h, 0.1f, 100));
        shader.setUniform("view", MatrixUtils.viewMatrix(new Vector3f(0,0,0), new Vector3f(0,0,0)));

        glEnable(GL_FRAMEBUFFER_SRGB);
        glDrawElements(GL11.GL_TRIANGLES, msh.indsCount, GL_UNSIGNED_INT, 0);

        glUseProgram(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }
}
