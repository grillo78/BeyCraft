package ga.beycraft.util;

import friedrichlp.renderlib.caching.serialization.Serializer;
import friedrichlp.renderlib.util.IFileContainer;

import java.io.IOException;
import java.io.InputStream;

public class StreamFileContainer implements IFileContainer {

    private InputStream stream;
    private final String path;
    private final String name;

    public StreamFileContainer(String path, String name) {
        this.path = path;
        this.name = name;
        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.path + "/" + this.name);
    }

    @Override
    public InputStream getStream() throws IOException {
        return stream;
    }

    @Override
    public StreamFileContainer getRelative(String s) {
        return new StreamFileContainer(s.substring(0, s.lastIndexOf("/")), s.substring(s.lastIndexOf("/") + 1));
    }

    @Override
    public String getPath() {
        return path + "/" + name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void save(Serializer.Out out) throws IOException {
        out.writeStr(path);
        out.writeStr(name);
    }

    @Override
    public void load(Serializer.In in) throws IOException {
        stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(in.readStr() + "/" + in.readStr());
    }
}
