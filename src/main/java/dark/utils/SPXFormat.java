package dark.utils;

import arc.struct.*;
import dark.editor.Layer;
import dark.history.Compress;

import java.io.*;

import static dark.Main.editor;

public class SPXFormat {

    public static final byte[] header = { 'S', 'P', 'R', 'I', 'T', 'E', 'X' };

    public static void write(Seq<Layer> layers, OutputStream output) throws IOException {
        try (var stream = new DataOutputStream(output)) {
            stream.write(header);

            stream.writeInt(layers.peek().width);
            stream.writeInt(layers.peek().height);
            stream.writeInt(layers.size);

            for (var layer : layers) {
                stream.writeUTF(layer.name);

                var data = Compress.write(layer);
                stream.writeInt(data.size);

                for (int i = 0; i < data.size; i++)
                    stream.writeInt(data.get(i));
            }
        }
    }

    public static void write(OutputStream output) throws IOException {
        write(editor.renderer.layers, output);
    }

    public static Layer[] read(InputStream input) throws IOException {
        try (var stream = new DataInputStream(input)) {
            for (byte b : header)
                if (stream.read() != b) throw new IOException("Not a SPX file: missing header.");

            int width = stream.readInt();
            int height = stream.readInt();
            int amount = stream.readInt();

            var layers = new Layer[amount];

            for (int i = 0; i < amount; i++) {
                layers[i] = new Layer(width, height);
                layers[i].name = stream.readUTF();

                var data = new IntSeq();
                int size = stream.readInt();

                for (int j = 0; j < size; j++)
                    data.add(stream.readInt());

                Compress.read(data, layers[i]::setRaw);
            }

            return layers;
        }
    }
}