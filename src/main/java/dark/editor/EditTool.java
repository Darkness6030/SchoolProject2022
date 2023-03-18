package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;
import dark.ui.elements.Switch;

import static arc.Core.scene;
import static dark.Main.editor;
import static dark.Main.ui;

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
        public int value;

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

        public void buildBrushTable() {
            configTable.table(table -> { // TODO иконки, чтобы понимать, что где
                table.slider(1f, 100f, 1f, value -> size = (int) value).row();
                table.slider(1f, 100f, 1f, value -> softness = (int) value);
            }).padRight(8f);

            configTable.add(new Switch("@hud.square", value -> square = value)).padRight(8f);
        }
    }
}