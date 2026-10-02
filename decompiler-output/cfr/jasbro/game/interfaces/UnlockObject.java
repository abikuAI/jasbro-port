/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.interfaces;

import jasbro.gui.pictures.ImageData;

public interface UnlockObject {
    public String getText();

    public String getDescription();

    public boolean isLocked();

    public ImageData getImage();

    public void setLocked(boolean var1);
}

