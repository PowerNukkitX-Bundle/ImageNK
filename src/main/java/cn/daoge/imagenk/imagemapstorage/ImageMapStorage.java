package cn.daoge.imagenk.imagemapstorage;

import cn.daoge.imagenk.imagemap.ImageMap;

import java.util.Map;
import java.util.Set;

/**
 * ImageMap存储接口
 */
public interface ImageMapStorage {
    ImageMap read(String name);

    Map<String, ImageMap> readAll();

    void save(ImageMap imageMap);

    void saveAll(Set<ImageMap> imageMaps);

    void remove(String name);

    void saveToFile();
}
