package dark;

import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.util.Strings;
import dark.editor.Editor;
import dark.ui.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static arc.util.Log.*;

public class Main {

    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static Editor editor;
    public static UI ui;

    public static void main(String[] args) {
        logger = (level, text) -> System.out.println(format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + text));

        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";
            maximized = true;

            setWindowIcon(FileType.internal, "icon.png");
        }});
    }

    public static void info(String text, Object... values) {
        infoTag("SpriteX", Strings.format(text, values));
    }
}
