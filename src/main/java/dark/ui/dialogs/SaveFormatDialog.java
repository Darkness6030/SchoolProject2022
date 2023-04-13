package dark.ui.dialogs;

import arc.func.Cons;
import arc.scene.ui.layout.Table;
import dark.ui.Drawables;
import dark.ui.Styles;
import dark.ui.elements.*;

public class SaveFormatDialog extends BaseDialog {

    public static boolean transparent;
    public static int quality = 5;

    public Cons<Format> callback;
    public Format selected = Format.png;

    public SaveFormatDialog() {
        super("@save-format");

        addCloseButton();
        addConfirmButton(() -> {
            callback.get(selected);
            hide();
        });

        getCells().get(2).height(300f);
        cont.top();

        for (Format format : Format.values()) {
            cont.button(format.name().toUpperCase(), Styles.textButtonCheck, () -> selected = format).checked(b -> selected == format).with(button -> {
                button.row();
                button.collapser(format.settings::get, true, button::isChecked).grow().pad(4f);
            }).width(300f).top().row();
        }
    }

    public void show(Cons<Format> callback) {
        this.callback = callback;
        super.show();
    }

    public enum Format {
        spx(cont -> {}), // TODO Adi, добавь сохранение во внутренний формат с поддержкой слоёв, это изи

        png(cont -> {
            cont.add(new Switch("@transparent", value -> transparent = value));
        }),

        jpg(cont -> {
            cont.add(new Switch("@transparent", value -> transparent = value)).row();

            cont.add(new Field("@quality", 64f, quality, 1, 0, 9, value -> quality = value));
        }),

        bmp(cont -> {});

        public final Cons<Table> settings;

        Format(Cons<Table> settings) {
            this.settings = cont -> {
                cont.background(Drawables.darkmain_rounded);
                cont.margin(4f).left();

                settings.get(cont);
            };
        }
    }
}
