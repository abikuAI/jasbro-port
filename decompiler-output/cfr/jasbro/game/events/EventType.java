/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events;

public enum EventType {
    ACTIVITY(true),
    ACTIVITYPERFORMED(true),
    ACTIVITYFINISHED(true),
    ACTIVITYCREATED(true),
    CHARACTERLOST,
    CHARACTERGAINED,
    SLAVESOLD,
    ATTRIBUTECHANGE,
    ATTRIBUTECHANGED,
    ACTIVITYCHANGE,
    ENERGYZERO,
    HEALTHZERO(true),
    CHARACTERDEATH,
    NEXTSHIFT(true),
    NEXTDAY(true),
    NEXTSHIFTSTARTED,
    ITEMUSED,
    SHIFTSTART(true),
    CUSTOMERSARRIVE,
    MONEYEARNED,
    MONEYSPENT,
    BROKE,
    ATTACK,
    ATTACKMISS,
    ATTACKBLOCK,
    ATTACKCRIT,
    ATTACKHIT,
    STATUSCHANGE,
    MOTIVATIONLOW(true),
    MOTIVATIONHIGH(true),
    MOTIVATIONNORMAL(true),
    GAMESTART(true);

    private boolean customContentRelevant = false;

    private EventType() {
    }

    private EventType(boolean customContentRelevant) {
        this.customContentRelevant = customContentRelevant;
    }

    public boolean isCustomContentRelevant() {
        return this.customContentRelevant;
    }
}

