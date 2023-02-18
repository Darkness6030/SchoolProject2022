package dark.ui.dialogs;

import arc.scene.ui.TextField.TextFieldFilter;
import arc.util.Strings;
import dark.ui.Icons;

import static dark.Main.editor;

public class ResizeDialog extends BaseDialog {

    public static final int minSize = 64, maxSize = 4096;

    public int width, height;

    public ResizeDialog() {
        super("@resize");
        closeOnBack();

        shown(() -> {
            width = editor.canvas.width;
            height = editor.canvas.height;

            cont.clear();
            cont.defaults().height(60f).padTop(8f);

            cont.add("@width").padRight(8f);
            cont.field(String.valueOf(width), TextFieldFilter.digitsOnly, value -> width = Strings.parseInt(value)).valid(value -> {
                int number = Strings.parseInt(value);
                return number >= minSize && number <= maxSize;
            }).maxTextLength(3).row();

            cont.add("@height").padRight(8f);
            cont.field(String.valueOf(height), TextFieldFilter.digitsOnly, value -> height = Strings.parseInt(value)).valid(value -> {
                int number = Strings.parseInt(value);
                return number >= minSize && number <= maxSize;
            }).maxTextLength(3).row();
        });

        buttons.buttonRow("@back", Icons.left, this::hide);
        buttons.buttonRow("@ok", Icons.ok, () -> {
            hide();
            editor.reset(width, height);
        });
    }
}