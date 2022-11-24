package dark.ui.elements;

import arc.graphics.g2d.Lines;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import dark.editor.Layer;
import dark.ui.Palette;

import static dark.Main.editor;

public class LayerButton extends ImageButton {

    public final Layer layer;

    public LayerButton(Layer layer) {
        super(new TextureRegionDrawable(layer.getRegion()));

        this.layer = layer;
        this.clicked(() -> editor.canvas.layer(layer.index()));
    }

    @Override
    public void draw() {
        Lines.stroke(4f, editor.canvas.layer() == layer ? Palette.active : Palette.main);
        Lines.rect(x, y, width, height);

        super.draw();
    }
}