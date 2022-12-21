package dark.editor;

import arc.files.Fi;
import arc.func.Intc2;
import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;

public class Layer extends Pixmap {

    public final TextureRegion region = new TextureRegion(new Texture(this));

    public Layer(int width, int height) {
        super(width, height);
    }

    public Layer(Fi file) {
        super(file);
    }

    public TextureRegion getRegion() {
        region.texture.load(region.texture.getTextureData());
        return region;
    }

    public void draw(float x, float y, float width, float height) {
        Draw.rect(getRegion(), x, y, width, height);
    }

    // region paint

    public void iterateSquare(int x, int y, int size, Intc2 intc2) {
        for (int cx = 0; cx < size; cx++)
            for (int cy = 0; cy < size; cy++)
                intc2.get(x + cx - size / 2, y + cy - size / 2);
    }

    public void drawSquare(int x, int y, int brushSize, Color color) {
        iterateSquare(x, y, brushSize, (cx, cy) -> set(cx, cy, color));
    }

    public void drawCircle(int x, int y, int brushSize, Color color) {
        iterateSquare(x, y, brushSize, (cx, cy) -> {
            if (Mathf.within(cx - x, cy - y, brushSize / 2f - 0.5f + 0.0001f))
                set(cx, cy, color);
        });
    }

    // endregion
}