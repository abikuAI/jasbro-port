/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.events.MyEvent;

public interface PregnancyInterface {
    public void reduceDays(int var1);

    public void modifyDays(int var1);

    public int getDays();

    public void setCharacter(Charakter var1);

    public void handleEvent(MyEvent var1);
}

