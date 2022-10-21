package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.app;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        buttons.button(String.valueOf(Icons.exit), () -> { // TODO временно
            app.exit();
        });
    }
}