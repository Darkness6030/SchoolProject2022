package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.showFileChooser("@file.save", false, "png", editor::save));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser("@file.load", true, "png", editor::load));
    }
}