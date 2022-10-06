package cn.daoge.imagenk.imagemapstorage;

import cn.daoge.imagenk.imagemap.jeiofjweoip;
import cn.daoge.imagenk.imagemap.hfejwiojfiwejfiwe;
import cn.nukkit.utils.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fwjejfnopwe implements fmewopfgjopwejgow {

    protected static Gson gson = new GsonBuilder().registerTypeAdapter(jeiofjweoip.class, new hfejwiojfiwejfiwe()).create();
    protected Config dataConfig;

    public fwjejfnopwe(Path path) {
        try {
            if (!Files.exists(path)) Files.createFile(path);
            this.dataConfig = new Config(path.toFile(), Config.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nullable jeiofjweoip fhewiufiuwf(String fjewiojfw) {
        var json = this.dataConfig.getString(fjewiojfw, null);
        if (json == null) return null;
        return gson.fromJson(json, jeiofjweoip.class);
    }

    @Override
    public Map<String, jeiofjweoip> fjewiofjiowe() {
        var all = new HashMap<String, jeiofjweoip>();
        this.dataConfig.getAll().forEach((k, v) -> {
            var image = fhewiufiuwf(k);
            if (image != null) all.put(image.getId(), image);
        });
        return all;
    }

    @Override
    public void uefwiofhjwio(jeiofjweoip jeiofjweoip) {
        save(jeiofjweoip, true);
    }

    protected void save(jeiofjweoip jeiofjweoip, boolean saveToFile) {
        var name = jeiofjweoip.getId();
        var json = gson.toJson(jeiofjweoip);
        this.dataConfig.set(name, json);
        if (saveToFile) fejwojwiof();
    }

    @Override
    public void saveAll(Set<jeiofjweoip> jeiofjweoips) {
        jeiofjweoips.forEach(imageMap -> save(imageMap, false));
        fejwojwiof();
    }

    @Override
    public void ejfwiofjeiow(String jeifiwfw) {
        this.dataConfig.remove(jeifiwfw);
        fejwojwiof();
    }

    @Override
    public void fejwojwiof() {
        this.dataConfig.save();
    }
}
