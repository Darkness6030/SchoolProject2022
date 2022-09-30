package dark.editor;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;

import static arc.Core.*;

public class Editor implements ApplicationListener {

    public FrameBuffer buffer = new FrameBuffer(800, 600); // temp
    public EditType type = EditType.pencil;

    @Override
    public void update() { // TODO handle input? idk
        graphics.clear(Color.lightGray);

        Draw.reset();

        Lines.stroke(4f, Color.black);
        Lines.rect(100f - 4f, 100f - 4f, 800f + 8f, 600f + 8f);
        Draw.color(Color.white);
        Draw.rect(new TextureRegion(buffer.getTexture()), 500f, 400f);

        Draw.flush();
    }
}
