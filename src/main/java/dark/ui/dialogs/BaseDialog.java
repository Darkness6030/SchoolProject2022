package dark.ui.dialogs;

import arc.scene.ui.Dialog;
import dark.ui.*;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        titleTable.row(); // horizontal gap
        titleTable.image(Textures.whiteui, Palette.accent).growX().height(3f).pad(4f);

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

    public void addButton(char icon, String text, Runnable listener) {
        buttons.button(icon + " " + text, listener);
    }
}