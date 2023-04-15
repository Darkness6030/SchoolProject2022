package dark.history;

import arc.scene.style.Drawable;
import dark.editor.Layer;
import dark.ui.Icons;

import static arc.Core.bundle;
import static dark.Main.*;

public class RemoveOperation implements Operation {

    public Layer layer;
    public int index;

    public RemoveOperation(Layer layer, int index) {
        this.layer = layer;
        this.index = index;
    }

    public void undo() {
        editor.renderer.layers.insert(index, layer);
        ui.hudFragment.updateLayers();
    }

    public void redo() {
        editor.renderer.removeLayer(layer, false);
        ui.hudFragment.updateLayers();
    }

    public Drawable icon() {
        return Icons.trash;
    }

    public String name() {
        return "@history.remove";
    }

    public String desc() {
        return bundle.format("history.layer", layer.name);
    }
}