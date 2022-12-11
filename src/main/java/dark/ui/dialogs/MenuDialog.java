package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        addButton("Exit", Icons.exit, app::exit);

        buttons.row();

        addButton("Save", Icons.save, () -> ui.showFileChooser(false, "@file.save", "png", editor::save));
        addButton("Load", Icons.load, () -> ui.showFileChooser(true, "@file.load", "png", editor::load));
    }
}