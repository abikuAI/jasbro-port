/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import java.util.EnumMap;
import java.util.Map;

public abstract class OtherLocation
extends CharacterLocation {
    private Map<Time, PlannedActivity> usageMap = new EnumMap<Time, PlannedActivity>(Time.class);

    @Override
    public PlannedActivity getCurrentUsage() {
        return this.getUsageMap().get((Object)Jasbro.getInstance().getData().getTime());
    }

    @Override
    public Map<Time, PlannedActivity> getUsageMap() {
        return this.usageMap;
    }
}

