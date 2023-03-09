package dark.ui.dialogs;

import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.Image;
import arc.scene.ui.TextField;
import arc.scene.ui.TextField.TextFieldFilter;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import dark.editor.Binding;
import dark.ui.Drawables;
import dark.ui.Icons;

import static arc.Core.*;
import static dark.Main.*;

public class PaletteDialog extends BaseDialog {

    public static final String validHexChars = "0123456789AaBbCcDdEeFf";

    public PaletteImage image = new PaletteImage();
    public Color callback, color;

    public Seq<Runnable> rebuild = Seq.with(() -> image.update());

    public PaletteDialog() {
        super("@palette");
        addCloseButton();

        buttons.buttonRow("@ok", Icons.ok, () -> {
            callback.set(color);
            ui.colorWheel.add(color);

            hide();
        });

        cont.add(image).size(256f);
        cont.table(ctrl -> {
            ctrl.top();
            ctrl.defaults().size(128f, 32f);

            ctrl.image(Drawables.white_rounded_left).update(i -> i.setColor(callback));
            ctrl.image(Drawables.white_rounded_right).update(i -> i.setColor(color));

            ctrl.row();

            field(ctrl, "R:", 255, i -> color.r(i / 255f), () -> (int) (color.r * 255));
            field(ctrl, "H:", 360, i -> {}, () -> Color.RGBtoHSV(color)[0]); // TODO hsv не работает

            ctrl.row();

            field(ctrl, "G:", 255, i -> color.g(i / 255f), () -> (int) (color.g * 255));
            field(ctrl, "S:", 100, i -> {}, () -> Color.RGBtoHSV(color)[1]);

            ctrl.row();

            field(ctrl, "B:", 255, i -> color.b(i / 255f), () -> (int) (color.b * 255));
            field(ctrl, "V:", 100, i -> {}, () -> Color.RGBtoHSV(color)[2]);

            ctrl.row();

            field(ctrl, "#", hex -> color = Color.valueOf(hex), () -> color.toString(), field -> {
                field.setFilter((f, c) -> validHexChars.contains("" + c));
                field.setValidator(hex -> hex.length() == 6 || hex.length() == 8);
                field.setMaxLength(8);
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
            }).width(112f);
        }).marginLeft(6f);
    }

    public void field(Table table, String name, int maxValue, Cons<Integer> cons, Prov<Integer> prov) {
        field(table, name, t -> cons.get(Integer.valueOf(t)), () -> String.valueOf(prov.get()), field -> {
            field.setValidator(text -> !text.isEmpty() && Integer.valueOf(text) <= maxValue);
            field.setMaxLength(3);
        });
    }

    public void rebuild() {
        rebuild.each(Runnable::run);
    }

    public void show(Color color) {
        this.callback = color;
        this.color = color.cpy();

        rebuild();
        super.show();
    }

    public class PaletteImage extends Image {

        public Pixmap pixmap = new Pixmap(100, 100);
        public Texture texture = new Texture(pixmap);

        public PaletteImage() {
            setDrawable(new TextureRegion(texture));
            update(() -> {
                var mouse = screenToLocalCoordinates(input.mouse());
                if (mouse.x < 0f || mouse.x > 256f || mouse.y < 0f || mouse.y > 256f || !Binding.draw1.down()) return;

                int[] hsv = Color.RGBtoHSV(PaletteDialog.this.color);
                PaletteDialog.this.color = Color.HSVtoRGB(hsv[0], mouse.x / 2.56f, mouse.y / 2.56f);
                rebuild();
            });
        }

        public void update() {
            int[] hsv = Color.RGBtoHSV(PaletteDialog.this.color);
            pixmap.each((x, y) -> {
                pixmap.set(x, y, Color.HSVtoRGB(hsv[0], x, 100 - y));
            });

            texture.load(texture.getTextureData());
        }

        @Override
        public void draw() {
            super.draw();
            int[] hsv = Color.RGBtoHSV(PaletteDialog.this.color);

            Lines.stroke(2f, Color.lightGray);
            Lines.circle(x + hsv[1] * 2.56f, y + hsv[2] * 2.56f, 6f);
        }
    }
}
