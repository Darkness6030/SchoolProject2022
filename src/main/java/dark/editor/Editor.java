package dark.editor;

import arc.ApplicationListener;
import arc.files.Fi;
import arc.graphics.*;
import arc.input.GestureDetector;
import arc.input.GestureDetector.GestureListener;
import arc.math.geom.Bresenham2;
import dark.ui.Icons;
import dark.ui.Palette;
import dark.utils.Clipboard;

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
            if (!scene.hasKeyboard()) canvas.move(Binding.move_x.axis() * canvas.zoom * -8f, Binding.move_y.axis() * canvas.zoom * -8f);
            if (!scene.hasScroll()) canvas.zoom(Binding.zoom.scroll() * canvas.zoom * .05f);

            input();
        }

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();

        graphics.clear(Palette.darkmain);
        renderer.draw(canvas.x, canvas.y, canvas.scaledWidth(), canvas.scaledHeight());
    }

    @Override
    public void resize(int width, int height) {
        canvas.set(width / 2f, height / 2f);
    }

    public void input() {
        if (scene.hasKeyboard()) return;

        if (Binding.pan.down())
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);
        else if (Binding.draw1.down())
            draw(first);
        else if (Binding.draw2.down())
            draw(second);

        for (var tool : EditTool.values())
            if (tool.hotkey.tap()) this.tool = tool;

        if (Binding.new_canvas.tap()) ui.resize.show();
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

        if (input.ctrl() && Binding.copy.tap()) {
            var pixmap = renderer.save();
            Clipboard.setImage(pixmap);
        }

        if (input.ctrl() && Binding.paste.tap()) {
            var pixmap = Clipboard.getImage();
            if (pixmap != null) { // TODO вынести в отдельный метод
                var layer = new Layer(pixmap.width, pixmap.height);
                layer.draw(pixmap);

                renderer.reset(layer);
                canvas.reset(pixmap.width, pixmap.height);
            }
        }
    }

    public void draw(Color color) {
        if (tool.draggable) Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(renderer.current, x, y, color));
        else tool.touched(renderer.current, canvas.canvasX(), canvas.canvasY(), color);
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
        var layer = new Layer(file);

        renderer.reset(layer);
        canvas.reset(layer.width, layer.height);

        ui.showInfoToast(Icons.load, bundle.format("loaded", file.name()));
    }
}