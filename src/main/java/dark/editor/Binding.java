package dark.editor;

import arc.KeyBinds.*;
import arc.input.InputDevice.DeviceType;
import arc.input.KeyCode;

import static arc.Core.*;

public enum Binding implements KeyBind {

    pan(KeyCode.mouseMiddle),
    zoom(KeyCode.scroll),

    draw1(KeyCode.mouseLeft),
    draw2(KeyCode.mouseRight),
    pick(KeyCode.controlLeft),

    move_x(new Axis(KeyCode.a, KeyCode.d)),
    move_y(new Axis(KeyCode.s, KeyCode.w)),

    new_canvas(KeyCode.tab),
    new_layer(KeyCode.plus);

    private final KeybindValue value;

    Binding(KeybindValue value) {
        this.value = value;
    }

    @Override
    public KeybindValue defaultValue(DeviceType type) {
        return value;
    }

    public float axis() {
        return value instanceof Axis axis
                ? (input.keyDown(axis.min) && input.keyDown(axis.max) ? 0 : input.keyDown(axis.min) ? -1 : input.keyDown(axis.max) ? 1 : 0f)
                : 0f;
    }
}