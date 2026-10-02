package jasbro.util;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.locations.DivLocations;
import jasbro.texts.TextUtil;

public class UserHelper {
   public void perform(AreaInterface area, UserHelper.HelpOption helpOption) {
      if (helpOption == UserHelper.HelpOption.REMOVEALL) {
         this.removeAll(area);
      } else if (helpOption == UserHelper.HelpOption.COPYMORNINGSHIFT) {
         this.copy(area, Time.MORNING);
      } else if (helpOption == UserHelper.HelpOption.COPYAFTERNOONSHIFT) {
         this.copy(area, Time.AFTERNOON);
      } else if (helpOption == UserHelper.HelpOption.COPYNIGHTSHIFT) {
         this.copy(area, Time.NIGHT);
      } else if (helpOption == UserHelper.HelpOption.REMOVEALLEVERYWHERE
         || helpOption == UserHelper.HelpOption.COPYMORNINGSHIFTEVERYWHERE
         || helpOption == UserHelper.HelpOption.COPYAFTERNOONSHIFTEVERYWHERE
         || helpOption == UserHelper.HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
         for (House house : Jasbro.getInstance().getData().getHouses()) {
            if (helpOption == UserHelper.HelpOption.REMOVEALLEVERYWHERE) {
               this.perform(house, UserHelper.HelpOption.REMOVEALL);
            } else if (helpOption == UserHelper.HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
               this.perform(house, UserHelper.HelpOption.COPYMORNINGSHIFT);
            } else if (helpOption == UserHelper.HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
               this.perform(house, UserHelper.HelpOption.COPYAFTERNOONSHIFT);
            } else if (helpOption == UserHelper.HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
               this.perform(house, UserHelper.HelpOption.COPYNIGHTSHIFT);
            }
         }

         AreaInterface location = new DivLocations();
         if (helpOption == UserHelper.HelpOption.REMOVEALLEVERYWHERE) {
            this.perform(location, UserHelper.HelpOption.REMOVEALL);
         } else if (helpOption == UserHelper.HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
            this.perform(location, UserHelper.HelpOption.COPYMORNINGSHIFT);
         } else if (helpOption == UserHelper.HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
            this.perform(location, UserHelper.HelpOption.COPYMORNINGSHIFT);
         } else if (helpOption == UserHelper.HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
            this.perform(location, UserHelper.HelpOption.COPYNIGHTSHIFT);
         }
      }
   }

   public void copy(AreaInterface area, Time timeToCopy) {
      Time curtime = Jasbro.getInstance().getData().getTime();
      if (timeToCopy != curtime) {
         for (CharacterLocation location : area.getLocations()) {
            PlannedActivity curActivity = location.getCurrentUsage();
            PlannedActivity activityToCopy = location.getUsage(timeToCopy);
            curActivity.removeAllCharacters();

            for (Charakter character : activityToCopy.getCharacters()) {
               curActivity.add(character);
            }

            curActivity.setType(activityToCopy.getType());
            curActivity.setSelectedOption(activityToCopy.getSelectedOption());
         }
      }
   }

   public void removeAll(AreaInterface area) {
      for (CharacterLocation location : area.getLocations()) {
         PlannedActivity curActivity = location.getCurrentUsage();
         curActivity.removeAllCharacters();
      }
   }

   public enum HelpOption {
      COPYMORNINGSHIFT,
      COPYAFTERNOONSHIFT,
      COPYNIGHTSHIFT,
      REMOVEALL,
      COPYMORNINGSHIFTEVERYWHERE,
      COPYAFTERNOONSHIFTEVERYWHERE,
      COPYNIGHTSHIFTEVERYWHERE,
      REMOVEALLEVERYWHERE;

      public String getText() {
         return TextUtil.t(this.toString());
      }
   }
}
