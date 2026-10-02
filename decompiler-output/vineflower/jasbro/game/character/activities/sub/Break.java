package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Break extends RunningActivity {
   private Charakter trainer;
   private Charakter slave;

   @Override
   public void init() {
      if (this.getCharacters().get(0).getType() == CharacterType.TRAINER) {
         this.trainer = this.getCharacters().get(0);
         this.slave = this.getCharacters().get(1);
      } else {
         this.trainer = this.getCharacters().get(1);
         this.slave = this.getCharacters().get(0);
      }
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      float obedienceMod = 0.05F;
      float modObLow = (12.0F - this.slave.getObedience() * 1.5F) / 15.0F;
      if (modObLow > 0.0F) {
         obedienceMod += modObLow;
      }

      obedienceMod = (float)(obedienceMod + this.trainer.getCommand() / 50.0);
      obedienceMod = (float)(obedienceMod + this.trainer.getFinalValue(SpecializationAttribute.DOMINATE) / 50.0);
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, obedienceMod, BaseAttributeTypes.OBEDIENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.2F, BaseAttributeTypes.COMMAND));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -1.3F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 1.3F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -30.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, -15.0F, EssentialAttributes.HEALTH));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -20.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 1.0F, SpecializationAttribute.DOMINATE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, Sextype.BONDAGE));
      return modifications;
   }

   @Override
   public MessageData getBaseMessage() {
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BONDAGE, this.slave);
      String message = TextUtil.t("break.basic", this.trainer, this.slave);
      return new MessageData(message, image, null);
   }
}
