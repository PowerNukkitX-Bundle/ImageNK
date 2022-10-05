package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemapstorage.ImageMapStorage;
import cn.daoge.imagenk.imageprovider.ImageProvider;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockItemFrame;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.item.ItemMap;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.SimpleAxisAlignedBB;
import lombok.Getter;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class SimpleImageMapManager implements ImageMapManager{

    @Getter
    protected ImageMapStorage storage;
    @Getter
    protected ImageProvider provider;

    protected Map<String, ImageMap> imageMaps;

    public SimpleImageMapManager(ImageMapStorage storage, ImageProvider provider) {
        this.storage = storage;
        this.provider = provider;
        this.imageMaps = this.storage.readAll();
    }

    @Override
    public Map<String, ImageMap> getAllImageMap() {
        return this.imageMaps;
    }

    @Override
    public boolean createImageMap(ImageMap imageMap, BlockFace face) {
        var level = imageMap.getLevel();
        if (level == null) return false;

        var pos1 = imageMap.getPos1();
        var pos2 = imageMap.getPos2();

        var fullImage = this.provider.get(imageMap.getImageName());
        var id = imageMap.getId();

        //未找到图片
        if (fullImage == null) return false;

        var minX = Math.min(pos1.x, pos2.x);
        var minY = Math.min(pos1.y, pos2.y);
        var minZ = Math.min(pos1.z, pos2.z);

        var maxX = Math.max(pos1.x, pos2.x);
        var maxY = Math.max(pos1.y, pos2.y);
        var maxZ = Math.max(pos1.z, pos2.z);

        if (minX == maxX) {
            //在x维面延伸
            //分割图片
            var images = splitImage(fullImage, (int) Math.abs(pos1.y - pos2.y) + 1, (int) Math.abs(pos1.z - pos2.z) + 1, imageMap.getMode());
            switch (face) {
                case WEST -> {
                    //左上点为yMax & zMin
                    for (int tmpY = 0; maxY - tmpY >= minY; tmpY++) {
                        for (int tmpZ = 0; tmpZ + minZ <= maxZ; tmpZ++) {
                            var subImage = images[tmpZ][tmpY];
                            var pos = new Position(minX, maxY - tmpY, tmpZ + minZ, level);
                            level.setBlock(pos, Block.get(BlockID.AIR));

                            //获得地图
                            var itemMap = new ItemMap();
                            itemMap.setImage(subImage);

                            var frame = new BlockItemFrame();
                            frame.position(pos);
                            frame.setBlockFace(face);
                            frame.setStoringMap(true);
                            //设置显示地图
                            var blockEntity = BlockEntityHolder.setBlockAndCreateEntity(frame);
                            blockEntity.setItem(itemMap);
                            //更新方块实体以将数据发送到客户端
                            blockEntity.onUpdate();
                        }
                    }
                }
                case EAST -> {

                }
            }
        } else if (minY ==  maxY) {
            //todo: 在y维面延伸
            return false;
        } else if (minZ == maxZ) {
            //在z维面延伸

        } else {
            //两点无效
            return false;
        }

        this.storage.save(imageMap);
        return true;
    }

    @Override
    public boolean removeImageMap(String name) {
        var imageMap = imageMaps.get(name);
        if (imageMap == null) return false;
        var level = imageMap.getLevel();
        if (level == null) return false;
        var box = new SimpleAxisAlignedBB(imageMap.getPos1(), imageMap.getPos2());
        box.forEach((x, y, z) -> level.setBlock(x, y, z, Block.get(BlockID.AIR), false, true));
        return true;
    }

    @Override
    public boolean containImageMap(String name) {
        return this.imageMaps.containsKey(name);
    }

    @Override
    public boolean containImageMapInPosition(Position pos) {
        return this.imageMaps.values().stream().anyMatch(imageMap -> {
            if (!pos.level.getName().equals(imageMap.getLevel().getName())) return false;
            var box = new SimpleAxisAlignedBB(imageMap.getPos1(), imageMap.getPos2());
            return box.isVectorInside(pos);
        });
    }

    /**
     * 分割源图片使得其可被放入地图(128*128)中
     * @param image 源图片
     * @param height 预先准备的垂直地图数量
     * @param width 预先准备的水平地图数量
     * @param mode 分割模式
     * @return BufferedImage[x][y] （左上角开始）
     */
    protected BufferedImage[][] splitImage(BufferedImage image, int height, int width, SplitMode mode) {
        switch (mode) {
            case FILL -> {
                //填充模式
                //预先将图片拉伸到指定大小，提高画面质量
                var scaledInstance = image.getScaledInstance(width * 128, height * 128, Image.SCALE_SMOOTH);
                var newImage = new BufferedImage(width * 128, height * 128, image.getType());
                var gr = newImage.getGraphics();
                gr.drawImage(scaledInstance, 0, 0, null);
                gr.dispose();
                image = newImage;

                BufferedImage[][] imgs = new BufferedImage[width][height];
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        imgs[x][y] = new BufferedImage(128, 128, image.getType());
                        Graphics2D gr1 = imgs[x][y].createGraphics();
                        gr1.drawImage(image, 0, 0, 128, 128, 128 * x, 128 * y, 128 * x + 128, 128 * y + 128, null);
                        gr1.dispose();
                    }
                }
                return imgs;
            }
            case CENTER -> {
                //居中模式
                //计算背景色（通过混合所有像素的RGB）
                long r = 0;
                long g = 0;
                long b = 0;
                for (int imageX = 0; imageX < image.getWidth(); imageX++) {
                    for (int imageY = 0; imageY < image.getHeight(); imageY++) {
                        var color = new Color(image.getRGB(imageX, imageY));
                        r += color.getRed();
                        g += color.getGreen();
                        b += color.getBlue();
                    }
                }
                //总的像素数量
                var pixelCount = ((long) (image.getWidth() + 1) * (long) (image.getHeight() + 1));
                //计算出平均RGB
                var backgroundColor = new Color((int) (r / pixelCount), (int) (g / pixelCount), (int) (b / pixelCount));

                //新的正方形图片
                var fullImage = new BufferedImage(width * 128, height * 128, image.getType());

                //若分配的地图大小不够装下整个图片，需要缩放图片
                if (image.getHeight() > fullImage.getHeight() || image.getWidth() > fullImage.getWidth()) {
                    double k;
                    if (image.getHeight() - fullImage.getHeight() > image.getWidth() - fullImage.getWidth()) {
                        //优先满足高度缩放
                        k = (double)fullImage.getHeight() / (double)image.getHeight();
                    } else {
                        //优先满足宽度缩放
                        k = (double)fullImage.getWidth() / (double)image.getWidth();
                    }

                    var newImage = new BufferedImage((int) (k * image.getWidth()), (int) (k * image.getHeight()), image.getType());
                    var gr = newImage.getGraphics();
                    gr.drawImage(image.getScaledInstance(newImage.getWidth(), newImage.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
                    gr.dispose();
                    image = newImage;
                }

                //计算源图片到此正方形空间中心需要的偏移量
                int offsetX = (fullImage.getWidth() - image.getWidth()) / 2;
                int offsetY = (fullImage.getHeight() - image.getHeight()) / 2;

                var gr1 = fullImage.createGraphics();
                //设置画笔颜色
                gr1.setColor(backgroundColor);
                //填充整个图像为背景色
                gr1.fillRect(0, 0, fullImage.getWidth(), fullImage.getHeight());

                //将源图片复制到中间
                gr1.drawImage(image,
                            offsetX, offsetY, offsetX + image.getWidth(), offsetY + image.getHeight(),
                            0, 0, image.getWidth(), image.getHeight(), null);
                gr1.dispose();

                BufferedImage[][] imgs = new BufferedImage[width][height];
                for (int x = 0; x < width; ++x) {
                    for (int y = 0; y < height; ++y) {
                        imgs[x][y] = new BufferedImage(128, 128, fullImage.getType());
                        Graphics2D gr2 = imgs[x][y].createGraphics();
                        gr2.drawImage(fullImage, 0, 0, 128, 128, 128 * x, 128 * y, 128 * x + 128, 128 * y + 128, null);
                        gr2.dispose();
                    }
                }

                return imgs;
            }
        }
        return null;
    }

    public enum SplitMode {
        //居中
        CENTER,
        //填充
        FILL
    }
}
