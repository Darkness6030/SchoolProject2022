package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;
import static dark.Main.ui;

public class Editor implements ApplicationListener, GestureListener {

    /** Primary and secondary color. */
    public final Color first = Color.white.cpy(), second = Color.black.cpy();
    public Canvas canvas = new Canvas(800, 600); // temp
    public EditType type = EditType.pencil, temp = EditType.pencil;
    public int drawSize = 2; // TODO temp move to EditType cuz pick tool have not size
    /** Last known mouse position. */
    public int lastX = -1, lastY = -1;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192f);

        if (type == EditType.pencil && input.keyDown(Binding.draw) && !scene.hasMouse()) {
            if (lastX > 0 && lastY > 0)
                canvas.draw(first, () -> Paint.line(lastX, lastY, canvas.canvasX(), canvas.canvasY(), drawSize));
            lastX = canvas.canvasX();
            lastY = canvas.canvasY();
        } else lastX = lastY = -1;

        if (type == EditType.pick && !scene.hasMouse()) { // TODO may be call some method in EditType?
            if (input.keyRelease(Binding.draw)) {
                first.set(canvas.pickColor(canvas.canvasX(), canvas.canvasY()));
                ui.colorWheel.add(first);
            }
        }

        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        } else if (ui.colorWheel.shown() && (input.keyRelease(Binding.pick) || input.keyRelease(Binding.draw))) {
            type = temp;
            ui.colorWheel.hide();
        }

        if (input.ctrl() && input.keyTap(Binding.new_canvas)) ui.canvasDialog.show();

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move(deltaX, deltaY);
        return false;
    }
}