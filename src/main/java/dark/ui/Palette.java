package dark.ui;

import arc.graphics.Color;
import arc.graphics.Colors;

public class Palette {

    public static final Color 
            main = Color.valueOf("#1E1E28"),
            darkmain = Color.valueOf("#14141E"),
            active = Color.valueOf("#1EC864"),
            accent = Color.valueOf("#ffd37f"),
            unlaunched = Color.valueOf("#8982ed");

    public static void load() {
        Colors.put("main", main);
        Colors.put("darkmain", darkmain);
        Colors.put("active", active);
        Colors.put("accent", accent);
        Colors.put("unlaunched", unlaunched);
    }
}