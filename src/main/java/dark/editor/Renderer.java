package dark.editor;

import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Geometry;
import arc.struct.Seq;
import dark.ui.Palette;

import static dark.Main.editor;

public class Renderer {

    public static final int maxLayers = 6;

    public final Seq<Layer> layers = new Seq<>();
    public Layer current;

    public void draw(float x, float y, float width, float height) {
        Lines.stroke(4f, Palette.main);
        Lines.rect(x - width / 2f - 4f, y - height / 2f - 4f, width + 8f, height + 8f);

        Draw.color();
        for (int i = layers.size - 1; i >= 0; i--)
            layers.get(i).draw(x, y, width, height);

        Draw.flush();
    }

    public void drawMouse() {
        Draw.color(Palette.active);

        float x = editor.canvas.mouseX();
        float y = editor.canvas.mouseY();

        if (editor.square) {
            x += editor.brushSize % 2 * editor.canvas.zoom / 2f;
            y -= editor.brushSize % 2 * editor.canvas.zoom / 2f;

            Lines.square(x, y, editor.canvas.zoom * editor.brushSize / 2f);
        } else {
            Lines.poly(Geometry.pixelCircle(editor.brushSize), x, y - editor.canvas.zoom, editor.canvas.zoom);
        }
    }

    public void draw(Pixmap pixmap) {
        // TODO это костыль, оптимизировать?
        layers.each(layer -> layer.each((x, y) -> {
            if (layer.get(x, y) != 0) {
                pixmap.set(x, y, layer.get(x, y));
            }
        }));
    }

    public void addLayer(Layer layer) {
        if (!canAdd()) return;

        layers.add(current = layer);
    }

    public boolean canAdd() {
        return layers.size < maxLayers;
    }

    public void removeLayer(Layer layer) {
        if (!canRemove()) return;

        if (current == layer) current = layers.first();
        layers.remove(layer);
    }

    public boolean canRemove() {
        return layers.size > 1;
    }

    public void moveLayer(Layer layer, int direction) {
        if (!canMove(layer, direction)) return;

        if (current == layer) current = layers.first();
        int index = layers.indexOf(layer);
        layers.swap(index, index + direction);
    }

    public boolean canMove(Layer layer, int direction) {
        if (layers.size <= 1) return false;

        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < layers.size;
    }
}