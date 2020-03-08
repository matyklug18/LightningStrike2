package matyk.engine.nodes;

import matyk.engine.components.CLight;
import matyk.engine.components.CMaterial;
import matyk.engine.data.Material;
import matyk.engine.managers.NodeManager;

import java.util.ArrayList;

public class Light extends Point {

    public Light() {
        components.add(new CLight());
    }

    @Override
    public Node get() {

        ArrayList<Spatial> spatials = new ArrayList<>();
        ArrayList<Light> lights = new ArrayList<>();

        for(Node nd:NodeManager.iterate()) {
            if(nd instanceof Spatial)
                spatials.add((Spatial) nd);
            if(nd instanceof Light)
                lights.add((Light) nd);
        }

        lights.add(this);

        for(Spatial spatial:spatials) {
            spatial.getComponent(CMaterial.class).material.shader.loadUniforms(lights);
        }

        return this;
    }
}
