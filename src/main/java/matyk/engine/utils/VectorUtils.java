package matyk.engine.utils;

import org.joml.Vector3f;

public class VectorUtils {
    public static Vector3f add(Vector3f a, Vector3f b) {
        a.add(b);
        return a;
    }
}
