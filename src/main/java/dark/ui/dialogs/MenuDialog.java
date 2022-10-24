package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        addButton(Icons.exit, "Exit", app::exit);

        buttons.row();

        addButton(Icons.save, "Save", () -> {});
        addButton(Icons.load, "Load", () -> {});
    }
}