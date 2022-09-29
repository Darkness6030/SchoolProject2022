package dark;

import arc.util.Log;
import arc.util.Strings;
import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import dark.ui.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static UI ui;

    public static void main(String[] args) {
        Log.logger = (level, text) -> {
            String result = Log.format("[" + dateTime.format(LocalDateTime.now()) + "] " + text);
            System.out.println(result);
        };

        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";
            maximized = true;

            setWindowIcon(FileType.internal, "icon.png");
        }});
    }

    public static void info(String text, Object... values) {
        Log.infoTag("SpriteX", Strings.format(text, values));
    }
}
