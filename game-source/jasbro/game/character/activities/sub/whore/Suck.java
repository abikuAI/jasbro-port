package jasbro.game.character.activities.sub.whore;

import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Suck extends Whore {
   @Override
   public int rateCustomer(Customer customer) {
      int rating = super.rateCustomer(customer);
      return customer.getPreferredSextype() != Sextype.ORAL || customer.getGender() != Gender.MALE && customer.getGender() != Gender.FUTA ? 0 : rating * 4;
   }

   @Override
   public String checkPossible(RunningActivity activity, Charakter whore) {
      return "";
   }

   @Override
   public void init() {
      super.init();
      this.setExecutionTime(15);
      this.setCooldownTime(5);
      this.setAmountActions(20.0F);
   }

   @Override
   public MessageData getBaseMessage() {
      MessageData message = super.getBaseMessage();
      message.addToMessage(TextUtil.t("gloryhole.basic", this.getCharacter(), this.getMainCustomer()));
      List<ImageTag> tags = this.getCharacter().getBaseTags();
      tags.add(0, ImageTag.BLOWJOB);
      tags.add(1, ImageTag.GLORYHOLE);
      if (this.getMainCustomer().getType() == CustomerType.GROUP) {
         tags.add(2, ImageTag.BUKKAKE);
      }

      tags.addAll(ImageTag.getAssociatedImageTags(this.getCharacter(), this.getMainCustomer()));
      ImageData imageData = ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages());
      message.setImage(imageData);
      return message;
   }

   @Override
   public List<Sextype> getPossibleSextypes(Customer customer) {
      List<Sextype> sextypes = new ArrayList<>();
      sextypes.add(Sextype.ORAL);
      return sextypes;
   }

   @Override
   public void perform() {
      if (this.getMainCustomer().getPreferredSextype() == Sextype.ORAL) {
         this.getMainCustomer().addToSatisfaction(this.getCharacter().getFinalValue(Sextype.ORAL) / 4, this);
      }

      this.getMainCustomer().changePayModifier(-0.33F);
      super.perform();
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modificationData = new ArrayList<>();
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01F, BaseAttributeTypes.OBEDIENCE));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.2F, BaseAttributeTypes.OBEDIENCE));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, Sextype.ORAL));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -4.0F, EssentialAttributes.ENERGY));
      return modificationData;
   }

   @Override
   public Float getAmountActions() {
      return 0.7F;
   }

   @Override
   public float getExecutionModifier() {
      return -0.3F;
   }

   @Override
   public float getCooldownModifier() {
      return -0.5F;
   }
}
