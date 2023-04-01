package dark.history;

import arc.graphics.Pixmap;
import arc.struct.IntSeq;
import dark.editor.EditTool;
import dark.editor.Layer;

public class Operation {

    public static Pixmap before;

    public EditTool tool;
    public Layer layer;
    public IntSeq data;

    public Operation(EditTool tool, Layer layer) {
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
        layer.updateTexture();
    }

    public void redo() {
        Compress.read(data, (x, y, color) -> layer.setRaw(x, y, layer.getRaw(x, y) + color));
        layer.updateTexture();
    }
}