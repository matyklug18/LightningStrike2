package matyk.engine.managers;

import matyk.engine.nodes.Node;

import java.util.HashMap;

public class NodeRegistryManager {
    private static HashMap<String, Class<? extends Node>> registry = new HashMap<>();

    public static void add(String name, Class<? extends Node> node) {
        registry.put(name, node);
    }

    public static Class<? extends Node> get(String name) {
        return registry.get(name);
    }
}
