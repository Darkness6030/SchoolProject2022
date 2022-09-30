package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.math.geom.Vec2;

import static arc.Core.*;

public class Editor implements ApplicationListener, GestureListener {

    public static final Vec2 camera = new Vec2();

    public FrameBuffer buffer = new FrameBuffer(800, 600); // temp
    public EditType type = EditType.pencil;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    @Override
    public void update() {
        updateInput();
        draw();
    }

    public void updateInput() { // temp
        camera.add(
                (input.keyDown(Binding.move_x.min) ? 2 : 0) + (input.keyDown(Binding.move_x.max) ? -2 : 0),
                (input.keyDown(Binding.move_y.min) ? 2 : 0) + (input.keyDown(Binding.move_y.max) ? -2 : 0)
        );
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) camera.add(-deltaX, -deltaY);
        return false;
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
