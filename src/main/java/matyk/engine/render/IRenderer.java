package matyk.engine.render;

import matyk.engine.data.Window;
import matyk.engine.nodes.Spatial;

public interface IRenderer {
    void render(Spatial spatial, Window wnd);
}
