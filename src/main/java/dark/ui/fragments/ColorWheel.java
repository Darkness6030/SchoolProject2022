package dark.ui.fragments;

import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.layout.*;
import arc.struct.Seq;
import dark.ui.Drawables;
import dark.ui.Styles;

public class ColorWheel {

    public static final float radius = 128f;
    public static final int max = 24;

    public Stack stack;
    public Seq<Color> colors;

    private float deg;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Color wheel";
            cont.fillParent = false;

            stack = cont.stack().get();
        });

        colors = Seq.with(Color.white, Color.gray, Color.black, Color.red, Color.orange, Color.yellow, Color.green, Color.cyan, Color.blue, Color.purple);
    }

    public void add(Color color) {
        if (colors.contains(color)) return;

        colors.add(color.cpy());
        if (colors.size == max) colors.remove(0);
    }

    public void show(float x, float y, Cons<Color> callback) {
        stack.clear();
        deg = 90f;

        colors.each(color -> stack.add(new Table(table -> {
            table.defaults().size(32f);
            table.button(Drawables.color_wheel_blob, Styles.emptyImageButton, () -> {
                callback.get(color);
                hide();
            }).with(button -> {
                button.setTranslation(Mathf.round(Mathf.cosDeg(deg += 15f) * radius), Mathf.round(Mathf.sinDeg(deg) * radius));
                button.getImage().setColor(color);
            });
        })));

        stack.setTranslation(x, y);
        stack.visible = true;
    }

    public void hide() {
        stack.visible = false;
    }
}