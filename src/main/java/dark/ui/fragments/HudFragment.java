package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;
import dark.editor.Layer;
import dark.ui.Drawables;
import dark.ui.Icons;
import dark.ui.elements.LayerButton;
import dark.ui.elements.TextSlider;

import static arc.Core.*;
import static dark.Main.*;
import static dark.ui.Drawables.alpha_chan;
import static dark.ui.Styles.alphaStyle;

public class HudFragment {

    public Runnable rebuildLayers;
    public SideButtons sideButtons;

    public void build(WidgetGroup parent) {
        parent.fill(hud -> {
            hud.name = "Menu Bar";
            hud.top();

            hud.table(Drawables.underline, underline -> {
                underline.defaults().pad(24f);
                underline.left();

                underline.button(alpha_chan, alphaStyle, () -> ui.menuDialog.show()).checked(button -> ui.menuDialog.isShown());

                new TextSlider(1f, 100f, 1f, editor.drawSize, value -> bundle.format("hud.drawSize", editor.drawSize = value.intValue())).build(underline);

                underline.stack(
                        new SwapButton(editor.first, editor.second, 18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(64f);

                underline.label(() -> bundle.format("hud.layer", editor.canvas.currentLayer + 1, editor.canvas.layers.size));
            }).height(64f).growX();
        });

        parent.fill(sideline -> {
            sideline.name = "Tools Bar";
            sideline.left();

            sideline.table(Drawables.sideline, pad -> {
                pad.top();

                for (var type : EditType.values())
                    type.button(pad);
            }).width(64f).growY().padTop(64f);
        });

        parent.fill(layers -> {
            layers.name = "Layers";
            layers.right();

            layers.table(Drawables.sideline_left, sideline -> {
                sideline.top().marginLeft(8f);
                sideline.defaults().size(128f).padBottom(4f);

                rebuildLayers = () -> {
                    sideline.clear();
                    editor.canvas.layers.map(LayerButton::new).each(button -> sideline.add(button).tooltip("Layer #" + button.layer.index()).row());
                };

                rebuildLayers.run();
            }).growY().padTop(64f);
        });

        parent.fill(cont -> {
            cont.name = "Layer Buttons";
            cont.right();

            cont.fillParent = false;
            sideButtons = new SideButtons(cont);
        });
    }

    public static class SwapButton extends TextButton {

        public SwapButton(Color first, Color second, float x, float y) {
            super(String.valueOf(Icons.swap));
            setTranslation(x, y);

            clicked(() -> {
                var temp = first.cpy();
                first.set(second.cpy());
                second.set(temp);
            });
        }
    }

    public static class ColorBlob extends ImageButton {

        public ColorBlob(Color color, float x, float y) {
            super(Drawables.color_blob);
            setTranslation(x, y);

            clicked(() -> ui.pickerDialog.show(color));
            update(() -> getImage().setColor(color));
        }
    }

    public static class SideButtons extends Table {

        public static final float sideBarWidth = Scl.scl(128f + 8f);

        public Layer layer;

        public SideButtons(Table parent) {
            super(Drawables.sideline_left);
            parent.add(this);

            defaults().size(128f / 3f);
            visible(() ->
                    input.mouseX() > this.x + translation.x &&
                    input.mouseY() > this.y + translation.y &&
                    input.mouseY() < this.y + translation.y + height);

            button(Icons.back + "",   () -> editor.canvas.moveLayer(layer, true)).tooltip("Move Up").row();
            button(Icons.eraser + "", () -> editor.canvas.removeLayer(layer)).tooltip("Remove").row();
            button(Icons.back + "",   () -> editor.canvas.moveLayer(layer, false)).tooltip("Move Down").row();
        }

        public void show(Layer layer, float ty) {
            this.layer = layer;
            setTranslation(graphics.getWidth() - sideBarWidth, ty);
        }
    }
}