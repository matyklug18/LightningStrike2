package matyk.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixUtils {

    public static Matrix4f transformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        rotation = rotation.mul((float) (Math.PI / 180.0f), new Vector3f());
        Matrix4f matrix = new Matrix4f();
        return matrix.translate(translation).rotateXYZ(rotation).scale(scale);
    }

    public static Matrix4f projectionMatrix(float fov, float aspectRatio, float zNear, float zFar) {
        return new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar);
    }

    public static Matrix4f viewMatrix(Vector3f position, Vector3f rotation) {
        rotation = rotation.mul((float) (Math.PI / 180.0f), new Vector3f());
        return new Matrix4f().rotateXYZ(rotation).translate(position.negate(new Vector3f()));
    }

}