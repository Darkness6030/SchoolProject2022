package dark.ui.dialogs;

import dark.ui.Icon;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public MenuDialog() {
        super("Menu");

        addCloseButton();
        addButton("Exit", Icon.exit, app::exit);

        buttons.row();

        addButton("Save", Icon.save, () -> ui.showFileChooser(false, "@select.save", "png", editor::save));
        addButton("Load", Icon.load, () -> ui.showFileChooser(false, "@select.load", "png", editor::load));
    }
}