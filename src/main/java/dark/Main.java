package dark;

import arc.Files.FileType;
import arc.backend.sdl.*;
import arc.backend.sdl.jni.SDL;
import arc.util.*;
import dark.editor.Editor;
import dark.history.History;
import dark.ui.UI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static final String[] tags = { "&lc&fb[D]&fr", "&lb&fb[I]&fr", "&ly&fb[W]&fr", "&lr&fb[E]", "" };
    public static final DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static UI ui = new UI();
    public static Editor editor = new Editor();
    public static History history = new History();

    public static void main(String[] args) {
        Log.logger = (level, text) -> {
            var result = Log.format("&lk&fb[" + dateTime.format(LocalDateTime.now()) + "]&fr " + tags[level.ordinal()] + " " + text + "&fr");
            System.out.println(result);
        };

        try {
            new SdlApplication(new SpriteX(), new SdlConfig() {{
                title = "SpriteX";
                maximized = true;

                setWindowIcon(FileType.internal, "sprites/alpha-chan.png");
            }});
        } catch (Throwable t) {
            crashed(t);
        }
    }

    public static void crashed(Throwable t) {
        SDL.SDL_ShowSimpleMessageBox(SDL.SDL_MESSAGEBOX_ERROR, "Oh no, critical error", Strings.getStackTrace(t));
    }
}