package dark.ui.dialogs;

import static arc.Core.app;

import dark.ui.Icons;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        buttons.button(String.valueOf(Icons.exit), () -> { // TODO временно
            app.exit();
        });
    }
}
