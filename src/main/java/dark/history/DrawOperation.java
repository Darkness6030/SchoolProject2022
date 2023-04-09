package dark.history;

import arc.graphics.Pixmap;
import arc.scene.style.Drawable;
import arc.struct.IntSeq;
import dark.editor.EditTool;
import dark.editor.Layer;
import dark.ui.Icons;

import static arc.Core.*;

public class DrawOperation implements Operation {

    public static Pixmap before;

    public EditTool tool;
    public Layer layer;
    public IntSeq data;

    public DrawOperation(EditTool tool, Layer layer) {
        this.tool = tool;
        this.layer = layer;
    }

    /** Thread unsafe, do not call in multithreaded code! */
    public void begin() {
        before = layer.copy();
    }

    /** Stores difference between {@link #before} and {@link #layer} in {@link IntSeq}. */
    public void end() {
        data = Compress.difference(before, layer);
    }

    public void undo() {
        Compress.read(data, (x, y, color) -> layer.setRaw(x, y, layer.getRaw(x, y) - color));
        layer.unchange();
    }

    public void redo() {
        Compress.read(data, (x, y, color) -> layer.setRaw(x, y, layer.getRaw(x, y) + color));
        layer.unchange();
    }

    public Drawable icon() {
        return Icons.drawable(tool.name());
    }

    public String name() {
        return "@history." + tool.name();
    }

    public String desc() {
        return bundle.format("history.layer", layer.name);
    }
}