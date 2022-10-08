package dark.ui;

import arc.freetype.*;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.PixmapPacker;

import static arc.Core.*;

public class Fonts {
    
    public static PixmapPacker packer;
    public static Font def, icon;

    public static void load() {
        packer = new PixmapPacker(Gl.getInt(Gl.maxTextureSize) >= 4096 ? 4096 : 2048, 2048, 2, true);

        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(files::internal));
        assets.setLoader(Font.class, null, new FreetypeFontLoader(files::internal));

        assets.load("font", Font.class, new FreeTypeFontLoaderParameter("fonts/font.woff", new FreeTypeFontParameter() {{
            size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }})).loaded = font -> {
            font.getData().markupEnabled = true;
            def = font;
        };

        assets.load("icon", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter() {{
            size = 30;
            characters = "\0";
            incremental = true;
        }})).loaded = font -> {
            font.getData().markupEnabled = true;
            icon = font;
        };
    }
}