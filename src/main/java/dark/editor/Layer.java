package dark.editor;

import arc.files.Fi;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.Point2;
import arc.struct.*;
import arc.util.*;
import dark.utils.Shapes;

import static arc.Core.*;

public class Layer extends Pixmap {

    public TextureRegion region = new TextureRegion(new Texture(this));
    public String name = bundle.get("layer.default-name");

    public boolean changed = true;
    public boolean visible = true;

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

    public Layer resize(int width, int height, boolean filter) {
        var copy = new Layer(width, height);
        copy.name = name;
        copy.draw(this, 0, 0, this.width, this.height, 0, 0, width, height, filter);

        return copy;
    }

    public Layer resize(int width, int height, int align) {
        var copy = new Layer(width, height);
        copy.name = name;

        int x = Align.isLeft(align) ? 0 : Align.isRight(align) ? this.width - width : (this.width - width) / 2;
        int y = Align.isTop(align) ? 0 : Align.isBottom(align) ? this.height - height : (this.height - height) / 2;
        copy.draw(this, 0, 0, x, y, width, height);

        return copy;
    }

    public Layer copyLayer() {
        var copy = new Layer(width, height);
        copy.name = name;

        pixels.position(0);
        copy.pixels.position(0);
        copy.pixels.put(pixels);

        return copy;
    }

    public void change() {
        changed = true;
    }

    public void unchange() {
        region.texture.load(region.texture.getTextureData());
        changed = false;
    }

    public void draw(float x, float y, float width, float height) {
        Draw.rect(region, x, y, width, height);
    }

    public void drawSquare(int x, int y, int size, Color color) {
        Shapes.square(x, y, size, (cx, cy) -> set(cx, cy, color));
        change();
    }

    public void drawCircle(int x, int y, int size, Color color) {
        Shapes.circle(x, y, size, (cx, cy) -> set(cx, cy, color));
        change();
    }

    public void fill(int x, int y, float tolerance, Color color) {
        int previous = get(x, y);
        var hits = new Bits(width * height);

        var queue = new IntQueue();
        queue.addLast(Point2.pack(x, y));

        while (!queue.isEmpty()) {
            int pos = queue.removeFirst();
            x = Point2.x(pos);
            y = Point2.y(pos);

            if (in(x, y) && compareColor(previous, get(x, y), tolerance) && !hits.getAndSet(x + y * width)) {
                setRaw(x, y, color.rgba());

                queue.addLast(Point2.pack(x, y - 1));
                queue.addLast(Point2.pack(x, y + 1));
                queue.addLast(Point2.pack(x - 1, y));
                queue.addLast(Point2.pack(x + 1, y));
            }
        }

        change();
    }

    public boolean compareColor(int previous, int color, float tolerance) {
        return difference(Tmp.c2.set(previous), Tmp.c3.set(color)) <= tolerance;
    }

    public float difference(Color first, Color second) {
        return (Math.abs(first.r - second.r)
                + Math.abs(first.g - second.g)
                + Math.abs(first.b - second.b)
                + Math.abs(first.a - second.a)) / 4f;
    }
}