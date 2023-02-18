package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.app;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("@menu");
        closeOnBack();

        buttons.buttonRow("@back", Icons.left, this::hide);
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.showFileChooser("@file.save", false, "png", editor::save));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser("@file.load", true, "png", editor::load));

        buttons.row();

        buttons.buttonRow("@settings", Icons.plus, () -> ui.settings.show()).colspan(2).width(428f);
    }
}