package matyk.engine.data;

public class Material {
    public Shader shader;
    public Texture[] texture;

    public Material init() {
        shader = new Shader().init("vert.glsl", "frag.glsl", null);
        return this;
    }
}
