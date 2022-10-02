package dark.editor;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import arc.math.geom.Vec2;

import static arc.Core.*;

public class Canvas extends FrameBuffer {

    public final Vec2 position = new Vec2();
    public float scale = 1f;

    private float swidth, sheight;

    public Canvas(int width, int height) {
        super(width, height);

        move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
        scale(0f); // cache scaled width and height
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

    public Color pickColor(int x, int y) { // TODO throws ArcRuntimeException: This TextureData implementation does not return a Pixmap
        return new Color(getTexture().getTextureData().getPixmap().get(x, y));
    }

    public void draw() {
        Draw.reset();

        Draw.rect(new TextureRegion(getTexture()),
                position.x,
                position.y,
                getWidth() * scale,
                getHeight() * scale);

        Draw.flush();
    }
}
