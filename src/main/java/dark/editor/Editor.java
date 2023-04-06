package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.util.Tmp;
import dark.ui.*;
import dark.utils.Clipboard;

import java.awt.image.BufferedImage;

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