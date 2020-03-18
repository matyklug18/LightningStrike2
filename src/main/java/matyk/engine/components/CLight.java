package matyk.engine.components;

import matyk.engine.Component;
import matyk.engine.data.Color;
import org.json.simple.JSONObject;

public class CLight extends Component {

    public CLight() {
        albedo = new Color();
        albedo.r *= strength;
        albedo.g *= strength;
        albedo.b *= strength;
    }

    public Color albedo;
    public float strength = 1;

    @Override
    public void fromJSON(JSONObject json) {
        albedo.r = (long) json.get("r");
        albedo.g = (long) json.get("g");
        albedo.b = (long) json.get("b");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("r", albedo.r);
        obj.put("g", albedo.g);
        obj.put("b", albedo.b);
        return obj;
    }
}
