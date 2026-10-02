package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library extends OtherLocation {
   public Library() {
      Map<Time, PlannedActivity> roomUsageMap = this.getUsageMap();

      for (Time time : Time.values()) {
         roomUsageMap.put(time, new PlannedActivity(this, ActivityType.STUDY));
      }
   }

   @Override
   public ImageData getImage() {
      return new ImageData("images/backgrounds/library.jpg");
   }

   @Override
   public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
      List<ActivityDetails> possibleActivities = new ArrayList<>();
      possibleActivities.add(new ActivityDetails(ActivityType.STUDY));
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         possibleActivities.add(new ActivityDetails(ActivityType.WORKGUILD));
      }

      for (int i = 0; i < this.getCurrentUsage().getCharacters().size(); i++) {
         Charakter character = this.getCurrentUsage().getCharacters().get(i);
         if (character.getType() == CharacterType.SLAVE) {
            this.getCurrentUsage().removeCharacter(character);
            i--;
         }
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
      return LocationType.LIBRARY;
   }
}
