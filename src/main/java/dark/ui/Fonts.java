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
import arc.scene.ui.layout.Scl;
import arc.struct.ObjectSet;

import static arc.Core.*;

public class Fonts {
    
    public static PixmapPacker packer;
    public static Font def, icon;

    public static void load() {
        packer = new PixmapPacker(Gl.getInt(Gl.maxTextureSize) >= 4096 ? 4096 : 2048, 2048, 2, true);

        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(files::internal));
        assets.setLoader(Font.class, null, new FreetypeFontLoader(files::internal) {
            ObjectSet<FreeTypeFontParameter> scaled = new ObjectSet<>();

            @Override
            public Font loadSync(AssetManager manager, String fileName, Fi file, FreeTypeFontLoaderParameter parameter) {
                FreeTypeFontParameter fontParams = parameter.fontParameters;

                if (fileName.equals("outline")) {
                    fontParams.borderWidth = Scl.scl(2f);
                    fontParams.spaceX -= fontParams.borderWidth;
                }

                if (!scaled.contains(fontParams)) {
                    fontParams.size = (int) (Scl.scl(fontParams.size));
                    scaled.add(fontParams);
                }

                fontParams.magFilter = fontParams.minFilter = TextureFilter.linear;
                fontParams.packer = packer;

                return super.loadSync(manager, fileName, file, parameter);
            }
        });

        assets.load("font-default", Font.class, new FreeTypeFontLoaderParameter("fonts/font.woff", new FreeTypeFontParameter() {{
            size = 18;
            shadowColor = Color.darkGray;
            shadowOffsetY = 2;
            incremental = true;
        }})).loaded = f -> def = f;

        assets.load("font-icon", Font.class, new FreeTypeFontLoaderParameter("fonts/icon.ttf", new FreeTypeFontParameter() {{
            size = 30;
            characters = "\0";
            incremental = true;
        }})).loaded = f -> icon = f;
    }
}
