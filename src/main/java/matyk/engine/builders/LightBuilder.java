package matyk.engine.builders;

import matyk.engine.components.CLight;
import matyk.engine.components.CTransform;
import matyk.engine.data.Color;
import matyk.engine.nodes.Light;
import matyk.engine.utils.VectorUtils;
import org.joml.Vector3f;

public class LightBuilder {

    private Light light;

    public LightBuilder(Light light) {
        this.light = light;
    }

    public LightBuilder setColor(Color color) {
        this.light.getComponent(CLight.class).albedo = color;
        return this;
    }

    public LightBuilder setStrength(float strength) {
        this.light.getComponent(CLight.class).strength = strength;
        return this;
    }

    public LightBuilder setPos(Vector3f pos) {
        this.light.getComponent(CTransform.class).pos = pos;
        return this;
    }

    public Light build() {
        return light;
    }
}
