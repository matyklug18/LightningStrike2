package matyk.engine.managers;

import de.javagl.obj.Obj;
import matyk.engine.Engine;
import matyk.engine.components.CMaterial;
import matyk.engine.components.CMesh;
import matyk.engine.components.CTransform;
import matyk.engine.data.Material;
import matyk.engine.data.Mesh;
import matyk.engine.nodes.Light;
import matyk.engine.nodes.Node;
import matyk.engine.nodes.Spatial;
import matyk.engine.utils.OBJLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.system.CallbackI;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class SceneManager {
    private static HashMap<String, Node> scenes = new HashMap();
    private static Node currScene;

    public static void add(String name, Node scene) {
        scenes.put(name, scene);
    }

    public static void setCurrScene(String name) {
        currScene = scenes.get(name);
    }

    public static Node getCurrScene() {
        return currScene;
    }

    public static void loadFromJSON(String json){
        try {
            JSONObject rootObj = (JSONObject) new JSONParser().parse(json);
            Node scene = iter(rootObj, false);
            scenes.replace("default", scene);
            setCurrScene("default");

            ArrayList<Spatial> spatials = new ArrayList<>();
            ArrayList<Light> lights = new ArrayList<>();

            for(Node nd: NodeManager.iterate()) {
                if(nd instanceof Spatial)
                    spatials.add((Spatial) nd);
                if(nd instanceof Light)
                    lights.add((Light) nd);
            }

            for(Spatial spatial:spatials) {
                spatial.getComponent(CMaterial.class).material.shader.loadUniforms(lights);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private static Node iter(JSONObject obj, boolean inArr) {
        Node node = new Node();

        for(Iterator iterator = obj.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            if(obj.keySet().contains("TYPE") && !key.contains("TYPE")) {
                try {
                    node = NodeRegistryManager.get((String) obj.get("TYPE")).getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if(inArr) {
                if(node instanceof Spatial) {
                    try {
                        node.getComponent(CMesh.class).mesh = new Mesh().init(OBJLoader.load("object.obj"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    node.getComponent(CMaterial.class).material = new Material().init();
                }
            }
            if(key.contains("CHILDREN")) {
                for(Object obj0:(JSONArray) obj.get(key)) {
                    JSONObject obj1 = (JSONObject) obj0;
                    Node node1 = iter(obj1, true);
                    node.add(node1);
                }
            }
            else if(key.contains("COMPONENTS")) {
                JSONObject obj0 = ((JSONObject) obj.get(key));
                for(Iterator iterator0 = obj0.keySet().iterator(); iterator0.hasNext();) {
                    String key0 = (String) iterator0.next();
                    node.getComponent(ComponentRegistryManager.get(key0)).fromJSON((JSONObject) obj0.get(key0));
                }
            }
        }
        return node;
    }
}
