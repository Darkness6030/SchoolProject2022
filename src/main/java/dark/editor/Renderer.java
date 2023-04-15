package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import arc.util.Time;
import dark.history.*;
import dark.ui.*;

import static dark.Main.*;

public class Renderer {

    public static final int background = Color.lightGray.rgba(); // Цвет фона холста

    public static final int border = 4;     // Толщина границы холста
    public static final int maxLayers = 32; // Максимальное количество слоёв

    public final Seq<Layer> layers = new Seq<>();
    public Layer current, overlay;

    public void draw(float x, float y, float width, float height) {
        Lines.stroke(border, Palette.main); // Рисуем границу холста
        Lines.rect(x - width / 2f - border, y - height / 2f - border, width + border * 2f, height + border * 2f);

        Draw.color(background); // Рисуем фон
        Fill.rect(x, y, width, height);

        Draw.reset();

        layers.each(layer -> layer.changed, Layer::unchange);
        layers.each(layer -> layer.visible, layer -> layer.draw(x, y, width, height)); // Рисуем слои

        Time.update();
        Shaders.render(() -> overlay.draw(x, y, width, height), Shaders.overlay);

        Draw.flush();
    }

    public void reset(int width, int height) {
        reset(new Layer(width, height));
    }

    public void reset(Layer layer) {
        layers.each(Layer::dispose);
        layers.clear().add(layer);

        current = layer;
        overlay = new Layer(layer.width, layer.height);

        ui.hudFragment.updateLayers();
    }

    public void resize(int width, int height, boolean scale, boolean filter, int align) {
        int index = layers.indexOf(current);
        layers.replace(scale ? layer -> layer.resize(width, height, filter) : layer -> layer.resize(width, height, align));

        current = layers.get(index);
        overlay = new Layer(width, height);

        ui.hudFragment.updateLayers();
    }

    public Pixmap copy() {
        var pixmap = new Pixmap(current.width, current.height);
        layers.each(layer -> pixmap.draw(layer, true));

        return pixmap;
    }

    public void addLayer(Layer layer, boolean push) {
        if (!canAdd()) return;

        layers.add(current = layer);
        ui.hudFragment.updateLayers();

        if (push)
            history.push(new CreateOperation(layer, layers.size - 1));
    }

    public boolean canAdd() {
        return layers.size < maxLayers;
    }

    public void removeLayer(Layer layer, boolean push) {
        if (!canRemove()) return;

        int index = layers.indexOf(layer);
        layers.remove(index);

        // Был удален текущий слой, перемещаемся на один слой назад
        if (current == layer) current = layers.get(Math.max(index - 1, 0));
        ui.hudFragment.updateLayers();

        if (push)
            history.push(new RemoveOperation(layer, index));
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