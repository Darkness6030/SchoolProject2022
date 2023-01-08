package dark.ui.dialogs;

import dark.ui.Icons;
import dark.ui.elements.TextFieldSlider;

import static dark.Main.*;

public class ResizeDialog extends BaseDialog {

    public int lastWidth = 800;
    public int lastHeight = 600;

    public ResizeDialog() {
        super("@canvas.resize");

        cont.add(new TextFieldSlider("", 100f, 1000f, 10f, lastWidth, value -> lastWidth = (int) value)).growX().row();
        cont.add(new TextFieldSlider("", 100f, 1000f, 10f, lastHeight, value -> lastHeight = (int) value)).growX().row();

        addCloseButton();
        buttons.buttonRow("@ok", Icons.ok, () -> {
            hide();

            editor.reset(lastWidth, lastHeight);
            ui.hudFragment.rebuildLayers.run();
        });
    }
}