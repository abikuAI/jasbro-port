package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Streets extends OtherLocation {
   public Streets() {
      Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();

      for (Time time : Time.values()) {
         roomUsageMap.put(time, new PlannedActivity(this, ActivityType.WHORESTREETS));
      }
   }

   @Override
   public ImageData getImage() {
      if (Jasbro.getInstance().getData().getTime() == Time.MORNING) {
         return new ImageData("images/backgrounds/streets_morning.jpg");
      } else {
         return Jasbro.getInstance().getData().getTime() == Time.AFTERNOON
            ? new ImageData("images/backgrounds/streets_afternoon.jpg")
            : new ImageData("images/backgrounds/streets_night.jpg");
      }
   }

   @Override
   public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
      List<ActivityDetails> possibleActivities = new ArrayList<>();
      if (Util.getTrainers(this.getCurrentUsage().getCharacters()).size() == 0) {
         possibleActivities.add(new ActivityDetails(ActivityType.WHORESTREETS));
      }

      possibleActivities.add(new ActivityDetails(ActivityType.ADVERTISE));
      possibleActivities.add(new ActivityDetails(ActivityType.WALK));
      possibleActivities.add(new ActivityDetails(ActivityType.ROB));
      if (possibleActivities.size() == 0) {
         possibleActivities.add(new ActivityDetails(ActivityType.IDLE));
      }

      return possibleActivities;
   }

   @Override
   public String getName() {
      return this.getType().getText();
   }

   @Override
   public String getDescription() {
      return this.getType().getDescription();
   }

   @Override
   public LocationTypeInterface getLocationType() {
      return this.getType();
   }

   public LocationType getType() {
      return LocationType.STREETS;
   }
}
