package matyk.engine.data;

import matyk.engine.nodes.Node;

import java.util.ArrayList;

public class NodeItem {
    public NodeItem(Node node) {
        this.node = node;
    }

    public ArrayList<NodeItem> children = new ArrayList<>();
    public Node node;
}
