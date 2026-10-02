/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  bsh.EvalError
 *  net.java.truevfs.access.TFile
 *  net.java.truevfs.access.TPath
 */
package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.game.character.Charakter;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.io.File;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TPath;

public class ImageSelection {
    private String image;
    private ImageLocation imageLocation = ImageLocation.LOCAL;
    private List<ImageTag> imageTags;
    private boolean background = false;
    private String target = "character";

    public ImageData getImageData(WorldEvent worldEvent) throws EvalError {
        if (this.background) {
            return ((Charakter)worldEvent.getAttribute(this.target)).getBackground();
        }
        if (this.image != null) {
            String relativePath;
            if (this.imageLocation == ImageLocation.LOCAL) {
                TFile folder = worldEvent.getFile().getParentFile();
                relativePath = this.getRelativePathFor(folder);
            } else {
                relativePath = "images";
            }
            return new ImageData(relativePath + "/" + this.image);
        }
        if (this.imageTags.size() > 0) {
            HasImagesInterface character = (HasImagesInterface)worldEvent.getAttribute(this.target);
            return ImageUtil.getInstance().getImageDataByTag(this.imageTags.get(0), character);
        }
        return null;
    }

    private String getRelativePathFor(final TFile folder) {
        String path = AccessController.doPrivileged(new PrivilegedAction<String>(){

            @Override
            public String run() {
                return new TPath((File)new TFile("")).relativize((Path)new TPath((File)folder)).toString();
            }
        });
        return path;
    }

    public ImageData getImageData(Item item) {
        if (this.image != null) {
            String relativePath;
            if (this.imageLocation == ImageLocation.LOCAL) {
                TFile folder = item.getFile().getParentFile();
                relativePath = this.getRelativePathFor(folder);
            } else {
                relativePath = "images";
            }
            return new ImageData(relativePath + "/" + this.image);
        }
        return null;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isBackground() {
        return this.background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public ImageLocation getImageLocation() {
        return this.imageLocation;
    }

    public void setImageLocation(ImageLocation imageLocation) {
        this.imageLocation = imageLocation;
    }

    public List<ImageTag> getImageTags() {
        if (this.imageTags == null) {
            this.imageTags = new ArrayList<ImageTag>();
            this.imageTags.add(ImageTag.STANDARD);
        }
        return this.imageTags;
    }

    public void setImageTags(List<ImageTag> imageTags) {
        this.imageTags = imageTags;
    }

    public static enum ImageLocation {
        LOCAL,
        GLOBAL;

    }
}

