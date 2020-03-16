package matyk.engine.render;

import matyk.engine.Engine;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.data.Shader;
import matyk.engine.data.Window;
import matyk.engine.managers.NodeManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.MatrixUtils;
import matyk.engine.utils.OBJLoader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.system.MemoryUtil.*;

public class LightRenderer {

    int depthCubemap, depthMapFBO;
    int SHADOW_WIDTH = 1024, SHADOW_HEIGHT = 1024;
    Shader shader;

    public void init() {
        depthCubemap = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubemap);
        for (int i = 0; i < 6; ++i)
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_DEPTH_COMPONENT,
                    SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        depthMapFBO = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthCubemap, 0);
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        shader = new Shader().init("vertLight.glsl", "fragLight.glsl", "geomLight.glsl");
    }

    public void render(Light light, Window wnd) {
        glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glBindTexture(GL_TEXTURE_CUBE_MAP, depthCubemap);
        glClear(GL_DEPTH_BUFFER_BIT);

        float aspect = (float)SHADOW_WIDTH/(float)SHADOW_HEIGHT;
        float near = 1f;
        float far = 25f;

/*
        Matrix4f a = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f( 1.0f,  0.0f,  0.0f)), new Vector3f(0.0f,-1.0f,  0.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
        Matrix4f b = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f(-1.0f,  0.0f,  0.0f)), new Vector3f(0.0f,-1.0f,  0.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
        Matrix4f c = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f( 0.0f,  1.0f,  0.0f)), new Vector3f(0.0f, 0.0f,  1.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
        Matrix4f d = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f( 0.0f, -1.0f,  0.0f)), new Vector3f(0.0f, 0.0f, -1.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
        Matrix4f e = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f( 0.0f,  0.0f,  1.0f)), new Vector3f(0.0f,-1.0f,  0.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
        Matrix4f f = new Matrix4f().lookAt(light.getComponent(CTransform.class).getPos(), light.getComponent(CTransform.class).getPos().add(new Vector3f( 0.0f,  0.0f, -1.0f)), new Vector3f(0.0f,-1.0f,  0.0f)).setPerspective((float) Math.toRadians(90.0f), aspect, near, far);
*/
        Matrix4f shadowPerspective = MatrixUtils.projectionMatrix(90.0f, aspect, near, far);

        Matrix4f[] matrices = {
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f( 1, 0, 0).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, -1, 0)),
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f(-1, 0, 0).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, -1, 0)),
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f( 0, 1, 0).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, 0, 1)),
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f( 0,-1, 0).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, 0, -1)),
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f( 0, 0, 1).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, -1, 0)),
                new Matrix4f(shadowPerspective).lookAt(light.getComponent(CTransform.class).getPos(), new Vector3f( 0, 0,-1).add(light.getComponent(CTransform.class).getPos()), new Vector3f(0, -1, 0))
        };

        /*Matrix4f[] matrices = {
                a,b,c,d,e,f,
        };*/

        for (Node node : NodeManager.iterate())
            if (node instanceof Spatial)
                renderObj((Spatial) node, shader, matrices, far, light);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void renderObj(Spatial node, Shader shader, Matrix4f[] mats, float far, Light light) {
        Mesh msh = node.getComponent(CMesh.class).mesh;

        glBindVertexArray(msh.vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, msh.ibo);

        glUseProgram(shader.PID);

        CTransform trans = node.getComponent(CTransform.class);

        shader.setUniform("model", MatrixUtils.transformationMatrix(trans.pos, trans.rot, trans.scale));

        for(int i = 0; i < 6; i++)
            shader.setUniform("shadowMatrices["+(i)+"]", mats[i]);

        shader.setUniform("far_plane", far);

        shader.setUniform("lightPos", light.getComponent(CTransform.class).getPos());

        glDrawElements(GL_TRIANGLES, msh.indsCount, GL_UNSIGNED_INT, 0);

        glUseProgram(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }
}
