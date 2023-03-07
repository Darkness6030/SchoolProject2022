package dark.editor;

import arc.graphics.*;
import arc.scene.ui.layout.Table;
import dark.ui.*;

import static arc.Core.*;
import static dark.Main.*;

public enum EditTool {
    pencil(true, Binding.pencil) {
        public void touched(Layer layer, int x, int y, Color color) {
            if (editor.square)
                layer.drawSquare(x, y, editor.brushSize, color);
            else
                layer.drawCircle(x, y, editor.brushSize, color);
        }
    },

    eraser(true, Binding.eraser) {
        public void touched(Layer layer, int x, int y, Color color) {
            if (editor.square)
                layer.drawSquare(x, y, editor.brushSize, Color.clear);
            else
                layer.drawCircle(x, y, editor.brushSize, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        public void touched(Layer layer, int x, int y, Color color) {
            layer.fill(x, y, color);
        }
    },

    line(false, null) {
        public void touched(Layer layer, int x, int y, Color color) {}
    },

    pick(false, Binding.pick) {
        public void touched(Layer layer, int x, int y, Color color) {
            if (scene.hasMouse()) return;

            // for (var layer : editor.renderer.layers) {
                //if (layer.get(x, y) != 0) {
                //    ui.colorWheel.add(color.set(layer.get(x, y)));
                //    return;
                //}
            // }
        }
    };

    public final boolean draggable;
    public final Binding hotkey;

    EditTool(boolean draggable, Binding hotkey) {
        this.draggable = draggable;
        this.hotkey = hotkey;
    }

    public abstract void touched(Layer layer, int x, int y, Color color);

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.imageButtonCheck, 48f, () -> editor.tool = this)
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(48f).pad(8f, 8f, 0f, 8f).row();
    }
}