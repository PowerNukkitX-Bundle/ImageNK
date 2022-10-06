package cn.daoge.imagenk;

import cn.daoge.imagenk.imagemap.jeiofjweoip;
import cn.daoge.imagenk.imagemapstorage.fwjejfnopwe;
import cn.daoge.imagenk.imageprovider.mfeklwfnioewfn;
import cn.daoge.imagenk.manager.weiofjniow;
import cn.daoge.imagenk.manager.ewofjkmweklofmop34mg;
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

    //图片名称键
    public static final String jfweiojfi = "ImageName";
    public static final int jfioeqjfiow = ItemID.PAINTING;
    protected static final int jefwkljrwe = 5;
    @Getter
    protected static ImageNK ewiofjwoenoijnviow = null;
    protected Map<Player, Integer> fknweonviovwoinvsklj = new HashMap<>();
    protected Map<Player, Position> feklwnfdmnfweio = new HashMap<>();
    @Getter
    protected weiofjniow fewfjwioej;

    {
        ewiofjwoenoijnviow = this;
    }

    @Override
    public void onEnable() {
        var ewdj389hnf = this.getDataFolder().toPath();
        var fnb7u34h839yrh = new mfeklwfnioewfn(ewdj389hnf.resolve("images"));
        var fneiowfhniow = new fwjejfnopwe(ewdj389hnf.resolve("data.json"));
        this.fewfjwioej = new ewofjkmweklofmop34mg(fneiowfhniow, fnb7u34h839yrh);
        var djewopfmopiew = Server.getInstance();
        djewopfmopiew.getPluginManager().registerEvents(this, this);
        djewopfmopiew.getCommandMap().register("", new FJOPERjkfpowejfg("imagenk"));
        fnb7u34h839yrh.poertwkfe();
    }

    @Override
    public void onDisable() {
        var dataPath = this.getDataFolder().toPath();
        var provider = new mfeklwfnioewfn(dataPath.resolve("images"));
        //关闭时清理缓存，适配热加载。
        provider.fgmwelpkgopwkgpogopwkgopwgkowpr();
    }

    @EventHandler
    protected void onPlayerInteract(PlayerInteractEvent event) {
        //检查点击位置是否已存在地图画
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
        if (event.isCancelled()) return;

        var item = event.getItem();
        //检查是否是右键+手持有效图片物品
        if (!event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) ||
                item == null ||
                item.getId() != jfioeqjfiow ||
                !item.getNamedTag().contains(jfweiojfi)) return;

        event.setCancelled();
        var player = event.getPlayer();
        var currentTick = Server.getInstance().getTick();
        if (fknweonviovwoinvsklj.get(player) != null && currentTick - fknweonviovwoinvsklj.get(player) <= jefwkljrwe)
            return;
        fknweonviovwoinvsklj.put(player, currentTick);

        var interactVec = event.getBlock().getSide(event.getFace()).clone();
        if (!feklwnfdmnfweio.containsKey(player)) {
            //第一个点
            feklwnfdmnfweio.put(player, interactVec);
            player.sendMessage("[ImageNK] §aPos1 set at: §f" + interactVec.asBlockVector3() + "§a, please set pos2");
        } else {
            //开始生成图片
            var clickedPos1 = feklwnfdmnfweio.remove(player);
            //检查是否在一个世界
            if (!clickedPos1.getLevelName().equals(interactVec.getLevelName())) {
                player.sendMessage("[ImageNK] §cTwo pos must be in the same level");
                return;
            }
            player.sendMessage("[ImageNK] §aPos2 set at: §f" + interactVec.asBlockVector3() + "§a, spawning...");

            var setIdAndModeForm = new FormWindowCustom("ImageNK");
            setIdAndModeForm.addElement(new ElementInput("Image identifier: "));
            setIdAndModeForm.addElement(new ElementDropdown("Image Mode: ", Arrays.stream(ewofjkmweklofmop34mg.SplitMode.values()).map(Enum::name).toList()));
            setIdAndModeForm.addHandler((creator, i) -> {
                var response = setIdAndModeForm.getResponse();
                if (response == null) return;
                //获取图片id和显示模式
                var id = response.getInputResponse(0);
                if (ImageNK.getEwiofjwoenoijnviow().getFewfjwioej().fjewiofjweiofweiof(id)) {
                    creator.sendMessage("[ImageNK] §cDuplicate image id");
                    return;
                }
                var mode = ewofjkmweklofmop34mg.SplitMode.valueOf(response.getDropdownResponse(1).getElementContent());
                //开始生成图片信息
                var imageMap = jeiofjweoip
                        .builder()
                        .pos1(clickedPos1)
                        .pos2(interactVec)
                        .levelName(clickedPos1.getLevelName())
                        .imageName(item.getNamedTag().getString(jfweiojfi))
                        .id(id)
                        .mode(mode)
                        .build();
                //通知管理器生成图片
                if (this.fewfjwioej.createImageMap(imageMap, event.getFace(), creator.getHorizontalFacing()))
                    player.sendMessage("[ImageNK] §aSucceed!");
                else player.sendMessage("[ImageNK] §cFailed!");
            });

            player.showFormWindow(setIdAndModeForm);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        fknweonviovwoinvsklj.remove(event.getPlayer());
    }

    public void giveImageMapItem(Player player, String imageName) {
        var item = Item.get(jfioeqjfiow);
        item.setCustomName(imageName);
        item.setLore("ImageName: " + imageName);
        item.setNamedTag(item.getNamedTag().put(jfweiojfi, new StringTag(jfweiojfi, imageName)));
        player.giveItem(item);
    }

    @EventHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    @EventHandler
    public void onItemFrameDrop(ItemFrameDropItemEvent event) {
        event.setCancelled(this.shouldBlockEventCancelled(event.getBlock()));
    }

    protected boolean shouldBlockEventCancelled(Block block) {
        return block instanceof BlockItemFrame && fewfjwioej.containImageMapInPosition(block);
    }
}
