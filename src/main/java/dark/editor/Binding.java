package dark.editor;

import arc.KeyBinds.Axis;
import arc.input.KeyCode;

public class Binding {

    public static Axis move_x = new Axis(KeyCode.a, KeyCode.d);
    public static Axis move_y = new Axis(KeyCode.s, KeyCode.w);
    public static KeyCode mouse_move = KeyCode.mouseMiddle;
}
