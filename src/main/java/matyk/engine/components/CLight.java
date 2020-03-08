package matyk.engine.components;

import matyk.engine.Component;
import matyk.engine.data.Color;

public class CLight extends Component {

    public CLight() {
        albedo = new Color();
        albedo.r *= strength;
        albedo.g *= strength;
        albedo.b *= strength;
    }

    public Color albedo;
    public float strength = 1;

}
