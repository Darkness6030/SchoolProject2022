package dark.ui.dialogs;

import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public boolean english = settings.getString("locale").equals("en"), changed;

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.showFileChooser("@file.save", false, "png", editor::save));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser("@file.load", true, "png", editor::load));

        buttons.row();

        buttons.button("English", Styles.layersTab, () -> {
            settings.put("locale", "en");
            english = changed = true;
        }).checked(b -> english);

        buttons.button("Русский", Styles.historyTab, () -> {
            settings.put("locale", "ru");
            english = !(changed = true);
        }).checked(b -> !english);

        hidden(() -> {
            if (changed) ui.showInfoToast(Icons.home, "@restart-required");
            changed = false;
        });
    }
}