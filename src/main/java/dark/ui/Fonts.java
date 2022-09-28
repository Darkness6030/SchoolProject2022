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
        assets.load("font", Font.class, new FreeTypeFontLoaderParameter("fonts/font.woff", new FreeTypeFontParameter() {{
            size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }})).loaded = font -> {
            Log.info("Font loaded.");
            def = font;
        };

        assets.load("icon", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter() {{
            size = 30;
            characters = "\0";
            incremental = true;
        }})).loaded = font -> {
            Log.info("Icons loaded.");
            icon = font;
        };
    }
}
