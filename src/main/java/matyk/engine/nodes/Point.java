package matyk.engine.nodes;

import matyk.engine.components.CTransform;

public class Point extends Node {
    public Point() {
        components.add(new CTransform());
    }
}