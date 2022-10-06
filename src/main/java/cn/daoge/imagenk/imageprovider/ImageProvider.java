package cn.daoge.imagenk.imageprovider;


import javax.annotation.Nullable;
import java.awt.*;
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

    /**
     * 要求提供源重新加载文件
     * 对于非缓存实现，此方法没啥用
     */
    void reload();
}
