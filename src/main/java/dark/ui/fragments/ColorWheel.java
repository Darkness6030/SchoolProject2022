package dark.ui.fragments;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import dark.ui.Icons;

public class ColorWheel {

    public static final float radius = 256f;
    public static final int max = 16;

    public Stack stack;
    public Seq<Color> colors;

    private float deg;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Color Wheel";
            stack = cont.stack().size(radius).get();
        });
        colors = Seq.with(Color.white, Color.lightGray, Color.gray, Color.darkGray, Color.black);
    }

    public void add(Color color) {
        colors.add(color);
        if (colors.size > max) colors.remove(0);
    }

    public void show(Cons<Color> callback) {
        stack.clear();
        deg = 180f;

        colors.each(color -> stack.add(new Table(table -> {
            table.defaults().size(32f);
            table.button("[#" + color + "]" + Icons.block, () -> callback.get(color)).get().setTranslation(
                    Mathf.cosDeg(deg += 20f) * radius,
                    Mathf.sinDeg(deg) * radius);
        })));

        stack.visible = true;
    }

    public void hide() {
        stack.visible = false;
    }

    public boolean shown() {
        return stack != null && stack.visible;
    }
}
