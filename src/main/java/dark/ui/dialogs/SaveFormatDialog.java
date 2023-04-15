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

        getCells().get(2).height(350f);
        cont.top();

        for (var format : Format.values()) {
            cont.button(format.name().toUpperCase(), Styles.textButtonCheck, () -> selected = format).checked(button -> selected == format).with(button -> {
                button.row();
                button.collapser(table -> {
                    table.background(Drawables.main_rounded);
                    table.margin(8f).left();

                    table.labelWrap("@format." + format.name()).width(320f);
                }, true, button::isChecked).grow().pad(4f);
            }).width(350f).top().row();
        }
    }

    public void show(Cons<Format> callback) {
        this.callback = callback;
        super.show();
    }

    public enum Format {
        spx, png, jpg, jpeg, bmp;
    }
}