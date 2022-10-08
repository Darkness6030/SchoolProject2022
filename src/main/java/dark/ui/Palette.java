package dark.ui;

import arc.graphics.Color;
import arc.graphics.Colors;

public class Palette {

    public static final Color
            accent = Color.valueOf("#ffd37f"),
            unlaunched = Color.valueOf("#8982ed");

    public static void load() {
        Colors.put("accent", accent);
        Colors.put("unlaunched", unlaunched);
    }
}