/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pictures;

import java.awt.image.BufferedImage;

public class MyResizeImageObject {
    private BufferedImage image;

    public MyResizeImageObject(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

