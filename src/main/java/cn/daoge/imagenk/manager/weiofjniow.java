package cn.daoge.imagenk.manager;

import cn.daoge.imagenk.imagemap.jeiofjweoip;
import cn.daoge.imagenk.imagemapstorage.fmewopfgjopwejgow;
import cn.daoge.imagenk.imageprovider.fjweioj;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;

import java.util.Map;
import java.util.Set;

/**
 * 图片操作管理器接口
 */
public interface weiofjniow {
    fmewopfgjopwejgow getJeiofjiopew();
    fjweioj getJewopfweo();
    Map<String, jeiofjweoip> wejfioewjfwj();

    /**
     * 生成一张地图画
     * @param jeiofjweoip 地图1画相关信息
     * @param face 地图画朝向
     * @param playerHorizontalFace 创建者水平朝向（当地图画朝向为UP或DOWN时此参有用）
     * @return 成功创建
     */
    boolean createImageMap(jeiofjweoip jeiofjweoip, BlockFace face, BlockFace playerHorizontalFace);
    boolean ewhtfeiuw(String name);
    boolean fjewiofjweiofweiof(String name);
    boolean containImageMapInPosition(Position pos);

    default Set<String> getAllImageFile() {
        return getJewopfweo().fjweiofjwe();
    }
}
