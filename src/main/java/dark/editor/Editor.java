package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.math.geom.Bresenham2;
import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.ui;

public class Editor implements ApplicationListener, GestureListener {

    public int mouseX, mouseY, canvasX, canvasY;
    public int brushSize = 1;

    public boolean square;

    public Renderer renderer = new Renderer();
    public Canvas canvas = new Canvas();

    public EditTool tool = EditTool.pencil, temp;
    public Color first = Color.white.cpy(), second = Color.black.cpy();

    public void load() {
        input.addProcessor(new GestureDetector(this));
        reset(800, 600);
    }

    @Override
    public void update() {
        if (!scene.hasDialog()) {
            canvas.move(Binding.move_x.axis() * canvas.zoom * -8f, Binding.move_y.axis() * canvas.zoom * -8f);
            if (!scene.hasScroll()) canvas.zoom(Binding.zoom.scroll() * canvas.zoom * .05f);

            input();
        }

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();

        graphics.clear(Color.sky);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
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

        if (Binding.wheel.tap() && temp == null) {
            temp = tool;
            tool = EditTool.pick;

            ui.colorWheel.show(input.mouseX(), input.mouseY(), first::set);
        }

        if ((Binding.wheel.release() || Binding.draw1.release()) && temp != null) {
            tool = temp;
            temp = null;

            ui.colorWheel.hide();
        }
    }

    public void draw(Color color) {
        if (tool.draggable) Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(x, y, color));
        else tool.touched(canvas.canvasX(), canvas.canvasY(), color);
    }

    public void reset(int width, int height) {
        renderer.reset(width, height);
        canvas.reset(width, height);
    }

    public void newLayer() {
        renderer.addLayer(new Layer(canvas.width, canvas.height));
    }

    public void save(Fi file) {
        var pixmap = renderer.save();
        PixmapIO.writePng(file, pixmap);

        ui.showInfoToast(Icons.save, bundle.format("saved", file.name()));
    }

    public void load(Fi file) {
        var pixmap = new Pixmap(file);

        renderer.reset(pixmap.width, pixmap.height);
        canvas.reset(pixmap.width, pixmap.height);

        renderer.current.drawPixmap(pixmap);

        ui.showInfoToast(Icons.load, bundle.format("loaded", file.name()));
    }
}