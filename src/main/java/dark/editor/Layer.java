package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;

import static arc.util.Tmp.tr1;

public class Layer extends Pixmap {

    public final TextureRegion region = new TextureRegion(new Texture(this));

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

    public TextureRegion getRegion() {
        region.texture.load(region.texture.getTextureData());
        return region;
    }

    public void draw(int x, int y, int width, int height) {
        Draw.rect(getRegion(), x, y, width, height);
    }

    public int pickColor(int x, int y) {
        return getRaw(x, y);
    }
}