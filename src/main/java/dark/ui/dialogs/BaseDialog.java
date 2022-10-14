package dark.ui.dialogs;

import arc.scene.ui.Dialog;
import dark.ui.Icons;
import dark.ui.Palette;
import dark.ui.Textures;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        titleTable.row(); // horizontal gap
        titleTable.image(Textures.whiteui, Palette.active).growX().height(3f).pad(4f);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        buttons.defaults().size(210f, 64f);
        buttons.button(String.valueOf(Icons.back), this::hide);

        closeOnBack();
    }
}
