package matyk.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class StringLoader {
    public static InputStream loadResourceAsStream(String path) {
        return StringLoader.class.getClassLoader().getResourceAsStream(path);
    }

    public static String loadResourceAsString(String path) {
        return new BufferedReader(new InputStreamReader(loadResourceAsStream(path))).lines().collect(Collectors.joining("\n"));
    }
}
