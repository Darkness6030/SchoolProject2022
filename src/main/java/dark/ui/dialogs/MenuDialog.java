package dark.ui.dialogs;

import dark.ui.*;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public String locale = settings.getString("locale");

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.showFileChooser("@file.save", false, "png", editor::save));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser("@file.load", true, "png", editor::load));

        buttons.row();

        buttons.button("English", Styles.textButtonCheck, () -> locale = "en").checked(button -> locale.equals("en"));
        buttons.button("Русский", Styles.textButtonCheck, () -> locale = "ru").checked(button -> locale.equals("ru"));

        hidden(() -> {
            if (!locale.equals(settings.getString("locale"))) ui.showInfoToast(Icons.home, "@restart-required");

            settings.put("locale", locale);
            settings.forceSave();
        });
    }
}