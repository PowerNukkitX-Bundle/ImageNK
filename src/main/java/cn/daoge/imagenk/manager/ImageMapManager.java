package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.storage.ImageStorage;

import java.util.Map;

public interface ImageMapManager {
    ImageStorage getStorage();
    Map<String, ImageMap> getAllImageMap();
    boolean createImageMap(ImageMap imageMap);
    boolean removeImageMap(String name);
}
