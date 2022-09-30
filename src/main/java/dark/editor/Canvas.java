package dark.editor;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.math.geom.Vec2;

public class Canvas extends FrameBuffer {

    public final Vec2 position = new Vec2();
    public float scale = 1f;

    public Canvas(int width, int height) {
        super(width, height);
    }

    public void move(float x, float y) {
        this.position.add(x, y);
    }

    public void scale(float scale) {
        this.scale += scale;
    }

    public void draw() {
        Draw.reset();

        Draw.rect(new TextureRegion(getTexture()),
                getWidth() / 2f + position.x,
                getHeight() / 2f + position.y,
                getWidth() * scale,
                getHeight() * scale);

        Draw.flush();
    }
}
