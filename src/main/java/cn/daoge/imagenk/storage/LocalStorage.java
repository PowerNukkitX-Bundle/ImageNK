package cn.daoge.imagenk.storage;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * 本地图片存储实现类
 */
@Getter
public class LocalStorage extends BufferedImageStorage {
    protected Path rootPath;

    public LocalStorage(Path rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public Set<String> loadAll() {
        Set<String> all = new HashSet<>();
        try (var stream = Files.walk(rootPath, 1)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                all.add(path.getName(path.getNameCount() - 1).toString());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return all;
    }

    @Override
    protected Image load(String name) {
        try {
            return ImageIO.read(rootPath.resolve(name).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
