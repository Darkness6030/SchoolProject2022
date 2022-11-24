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

    public LayerCanvas canvas = new LayerCanvas(800, 600);
    public EditType type = pencil, temp = pencil;

    public int drawSize = 2;
    public int lastX = -1, lastY = -1;

    public Editor() {
        input.addProcessor(new GestureDetector(this));
    }

    public void resetCanvas(int width, int height) {
        this.canvas = new LayerCanvas(width, height);
        this.canvas.move(graphics.getWidth() / 2, graphics.getHeight() / 2);
    }

    @Override
    public void update() {
        canvas.scale(input.axis(Binding.zoom) * .02f);
        canvas.clampToScreen(192);

        if (pencil.isSelected()) {
            if (input.keyDown(Binding.pencil1)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, first);
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else if (input.keyDown(Binding.pencil2)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, second);
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else lastX = lastY = none;
        }

        if (pick.isSelected() && input.keyRelease(Binding.pickMouse)) {
            first.set(canvas.pickColor(canvas.mouseX(), canvas.mouseY()));
            ui.colorWheel.add(first);
        }

        if (eraser.isSelected()) {
            if (input.keyDown(Binding.eraser)) {
                Paint.draw(canvas.layer(), lastX, lastY, canvas.mouseX(), canvas.mouseY(), drawSize, Color.clear);
                lastX = canvas.mouseX();
                lastY = canvas.mouseY();
            } else lastX = lastY = none;
        }

        if (input.keyTap(Binding.pick)) {
            temp = type;
            type = EditType.pick;
            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        } else if (ui.colorWheel.shown() && (input.keyRelease(Binding.pick) || input.keyRelease(Binding.pickMouse))) {
            type = temp;
            ui.colorWheel.hide();
        }

        if (input.keyTap(Binding.new_canvas)) ui.canvasDialog.show();

        if (input.keyTap(Binding.new_layer) && canvas.layers.size < max_layers) {
            canvas.addLayer();
            ui.hudFragment.rebuildLayers.run();
        }

        if (input.keyTap(Binding.layer_up))
            canvas.layer(canvas.currentLayer + 1);

        if (input.keyTap(Binding.layer_down))
            canvas.layer(canvas.currentLayer - 1);

        graphics.clear(Color.lightGray);
        canvas.draw();
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(Binding.mouse_move)) canvas.move((int) deltaX, (int) deltaY);
        return false;
    }

    public void save(Fi file) {
        var pixmap = canvas.pixmap();
        PixmapIO.writePng(file, pixmap);
    }

    public void load(Fi file) {

    }
}