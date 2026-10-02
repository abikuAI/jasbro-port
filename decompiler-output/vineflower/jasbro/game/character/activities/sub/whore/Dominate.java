package jasbro.game.character.activities.sub.whore;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Dominate extends Whore {
   private MessageData messageData;
   private float actions = 2.0F;

   @Override
   public void init() {
      super.init();
      this.setMinimumObedience(0);
      this.setExecutionTime(45);
      this.setCooldownTime(45);
   }

   @Override
   public MessageData getBaseMessage() {
      String house = this.getHouse().getName();
      String customer = this.getMainCustomer().getName();
      String mood = this.getMainCustomer().getStatusName();
      String message;
      if (this.getHouse().getInternName() != null && !this.getHouse().getInternName().trim().equals("")) {
         message = TextUtil.t("whore.basic2", mood, customer, house) + " ";
      } else {
         message = TextUtil.t("whore.basic1", mood, customer, house) + " ";
      }

      message = message + TextUtil.t("whore.service", this.getCharacter(), this.getMainCustomer());
      message = message + "\n";
      message = message + TextUtil.t("dominate.basic", this.getCharacter(), this.getMainCustomer());
      List<ImageTag> tags = this.getCharacter().getBaseTags();
      tags.add(0, ImageTag.DOMINATRIX);
      if (this.getMainCustomer().getType() == CustomerType.GROUP) {
         tags.add(ImageTag.GROUP);
      }

      this.messageData = new MessageData(message, ImageUtil.getInstance().getImageDataByTags(tags, this.getCharacter().getImages()), this.getBackground());
      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.getMainCustomer() != null) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.1F, BaseAttributeTypes.COMMAND));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, Sextype.BONDAGE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, SpecializationAttribute.DOMINATE));
         return modifications;
      } else {
         return new Idle().getStatModifications();
      }
   }

   @Override
   public void perform() {
      if (this.getMainCustomer() != null) {
         int skill = this.getCharacter().getCommand()
            + this.getCharacter().getFinalValue(SpecializationAttribute.DOMINATE)
            + this.getCharacter().getFinalValue(Sextype.BONDAGE) / 4;
         this.getMainCustomer().addToSatisfaction(skill, this);
         this.setAmountActions(this.getExecutionTime() + this.getCooldownTime());
         int pay = this.getMainCustomer().pay(this.getCharacter().getMoneyModifier() + 0.3F);
         this.modifyIncome(pay);
         this.getMessages()
            .get(0)
            .addToMessage(
               "\n\n" + TextUtil.t("whore.end", this.getCharacter(), this.getMainCustomer(), this.getMainCustomer().getSatisfaction().getText(), pay)
            );
      }
   }

   @Override
   public List<Sextype> getPossibleSextypes(Customer customer) {
      List<Sextype> sextypes = new ArrayList<>();
      sextypes.add(Sextype.BONDAGE);
      return sextypes;
   }

   @Override
   public int rateCustomer(Customer customer) {
      int rating = super.rateCustomer(customer);
      return customer.getPreferredSextype() == Sextype.BONDAGE ? rating * 4 : 0;
   }

   public void setActionCost(float cost) {
      this.actions = cost;
   }

   @Override
   public Float getAmountActions() {
      return this.actions;
   }
}
