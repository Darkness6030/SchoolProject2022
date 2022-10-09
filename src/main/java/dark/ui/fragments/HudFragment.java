package dark.ui.fragments;

import arc.scene.ui.layout.WidgetGroup;
import dark.editor.EditType;
import dark.ui.Textures;
import dark.ui.elements.TextSlider;

import static dark.Main.*;

public class HudFragment {

    public void build(WidgetGroup parent) {
        parent.fill(cont -> {
            cont.name = "Menu Bar";
            cont.top();

            cont.table(Textures.underline, pad -> {
                pad.left();

                pad.button("save", () -> {}).size(64f);
                pad.button("load", () -> {}).size(64f);

                new TextSlider(1f, 100f, 1f, 2f, value -> (editor.drawSize = value.intValue()) + "px").build(pad);
            }).height(64f).growX();
        });

        parent.fill(cont -> {
            cont.name = "Tools Bar";
            cont.left();

            cont.table(Textures.sideline, pad -> {
                pad.top();

                for (var type : EditType.values())
                    type.button(pad);
            }).width(64f).growY().padTop(64f);
        });
    }
}