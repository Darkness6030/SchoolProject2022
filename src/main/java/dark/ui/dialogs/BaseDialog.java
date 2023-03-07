package dark.ui.dialogs;

import arc.scene.ui.Dialog;
import arc.util.Align;
import dark.ui.Drawables;
import dark.ui.Icons;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);
        this.title.setAlignment(Align.center);

        this.titleTable.row(); // horizontal gap
        this.titleTable.image(Drawables.active).growX().height(4f).pad(4f);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        buttons.defaults().size(200f, 64f);
        buttons.buttonRow("@back", Icons.back, this::hide);

        closeOnBack();
    }
}
