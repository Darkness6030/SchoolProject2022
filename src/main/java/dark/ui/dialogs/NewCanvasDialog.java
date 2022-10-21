package dark.ui.dialogs;

import dark.editor.Canvas;
import dark.ui.Icons;
import dark.ui.elements.TextSlider;

import static dark.Main.editor;

public class NewCanvasDialog extends BaseDialog {

    public int lastWidth = 800;
    public int lastHeight = 600;

    public NewCanvasDialog() {
        super("New Canvas");

        new TextSlider(0f, 1000f, 10f, lastWidth, value -> "Width: " + (lastWidth = value.intValue()) + "px").build(cont).growX().row();
        new TextSlider(0f, 1000f, 10f, lastHeight, value -> "Height: " + (lastHeight = value.intValue()) + "px").build(cont).growX().row();

        addCloseButton();
        buttons.button(String.valueOf(Icons.ok), () -> {
            this.hide();
            editor.canvas = new Canvas(lastWidth, lastHeight);
        });
    }
}