package dark.editor;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;

import static arc.Core.*;

public class LayerCanvas extends Layer {

    public final Seq<Layer> layers = new Seq<>(10);
    public final Layer background;

    public int currentLayer;

    public int x, y;

    public LayerCanvas(int width, int height) {
        super(width, height);
        this.move(graphics.getWidth() / 2, graphics.getHeight() / 2);
        this.addLayer(width, height);

        background = new Layer(width, height);
        background.fill(Color.valueOf("#577187"));
    }

    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void addLayer(int width, int height) {
        currentLayer = layers.add(new Layer(width, height)).size - 1;
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
        return layers.get(currentLayer);
    }

    public void layer(int currentLayer) {
        this.currentLayer = currentLayer;
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