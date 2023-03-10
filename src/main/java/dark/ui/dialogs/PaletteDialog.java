package dark.ui.dialogs;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.Image;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Palette;

import static arc.Core.*;
import static dark.Main.*;

public class PaletteDialog extends BaseDialog {
    public static final String validHexChars = "0123456789AaBbCcDdEeFf";

    public PaletteImage image = new PaletteImage();
    public Color callback, current;

    public Seq<Runnable> rebuild = Seq.with(() -> image.update());

    public PaletteDialog() {
        super("@palette");
        addCloseButton();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            callback.set(current);
            ui.colorWheel.add(current);

            hide();
        });

        cont.add(image).size(256f);
        cont.table(ctrl -> {
            ctrl.top();
            ctrl.defaults().size(128f, 32f);

            ctrl.image(Drawables.white_rounded_left).update(i -> i.setColor(callback));
            ctrl.image(Drawables.white_rounded_right).update(i -> i.setColor(current));

            ctrl.row();

            field(ctrl, "[red]R: ", 255, i -> current.r(i / 255f), () -> (int) (current.r * 255));
            field(ctrl, "[magenta]H: ", 360, i -> setHSV(i, 0), () -> Color.RGBtoHSV(current)[0]);

            ctrl.row();

            field(ctrl, "[green]G: ", 255, i -> current.g(i / 255f), () -> (int) (current.g * 255));
            field(ctrl, "[yellow]S: ", 100, i -> setHSV(i, 1), () -> Color.RGBtoHSV(current)[1]);

            ctrl.row();

            field(ctrl, "[blue]B: ", 255, i -> current.b(i / 255f), () -> (int) (current.b * 255));
            field(ctrl, "[cyan]V: ", 100, i -> setHSV(i, 2), () -> Color.RGBtoHSV(current)[2]);

            ctrl.row();

            field(ctrl, "[lightgray]A: ", 255, i -> current.a(i / 255f), () -> (int) (current.a * 255));

            ctrl.row();

            field(ctrl, "[accent]HEX: ", hex -> current = Color.valueOf(hex), () -> current.toString(), field -> {
                field.setFilter((f, c) -> validHexChars.contains(String.valueOf(c)));
                field.setValidator(hex -> hex.length() == 6 || hex.length() == 8);
                field.setMaxLength(8);

                app.post(() -> ((Table) field.parent).getCells().peek().width(182f)); // увеличивает размер поля под hex
            });
        }).growY();
    }

    public void field(Table table, String name, Cons<String> cons, Prov<String> prov, Cons<TextField> with) {
        table.table(cont -> {
            cont.left();

            cont.add(name);
            cont.field("", TextFieldFilter.digitsOnly, text -> {
                cons.get(text);
                rebuild();
            }).with(field -> {
                with.get(field);
                rebuild.add(() -> {
                    if (scene.getKeyboardFocus() != field) field.setText(prov.get());
                });
            }).width(80f);
        }).marginLeft(6f);
    }

    public void field(Table table, String name, int maxValue, Cons<Integer> cons, Prov<Integer> prov) {
        field(table, name, t -> cons.get(Integer.parseInt(t)), () -> String.valueOf(prov.get()), field -> {
            field.setValidator(text -> !text.isEmpty() && Integer.parseInt(text) <= maxValue);
            field.setMaxLength(3);
        });
    }

    public void setHSV(int value, int index) {
        int[] hsv = Color.RGBtoHSV(current);
        hsv[index] = value;
        current = Color.HSVtoRGB(hsv[0], hsv[1], hsv[2]);
    }

    public void rebuild() {
        rebuild.each(Runnable::run);
    }

    public void show(Color color) {
        this.callback = color;
        this.current = color.cpy();

        rebuild();
        super.show();
    }

    public class PaletteImage extends Image {
        public Pixmap pixmap = new Pixmap(100, 100);
        public Texture texture = new Texture(pixmap);

        public boolean clicked;

        public PaletteImage() {
            setDrawable(new TextureRegion(texture));
            update(() -> {
                if (!clicked) return;

                var mouse = screenToLocalCoordinates(input.mouse());
                var hsv = Color.RGBtoHSV(current);

                current = Color.HSVtoRGB(hsv[0], Mathf.clamp(mouse.x / 2.56f, 0f, 100f), Mathf.clamp(mouse.y / 2.56f, 0f, 100f));
                rebuild();
            });

            tapped(() -> clicked = true);
            released(() -> clicked = false);
        }

        public void update() {
            var hsv = Color.RGBtoHSV(current);
            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(hsv[0], x, 100f - y)));

            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();
            var hsv = Color.RGBtoHSV(current);

            Lines.stroke(2f, Palette.active);
            Lines.circle(x + hsv[1] * 2.56f, y + hsv[2] * 2.56f, 8f);
        }
    }
}