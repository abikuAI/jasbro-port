/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.stringtemplate.v4.ST
 */
package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.game.items.usableItemEffects.UsableItemEffectType;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextWrapper;
import org.stringtemplate.v4.ST;

public class UsableItemShowMessage
extends UsableItemEffect {
    private String message;
    private ImageTag imageTag;

    @Override
    public String getName() {
        return "Show message";
    }

    @Override
    public void apply(Charakter character, Item item) {
        if (this.imageTag == null) {
            this.imageTag = ImageTag.STANDARD;
        }
        ST stringTemplate = new ST(this.message);
        if (character != null) {
            stringTemplate.add("name", (Object)character.getName());
            stringTemplate.add("type", (Object)character.getType().getText());
            TextWrapper textWrapper = new TextWrapper(character);
            stringTemplate.add("c", (Object)textWrapper);
        }
        new MessageScreen(stringTemplate.render(), ImageUtil.getInstance().getImageDataByTag(this.imageTag, character), character.getBackground());
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.SHOWMESSAGE;
    }

    public String getMessage() {
        return this.message;
    }

    public ImageTag getImageTag() {
        return this.imageTag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImageTag(ImageTag imageTag) {
        this.imageTag = imageTag;
    }
}

