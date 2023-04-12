package dark.ui;

import arc.files.Fi;
import arc.graphics.Color;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.util.Time;

import static arc.Core.*;
import static dark.Main.*;

public class Shaders {

    public static Shader drawOverlay;
    public static FrameBuffer buffer;

    public static void load() {
        drawOverlay = new OverlayShader();
        buffer = new FrameBuffer();
    }

    public static void renderer(Runnable draw, Shader shader) {
        buffer.resize(graphics.getWidth(), graphics.getHeight());
        buffer.begin(Color.clear);

        draw.run();

        buffer.end();
        buffer.blit(shader);
    }

    public static class OverlayShader extends Shader {

        public OverlayShader() {
            super(getShader("screenspace.vert"), getShader("overlay.frag"));
        }

        @Override
        public void apply() {
            setUniformf("u_dp", 2f);
            setUniformf("u_time", Time.time);
            setUniformf("u_texsize", canvas.width, canvas.height);
            setUniformf("u_invsize", 1f / canvas.width, 1f / canvas.height);
        }
    }

    public static Fi getShader(String file) {
        return files.internal("shaders/" + file);
    }
}
