package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.math.geom.Bresenham2;
import arc.util.Tmp;
import dark.history.Operation;
import dark.ui.*;
import dark.utils.Clipboard;

import java.awt.image.BufferedImage;

import static arc.Core.*;
import static dark.Main.*;

public class Editor implements ApplicationListener, GestureListener {

    public int mouseX, mouseY, canvasX, canvasY;

    public Renderer renderer = new Renderer();
    public Canvas canvas = new Canvas();

    public EditTool tool = EditTool.pencil, temp;
    public Color first = Color.white.cpy(), second = Color.black.cpy();
    public Operation operation;

    @Override
    public void init() {
        input.addProcessor(new GestureDetector(this));
        reset(800, 600);
    }

    @Override
    public void update() {
        if (!scene.hasDialog()) {
            if (!scene.hasKeyboard())
                canvas.move(Binding.move_x.axis() * canvas.zoom * -8f, Binding.move_y.axis() * canvas.zoom * -8f);
            if (!scene.hasScroll()) canvas.zoom(Binding.zoom.scroll() * canvas.zoom * .05f);

            input();
        }

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();

        graphics.clear(Palette.darkmain);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
    }

    @Override
    public void resize(int width, int height) {
        canvas.set(width / 2f, height / 2f);
    }

    public void input() {
        if (scene.hasKeyboard()) return;

        if (!scene.hasMouse()) {
            if ((Binding.draw1.down() || Binding.draw2.down()))
                begin();

            draw(Binding.draw1, first);
            draw(Binding.draw2, second);
        }

        if (((Binding.draw1.release() && !Binding.draw2.down()) || (Binding.draw2.release() && !Binding.draw1.down())))
            flush();

        if (Binding.pan.down())
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);

        for (var tool : EditTool.values())
            if (tool.hotkey.tap()) {
                this.tool = tool;
                ui.hudFragment.updateConfig();
            }

        if (Binding.swap.tap()) swap();

        if (Binding.resize_canvas.tap()) ui.resize.show();
        if (Binding.new_canvas.tap()) ui.newCanvas.show();
        if (Binding.new_layer.tap()) newLayer();

        if (Binding.wheel.tap() && temp == null) {
            temp = tool;
            tool = EditTool.pick;

            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        }

        if ((Binding.wheel.release() || Binding.draw1.release()) && temp != null) {
            tool = temp;
            temp = null;

            ui.colorWheel.hide();
        }

        if (Binding.copy.tap()) copy();
        if (Binding.paste.tap()) paste();
        if (Binding.undo.tap() && history.hasUndo()) undo();
        if (Binding.redo.tap() && history.hasRedo()) redo();
    }

    // region actions

    public void begin() {
        if (operation != null) return;

        operation = new Operation(tool, renderer.current);
        operation.begin();
    }

    public void flush() {
        if (operation == null) return;

        operation.end();
        if (!operation.data.isEmpty()) history.push(operation);
        operation = null;
    }

    public void draw(Binding binding, Color color) {
        if (tool.draggable && binding.down())
            Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(renderer.current, x, y, color));
        else if (binding.tap())
            tool.touched(renderer.current, canvas.canvasX(), canvas.canvasY(), color);
    }

    public void reset(int width, int height) {
        history.clear();

        renderer.reset(width, height);
        canvas.reset(width, height);
    }

    public void reset(Layer layer) {
        history.clear();

        renderer.reset(layer);
        canvas.reset(layer.width, layer.height);
    }

    public void undo() {
        history.undo();
        ui.showInfoToast(Icons.undo, "@undone");
    }

    public void redo() {
        history.redo();
        ui.showInfoToast(Icons.redo, "@redone");
    }

    public void copy() {
        try {
            renderer.copy(pixmap -> {
                var image = new BufferedImage(pixmap.width, pixmap.height, BufferedImage.TYPE_INT_RGB);
                pixmap.each((x, y) -> image.setRGB(x, y, Tmp.c1.set(pixmap.get(x, y)).argb8888()));

                Clipboard.copy(image);
                ui.showInfoToast(Icons.copy, "@copied");
            });
        } catch (Exception e) {
            // ui.showException("Failed to copy", e);
        }
    }

    public void paste() {
        try {
            Clipboard.paste(image -> {
                var layer = new Layer(image.getWidth(), image.getHeight());
                layer.each((x, y) -> layer.set(x, y, Tmp.c1.argb8888(image.getRGB(x, y)).rgba8888()));

                reset(layer);
                ui.showInfoToast(Icons.paste, "@pasted");
            });
        } catch (Exception e) {
            // ui.showException("Failed to paste", e);
        }
    }

    public void save(Fi file) {
        try {
            renderer.copy(pixmap -> PixmapIO.writePng(file, pixmap));

            ui.showInfoToast(Icons.save, bundle.format("saved", file.name()));
            ui.menu.hide();
        } catch (Exception e) {
            // ui.showException("Failed to save", e);
        }
    }

    public void load(Fi file) {
        try {
            reset(new Layer(file));

            ui.showInfoToast(Icons.load, bundle.format("loaded", file.name()));
            ui.menu.hide();
        } catch (Exception e) {
            // ui.showException("Failed to load", e);
        }
    }

    // endregion
    // region tools

    public void moveUp() {
        renderer.moveLayer(renderer.current, 1);
    }

    public boolean canMoveUp() {
        return renderer.canMove(renderer.current, 1);
    }

    public void moveDown() {
        renderer.moveLayer(renderer.current, -1);
    }

    public boolean canMoveDown() {
        return renderer.canMove(renderer.current, -1);
    }

    public void newLayer() {
        renderer.addLayer(new Layer(canvas.width, canvas.height));
    }

    public void copyLayer() {
        renderer.addLayer(renderer.current.copyLayer());
    }

    public void removeLayer() {
        renderer.removeLayer(renderer.current);
    }

    public void swap() {
        Tmp.c1.set(first);
        first.set(second);
        second.set(Tmp.c1);

        ui.showInfoToast(Icons.swap, "@swapped");
    }

    // endregion
}