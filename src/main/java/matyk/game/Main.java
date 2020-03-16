package matyk.game;

import matyk.engine.Engine;
import matyk.engine.components.*;
import matyk.engine.data.*;
import matyk.engine.managers.LightManager;
import matyk.engine.managers.NodeManager;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Spatial;
import matyk.engine.render.DefaultRenderer;
import matyk.engine.utils.OBJLoader;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL13;

import java.io.IOException;

public class Main {

    public static Spatial spatial0 = new Spatial(), spatial1 = new Spatial(), debug = new Spatial();

    public static void main(String[] args) {
        Engine.start();


        Engine.runBefore(() -> {
            try {
                spatial0.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                spatial1.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                debug.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
            } catch(IOException e) {
                e.printStackTrace();
            }
            spatial0.getComponent(CMaterial.class).material = new Material().init();
            spatial1.getComponent(CMaterial.class).material = new Material().init();
            debug.getComponent(CMaterial.class).material = new Material();
            debug.getComponent(CMaterial.class).material.shader = new Shader().init("debug_vert.glsl", "debug_frag.glsl", null);
        });

        spatial0.getComponent(CTransform.class).set(
                0, 0, 0,
                0, 0, 0,
                10, 0.2f, 10
        );

        spatial1.getComponent(CTransform.class).set(
                0, 5, 0,
                0, 0, 0,
                1, 1, 1
        );

        debug.getComponent(CTransform.class).set(
                0, 10, -5,
                0, 0, 0,
                1, 1, 1
        );

        Light light = new Light();
        light.getComponent(CLight.class).albedo = new Color(0,1,0);
        light.getComponent(CTransform.class).pos = Engine.lightCamera.position.set(0, 10, 0);

        Engine.runAfter(() -> {
            NodeManager.root.add(spatial0);
            NodeManager.root.add(spatial1);
            NodeManager.root.add(debug);
            NodeManager.root.add(light);
        });

    }
}
