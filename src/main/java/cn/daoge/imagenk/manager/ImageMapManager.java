package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemapstorage.ImageMapStorage;
import cn.daoge.imagenk.imageprovider.ImageProvider;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * 图片操作管理器接口
 */
public interface ImageMapManager {
    ImageMapStorage getStorage();
    ImageProvider getProvider();
    @Nullable ImageMap getImageMap(String id);
    @Nullable ImageMap getImageMapInPosition(Position pos);
    Map<String, ImageMap> getAllImageMap();

    /**
     * 生成一张地图画
     * @param imageMap 地图1画相关信息
     * @param face 地图画朝向
     * @param playerHorizontalFace 创建者水平朝向（当地图画朝向为UP或DOWN时此参有用）
     * @return 成功创建
     */
    boolean createImageMap(ImageMap imageMap, BlockFace face, BlockFace playerHorizontalFace);
    boolean removeImageMap(String name);
    boolean containImageMap(String name);
    boolean containImageMapInPosition(Position pos);

    default Set<String> getAllImageFile() {
        return getProvider().getAll();
    }
}
