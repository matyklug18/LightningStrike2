package matyk.engine.nodes;

import matyk.engine.Component;

import java.util.ArrayList;

public class Node {
    protected ArrayList<Component> components = new ArrayList<>();

    public Component getComponent(Class<?> comp) {
        for(Component c:components)
            if(c.getClass().equals(comp))
                return c;
        return null;
    }


}
