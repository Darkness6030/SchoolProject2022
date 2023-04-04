package dark.editor;

import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import dark.history.RemoveOperation;
import dark.ui.Palette;

import static dark.Main.*;

public class Renderer {

    public static final int background = Color.lightGray.rgba(); // Цвет фона холста

    public static final int border = 4;     // Толщина границы холста
    public static final int maxLayers = 32; // Максимальное количество слоёв

    public final Seq<Layer> layers = new Seq<>();
    public Layer current;

    public void draw(float x, float y, float width, float height) {
        Lines.stroke(border, Palette.main); // Рисуем границу холста
        Lines.rect(x - width / 2f - border, y - height / 2f - border, width + border * 2f, height + border * 2f);

        Draw.color(background); // Рисуем фон
        Fill.rect(x, y, width, height);

        Draw.reset();
        layers.each(layer -> layer.visible, layer -> layer.draw(x, y, width, height)); // Рисуем слои

        Draw.flush();
    }

    public void reset(int width, int height) {
        reset(new Layer(width, height));
    }

    public void reset(Layer layer) {
        layers.each(Layer::dispose);
        layers.clear().add(current = layer);

        ui.hudFragment.updateLayers();
    }

    public void resize(int width, int height, boolean scale, boolean filter, int align) {
        int index = layers.indexOf(current);
        layers.replace(scale ? l -> l.resize(width, height, filter) : l -> l.resize(width, height, align));

        current = layers.get(index);
        ui.hudFragment.updateLayers();
    }

    public void copy(Cons<Pixmap> cons) {
        var pixmap = new Pixmap(current.width, current.height);
        layers.each(layer -> pixmap.draw(layer, true));

        cons.get(pixmap);
        pixmap.dispose();
    }

    public void addLayer(Layer layer) {
        if (!canAdd()) return;

        layers.add(current = layer);
        ui.hudFragment.updateLayers();
    }

    public boolean canAdd() {
        return layers.size < maxLayers;
    }

    public void removeLayer(Layer layer) {
        if (!canRemove()) return;

        int index = layers.indexOf(layer);
        layers.remove(index);

        history.push(new RemoveOperation(layer, index));

        // Был удален текущий слой, перемещаемся на один слой назад
        if (current == layer) current = layers.get(Math.max(index - 1, 0));
        ui.hudFragment.updateLayers();
    }

    public boolean canRemove() {
        return layers.size > 1;
    }

    public void moveLayer(Layer layer, int direction) {
        if (!canMove(layer, direction)) return;

        int index = layers.indexOf(layer);
        layers.swap(index, index + direction);

        ui.hudFragment.updateLayers();
    }

    public boolean canMove(Layer layer, int direction) {
        if (layers.size <= 1) return false;

        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < layers.size;
    }
}