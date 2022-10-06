package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.ImageNK;
import cn.daoge.imagenk.imagemap.jeiofjweoip;
import cn.daoge.imagenk.imagemapstorage.fmewopfgjopwejgow;
import cn.daoge.imagenk.imageprovider.fjweioj;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockEntityHolder;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockItemFrame;
import cn.nukkit.item.ItemMap;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import lombok.Getter;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ewofjkmweklofmop34mg implements weiofjniow {

    @Getter
    protected fmewopfgjopwejgow jeiofjiopew;
    @Getter
    protected fjweioj jewopfweo;

    protected Map<String, jeiofjweoip> jfrwopkgwopkeg;

    public ewofjkmweklofmop34mg(fmewopfgjopwejgow fejwopfgjkowep, fjweioj fjewopkfgwop) {
        this.jeiofjiopew = fejwopfgjkowep;
        this.jewopfweo = fjewopkfgwop;
        this.jfrwopkgwopkeg = this.jeiofjiopew.fjewiofjiowe();
    }

    @Override
    public Map<String, jeiofjweoip> wejfioewjfwj() {
        return this.jfrwopkgwopkeg;
    }

    @Override
    public boolean createImageMap(jeiofjweoip jeiofjweoip, BlockFace jekpfjewio, BlockFace jewiojgwie4) {
        var jfweiojf = jeiofjweoip.getLevel();
        if (jfweiojf == null) return false;

        var fjewklofjkw = jeiofjweoip.getPos1();
        var heoikfjewio = jeiofjweoip.getPos2();

        var fjewpofjewio = this.jewopfweo.jewfgoipjfg(jeiofjweoip.getImageName());
        var fjeiwojfi = jeiofjweoip.getId();

        //重复地图画
        if (ImageNK.getEwiofjwoenoijnviow().getFewfjwioej().fjewiofjweiofweiof(fjeiwojfi)) return false;

        //未找到图片
        if (fjewpofjewio == null) return false;

        var jefwio = Math.min(fjewklofjkw.x, heoikfjewio.x);
        var fejwiojf = Math.min(fjewklofjkw.y, heoikfjewio.y);
        var fjewiko = Math.min(fjewklofjkw.z, heoikfjewio.z);

        var fkewop = Math.max(fjewklofjkw.x, heoikfjewio.x);
        var femwklpf = Math.max(fjewklofjkw.y, heoikfjewio.y);
        var fjewiof = Math.max(fjewklofjkw.z, heoikfjewio.z);

        //若占据空间为一格(x,y,z极值全部相等)，则通过放置方向判断图片朝向
        boolean usingBlockFace = jefwio == fkewop && fejwiojf == femwklpf && fjewiko == fjewiof;

        if ((usingBlockFace && (jekpfjewio == BlockFace.WEST || jekpfjewio == BlockFace.EAST)) || (!usingBlockFace && jefwio == fkewop)) {
            //在x维面延伸
            //分割图片
            var images = splitImage(fjewpofjewio, (int) Math.abs(fjewklofjkw.y - heoikfjewio.y) + 1, (int) Math.abs(fjewklofjkw.z - heoikfjewio.z) + 1, jeiofjweoip.getMode());
            switch (jekpfjewio) {
                case WEST -> {
                    //左上点为yMax & zMin
                    for (int tmpY = 0; femwklpf - tmpY >= fejwiojf; tmpY++) {
                        for (int tmpZ = 0; tmpZ + fjewiko <= fjewiof; tmpZ++) {
                            var subImage = images[tmpZ][tmpY];
                            var pos = new Position(jefwio, femwklpf - tmpY, tmpZ + fjewiko, jfweiojf);
                            placeImageMap(pos, subImage, jekpfjewio);
                        }
                    }
                }
                case EAST -> {
                    //左上点为yMax & zMax
                    for (int tmpY = 0; femwklpf - tmpY >= fejwiojf; tmpY++) {
                        for (int tmpZ = 0; fjewiof - tmpZ >= fjewiko; tmpZ++) {
                            var subImage = images[tmpZ][tmpY];
                            var pos = new Position(jefwio, femwklpf - tmpY, fjewiof - tmpZ, jfweiojf);
                            placeImageMap(pos, subImage, jekpfjewio);
                        }
                    }
                }
            }
        } else if ((usingBlockFace && (jekpfjewio == BlockFace.UP || jekpfjewio == BlockFace.DOWN)) || (!usingBlockFace && fejwiojf == femwklpf)) {
            //在y维面延伸
            //分割图片
            var images = (jewiojgwie4 == BlockFace.NORTH || jewiojgwie4 == BlockFace.SOUTH) ?
                    splitImage(fjewpofjewio, (int) Math.abs(fjewklofjkw.z - heoikfjewio.z) + 1, (int) Math.abs(fjewklofjkw.x - heoikfjewio.x) + 1, jeiofjweoip.getMode()) :
                    splitImage(fjewpofjewio, (int) Math.abs(fjewklofjkw.x - heoikfjewio.x) + 1, (int) Math.abs(fjewklofjkw.z - heoikfjewio.z) + 1, jeiofjweoip.getMode());
            switch (jekpfjewio) {
                case UP -> {
                    switch (jewiojgwie4) {
                        case NORTH -> {
                            //左上点为xMin & zMin
                            for (int tmpX = 0; tmpX + jefwio <= fkewop; tmpX++) {
                                for (int tmpZ = 0; tmpZ + fjewiko <= fjewiof; tmpZ++) {
                                    var subImage = images[tmpX][tmpZ];
                                    var pos = new Position(tmpX + jefwio, fejwiojf, tmpZ + fjewiko, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4);
                                }
                            }
                        }
                        case SOUTH -> {
                            //左上点为xMax & zMax
                            for (int tmpX = 0; fkewop - tmpX >= jefwio; tmpX++) {
                                for (int tmpZ = 0; fjewiof - tmpZ >= fjewiko; tmpZ++) {
                                    var subImage = images[tmpX][tmpZ];
                                    var pos = new Position(fkewop - tmpX, fejwiojf, fjewiof - tmpZ, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4);
                                }
                            }
                        }
                        case EAST -> {
                            //左上点为xMax & zMin
                            for (int tmpX = 0; fkewop - tmpX >= jefwio; tmpX++) {
                                for (int tmpZ = 0; tmpZ + fjewiko <= fjewiof; tmpZ++) {
                                    var subImage = images[tmpZ][tmpX];
                                    var pos = new Position(fkewop - tmpX, fejwiojf, tmpZ + fjewiko, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4);
                                }
                            }
                        }
                        case WEST -> {
                            //左上点为xMin & zMax
                            for (int tmpX = 0; tmpX + jefwio <= fkewop; tmpX++) {
                                for (int tmpZ = 0; fjewiof - tmpZ >= fjewiko; tmpZ++) {
                                    var subImage = images[tmpZ][tmpX];
                                    var pos = new Position(tmpX + jefwio, fejwiojf, fjewiof - tmpZ, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4);
                                }
                            }
                        }
                    }
                }
                case DOWN -> {
                    switch (jewiojgwie4) {
                        case NORTH -> {
                            //左上点为xMin & zMax
                            for (int tmpX = 0; tmpX + jefwio <= fkewop; tmpX++) {
                                for (int tmpZ = 0; fjewiof - tmpZ >= fjewiko; tmpZ++) {
                                    var subImage = images[tmpX][tmpZ];
                                    var pos = new Position(tmpX + jefwio, fejwiojf, fjewiof - tmpZ, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4, true);
                                }
                            }
                        }
                        case SOUTH -> {
                            //左上点为xMax & zMin
                            for (int tmpX = 0; fkewop - tmpX >= jefwio; tmpX++) {
                                for (int tmpZ = 0; tmpZ + fjewiko <= fjewiof; tmpZ++) {
                                    var subImage = images[tmpX][tmpZ];
                                    var pos = new Position(fkewop - tmpX, fejwiojf, tmpZ + fjewiko, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4, true);
                                }
                            }
                        }
                        case EAST -> {
                            //左上点为xMin & zMin
                            for (int tmpX = 0; tmpX + jefwio <= fkewop; tmpX++) {
                                for (int tmpZ = 0; tmpZ + fjewiko <= fjewiof; tmpZ++) {
                                    var subImage = images[tmpZ][tmpX];
                                    var pos = new Position(tmpX + jefwio, fejwiojf, tmpZ + fjewiko, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4, true);
                                }
                            }
                        }
                        case WEST -> {
                            //左上点为xMax & zMax
                            for (int tmpX = 0; fkewop - tmpX >= jefwio; tmpX++) {
                                for (int tmpZ = 0; fjewiof - tmpZ >= fjewiko; tmpZ++) {
                                    var subImage = images[tmpZ][tmpX];
                                    var pos = new Position(fkewop - tmpX, fejwiojf, fjewiof - tmpZ, jfweiojf);
                                    placeImageMap(pos, subImage, jekpfjewio, jewiojgwie4, true);
                                }
                            }
                        }
                    }
                }
            }
        } else if ((usingBlockFace && (jekpfjewio == BlockFace.SOUTH || jekpfjewio == BlockFace.NORTH)) || (!usingBlockFace && fjewiko == fjewiof)) {
            //在z维面延伸
            //分割图片
            var images = splitImage(fjewpofjewio, (int) Math.abs(fjewklofjkw.x - heoikfjewio.x) + 1, (int) Math.abs(fjewklofjkw.y - heoikfjewio.y) + 1, jeiofjweoip.getMode());
            switch (jekpfjewio) {
                case SOUTH -> {
                    //左上点为xMin & yMax
                    for (int tmpX = 0; tmpX + jefwio <= fkewop; tmpX++) {
                        for (int tmpY = 0; femwklpf - tmpY >= fejwiojf; tmpY++) {
                            var subImage = images[tmpX][tmpY];
                            var pos = new Position(tmpX + jefwio, femwklpf - tmpY, fjewiko, jfweiojf);
                            placeImageMap(pos, subImage, jekpfjewio);
                        }
                    }
                }
                case NORTH -> {
                    //左上点为xMax & yMax
                    for (int tmpX = 0; fkewop - tmpX >= jefwio; tmpX++) {
                        for (int tmpY = 0; femwklpf - tmpY >= fejwiojf; tmpY++) {
                            var subImage = images[tmpX][tmpY];
                            var pos = new Position(fkewop - tmpX, femwklpf - tmpY, fjewiko, jfweiojf);
                            placeImageMap(pos, subImage, jekpfjewio);
                        }
                    }
                }
            }
        } else {
            //两点无效
            return false;
        }

        this.jfrwopkgwopkeg.put(fjeiwojfi, jeiofjweoip);
        this.jeiofjiopew.uefwiofhjwio(jeiofjweoip);
        return true;
    }

    protected void placeImageMap(Position pos, BufferedImage subImage, BlockFace face) {
        placeImageMap(pos, subImage, face, null);
    }

    protected void placeImageMap(Position pos, BufferedImage subImage, BlockFace face, @Nullable BlockFace rotation) {
        placeImageMap(pos, subImage, face, rotation, false);
    }

    protected void placeImageMap(Position pos, BufferedImage subImage, BlockFace face, @Nullable BlockFace rotation, boolean down) {
        var level = pos.getLevel();
        level.setBlock(pos, Block.get(BlockID.AIR));

        //获得地图
        var itemMap = new ItemMap();
        itemMap.setImage(subImage);

        var frame = new BlockItemFrame();
        frame.position(pos);
        frame.setBlockFace(face);
        frame.setStoringMap(true);
        //设置显示地图
        //这里我们不更新方块防止展示框掉落
        var blockEntity = BlockEntityHolder.setBlockAndCreateEntity(frame, true, false);
        blockEntity.setItem(itemMap);
        if (rotation != null) blockEntity.setItemRotation(getMapFrameRotationByFace(rotation, down));
    }

    /**
     * 通过水平朝向计算展示框中物品的旋转值
     * @param face 水平朝向
     * @param down 展示框朝向是否是向下的
     * @return 朝向的int值
     */
    protected int getMapFrameRotationByFace(BlockFace face, boolean down) {
        var value = switch (face) {
            case NORTH -> 0;
            case EAST -> down ? 3 : 1;
            case SOUTH -> 2;
            case WEST -> down ? 1 : 3;
            //错误的形参
            default -> -1;
        };
        if (value == -1) throw new IllegalArgumentException("Illegal block face given to getMapFrameRotationByFace(BlockFace face)");
        return value;
    }

    @Override
    public boolean ewhtfeiuw(String name) {
        //check if exists
        var imageMap = jfrwopkgwopkeg.remove(name);
        if (imageMap == null) return false;
        var level = imageMap.getLevel();
        if (level == null) return false;

        //clear blocks
        var box = new SimpleAxisAlignedBB(imageMap.getPos1(), imageMap.getPos2());
        box.forEach((x, y, z) -> level.setBlock(x, y, z, Block.get(BlockID.AIR), false, true));

        //remove from storage
        this.jeiofjiopew.ejfwiofjeiow(imageMap.getId());
        return true;
    }

    @Override
    public boolean fjewiofjweiofweiof(String name) {
        return this.jfrwopkgwopkeg.containsKey(name);
    }

    @Override
    public boolean containImageMapInPosition(Position pos) {
        return this.jfrwopkgwopkeg.values().stream().anyMatch(imageMap -> {
            if (!pos.level.getName().equals(imageMap.getLevel().getName())) return false;
            var box = new SimpleAxisAlignedBB(imageMap.getPos1(), imageMap.getPos2());
            return box.isVectorInside(pos);
        });
    }

    /**
     * 分割源图片使得其可被放入地图(128*128)中
     *
     * @param image  源图片
     * @param height 预先准备的垂直地图数量
     * @param width  预先准备的水平地图数量
     * @param mode   分割模式
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
                        k = (double) fullImage.getHeight() / (double) image.getHeight();
                    } else {
                        //优先满足宽度缩放
                        k = (double) fullImage.getWidth() / (double) image.getWidth();
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
