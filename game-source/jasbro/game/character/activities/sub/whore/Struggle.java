package jasbro.game.character.activities.sub.whore;

import jasbro.Util;
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

public class Struggle extends Whore {
   private MessageData messageData;
   private float actions = 1.5F;
   private boolean submit = false;

   @Override
   public void init() {
      super.init();
      this.setMinimumObedience(10);
      this.determineSubmit(this.getMainCustomer());
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
      message = message + "\n" + TextUtil.t("struggle.basic", this.getCharacter(), this.getMainCustomer());
      if (this.submit) {
         message = message + "\n" + TextUtil.t("struggle.result.lost", this.getCharacter());
         message = message + "\n" + TextUtil.t("struggle.bondage", this.getMainCustomer(), this.getCharacter());
      } else {
         message = message + "\n" + TextUtil.t("struggle.result.won", this.getCharacter(), this.getMainCustomer());
         message = message + "\n" + TextUtil.t("struggle.dominate", this.getCharacter());
      }

      message = message + "\n" + TextUtil.t("struggle.finish") + "\n";
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
         if (this.submit) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.5F, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, SpecializationAttribute.DOMINATE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, BaseAttributeTypes.STAMINA));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -3.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 4.0F, Sextype.BONDAGE));
         } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, 0.5F, BaseAttributeTypes.COMMAND));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 4.0F, SpecializationAttribute.DOMINATE));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, BaseAttributeTypes.STRENGTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -1.0F, EssentialAttributes.HEALTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0F, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0F, Sextype.BONDAGE));
         }

         return modifications;
      } else {
         return new Idle().getStatModifications();
      }
   }

   @Override
   public void perform() {
      if (this.getMainCustomer() != null) {
         int skill;
         if (this.submit) {
            skill = this.getCharacter().getCommand() + this.getCharacter().getFinalValue(Sextype.BONDAGE) * 2;
         } else {
            skill = this.getCharacter().getCommand() + this.getCharacter().getFinalValue(SpecializationAttribute.DOMINATE) * 2;
         }

         this.getMainCustomer().addToSatisfaction(skill, this);
         int pay = this.getMainCustomer().pay(this.getCharacter().getMoneyModifier() + 0.5F);
         this.modifyIncome(pay);
         this.getMessages()
            .get(0)
            .addToMessage(
               "\n\n" + TextUtil.t("whore.end", this.getCharacter(), this.getMainCustomer(), this.getMainCustomer().getSatisfaction().getText(), pay)
            );
      }
   }

   private void determineSubmit(Customer customer) {
      int bonus = 0;
      int bumBase = 2;
      int businessBase = 2;
      int celebrityBase = 4;
      int lordBase = 4;
      int merchantBase = 3;
      int nobleBase = 3;
      int peasantBase = 2;
      int soldierBase = 7;
      byte var11;
      switch (customer.getStatus()) {
         case DRUNK:
            var11 = -1;
            break;
         case HYPED:
            var11 = 1;
            break;
         case PISSED:
            var11 = 2;
            break;
         case SAD:
            var11 = -2;
            break;
         case SHYSTATUS:
            var11 = -1;
            break;
         case STRONGSTATUS:
            var11 = 3;
            break;
         case TIRED:
            var11 = -2;
            break;
         case VERYDRUNK:
            var11 = -3;
            break;
         default:
            var11 = 0;
      }

      switch (customer.getType()) {
         case BUM:
            if (Util.getInt(0, 10) < bumBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case BUSINESSMAN:
            if (Util.getInt(0, 10) < businessBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case CELEBRITY:
            if (Util.getInt(0, 10) < celebrityBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case LORD:
            if (Util.getInt(0, 10) < lordBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case MERCHANT:
            if (Util.getInt(0, 10) < merchantBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case MINORNOBLE:
            if (Util.getInt(0, 10) < nobleBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case PEASANT:
            if (Util.getInt(0, 10) < peasantBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         case SOLDIER:
            if (Util.getInt(0, 10) < soldierBase + var11) {
               this.submit = true;
            } else {
               this.submit = false;
            }
            break;
         default:
            this.submit = true;
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
