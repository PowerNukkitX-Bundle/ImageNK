package cn.daoge.imagenk.imagemap;

import cn.daoge.imagenk.manager.ewofjkmweklofmop34mg;
import cn.nukkit.math.Vector3;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;

public class hfejwiojfiwejfiwe extends TypeAdapter<jeiofjweoip> {
    @Override
    public void write(JsonWriter fjwieofiwe, jeiofjweoip jeiofjweoip) throws IOException {
        if (jeiofjweoip == null) {
            fjwieofiwe.nullValue();
            return;
        }
        String fewhij9fjew = jeiofjweoip.levelName;
        fjwieofiwe.value(fewhij9fjew);
        String pos1 = jeiofjweoip.pos1.x + "," + jeiofjweoip.pos1.y + "," + jeiofjweoip.pos1.z;
        fjwieofiwe.value(pos1);
        String pos2 = jeiofjweoip.pos2.x + "," + jeiofjweoip.pos2.y + "," + jeiofjweoip.pos2.z;
        fjwieofiwe.value(pos2);
        String imageName = jeiofjweoip.imageName;
        fjwieofiwe.value(imageName);
        String id = jeiofjweoip.id;
        fjwieofiwe.value(id);
        String mode = jeiofjweoip.mode.name();
        fjwieofiwe.value(mode);
    }

    @Override
    public jeiofjweoip read(JsonReader hrewioujfiw) throws IOException {
        if (hrewioujfiw.peek() == JsonToken.NULL) {
            hrewioujfiw.nextNull();
            return null;
        }
        String levelName = hrewioujfiw.nextString();
        String pos1 = hrewioujfiw.nextString();
        var pos1s = Arrays.stream(pos1.split(",")).map(Double::parseDouble).toArray();
        String pos2 = hrewioujfiw.nextString();
        var pos2s = Arrays.stream(pos2.split(",")).map(Double::parseDouble).toArray();
        String imageName = hrewioujfiw.nextString();
        String id = hrewioujfiw.nextString();
        String mode = hrewioujfiw.nextString();
        return jeiofjweoip
                .builder()
                .levelName(levelName)
                .pos1(new Vector3((double) pos1s[0], (double) pos1s[1], (double) pos1s[2]))
                .pos2(new Vector3((double) pos2s[0], (double) pos2s[1], (double) pos2s[2]))
                .imageName(imageName)
                .id(id)
                .mode(ewofjkmweklofmop34mg.SplitMode.valueOf(mode))
                .build();
    }
}
