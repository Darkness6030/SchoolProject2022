package dark;

import arc.ApplicationListener;
import arc.Core;
import arc.graphics.Color;
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

    @Override
    public void update() {
        Core.graphics.clear(Color.blue);
    }
}
