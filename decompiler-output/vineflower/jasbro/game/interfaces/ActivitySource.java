package jasbro.game.interfaces;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.gui.pictures.ImageData;
import java.util.List;

public interface ActivitySource extends MyEventListener, MoneyEarnedModifier {
   int getMaxPeople();

   List<ActivityType> getPossibleActivities();

   ImageData getImage();

   String getName();

   PlannedActivity getCurrentUsage();
}
