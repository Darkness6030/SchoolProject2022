package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.*;

import static arc.Core.bundle;

public class Layer extends Pixmap {

    public TextureRegion region;
    public String name;

    public boolean visible = true;
    public boolean changed = true;

    public Layer(int width, int height, String name) {
        super(width, height);

        this.region = new TextureRegion(new Texture(this));
        this.name = name;
    }

    public Layer(int width, int height) {
        this(width, height, bundle.get("layer.new"));
    }

    public Layer copy() {
        Layer copy = new Layer(width, height, name);

        pixels.position(0);
        copy.pixels.position(0);
        copy.pixels.put(pixels);

        return copy;
    }

    public void draw(float x, float y, float width, float height) {
        unchange(() -> region.texture.load(region.texture.getTextureData()));
        Draw.rect(region, x, y, width, height);
    }

    public void change() {
        changed = true;
    }

    public void unchange(Runnable runnable) {
        if (changed) {
            runnable.run();
            changed = false;
        }
    }

    public void drawPixmap(Pixmap pixmap) {
        change();
        draw(pixmap);
    }

    public void drawSquare(int x, int y, int size, Color color) {
        change();
        fillRect(x - size / 2, y - size / 2, size, size, color.rgba());
    }

    public void drawCircle(int x, int y, int size, Color color) {
        change();
        fillCircle(x, y, Mathf.floor(size / 2f), color.rgba());
    }

    public void fill(int x, int y, Color color) {
        change();

        int prevColor = get(x, y);
        var hits = new Bits(width * height);

        var queue = new IntQueue();
        queue.addLast(Point2.pack(x, y));

        while (!queue.isEmpty()) {
            int pos = queue.removeFirst();
            x = Point2.x(pos);
            y = Point2.y(pos);

            if (in(x, y) && get(x, y) == prevColor && !hits.getAndSet(x + y * width)) {
                set(x, y, color);

                queue.addLast(Point2.pack(x, y - 1));
                queue.addLast(Point2.pack(x, y + 1));
                queue.addLast(Point2.pack(x - 1, y));
                queue.addLast(Point2.pack(x + 1, y));
            }
        }
    }
}