package dark.ui;

import arc.assets.AssetManager;
import arc.files.Fi;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGeneratorLoader;
import arc.freetype.FreetypeFontLoader;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import arc.graphics.Color;
import arc.graphics.Gl;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.PixmapPacker;

import static arc.Core.*;

public class Fonts {
    
    public static PixmapPacker packer;
    public static Font def, icon;

    public static void load() {
        packer = new PixmapPacker(Gl.getInt(Gl.maxTextureSize) >= 4096 ? 4096 : 2048, 2048, 2, true);

        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(files::internal));
        assets.setLoader(Font.class, null, new FreetypeFontLoader(files::internal) {
            @Override
            public Font loadSync(AssetManager manager, String fileName, Fi file, FreeTypeFontLoaderParameter parameter) {
                parameter.fontParameters.magFilter = parameter.fontParameters.minFilter = TextureFilter.linear;
                parameter.fontParameters.packer = packer;

                return super.loadSync(manager, fileName, file, parameter);
            }
        });

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
