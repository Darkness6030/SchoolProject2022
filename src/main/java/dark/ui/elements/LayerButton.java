package dark.ui.elements;

import arc.graphics.g2d.Lines;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import dark.ui.Palette;

import static dark.Main.editor;

public class LayerButton extends ImageButton {

    public final int layer;

    public LayerButton(int layer) {
        super(new TextureRegionDrawable(editor.canvas.layers.get(layer).getRegion()));

        this.layer = layer;
        this.clicked(() -> editor.canvas.layer(layer));
    }

    @Override
    public void draw() {
        Lines.stroke(4f, editor.canvas.currentLayer == layer ? Palette.active : Palette.main);
        Lines.rect(x, y, width, height);

        super.draw();
    }
}