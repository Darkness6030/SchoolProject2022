package dark.ui.dialogs;

import arc.scene.ui.Dialog;
import arc.util.Align;
import dark.ui.Drawables;
import dark.ui.Icons;

import static arc.Core.*;

public class BaseDialog extends Dialog {

    public BaseDialog(String title, DialogStyle style) {
        super(title, style);

        margin(0f).clear();
        defaults().pad(4f);

        add(titleTable).growX().row();
        image(Drawables.darkmain).growX().height(8f).pad(0f).row(); // horizontal gap

        add(cont).fill().row();
        add(buttons).fillX();

        this.title.setAlignment(Align.center);
        titleTable.marginLeft(28f); // center the title
        titleTable.button(Icons.cancel, this::hide);
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