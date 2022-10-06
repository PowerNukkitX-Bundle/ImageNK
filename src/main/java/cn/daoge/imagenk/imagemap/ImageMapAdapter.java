package cn.daoge.imagenk.imagemap;

import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.math.Vector3;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ImageMapAdapter extends TypeAdapter<ImageMap> {
    @Override
    public void write(JsonWriter jsonWriter, ImageMap imageMap) throws IOException {
        if (imageMap == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject()
                .name("levelName").value(imageMap.levelName)
                .name("pos1")
                .beginArray().value(imageMap.pos1.x)
                .value(imageMap.pos1.y)
                .value(imageMap.pos1.z)
                .endArray()
                .name("pos2")
                .beginArray().value(imageMap.pos2.x)
                .value(imageMap.pos2.y)
                .value(imageMap.pos2.z)
                .endArray()
                .name("imageName").value(imageMap.imageName)
                .name("id").value(imageMap.id)
                .name("mode").value(imageMap.mode.name())
                .name("compressibility").value(imageMap.compressibility)
                .endObject();
    }

    @Override
    public ImageMap read(JsonReader jsonReader) throws IOException {
        var imageMapFactory = ImageMap.builder();
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "levelName" -> imageMapFactory.levelName(jsonReader.nextString());
                case "pos1" -> {
                    jsonReader.beginArray();
                    imageMapFactory.pos1(new Vector3(jsonReader.nextDouble(), jsonReader.nextDouble(), jsonReader.nextDouble()));
                    jsonReader.endArray();
                }
                case "pos2" -> {
                    jsonReader.beginArray();
                    imageMapFactory.pos2(new Vector3(jsonReader.nextDouble(), jsonReader.nextDouble(), jsonReader.nextDouble()));
                    jsonReader.endArray();
                }
                case "imageName" -> imageMapFactory.imageName(jsonReader.nextString());
                case "id" -> imageMapFactory.id(jsonReader.nextString());
                case "mode" -> imageMapFactory.mode(SimpleImageMapManager.SplitMode.valueOf(jsonReader.nextString()));
                case "compressibility" -> imageMapFactory.compressibility(jsonReader.nextDouble());
            }
        }
        jsonReader.endObject();
        return imageMapFactory.build();
    }
}
