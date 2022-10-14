package dark.editor;

import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static dark.Main.*;

public enum EditType {
    pencil(Icons.pencil), pick(Icons.pick), eraser(Icons.eraser), line(Icons.line);

    public final char icon;

    EditType(char icon) {
        this.icon = icon;
    }

    public void button(Table table) {
        table.button(String.valueOf(icon), Styles.checkt, () -> editor.type = this).checked(button -> editor.type == this).size(64f).row();
    }
}