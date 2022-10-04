package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;

import static arc.Core.*;

public class Canvas extends Pixmap {

    public final Vec2 position = new Vec2();
    public float scale = 1f;

    private final TextureRegion region = new TextureRegion(new Texture(this));

    public Canvas(int width, int height) {
        super(width, height);

        move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);

        each((x, y) -> { // temp
            set(x, y, Color.purple.cpy().lerp(Color.blue, Mathf.sqr((x + y) / 1400f)));
        });
    }

    public void move(float x, float y) {
        this.position.add(x, y);
    }

    public void clampToScreen(float margin) {
        position.x = Mathf.clamp(position.x, graphics.getWidth() - margin - scaledWidth() / 2f, margin + scaledWidth() / 2f);
        position.y = Mathf.clamp(position.y, graphics.getHeight() - margin - scaledHeight() / 2f, margin + scaledHeight() / 2f);

        if (scaledWidth() < graphics.getWidth() - margin * 2f) position.x = graphics.getWidth() / 2f;
        if (scaledHeight() < graphics.getHeight() - margin * 2f) position.y = graphics.getHeight() / 2f;
    }

    public void scale(float scale) {
        this.scale += scale;
    }

    public float scaledWidth() {
        return getWidth() * scale;
    }

    public float scaledHeight() {
        return getHeight() * scale;
    }

    public float mouseX() {
        return (scaledWidth() / 2f + input.mouseX() - position.x) / scale;
    }

    public float mouseY() {
        return (scaledHeight() / 2f - input.mouseY() + position.y) / scale;
    }

    public Color pickColor(int x, int y) {
        return new Color(get(x, y));
    }

    public void draw() {
        Draw.reset();

        region.texture.load(region.texture.getTextureData());
        Draw.rect(region,
                position.x,
                position.y,
                getWidth() * scale,
                getHeight() * scale);

        Draw.flush();
    }
}