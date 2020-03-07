package matyk.engine.nodes;

import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;

public class Spatial extends Node {
    public Spatial() {
        components.add(new CMaterial());
        components.add(new CMesh());
        components.add(new CTransform());
    }
}
