package cn.daoge.imagenk;

import cn.daoge.imagenk.imagemap.ImageMap;
import cn.daoge.imagenk.imagemapstorage.LocalImageMapStorage;
import cn.daoge.imagenk.imageprovider.LocalImageProvider;
import cn.daoge.imagenk.manager.ImageMapManager;
import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ImageNK extends PluginBase implements Listener {

    @Getter
    protected static ImageNK instance = null;
    protected static final int INTERACT_COOL_DOWN = 10;
    //图片名称键
    public static final String KEY_IMAGE_NAME = "ImageName";
    public static final int IMAGE_ITEM_ID = ItemID.PAINTING;

    protected Map<Player, Integer> interactCoolDown = new HashMap<>();
    protected Map<Player, Position> pos1 = new HashMap<>();

    {instance = this;}

    @Getter
    protected ImageMapManager imageMapManager;

    @Override
    public void onEnable() {
        var dataPath = this.getDataFolder().toPath();
        var provider = new LocalImageProvider(dataPath.resolve("images"));
        //预缓存图片
        provider.cache();
        var storage = new LocalImageMapStorage(dataPath.resolve("data.json"));
        this.imageMapManager = new SimpleImageMapManager(storage, provider);
        var server = Server.getInstance();
        server.getPluginManager().registerEvents(this, this);
        server.getCommandMap().register("", new ImageNKCommand("imagenk"));
    }

    @EventHandler
    protected void onPlayerInteract(PlayerInteractEvent event) {
        event.getPlayer().sendMessage(event.getFace().name());
        var item = event.getItem();
        //检查是否是右键+手持有效图片物品
        if (!event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) ||
                item == null ||
                item.getId() != IMAGE_ITEM_ID ||
                !item.getNamedTag().contains(KEY_IMAGE_NAME)) return;

        event.setCancelled();
        var player = event.getPlayer();
        var currentTick = Server.getInstance().getTick();
        if (interactCoolDown.get(player) != null && currentTick - interactCoolDown.get(player) <= INTERACT_COOL_DOWN) return;
        interactCoolDown.put(player, currentTick);

        var interactVec = event.getBlock();
        if (!pos1.containsKey(player)) {
            //第一个点
            pos1.put(player, interactVec);
            player.sendMessage("[ImageNK] §aPos1 set at: §f" + interactVec.asBlockVector3() + "§a, please set pos2");
        } else {
            //开始生成图片
            var clickedPos1 = pos1.remove(player);
            //检查是否在一个世界
            if (!clickedPos1.getLevelName().equals(interactVec.getLevelName())) {
                player.sendMessage("[ImageNK] §cTwo pos must be in the same level");
                return;
            }
            player.sendMessage("[ImageNK] §aPos2 set at: §f" + interactVec.asBlockVector3() + "§a, spawning...");

            var setIdAndModeForm = new FormWindowCustom("ImageNK");
            setIdAndModeForm.addElement(new ElementInput("Image identifier: "));
            setIdAndModeForm.addElement(new ElementDropdown("Image Mode: ", Arrays.stream(SimpleImageMapManager.SplitMode.values()).map(Enum::name).toList()));
            setIdAndModeForm.addHandler((creator, i) -> {
                var response = setIdAndModeForm.getResponse();
                if (response == null) return;
                //获取图片id和显示模式
                var id = response.getInputResponse(0);
                var mode = SimpleImageMapManager.SplitMode.valueOf(response.getDropdownResponse(1).getElementContent());
                //开始生成图片信息
                var imageMap = ImageMap
                        .builder()
                        .pos1(clickedPos1)
                        .pos2(interactVec)
                        .levelName(clickedPos1.getLevelName())
                        .imageName(item.getNamedTag().getString(KEY_IMAGE_NAME))
                        .id(id)
                        .mode(mode)
                        .build();
                //通知管理器生成图片
                if (this.imageMapManager.createImageMap(imageMap, event.getFace())) player.sendMessage("[ImageNK] §aSucceed!");
                else player.sendMessage("[ImageNK] §cFailed!");
            });

            player.showFormWindow(setIdAndModeForm);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        interactCoolDown.remove(event.getPlayer());
    }

    public void giveImageMapItem(Player player, String imageName) {
        var item = Item.get(IMAGE_ITEM_ID);
        item.setCustomName(imageName);
        item.setLore("ImageName: " + imageName);
        item.setNamedTag(item.getNamedTag().put(KEY_IMAGE_NAME, new StringTag(KEY_IMAGE_NAME, imageName)));
        player.giveItem(item);
    }
}
