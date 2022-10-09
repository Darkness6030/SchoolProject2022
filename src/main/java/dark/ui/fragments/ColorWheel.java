package dark.ui.fragments;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import dark.ui.Icons;
import dark.ui.Textures;

public class ColorWheel {

    public static final float radius = 128f;
    public static final int max = 18;

    public Stack stack;
    public Seq<Color> colors;

    private float deg;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Color Wheel";
            stack = cont.stack().size(radius * 2f).get();
        });
        colors = Seq.with(Color.white, Color.lightGray, Color.gray, Color.darkGray, Color.black,Color.red, Color.green, Color.blue);
    }

    public void add(Color color) {
        if (colors.contains(color)) return;
        colors.add(color.cpy());
        if (colors.size > max) colors.remove(0);
    }

    public void show(float x, float y, Cons<Color> callback) {
        stack.clear();
        deg = 90f;

        colors.each(color -> stack.add(new Table(table -> {
            table.defaults().size(32f); // TODO replace with something
            table.button(Textures.color_blob, 24f, () -> callback.get(color)).with(button -> {
                button.setTranslation(Mathf.cosDeg(deg += 15f) * radius,Mathf.sinDeg(deg) * radius);
                button.getImage().setColor(color);
            });
        })));

        stack.setPosition(x, y); // TODO don't work and idk why
        stack.visible = true;
    }

    public void hide() {
        stack.visible = false;
    }

    public boolean shown() {
        return stack != null && stack.visible;
    }
}