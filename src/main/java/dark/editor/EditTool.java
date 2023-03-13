package dark.editor;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import dark.ui.Icons;
import dark.ui.Styles;

import static arc.Core.*;
import static dark.Main.*;

public enum EditTool {
    pencil(true, Binding.pencil) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer layer, int x, int y, Color color) {
            if (config.square)
                layer.drawSquare(x, y, config.size, color);
            else
                layer.drawCircle(x, y, config.size, color);
        }
    },

    eraser(true, Binding.eraser) {
        public void build() {
            config.buildBrushTable();
        }

        public void touched(Layer layer, int x, int y, Color color) {
            if (config.square)
                layer.drawSquare(x, y, config.size, Color.clear);
            else
                layer.drawCircle(x, y, config.size, Color.clear);
        }
    },

    fill(false, Binding.fill) {
        public void build() {}

        public void touched(Layer layer, int x, int y, Color color) {
            layer.fill(x, y, color);
        }
    },

    line(false) {
        public void build() {
            config.buildBrushTable();
            cfgTable.check("@hud.straight", value -> config.straight = value); // всегда прямой угол
        }

        public void touched(Layer layer, int x, int y, Color color) {}
    },

    pick(false, Binding.pick) {
        public void build() {}

        public void touched(Layer layer, int x, int y, Color color) {
            if (scene.hasMouse()) return;

            // for (var layer : editor.renderer.layers) {
                //if (layer.get(x, y) != 0) {
                //    ui.colorWheel.add(color.set(layer.get(x, y)));
                //    return;
                //}
            // }
        }
    };

    public final boolean draggable;
    public final Binding hotkey;

    public final Config config = new Config();
    public final Table cfgTable = new Table();

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
        table.button(Icons.getDrawable(name()), Styles.imageButtonCheck, 48f, () -> {
            editor.tool = this;
            ui.hudFragment.updateConfig();
        })
                .checked(button -> editor.tool == this)
                .tooltip("@" + name() + ".tooltip")
                .size(48f).pad(8f, 8f, 0f, 8f).row();
    }

    public class Config {

        public int size = 16, softness = 4;
        public boolean square, straight;

        public void buildBrushTable() {
            cfgTable.table(t -> { // TODO иконки, чтобы понимать, что где
                t.slider(1f, 100f, 1f, value -> size = (int) value).row();
                t.slider(1f, 100f, 1f, value -> softness = (int) value);
            }).padRight(8f);

            cfgTable.check("@hud.square", value -> square = value).padRight(8f);
        }
    }
}