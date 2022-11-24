package dark.ui.dialogs;

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
        addButton(Icons.back, "Back", this::hide);
        closeOnBack();
    }

    public Cell<TextButton> addButton(char icon, String text, Runnable listener) {
        return buttons.button(icon + " " + text, listener);
    }
}