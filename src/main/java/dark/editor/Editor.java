package dark.editor;

import arc.files.Fi;
import arc.graphics.Color;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.input.KeyCode;
import arc.math.geom.*;
import arc.scene.Element;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.util.Tmp;
import dark.util.Utils;

import static arc.Core.*;
import static dark.editor.EditTool.pencil;

public class Editor extends Element implements GestureListener {

    public static final float minZoom = 0.2f, maxZoom = 20f;

    public int width, height;
    public int lastX, lastY, startX, startY;
    public int brushSize = 1;

    public float offsetX, offsetY, mouseX, mouseY;
    public float zoom = 1f;

    public boolean drawing;

    public Renderer renderer = new Renderer(800, 600);
    public EditTool tool = pencil, temp = pencil;

    public Color first = Color.white.cpy(), second = Color.black.cpy();

    public Editor() {
        input.addProcessor(new GestureDetector(this));

        scene.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                mouseX = x;
                mouseY = y;
                requestScroll();

                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Element fromActor) {
                requestScroll();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode code) {
                if (!Utils.isMouse(code)) return true;

                drawing = true;

                var point = convert(mouseX = x, mouseY = y);
                tool.touched(lastX = startX = point.x, lastY = startY = point.y);

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode code) {
                if (!Utils.isMouse(code)) return;

                drawing = false;

                var point = convert(x, y);
                tool.touchedLine(startX, startY, point.x, point.y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                mouseX = x;
                mouseY = y;

                var point = convert(x, y);

                if (drawing && tool.draggable)
                    Bresenham2.line(lastX, lastY, point.x, point.y, (cx, cy) -> tool.touched(cx, cy));

                lastX = point.x;
                lastY = point.y;
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // TODO
    }

    @Override
    public void draw() {
        // TODO
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if (!input.keyDown(KeyCode.mouseMiddle)) return false;

        offsetX += deltaX / zoom;
        offsetY += deltaY / zoom;

        return false;
    }

    public Point2 convert(float x, float y) {
        float ratio = 1f / ((float)width / height);
        float size = Math.min(width, height);

        int pointX = (int) ((x - getWidth() / 2 + size * zoom / 2 - offsetX * zoom) / size * zoom * width);
        int pointY = (int) ((y - getHeight() / 2 + size * zoom * ratio / 2 - offsetY * zoom) / size * zoom * ratio * height);

        return Tmp.p1.set(pointX, pointY);
    }

    public void save(Fi file) {}

    public void load(Fi file) {}
}