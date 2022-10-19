package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.graphics.g2d.Lines;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.input.InputProcessor;

import static arc.Core.*;
import static dark.Main.ui;

public class Editor implements ApplicationListener, GestureListener, InputProcessor {

    public Canvas canvas = new Canvas(800, 600); // temp
    public EditType type = EditType.pencil, temp = EditType.pencil;

    public int drawSize = 2; // TODO temp move to EditType cuz pick tool have not size
    public int lastX = -1, lastY = -1;

    /** Primary and secondary color. */
    public final Color first = Color.white.cpy(), second = Color.black.cpy();

    public Editor() {
        input.addProcessor(new GestureDetector(this));
        input.addProcessor(this);
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192f);

        if (type == EditType.pick && !scene.hasMouse()) { // TODO may be call some method in EditType?
            if (input.keyRelease(Binding.draw)) {
                first.set(canvas.pickColor(canvas.canvasX(input.mouseX()), canvas.canvasY(input.mouseY())));
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

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (type == EditType.pencil && input.keyDown(Binding.draw) && !scene.hasMouse()) {
            if (lastX > 0 && lastY > 0)
                canvas.draw(first, () -> Lines.line(canvas.canvasX(lastX), canvas.canvasY(lastY), canvas.canvasX(screenX), canvas.canvasY(screenY), false));
            lastX = screenX;
            lastY = screenY;
            return true;
        }

        lastX = lastY = -1;
        return true;
    }
}