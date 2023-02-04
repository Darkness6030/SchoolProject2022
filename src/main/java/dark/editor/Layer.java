package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;

public class Layer extends FrameBuffer {

    public Layer(int width, int height) {
        super(width, height);
    }

    public void draw(float x, float y, float width, float height) {
        Draw.rect(new TextureRegion(getTexture()), x, y, width, height);
    }

    public void drawSquare(int x, int y, int brushSize, Color color) {
        begin();

        Draw.color(color);
        Fill.square(x, y, brushSize);

        end();
    }

    public void drawCircle(int x, int y, int brushSize, Color color) {
        begin();

        Draw.color(color);
        Fill.circle(x, y, brushSize);

        end();
    }

    // TODO
    public void drawPixmap(Pixmap pixmap) {
        begin();

        //Draw.rect(new TextureRegion(new Texture(pixmap)), );

        end();
    }
}