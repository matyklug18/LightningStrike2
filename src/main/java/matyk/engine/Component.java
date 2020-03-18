package matyk.engine;

import org.json.simple.JSONObject;

public abstract class Component {
    public abstract void fromJSON(JSONObject json);

    public abstract JSONObject toJSON();
}
