package dark.editor;

import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.scene;
import static dark.Main.editor;

public enum EditType {
    pencil(Icons.pencil),
    pick(Icons.pick),
    eraser(Icons.eraser),
    line(Icons.line);

    public final char icon;

    EditType(char icon) {
        this.icon = icon;
    }

    public void button(Table table) {
        table.button(String.valueOf(icon), Styles.checkButtonStyle, () -> editor.type = this).checked(button -> editor.type == this).size(64).scaling(Scaling.fit).row();
    }

    public boolean isSelected() {
        return editor.type == this && !scene.hasMouse();
    }
}