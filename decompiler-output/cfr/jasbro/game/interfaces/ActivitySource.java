/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.interfaces;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.MoneyEarnedModifier;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.pictures.ImageData;
import java.util.List;

public interface ActivitySource
extends MyEventListener,
MoneyEarnedModifier {
    public int getMaxPeople();

    public List<ActivityType> getPossibleActivities();

    public ImageData getImage();

    public String getName();

    public PlannedActivity getCurrentUsage();
}

