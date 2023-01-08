package dark.editor;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import dark.ui.Palette;

import static dark.Main.ui;

public class Renderer {

    public static final float border = 4f; // Толщина границы холста

    public final Seq<Layer> layers = new Seq<>();
    public Layer current, background;

    public void draw(float x, float y, float width, float height) {
        Lines.stroke(border, Palette.main); // Рисуем границу холста
        Lines.rect(x - width / 2f - border, y - height / 2f - border, width + border * 2f, height + border * 2f);

        Draw.reset();

        background.draw(x, y, width, height); // Рисуем фон
        layers.each(layer -> layer.draw(x, y, width, height)); // Рисуем слои

        Draw.flush();
    }

    public void reset(int width, int height) {
        reset(new Layer(width, height));
    }

    public void reset(Layer layer) {
        layers.each(Layer::dispose);
        layers.clear();

        layers.add(current = layer);

        background = new Layer(layer.width, layer.height);
        background.fill(Color.lightGray);

        ui.hudFragment.rebuildLayers();
    }

    public void draw(Pixmap pixmap) {
        layers.each(layer -> pixmap.draw(layer, true));
    }

    public void addLayer(Layer layer) {
        layers.add(current = layer);
        ui.hudFragment.rebuildLayers();
    }

    public void removeLayer(Layer layer) {
        if (!canRemove()) return;

        int index = layers.indexOf(layer);
        layers.remove(index);

        layer.dispose(); // Освобождаем ресурсы, связанные со слоем

        // Был удален текущий слой, перемещаемся на один слой назад
        if (current == layer) current = layers.get(Math.max(index - 1, 0));
        ui.hudFragment.rebuildLayers();
    }

    public boolean canRemove() {
        return layers.size > 1;
    }

    public void moveLayer(Layer layer, int direction) {
        if (!canMove(layer, direction)) return;

        int index = layers.indexOf(layer);
        layers.swap(index, index + direction);

        ui.hudFragment.rebuildLayers();
    }

    public boolean canMove(Layer layer, int direction) {
        if (layers.size <= 1) return false;

        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < layers.size;
    }
}