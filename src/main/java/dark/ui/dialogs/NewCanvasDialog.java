package dark.ui.dialogs;

import arc.graphics.Color;
import dark.ui.*;
import dark.ui.elements.*;

import static dark.Main.*;

public class NewCanvasDialog extends BaseDialog {

    public static final int maxSize = 4096;

    public int width, height;
    public boolean fill;
    public Color background = Color.white.cpy();

    public NewCanvasDialog() {
        super("@new-canvas");

        addCloseButton();
        addConfirmButton(() -> {
            editor.reset(width, height);
            hide();

            if (fill) editor.renderer.current.fill(background);
        });

        shown(() -> {
            width = canvas.width;
            height = canvas.height;
            fill = false;

            cont.clear();
            cont.defaults().width(196f).padTop(8f);

            cont.add(new Field("@width", 96f, width, 4, 1, maxSize, (int value) -> width = value));

            cont.add(new Switch("@fill-background", value -> fill = value).left()).row();

            cont.add(new Field("@height", 96f, height, 4, 1, maxSize, (int value) -> height = value));

            cont.button(Drawables.empty, Styles.background, () -> ui.palette.show(background))
                    .update(button -> button.setColor(fill ? background : Color.clear))
                    .disabled(button -> !fill);
        });
    }
}