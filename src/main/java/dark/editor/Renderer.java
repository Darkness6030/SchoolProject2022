package dark.editor;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.struct.Seq;
import dark.ui.Palette;

import static dark.Main.*;

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

    public void drawMouse(int mouseX, int mouseY, int brushSize, float zoom) {
        Lines.stroke(4f, Palette.active);

        float x = editor.canvas.x + Mathf.floor((mouseX - editor.canvas.x) / zoom) * zoom + (brushSize % 2 * zoom / 2f);
        float y = editor.canvas.y + Mathf.floor((mouseY - editor.canvas.y) / zoom) * zoom + (brushSize % 2 == 0 ? zoom : zoom / 2f);

        if (editor.square)
            Lines.square(x, y, zoom * brushSize / 2f);
        else
            Lines.poly(Geometry.pixelCircle(brushSize / 2f), x, y, zoom);
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
        if (layers.size <= 1) return false;

        int index = layers.indexOf(layer);
        return index + direction >= 0 && index + direction < layers.size;
    }
}