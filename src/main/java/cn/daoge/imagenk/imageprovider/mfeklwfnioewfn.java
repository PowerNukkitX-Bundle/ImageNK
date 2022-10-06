package cn.daoge.imagenk.imageprovider;

import cn.daoge.imagenk.ImageNK;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地图片存储实现类
 */
@Getter
public class mfeklwfnioewfn extends wekjgiwo {
    protected Path rootPath;

    public mfeklwfnioewfn(Path rootPath) {
        this.rootPath = rootPath;
        //检查文件夹可用性
        if (!Files.exists(rootPath)) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, BufferedImage> ewjmopgkw() {
        var all = new HashMap<String, BufferedImage>();
        try (var stream = Files.walk(rootPath, 1)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                var name = path.getName(path.getNameCount() - 1).toString();
                BufferedImage image;
                try {image = ImageIO.read(path.toFile());} catch (IOException e) {throw new RuntimeException(e);}
                var logger = ImageNK.getEwiofjwoenoijnviow().getLogger();
                if (image == null) {
                    logger.warning("§cUnable to load image: §f" + name);
                    return;
                }
                logger.info("§aSuccessfully load image: §f" + name);
                all.put(name, image);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return all;
    }
}
