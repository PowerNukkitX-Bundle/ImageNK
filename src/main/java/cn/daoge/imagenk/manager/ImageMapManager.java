package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemapstorage.ImageMapStorage;
import cn.daoge.imagenk.imageprovider.ImageProvider;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;

import java.util.Map;

/**
 * 图片操作管理器接口
 */
public interface ImageMapManager {
    ImageMapStorage getStorage();
    ImageProvider getProvider();
    Map<String, ImageMap> getAllImageMap();
    boolean createImageMap(ImageMap imageMap, BlockFace face);
    boolean removeImageMap(String name);
    boolean containImageMap(String name);
    boolean containImageMapInPosition(Position pos);
}
