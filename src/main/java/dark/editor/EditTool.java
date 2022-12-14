package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static dark.Main.*;

public enum EditTool {
    pencil {
        {
            draggable = true;
        }

        @Override
        public void touched(int x, int y, Color color) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, color);
        }
    },

    eraser {
        {
            draggable = true;
        }

        @Override
        public void touched(int x, int y, Color color) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, Color.clear);
        }
    },

    fill {
        @Override
        public void touched(int x, int y, Color color) {
            Paint.fill(editor.renderer.current, x, y, color);
        }
    },

    line,
    pick;

    public boolean draggable;

    public void touched(int x, int y, Color color) {}

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.checkImageButtonStyle, 64f, () -> editor.tool = this)
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .fontScale(32f).row();
    }
}