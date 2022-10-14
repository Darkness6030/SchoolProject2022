package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;
import static dark.Main.ui;

public class Editor implements ApplicationListener, GestureListener {

    public Canvas canvas = new Canvas(800, 600); // temp
    public EditType type = EditType.pencil;

    public int drawSize = 2; // TODO temp move to EditType cuz pick tool have not size

    /** Primary and secondary color. */
    private final Color first = Color.white.cpy(); //second = Color.black.cpy();
    /** Used to save the edit mode during color selection with ctrl. */
    private EditType temp = EditType.pencil;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192f);

        if (type == EditType.pencil && !scene.hasMouse()) {
            if (input.keyDown(Binding.draw)) {
                canvas.fillCircle((int) canvas.mouseX(), (int) canvas.mouseY(), drawSize, first.rgba8888());
            }
        }

        if (type == EditType.pick && !scene.hasMouse()) { // TODO may be call some method in EditType?
            if (input.keyRelease(Binding.draw)) {
                first.set(canvas.pickColor((int) canvas.mouseX(), (int) canvas.mouseY()));
                ui.wheelfrag.add(first);
            }
        }

        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
            ui.wheelfrag.show(input.mouseX(), input.mouseY(), first::set);
        } else if (ui.wheelfrag.shown() && (input.keyRelease(Binding.pick) || input.keyRelease(Binding.draw))) {
            type = temp;
            ui.wheelfrag.hide();
        }

        if (input.ctrl() && input.keyTap(Binding.new_canvas)) canvas = canvas.width == 800 ? new Canvas(600, 800) : new Canvas(800, 600);

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move(deltaX, deltaY);
        return false;
    }
}