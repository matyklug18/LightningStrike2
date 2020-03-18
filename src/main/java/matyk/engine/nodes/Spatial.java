package matyk.engine.nodes;

import matyk.engine.builders.SpatialBuilder;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import org.lwjgl.system.CallbackI;

public class Spatial extends Point {

    private SpatialBuilder builder = new SpatialBuilder(this);

    public Spatial() {
        components.add(new CMaterial());
        components.add(new CMesh());
    }

    public SpatialBuilder getBuilder() {
        return builder;
    }
}
