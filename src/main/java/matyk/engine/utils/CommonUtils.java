package matyk.engine.utils;

import java.util.stream.Stream;

public class CommonUtils {

    public static <S, D> Stream<D> cast(Stream<S> source, Class<D> cast) {
        return source.flatMap(s -> cast.isInstance(s)? Stream.of((D) s) : Stream.empty());
    }

}
