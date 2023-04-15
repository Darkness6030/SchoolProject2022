package dark.ui.dialogs;

import arc.func.Cons;
import dark.ui.Drawables;
import dark.ui.Styles;

public class SaveFormatDialog extends BaseDialog {

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

        for (var format : Format.values()) {
            cont.button(format.name().toUpperCase(), Styles.textButtonCheck, () -> selected = format).checked(b -> selected == format).with(button -> {
                button.row();
                button.collapser(table -> {
                    table.background(Drawables.main_rounded);
                    table.margin(4f).left();

                    table.labelWrap("@extension." + format.name() + ".description");
                }, true, button::isChecked).grow().pad(4f);
            }).width(300f).top().row();
        }
    }

    public void show(Cons<Format> callback) {
        this.callback = callback;
        super.show();
    }

    public enum Format {
        spx,
        png,
        jpg,
        jpeg,
        bmp;
    }
}