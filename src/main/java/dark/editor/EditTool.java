package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static dark.Main.*;

public enum EditTool {
    pencil(true, Binding.pencil) {
        @Override
        public void touched(int x, int y, Color color) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, color);
        }
    },

    eraser(true, Binding.eraser) {
        @Override
        public void touched(int x, int y, Color color) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        @Override
        public void touched(int x, int y, Color color) {
            Paint.fill(editor.renderer.current, x, y, color);
        }
    },

    line(false, null), pick(false, null);

    public boolean draggable;
    public Binding hotkey;

    private EditTool(boolean draggable, Binding hotkey) {
        this.draggable = draggable;
        this.hotkey = hotkey;
    }

    public void touched(int x, int y, Color color) {}

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.checkImageButtonStyle, 64f, () -> editor.tool = this)
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(64f).row();
    }
}