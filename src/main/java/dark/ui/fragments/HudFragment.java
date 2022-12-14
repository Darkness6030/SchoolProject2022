package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.*;
import dark.editor.EditTool;
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
                underline.left();

                underline.button(alpha_chan, alphaStyle, () -> ui.menuDialog.show()).checked(button -> ui.menuDialog.isShown()).size(64f);
                underline.stack(
                        new SwapButton(editor.first, editor.second, 18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(64f).padRight(8f);

                new TextSlider(1f, 100f, 1f, editor.brushSize, value -> bundle.format("hud.brushSize", editor.brushSize = value.intValue())).build(underline);
            }).height(64f).growX();
        });

        parent.fill(sideline -> {
            sideline.name = "Tools Bar";
            sideline.left();

            sideline.table(Drawables.sideline, pad -> {
                pad.top();
                for (var type : EditTool.values()) type.button(pad);
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
                    editor.renderer.layers.map(LayerButton::new).each(button -> sideline.add(button).row());

                    sideline.button(Icons.plus, 32f, editor::newLayer).size(32f);
                };

                rebuildLayers();
            }).growY().padTop(64f);
        });

        parent.fill(cont -> {
            cont.name = "Layer Buttons";
            cont.right();

            cont.fillParent = false;
            sideLayerTable = new SideLayerTable(cont);
        });
    }

    public void rebuildLayers() {
        if (rebuildLayers != null) rebuildLayers.run();
        if (sideLayerTable != null) sideLayerTable.hide();
    }

    // region subclasses

    public static class SwapButton extends ImageButton {

        public SwapButton(Color first, Color second, float x, float y) {
            super(Icons.swap, Styles.imageNoneStyle);
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
            super(Drawables.color_blob, Styles.imageNoneStyle);
            setTranslation(x, y);

            clicked(() -> ui.pickerDialog.show(color));
            update(() -> getImage().setColor(color));
        }
    }

    public static class LayerButton extends ImageButton {

        public Layer layer;

        public LayerButton(Layer layer) {
            super(layer.getRegion(), Styles.layerImageButtonStyle);
            this.layer = layer;

            resizeImage(118f);

            clicked(() -> editor.renderer.current = layer);
            hovered(() -> ui.hudFragment.sideLayerTable.show(layer, y + height / 2f));
            update(() -> {
                setChecked(editor.renderer.current == layer);
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

            button(Icons.up, () -> {
                editor.renderer.moveLayer(layer, -1);
                ui.hudFragment.rebuildLayers();
            }).disabled(button -> !editor.renderer.canMove(layer, -1)).tooltip("@layer.move.up").row();

            button(Icons.eraser, () -> {
                editor.renderer.removeLayer(layer);
                ui.hudFragment.rebuildLayers();
            }).disabled(button -> !editor.renderer.canRemove()).tooltip("@layer.remove").row();

            button(Icons.down, () -> {
                editor.renderer.moveLayer(layer, 1);
                ui.hudFragment.rebuildLayers();
            }).disabled(button -> !editor.renderer.canMove(layer, 1)).tooltip("@layer.move.down").row();
        }

        public void show(Layer layer, float ty) {
            this.layer = layer;
            setTranslation(graphics.getWidth() - sideBarWidth, ty);
        }

        public void hide() {
            this.layer = null;
            setTranslation(0f, 0f);
        }
    }

    // endregion
}