package matyk.engine.components;

import matyk.engine.Component;
import org.joml.Vector3f;
import org.json.simple.JSONObject;

public class CTransform extends Component {
    public Vector3f pos = new Vector3f(), rot = new Vector3f(), scale = new Vector3f();

    public Vector3f getPos() {
        Vector3f pos0 = new Vector3f();
        pos.get(pos0);
        return pos0;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public void fromJSON(JSONObject json) {

        if(json.containsKey("pos")) {
            JSONObject jpos = (JSONObject) json.get("pos");
            pos = new Vector3f(
                    (float) ((Number) jpos.get("x")).doubleValue(),
                    (float) ((Number) jpos.get("y")).doubleValue(),
                    (float) ((Number) jpos.get("z")).doubleValue()
            );
        } else {
            pos = new Vector3f(1,1,1);
        }

        if(json.containsKey("scale")) {
            JSONObject jscale = (JSONObject) json.get("scale");
            scale = new Vector3f(
                    (float) ((Number) jscale.get("x")).doubleValue(),
                    (float) ((Number) jscale.get("y")).doubleValue(),
                    (float) ((Number) jscale.get("z")).doubleValue()
            );
        } else {
            scale = new Vector3f(1,1,1);
        }
    }
}
