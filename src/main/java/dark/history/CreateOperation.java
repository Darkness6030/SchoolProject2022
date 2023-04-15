package dark.history;

import arc.scene.style.Drawable;
import dark.editor.Layer;
import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.*;

public class CreateOperation implements Operation {

    public Layer layer;
    public int index;

    public CreateOperation(Layer layer, int index) {
        this.layer = layer;
        this.index = index;
    }

    public void undo() {
        editor.renderer.removeLayer(layer, false);
        ui.hudFragment.updateLayers();
    }

    public void redo() {
        editor.renderer.layers.insert(index, layer);
        ui.hudFragment.updateLayers();
    }

    public Drawable icon() {
        return Icons.plus;
    }

    public String name() {
        return "@history.add";
    }

    public String desc() {
        return bundle.format("history.layer", layer.name);
    }
}
