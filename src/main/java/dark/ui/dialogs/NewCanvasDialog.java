package dark.ui.dialogs;

import dark.editor.LayerCanvas;
import dark.ui.Icons;
import dark.ui.elements.TextSlider;

import static dark.Main.*;

public class NewCanvasDialog extends BaseDialog {

    public int lastWidth = 800;
    public int lastHeight = 600;

    public NewCanvasDialog() {
        super("New Canvas");

        new TextSlider(100f, 1000f, 10f, lastWidth, value -> "Width: " + (lastWidth = value.intValue()) + "px").build(cont).growX().row();
        new TextSlider(100f, 1000f, 10f, lastHeight, value -> "Height: " + (lastHeight = value.intValue()) + "px").build(cont).growX().row();

        addCloseButton();
        addButton(Icons.ok, "Ok", () -> {
            hide();
            editor.resetCanvas(lastWidth, lastHeight);
            ui.hudFragment.needRebuildLayers = true;
        });
    }
}