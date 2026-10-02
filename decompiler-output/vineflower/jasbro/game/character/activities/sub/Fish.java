package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Fish extends RunningActivity {
   private static final String smallFish = "FISH_Small_Fish";
   private static final String mediumFish = "FISH_Medium_Fish";
   private static final String bigFish = "FISH_Big_Fish";

   @Override
   public MessageData getBaseMessage() {
      String message = TextUtil.t("fish.basic", this.getCharacter());
      message = message + "\n";
      int fishCatch = Util.getInt(0, 8);
      int amount = Util.getInt(2, 3);
      Object[] arguments = new Object[]{amount, this.getCharacter()};
      switch (fishCatch) {
         case 1:
            message = message + TextUtil.t("fish.none", this.getCharacter(), arguments);
            break;
         case 2:
            message = message + TextUtil.t("fish.small", this.getCharacter(), arguments);
            break;
         case 3:
            message = message + TextUtil.t("fish.medium", this.getCharacter(), arguments);
            break;
         case 4:
            message = message + TextUtil.t("fish.big", this.getCharacter(), arguments);
            break;
         case 5:
            message = message + TextUtil.t("fish.asleep", this.getCharacter(), arguments);
            break;
         default:
            message = message + TextUtil.t("fish.none", this.getCharacter(), arguments);
      }

      message = message + "\n";
      if (fishCatch > 1 && fishCatch < 5) {
         if (Util.getInt(0, 10) < 5) {
            message = message + TextUtil.t("fish.eat", this.getCharacter());
            this.getCharacter().addCondition(new Buff.Satiated(fishCatch, this.getCharacter()));
         } else {
            message = message + TextUtil.t("fish.keep", this.getCharacter());
            if (Jasbro.getInstance().getItems().containsKey("FISH_Small_Fish")
               && Jasbro.getInstance().getItems().containsKey("FISH_Big_Fish")
               && Jasbro.getInstance().getItems().containsKey("FISH_Medium_Fish")) {
               Item item = Jasbro.getInstance().getItems().get("FISH_Small_Fish");
               switch (fishCatch) {
                  case 2:
                     Jasbro.getInstance().getData().getInventory().addItems(item, amount);
                     break;
                  case 3:
                     item = Jasbro.getInstance().getItems().get("FISH_Medium_Fish");
                     Jasbro.getInstance().getData().getInventory().addItems(item, amount);
                     break;
                  case 4:
                     item = Jasbro.getInstance().getItems().get("FISH_Big_Fish");
                     Jasbro.getInstance().getData().getInventory().addItems(item, amount);
               }
            }
         }
      }

      return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, this.getCharacter()), this.getCharacterLocation().getImage());
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.6F, EssentialAttributes.MOTIVATION));
      return modifications;
   }
}
