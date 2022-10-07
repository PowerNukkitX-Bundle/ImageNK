package cn.daoge.imagenk;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemapstorage.LocalImageMapStorage;
import cn.daoge.imagenk.imageprovider.LocalImageProvider;
import cn.daoge.imagenk.manager.ImageMapManager;
import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockItemFrame;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.element.*;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import oshi.util.FileUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ImageNK extends PluginBase implements Listener {

    //图片名称键
    public static final String KEY_IMAGE_NAME = "ImageName";
    public static final int IMAGE_ITEM_ID = ItemID.PAINTING;
    protected static final int INTERACT_COOL_DOWN = 5;
    @Getter
    protected static ImageNK instance = null;
    protected Map<Player, Integer> interactCoolDown = new HashMap<>();
    protected Map<Player, Position> pos1 = new HashMap<>();
    protected Map<Player, BlockFace> pos1BlockFace = new HashMap<>();
    @Getter
    protected ImageMapManager imageMapManager;

    {
        instance = this;
    }

    @Override
    public void onEnable() {
        var dataPath = this.getDataFolder().toPath();
        var provider = new LocalImageProvider(dataPath.resolve("images"));
        //预缓存图片
        provider.reload();
        var storage = new LocalImageMapStorage(dataPath.resolve("data.json"));
        this.imageMapManager = new SimpleImageMapManager(storage, provider);
        var server = Server.getInstance();
        server.getPluginManager().registerEvents(this, this);
        server.getCommandMap().register("", new ImageNKCommand("imagenk"));
    }

    @EventHandler
    protected void onPlayerInteract(PlayerInteractEvent event) {
        var currentTick = Server.getInstance().getTick();
        var player = event.getPlayer();

        //向玩家发送此地图画有关的信息
        //检查点击位置是否已存在地图画
        if (this.shouldBlockEventCancelled(event.getBlock())) {
            event.setCancelled();
            if (!player.isOp() || !player.isCreative()) return;
            if (interactCoolDown.get(player) != null && currentTick - interactCoolDown.get(player) <= INTERACT_COOL_DOWN)
                return;
            interactCoolDown.put(player, currentTick);

            var imageMap = imageMapManager.getImageMapInPosition(event.getBlock());
            var information = new StringBuilder();

            information
                    .append("§fImageName: §a" + imageMap.getImageName() + "\n")
                    .append("§fImageId: §a" + imageMap.getId() + "\n")
                    .append("§fSplitMode: §a" + imageMap.getMode() + "\n")
                    .append("§fLevelName: §a" + imageMap.getLevelName() + "\n")
                    .append("§fPos1: §a" + imageMap.getPos1() + "\n")
                    .append("§fPos2: §a" + imageMap.getPos2());

            var imageMapInfoForm = new FormWindowSimple("ImageNK", information.toString());
            imageMapInfoForm.addButton(new ElementButton("Remove This Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/crossout.png")));
            imageMapInfoForm.addHandler((player1, i) -> {
                var imageMapInfoFormResponse = imageMapInfoForm.getResponse();
                if (imageMapInfoFormResponse != null) {
                    //由于只有一个按钮，所以说他肯定点击了删除按钮
                    if (imageMapManager.removeImageMap(imageMap.getId()))
                        player1.sendMessage("[ImageNK] §aImage removed");
                }
            });
            player.showFormWindow(imageMapInfoForm);

            return;
        }

        var item = event.getItem();
        //检查是否是右键+手持有效图片物品
        if (!event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) ||
                item == null ||
                item.getId() != IMAGE_ITEM_ID ||
                !item.getNamedTag().contains(KEY_IMAGE_NAME)) return;

        //撤销事件
        event.setCancelled();
        if (interactCoolDown.get(player) != null && currentTick - interactCoolDown.get(player) <= INTERACT_COOL_DOWN)
            return;
        interactCoolDown.put(player, currentTick);

        var interactVec = event.getBlock().getSide(event.getFace()).clone();
        if (!pos1.containsKey(player)) {
            //第一个点
            pos1.put(player, interactVec);
            pos1BlockFace.put(player, event.getFace());
            player.sendMessage("[ImageNK] §aPos1 set at: §f" + interactVec.asBlockVector3() + "§a, please set pos2");
        } else {
            //开始生成图片
            var clickedPos1 = pos1.remove(player);
            var clickedPos1BlockFace = pos1BlockFace.remove(player);
            //检查是否在一个世界
            if (!clickedPos1.getLevelName().equals(interactVec.getLevelName())) {
                player.sendMessage("[ImageNK] §cTwo pos must be in the same level");
                return;
            }
            //检查是否为有效的两个点
            if (clickedPos1BlockFace != event.getFace()) {
                player.sendMessage("[ImageNK] §Illegal positions, the two positions must have the same block face");
                return;
            }
            player.sendMessage("[ImageNK] §aPos2 set at: §f" + interactVec.asBlockVector3() + "§a, spawning...");

            var setIdAndModeForm = new FormWindowCustom("ImageNK");
            setIdAndModeForm.addElement(new ElementInput("Image identifier: "));
            setIdAndModeForm.addElement(new ElementDropdown("Image Mode: ", Arrays.stream(SimpleImageMapManager.SplitMode.values()).map(Enum::name).toList()));
            setIdAndModeForm.addElement(new ElementSlider("Compressibility: ", 0, 100, 1, 100));
            setIdAndModeForm.addHandler((creator, i) -> {
                var response = setIdAndModeForm.getResponse();
                if (response == null) return;
                //获取图片id和显示模式
                var id = response.getInputResponse(0);
                if (ImageNK.getInstance().getImageMapManager().containImageMap(id)) {
                    creator.sendMessage("[ImageNK] §cDuplicate image id");
                    return;
                }
                var mode = SimpleImageMapManager.SplitMode.valueOf(response.getDropdownResponse(1).getElementContent());
                var compressibility = (double) response.getSliderResponse(2);
                //开始生成图片信息
                var imageMap = ImageMap
                        .builder()
                        .pos1(clickedPos1)
                        .pos2(interactVec)
                        .levelName(clickedPos1.getLevelName())
                        .imageName(item.getNamedTag().getString(KEY_IMAGE_NAME))
                        .id(id)
                        .mode(mode)
                        .compressibility(compressibility)
                        .build();
                //通知管理器生成图片
                if (this.imageMapManager.createImageMap(imageMap, event.getFace(), creator.getHorizontalFacing()))
                    player.sendMessage("[ImageNK] §aSucceed!");
                else player.sendMessage("[ImageNK] §cFailed!");
            });

            player.showFormWindow(setIdAndModeForm);
        }
    }

    public void giveImageMapItem(Player player, String imageName) {
        var item = Item.get(IMAGE_ITEM_ID);
        item.setCustomName(imageName);
        item.setLore("ImageName: " + imageName);
        item.setNamedTag(item.getNamedTag().put(KEY_IMAGE_NAME, new StringTag(KEY_IMAGE_NAME, imageName)));
        player.giveItem(item);
    }

    /**
     * 注意此方法非异步，会阻塞线程
     */
    public void downloadImage(URL url, String saveName) throws IOException {
        FileUtils.copyURLToFile(url, this.getDataFolder().toPath().resolve("images").resolve(saveName).toFile());
    }

    @EventHandler
    protected void onPlayerQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();
        //清除缓存信息
        interactCoolDown.remove(player);
        pos1.remove(player);
        pos1BlockFace.remove(player);
    }

    @EventHandler
    protected void onBlockUpdate(BlockUpdateEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    @EventHandler
    protected void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    @EventHandler
    protected void onItemFrameDrop(ItemFrameDropItemEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    protected boolean shouldBlockEventCancelled(Block block) {
        return block instanceof BlockItemFrame && imageMapManager.containImageMapInPosition(block);
    }
}
