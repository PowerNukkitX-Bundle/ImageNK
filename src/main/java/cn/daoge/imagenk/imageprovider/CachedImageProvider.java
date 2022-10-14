package cn.daoge.imagenk.imageprovider;

import cn.nukkit.Player;

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
    public void reload(Player notifier) {
        this.cache = loadAll(notifier);
    }

    /**
     * 加载所有图片
     * 缓存实现需要覆写此方法
     * @param notifier 需要被发送消息的玩家，可空
     */
    protected abstract Map<String, BufferedImage> loadAll(@Nullable Player notifier);
}
