/*
 * Decompiled with CFR 0.152.
 */
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
    public void perform(AreaInterface area, HelpOption helpOption) {
        if (helpOption == HelpOption.REMOVEALL) {
            this.removeAll(area);
        } else if (helpOption == HelpOption.COPYMORNINGSHIFT) {
            this.copy(area, Time.MORNING);
        } else if (helpOption == HelpOption.COPYAFTERNOONSHIFT) {
            this.copy(area, Time.AFTERNOON);
        } else if (helpOption == HelpOption.COPYNIGHTSHIFT) {
            this.copy(area, Time.NIGHT);
        } else if (helpOption == HelpOption.REMOVEALLEVERYWHERE || helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE || helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE || helpOption == HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
            for (House house : Jasbro.getInstance().getData().getHouses()) {
                if (helpOption == HelpOption.REMOVEALLEVERYWHERE) {
                    this.perform(house, HelpOption.REMOVEALL);
                    continue;
                }
                if (helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
                    this.perform(house, HelpOption.COPYMORNINGSHIFT);
                    continue;
                }
                if (helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
                    this.perform(house, HelpOption.COPYAFTERNOONSHIFT);
                    continue;
                }
                if (helpOption != HelpOption.COPYNIGHTSHIFTEVERYWHERE) continue;
                this.perform(house, HelpOption.COPYNIGHTSHIFT);
            }
            DivLocations location = new DivLocations();
            if (helpOption == HelpOption.REMOVEALLEVERYWHERE) {
                this.perform(location, HelpOption.REMOVEALL);
            } else if (helpOption == HelpOption.COPYMORNINGSHIFTEVERYWHERE) {
                this.perform(location, HelpOption.COPYMORNINGSHIFT);
            } else if (helpOption == HelpOption.COPYAFTERNOONSHIFTEVERYWHERE) {
                this.perform(location, HelpOption.COPYMORNINGSHIFT);
            } else if (helpOption == HelpOption.COPYNIGHTSHIFTEVERYWHERE) {
                this.perform(location, HelpOption.COPYNIGHTSHIFT);
            }
        }
    }

    public void copy(AreaInterface area, Time timeToCopy) {
        Time curtime = Jasbro.getInstance().getData().getTime();
        if (timeToCopy != curtime) {
            for (CharacterLocation characterLocation : area.getLocations()) {
                PlannedActivity curActivity = characterLocation.getCurrentUsage();
                PlannedActivity activityToCopy = characterLocation.getUsage(timeToCopy);
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
        for (CharacterLocation characterLocation : area.getLocations()) {
            PlannedActivity curActivity = characterLocation.getCurrentUsage();
            curActivity.removeAllCharacters();
        }
    }

    public static enum HelpOption {
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

