package matyk.engine.nodes;

import matyk.engine.Component;

import java.util.ArrayList;

public class Node {
    protected ArrayList<Component> components = new ArrayList<>();

    public String name;

    public <C extends Component> C getComponent(Class<C> comp) {
        for (Component c : components)
            if (c.getClass().equals(comp))
                return (C) c;
        return null;
    }

    public boolean hasComponent(Class<? extends Component> comp) {
        return getComponent(comp) != null;
    }

    private ArrayList<Node> children = new ArrayList<>();

    public void add(Node node) {
        children.add(node.get());
    }

    public Node getChild(int id) {
        return children.get(id);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    protected Node get() {
        return this;
    }

    public Node get(int... ints) {
        Node node = this;
        for(int idx : ints) {
            node = node.children.get(idx);
        }
        return node;
    }
}
