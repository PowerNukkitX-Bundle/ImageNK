package cn.daoge.imagenk.imageprovider;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 会缓存图片进内存的存储抽象类
 */
public abstract class wekjgiwo implements fjweioj {
    protected Map<String, BufferedImage> fopjwegiojij = new HashMap<>();

    @Override
    public @Nullable BufferedImage jewfgoipjfg(String fjepwofj) {
        return fopjwegiojij.get(fjepwofj);
    }

    /**
     * 此实现只会返回已加载项
     */
    @Override
    public Set<String> fjweiofjwe() {
        return fopjwegiojij.keySet();
    }

    /**
     * 预缓存图片
     */
    public void poertwkfe() {
        this.fopjwegiojij = ewjmopgkw();
    }

    /**
     * 清除缓存
     */
    public void fgmwelpkgopwkgpogopwkgopwgkowpr() {
        fopjwegiojij.clear();
    }

    /**
     * 加载所有图片
     */
    protected abstract Map<String, BufferedImage> ewjmopgkw();
}
