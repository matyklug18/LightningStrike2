package matyk.game;

import matyk.engine.Engine;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.data.NodeItem;
import matyk.engine.managers.NodeManager;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.OBJLoader;
import org.joml.Vector3f;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Engine.start();
        Spatial spatial = new Spatial();
        Engine.runLater(() -> {
            try {
                ((CMesh) spatial.getComponent(CMesh.class)).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                ((CMaterial) spatial.getComponent(CMaterial.class)).material = new Material().init();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        ((CTransform) spatial.getComponent(CTransform.class)).pos = new Vector3f(0, -5, -10);
        ((CTransform) spatial.getComponent(CTransform.class)).rot = new Vector3f(0, 0, 0);
        ((CTransform) spatial.getComponent(CTransform.class)).scale = new Vector3f(1, 1, 1);
        NodeManager.root.children.add(new NodeItem(spatial));
    }
}
