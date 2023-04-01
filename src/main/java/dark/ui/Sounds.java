package dark.ui;

import arc.audio.Sound;

import static arc.Core.files;

public class Sounds {

    public static Sound message;

    public static void load() {
        message = new Sound(files.internal("sounds/message.ogg"));
    }
}