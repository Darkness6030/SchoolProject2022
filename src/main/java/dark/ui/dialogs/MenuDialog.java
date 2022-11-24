package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        addButton(Icons.exit, "Exit", app::exit);

        buttons.row();

        addButton(Icons.save, "Save", () -> ui.explorerDialog.show(editor::save, true));
        addButton(Icons.load, "Load", () -> ui.explorerDialog.show(editor::load, false));
    }
}