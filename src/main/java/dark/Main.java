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

    public static final String[] tags = { "&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", "" };
    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static UI ui;

    public static void main(String[] args) {
        Log.logger = (level, text) -> {
            String result = Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + tags[level.ordinal()] + " " + text + "&fr");
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
