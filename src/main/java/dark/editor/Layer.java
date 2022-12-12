package dark.editor;

import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;

public class Layer extends Pixmap {

    public final TextureRegion region = new TextureRegion(new Texture(this));

    public Layer(int width, int height) {
        super(width, height);
    }

    public TextureRegion getRegion() {
        region.texture.load(region.texture.getTextureData());
        return region;
    }

    public void draw(float x, float y, float width, float height) {
        Draw.rect(getRegion(), x, y, width, height);
    }

    public int pickColor(int x, int y) {
        return getRaw(x, y);
    }
}