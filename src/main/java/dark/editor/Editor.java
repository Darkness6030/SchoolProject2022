package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.math.Mathf;
import arc.math.geom.Bresenham2;

import static arc.Core.*;
import static dark.Main.*;

public class Editor implements ApplicationListener, GestureListener {

    public static final float minZoom = 0.2f, maxZoom = 20f;

    public int mouseX, mouseY, canvasX, canvasY;
    public int brushSize = 1;

    public boolean square = true;

    public Renderer renderer;
    public Canvas canvas;

    public EditTool tool = EditTool.pencil, temp = EditTool.pencil;
    public Color first = Color.white.cpy(), second = Color.black.cpy();

    public Editor() {
        input.addProcessor(new GestureDetector(this));
        newCanvas(800, 600);
    }

    @Override
    public void update() {
        if (!scene.hasScroll()) {
            canvas.move(Binding.move_x.axis() * canvas.zoom * -8f, Binding.move_y.axis() * canvas.zoom * -8f);
            canvas.zoom(Binding.zoom.scroll() * canvas.zoom * .02f);

            input();
        }

        graphics.clear(Color.sky);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
        renderer.drawMouse(mouseX, mouseY, brushSize, canvas.zoom);
    }

    @Override
    public void resize(int width, int height) {
        canvas.set(width / 2f, height / 2f);
    }

    public void input() {
        if (Binding.pan.down())
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);
        else if (Binding.draw1.down())
            draw(first);
        else if (Binding.draw2.down())
            draw(second);

        for (var tool : EditTool.values())
            if (tool.hotkey != null && tool.hotkey.tap()) this.tool = tool;

        if (Binding.new_canvas.tap()) ui.canvasDialog.show();
        if (Binding.new_layer.tap()) newLayer();

        mouseX = canvas.mouseX();
        mouseY = canvas.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();
    }

    public void draw(Color color) {
        if (tool.draggable) Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(x, y, color));
        else tool.touched(canvas.canvasX(), canvas.canvasY(), color);
    }

    public void newCanvas(int width, int height) {
        renderer = new Renderer();
        canvas = new Canvas(width, height);

        renderer.addLayer(width, height);
    }

    public void newLayer() {
        renderer.addLayer(canvas.width, canvas.height);
        ui.hudFragment.rebuildLayers();
    }

    public void save(Fi file) {
        // TODO
    }

    public void load(Fi file) {
        // TODO
    }

    public static class Canvas {
        public float x, y, zoom;
        public int width, height;

        public Canvas(int width, int height) {
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
            this.zoom = Mathf.clamp(this.zoom, minZoom, maxZoom);
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

        public int mouseX() {
            return input.mouseX();
        }

        public int mouseY() {
            return input.mouseY();
        }
    }
}