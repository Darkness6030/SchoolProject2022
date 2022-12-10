package dark.ui.dialogs;

import arc.scene.style.Drawable;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import dark.ui.*;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        titleTable.row(); // horizontal gap
        titleTable.image(Drawables.whiteui, Palette.accent).growX().height(3f).pad(4f);

        buttons.defaults().size(210f, 64f);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        addButton("Back", Icons.back, this::hide);
        closeOnBack();
    }

    public Cell<TextButton> addButton(String text, Drawable image, Runnable clicked) {
        return buttons.buttonRow(text, image, clicked);
    }
}