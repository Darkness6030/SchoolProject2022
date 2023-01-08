package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditTool;
import dark.editor.Layer;
import dark.ui.*;
import dark.ui.elements.TextFieldSlider;

import static arc.Core.*;
import static dark.Main.*;

public class HudFragment {

    public Runnable rebuildLayers;

    public ScrollPane pane;
    public SideLayerTable sideLayerTable;

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.top();

            cont.table(Drawables.underline, table -> {
                table.touchable = Touchable.enabled;

                table.left();
                table.button(Drawables.alpha_chan, Styles.alphaStyle, 40f, () -> ui.menuDialog.show()).checked(button -> ui.menuDialog.isShown()).size(40f).padLeft(8f);

                table.add(new TextFieldSlider("", 1, 100, 1, editor.brushSize, value -> editor.brushSize = (int) value)).padLeft(48f);

                table.stack(
                        new SwapButton(editor.first, editor.second, 18f, 18f),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(32f).padLeft(48f);

                table.check("@hud.square", value -> editor.square = value).padLeft(48f);
            }).height(64f).growX();
        });

        parent.fill(cont -> {
            cont.left();

            cont.table(Drawables.sideline, table -> {
                table.touchable = Touchable.enabled;

                table.top();
                for (var type : EditTool.values()) type.button(table);
            }).width(68f).growY().padTop(60f);
        });

        parent.fill(cont -> {
            cont.right();

            cont.table(Drawables.sideline_left, table -> {
                table.touchable = Touchable.enabled;

                table.top().marginLeft(8f);
                table.defaults().size(128f).padBottom(4f);

                pane = table.pane(layers -> rebuildLayers = () -> {
                    layers.clear();

                    editor.renderer.layers.map(LayerButton::new).each(button -> layers.add(button).row());

                    layers.button(Icons.plus, 32f, editor::newLayer).size(32f).tooltip("@layer.new");
                }).expand().get();

                pane.setOverscroll(true, true);
                pane.setFadeScrollBars(true);

                rebuildLayers();
            }).growY().padTop(60f);
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

                ui.showInfoToast(Icons.swap, "@swapped");
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
        public Layer layer;

        public SideLayerTable(Table parent) {
            parent.add(this);

            defaults().size(43f);
            visible(() ->
                    input.mouseX() > this.x + translation.x &&
                    input.mouseY() > this.y + translation.y &&
                    input.mouseY() < this.y + translation.y + height);

            button(Icons.up, Styles.sideLayerImageButtonStyle, () -> editor.renderer.moveLayer(layer, -1))
                    .visible(() -> editor.renderer.canMove(layer, -1))
                    .tooltip("@layer.move.up").row();

            button(Icons.eraser, Styles.sideLayerImageButtonStyle, () -> editor.renderer.removeLayer(layer))
                    .visible(() -> editor.renderer.canRemove())
                    .tooltip("@layer.remove").row();

            button(Icons.down, Styles.sideLayerImageButtonStyle, () -> editor.renderer.moveLayer(layer, 1))
                    .visible(() -> editor.renderer.canMove(layer, 1))
                    .tooltip("@layer.move.down").row();
        }

        public void show(Layer layer, float ty) {
            this.layer = layer;
            setTranslation(graphics.getWidth() - 136f, ty);
        }

        public void hide() {
            this.layer = null;
            setTranslation(0f, 0f);
        }
    }

    // endregion
}