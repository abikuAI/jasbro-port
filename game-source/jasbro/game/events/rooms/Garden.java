package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.ConfigurableRoom;
import jasbro.game.housing.House;
import jasbro.game.housing.RoomInfo;
import jasbro.game.world.Time;
import java.util.List;

public class Garden extends ConfigurableRoom implements RoomEventHandler {
   private Garden.Plant plant = Garden.Plant.GRASS;
   private String plantName = "Grass";
   private int growth = 10;
   private int quality = 50;

   public Garden(RoomInfo roomInfo) {
      super(roomInfo);
   }

   public Garden(RoomInfo roomInfo, House house) {
      super(roomInfo, house);
   }

   @Override
   public String getName() {
      return super.getName() + "" + this.growth + " " + this.plantName + " (Quality:" + this.quality + ")";
   }

   @Override
   public List<ActivityDetails> getPossibleActivities(Time time, Util.TypeAmounts typeAmounts) {
      List<ActivityDetails> possibleActivities = super.getPossibleActivities(time, typeAmounts);
      possibleActivities.add(new ActivityDetails(ActivityType.HARVEST));
      return possibleActivities;
   }

   @Override
   public void handleEvent(MyEvent e) {
      super.handleEvent(e);
      if (e.getType() == EventType.NEXTDAY) {
         if (this.growth != 0) {
            if (this.plant == Garden.Plant.FLOWERS) {
               this.growth = this.growth + 1 + this.growth / 5;
               this.quality -= 2;
            } else if (this.plant == Garden.Plant.CATNIP) {
               this.growth = this.growth + 1 + this.growth / 7;
               this.quality -= 3;
            } else if (this.plant == Garden.Plant.CHERRIES) {
               this.growth = this.growth + 1 + this.growth / 7;
               this.quality -= 3;
            } else if (this.plant == Garden.Plant.BERRIES) {
               this.growth = this.growth + 1 + this.growth / 7;
               this.quality -= 3;
            } else if (this.plant == Garden.Plant.CATNIP) {
               this.growth = this.growth + 1 + this.growth / 10;
               this.quality -= 4;
            } else if (this.plant == Garden.Plant.ORANGES) {
               this.growth = this.growth + 1 + this.growth / 10;
               this.quality -= 4;
            } else if (this.plant == Garden.Plant.SHROOMS) {
               this.growth = this.growth + 1 + this.growth / 12;
               this.quality -= 5;
            } else if (this.plant == Garden.Plant.VINES) {
               this.growth = this.growth + 1 + this.growth / 12;
               this.quality -= 5;
            } else {
               this.growth = this.growth + 1 + this.growth / 15;
               this.quality -= 6;
            }
         }

         if (this.quality < 1) {
            this.quality = 0;
            this.growth = this.growth - (5 + this.growth / 8);
         }

         if (this.quality > 100 && this.growth != 0) {
            this.quality = 90;
            this.growth += 10;
         }

         if (this.growth > 100) {
            this.growth = 100;
         }

         if (this.growth < 0) {
            this.growth = 0;
         }
      }
   }

   public Garden.Plant getPlant() {
      return this.plant;
   }

   public void setPlant(Garden.Plant plant) {
      this.plant = plant;
      this.growth = 0;
   }

   public void setPlantName(String plantName) {
      this.plantName = plantName;
   }

   public int getGrowth() {
      return this.growth;
   }

   public void setGrowth(int growth) {
      this.growth = growth;
   }

   public int getQuality() {
      return this.quality;
   }

   public void setQuality(int quality) {
      this.quality = quality;
   }

   public enum Plant {
      FLOWERS,
      CATNIP,
      WEED,
      GRASS,
      CHERRIES,
      BERRIES,
      ORANGES,
      PEACHES,
      VINES,
      SHROOMS;
   }
}
