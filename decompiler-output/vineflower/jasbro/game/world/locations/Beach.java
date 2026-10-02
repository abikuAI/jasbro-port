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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Beach extends OtherLocation {
   private transient HashMap<Time, ImageData> images;

   public Beach() {
      Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();

      for (Time time : Time.values()) {
         roomUsageMap.put(time, new PlannedActivity(this, ActivityType.SUNBATHE));
      }
   }

   @Override
   public ImageData getImage() {
      return this.getImages().get(Jasbro.getInstance().getData().getTime());
   }

   @Override
   public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
      List<ActivityDetails> possibleActivities = new ArrayList<>();
      possibleActivities.add(new ActivityDetails(ActivityType.SWIM));
      possibleActivities.add(new ActivityDetails(ActivityType.WALK));
      if (time != Time.NIGHT) {
         possibleActivities.add(new ActivityDetails(ActivityType.SUNBATHE));
         possibleActivities.add(new ActivityDetails(ActivityType.ADVERTISE));
      } else {
         possibleActivities.add(new ActivityDetails(ActivityType.CAMP));
      }

      return possibleActivities;
   }

   @Override
   public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, Util.TypeAmounts typeAmounts) {
      List<ActivityDetails> activities = new ArrayList<>();
      if (time != Time.NIGHT && typeAmounts.getInfantAmount() == 0 && (typeAmounts.getChildAmount() == 0 || typeAmounts.isAdultPresent())) {
         activities.add(new ActivityDetails(ActivityType.PLAY));
      }

      return activities;
   }

   @Override
   public String getName() {
      return this.getType().getText();
   }

   @Override
   public String getDescription() {
      return this.getType().getDescription();
   }

   private HashMap<Time, ImageData> getImages() {
      if (this.images == null) {
         this.images = new HashMap<>();
         this.images.put(Time.MORNING, new ImageData("images/backgrounds/beach_morning.jpg"));
         this.images.put(Time.AFTERNOON, new ImageData("images/backgrounds/beach_afternoon.jpg"));
         this.images.put(Time.NIGHT, new ImageData("images/backgrounds/beach_night.jpg"));
      }

      return this.images;
   }

   @Override
   public LocationTypeInterface getLocationType() {
      return this.getType();
   }

   public LocationType getType() {
      return LocationType.BEACH;
   }
}
