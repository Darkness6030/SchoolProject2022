package dark.history;

import arc.graphics.Pixmap;
import arc.struct.IntSeq;
import dark.editor.Layer;

public class Operation {

    public static Pixmap before;
    public static Layer after;

    public IntSeq data;

    /** Thread unsafe, do not call in multithreaded code! */
    public void begin(Layer layer) {
        before = layer.copy();
        after = layer;
    }

    /** Stores difference between {@link #before} and {@link #after} in {@link IntSeq}. */
    public void end() {
        data = Compress.difference(before, after);
    }

    public void undo() {}

    public void redo() {}
}
