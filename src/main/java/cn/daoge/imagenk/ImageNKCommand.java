package cn.daoge.imagenk;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class ImageNKCommand extends Command {
    public ImageNKCommand(String name) {
        super(name, "ImageNK Plugin Main Command", "", new String[]{"ig", "ignk", "ik"});
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (!sender.isPlayer() || !sender.isOp()) {
            return false;
        }

        var mainForm = new FormWindowSimple("ImageNK", "");
        mainForm.addButton(new ElementButton("Create Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/gui/newgui/anvil-hammer.png")));
        mainForm.addButton(new ElementButton("Remove Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/crossout.png")));
        mainForm.addButton(new ElementButton("Reload Image Source", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/servers.png")));
        mainForm.addButton(new ElementButton("Download Image", new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, "textures/ui/free_download.png")));
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
                    player.sendMessage("[ImageNK] §aStart reloading image source");
                    ImageNK.getInstance().imageMapManager.getProvider().reload();
                    player.sendMessage("[ImageNK] §aImage source reloaded");
                }
                case 3 -> {
                    //从URL下载图片
                    var downloadImageForm = new FormWindowCustom("Download Image");
                    downloadImageForm.addElement(new ElementInput("Image URL: "));
                    downloadImageForm.addElement(new ElementInput("Save The File As Name: ", "Use the name of the source file"));
                    downloadImageForm.addElement(new ElementToggle("Reload Image Source After Downloaded", true));
                    downloadImageForm.addHandler((downloader, i3) -> {
                        var downloadImageFormResponse = downloadImageForm.getResponse();
                        if (downloadImageFormResponse == null) return;

                        var urlStr = downloadImageFormResponse.getInputResponse(0);
                        var saveName = downloadImageFormResponse.getInputResponse(1);
                        var reload = downloadImageFormResponse.getToggleResponse(2);

                        if (urlStr.equals("")) {
                            downloader.sendMessage("[ImageNK] §cURL and save name cannot be empty!");
                            return;
                        }

                        URL url;
                        try {
                            url = new URL(urlStr);
                        } catch (MalformedURLException e) {
                            downloader.sendMessage("[ImageNK] §cMalformed URL! Please enter the correct URL!");
                            return;
                        }

                        String finalSaveName;
                        //获取源文件名称
                        var sourceFileName = Path.of(url.getPath()).getFileName().toString();
                        if (!saveName.isEmpty()) {
                            //获取源文件后缀名
                            var suffix = sourceFileName.split("\\.")[1];
                            finalSaveName = saveName + "." + suffix;
                        } else {
                            //使用源文件名称
                            finalSaveName = sourceFileName;
                        }

                        //检查是否重复
                        //注意如果上一个文件在下载后未重新加载源，此方法无法正确检查是否重复
                        if (ImageNK.getInstance().getImageMapManager().getProvider().getAll().contains(finalSaveName)) {
                            player.sendMessage("[ImageNK] §cDuplicate file name!");
                            return;
                        }

                        //异步下载文件
                        downloader.sendMessage("[ImageNK] §aThe asynchronous download has started. Please wait a moment...");
                        URL finalUrl = url;
                        CompletableFuture.runAsync(() -> {
                            try {
                                ImageNK.getInstance().downloadImage(finalUrl, finalSaveName);
                            } catch (IOException e) {
                                downloader.sendMessage("[ImageNK] §cAsynchronous download failed");
                                return;
                            }
                            downloader.sendMessage("[ImageNK] §aImage downloaded successfully! Has been saved as §f" + finalSaveName);
                            if (!reload)
                                downloader.sendMessage("[ImageNK] §aPlease reload image source to use it");
                            else {
                                //重载图片源
                                player.sendMessage("[ImageNK] §aStart reloading image source");
                                ImageNK.getInstance().getImageMapManager().getProvider().reload();
                                player.sendMessage("[ImageNK] §aImage source reloaded");
                            }
                        });
                    });

                    player.showFormWindow(downloadImageForm);
                }
            }
        });
        sender.asPlayer().showFormWindow(mainForm);
        return true;
    }
}
