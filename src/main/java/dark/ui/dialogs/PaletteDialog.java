package dark.ui.dialogs;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Vec2;
import arc.scene.ui.Image;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.Palette;

import static arc.Core.*;
import static dark.Main.*;

public class PaletteDialog extends BaseDialog {

    public static final String validHexChars = "0123456789AaBbCcDdEeFf";

    public PaletteImage image = new PaletteImage();
    public HueSlider slider = new HueSlider();
    public Color callback, current;

    public Seq<Runnable> rebuild = Seq.with(image::update, slider::update);

    public PaletteDialog() {
        super("@palette");
        addCloseButton();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            ui.colorWheel.add(callback.set(current));
            hide();
        });

        cont.add(image).size(256f);
        cont.add(slider).size(32f, 256f);

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
                    if (!field.hasKeyboard()) field.setText(prov.get());
                });
            }).width(80f);
        }).marginLeft(6f);
    }

    public void field(Table table, String name, int max, Cons<Integer> cons, Prov<Integer> prov) {
        field(table, name, t -> cons.get(Strings.parseInt(t)), () -> String.valueOf(prov.get()), field -> {
            field.setMaxLength(3);
            field.setValidator(text -> {
                int number = Strings.parseInt(text);
                return number >= 0 && number <= max;
            });
        });
    }

    public void setHSV(int value, int index) {
        var hsv = Color.RGBtoHSV(current);
        hsv[index] = value;
        Color.HSVtoRGB(hsv[0], hsv[1], hsv[2], current);
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

    public static abstract class ClickableImage extends Image {

        public Pixmap pixmap;
        public Texture texture;

        public Vec2 mouse = new Vec2();
        public boolean clicked;

        public ClickableImage(int width, int height) {
            pixmap = new Pixmap(width, height);
            texture = new Texture(pixmap);
            setDrawable(new TextureRegion(texture));

            tapped(() -> clicked = true);
            released(() -> clicked = false);
        }

        /** Подтягивает изменения цвета, внесённые другими элементами. */
        public abstract void update();
    }

    public class PaletteImage extends ClickableImage {

        public PaletteImage() {
            super(100, 100);
            update(() -> {
                if (!clicked) return;

                mouse.set(screenToLocalCoordinates(input.mouse()).clamp(0f, 0f, 256f, 256f));
                var hsv = Color.RGBtoHSV(current);

                Color.HSVtoRGB(hsv[0], mouse.x / 2.56f, mouse.y / 2.56f, current);
                rebuild();
            });
        }

        public void update() {
            var hsv = Color.RGBtoHSV(current);
            if (!clicked) mouse.set(hsv[1] * 2.56f, hsv[2] * 2.56f);

            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(hsv[0], x, 100f - y)));
            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();

            Lines.stroke(2f, Palette.active.cpy().a(parentAlpha));
            Lines.circle(x + mouse.x, y + mouse.y, 6f);
        }
    }

    public class HueSlider extends ClickableImage {

        public HueSlider() {
            super(1, 360);
            update(() -> {
                if (!clicked) return;

                mouse.set(screenToLocalCoordinates(input.mouse()).clamp(0f, 0f, 256f, 32f));
                var hsv = Color.RGBtoHSV(current);

                Color.HSVtoRGB(mouse.y * 1.40625f, hsv[1], hsv[2], current);
                rebuild();
            });
        }

        public void update() {
            var hsv = Color.RGBtoHSV(current);
            if (!clicked) mouse.set(0f, hsv[0] / 1.40625f);

            pixmap.each((x, y) -> pixmap.set(x, y, Color.HSVtoRGB(360 - y, hsv[1], hsv[2])));
            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();

            Lines.stroke(2f, Palette.active.cpy().a(parentAlpha));
            Lines.rect(x, y + mouse.y - 3f, width, 6f);
        }
    }
}