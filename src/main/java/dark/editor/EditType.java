package dark.editor;

import arc.scene.ui.layout.Table;
import dark.ui.Fonts;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.scene;
import static dark.Main.editor;

public enum EditType {
    pencil(Icons.pencil),
    eraser(Icons.eraser),
    pick(Icons.pick),
    fill(Icons.fill);

    public final char icon;

    EditType(char icon) {
        this.icon = icon;
    }

    public void button(Table table) {
        table.button(Fonts.getGlyph(icon), Styles.checkImageButtonStyle, 72f, () -> editor.type = this)
                .checked(button -> editor.type == this)
                .tooltip("@" + name() + ".tooltip")
                .fontScale(32f).row();
    }

    public boolean isSelected() {
        return editor.type == this && !scene.hasMouse();
    }
}