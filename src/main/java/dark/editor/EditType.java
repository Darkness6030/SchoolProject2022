package dark.editor;

import arc.input.KeyCode;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.*;
import static dark.Main.editor;

public enum EditType {
    pencil(Icons.pencil, Binding.pencil),
    pick(Icons.pick, Binding.pick),
    eraser(Icons.eraser, Binding.eraser),
    line(Icons.line, Binding.line);

    public final char icon;
    public final KeyCode code;

    EditType(char icon, KeyCode code) {
        this.icon = icon;
        this.code = code;
    }

    public void button(Table table) {
        table.button(String.valueOf(icon), Styles.checkButtonStyle, () -> editor.type = this).checked(button -> editor.type == this).size(64f).row();
    }

    public boolean isTapped() {
        return input.keyDown(code) && !scene.hasMouse();
    }
}