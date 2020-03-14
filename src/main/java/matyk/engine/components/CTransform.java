package matyk.engine.components;

import matyk.engine.Component;
import org.joml.Vector3f;

public class CTransform extends Component {
    public Vector3f pos, rot, scale;

    public Vector3f getPos() {
        Vector3f pos0 = new Vector3f();
        pos.get(pos0);
        return pos0;
    }
}
