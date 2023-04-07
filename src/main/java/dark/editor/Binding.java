package dark.editor;

import arc.KeyBinds.Axis;
import arc.func.Boolp;
import arc.input.KeyCode;

import static arc.Core.*;

public enum Binding {

    unknown(KeyCode.unknown),
    any(KeyCode.anyKey),

    pan(KeyCode.mouseMiddle),
    zoom(KeyCode.scroll),
    fastZoom(KeyCode.altLeft),

    draw1(KeyCode.mouseLeft),
    draw2(KeyCode.mouseRight),
    wheel(KeyCode.shiftLeft),

    move_x(KeyCode.a, KeyCode.d),
    move_y(KeyCode.s, KeyCode.w),

    menu(KeyCode.escape),
    resize_canvas(KeyCode.r, () -> input.ctrl()),
    new_canvas(KeyCode.n, () -> input.ctrl()),
    new_layer(KeyCode.plus),

    pencil(KeyCode.b),
    eraser(KeyCode.e),
    fill(KeyCode.f),
    line(KeyCode.l),
    pick(KeyCode.p),
    swap(KeyCode.x),

    copy(KeyCode.c, () -> input.ctrl()),
    paste(KeyCode.v, () -> input.ctrl()),
    undo(KeyCode.z, () -> input.ctrl()),
    redo(KeyCode.y, () -> input.ctrl());

    private final Axis axis;
    private final Boolp alt;

    Binding(KeyCode key) {
        this.axis = new Axis(key);
        this.alt = () -> true;
    }

    Binding(KeyCode key, Boolp alt) {
        this.axis = new Axis(key);
        this.alt = alt;
    }

    Binding(KeyCode min, KeyCode max) {
        this.axis = new Axis(min, max);
        this.alt = () -> true;
    }

    public boolean down() {
        return alt.get() && input.keyDown(axis.key);
    }

    public boolean tap() {
        return alt.get() && input.keyTap(axis.key);
    }

    public boolean release() {
        return input.keyRelease(axis.key);
    }

    public float axis() {
        return input.keyDown(axis.min) && input.keyDown(axis.max) ? 0f : input.keyDown(axis.min) ? -1f : input.keyDown(axis.max) ? 1f : 0f;
    }

    public float scroll() {
        return input.axis(axis.key);
    }
}