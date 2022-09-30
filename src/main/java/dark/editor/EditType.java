package dark.editor;

import arc.scene.ui.layout.Table;
import dark.ui.Icons;

import static dark.Main.editor;

public enum EditType {
    pencil(Icons.pencil), pick(Icons.pick), eraser(Icons.eraser), line(Icons.line);

    public final char icon;

    EditType(char icon) {
        this.icon = icon;
    }

    public void button(Table table) {
        table.button(String.valueOf(icon), () -> editor.type = this).checked(button -> editor.type == this);
    }
}
