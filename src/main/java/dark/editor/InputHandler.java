package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.math.geom.Bresenham2;
import arc.util.*;
import dark.history.DrawOperation;

import static arc.Core.*;
import static dark.Main.*;

public class InputHandler implements ApplicationListener {

    /** Last known mouse position on the screen. */
    public int mouseX, mouseY;

    /** Last known mouse position on the canvas. */
    public int canvasX, canvasY;

    /** Mouse position where user started dragging a line. */
    public int dragX = -1, dragY = -1;

    /** Whether the drag started from a UI element. */
    public boolean fromUI;

    /** Selected edit tool to be used for drawing. */
    public EditTool tool = EditTool.pencil, temp;
    public DrawOperation operation;

    @Override
    public void update() {
        if (scene.hasDialog()) return;

        if (Binding.pan.down())
            canvas.move(input.mouseX() - mouseX, input.mouseY() - mouseY);

        if (!scene.hasScroll())
            canvas.zoom(Binding.zoom.scroll() * canvas.zoom * (Binding.fastZoom.down() ? .15f : .05f));

        if (!scene.hasKeyboard()) {
            Tmp.v1.set(Binding.move_x.axis(), Binding.move_y.axis()).nor().scl(canvas.zoom * -8f);
            canvas.move(Tmp.v1.x, Tmp.v1.y);

            input();
        }

        if (canvasX != canvas.canvasX() || canvasY != canvas.canvasY()
                || Binding.draw1.tap() || Binding.draw2.tap()
                || Binding.draw1.release() || Binding.draw2.release()) {

            editor.renderer.overlay.fill(Color.clear);
            drawOverlay();

            editor.renderer.overlay.unchange();
        }

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();
    }

    public void input() {
        // region draw

        if (Binding.draw1.tap() || Binding.draw2.tap()) {
            fromUI = mouseX < 64 || mouseX > graphics.getWidth() - 256 || mouseY > graphics.getHeight() - 64 || scene.hasMouse();
            if (!fromUI) begin();
        }

        if (!fromUI) {
            draw(Binding.draw1, editor.first);
            draw(Binding.draw2, editor.second);
        }

        if ((Binding.draw1.release() && !Binding.draw2.down()) || (Binding.draw2.release() && !Binding.draw1.down()))
            flush();

        // endregion
        // region color wheel

        if (Binding.wheel.tap() && temp == null) {
            temp = tool;
            tool = EditTool.pick;

            ui.colorWheel.show(input.mouseX(), input.mouseY(), editor.first::set);
        }

        if ((Binding.wheel.release() || Binding.draw1.release()) && temp != null) {
            tool = temp;
            temp = null;

            ui.colorWheel.hide();
        }

        // endregion
        // region dialogs

        if (Binding.menu.tap() && !scene.hasDialog() && !ui.menu.isShown()) ui.menu.show();
        if (Binding.resize_canvas.tap()) ui.resize.show();
        if (Binding.new_canvas.tap()) ui.newCanvas.show();
        if (Binding.new_layer.tap()) editor.newLayer(); // IDK where to put it

        // endregion
        // region tools

        for (var tool : EditTool.values())
            if (this.tool != tool && tool.hotkey.tap()) tool(tool);

        if (Binding.swap.tap()) editor.swap();

        // endregion
        // region actions

        if (Binding.copy.tap()) Threads.daemon(editor::copy);
        if (Binding.paste.tap()) Threads.daemon(editor::paste);
        if (Binding.undo.tap() && history.hasUndo()) editor.undo();
        if (Binding.redo.tap() && history.hasRedo()) editor.redo();

        // endregion
    }

    public void begin() {
        if (operation != null) return;

        operation = new DrawOperation(tool, editor.renderer.current);
        operation.begin();

        dragX = canvas.canvasX(); // for line
        dragY = canvas.canvasY();
    }

    public void flush() {
        if (operation == null) return;

        operation.end();
        if (!operation.data.isEmpty()) history.push(operation);

        operation = null;
        fromUI = true; // to prevent further changes

        dragX = -1; // for line
        dragY = -1;
    }

    public void draw(Binding binding, Color color) {
        if (tool.draggable && binding.down())
            Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(editor.renderer.current, x, y, color));

        else if (tool.drawOnRelease && binding.release())
            Bresenham2.line(dragX, dragY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(editor.renderer.current, x, y, color));

        else if (!tool.drawOnRelease && binding.tap())
            tool.touched(editor.renderer.current, canvas.canvasX(), canvas.canvasY(), color);
    }

    public void drawOverlay() {
        if (tool.drawOnRelease && dragX != -1 && dragY != -1)
            Bresenham2.line(dragX, dragY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.drawOverlay(x, y));
        else
            tool.drawOverlay(canvas.canvasX(), canvas.canvasY());
    }

    public void tool(EditTool tool) {
        this.flush();
        this.tool = tool;

        ui.hudFragment.updateConfig();
    }
}