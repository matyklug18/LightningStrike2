package matyk.engine.components;

import matyk.engine.Component;
import matyk.engine.data.Material;
import org.json.simple.JSONObject;

public class CMaterial extends Component {
    public Material material;

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public void fromJSON(JSONObject json) {

    }
}
