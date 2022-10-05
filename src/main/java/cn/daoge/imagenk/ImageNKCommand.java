package cn.daoge.imagenk;

import cn.daoge.imagenk.manager.SimpleImageMapManager;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

import java.util.Arrays;
import java.util.List;

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
        //todo: image
        mainForm.addButton(new ElementButton("Create Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/gui/newgui/anvil-hammer.png")));
        mainForm.addHandler((player, i) -> {
            var mainFormResponse = mainForm.getResponse();
            if (mainFormResponse == null) return;
            switch (mainFormResponse.getClickedButtonId()) {
                case 0 -> {
                    //create image
                    var createImageForm = new FormWindowSimple("Create Image", "Choose an image");
                    ImageNK.getInstance().getImageMapManager().getProvider().getAll().forEach(
                            name -> createImageForm.addButton(new ElementButton(name, new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/items/painting.png")))
                    );
                    createImageForm.addHandler((creator, i1) -> {
                        var createImageFormResponse = createImageForm.getResponse();
                        if (createImageFormResponse == null) return;
                        ImageNK.getInstance().giveImageMapItem(creator, createImageFormResponse.getClickedButton().getText());
                        creator.sendMessage("[ImageNK] §aimage map item gave");
                    });
                    player.showFormWindow(createImageForm);
                }
            }
        });
        sender.asPlayer().showFormWindow(mainForm);
        return true;
    }
}
