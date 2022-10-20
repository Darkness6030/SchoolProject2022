package dark.editor;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.ScreenUtils;

import static arc.Core.*;

public class Canvas extends FrameBuffer {

    public final Vec2 position = new Vec2();
    public float scale = 1f;

    public Canvas(int width, int height) {
        super(width, height);

        move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);

        begin(Color.purple);
        end();
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

    public int scaledWidth() {
        return (int) (getWidth() * scale);
    }

    public int scaledHeight() {
        return (int) (getHeight() * scale);
    }

    public int bottomLeftX() {
        return (int) position.x - scaledWidth() / 2;
    }

    public int bottomLeftY() {
        return (int) position.y - scaledHeight() / 2;
    }

    public int canvasX(int screenX) {
        return (int) ((scaledWidth() / 2f + screenX - position.x) / scale);
    }

    public int canvasY(int screenY) {
        return (int) ((scaledHeight() / 2f - screenY + position.y) / scale);
    }

    public int canvasX() {
        return canvasX(input.mouseX());
    }

    public int canvasY() {
        return canvasY(input.mouseY());
    }

    public Pixmap toPixmap() {
        return ScreenUtils.getFrameBufferPixmap(bottomLeftX(), bottomLeftY(), scaledWidth(), scaledHeight());
    }

    public Color pickColor(int x, int y) {
        return new Color(toPixmap().get(x, y));
    }

    public void draw(Color color, Runnable runnable) {
        beginBind();
        Draw.color(color);
        runnable.run();
        end();
    }

    public void draw() {
        Draw.reset();

        Draw.rect(new TextureRegion(getTexture()),
                position.x,
                position.y,
                scaledWidth(),
                scaledHeight());

        Draw.flush();
    }
}