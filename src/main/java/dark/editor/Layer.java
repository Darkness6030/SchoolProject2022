package dark.editor;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;

public class Layer extends FrameBuffer {

    public TextureRegion region;
    public String name;
    public boolean visible = true;

    public Layer(int width, int height, String name) {
        super(width, height);

        this.region = new TextureRegion(getTexture());
        this.name = name;
    }

    public Layer(int width, int height) {
        this(width, height, "New layer"); // TODO Дарк, добавь сюда бандлы
    }

    public Layer copy() {
        Layer copy = new Layer(getWidth(), getHeight(), name);

        copy.begin();
        Draw.rect(region, 0f, 0f); // TODO Adi, протести
        copy.end();

        return copy;
    }

    public void draw(float x, float y, float width, float height) {
        Draw.rect(new TextureRegion(getTexture()), x, y, width, height);
    }

    public void drawSquare(int x, int y, int brushSize, Color color) {
        beginBind();

        Draw.color(color);
        Fill.square(x, y, brushSize);

        end();
    }

    public void drawCircle(int x, int y, int brushSize, Color color) {
        beginBind();

        Draw.color(color);
        Fill.circle(x, y, brushSize);

        end();
    }

    // TODO Дарк, ты серьёзно оставляешь пустые тудушники?
    public void drawPixmap(Pixmap pixmap) {
        begin();

        // Draw.rect(new TextureRegion(new Texture(pixmap)), );

        end();
    }
}