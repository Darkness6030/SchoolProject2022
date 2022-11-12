package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.Draw;

import static arc.util.Tmp.tr1;

public class Layer extends Pixmap {

    public final Texture texture = new Texture(this);

    public float scale = 1f;

    public Layer(int width, int height) {
        super(width, height);
    }

    public void scale(float scale) {
        this.scale += scale;
    }

    public int scaledWidth() {
        return (int) (width * scale);
    }

    public int scaledHeight() {
        return (int) (height * scale);
    }

    public void draw(int x, int y, int width, int height) {
        texture.load(texture.getTextureData());
        tr1.set(texture);

        Draw.rect(tr1, x, y, width, height);
    }

    public int pickColor(int x, int y) {
        return getRaw(x, y);
    }
}