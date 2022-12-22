package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.app;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        addButton("@exit", Icons.exit, app::exit);

        buttons.row();

        addButton("@save", Icons.save, () -> ui.showFileChooser(false, "@file.save", "png", editor::save));
        addButton("@load", Icons.load, () -> ui.showFileChooser(true, "@file.load", "png", editor::load));

        buttons.row();

        addButton("@settings", Icons.plus, () -> {});
    }
}