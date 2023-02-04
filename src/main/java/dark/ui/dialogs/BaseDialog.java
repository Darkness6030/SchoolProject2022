package dark.ui.dialogs;

import arc.scene.ui.Dialog;
import arc.util.Align;
import dark.ui.*;

import static arc.Core.scene;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        this.titleTable.row().row(); // horizontal gap
        this.titleTable.image(Drawables.white, Palette.accent).growX().height(3f).pad(4f);

        this.title.setAlignment(Align.center);
        this.buttons.defaults().size(210f, 64f);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }
}