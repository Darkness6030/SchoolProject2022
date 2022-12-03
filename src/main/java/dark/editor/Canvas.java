package dark.editor;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;

import static arc.Core.graphics;
import static arc.Core.input;
import static dark.Main.ui;

public class Canvas extends Layer {

    public final Seq<Layer> layers = new Seq<>(8);
    public final Layer background;

    public Layer current;
    public int x, y;

    public Canvas(int width, int height) {
        super(width, height);

        // Добавляем слой по умолчанию
        this.layers.add(current = new Layer(width, height));

        this.background = new Layer(width, height);
        this.background.fill(Color.valueOf("#577187"));
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void addLayer() {
        layers.add(current = new Layer(width, height));

        ui.hudFragment.rebuildLayers.run();
    }

    public void removeLayer(Layer layer) {
        if (layers.size == 1) return;

        layers.remove(layer);
        if (current == layer) current = layers.first();

        ui.hudFragment.rebuildLayers.run();
    }

    public void moveLayer(Layer layer, int direction) {
        int index = layers.indexOf(layer), newIndex = index + direction;
        if (newIndex < 0 || newIndex >= layers.size) return;

        layers.swap(index, newIndex);

        ui.hudFragment.rebuildLayers.run();
    }

    public void clampToScreen(int margin) {
        x = Mathf.clamp(x, graphics.getWidth() - margin - scaledWidth() / 2, margin + scaledWidth() / 2);
        y = Mathf.clamp(y, graphics.getHeight() - margin - scaledHeight() / 2, margin + scaledHeight() / 2);

        if (scaledWidth() < graphics.getWidth() - margin * 2)
            x = graphics.getWidth() / 2;
        if (scaledHeight() < graphics.getHeight() - margin * 2)
            y = graphics.getHeight() / 2;
    }

    public Layer layer() {
        return current;
    }

    public void layer(Layer layer) {
        this.current = layer;
    }

    public Pixmap toPixmap() {
        var pixmap = background.copy();
        layers.each(layer -> layer.each((x, y) -> pixmap.set(x, y, layer.get(x, y) == 0 ? pixmap.get(x, y) : layer.get(x, y))));
        return pixmap;
    }

    @Override
    public int pickColor(int x, int y) {
        return layer().pickColor(x, y);
    }

    public int mouseX() {
        return (int) ((scaledWidth() / 2 + input.mouseX() - x) / scale);
    }

    public int mouseY() {
        return (int) ((scaledHeight() / 2 - input.mouseY() + y) / scale);
    }

    public void draw() {
        Draw.reset();

        background.draw(x, y, scaledWidth(), scaledHeight());
        layers.each(layer -> layer.draw(x, y, scaledWidth(), scaledHeight()));

        Draw.flush();
    }
}