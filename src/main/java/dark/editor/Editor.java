package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;
import static dark.Main.ui;

public class Editor implements ApplicationListener, GestureListener {

    public static final int none = Integer.MIN_VALUE;

    /** Primary and secondary color. */
    public final Color first = Color.white.cpy(), second = Color.black.cpy();

    public LayerCanvas canvas = new LayerCanvas(800, 600); // temp
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
        canvas.clampToScreen(192);

        if (type.isTapped()) {
            switch (type) {
                case pencil -> {
                    if (lastX != none && lastY != none)
                        Paint.line(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, first);
                    else
                        Paint.circle(canvas.layer(), canvas.mouseX(), canvas.mouseY(), drawSize, first);

                    lastX = canvas.mouseX();
                    lastY = canvas.mouseY();
                }
                case pick -> {
                    if (input.keyRelease(Binding.pencil)) {
                        first.set(canvas.pickColor(canvas.mouseX(), canvas.mouseY()));
                        ui.colorWheel.add(first);
                    }
                }
                case eraser -> {
                    if (lastX != none && lastY != none)
                        Paint.line(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, Color.clear);
                    else
                        Paint.circle(canvas.layer(), canvas.mouseX(), canvas.mouseY(), drawSize, Color.clear);

                    lastX = canvas.mouseX();
                    lastY = canvas.mouseY();
                }
            }
        } else lastX = lastY = none;


        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        } else if (ui.colorWheel.shown() && (input.keyRelease(Binding.pick) || input.keyRelease(Binding.pencil))) {
            type = temp;
            ui.colorWheel.hide();
        }

        if (input.ctrl() && input.keyTap(Binding.new_canvas)) ui.canvasDialog.show();

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move((int) deltaX, (int) deltaY);
        return false;
    }
}