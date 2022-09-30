package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;

public class Editor implements ApplicationListener, GestureListener {

    public Canvas canvas = new Canvas(800, 600); // temp
    public EditType type = EditType.pencil;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .01f);

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move(deltaX, deltaY);
        return false;
    }
}
