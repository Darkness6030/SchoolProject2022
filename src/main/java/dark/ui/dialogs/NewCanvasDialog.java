package dark.ui.dialogs;

import dark.ui.Icons;
import dark.ui.elements.TextSlider;

import static arc.Core.bundle;
import static dark.Main.*;

public class NewCanvasDialog extends BaseDialog {

    public int lastWidth = 800;
    public int lastHeight = 600;

    public NewCanvasDialog() {
        super("@canvas.new");

        new TextSlider(100f, 1000f, 10f, lastWidth, value -> bundle.format("canvas.width", lastWidth = value.intValue())).build(cont).growX().row();
        new TextSlider(100f, 1000f, 10f, lastHeight, value -> bundle.format("canvas.height", lastHeight = value.intValue())).build(cont).growX().row();

        addCloseButton();
        addButton("Ok", Icons.load, () -> {
            hide();
            editor.resetCanvas(lastWidth, lastHeight);
            ui.hudFragment.rebuildLayers.run();
        });
    }
}