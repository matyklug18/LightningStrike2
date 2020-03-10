package matyk.engine.render;

import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Mesh;
import matyk.engine.data.Shader;
import matyk.engine.data.Window;
import matyk.engine.managers.NodeManager;
import matyk.engine.managers.WindowManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.MatrixUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.system.MemoryUtil.*;

public class LightRenderer {
    public void render(Light light, Window wnd) {
        int depthCubemap = glGenTextures();
        int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;
        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubemap);
        for (int i = 0; i < 6; ++i)
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_DEPTH_COMPONENT,
                SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        int depthMapFBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthCubemap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glClear(GL_DEPTH_BUFFER_BIT);
        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubemap);

        float aspect = (float)SHADOW_WIDTH/(float)SHADOW_HEIGHT;
        float near = 1.0f;
        float far = 25.0f;
        Matrix4f shadowProj = MatrixUtils.projectionMatrix(90.0f, aspect, near, far);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        Matrix4f a = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f( 1.0f,  0.0f,  0.0f)), new Vector3f(0.0f,-1.0f, 0.0f));
        Matrix4f b = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f(-1.0f,  0.0f,  0.0f)), new Vector3f(0.0f,-1.0f, 0.0f));
        Matrix4f c = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f( 0.0f,  1.0f,  0.0f)), new Vector3f(0.0f,-1.0f, 0.0f));
        Matrix4f d = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f( 1.0f, -1.0f,  0.0f)), new Vector3f(0.0f,-1.0f, 0.0f));
        Matrix4f e = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f( 1.0f,  0.0f,  1.0f)), new Vector3f(0.0f,-1.0f, 0.0f));
        Matrix4f f = shadowProj.lookAt(light.getComponent(CTransform.class).pos, light.getComponent(CTransform.class).pos.add(new Vector3f( 1.0f,  0.0f, -1.0f)), new Vector3f(0.0f,-1.0f, 0.0f));

        Matrix4f[] matrices = new Matrix4f[]{
               a,b,c,d,e,f
        };

        Shader shader = new Shader().init("vertLight.glsl", "fragLight.glsl", "geomLight.glsl");

        for (Node node : NodeManager.iterate())
            if (node instanceof Spatial)
                renderObj((Spatial) node, shader, matrices);
    }

    private void renderObj(Spatial node, Shader shader, Matrix4f... mats) {

        Mesh msh = node.getComponent(CMesh.class).mesh;

        glBindVertexArray(msh.vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, msh.ibo);

        glUseProgram(shader.PID);

        CTransform trans = node.getComponent(CTransform.class);
        shader.setUniform("model", MatrixUtils.transformationMatrix(trans.pos, trans.rot, trans.scale));
        shader.setUniform("shadowMatrices[0]", mats[0]);
        shader.setUniform("shadowMatrices[1]", mats[1]);
        shader.setUniform("shadowMatrices[2]", mats[2]);
        shader.setUniform("shadowMatrices[3]", mats[3]);
        shader.setUniform("shadowMatrices[4]", mats[4]);
        shader.setUniform("shadowMatrices[5]", mats[5]);

        glDrawElements(GL11.GL_TRIANGLES, msh.indsCount, GL_UNSIGNED_INT, 0);

        glUseProgram(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }
}
