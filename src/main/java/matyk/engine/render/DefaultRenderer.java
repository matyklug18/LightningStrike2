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
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class DefaultRenderer implements IRenderer {

    public static RenderOverride override;

    @Override
    public void render(Spatial spatial, Window wnd) {
        Mesh msh = spatial.getComponent(CMesh.class).mesh;
        Shader shader = spatial.getComponent(CMaterial.class).material.shader;
        CTransform trans = spatial.getComponent(CTransform.class);

        glBindVertexArray(msh.vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, msh.ibo);

        glUseProgram(shader.PID);

        shader.setUniform("transform", MatrixUtils.transformationMatrix(trans.pos, trans.rot, trans.scale));
        shader.setUniform("project", MatrixUtils.projectionMatrix(70, (float) wnd.w / (float) wnd.h, 0.1f, 100));
        shader.setUniform("view", MatrixUtils.viewMatrix(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0)));
        shader.setUniform("far_plane", 100.f);

        shader.loadUniforms();
        shader.setUniform("iRes", new Vector2f(wnd.h, wnd.w));

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
