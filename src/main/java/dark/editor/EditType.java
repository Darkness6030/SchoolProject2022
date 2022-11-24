package dark.editor;

import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import dark.ui.*;

import static arc.Core.*;
import static dark.Main.editor;

public enum EditType {
    pencil,
    eraser,
    pick,
    fill;

    public void button(Table table) {
        // TODO а как получить вообще эту иконку
        table.button(mindustry.ui.Fonts.getGlyph(mindustry.ui.Fonts.icon, Icons.pencil), Styles.checkImageButtonStyle, 24f, () -> editor.type = this).checked(button -> editor.type == this).size(64f).row();
    }

    public boolean isSelected() {
        return editor.type == this && !scene.hasMouse();
    }
}