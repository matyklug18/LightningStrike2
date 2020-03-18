package matyk.engine.nodes;

import matyk.engine.builders.SpatialBuilder;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.managers.NodeManager;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

public class Spatial extends Point {

    private SpatialBuilder builder = new SpatialBuilder(this);

    public Spatial() {
        components.add(new CMaterial());
        components.add(new CMesh());
    }

    public SpatialBuilder getBuilder() {
        return builder;
    }

    @Override
    protected Node get() {
        ArrayList<Spatial> spatials = new ArrayList<>();
        ArrayList<Light> lights = new ArrayList<>();

        for(Node nd: NodeManager.iterate()) {
            if(nd instanceof Spatial)
                spatials.add((Spatial) nd);
            if(nd instanceof Light)
                lights.add((Light) nd);
        }

        spatials.add(this);

        for(Spatial spatial:spatials) {
            spatial.getComponent(CMaterial.class).material.shader.loadUniforms(lights);
        }

        return this;
    }
}
