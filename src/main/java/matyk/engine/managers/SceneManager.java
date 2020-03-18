package matyk.engine.managers;

import matyk.engine.components.CTransform;
import matyk.engine.nodes.Node;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lwjgl.system.CallbackI;

import javax.swing.text.html.HTMLDocument;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

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
            Node scene = iter(rootObj);
            scenes.replace("default", scene);
            currScene = scene;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Node iter(JSONObject obj) {
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
            if(key.contains("NODE")) {
                node.add(iter((JSONObject) obj.get(key)));
            }
            else if(key.contains("CHILDREN")) {
                node.add(iter((JSONObject) obj.get(key)));
                System.out.println(node);
            }
            else if(key.contains("COMPONENTS")) {
                JSONObject obj0 = ((JSONObject)obj.get(key));
                for(Iterator iterator0 = obj0.keySet().iterator(); iterator0.hasNext();) {
                    String key0 = (String) iterator0.next();
                    node.getComponent(ComponentRegistryManager.get(key0)).fromJSON((JSONObject) obj0.get(key0));
                }
            }
        }
        return node;
    }
}
