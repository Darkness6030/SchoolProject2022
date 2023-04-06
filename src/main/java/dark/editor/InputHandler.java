package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.math.geom.Bresenham2;
import dark.history.DrawOperation;

import static arc.Core.*;
import static dark.Main.*;

public class InputHandler implements ApplicationListener {

    /** Last known mouse position on the screen. */
    public static int mouseX, mouseY;

    /** Last known mouse position on the canvas. */
    public static int canvasX, canvasY;

    /** Mouse position where user start dragging a line. */
    public static int dragX, dragY;

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
            canvas.move(Binding.move_x.axis() * canvas.zoom * -8f, Binding.move_y.axis() * canvas.zoom * -8f);

            input();
        }

        mouseX = input.mouseX();
        mouseY = input.mouseY();

        canvasX = canvas.canvasX();
        canvasY = canvas.canvasY();
    }

    public void input() {
        // region draw

        if (!scene.hasMouse()) {
            if ((Binding.draw1.down() || Binding.draw2.down())) begin();

            draw(Binding.draw1, editor.first);
            draw(Binding.draw2, editor.second);
        }

        if ((Binding.draw1.release() && !Binding.draw2.down()) || (Binding.draw2.release() && !Binding.draw1.down())) flush();

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
        if (Binding.new_layer.tap()) editor.newLayer(); // idk where to put it

        // endregion
        // region tools

        for (var tool : EditTool.values())
            if (this.tool != tool && tool.hotkey.tap()) tool(tool);

        if (Binding.swap.tap()) editor.swap();

        // endregion
        // region actions

        if (Binding.copy.tap()) editor.copy();
        if (Binding.paste.tap()) editor.paste();
        if (Binding.undo.tap() && history.hasUndo()) editor.undo();
        if (Binding.redo.tap() && history.hasRedo()) editor.redo();

        // endregion
    }

    public void begin() {
        if (operation != null) return;

        operation = new DrawOperation(tool, editor.renderer.current);
        operation.begin();
    }

    public void flush() {
        if (operation == null) return;

        operation.end();
        if (!operation.data.isEmpty()) history.push(operation);
        operation = null;
    }

    public void draw(Binding binding, Color color) {
        if (tool.draggable && binding.down())
            Bresenham2.line(canvasX, canvasY, canvas.canvasX(), canvas.canvasY(), (x, y) -> tool.touched(editor.renderer.current, x, y, color));
        else if (binding.tap())
            tool.touched(editor.renderer.current, canvas.canvasX(), canvas.canvasY(), color);
    }

    public void tool(EditTool tool) {
        this.flush();
        this.tool = tool;

        ui.hudFragment.updateConfig();
    }
}
