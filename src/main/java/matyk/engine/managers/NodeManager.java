package matyk.engine.managers;

import matyk.engine.nodes.Node;

import java.util.ArrayList;

public class NodeManager {

    public static Node root = new Node();
    private static ArrayList<Node> nodesArr = new ArrayList<>();

    public static ArrayList<Node> iterate() {
        nodesArr.clear();
        if(root.getChildren() != null)
            for(Node itm:root.getChildren()) {
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

    public Node get(int... ints) {
        return root.get(ints);
    }

}
