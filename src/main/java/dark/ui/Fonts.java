package dark.ui;

import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.graphics.Color;
import arc.graphics.g2d.Font;

import static arc.Core.*;

public class Fonts {

    public static Font def;

    public static void load() {
        def = load("font.woff", new FreeTypeFontParameter() {{
            size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }});

        def.getData().markupEnabled = true;
    }

    public static Font load(String name, FreeTypeFontParameter parameter) {
        var generator = new FreeTypeFontGenerator(files.internal("fonts/" + name));
        return generator.generateFont(parameter);
    }
}