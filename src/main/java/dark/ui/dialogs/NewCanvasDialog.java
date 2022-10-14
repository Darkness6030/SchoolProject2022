package dark.ui.dialogs;

import dark.ui.Icons;
import dark.ui.elements.TextSlider;

import static dark.Main.*;

import dark.editor.Canvas;

public class NewCanvasDialog extends BaseDialog {

    public int lastWidth = 800;
    public int lastHeight = 600;

    public NewCanvasDialog() {
        super("New Canvas"); // TODO add bundle

        new TextSlider(0f, 1000f, 10f, lastWidth, value -> "Width: " + (lastWidth = value.intValue()) + "px").build(cont);
        new TextSlider(0f, 1000f, 10f, lastHeight, value -> "Height: " + (lastHeight = value.intValue()) + "px").build(cont);

        addCloseButton();
        buttons.button(String.valueOf(Icons.ok), () -> {
            // may need more in the future
            editor.canvas = new Canvas(lastWidth, lastHeight);
        });
    }
}
