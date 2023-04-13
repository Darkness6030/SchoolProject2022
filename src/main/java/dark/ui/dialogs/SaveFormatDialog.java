package dark.ui.dialogs;

import arc.func.Cons;
import arc.scene.ui.layout.Table;
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
    }

    public void show(Cons<Format> callback) {
        this.callback = callback;
        super.show();
    }

    public enum Format {
        spx(cont -> {}),

        png(cont -> {
            cont.add(new Switch("@transparent", value -> transparent = value));
        }),

        jpg(cont -> {
            cont.add(new Switch("@transparent", value -> transparent = value)).row();

            cont.add(new Field("@quality", 64f, quality, 1, 0, 10, value -> quality = value)).with(field -> {
                new SliderTable(field, 0, 10, 1, value -> quality = value);
            });
        });

        public final Cons<Table> settings;

        Format(Cons<Table> settings) {
            this.settings = settings;
        }
    }
}
