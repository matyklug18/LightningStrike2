package matyk.engine.managers;

import matyk.engine.nodes.Node;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public static Stream<Node> iterateStream() {
        return _itr_str(root);
    }

    private static Stream<Node> _itr_str(Node node) {
        return Stream.concat(Stream.of(node), node.getChildren().stream().flatMap(NodeManager::_itr_str));
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
