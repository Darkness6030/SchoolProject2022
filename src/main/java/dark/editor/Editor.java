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
        canvas.move(Binding.move_x.axis() * -8f, Binding.move_y.axis() * -8f);
        canvas.zoom(input.axis(KeyCode.scroll) * .02f);

        input();

        graphics.clear(Color.sky);

        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
        renderer.drawMouse(mouseX, mouseY, brushSize, canvas.zoom);
    }

    public void input() {
        if (input.keyDown(KeyCode.mouseMiddle))
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);

        if (input.keyDown(KeyCode.mouseLeft) || input.keyDown(KeyCode.mouseRight))
            Bresenham2.line(canvasX, canvasY, canvas.mouseX(), canvas.mouseY(), tool::touched);

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.mouseX();
        canvasY = canvas.mouseY();
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
            return (int) ((scaledWidth() / 2 + input.mouseX() - x) / zoom);
        }

        public int mouseY() {
            return (int) ((scaledHeight() / 2 - input.mouseY() + y) / zoom);
        }
    }
}