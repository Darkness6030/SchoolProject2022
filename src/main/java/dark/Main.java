package dark;

import arc.Files.FileType;
import arc.backend.sdl.SdlApplication;
import arc.backend.sdl.SdlConfig;
import arc.backend.sdl.jni.SDL;
import arc.util.Log;
import arc.util.Strings;
import arc.util.serialization.JsonReader;
import dark.editor.Editor;
import dark.ui.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final JsonReader reader = new JsonReader();

    public static UI ui = new UI();
    public static Editor editor = new Editor();

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> showError(throwable));
        Log.logger = (level, text) -> System.out.println(Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + text));

        new SdlApplication(new SpriteX(), new SdlConfig() {{
            title = "SpriteX";

            maximized = true;

            setWindowIcon(FileType.internal, "sprites/alpha-chan.png");
        }});
    }

    public static void info(String text, Object... values) {
        Log.infoTag("SpriteX", Strings.format(text, values));
    }

    public static void showError(Throwable throwable) {
        SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, "oh no", "An error has occured!\n" + Strings.getStackTrace(throwable));
    }
}