package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Tease extends Whore {
   @Override
   public int rateCustomer(Customer customer) {
      int rating = super.rateCustomer(customer);
      if (customer.getPreferredSextype() == Sextype.FOREPLAY) {
         return rating;
      }

      rating /= 10;
      if (rating == 0) {
         rating = 1;
      }

      return rating;
   }

   @Override
   public String checkPossible(RunningActivity activity, Charakter whore) {
      return "";
   }

   @Override
   public void init() {
      super.init();
      if (this.getSexType() != Sextype.FOREPLAY) {
         this.setSexType(Sextype.FOREPLAY);
         this.getMainCustomer().addToSatisfaction(-40, this);
      }

      this.getMainCustomer().addToSatisfaction(this.getCharacter().getFinalValue(SpecializationAttribute.STRIP) / 4, this);
   }

   @Override
   public MessageData getBaseMessage() {
      MessageData message = super.getBaseMessage();
      if (this.getMainCustomer().getPreferredSextype() != Sextype.FOREPLAY) {
         message.addToMessage(TextUtil.t("tease.basic2", this.getCharacter(), this.getMainCustomer()));
      } else {
         message.addToMessage(TextUtil.t("tease.basic", this.getCharacter(), this.getMainCustomer()));
      }

      List<ImageTag> tags = this.getCharacter().getBaseTags();
      if (Util.getRnd().nextBoolean()) {
         tags.add(0, ImageTag.MASTURBATION);
      } else {
         tags.add(0, ImageTag.DANCE);
      }

      ImageData imageData = ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages());
      message.setImage(imageData);
      return message;
   }

   @Override
   public List<Sextype> getPossibleSextypes(Customer customer) {
      List<Sextype> sextypes = new ArrayList<>();
      sextypes.add(Sextype.FOREPLAY);
      return sextypes;
   }

   @Override
   public void perform() {
      this.getMainCustomer().changePayModifier(-0.33F);
      super.perform();
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modificationData = new ArrayList<>();
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, Sextype.FOREPLAY));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01F, BaseAttributeTypes.OBEDIENCE));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.1F, BaseAttributeTypes.OBEDIENCE));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.4F, SpecializationAttribute.SEDUCTION));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.2F, SpecializationAttribute.STRIP));
      modificationData.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -4.0F, EssentialAttributes.ENERGY));
      return modificationData;
   }

   @Override
   public Float getAmountActions() {
      return 0.7F;
   }

   @Override
   public float getExecutionModifier() {
      return -0.5F;
   }

   @Override
   public float getCooldownModifier() {
      return -100.0F;
   }
}
