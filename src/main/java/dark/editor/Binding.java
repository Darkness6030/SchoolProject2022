package dark.editor;

import arc.KeyBinds.Axis;
import arc.input.KeyCode;

import static arc.Core.*;

public enum Binding {

    pan(KeyCode.mouseMiddle),
    zoom(KeyCode.scroll),

    draw1(KeyCode.mouseLeft),
    draw2(KeyCode.mouseRight),
    pick(KeyCode.controlLeft),

    move_x(KeyCode.a, KeyCode.d),
    move_y(KeyCode.s, KeyCode.w),

    new_canvas(KeyCode.tab),
    new_layer(KeyCode.plus),

    pencil(KeyCode.b),
    eraser(KeyCode.e),
    fill(KeyCode.f);

    private final Axis axis;

    private Binding(KeyCode key) {
        this.axis = new Axis(key);
    }

    private Binding(KeyCode min, KeyCode max) {
        this.axis = new Axis(min, max);
    }

    public boolean down() {
        return input.keyDown(axis.key);
    }

    public boolean release() {
        return input.keyRelease(axis.key);
    }

    public boolean tap() {
        return input.keyTap(axis.key);
    }

    public float axis() {
        return input.keyDown(axis.min) && input.keyDown(axis.max) ? 0 : input.keyDown(axis.min) ? -1 : input.keyDown(axis.max) ? 1 : 0f;
    }

    public float scroll() {
        return input.axis(axis.key);
    }
}