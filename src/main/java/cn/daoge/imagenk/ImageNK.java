package cn.daoge.imagenk;

import cn.daoge.imagenk.storage.CachedImageStorage;
import cn.daoge.imagenk.storage.LocalImageStorage;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;

@Getter
public final class ImageNK extends PluginBase {

    @Getter
    private static ImageNK instance = null;

    {
        instance = this;
    }

    private CachedImageStorage storage;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        storage = new LocalImageStorage(this.getDataFolder().toPath().resolve("images"));
        //预缓存图片
        storage.cache();
    }

    @Override
    public void onDisable() {

    }
}
