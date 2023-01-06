package dark.ui.dialogs;

import dark.ui.Icons;

import static arc.Core.app;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.showFileChooser(false, "@file.save", "png", editor::save));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser(true, "@file.load", "png", editor::load));

        buttons.row();

        buttons.buttonRow("@settings", Icons.plus, () -> {}).colspan(2).width(428f);
    }
}