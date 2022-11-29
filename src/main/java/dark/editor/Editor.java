package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;
import static dark.Main.ui;
import static dark.editor.EditType.*;

public class Editor implements ApplicationListener, GestureListener {

    public static final int none = -2147483648, max_layers = 7;

    public final Color first = Color.white.cpy(), second = Color.black.cpy();

    public Canvas canvas = new Canvas(800, 600);
    public EditType type = pencil, temp = pencil;

    public int drawSize = 2;
    public int lastX = -1, lastY = -1;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    public void resetCanvas(int width, int height) {
        this.canvas = new Canvas(width, height);
        this.canvas.move(graphics.getWidth() / 2, graphics.getHeight() / 2);
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192);

        if (pencil.isSelected()) {
            if (input.keyDown(Binding.draw1)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, first.rgba());
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else if (input.keyDown(Binding.draw2)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, second.rgba());
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else lastX = lastY = none;
        }

        if (eraser.isSelected()) {
            if (input.keyDown(Binding.draw1)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, Color.clearRgba);
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else lastX = lastY = none;
        }

        if (pick.isSelected() && input.keyRelease(Binding.draw1)) {
            first.set(canvas.pickColor(canvas.mouseX(), canvas.mouseY()));
            ui.colorWheel.add(first);
        }

        if (fill.isSelected()) {
            if (input.keyDown(Binding.draw1))
                Paint.fill(canvas.layer(), canvas.mouseX(), canvas.mouseY(), first.rgba());
            else if (input.keyDown(Binding.draw2))
                Paint.fill(canvas.layer(), canvas.mouseX(), canvas.mouseY(), second.rgba());
        }

        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        } else if (ui.colorWheel.shown() && (input.keyRelease(Binding.pick) || input.keyRelease(Binding.draw1))) {
            type = temp;
            ui.colorWheel.hide();
        }

        if (input.keyTap(Binding.new_canvas)) ui.canvasDialog.show();

        if (input.keyTap(Binding.new_layer) && canvas.layers.size < max_layers) {
            canvas.addLayer();
        }

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move((int) deltaX, (int) deltaY);
        return false;
    }

    public void save(Fi file) {
        var pixmap = canvas.toPixmap();
        PixmapIO.writePng(file, pixmap);
    }

    public void load(Fi file) {

    }
}