package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.KeyCode;
import arc.input.GestureDetector.GestureListener;

import static arc.Core.*;
import static dark.editor.EditTool.*;

public class Editor implements ApplicationListener, GestureListener {

    public static final float minZoom = 0.2f, maxZoom = 20f;

    public int lastX, lastY, startX, startY;
    public int brushSize = 1;

    public float offsetX, offsetY, mouseX, mouseY;
    public float zoom = 1f;

    public boolean drawing;

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
        canvas.move(Binding.move_x.axis() * -3f, Binding.move_y.axis() * -3f);
        canvas.scale(input.axis(KeyCode.scroll) * .02f);

        graphics.clear(Color.sky);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (input.keyDown(KeyCode.mouseMiddle)) {
            canvas.move(deltaX, deltaY);
            return false;
        }

        offsetX += deltaX / zoom;
        offsetY += deltaY / zoom;

        return false;
    }

    public void newCanvas(int width, int height) {
        renderer = new Renderer();
        canvas = new Canvas(width, height);
    }

    public void save(Fi file) {}

    public void load(Fi file) {}

    public class Canvas {

        public float x, y, scale;
        public int width, height;

        public Canvas(int width, int height) {
            this.move(graphics.getWidth() / 2f, graphics.getHeight() / 2f);
            this.scale = 1f;

            this.width = width;
            this.height = height;
        }

        public void move(float x, float y) {
            this.x += x;
            this.y += y;
        }

        public void scale(float scale) {
            this.scale += scale;
        }

        public int scaledWidth() {
            return (int) (width * scale);
        }

        public int scaledHeight() {
            return (int) (height * scale);
        }

        public int mouseX() {
            return (int) ((scaledWidth() / 2 + input.mouseX() - x) / scale);
        }

        public int mouseY() {
            return (int) ((scaledHeight() / 2 - input.mouseY() + y) / scale);
        }
    }
}