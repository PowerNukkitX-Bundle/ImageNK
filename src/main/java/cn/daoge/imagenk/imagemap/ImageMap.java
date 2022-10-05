package cn.daoge.imagenk.imagemap;

import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import lombok.Builder;
import lombok.Getter;

/**
 * 描述服务器中一个已生成的图片信息
 */
@Getter
@Builder
public class ImageMap {
    protected String levelName;
    protected Vector3 pos1;
    protected Vector3 pos2;
    //图片文件名称
    protected String imageName;
    //标识名
    protected String id;
    //图片显示模式
    protected SimpleImageMapManager.SplitMode mode;

    public Level getLevel() {
        var server = Server.getInstance();
        return server.isLevelLoaded(levelName) ? server.getLevelByName(levelName) : (server.loadLevel(levelName) ? server.getLevelByName(levelName) : null);
    }

    public boolean isAvailable() {
        return getLevel() != null;
    }
}
