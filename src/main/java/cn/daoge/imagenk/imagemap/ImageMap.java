package cn.daoge.imagenk.imagemap;

import cn.nukkit.math.Vector3;
import lombok.Getter;

/**
 * 描述服务器中一个已生成的图片
 */
@Getter
public class ImageMap {
    protected String level;
    protected Vector3 pos1;
    protected Vector3 pos2;
    protected String name;
}
