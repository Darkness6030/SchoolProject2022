package dark.ui;

import arc.util.Log;

import static arc.Core.files;

public class Textures {

    public static void load() {
        Log.info(files.classpath("sprites").exists());
        Log.info(files.classpath("sprites").seq());
    }
}