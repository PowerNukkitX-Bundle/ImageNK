package cn.daoge.imagenk.imageprovider;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 会缓存图片进内存的存储抽象类
 */
public abstract class CachedImageProvider implements ImageProvider {
    protected Map<String, BufferedImage> cache = new HashMap<>();

    @Override
    public @Nullable BufferedImage get(String name) {
        return cache.get(name);
    }

    /**
     * 此实现只会返回已加载项
     */
    @Override
    public Set<String> getAll() {
        return cache.keySet();
    }

    @Override
    public void reload() {
        cache();
    }

    /**
     * 预缓存图片
     */
    public void cache() {
        this.cache = loadAll();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        cache.clear();
    }

    /**
     * 加载所有图片
     */
    protected abstract Map<String, BufferedImage> loadAll();
}
