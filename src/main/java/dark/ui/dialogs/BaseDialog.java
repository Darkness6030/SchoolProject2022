package dark.ui.dialogs;

import arc.scene.style.Drawable;
import arc.scene.ui.Dialog;
import arc.util.Align;
import dark.ui.*;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        this.titleTable.row().row(); // horizontal gap
        this.titleTable.image(Drawables.white, Palette.active).growX().height(3f).pad(4f);

        this.title.setAlignment(Align.center);
        this.buttons.defaults().size(210f, 64f);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        addButton("Back", Icons.back, this::hide);
        closeOnBack();
    }

    public void addButton(String text, Drawable image, Runnable clicked) {
        buttons.buttonRow(text, image, clicked);
    }
}