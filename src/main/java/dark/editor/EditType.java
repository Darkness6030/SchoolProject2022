package dark.editor;

import arc.scene.style.Drawable;
import arc.scene.ui.layout.Table;
import dark.ui.Icon;
import dark.ui.Styles;

import static arc.Core.scene;
import static dark.Main.editor;

public enum EditType {
    pencil(Icon.pencil),
    eraser(Icon.eraser),
    pick(Icon.pick),
    fill(Icon.fill);

    public final Drawable icon;

    EditType(Drawable icon) {
        this.icon = icon;
    }

    public void button(Table table) {
        table.button(icon, Styles.checkImageButtonStyle, 64f, () -> editor.type = this)
                .checked(button -> editor.type == this)
                .tooltip("@" + name() + ".tooltip")
                .fontScale(32f).row();
    }

    public boolean isSelected() {
        return editor.type == this && !scene.hasMouse();
    }
}