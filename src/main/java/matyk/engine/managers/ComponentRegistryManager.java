package matyk.engine.managers;

import matyk.engine.Component;

import java.util.HashMap;

public class ComponentRegistryManager {
    private static HashMap<String, Class<? extends Component>> registry = new HashMap<>();

    public static void add(String name, Class<? extends Component> component) {
        registry.put(name, component);
    }

    public static Class<? extends Component> get(String name) {
        return registry.get(name);
    }
}
