package dark.editor;

import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.Bits;
import arc.struct.IntQueue;
import arc.util.Tmp;

import static arc.Core.bundle;

public class Layer extends Pixmap {

    public TextureRegion region = new TextureRegion(new Texture(this));
    public String name = bundle.get("layer.default-name");

    public boolean visible = true;
    public boolean changed = true;

    public Layer(int width, int height) {
        super(width, height);
    }

    public Layer(Fi file) {
        super(file);
    }

    public Layer(Pixmap pixmap) {
        this(pixmap.width, pixmap.height);
        this.draw(pixmap);
    }

    public Layer copy() {
        var copy = new Layer(width, height);
        copy.name = name;

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

    public void drawSquare(int x, int y, int size, Color color) {
        change();
        fillRect(x - size / 2, y - size / 2, size, size, color.rgba());
    }

    public void drawCircle(int x, int y, int size, Color color) {
        change();
        fillCircle(x, y, Mathf.floor(size / 2f), color.rgba());
    }

    public void fill(int x, int y, float maxDifference, Color color) {
        change();

        int previous = get(x, y);
        var hits = new Bits(width * height);

        var queue = new IntQueue();
        queue.addLast(Point2.pack(x, y));

        while (!queue.isEmpty()) {
            int pos = queue.removeFirst();
            x = Point2.x(pos);
            y = Point2.y(pos);

            if (in(x, y) && compareColor(previous, get(x, y), maxDifference) && !hits.getAndSet(x + y * width)) {
                set(x, y, color);

                queue.addLast(Point2.pack(x, y - 1));
                queue.addLast(Point2.pack(x, y + 1));
                queue.addLast(Point2.pack(x - 1, y));
                queue.addLast(Point2.pack(x + 1, y));
            }
        }
    }

    public boolean compareColor(int previous, int color, float maxDifference) {
        return Tmp.c1.set(previous).diff(Tmp.c2.set(color)) <= maxDifference;
    }
}