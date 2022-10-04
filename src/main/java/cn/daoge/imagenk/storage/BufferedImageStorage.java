package cn.daoge.imagenk.storage;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 会缓存图片进内存的存储抽象类
 */
public abstract class BufferedImageStorage implements ImageStorage{

    protected Set<String> all;
    protected Map<String, Image> buffered = new HashMap<>();

    @Override
    public Image get(String name) {
        var image = buffered.get(name);
        if (image == null) buffered.put(name, image = load(name));
        return image;
    }

    @Override
    public Set<String> getAll() {
        if (all == null) all = loadAll();
        return all;
    }

    public void clear() {
        buffered.clear();
    }

    protected abstract Set<String> loadAll();

    protected abstract Image load(String name);
}
