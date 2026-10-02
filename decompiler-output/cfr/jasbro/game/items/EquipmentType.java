/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.items;

import jasbro.texts.TextUtil;

public enum EquipmentType {
    HEAD,
    DRESS,
    UNDERWEAR,
    SHOES,
    ACCESSORY;


    public String getText() {
        return TextUtil.t(this.toString());
    }
}

