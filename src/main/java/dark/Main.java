package dark;

import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.graphics.Color;

public class Main {

    public static void main(String[] args) {
        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";
            initialBackgroundColor = new Color(277353);

            // Отключаем загрузку модуля звука
            disableAudio = true;

            setWindowIcon(FileType.internal, "S.png");
        }});
    }
}