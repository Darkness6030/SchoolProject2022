package dark.ui;

import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import arc.graphics.Color;
import arc.graphics.g2d.Font;
import arc.util.Log;

import static arc.Core.*;

public class Fonts {
    
    public static Font def, icon;

    public static void load() {
        assets.load("font-default", Font.class, new FreeTypeFontLoaderParameter("fonts/font.woff", new FreeTypeFontParameter() {{
            size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }})).loaded = f -> {
            Log.info("FONT LOADED");
            def = f;
        };

        assets.load("font-icon", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter() {{
            size = 30;
            characters = "\0";
            incremental = true;
        }})).loaded = f -> icon = f;
    }
}
