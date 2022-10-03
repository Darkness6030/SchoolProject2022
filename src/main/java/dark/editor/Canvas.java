package dark.editor;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;

import static arc.Core.*;

public class Canvas extends Pixmap {

    public final Vec2 position = new Vec2();
    public float scale = 1f;

    private float swidth, sheight;
    private Texture texture = new Texture(this);

    public Canvas(int width, int height) {
        super(width, height);

        move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
        scale(0f); // cache scaled width and height

        each((x, y) -> { // temp
            set(x, y, Color.purple.cpy().lerp(Color.blue, Mathf.sqr((x + y) / 1400f)));
        });
    }

    public void move(float x, float y) {
        this.position.add(x, y);
    }

    public void clampToScreen(float margin) {
        position.x = Mathf.clamp(position.x, graphics.getWidth() - margin - swidth / 2f, margin + swidth / 2f);
        position.y = Mathf.clamp(position.y, graphics.getHeight() - margin - sheight / 2f, margin + sheight / 2f);

        if (swidth < graphics.getWidth() - margin * 2f) position.x = graphics.getWidth() / 2f;
        if (sheight < graphics.getHeight() - margin * 2f) position.y = graphics.getHeight() / 2f;
    }

    public void scale(float scale) {
        this.scale += scale;
        swidth = getWidth() * this.scale;
        sheight = getHeight() * this.scale;
    }

    public float scaledWidth() {
        return swidth;
    }

    public float scaledHeight() {
        return sheight;
    }

    public float mouseX() {
        return (swidth / 2f + input.mouseX() - position.x) / scale;
    }

    public float mouseY() {
        return (sheight / 2f - input.mouseY() + position.y) / scale;
    }

    public Color pickColor(int x, int y) {
        return new Color(get(x, y));
    }

    public void draw() {
        Draw.reset();

        texture.load(texture.getTextureData());
        Draw.rect(new TextureRegion(texture),
                position.x,
                position.y,
                getWidth() * scale,
                getHeight() * scale);

        Draw.flush();
    }
}
