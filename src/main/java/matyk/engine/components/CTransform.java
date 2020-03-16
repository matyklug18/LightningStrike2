package matyk.engine.components;

import matyk.engine.Component;
import org.joml.Vector3f;

public class CTransform extends Component {
    public Vector3f pos = new Vector3f(), rot = new Vector3f(), scale = new Vector3f(1f);

    public Vector3f getPos() {
        Vector3f pos0 = new Vector3f();
        pos.get(pos0);
        return pos0;
    }

    public void set(float posX, float posY, float posZ,
                    float rotX, float rotY, float rotZ,
                    float sclX, float sclY, float sclZ) {
        pos.set(posX, posY, posZ);
        rot.set(rotX, rotY, rotZ);
        scale.set(sclX, sclY, sclZ);
    }
}
