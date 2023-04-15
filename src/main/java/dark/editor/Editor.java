package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.util.Log;
import arc.util.Tmp;
import dark.ui.*;
import dark.utils.*;

import static arc.Core.*;
import static dark.Main.*;

public class Editor implements ApplicationListener, GestureListener {

    public Renderer renderer = new Renderer();
    public Color first = Color.white.cpy(), second = Color.black.cpy();

    @Override
    public void init() {
        input.addProcessor(new GestureDetector(this));
        reset(800, 600);
    }

    @Override
    public void update() {
        graphics.clear(Palette.darkmain);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
    }

    @Override
    public void resize(int width, int height) {
        canvas.set(width / 2f, height / 2f);
    }

    // region actions

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

    public void resize(int width, int height, boolean scale, boolean filter, int align) {
        // TODO добавить в историю элемент ресайза, чтобы потом не мучаться со скейлом истории

        renderer.resize(width, height, scale, filter, align);
        canvas.resize(width, height);
    }

    public void undo() {
        handler.flush();
        history.undo();
        ui.showInfoToast(Icons.undo, "@undone");
    }

    public void redo() {
        if (handler.operation != null) return;
        history.redo();
        ui.showInfoToast(Icons.redo, "@redone");
    }

    public void copy() {
        try {
            var pixmap = renderer.copy();

            Clipboard.copy(pixmap);
            pixmap.dispose();

            ui.showInfoToast(Icons.copy, "@copied");
        } catch (Exception e) {
            // ui.showException("Failed to copy", e);
        }
    }

    public void paste() {
        try {
            var pixmap = Clipboard.paste();
            if (pixmap == null) return;

            reset(new Layer(pixmap));
            pixmap.dispose();

            ui.showInfoToast(Icons.copy, "@pasted");
        } catch (Exception e) {
            // ui.showException("Failed to paste", e);
        }
    }

    public void save(Fi file) {
        try {
            Files.write(file);

            ui.showInfoToast(Icons.save, bundle.format("saved", file.name()));
            ui.menu.hide();
        } catch (Exception e) {
            Log.err(e);
            // ui.showException("Failed to save", e);
        }
    }

    public void load(Fi file) {
        try {
            Files.read(file);

            ui.showInfoToast(Icons.load, bundle.format("loaded", file.name()));
            ui.menu.hide();
        } catch (Exception e) {
            Log.err(e);
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
        renderer.addLayer(new Layer(canvas.width, canvas.height), true);
    }

    public void copyLayer() {
        renderer.addLayer(renderer.current.copyLayer(), true);
    }

    public void removeLayer() {
        renderer.removeLayer(renderer.current, true);
    }

    public void swap() {
        Tmp.c1.set(first);
        first.set(second);
        second.set(Tmp.c1);

        ui.showInfoToast(Icons.swap, "@swapped");
    }

    // endregion
}