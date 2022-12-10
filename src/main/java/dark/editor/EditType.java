package dark.editor;

import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.scene;
import static dark.Main.editor;

public enum EditType {
    pencil,
    eraser,
    pick,
    fill;

    public void button(Table table) {
        table.button(Icons.getDrawable(name()), Styles.checkImageButtonStyle, 64f, () -> editor.type = this)
                .checked(button -> editor.type == this)
                .tooltip("@" + name() + ".tooltip")
                .fontScale(32f).row();
    }

    public boolean isSelected() {
        return editor.type == this && !scene.hasMouse();
    }
}