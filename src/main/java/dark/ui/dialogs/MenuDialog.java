package dark.ui.dialogs;

import dark.ui.*;
import dark.utils.Files;

import static arc.Core.*;
import static dark.Main.*;

public class MenuDialog extends BaseDialog {

    public String locale = settings.getString("locale");

    public MenuDialog() {
        super("@menu");

        addCloseButton();
        buttons.buttonRow("@exit", Icons.exit, app::exit);

        buttons.row();

        buttons.buttonRow("@save", Icons.save, () -> ui.format.show(format -> ui.showFileChooser("@file.save", format.name(), editor::save)));
        buttons.buttonRow("@load", Icons.load, () -> ui.showFileChooser("@file.load", Files.extensions, editor::load));

        buttons.row();

        buttons.button("English", Styles.textButtonCheck, () -> locale = "en").checked(button -> locale.equals("en"));
        buttons.button("Русский", Styles.textButtonCheck, () -> locale = "ru").checked(button -> locale.equals("ru"));

        hidden(() -> {
            if (!locale.equals(settings.getString("locale"))) ui.showInfoToast(Drawables.alpha_chan, "@restart-required");

            settings.put("locale", locale);
            settings.forceSave();
        });
    }
}