package dark.ui.dialogs;

import arc.graphics.Color;
import arc.util.Strings;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.Field;
import dark.ui.elements.Switch;

import static dark.Main.*;

public class NewCanvasDialog extends BaseDialog {

    public static final int maxSize = 4096;

    public int width, height;
    public boolean fill;
    public Color background = Color.white.cpy();

    public NewCanvasDialog() {
        super("@new-canvas");
        addCloseButton();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            editor.reset(width, height);
            hide();

            if (fill) editor.renderer.current.fill(background);
        });

        shown(() -> {
            width = editor.canvas.width;
            height = editor.canvas.height;

            cont.clear();
            cont.defaults().width(196f).padTop(8f);

            cont.add(new Field("@width", 96f, width, (int value) -> width = value)).with(field -> {
                field.maxTextLength(4);
                field.valid(value -> {
                    int number = Strings.parseInt(value);
                    return number >= 1 && number <= maxSize;
                });
            });

            cont.add(new Switch("@fill-background", value -> fill = value)).row();

            cont.add(new Field("@height", 96f, height, (int value) -> height = value)).with(field -> {
                field.maxTextLength(4);
                field.valid(value -> {
                    int number = Strings.parseInt(value);
                    return number >= 1 && number <= maxSize;
                });
            });

            cont.button(Drawables.empty, Styles.background, () -> ui.palette.show(background))
                    .update(b -> b.setColor(fill ? background : Color.clear))
                    .disabled(b -> !fill);
        });
    }
}