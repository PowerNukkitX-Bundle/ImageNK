package cn.daoge.imagenk.imageprovider;


import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * 图片提供源接口
 */
public interface ImageProvider {
    /**
     * 通过名称获取图片
     */
    @Nullable
    BufferedImage get(String name);

    /**
     * 获取存在的所有图片名称
     */
    Set<String> getAll();

    default void reload() {reload(null);};


    /**
     * 要求提供源重新加载文件
     * 对于非缓存实现，此方法没啥用
     * @param notifier 需要被发送消息的玩家，可空
     */
    void reload(@Nullable Player notifier);
}
