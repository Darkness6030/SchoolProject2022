package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Bresenham2;

import static arc.Core.*;
import static dark.editor.EditTool.pencil;

public class Editor implements ApplicationListener, GestureListener {

    public static final float minZoom = 0.2f, maxZoom = 20f;

    public int mouseX, mouseY, canvasX, canvasY;
    public int brushSize = 1;

    public Renderer renderer;
    public Canvas canvas;

    public EditTool tool = pencil, temp = pencil;
    public Color first = Color.white.cpy(), second = Color.black.cpy();

    public Editor() {
        input.addProcessor(new GestureDetector(this));
        newCanvas(800, 600);
    }

    @Override
    public void update() {
        if (!scene.hasScroll()) {
            canvas.move(Binding.move_x.axis() * -8f, Binding.move_y.axis() * -8f);
            canvas.zoom(input.axis(KeyCode.scroll) * .02f);
        }

        input();

        graphics.clear(Color.sky);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
        renderer.drawMouse(mouseX, mouseY, brushSize, canvas.zoom);
    }

    public void input() {
        if (input.keyDown(Binding.pan))
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);

        if (input.keyDown(Binding.draw1))
            draw(first);
        else if (input.keyDown(Binding.draw2))
            draw(second);

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.mouseX();
        canvasY = canvas.mouseY();
    }

    public void draw(Color color) {
        if (tool.draggable)
            Bresenham2.line(canvasX, canvasY, canvas.mouseX(), canvas.mouseY(), (x, y) -> tool.touched(x, y, color));
        else tool.touched(canvas.mouseX(), canvas.mouseY(), color);
    }

    public void newCanvas(int width, int height) {
        renderer = new Renderer();
        canvas = new Canvas(width, height);

        renderer.addLayer(width, height);
    }

    public void save(Fi file) {}

    public void load(Fi file) {}

    public static class Canvas {

        public float x, y, zoom;
        public int width, height;

        public Canvas(int width, int height) {
            this.move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
            this.zoom = 1f;

            this.width = width;
            this.height = height;
        }

        public void move(float x, float y) {
            this.x += x;
            this.y += y;
        }

        public void zoom(float zoom) {
            this.zoom += this.zoom * zoom;
            this.zoom = Mathf.clamp(this.zoom, minZoom, maxZoom);
        }

        public int scaledWidth() {
            return (int) (width * zoom);
        }

        public int scaledHeight() {
            return (int) (height * zoom);
        }

        public int mouseX() {
            return (int) ((scaledWidth() / 2f + input.mouseX() - x) / zoom);
        }

        public int mouseY() {
            return (int) ((scaledHeight() / 2f - input.mouseY() + y) / zoom);
        }
    }
}