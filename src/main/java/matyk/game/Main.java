package matyk.game;

import matyk.engine.Engine;
import matyk.engine.components.CLight;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Color;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.managers.NodeManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.OBJLoader;
import org.joml.Vector3f;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Engine.start();
        Spatial spatial0 = new Spatial();
        Spatial spatial1 = new Spatial();
        Engine.runBefore(() -> {
            try {
                spatial0.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                spatial1.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
            } catch(IOException e) {
                e.printStackTrace();
            }
            spatial0.getComponent(CMaterial.class).material = new Material().init();
            spatial1.getComponent(CMaterial.class).material = new Material().init();
        });
        spatial0.getComponent(CTransform.class).pos = new Vector3f(0, -5, -20);
        spatial0.getComponent(CTransform.class).rot = new Vector3f(0, 0, 0);
        spatial0.getComponent(CTransform.class).scale = new Vector3f(10, 1, 10);

        spatial1.getComponent(CTransform.class).pos = new Vector3f(0, 0, -20);
        spatial1.getComponent(CTransform.class).rot = new Vector3f(0, 0, 0);
        spatial1.getComponent(CTransform.class).scale = new Vector3f(1, 1, 1);

        Light light = new Light();
        light.getComponent(CLight.class).albedo = new Color(0,1,0);
        light.getComponent(CTransform.class).pos = new Vector3f(0, 5, -20);

        Engine.runAfter(() -> {
            NodeManager.root.add(spatial0);
            NodeManager.root.add(spatial1);
            NodeManager.root.add(light);
        });

    }
}
