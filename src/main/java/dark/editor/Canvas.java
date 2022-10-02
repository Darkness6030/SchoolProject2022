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

    public Canvas(int width, int height) {
        super(width, height);
        move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
    }

    public void move(float x, float y) {
        this.position.add(x, y);
    }

    public void clampToScreen(float margin) {
        final float scaledWidth = getWidth() * scale, scaledHeight = getHeight() * scale;

        position.x = Mathf.clamp(position.x, graphics.getWidth() - margin - scaledWidth / 2f, margin + scaledWidth / 2f);
        position.y = Mathf.clamp(position.y, graphics.getHeight() - margin - scaledHeight / 2f, margin + scaledHeight / 2f);

        if (scaledWidth < graphics.getWidth() - margin * 2f) position.x = graphics.getWidth() / 2f;
        if (scaledHeight < graphics.getHeight() - margin * 2f) position.y = graphics.getHeight() / 2f;
    }

    public void scale(float scale) {
        this.scale += scale;
    }

    public float mouseX() {
        return input.mouseX() - position.x;
    }

    public float mouseY() {
        return getHeight() - input.mouseY() + position.y;
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
