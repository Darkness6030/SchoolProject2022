package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.editor;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        addButton(Icons.exit, "Exit", app::exit);

        buttons.row();

        addButton(Icons.save, "Save", editor::save);
        addButton(Icons.load, "Load", editor::load);
    }
}