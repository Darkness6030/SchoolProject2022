package dark.ui.dialogs;

import arc.func.Boolp;
import arc.input.KeyCode;
import arc.scene.ui.Dialog;
import arc.util.Align;
import dark.ui.*;

import static arc.Core.scene;

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
        this.titleTable.marginLeft(28f); // center the title
        this.titleTable.button(Icons.cancel, this::hide);
    }

    public BaseDialog(String title) {
        this(title, scene.getStyle(DialogStyle.class));
    }

    @Override
    public void addCloseButton() {
        closeOnBack();

        buttons.defaults().size(200f, 64f);
        buttons.buttonRow("@back", Icons.back, this::hide);
    }

    public void addConfirmButton(Runnable runnable) {
        keyDown(KeyCode.enter, runnable);

        buttons.defaults().size(200f, 64f);
        buttons.buttonRow("@ok", Icons.ok, runnable);
    }

    public void addConfirmButton(Runnable runnable, Boolp disabled) {
        keyDown(KeyCode.enter, () -> {
            if (!disabled.get())
                runnable.run();
        });

        buttons.defaults().size(200f, 64f);
        buttons.buttonRow("@ok", Icons.ok, runnable).disabled(button -> disabled.get());
    }
}