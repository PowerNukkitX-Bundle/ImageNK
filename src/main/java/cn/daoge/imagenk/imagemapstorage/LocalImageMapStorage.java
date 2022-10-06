package cn.daoge.imagenk.imagemapstorage;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemap.ImageMapAdapter;
import cn.nukkit.utils.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalImageMapStorage implements ImageMapStorage {

    protected static Gson gson = new GsonBuilder().registerTypeAdapter(ImageMap.class, new ImageMapAdapter()).create();
    protected Config dataConfig;

    public LocalImageMapStorage(Path path) {
        try {
            if (!Files.exists(path)) Files.createFile(path);
            this.dataConfig = new Config(path.toFile(), Config.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nullable ImageMap read(String name) {
        var json = this.dataConfig.getString(name, null);
        if (json == null) return null;
        return gson.fromJson(json, ImageMap.class);
    }

    @Override
    public Map<String, ImageMap> readAll() {
        var all = new HashMap<String, ImageMap>();
        this.dataConfig.getAll().forEach((k, v) -> {
            var image = read(k);
            if (image != null) all.put(image.getId(), image);
        });
        return all;
    }

    @Override
    public void save(ImageMap imageMap) {
        save(imageMap, true);
    }

    protected void save(ImageMap imageMap, boolean saveToFile) {
        var name = imageMap.getId();
        var json = gson.toJson(imageMap);
        this.dataConfig.set(name, json);
        if (saveToFile) saveToFile();
    }

    @Override
    public void saveAll(Set<ImageMap> imageMaps) {
        imageMaps.forEach(imageMap -> save(imageMap, false));
        saveToFile();
    }

    @Override
    public void remove(String name) {
        this.dataConfig.remove(name);
        saveToFile();
    }

    @Override
    public void saveToFile() {
        this.dataConfig.save();
    }
}
