package dark;

import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.util.Strings;
import arc.util.serialization.JsonReader;
import dark.editor.Editor;
import dark.ui.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static arc.backend.sdl.jni.SDL.*;
import static arc.util.Log.*;

public class Main {

    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final JsonReader reader = new JsonReader();

    public static Editor editor;
    public static UI ui;

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> showError(throwable));
        logger = (level, text) -> System.out.println(format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + text));

        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";
            maximized = true;

            setWindowIcon(FileType.internal, "sprites/alpha-chan.png");
        }});
    }

    public static void info(String text, Object... values) {
        infoTag("SpriteX", Strings.format(text, values));
    }

    public static void showError(Throwable throwable) {
        SDL_ShowSimpleMessageBox(SDL_MESSAGEBOX_ERROR, "oh no", "An error has occured!\n" + Strings.getStackTrace(throwable));
    }
}