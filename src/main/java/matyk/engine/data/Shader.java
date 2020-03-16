package matyk.engine.data;

import matyk.engine.components.CLight;
import matyk.engine.components.CTransform;
import matyk.engine.nodes.Light;
import matyk.engine.utils.StringLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    public int PID;

    public Shader init(String VSF, String FSF, String GSF) {
        String VF = StringLoader.loadResourceAsString(VSF);
        String FF = StringLoader.loadResourceAsString(FSF);
        String GF = null;
        if(GSF != null) {
            GF = StringLoader.loadResourceAsString(GSF);
        }


        FF = FF.replace("maxpointlights", Integer.toString(MAX_POINT_LIGHTS));

        PID = GL20.glCreateProgram();

        int VID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(VID, VF);
        GL20.glCompileShader(VID);

        if (GL20.glGetShaderi(VID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(VID));
            return this;
        }

        int FID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(FID, FF);
        GL20.glCompileShader(FID);

        if (GL20.glGetShaderi(FID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(FID));
            return this;
        }
        if(GSF != null) {
            int GID = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);

            GL20.glShaderSource(GID, GF);
            GL20.glCompileShader(GID);

            if (GL20.glGetShaderi(GID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(GID));
                return this;
            }
            GL20.glAttachShader(PID, GID);
        }

        GL20.glAttachShader(PID, VID);
        GL20.glAttachShader(PID, FID);

        GL20.glLinkProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(PID));
            return this;
        }

        GL20.glValidateProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(PID));
            return this;
        }
        return this;
    }

    public void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(getUniformLocation(PID, name), false, matrixB);
    }

    public void setUniform(String name, Vector4f vector) {
        glUniform4fv(getUniformLocation(PID, name), new float[] {vector.x, vector.y, vector.z, vector.w});
    }

    public void setUniform(String name, Vector3f vector) {
        glUniform3fv(getUniformLocation(PID, name), new float[] {vector.x, vector.y, vector.z});
    }

    public void setUniform(String name, Vector2f vector) {
        glUniform2fv(getUniformLocation(PID, name), new float[] {vector.x, vector.y});
    }

    public void setUniform(String name, float f) {
        glUniform1f(getUniformLocation(PID, name), f);
    }

    public int MAX_POINT_LIGHTS = 5;

    private ArrayList<Light> lights = new ArrayList<>();

    public void loadUniforms(ArrayList<Light> lights) {
        this.lights = lights;
    }

    public void loadUniforms() {
        if(MAX_POINT_LIGHTS != 0)
            loadPointLights(lights);
    }

    private void setUniform(String name, int in) {
        glUniform1i(getUniformLocation(PID,  name), in);
    }

    private static int getUniformLocation(int PID, String name) {
        int location = glGetUniformLocation(PID, name);
        if(location == -1)  System.err.println("Uniform variable " + name + " is not loaded");
        return location;
    }

    private void loadPointLights(ArrayList<Light> pointLights) {
        int count = Math.min(pointLights.size(), MAX_POINT_LIGHTS);
        setUniform("pointLightCount", count);
        int i = 0;
        for(Light light : pointLights) {
            Color color = light.getComponent(CLight.class).albedo;
            setUniform("pointLights[" + i + "].pos", light.getComponent(CTransform.class).pos);
            setUniform("pointLights[" + i + "].color", new Vector3f(color.r, color.g, color.b));
            if(++i >= count)
                break;
        }
    }
}
