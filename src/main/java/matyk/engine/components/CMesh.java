package matyk.engine.components;

import matyk.engine.Component;
import matyk.engine.data.Mesh;
import org.json.simple.JSONObject;

public class CMesh extends Component {
    public Mesh mesh;

    @Override
    public JSONObject toJSON() {
        return null;
    }

    @Override
    public void fromJSON(JSONObject json) {

    }
}
