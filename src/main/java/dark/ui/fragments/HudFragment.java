package dark.ui.fragments;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;
import dark.ui.Icons;
import dark.ui.Textures;
import dark.ui.elements.LayerButton;
import dark.ui.elements.TextSlider;

import static dark.Main.*;

public class HudFragment {

    public Runnable rebuildLayers;;

    public void build(WidgetGroup parent) {
        parent.fill(hud -> {
            hud.name = "Menu Bar";
            hud.top();

            hud.table(Textures.underline, underline -> {
                underline.defaults().pad(24f);

                underline.left();

                underline.button("Menu", () -> ui.menuDialog.show()).size(64f);

                new TextSlider(1f, 100f, 1f, editor.drawSize, value -> (editor.drawSize = value.intValue()) + "px").build(underline);

                underline.stack(
                        new Table(color -> color.button(String.valueOf(Icons.swap), () -> {
                            var temp = editor.first.cpy();
                            editor.first.set(editor.second);
                            editor.second.set(temp);
                        }).size(16f).with(button -> button.setTranslation(18f, 18f))),
                        new ColorBlob(editor.second, 8f, -8f),
                        new ColorBlob(editor.first, -8f, 8f)
                ).size(64f);

                underline.label(() -> "Layer " + (editor.canvas.currentLayer + 1) + "/" + editor.canvas.layers.size);
            }).height(64f).growX();
        });

        parent.fill(sideline -> {
            sideline.name = "Tools Bar";
            sideline.left();

            sideline.table(Textures.sideline, pad -> {
                pad.top();

                for (var type : EditType.values())
                    type.button(pad);
            }).width(64f).growY().padTop(64f);
        });

        parent.fill(layers -> {
            layers.name = "Layers";
            layers.right();

            layers.table(Textures.sideline_left, sideline -> {
                sideline.top().marginLeft(8f);
                sideline.defaults().size(128f).padBottom(4f);

                rebuildLayers = () -> {
                    sideline.clear();
                    editor.canvas.layers.map(LayerButton::new).each(button -> sideline.add(button).tooltip("Layer #" + button.layer.index()).row());
                };
                rebuildLayers.run();
            }).growY().padTop(64f);
        });
    }

    public static class ColorBlob extends Table {

        public ColorBlob(Color color, float x, float y) {
            button(Textures.color_blob, () -> ui.pickerDialog.show(color))
                    .with(button -> button.setTranslation(x, y))
                    .update(button -> button.getImage().setColor(color));
        }
    }
}