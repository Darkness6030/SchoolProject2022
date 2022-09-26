package dark;

import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;

public class Main {

    public static void main(String[] args) {
        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";
            maximized = true;

            setWindowIcon(FileType.internal, "icon.png");
        }});
    }
}