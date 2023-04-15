package dark.ui;

import arc.graphics.*;

public class Palette {

    public static final Color

    main = Color.valueOf("#1e1e28"),

    darkmain = Color.valueOf("#101019"),

    active = Color.valueOf("#1ec864"),

    accent = Color.valueOf("#ffd37f");

    public static void load() {
        Colors.put("main", main);
        Colors.put("darkmain", darkmain);
        Colors.put("active", active);
        Colors.put("accent", accent);
    }
}