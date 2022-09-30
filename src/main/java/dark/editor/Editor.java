package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.input.KeyCode;
import arc.math.geom.Vec2;

import static arc.Core.*;

public class Editor implements ApplicationListener {

    public static final Vec2 camera = new Vec2();

    public FrameBuffer buffer = new FrameBuffer(800, 600); // temp
    public EditType type = EditType.pencil;

    @Override
    public void update() {
        updateInput();
        draw();
    }

    public void updateInput() { // temp
        if (input.keyDown(KeyCode.d)) camera.x += 1f;
        else if (input.keyDown(KeyCode.a)) camera.x -= 1f;
        if (input.keyDown(KeyCode.w)) camera.y += 1f;
        else if (input.keyDown(KeyCode.s)) camera.y -= 1f;
    }

    public void draw() {
        graphics.clear(Color.lightGray);

        Draw.reset();

        Draw.color(Color.black);
        Fill.crect(-camera.x - 4f, -camera.y - 4f, buffer.getWidth() + 8f, buffer.getHeight() + 8f);
        Draw.color(Color.white);
        Draw.rect(new TextureRegion(buffer.getTexture()), buffer.getWidth() / 2 - camera.x, buffer.getHeight() / 2 - camera.y);

        Draw.flush();
    }
}
