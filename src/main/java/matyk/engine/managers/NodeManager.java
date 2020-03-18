package matyk.engine.managers;

import matyk.engine.nodes.Node;

import java.util.ArrayList;

public class NodeManager {

    private static ArrayList<Node> nodesArr = new ArrayList<>();

    public static ArrayList<Node> iterate() {
        nodesArr.clear();
        if(SceneManager.getCurrScene().getChildren() != null)
            for(Node itm:SceneManager.getCurrScene().getChildren()) {
                nodesArr.add(itm);
                if(itm.getChildren() != null)
                    iterateOnNode(itm.getChildren());
            }
        return nodesArr;
    }

    private static void iterateOnNode(ArrayList<Node> nodes) {
        for(Node itm:nodes) {
            nodesArr.add(itm);
            if(itm.getChildren() != null)
                iterateOnNode(itm.getChildren());
        }
    }

    public static Node get(int... ints) {
        return SceneManager.getCurrScene().get(ints);
    }

    public static void add(Node node) {
        SceneManager.getCurrScene().add(node);
    }
}
