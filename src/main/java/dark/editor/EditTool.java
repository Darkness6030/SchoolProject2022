package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.*;
import static dark.Main.*;

public enum EditTool {
    pencil(true, Binding.pencil) {
        public void touched(int x, int y, Color color) {
            Paint.pencil(editor.renderer.current, x, y, editor.brushSize, color);
        }
    },

    eraser(true, Binding.eraser) {
        public void touched(int x, int y, Color color) {
            Paint.pencil(editor.renderer.current, x, y, editor.brushSize, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        public void touched(int x, int y, Color color) {
            Paint.fill(editor.renderer.current, x, y, color);
        }
    },

    line(false, null) {
        public void touched(int x, int y, Color color) {}
    },

    pick(false, Binding.pick) {
        public void touched(int x, int y, Color color) {
            if (scene.hasMouse()) return;

            for (Layer layer : editor.renderer.layers) {
                if (layer.getRaw(x, y) != Color.clearRgba) {
                    color.set(layer.getRaw(x, y));
                    return;
                }
            }

            color.set(Color.clearRgba);
        }
    };

    public final boolean draggable;
    public final Binding hotkey;

    private EditTool(boolean draggable, Binding hotkey) {
        this.draggable = draggable;
        this.hotkey = hotkey;
    }

    public abstract void touched(int x, int y, Color color);

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.checkImageButtonStyle, 64f, () -> editor.tool = this)
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(64f).row();
    }
}