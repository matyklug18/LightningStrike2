package matyk.engine.builders;

import de.javagl.obj.Obj;
import matyk.engine.Engine;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.MatrixUtils;
import matyk.engine.utils.OBJLoader;
import org.joml.Vector3f;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

public class SpatialBuilder {

    Spatial spatial;

    public SpatialBuilder(Spatial spatial) {
        this.spatial = spatial;
    }
    public SpatialBuilder setPos(Vector3f pos) {
        this.spatial.getComponent(CTransform.class).pos = pos;
        return this;
    }

    public SpatialBuilder setRot(Vector3f rot) {
        this.spatial.getComponent(CTransform.class).rot = rot;
        return this;
    }

    public SpatialBuilder setScale(Vector3f scale) {
        this.spatial.getComponent(CTransform.class).scale = scale;
        return this;
    }

    public SpatialBuilder setMat(Material mat) {
        this.spatial.getComponent(CMaterial.class).material = mat;
        return this;
    }

    public SpatialBuilder setMesh(Mesh mesh) {
        this.spatial.getComponent(CMesh.class).mesh = mesh;
        return this;
    }

    public SpatialBuilder initMesh(String path) {
        Engine.runBefore(() -> {
            try {
                spatial.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public SpatialBuilder initMat(Material mat) {
        Engine.runBefore(() -> {
            spatial.getComponent(CMaterial.class).material = mat.init();
        });
        return this;
    }

    public Spatial build() {
        return spatial;
    }

    public SpatialBuilder defaultRot() {
        this.spatial.getComponent(CTransform.class).rot = new Vector3f(0,0,0);
        return this;
    }

    public SpatialBuilder defaultScale() {
        this.spatial.getComponent(CTransform.class).scale = new Vector3f(1,1,1);
        return this;
    }
}
