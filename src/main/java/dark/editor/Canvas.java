package dark.editor;

import arc.math.Mathf;

import static arc.Core.*;

public class Canvas {
    public float x, y, zoom;
    public int width, height;

    public void reset(int width, int height) {
        this.set(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
        this.zoom = 1f;

        this.width = width;
        this.height = height;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void zoom(float zoom) {
        this.zoom += zoom;
        this.zoom = Mathf.clamp(this.zoom, 0.2f, 20f);

        var mouse = input.mouse().sub(x, y);
        this.move(mouse.x * zoom, mouse.y * zoom);
    }

    public int scaledWidth() {
        return (int) (width * zoom);
    }

    public int scaledHeight() {
        return (int) (height * zoom);
    }

    public int canvasX() {
        return (int) ((scaledWidth() / 2f + input.mouseX() - x) / zoom);
    }

    public int canvasY() {
        return (int) ((scaledHeight() / 2f - input.mouseY() + y) / zoom);
    }
}