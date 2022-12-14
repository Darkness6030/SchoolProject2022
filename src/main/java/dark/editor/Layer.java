package dark.editor;

import arc.files.Fi;
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

    public void fillSquare(int x, int y, int brushSize, Color color) {
        fillRect(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize, color.rgba());
    }

    public void fillCircle(int x, int y, int brushSize, Color color) {
        fillCircle(x, y, Mathf.ceil(brushSize / 2f), color.rgba());
    }

    // endregion
}