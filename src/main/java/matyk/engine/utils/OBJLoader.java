package matyk.engine.utils;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

import java.io.IOException;
import java.io.InputStream;

public class OBJLoader {
    public static Obj load(String path) throws IOException {
        InputStream objInputStream = OBJLoader.class.getResourceAsStream("/" + path);
        return ObjUtils.convertToRenderable(ObjReader.read(objInputStream));
    }

    public static Obj load(InputStream path) throws IOException {
        return ObjUtils.convertToRenderable(ObjReader.read(path));
    }
}
