package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.Switch;
import dark.ui.elements.UnderTable;

import static arc.Core.*;
import static dark.Main.*;

public enum EditTool {
    pencil(true, Binding.pencil) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, color);
            else
                current.drawCircle(x, y, config.size, color);
        }
    },

    eraser(true, Binding.eraser) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer current, int x, int y, Color color) {
            if (config.square)
                current.drawSquare(x, y, config.size, Color.clear);
            else
                current.drawCircle(x, y, config.size, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        public void build() {
            configTable.slider(0f, 1f, 0.01f, value -> config.maxDifference = value).padRight(8f);
        }

        public void touched(Layer current, int x, int y, Color color) {
            current.fill(x, y, config.maxDifference, color);
        }
    },

    line(false) {
        public void build() {
            config.buildBrushTable();
            configTable.add(new Switch("@hud.straight", value -> config.straight = value)); // всегда прямой угол
        }

        public void touched(Layer layer, int x, int y, Color color) {}
    },

    pick(false, Binding.pick) {
        public void build() {}

        public void touched(Layer current, int x, int y, Color color) {
            if (scene.hasMouse()) return;

            // TODO как-то смешивать цвета
            for (var layer : editor.renderer.layers) {
                if (layer.get(x, y) != 0) {
                    ui.colorWheel.add(color.set(layer.get(x, y)));
                    return;
                }
            }
        }
    };

    public final boolean draggable;
    public final Binding hotkey;

    public final Config config = new Config();
    public final Table configTable = new Table();

    EditTool(boolean draggable) {
        this(draggable, Binding.unknown);
    }

    EditTool(boolean draggable, Binding hotkey) {
        this.draggable = draggable;
        this.hotkey = hotkey;
    }

    public abstract void build();

    public abstract void touched(Layer layer, int x, int y, Color color);

    public void button(Table table) {
        table.button(Icons.drawable(name()), Styles.imageButtonCheck, 48f, () -> {
            editor.tool = this;
            ui.hudFragment.updateConfig();
        })
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(48f).pad(8f, 8f, 0f, 8f).row();
    }

    public class Config {

        public int size = 16, softness = 4;
        public float maxDifference = 0.2f;

        public boolean square, straight;

        // TODO Дарк, если надо, прикрути валидацию по размеру для field'ов, ибо кисть в 999999 не круто
        public void buildBrushTable() {
            configTable.table(table -> {
                table.image(Icons.circle).size(24f);
                table.slider(1, 100, 1, value -> size = (int) value).with(slider -> new UnderTable(slider, under -> {
                    under.field(String.valueOf(size), value -> slider.setValue(size = Strings.parseInt(value))).valid(value -> {
                        int number = Strings.parseInt(value);
                        return number >= 1 && number <= 100;
                    }).update(field -> {
                        if (!field.hasKeyboard())
                            field.setText(String.valueOf((int) slider.getValue()));
                    });
                })).row();

                table.image(Icons.spray).size(24f);
                table.slider(1, 100, 1, value -> softness = (int) value).with(slider -> new UnderTable(slider, under -> {
                    under.field(String.valueOf(softness), value -> slider.setValue(softness = Strings.parseInt(value))).valid(value -> {
                        int number = Strings.parseInt(value);
                        return number >= 1 && number <= 100;
                    }).update(field -> {
                        if (!field.hasKeyboard())
                            field.setText(String.valueOf((int) slider.getValue()));
                    });
                }));
            }).padRight(8f);

            configTable.add(new Switch("@hud.square", value -> square = value)).padRight(8f);
        }
    }
}