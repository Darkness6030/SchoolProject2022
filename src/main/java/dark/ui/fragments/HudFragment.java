package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import dark.editor.EditType;
import dark.editor.Layer;
import dark.ui.*;
import dark.ui.elements.TextSlider;

import static arc.Core.*;
import static dark.Main.*;
import static dark.ui.Drawables.alpha_chan;
import static dark.ui.Styles.alphaStyle;

public class HudFragment {

    public Runnable rebuildLayers;
    public SideLayerTable sideLayerTable;

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

                underline.label(() -> bundle.format("hud.layer", editor.canvas.layers.indexOf(editor.canvas.current) + 1, editor.canvas.layers.size));
            }).height(64f).growX();
        });

        parent.fill(sideline -> {
            sideline.name = "Tools Bar";
            sideline.left();

            sideline.table(Drawables.sideline, pad -> {
                pad.top();

                for (var type : EditType.values())
                    type.button(pad);
            }).width(64f).growY().padTop(60f);
        });

        parent.fill(layers -> {
            layers.name = "Layers";
            layers.right();

            layers.table(Drawables.sideline_left, sideline -> {
                sideline.top().marginLeft(8f);
                sideline.defaults().size(128f).padBottom(4f);

                rebuildLayers = () -> {
                    sideline.clear();
                    editor.canvas.layers.map(LayerButton::new).each(layerButton -> sideline.add(layerButton).row());
                };

                rebuildLayers.run();
            }).growY().padTop(64f);
        });

        parent.fill(cont -> {
            cont.name = "Layer Buttons";
            cont.right();

            cont.fillParent = false;
            sideLayerTable = new SideLayerTable(cont);
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

                ui.showInfoFade("@swapped", 2f);
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

    public static class LayerButton extends ImageButton {

        public LayerButton(Layer layer) {
            super(layer.getRegion(), Styles.layerImageButtonStyle);

            resizeImage(118f);

            clicked(() -> editor.canvas.layer(layer));
            hovered(() -> ui.hudFragment.sideLayerTable.show(layer, y + height / 2f));
            update(() -> {
                setChecked(editor.canvas.layer() == layer);
                getImage().setDrawable(layer.getRegion());
            });
        }
    }

    public static class SideLayerTable extends Table {

        public static final float sideBarWidth = Scl.scl(128f + 8f);

        public Layer layer;

        public SideLayerTable(Table parent) {
            super(Drawables.sideline_left);
            parent.add(this);

            defaults().size(128f / 3f);
            visible(() ->
                    input.mouseX() > this.x + translation.x &&
                    input.mouseY() > this.y + translation.y &&
                    input.mouseY() < this.y + translation.y + height);

            button(Fonts.getGlyph(Icons.up),     () -> editor.canvas.moveLayer(layer, -1)).tooltip("Move Up").row();
            button(Fonts.getGlyph(Icons.eraser), () -> editor.canvas.removeLayer(layer)).tooltip("Remove").row();
            button(Fonts.getGlyph(Icons.down),   () -> editor.canvas.moveLayer(layer, 1)).tooltip("Move Down").row();
        }

        public void show(Layer layer, float ty) {
            this.layer = layer;
            setTranslation(graphics.getWidth() - sideBarWidth, ty);
        }
    }
}