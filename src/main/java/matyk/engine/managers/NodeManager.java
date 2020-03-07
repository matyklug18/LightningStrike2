package matyk.engine.managers;

import matyk.engine.data.NodeItem;
import matyk.engine.nodes.Node;

import java.util.ArrayList;

public class NodeManager {
    public static NodeItem root = new NodeItem(null);
    private static ArrayList<Node> nodesArr = new ArrayList<>();

    public static ArrayList<Node> iterate() {
        if(root.children != null)
            for(NodeItem itm:root.children) {
                nodesArr.add(itm.node);
                if(itm.children != null)
                    iterateOnNode(itm.children);
            }
        return nodesArr;
    }

    private static void iterateOnNode(ArrayList<NodeItem> nodes) {
        for(NodeItem itm:nodes) {
            nodesArr.add(itm.node);
            if(itm.children != null)
                iterateOnNode(itm.children);
        }
    }
}
