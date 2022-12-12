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
        public void touched(int x, int y) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, editor.first.rgba());
        }
    },

    eraser {
        {
            draggable = true;
        }

        @Override
        public void touched(int x, int y) {
            Paint.circle(editor.renderer.current, x, y, editor.brushSize, Color.clearRgba);
        }
    },

    fill,
    pick,
    line,
    zoom;

    public boolean draggable;

    public void touched(int x, int y) {}

    public void touchedLine(int startX, int startY, int endX, int endY) {}

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.checkImageButtonStyle, 64f, () -> editor.tool = this)
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .fontScale(32f).row();
    }
}