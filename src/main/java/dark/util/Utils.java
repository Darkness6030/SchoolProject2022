package dark.util;

import arc.input.KeyCode;

public class Utils {

    public static boolean isMouse(KeyCode code) {
        return code == KeyCode.mouseLeft || code == KeyCode.mouseMiddle || code == KeyCode.mouseRight;
    }
}