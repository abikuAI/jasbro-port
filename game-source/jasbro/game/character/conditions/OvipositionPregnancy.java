package jasbro.game.character.conditions;

import jasbro.Util;
import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.util.List;

public class OvipositionPregnancy extends Condition implements PregnancyInterface {
   private PregnancyInterface realPregnancy;

   @Override
   public void init() {
      super.init();
      int selection = Util.getInt(0, 100);
      if (selection <= 5) {
         this.realPregnancy = new Pregnancy(this.getCharacter(), null, null, false);
      } else {
         this.realPregnancy = new MonsterPregnancy(this.getCharacter(), null, false);
      }

      if (this.realPregnancy.getDays() > 30) {
         this.realPregnancy.modifyDays(-this.realPregnancy.getDays() + 30);
      }

      this.realPregnancy.setCharacter(this.getCharacter());
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY) {
         this.modifyDays(-1);
         if (this.getDays() <= 0) {
            this.realPregnancy.handleEvent(e);
            this.getCharacter().removeCondition(this);
         }
      }
   }

   @Override
   public void reduceDays(int amount) {
      this.realPregnancy.reduceDays(amount);
   }

   @Override
   public void modifyDays(int amount) {
      this.realPregnancy.modifyDays(amount);
   }

   @Override
   public int getDays() {
      return this.realPregnancy.getDays();
   }

   @Override
   public String getName() {
      return TextUtil.t("oviposition", this.getCharacter());
   }

   @Override
   public String getDescription() {
      return this.getName() + "\n" + TextUtil.t("oviposition.description", this.realPregnancy.getDays());
   }

   @Override
   public ImageData getIcon() {
      return new ImageData("images/icons/perks/oviposition.png");
   }

   @Override
   public void modifyImageTags(List<ImageTag> imageTags) {
      imageTags.add(ImageTag.PREGNANT);
   }
}
