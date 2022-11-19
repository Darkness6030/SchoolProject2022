package dark.ui.elements;

import arc.graphics.g2d.Lines;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import dark.editor.Layer;
import dark.ui.Palette;

import static dark.Main.*;

public class LayerButton extends ImageButton {

    public Layer layer;

    public LayerButton(Layer layer) {
        super(new TextureRegionDrawable(layer.getRegion()));
        this.layer = layer;
    }

    @Override
    public void draw() {
        boolean active = editor.canvas.layer() == layer;

        Lines.stroke(4f, active ? Palette.active : Palette.main);
        Lines.rect(x, y, width, height);

        super.draw();
    }
}
