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
import arc.struct.Queue;
import arc.struct.Seq;
import arc.util.Tmp;
import com.github.bsideup.jabel.Desugar;

import static arc.Core.bundle;

public class Layer extends Pixmap {

    public TextureRegion region = new TextureRegion(new Texture(this));
    public String name = bundle.get("layer.default-name");

    public final OperationStack stack = new OperationStack();
    public Operation current = new Operation();

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

    @Override
    public void setRaw(int x, int y, int color) {
        super.setRaw(x, y, color);
        this.current.add(x, y, color);
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
        if (changed) region.texture.load(region.texture.getTextureData());
        changed = false;

        Draw.rect(region, x, y, width, height);
    }

    public void flush() {
        if (current.empty()) return;

        stack.add(current);
        current = new Operation();
    }

    public void updateTexture() {
        changed = true;
    }

    public void drawSquare(int x, int y, int size, Color color) {
        fillRect(x - size / 2, y - size / 2, size, size, color.rgba());
        updateTexture();
    }

    public void drawCircle(int x, int y, int size, Color color) {
        fillCircle(x, y, Mathf.floor(size / 2f), color.rgba());
        updateTexture();
    }

    public void fill(int x, int y, float maxDifference, Color color) {
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

        updateTexture();
    }

    public boolean compareColor(int previous, int color, float maxDifference) {
        return Tmp.c1.set(previous).diff(Tmp.c2.set(color)) <= maxDifference;
    }

    public class OperationStack {
        public final Queue<Operation> stack = new Queue<>();
        public int index;

        public void clear() {
            stack.clear();
            index = 0;
        }

        public void add(Operation operation) {
            stack.add(operation);
            index++;

            if (stack.size > 16)
                stack.removeFirst();
        }

        public boolean canUndo() {
            return index > 1;
        }

        public boolean canRedo() {
            return index < stack.size;
        }

        public void undo() {
            if (!canUndo()) return;
            stack.get(--index - 1).undo();
        }

        public void redo() {
            if (!canRedo()) return;
            stack.get(index++).redo();
        }
    }

    public class Operation {
        public final Seq<Pixel> pixels = new Seq<>();

        public boolean empty() {
            return pixels.isEmpty();
        }

        public void add(int x, int y, int color) {
            pixels.add(new Pixel(x, y, color));
        }

        public void undo() {
            for (int index = pixels.size - 1; index >= 0; index--)
                update(index);
        }

        public void redo() {
            for (int index = 0; index < pixels.size; index++)
                update(index);
        }

        public void update(int index) {
            var pixel = pixels.get(index);
            pixels.set(index, new Pixel(pixel.x, pixel.y, get(pixel.x, pixel.y)));

            set(pixel.x, pixel.y, pixel.color);
        }
    }

    @Desugar
    public record Pixel(int x, int y, int color) {
    }
}