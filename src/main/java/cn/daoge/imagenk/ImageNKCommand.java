package cn.daoge.imagenk;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;

public class ImageNKCommand extends Command {
    public ImageNKCommand(String name) {
        super(name, "ImageNK Plugin Main Command", "", new String[]{"ig", "ignk", "ik"});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!sender.isPlayer()) {
            return false;
        }

        var mainForm = new FormWindowSimple("ImageNK", "");
        mainForm.addButton(new ElementButton("Create Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/gui/newgui/anvil-hammer.png")));
        mainForm.addButton(new ElementButton("Remove Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/crossout.png")));
        mainForm.addButton(new ElementButton("Reload Image Source", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/servers.png")));
        mainForm.addHandler((player, i) -> {
            var mainFormResponse = mainForm.getResponse();
            if (mainFormResponse == null) return;
            switch (mainFormResponse.getClickedButtonId()) {
                case 0 -> {
                    //创建图片
                    var createImageForm = new FormWindowSimple("Create Image", "Choose an image");
                    ImageNK.getInstance().getImageMapManager().getProvider().getAll().forEach(
                            name -> createImageForm.addButton(new ElementButton(name, new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/items/painting.png")))
                    );
                    createImageForm.addHandler((creator, i1) -> {
                        var createImageFormResponse = createImageForm.getResponse();
                        if (createImageFormResponse == null) return;
                        ImageNK.getInstance().giveImageMapItem(creator, createImageFormResponse.getClickedButton().getText());
                        creator.sendMessage("[ImageNK] §aImage map item gave");
                    });
                    player.showFormWindow(createImageForm);
                }
                case 1 -> {
                    //删除图片
                    var removeImageForm = new FormWindowSimple("Remove Image", "Choose an image");
                    ImageNK.getInstance().getImageMapManager().getAllImageMap().forEach(
                            (name, imageMap) -> removeImageForm.addButton(new ElementButton("Id: " + name + "\n" + imageMap.getImageName(), new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/items/painting.png")))
                    );
                    removeImageForm.addHandler((remover, i2) -> {
                        var removeImageFormResponse = removeImageForm.getResponse();
                        if (removeImageFormResponse == null) return;
                        var succeed = ImageNK.getInstance().getImageMapManager().removeImageMap(removeImageFormResponse.getClickedButton().getText().split("\n")[0].substring(4));
                        if (succeed) remover.sendMessage("[ImageNK] §aImage removed");
                        else remover.sendMessage("[ImageNK] §cFailed");
                    });
                    player.showFormWindow(removeImageForm);
                }
                case 2 -> {
                    //要求图片源重载
                    ImageNK.getInstance().imageMapManager.getProvider().reload();
                    player.sendMessage("[ImageNK] §aImage source reloaded");
                }
            }
        });
        sender.asPlayer().showFormWindow(mainForm);
        return true;
    }
}
