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

    /** Primary and secondary color. */
    private final Color first = Color.white.cpy(), second = Color.black.cpy();
    /** Used to save the edit mode during color selection with ctrl. */
    private EditType temp = EditType.pencil;

    public Editor() {
        input.addProcessor(new GestureDetector(this));

        canvas.begin(Color.white); // temp

        for (float i = 0; i < 600f; i++) {
            Lines.stroke(2f, Color.purple.cpy().lerp(Color.blue, i / 600f));
            Lines.rect(i * 1.25f, i, 800f, 600f);
        }

        canvas.end();
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192f);

        if (input.keyDown(Binding.draw)) { // TODO may be call some method in EditType?
            if (type == EditType.pick) first.set(canvas.pickColor((int) canvas.mouseX(), (int) canvas.mouseY()));
            else {
                canvas.beginBind();

                Draw.color(first);
                Fill.rect(canvas.mouseX(), canvas.mouseY(), 3f, 3f);

                canvas.end();
            }
        }

        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
        } else if (input.keyRelease(Binding.pick)) type = temp;

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move(deltaX, deltaY);
        return false;
    }
}
