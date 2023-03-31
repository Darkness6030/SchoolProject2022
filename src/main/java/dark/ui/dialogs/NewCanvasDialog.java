package dark.ui.dialogs;

import arc.graphics.Color;
import arc.util.Strings;
import dark.ui.Icons;
import dark.ui.elements.Field;

import static dark.Main.*;

public class NewCanvasDialog extends BaseDialog {

    public static final int maxSize = 4096;

    public int width, height;
    public Color background;

    public NewCanvasDialog() {
        super("@resize");
        addCloseButton();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            editor.reset(width, height);
            hide();
        });

        shown(() -> {
            width = editor.canvas.width;
            height = editor.canvas.height;

            cont.clear();
            cont.defaults().padTop(8f);

            cont.add(new Field("@width", 64f, width, (int value) -> width = value)).with(field -> {
                field.maxTextLength(4);
                field.valid(value -> {
                    int number = Strings.parseInt(value);
                    return number >= 1 && number <= maxSize;
                });
            }).row();

            cont.add(new Field("@height", 64f, height, (int value) -> height = value)).with(field -> {
                field.maxTextLength(4);
                field.valid(value -> {
                    int number = Strings.parseInt(value);
                    return number >= 1 && number <= maxSize;
                });
            });
        });
    }
}