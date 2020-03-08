package matyk.engine.nodes;

import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;

public class Spatial extends Point {
    public Spatial() {
        components.add(new CMaterial());
        components.add(new CMesh());
    }
}
