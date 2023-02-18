package dark.ui;

import arc.audio.Sound;

import static arc.Core.files;
import static arc.Core.settings;

public class Sounds {

    public static Sound message;

    public static void load() {
        message = new Sound(files.internal("sounds/message.ogg"));
    }

    public static void play(Sound sound) {
        sound.play(settings.getInt("sfxvol") / 100f);
    }
}