package dark;

import arc.ApplicationListener;
import arc.util.Log;

public class SpriteX implements ApplicationListener {

    @Override
    public void init() {
        Log.info("Initialised");
    }

    @Override
    public void dispose() {
        Log.info("Disposed");
    }
}
