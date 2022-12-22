package dark.editor;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import dark.ui.Palette;

import static dark.Main.*;

public class Renderer {

    public static final int maxLayers = 6;

    public final Seq<Layer> layers = new Seq<>();
    public Layer current, background, overlay;

    public Renderer(int width, int height) {
        addLayer(current = new Layer(width, height));
        init(width, height);
    }

    public Renderer(Layer layer) {
        addLayer(current = layer);
        init(layer.width, layer.height);
    }

    public void init(int width, int height) {
        background = new Layer(width, height);
        background.fill(Color.lightGray); // TODO выбор цвета фона

        overlay = new Layer(width, height);
    }

    public void draw(float x, float y, float width, float height) {
        Lines.stroke(4f, Palette.main); // Рисуем границу холста
        Lines.rect(x - width / 2f - 4f, y - height / 2f - 4f, width + 8f, height + 8f);
        Draw.color(); // Сбрасываем цвет

        background.draw(x, y, width, height); // Рисуем фон

        for (int i = layers.size - 1; i >= 0; i--)
            layers.get(i).draw(x, y, width, height); // Рисуем слои в обратном порядке

        overlay.fill(0);
        editor.drawOverlay();
        overlay.draw(x, y, width, height); // Рисуем покрытие

        Draw.flush();
    }

    public void draw(Pixmap pixmap) {
        layers.each(layer -> layer.each((x, y) -> {
            if (layer.get(x, y) != 0)
                pixmap.set(x, y, layer.get(x, y));
        }));
    }

    public void addLayer(Layer layer) {
        if (!canAdd()) return;

        layers.add(current = layer);

        if (ui != null) ui.hudFragment.rebuildLayers();
    }

    public boolean canAdd() {
        return layers.size < maxLayers;
    }

    public void removeLayer(Layer layer) {
        if (!canRemove()) return;

        int index = layers.indexOf(layer);
        if (current == layer) current = layers.get(index - 1);
        layers.remove(index);

        if (ui != null) ui.hudFragment.rebuildLayers();
    }

    public boolean canRemove() {
        return layers.size > 1;
    }

    public void moveLayer(Layer layer, int direction) {
        if (!canMove(layer, direction)) return;

        int index = layers.indexOf(layer);
        layers.swap(index, index + direction);

        if (ui != null) ui.hudFragment.rebuildLayers();
    }

    public boolean canMove(Layer layer, int direction) {
        if (layers.size <= 1) return false;

        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < layers.size;
    }
}