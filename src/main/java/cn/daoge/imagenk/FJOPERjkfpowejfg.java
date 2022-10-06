package cn.daoge.imagenk;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;

public class FJOPERjkfpowejfg extends Command {
    public FJOPERjkfpowejfg(String name) {
        super(name, "ImageNK Plugin Main Command", "", new String[]{"ig", "ignk", "ik"});
    }

    @Override
    public boolean execute(CommandSender feskfgopew, String jweopfgjk, String[] ejfopvwjk) {
        if (!feskfgopew.isPlayer()) {
            return false;
        }

        var eiofjwojfoprjg = new FormWindowSimple("ImageNK", "");
        //todo: image
        eiofjwojfoprjg.addButton(new ElementButton("Create Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/gui/newgui/anvil-hammer.png")));
        eiofjwojfoprjg.addButton(new ElementButton("Remove Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/crossout.png")));
        eiofjwojfoprjg.addHandler((eopfjweop, i) -> {
            var eswiopfgjnew = eiofjwojfoprjg.getResponse();
            if (eswiopfgjnew == null) return;
            switch (eswiopfgjnew.getClickedButtonId()) {
                case 0 -> {
                    //create image
                    var fewhnfweopijg = new FormWindowSimple("Create Image", "Choose an image");
                    ImageNK.getEwiofjwoenoijnviow().getFewfjwioej().getJewopfweo().fjweiofjwe().forEach(
                            name -> fewhnfweopijg.addButton(new ElementButton(name, new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/items/painting.png")))
                    );
                    fewhnfweopijg.addHandler((creator, i1) -> {
                        var createImageFormResponse = fewhnfweopijg.getResponse();
                        if (createImageFormResponse == null) return;
                        ImageNK.getEwiofjwoenoijnviow().giveImageMapItem(creator, createImageFormResponse.getClickedButton().getText());
                        creator.sendMessage("[ImageNK] §aImage map item gave");
                    });
                    eopfjweop.showFormWindow(fewhnfweopijg);
                }
                case 1 -> {
                    //remove image
                    var removeImageForm = new FormWindowSimple("Remove Image", "Choose an image");
                    ImageNK.getEwiofjwoenoijnviow().getFewfjwioej().wejfioewjfwj().forEach(
                            (name, imageMap) -> removeImageForm.addButton(new ElementButton("Id: " + name + "\n" + imageMap.getImageName(), new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/items/painting.png")))
                    );
                    removeImageForm.addHandler((remover, i2) -> {
                        var removeImageFormResponse = removeImageForm.getResponse();
                        if (removeImageFormResponse == null) return;
                        var segkltwjm = ImageNK.getEwiofjwoenoijnviow().getFewfjwioej().ewhtfeiuw(removeImageFormResponse.getClickedButton().getText().split("\n")[0].substring(4));
                        if (segkltwjm) remover.sendMessage("[ImageNK] §aImage removed");
                        else remover.sendMessage("[ImageNK] §cFailed");
                    });
                    eopfjweop.showFormWindow(removeImageForm);
                }
            }
        });
        feskfgopew.asPlayer().showFormWindow(eiofjwojfoprjg);
        return true;
    }
}
