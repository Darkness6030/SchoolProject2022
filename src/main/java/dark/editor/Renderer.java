package dark.editor;

import arc.graphics.g2d.Draw;
import arc.struct.Seq;

public class Renderer {

    public static final int maxLayers = 7;

    public final Seq<Layer> layers = new Seq<>();
    public Layer current;

    public Renderer(int width, int height) {
        this.addLayer(width, height);
    }

    public void draw(float x, float y, float width, float height) {
        Draw.reset();
        layers.each(layer -> layer.draw(x, y, width, height));
        Draw.flush();
    }

    public void addLayer(int width, int height) {
        if (!canAdd()) return;

        current = layers.add(new Layer(width, height)).peek();
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
        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < maxLayers;
    }
}