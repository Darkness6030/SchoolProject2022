package dark.ui.dialogs;

import static arc.Core.app;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();

        // TODO временно
        buttons.button("Exit", () -> {
            app.exit();
        });
    }
}
