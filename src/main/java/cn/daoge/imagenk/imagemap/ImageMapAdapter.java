package cn.daoge.imagenk.imagemap;

import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.math.Vector3;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;

public class ImageMapAdapter extends TypeAdapter<ImageMap> {
    @Override
    public void write(JsonWriter jsonWriter, ImageMap imageMap) throws IOException {
        if (imageMap == null) {
            jsonWriter.nullValue();
            return;
        }
        String levelName = imageMap.levelName;
        jsonWriter.value(levelName);
        String pos1 = imageMap.pos1.x + "," + imageMap.pos1.y + "," + imageMap.pos1.z;
        jsonWriter.value(pos1);
        String pos2 = imageMap.pos2.x + "," + imageMap.pos2.y + "," + imageMap.pos2.z;
        jsonWriter.value(pos2);
        String imageName = imageMap.imageName;
        jsonWriter.value(imageName);
        String id = imageMap.id;
        jsonWriter.value(id);
        String mode = imageMap.mode.name();
        jsonWriter.value(mode);
    }

    @Override
    public ImageMap read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String levelName = jsonReader.nextString();
        String pos1 = jsonReader.nextString();
        Integer[] pos1s = (Integer[]) Arrays.stream(pos1.split(",")).map(Integer::parseInt).toArray();
        String pos2 = jsonReader.nextString();
        Integer[] pos2s = (Integer[]) Arrays.stream(pos2.split(",")).map(Integer::parseInt).toArray();
        String imageName = jsonReader.nextString();
        String id = jsonReader.nextString();
        String mode = jsonReader.nextString();
        return ImageMap
                .builder()
                .levelName(levelName)
                .pos1(new Vector3(pos1s[0], pos1s[1], pos1s[2]))
                .pos2(new Vector3(pos2s[0], pos2s[1], pos2s[2]))
                .imageName(imageName)
                .id(id)
                .mode(SimpleImageMapManager.SplitMode.valueOf(mode))
                .build();
    }
}
