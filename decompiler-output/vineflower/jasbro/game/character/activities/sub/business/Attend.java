package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Attend extends RunningActivity implements BusinessSecondaryActivity {
   private MessageData messageData;
   private List<Charakter> dancers = new ArrayList<>();
   private List<Charakter> bartenders = new ArrayList<>();
   private Map<Charakter, Strip.StripAction> stripperAction = new HashMap<>();
   private Map<Charakter, Bartend.BarAction> bartenderAction = new HashMap<>();

   @Override
   public void init() {
      for (Charakter character : this.getCharacters()) {
         if (character.getSpecializations().contains(SpecializationType.DANCER) && this.dancers.size() < 2) {
            this.dancers.add(character);
         } else {
            this.bartenders.add(character);
         }
      }
   }

   @Override
   public void perform() {
      int amountEarned = 0;
      int stripBonus = 0;
      int stripSkill = 0;
      if (!this.dancers.isEmpty()) {
         for (Charakter stripper : this.dancers) {
            stripSkill += stripper.getCharisma() / 5 + stripper.getFinalValue(SpecializationAttribute.STRIP) / 5 + 1;
         }
      }

      int pay = 0;
      float portionPay = 0.0F;

      for (Customer customer : this.getCustomers()) {
         Charakter bartender = this.bartenders.get(Util.getInt(0, this.bartenders.size()));
         int skill = 5
            + bartender.getFinalValue(BaseAttributeTypes.CHARISMA) / 10
            + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 10
            + bartender.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 10;
         switch (customer.getType()) {
            case PEASANT:
               portionPay = 0.03F;
               skill = (int)(skill + stripSkill * 0.8F);
               break;
            case SOLDIER:
               portionPay = 0.02F;
               skill = (int)(skill + stripSkill * 0.7F);
               break;
            case MERCHANT:
               portionPay = 0.015F;
               skill = (int)(skill + stripSkill * 0.5F);
               break;
            case BUSINESSMAN:
               portionPay = 0.01F;
               skill = (int)(skill + stripSkill * 0.3F);
               break;
            case MINORNOBLE:
               portionPay = 0.005F;
               skill = (int)(skill + stripSkill * 0.2F);
               break;
            case LORD:
               portionPay = 0.001F;
               skill = (int)(skill + stripSkill * 0.1F);
               break;
            case CELEBRITY:
               portionPay = 5.0E-4F;
               skill = (int)(skill + stripSkill * 0.07F);
               break;
            default:
               portionPay = 0.09F;
         }

         customer.addToSatisfaction((int)(skill * portionPay * 10.0F), this);
         pay = (int)(customer.getMoney() * portionPay * Util.getInt(50, 125) / 100.0F);
         pay += 5 + customer.getMoney() * bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 1200;
         customer.payFixed(pay);
         amountEarned += pay;
         customer.changePayModifier(0.2F);
      }

      this.modifyIncome(amountEarned);
      Object[] arguments = new Object[]{
         TextUtil.listCharacters(this.dancers), TextUtil.listCharacters(this.bartenders), this.getCustomers().size(), amountEarned
      };
      this.messageData.addToMessage("\n" + TextUtil.t("cabaret.result", arguments));

      for (Charakter stripper2 : this.dancers) {
         List<Strip.StripAction> actions = new ArrayList<>();
         if (stripper2.getTraits().contains(Trait.EXTRAS)) {
            actions.add(Strip.StripAction.EXTRAS);
            actions.add(Strip.StripAction.EXTRAS);
            actions.add(Strip.StripAction.EXTRAS);
            actions.add(Strip.StripAction.EXTRAS);
         }

         if (stripper2.getTraits().contains(Trait.SHY)) {
            actions.add(Strip.StripAction.SHY);
         }

         if (stripper2.getTraits().contains(Trait.UNINHIBITED)) {
            actions.add(Strip.StripAction.UNHINIBITED);
            actions.add(Strip.StripAction.LICKPOLE);
         }

         if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 7L) {
            actions.add(Strip.StripAction.SEXSMELL);
         }

         if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
            actions.add(Strip.StripAction.SEXSMELL);
         }

         if (stripper2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
            actions.add(Strip.StripAction.SEXSMELL);
         }

         if (stripper2.getTraits().contains(Trait.STIFF)) {
            actions.add(Strip.StripAction.STIFF);
         }

         if (stripper2.getTraits().contains(Trait.HORNY)) {
            actions.add(Strip.StripAction.RUBCLIT);
         }

         if (stripper2.getTraits().contains(Trait.FIT)) {
            actions.add(Strip.StripAction.FIT);
         }

         if (stripper2.getTraits().contains(Trait.FRAGILE)) {
            actions.add(Strip.StripAction.FRAGILE);
         }

         if (stripper2.getTraits().contains(Trait.OILY)) {
            actions.add(Strip.StripAction.OILY);
         }

         if (stripper2.getTraits().contains(Trait.SUBMISSIVE)) {
            actions.add(Strip.StripAction.SUBMISSIVE);
         }

         if (stripper2.getTraits().contains(Trait.CUMSLUT)) {
            actions.add(Strip.StripAction.CUMDRINK);
         }

         if (stripper2.getTraits().contains(Trait.AFLEURDEPEAU)) {
            actions.add(Strip.StripAction.FONDLE);
         }

         if (stripper2.getTraits().contains(Trait.FELINEHEAT) && Jasbro.getInstance().getData().getDay() % 15 == 0) {
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
            actions.add(Strip.StripAction.FELINEORGY);
         }

         if (stripper2.getFinalValue(SpecializationAttribute.COOKING) > 255) {
            actions.add(Strip.StripAction.CREAM);
         }

         if (Util.getInt(0, 100) < 10 + actions.size() * 6 && this.getCustomers().size() > 10 && actions.size() > 0) {
            this.stripperAction.put(stripper2, actions.get(Util.getInt(0, actions.size())));
            String message = null;
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
            int a = Util.getInt(0, this.getCustomers().size() - 1);
            Object[] arg = new Object[]{this.getCustomers().get(a).getName()};
            int rnd = 0;
            switch ((Strip.StripAction)this.stripperAction.get(stripper2)) {
               case EXTRAS:
                  if (this.getCustomers().get(a).getType() == CustomerType.BUM) {
                     break;
                  }

                  stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                  int extra = this.getCustomers().get(a).getMoney() * stripper2.getFinalValue(SpecializationAttribute.STRIP) / 300;
                  this.modifyIncome(extra);
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);
                  message = TextUtil.t("STRIP.extras", stripper2, this.getCustomers().get(a).getStatusName(), this.getCustomers().get(a).getName(), extra);
                  if (this.getCustomers().get(a).getPreferredSextype() == Sextype.VAGINAL && stripper2.getAllowedServices().isAllowed(Sextype.VAGINAL)) {
                     if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                        if (Util.getInt(0, 10) > 5
                           && (
                              stripper2.getTraits().contains(Trait.NYMPHO)
                                 || stripper2.getTraits().contains(Trait.SLUT)
                                 || stripper2.getTraits().contains(Trait.HORNY)
                           )) {
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.VAGINAL, stripper2));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.two", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 5, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                           }

                           if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           stripper2.getFame().modifyFame(50.0);

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 10, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                              }
                           }
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.VAGINAL, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.one", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                           }

                           if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 7, this);
                        }
                     }

                     if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) {
                        break;
                     }

                     if (Util.getInt(0, 10) <= 5
                        || !stripper2.getTraits().contains(Trait.NYMPHO)
                           && !stripper2.getTraits().contains(Trait.KINKY)
                           && !stripper2.getTraits().contains(Trait.SENSITIVE)) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.VAGINAL, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.one", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        if (Util.getInt(1, 3) == 1
                           && (
                              this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                           )) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }

                        if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.VAGINAL, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.two", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                        stripper2.getFame().modifyFame(50.0);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                           }
                        }
                     }
                  } else if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ANAL && stripper2.getAllowedServices().isAllowed(Sextype.ANAL)) {
                     if (this.getCustomers().get(a).getGender() == Gender.MALE && Util.getInt(0, 10) > 5) {
                        if (Util.getInt(0, 10) <= 5
                           || !stripper2.getTraits().contains(Trait.ROWDYRUMP)
                              && !stripper2.getTraits().contains(Trait.AMBITOUSLOVER)
                              && !stripper2.getTraits().contains(Trait.HORNY)) {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.one", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                           }

                           if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);
                           }
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, stripper2));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.two", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 5, this);
                           stripper2.getFame().modifyFame(50.0);

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 10, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                              }
                           }
                        }
                     } else {
                        int analBead = 2 + stripper2.getFinalValue(Sextype.ANAL) / 10;
                        if (stripper2.getTraits().contains(Trait.DEEPLOVE)) {
                           analBead = (int)(analBead * 1.5);
                        }

                        Object[] arg2 = new Object[]{analBead, this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getName()};
                        if (Util.getInt(0, 10) > 5
                           && (
                              stripper2.getTraits().contains(Trait.KINKY)
                                 || stripper2.getTraits().contains(Trait.SUBMISSIVE)
                                 || stripper2.getTraits().contains(Trait.UNINHIBITED)
                           )) {
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ANAL, stripper2));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.two", stripper2, arg2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                           stripper2.getFame().modifyFame(50.0);

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(analBead + stripper2.getFinalValue(Sextype.ANAL) / 12, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                              }
                           }
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.one", stripper2, arg2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 7, this);

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 12, this);
                           }
                        }
                     }
                  } else if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ORAL && stripper2.getAllowedServices().isAllowed(Sextype.ORAL)) {
                     if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                        if (Util.getInt(0, 10) > 5
                           && (
                              stripper2.getTraits().contains(Trait.CUMSLUT)
                                 || stripper2.getTraits().contains(Trait.SENSUALTONGUE)
                                 || stripper2.getTraits().contains(Trait.ABSORPTION)
                           )) {
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ORAL, stripper2));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.two", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 5, this);
                           stripper2.getFame().modifyFame(50.0);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                           }

                           if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 10, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                       || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                 )) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                              }
                           }
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ORAL, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.one", stripper2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 7, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                           }

                           if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }
                        }
                     }

                     if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) {
                        break;
                     }

                     if (Util.getInt(0, 10) <= 5
                        || !stripper2.getTraits().contains(Trait.SENSUALTONGUE)
                           && !stripper2.getTraits().contains(Trait.OPENMINDED)
                           && !stripper2.getTraits().contains(Trait.HORNY)) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ORAL, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.one", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 7, this);
                        if (Util.getInt(1, 3) == 1
                           && (
                              this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                           )) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }

                        if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ORAL, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.two", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                        stripper2.getFame().modifyFame(50.0);
                        if (Util.getInt(1, 3) == 1
                           && (
                              this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                           )) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }

                        if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                           }
                        }
                     }
                  } else if (this.getCustomers().get(a).getPreferredSextype() != Sextype.TITFUCK
                     || !stripper2.getAllowedServices().isAllowed(Sextype.TITFUCK)
                     || stripper2.getGender() != Gender.FEMALE
                     || !stripper2.getTraits().contains(Trait.SMALLBOOBS) && !stripper2.getTraits().contains(Trait.BIGBOOBS)) {
                     if (Util.getInt(0, 10) <= 4 && this.getCustomers().size() >= 8) {
                        if (Util.getInt(0, 10) <= 5
                           || !stripper2.getTraits().contains(Trait.GANGBANGQUEEN)
                              && !stripper2.getTraits().contains(Trait.INSATIABLE)
                              && !stripper2.getTraits().contains(Trait.MULTIFACETED)) {
                           stripper2.getCounter()
                              .add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long)Util.getInt(1, 2 + this.getCustomers().size() / 4));
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.GROUP, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.group.one", stripper2, this.getCustomers().get(a));
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);
                           stripper2.getFame().modifyFame(100.0);

                           for (int cust = 0; cust < 8; cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.GROUP) / 7, this);
                           }

                           for (int cust = 8; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(-15, this);
                           }
                        } else {
                           stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), this.getCustomers().size() - 1L);
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.GROUP, stripper2));
                           this.getAttributeModifications().add(new AttributeModification(2.0F, EssentialAttributes.MOTIVATION, stripper2));
                           message = message + "\n" + TextUtil.t("STRIP.extras.group.two", stripper2, this.getCustomers().get(a));
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);
                           this.getHouse().modDirt(70);
                           stripper2.getFame().modifyFame(450.0);
                           stripper2.addCondition(new Buff.RoughenedUp());

                           for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                              this.getCustomers().get(cust).addToSatisfaction(10 + stripper2.getFinalValue(Sextype.GROUP) / 5, this);
                              if (Util.getInt(1, 3) == 1 && this.getCustomers().get(cust).getStatus() != CustomerStatus.STRONGSTATUS) {
                                 this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                              }
                           }
                        }
                     } else if (Util.getInt(0, 10) <= 5
                        || !stripper2.getTraits().contains(Trait.SENSITIVE)
                           && !stripper2.getTraits().contains(Trait.NICEBODY)
                           && !stripper2.getTraits().contains(Trait.TOUCHYFEELY)) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.FOREPLAY, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.one", stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, stripper2);
                        stripper2.getFame().modifyFame(20.0);
                        if (Util.getInt(1, 3) == 1
                           && (
                              this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                           )) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }

                        if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers()
                              .get(cust)
                              .addToSatisfaction(
                                 (stripper2.getFinalValue(Sextype.FOREPLAY) + stripper2.getFinalValue(SpecializationAttribute.STRIP)) / 15, this
                              );
                        }
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.FOREPLAY, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.two", stripper2, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, stripper2);
                        stripper2.getFame().modifyFame(50.0);
                        if (Util.getInt(1, 3) == 1
                           && (
                              this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                           )) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                        }

                        if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                        }

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers()
                              .get(cust)
                              .addToSatisfaction(
                                 (stripper2.getFinalValue(Sextype.FOREPLAY) + stripper2.getFinalValue(SpecializationAttribute.STRIP)) / 10, this
                              );
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                           }
                        }
                     }
                  } else {
                     if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                        if (stripper2.getTraits().contains(Trait.SMALLBOOBS)) {
                           if (Util.getInt(0, 10) > 5
                              && (
                                 stripper2.getTraits().contains(Trait.CUMSLUT)
                                    || stripper2.getTraits().contains(Trait.WILD)
                                    || stripper2.getTraits().contains(Trait.OILY)
                              )) {
                              this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.TITFUCK, stripper2));
                              this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                              message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", stripper2);
                              image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                              this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 5, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                                 )) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                              }

                              if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                                 this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 10, this);
                                 if (Util.getInt(1, 3) == 1
                                    && (
                                       this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                          || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                    )) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                 }

                                 if (Util.getInt(1, 3) == 1
                                    && (
                                       this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                          || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                    )) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                 }
                              }

                              stripper2.getFame().modifyFame(50.0);
                           } else {
                              this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, stripper2));
                              message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", stripper2);
                              image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                              this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                                 )) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                              }

                              if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                              }
                           }
                        } else if (stripper2.getTraits().contains(Trait.BIGBOOBS)) {
                           if (Util.getInt(0, 10) > 5
                              && (
                                 stripper2.getTraits().contains(Trait.WENCH)
                                    || stripper2.getTraits().contains(Trait.WENCH)
                                    || stripper2.getTraits().contains(Trait.COMETOMOMMY)
                              )) {
                              this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.TITFUCK, stripper2));
                              this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                              message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", stripper2);
                              image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                              this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 5, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                                 )) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                              }

                              if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                                 this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 10, this);
                                 if (Util.getInt(1, 3) == 1
                                    && (
                                       this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                          || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                                    )) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                                 }

                                 if (Util.getInt(1, 3) == 1
                                    && (
                                       this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                          || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                                    )) {
                                    this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                                 }
                              }

                              stripper2.getFame().modifyFame(50.0);
                           } else {
                              this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, stripper2));
                              message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", stripper2);
                              image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);
                              this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
                              if (Util.getInt(1, 3) == 1
                                 && (
                                    this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                                       || this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                                 )) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.TIRED);
                              }

                              if (Util.getInt(1, 2) == 1 && this.getCustomers().get(a).getStatus() == CustomerStatus.VERYHORNY) {
                                 this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                              }

                              for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                                 this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-15, 15), this);
                              }
                           }
                        }
                     }

                     if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) {
                        break;
                     }

                     if (Util.getInt(0, 10) > 5
                        && (
                           stripper2.getTraits().contains(Trait.EXHIBITIONIST)
                              || stripper2.getTraits().contains(Trait.UNINHIBITED)
                              || stripper2.getTraits().contains(Trait.TOUCHYFEELY)
                        )) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, stripper2));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.two", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.LIVELY
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           if (Util.getInt(1, 3) == 1
                              && (
                                 this.getCustomers().get(cust).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(cust).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);
                           }
                        }

                        stripper2.getFame().modifyFame(50.0);
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, stripper2));
                        message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.one", stripper2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, stripper2);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(10 + stripper2.getFinalValue(Sextype.FOREPLAY), this);
                        }
                     }
                  }
                  break;
               case SHY:
                  if (stripper2.getTraits().contains(Trait.PURE)) {
                     this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.shy.win", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(10, this);
                     }
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.shy.lose", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(-10, this);
                     }
                  }
                  break;
               case UNHINIBITED:
                  if (!stripper2.getTraits().contains(Trait.LEWD) && Util.getInt(0, 3) != 2) {
                     message = TextUtil.t("STRIP.event.lewd.lose", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 5), this);
                     }
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(0.1F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.lewd.win", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(10, this);
                     }
                  }
                  break;
               case FRAGILE:
                  if (stripper2.getTraits().contains(Trait.LASCIVIOUS)) {
                     this.getAttributeModifications().add(new AttributeModification(0.1F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.fragile.win", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(7, this);
                     }
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.fragile.lose", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(-15, this);
                     }
                  }
                  break;
               case FIT:
                  if (stripper2.getTraits().contains(Trait.PERFECTCONDITION) && Util.getInt(0, 3) == 1) {
                     this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.fit.win", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(stripSkill / 5, this);
                     }
                  } else {
                     message = TextUtil.t("STRIP.event.fit.winwin", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(10, this);
                        if (this.getCustomers().get(cust).getStatus() == CustomerStatus.SHYSTATUS && Util.getInt(1, 4) == 2) {
                           this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                        }

                        if (this.getCustomers().get(cust).getStatus() == CustomerStatus.TIRED && Util.getInt(1, 4) == 2) {
                           this.getCustomers().get(cust).setStatus(CustomerStatus.LIVELY);
                        }
                     }
                  }
                  break;
               case STIFF:
                  message = TextUtil.t("STRIP.event.stiff", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(-stripSkill / 2, this);
                  }
                  break;
               case BIGTITS:
                  message = TextUtil.t("STRIP.event.bigtits", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.TITFUCK) / 7, this);
                  }
                  break;
               case CLUMSY:
                  if (Util.getInt(0, 3) == 2) {
                     this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.clumsy.win", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 7), this);
                     }
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(-0.3F, EssentialAttributes.MOTIVATION, stripper2));
                     message = TextUtil.t("STRIP.event.clumsy.lose", stripper2, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(-15, this);
                     }
                  }
                  break;
               case RUBCLIT:
                  message = TextUtil.t("STRIP.event.rubclit", stripper2, this.getCustomers().get(a));
                  this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getFinalValue(Sextype.FOREPLAY) / 10, this);
                  }
                  break;
               case LICKPOLE:
                  message = TextUtil.t("STRIP.event.lickpole", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10, this);
                  }
                  break;
               case OILY:
                  message = TextUtil.t("STRIP.event.oily", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(5 + stripper2.getCharisma() / 5, this);
                  }
                  break;
               case FELINEORGY:
                  message = TextUtil.t("STRIP.event.felineorgy", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getCharisma() / 5, this);
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ANAL) / 5, this);
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.VAGINAL) / 5, this);
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.ORAL) / 5, this);
                     this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.ENERGY, stripper2));
                     this.getAttributeModifications().add(new AttributeModification(0.5F, Sextype.GROUP, stripper2));
                     if (Util.getInt(0, 100) < this.getCustomers().size()) {
                        stripper2.addCondition(new Buff.Exhausted());
                     }

                     stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                  }
                  break;
               case CUMDRINK:
                  message = TextUtil.t("STRIP.event.cumdrink", stripper2, this.getCustomers().get(a));
                  this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, stripper2));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, stripper2);
                  stripper2.getFame().modifyFame(250.0);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers()
                        .get(cust)
                        .addToSatisfaction(10 + (stripper2.getFinalValue(Sextype.ORAL) + stripper2.getFinalValue(Sextype.FOREPLAY)) / 12, this);
                  }
                  break;
               case SEXSMELL:
                  if (Util.getInt(0, 2) == 0) {
                     message = TextUtil.t("STRIP.event.sexsmell.smell", stripper2, this.getCustomers().get(a));
                  } else {
                     message = TextUtil.t("STRIP.event.sexsmell.move", stripper2, this.getCustomers().get(a));
                  }

                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, stripper2);
                  stripper2.getFame().modifyFame(150.0);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(15, this);
                  }
                  break;
               case FONDLE:
                  rnd = Util.getInt(0, 3);
                  if (stripper2.getTraits().contains(Trait.BIGBOOBS) && rnd == 0) {
                     message = TextUtil.t("STRIP.event.fondle.bigbreasts", stripper2, this.getCustomers().get(a));
                  }

                  if (stripper2.getTraits().contains(Trait.SMALLBOOBS) && rnd == 1) {
                     message = TextUtil.t("STRIP.event.fondle.smallbreasts", stripper2, this.getCustomers().get(a));
                  } else {
                     message = TextUtil.t("STRIP.event.fondle.butt", stripper2, this.getCustomers().get(a));
                  }

                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     if (this.getCustomers().get(cust).getType() == CustomerType.MINORNOBLE
                        || this.getCustomers().get(cust).getType() == CustomerType.BUSINESSMAN
                        || this.getCustomers().get(cust).getType() == CustomerType.LORD
                        || this.getCustomers().get(cust).getType() == CustomerType.CELEBRITY) {
                        this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 5, this);
                     }
                  }
                  break;
               case CREAM:
                  message = TextUtil.t("STRIP.event.cream", stripper2, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, stripper2);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                  }
                  break;
               case SUBMISSIVE:
                  message = TextUtil.t("STRIP.event.submissive", stripper2, arg) + "\n";
                  stripper2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                  rnd = Util.getInt(0, 3);
                  switch (rnd) {
                     case 0:
                        if (stripper2.getGender() != Gender.MALE) {
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, stripper2);
                           message = message + TextUtil.t("STRIP.event.submissive.vaginal", stripper2, arg);
                           this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        }
                        break;
                     case 1:
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, stripper2);
                        message = message + TextUtil.t("STRIP.event.submissive.anal", stripper2, arg);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                        break;
                     default:
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, stripper2);
                        message = message + TextUtil.t("STRIP.event.submissive.oral", stripper2, arg);
                        this.getCustomers().get(a).addToSatisfaction(stripper2.getFinalValue(Sextype.FOREPLAY) / 7, this);
                  }
            }

            if (message != null) {
               this.getMessages().add(new MessageData(message, image, stripper2.getBackground()));
            }
         }
      }

      Iterator i$ = this.bartenders.iterator();

      while (true) {
         Charakter bartender2;
         String message;
         ImageData image;
         label1226:
         while (true) {
            if (!i$.hasNext()) {
               return;
            }

            bartender2 = (Charakter)i$.next();
            List<Bartend.BarAction> actions = new ArrayList<>();
            if (bartender2.getTraits().contains(Trait.DATASS)) {
               actions.add(Bartend.BarAction.GROPE);
               actions.add(Bartend.BarAction.FLIP);
               actions.add(Bartend.BarAction.LOOK);
               actions.add(Bartend.BarAction.CROWD);
               actions.add(Bartend.BarAction.SLAP);
            }

            if (bartender2.getTraits().contains(Trait.FLIRTY)) {
               actions.add(Bartend.BarAction.SITLAP);
               actions.add(Bartend.BarAction.SITGROUP);
               actions.add(Bartend.BarAction.CHAT);
               actions.add(Bartend.BarAction.TEASELOT);
            }

            if (bartender2.getTraits().contains(Trait.UNDERTHETABLE)) {
               actions.add(Bartend.BarAction.FUCK);
               actions.add(Bartend.BarAction.BLOWJOB);
               actions.add(Bartend.BarAction.GROUP);
            }

            if (bartender2.getTraits().contains(Trait.UNDERTHETABLE) && bartender2.getTraits().contains(Trait.WENCH)) {
               actions.add(Bartend.BarAction.FUCK);
               actions.add(Bartend.BarAction.BLOWJOB);
               actions.add(Bartend.BarAction.GROUP);
            }

            if (bartender2.getTraits().contains(Trait.OUTGOING)) {
               actions.add(Bartend.BarAction.LOUDBUNCH);
               actions.add(Bartend.BarAction.SMALLTALK);
            }

            if (bartender2.getTraits().contains(Trait.STUPID)) {
               actions.add(Bartend.BarAction.DUMBBJ);
               actions.add(Bartend.BarAction.DUMBFUCK);
            }

            if (bartender2.getIntelligence() < 5) {
               actions.add(Bartend.BarAction.DUMBBJ);
               actions.add(Bartend.BarAction.DUMBFUCK);
            }

            if (bartender2.getTraits().contains(Trait.OUTGOING) && bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > 300) {
               actions.add(Bartend.BarAction.LOUDBUNCH);
               actions.add(Bartend.BarAction.SMALLTALK);
            }

            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 5L) {
               actions.add(Bartend.BarAction.SEXSMELL);
            }

            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 10L) {
               actions.add(Bartend.BarAction.SEXSMELL);
            }

            if (bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 15L) {
               actions.add(Bartend.BarAction.SEXSMELL);
            }

            if (bartender2.getTraits().contains(Trait.BIGBOOBS)) {
               actions.add(Bartend.BarAction.BIGBOOB);
            }

            if (bartender2.getTraits().contains(Trait.SMALLBOOBS)) {
               actions.add(Bartend.BarAction.SMALLBOOB);
            }

            if (bartender2.getTraits().contains(Trait.LOLI)) {
               actions.add(Bartend.BarAction.LOLI);
            }

            if (bartender2.getTraits().contains(Trait.CLUMSY)) {
               actions.add(Bartend.BarAction.BREAKSTUFF);
            }

            if (bartender2.getTraits().contains(Trait.FRAGILE)) {
               actions.add(Bartend.BarAction.TIRED);
            }

            if (bartender2.getTraits().contains(Trait.SHY)) {
               actions.add(Bartend.BarAction.SHY);
            }

            if (bartender2.getTraits().contains(Trait.LAGOMORPHQUICKY)) {
               actions.add(Bartend.BarAction.LAGOMORPHQUICKIE);
            }

            if (bartender2.getTraits().contains(Trait.LAGOMORPHORGY) && Util.getInt(0, 100) < 70) {
               actions.add(Bartend.BarAction.LAGOMORPHORGY);
            }

            if (bartender2.getTraits().contains(Trait.RESTAURATEUR) || bartender2.getFinalValue(SpecializationAttribute.COOKING) > 150) {
               actions.add(Bartend.BarAction.SNACKS);
            }

            if (Util.getInt(0, 100) < 10 + actions.size() * 6 && this.getCustomers().size() > 10 && actions.size() > 0) {
               this.bartenderAction.put(bartender2, actions.get(Util.getInt(0, actions.size())));
               message = null;
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
               int a = Util.getInt(0, this.getCustomers().size() - 1);
               Object[] arg = new Object[]{this.getCustomers().get(a).getName()};
               int rnd = 0;
               switch ((Bartend.BarAction)this.bartenderAction.get(bartender2)) {
                  case FLIP:
                     message = TextUtil.t("barevent.flip", bartender2, arg);
                     if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) <= 7
                        && !bartender2.getTraits().contains(Trait.OPENMINDED)
                        && !bartender2.getTraits().contains(Trait.OBEDIENT)) {
                        this.getAttributeModifications().add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.flip.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        rnd = Util.getInt(1, this.getCustomers().size() - 1);
                        this.getCustomers().get(rnd).setStatus(CustomerStatus.PISSED);
                        this.getCustomers().get(rnd).addToSatisfaction(-20, this);
                        if (Util.getInt(1, 4) == 1
                           && (
                              this.getCustomers().get(rnd).getStatus() == CustomerStatus.DRUNK
                                 || this.getCustomers().get(rnd).getStatus() == CustomerStatus.VERYDRUNK
                           )) {
                           this.getCustomers().get(rnd).setStatus(CustomerStatus.TIRED);
                        }
                        break label1226;
                     }

                     if (Util.getInt(0, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.flip.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                        this.getCustomers().get(Util.getInt(1, this.getCustomers().size() - 1)).addToSatisfaction(5, this);
                        break label1226;
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.OBEDIENCE, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.flip.okay", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);

                     for (Customer cust : this.getCustomers()) {
                        if ((cust.getStatus() == CustomerStatus.LIVELY || cust.getStatus() == CustomerStatus.HAPPY || cust.getStatus() == CustomerStatus.DRUNK)
                           && Util.getInt(0, 100) > 50) {
                           cust.addToSatisfaction(5, this);
                        }
                     }
                     break label1226;
                  case GROPE:
                     message = TextUtil.t("barevent.grope", bartender2, arg);
                     if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) > 7
                        || bartender2.getTraits().contains(Trait.WENCH)
                        || bartender2.getTraits().contains(Trait.TOUCHYFEELY) && Util.getInt(0, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.07F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                        bartender2.getFame().modifyFame(15.0);
                        int income = bartender2.getCharisma() / 2;

                        for (Customer customer : this.getCustomers()) {
                           if (Util.getInt(1, 10) > 4 && customer.getStatus() == CustomerStatus.HORNYSTATUS || customer.getStatus() == CustomerStatus.VERYHORNY
                              )
                            {
                              customer.payFixed(bartender2.getCharisma() / 2);
                              this.modifyIncome(bartender2.getCharisma() / 2);
                              customer.addToSatisfaction(5, this);
                              income += bartender2.getCharisma() / 2;
                           }
                        }

                        Object[] gropeincome = new Object[]{income};
                        message = message + "\n" + TextUtil.t("barevent.grope.winwin", bartender2, gropeincome);
                        break label1226;
                     }

                     if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) <= 5
                        && !bartender2.getTraits().contains(Trait.WENCH)
                        && !bartender2.getTraits().contains(Trait.TOUCHYFEELY)) {
                        this.getAttributeModifications().add(new AttributeModification(-0.07F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.grope.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-10, this);
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.07F, BaseAttributeTypes.OBEDIENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.grope.win", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                     bartender2.getFame().modifyFame(15.0);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                           customer.setStatus(CustomerStatus.VERYHORNY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                           customer.setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                           customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     }
                  case LOOK:
                     message = TextUtil.t("barevent.look", bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) <= Util.getInt(10, 25)
                        && !bartender2.getTraits().contains(Trait.OUTGOING)
                        && !bartender2.getTraits().contains(Trait.CROWDLOVER)) {
                        message = message + "\n" + TextUtil.t("barevent.look.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.3F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-10, this);
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.look.win", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 5, this);
                        if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                           customer.setStatus(CustomerStatus.VERYHORNY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                           customer.setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                           customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     }
                  case CROWD:
                     message = TextUtil.t("barevent.crowd", bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.STRIP) <= 30 && !bartender2.getTraits().contains(Trait.WILD)) {
                        this.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.05F, Sextype.FOREPLAY, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.crowd.lose", bartender2, arg);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(Util.getInt(-10, 15), this);
                           if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.SHYSTATUS) {
                              customer.setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SHYSTATUS || customer.getStatus() == CustomerStatus.LIVELY)) {
                              customer.setStatus(CustomerStatus.PISSED);
                           }

                           if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.HYPED || customer.getStatus() == CustomerStatus.DRUNK)) {
                              customer.setStatus(CustomerStatus.PISSED);
                           }

                           if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                              customer.setStatus(CustomerStatus.VERYHORNY);
                           }

                           if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                              customer.setStatus(CustomerStatus.HAPPY);
                           }

                           if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                              customer.setStatus(CustomerStatus.HORNYSTATUS);
                           }
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.STAMINA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(1.05F, SpecializationAttribute.BARTENDING, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.55F, SpecializationAttribute.STRIP, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.7F, EssentialAttributes.MOTIVATION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.crowd.win", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.STRIP) / 9, this);
                        if (Util.getInt(1, 6) == 1 && customer.getStatus() == CustomerStatus.HORNYSTATUS) {
                           customer.setStatus(CustomerStatus.VERYHORNY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                           customer.setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                           customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     }
                  case SITLAP:
                     message = TextUtil.t("barevent.sitlap", bartender2);
                     if (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) > 15 && Util.getInt(1, 4) == 2) {
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.65F, SpecializationAttribute.BARTENDING, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.sitlap.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        rnd = Util.getInt(0, this.getCustomers().size());
                        this.getCustomers()
                           .get(rnd)
                           .addToSatisfaction(
                              (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 2 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 7) / 2,
                              this
                           );
                        this.getCustomers().get(rnd).payFixed(200);
                        if (Util.getInt(1, 6) == 1 && this.getCustomers().get(rnd).getStatus() == CustomerStatus.HORNYSTATUS) {
                           this.getCustomers().get(rnd).setStatus(CustomerStatus.VERYHORNY);
                        }

                        if (Util.getInt(1, 4) == 1
                           && (
                              this.getCustomers().get(rnd).getStatus() == CustomerStatus.SAD
                                 || this.getCustomers().get(rnd).getStatus() == CustomerStatus.PISSED
                           )) {
                           this.getCustomers().get(rnd).setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1
                           && (
                              this.getCustomers().get(rnd).getStatus() == CustomerStatus.LIVELY
                                 || this.getCustomers().get(rnd).getStatus() == CustomerStatus.DRUNK
                           )) {
                           this.getCustomers().get(rnd).setStatus(CustomerStatus.HORNYSTATUS);
                        }

                        this.modifyIncome(200);
                        break label1226;
                     }

                     message = message + "\n" + TextUtil.t("barevent.sitlap.lose", bartender2);
                     this.getAttributeModifications().add(new AttributeModification(-0.2F, EssentialAttributes.MOTIVATION, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                     break label1226;
                  case CHAT:
                     message = TextUtil.t("barevent.chat", bartender2, arg);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.BARTENDING, bartender2));
                     if (Util.getInt(0, 20) <= 10) {
                        message = message + "\n" + TextUtil.t("barevent.chat.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        break label1226;
                     }

                     message = message + "\n" + TextUtil.t("barevent.chat.win", bartender2);
                     this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                     this.getCustomers().get(a).addToSatisfaction(5, this);
                     if (Util.getInt(1, 4) == 1
                        && (this.getCustomers().get(a).getStatus() == CustomerStatus.SAD || this.getCustomers().get(a).getStatus() == CustomerStatus.PISSED)) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.HAPPY);
                     }

                     if (Util.getInt(1, 4) == 1
                        && (
                           this.getCustomers().get(a).getStatus() == CustomerStatus.HAPPY || this.getCustomers().get(a).getStatus() == CustomerStatus.SHYSTATUS
                        )) {
                        this.getCustomers().get(a).setStatus(CustomerStatus.LIVELY);
                     }
                     break label1226;
                  case SITGROUP:
                     message = TextUtil.t("barevent.sitgroup", bartender2);
                     if (Util.getInt(1, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.BARTENDING, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        Object[] arg2 = new Object[]{this.getCustomers().size() * 10 - 10};
                        message = message + "\n" + TextUtil.t("barevent.sitgroup.win", bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        int b = 0;

                        while (true) {
                           if (b >= this.getCustomers().size() / 10) {
                              break label1226;
                           }

                           this.getCustomers().get(b).payFixed(100);
                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE)) / 3, this
                              );
                           this.modifyIncome(100);
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.SAD
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.HAPPY);
                           }

                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.SHYSTATUS
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.HAPPY
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.DRUNK);
                           }

                           if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() == CustomerStatus.DRUNK) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.VERYDRUNK);
                           }

                           b++;
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.75F, Sextype.FOREPLAY, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     Object[] arg2 = new Object[]{this.getCustomers().size() * 40 - 40};
                     message = message + "\n" + TextUtil.t("barevent.sitgroup.lose", bartender2, arg2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                     int b = 0;

                     while (true) {
                        if (b >= this.getCustomers().size()) {
                           break label1226;
                        }

                        if (a < this.getCustomers().size() / 10) {
                           this.getCustomers().get(b).payFixed(400);
                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE)) / 3, this
                              );
                           this.modifyIncome(400);
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.SAD
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.HAPPY);
                           }

                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.SHYSTATUS
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.HAPPY
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.DRUNK);
                           }

                           if (Util.getInt(1, 4) == 1
                              && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS
                              && this.getCustomers().get(b).getStatus() != CustomerStatus.VERYHORNY) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);
                           }
                        } else {
                           this.getCustomers().get(a).addToSatisfaction(-10, this);
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.HYPED
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.PISSED);
                           }
                        }

                        b++;
                     }
                  case TEASELOT:
                     message = TextUtil.t("barevent.teaselot", bartender2);
                     this.getAttributeModifications().add(new AttributeModification(0.3F, EssentialAttributes.MOTIVATION, bartender2));
                     if (Util.getInt(1, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.55F, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.teaselot.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        int b = 0;

                        while (true) {
                           if (b >= this.getCustomers().size()) {
                              break label1226;
                           }

                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 10 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10,
                                 this
                              );
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.VERYHORNY);
                           }

                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(b).getStatus() == CustomerStatus.SAD
                                    || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED
                              )) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           b++;
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.75F, SpecializationAttribute.BARTENDING, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.teaselot.lose", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                     int b = 0;

                     while (true) {
                        if (b >= this.getCustomers().size()) {
                           break label1226;
                        }

                        this.getCustomers()
                           .get(b)
                           .addToSatisfaction(
                              bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 10 + bartender2.getFinalValue(SpecializationAttribute.BARTENDING) / 10,
                              this
                           );
                        if (Util.getInt(1, 4) == 1
                           && (this.getCustomers().get(b).getStatus() == CustomerStatus.SAD || this.getCustomers().get(b).getStatus() == CustomerStatus.PISSED)
                           )
                         {
                           this.getCustomers().get(a).setStatus(CustomerStatus.HAPPY);
                        }

                        b++;
                     }
                  case BLOWJOB:
                     message = TextUtil.t("barevent.ninjablowjob", bartender2, arg);
                     Customer cust = this.getCustomers().get(a);
                     int blowjobPay = 25 + bartender2.getFinalValue(Sextype.ORAL) * bartender2.getFinalValue(Sextype.ORAL) / 10;
                     Object[] arg3 = new Object[]{this.getCustomers().get(a).getName(), blowjobPay};
                     if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) <= 6 && !bartender2.getTraits().contains(Trait.WENCH)) {
                        message = message + "\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        cust.addToSatisfaction(-100, this);
                        cust.payFixed(100);
                        this.modifyIncome(100);
                        cust.setStatus(CustomerStatus.PISSED);
                        break label1226;
                     }

                     bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.6F, Sextype.ORAL, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.65F, SpecializationAttribute.SEDUCTION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.ninjablowjob.win", bartender2, cust, arg3);
                     message = message + "\n" + TextUtil.t("barevent.ninjablowjob.gold", bartender2, arg3);
                     if (cust.getGender() == Gender.MALE) {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, bartender2);
                     } else {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, bartender2);
                     }

                     cust.addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL) / 6, this);
                     cust.payFixed(blowjobPay);
                     this.modifyIncome(blowjobPay);
                     if (bartender2.getTraits().contains(Trait.SLURPYSLURP) && bartender2.getFinalValue(Sextype.ORAL) > 25 && Util.getInt(0, 100) > 50) {
                        int i = Util.getInt(1, 5);
                        int total = 0;

                        for (int z = 0; z < i + bartender2.getFinalValue(Sextype.ORAL) / 20; z++) {
                           this.getCustomers().get(z).payFixed(blowjobPay * (100 - i) / 100);
                           this.modifyIncome(blowjobPay * (100 - i) / 100);
                           total += blowjobPay * (100 - i) / 100;
                           this.getCustomers().get(z).addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL) / 6, this);
                           if (cust.getStatus() == CustomerStatus.HORNYSTATUS) {
                              cust.setStatus(CustomerStatus.LIVELY);
                           }

                           if (cust.getStatus() == CustomerStatus.SAD || cust.getStatus() == CustomerStatus.PISSED) {
                              cust.setStatus(CustomerStatus.HAPPY);
                           }
                        }

                        Object[] multiBJincome = new Object[]{total};
                        message = message + "\n" + TextUtil.t("barevent.ninjablowjob.winmore", bartender2, cust, multiBJincome);
                        if (Util.getInt(0, 100) > 50) {
                           message = message + "\n" + TextUtil.t("barevent.ninjablowjob.drink", bartender2);
                        } else {
                           message = message + "\n" + TextUtil.t("barevent.ninjablowjob.bukkake", bartender2);
                        }
                     }

                     if (cust.getStatus() == CustomerStatus.HORNYSTATUS) {
                        cust.setStatus(CustomerStatus.LIVELY);
                     }

                     if (cust.getStatus() == CustomerStatus.SAD || cust.getStatus() == CustomerStatus.PISSED) {
                        cust.setStatus(CustomerStatus.HAPPY);
                     }
                     break label1226;
                  case LAGOMORPHQUICKIE:
                     int quickieAmount = 0;
                     Sextype sex2 = null;
                     int tips = 0;
                     int tip = 0;
                     if (Util.getInt(1, 100) > 50) {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
                     } else {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     }

                     for (Customer cust2 : this.getCustomers()) {
                        if ((cust2.getType() == CustomerType.SOLDIER || cust2.getStatus() == CustomerStatus.STRONGSTATUS)
                           && Util.getInt(0, 100) < 30
                           && quickieAmount < 3 + (bartender2.getFinalValue(Sextype.ANAL) + bartender2.getFinalValue(Sextype.VAGINAL)) / 5) {
                           if (Util.getInt(1, 100) > 50) {
                              sex2 = Sextype.VAGINAL;
                           } else {
                              sex2 = Sextype.ANAL;
                           }

                           this.getAttributeModifications().add(new AttributeModification(0.2F, sex2, bartender2));
                           cust2.addToSatisfaction(5, this);
                           quickieAmount++;
                           if (Util.getInt(0, 100) < 45) {
                              tip = Util.getInt(5, 10) + bartender2.getFinalValue(sex2) / 3;
                              tip = cust2.payFixed(tip);
                              tips += tip;
                           }
                        }
                     }

                     Object[] quickieArgs = new Object[]{quickieAmount, tips};
                     message = TextUtil.t("barevent.lagomorphquickie", bartender2, quickieArgs);
                     break label1226;
                  case DUMBFUCK:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.VAGINAL, bartender2));
                     this.getCustomers()
                        .get(Util.getInt(0, this.getCustomers().size() - 1))
                        .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                     message = TextUtil.t("barevent.dumbfuck", bartender2);
                     break label1226;
                  case DUMBBJ:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.ORAL, bartender2));
                     this.getCustomers()
                        .get(Util.getInt(0, this.getCustomers().size() - 1))
                        .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                     message = TextUtil.t("barevent.bj", bartender2);
                     break label1226;
                  case LAGOMORPHORGY:
                     int rand2 = Util.getInt(10, 20);
                     int fraction = Util.getInt(7, 10) - bartender2.getFinalValue(SpecializationAttribute.TRANSFORMATION) / 10;
                     Object[] arg5 = new Object[]{1 + this.getCustomers().size() / fraction, this.getCustomers().size() * rand2 / fraction};
                     message = TextUtil.t("barevent.lagomorphorgy", bartender2, arg5);
                     bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L + this.getCustomers().size() / 10);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.GROUP, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.SEDUCTION, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                     int b = 0;

                     while (true) {
                        if (b >= this.getCustomers().size() / fraction) {
                           break label1226;
                        }

                        this.getCustomers().get(b).payFixed(rand2);
                        this.getCustomers()
                           .get(b)
                           .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                        if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                           this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }

                        b++;
                     }
                  case FUCK:
                     Customer cust2 = this.getCustomers().get(a);
                     Sextype sex = null;
                     if (Util.getInt(1, 100) > 50) {
                        sex = Sextype.VAGINAL;
                     } else {
                        sex = Sextype.ANAL;
                     }

                     int fuckPay = bartender2.getFinalValue(sex) * bartender2.getFinalValue(sex) / 8;
                     fuckPay += 100;
                     fuckPay = Math.min(fuckPay, cust2.getMoney());
                     Object[] arg4 = new Object[]{this.getCustomers().get(a).getName(), fuckPay};
                     message = TextUtil.t("barevent.ninjablowjob", bartender2, arg4);
                     if (bartender2.getTraits().contains(Trait.WENCH) && Util.getInt(1, 100) > 50) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1F, sex, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.05F, SpecializationAttribute.SEDUCTION, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.8F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.fuck.wench", bartender2, arg4);
                        if (sex == Sextype.VAGINAL) {
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                        } else {
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
                        }

                        cust2.addToSatisfaction(bartender2.getFinalValue(sex) / 4, this);
                        cust2.payFixed(fuckPay);
                        this.modifyIncome(fuckPay);
                        if (Util.getInt(0, 100) < 20 + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 2) {
                           int linefuck = 0;
                           int total = 0;

                           for (int z = 0; z < this.getCustomers().size(); z++) {
                              if (linefuck <= (bartender2.getFinalValue(Sextype.VAGINAL) + bartender2.getFinalValue(Sextype.ANAL)) / 5
                                    && (
                                       this.getCustomers().get(z).getStatus() == CustomerStatus.HORNYSTATUS
                                          || this.getCustomers().get(z).getStatus() == CustomerStatus.VERYHORNY
                                    )
                                 || linefuck < 3) {
                                 linefuck++;
                                 this.getCustomers()
                                    .get(z)
                                    .addToSatisfaction(bartender2.getFinalValue(sex) - bartender2.getFinalValue(sex) * linefuck / 50, this);
                                 this.getCustomers().get(z).payFixed(fuckPay - fuckPay * linefuck / 50);
                                 total += fuckPay - fuckPay * linefuck / 50;
                                 bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                              }
                           }

                           if (linefuck > 4) {
                              this.getAttributeModifications().add(new AttributeModification(linefuck * 0.4F, sex, bartender2));
                              this.getAttributeModifications().add(new AttributeModification(linefuck * -1.8F, EssentialAttributes.ENERGY, bartender2));
                              Object[] LinefuckTotal = new Object[]{linefuck};
                              message = message + "\n" + TextUtil.t("barevent.fuck.line", bartender2, LinefuckTotal);
                              Object[] LinefuckPay = new Object[]{total};
                              message = message + "\n" + TextUtil.t("barevent.fuck.line.pay", bartender2, LinefuckPay);
                              this.modifyIncome(total);
                           }
                           break label1226;
                        }

                        int z = 0;

                        while (true) {
                           if (z >= this.getCustomers().size()) {
                              break label1226;
                           }

                           this.getCustomers().get(z).payFixed(100);
                           this.getCustomers()
                              .get(z)
                              .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(sex) / 8) / 4, this);
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.VERYHORNY);
                           }

                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(a).getStatus() == CustomerStatus.HAPPY
                                    || this.getCustomers().get(a).getStatus() == CustomerStatus.LIVELY
                              )) {
                              this.getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           z++;
                        }
                     }

                     if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) <= 9 && !bartender2.getTraits().contains(Trait.WENCH)) {
                        message = message + "\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        cust2.addToSatisfaction(-50, this);
                        cust2.payFixed(Math.min(100, cust2.getMoney()));
                        this.modifyIncome(Math.min(100, cust2.getMoney()));
                        cust2.setStatus(CustomerStatus.PISSED);
                        break label1226;
                     }

                     bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.5F, sex, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.5F, SpecializationAttribute.SEDUCTION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.fuck.win", bartender2, arg4);
                     if (sex == Sextype.VAGINAL) {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     } else {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
                     }

                     cust2.addToSatisfaction(bartender2.getFinalValue(sex) / 5, this);
                     cust2.payFixed(fuckPay);
                     this.modifyIncome(fuckPay);
                     if (cust2.getStatus() == CustomerStatus.HORNYSTATUS) {
                        cust2.setStatus(CustomerStatus.LIVELY);
                     }

                     if (cust2.getStatus() == CustomerStatus.SAD || cust2.getStatus() == CustomerStatus.PISSED) {
                        cust2.setStatus(CustomerStatus.HAPPY);
                     }
                     break label1226;
                  case GROUP:
                     long servedToday2 = bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
                     int rand = Util.getInt(100, 101 + bartender2.getFinalValue(Sextype.GROUP) + bartender2.getFinalValue(SpecializationAttribute.SEDUCTION));
                     int size = Util.getInt(8, 10);
                     Object[] arg2 = new Object[]{
                        1 + this.getCustomers().size() / size,
                        this.getCustomers().get(Util.getInt(0, this.getCustomers().size() - 1)).getName(),
                        this.getCustomers().size() * rand / size
                     };
                     message = TextUtil.t("barevent.group", bartender2, arg2);
                     if (bartender2.getTraits().contains(Trait.SEXADDICT) && Util.getInt(1, 100) > 50) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L + this.getCustomers().size() / 10);
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.55F, SpecializationAttribute.SEDUCTION, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(2.0F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.queen", bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);

                        for (int b = 0; b < this.getCustomers().size() / size; b++) {
                           this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this
                              );
                           if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                           }
                        }

                        int z = this.getCustomers().size() / size;

                        while (true) {
                           if (z >= this.getCustomers().size()) {
                              break label1226;
                           }

                           this.getCustomers()
                              .get(z)
                              .addToSatisfaction(
                                 15 + (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 4, this
                              );
                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(z).getStatus() == CustomerStatus.HORNYSTATUS
                                    || this.getCustomers().get(z).getStatus() == CustomerStatus.HYPED
                              )) {
                              this.getCustomers().get(z).setStatus(CustomerStatus.VERYHORNY);
                           }

                           if (Util.getInt(1, 4) == 1
                              && (
                                 this.getCustomers().get(z).getStatus() == CustomerStatus.HAPPY
                                    || this.getCustomers().get(z).getStatus() == CustomerStatus.LIVELY
                              )) {
                              this.getCustomers().get(z).setStatus(CustomerStatus.HORNYSTATUS);
                           }

                           z++;
                        }
                     }

                     if (servedToday2 > 7L) {
                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L + this.getCustomers().size() / 10);
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.lotalready", bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        int b = 0;

                        while (true) {
                           if (b >= this.getCustomers().size() / size) {
                              break label1226;
                           }

                           this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this
                              );
                           if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                           }

                           b++;
                        }
                     }

                     if (bartender2.getFinalValue(Sextype.GROUP) <= 35 && bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) <= 35) {
                        if (bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE) <= 15 && !bartender2.getTraits().contains(Trait.NYMPHO)) {
                           message = message + "\n" + TextUtil.t("barevent.group.lose", bartender2);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                           int b = 0;

                           while (true) {
                              if (b >= this.getCustomers().size() / 15) {
                                 break label1226;
                              }

                              this.getCustomers().get(b).addToSatisfaction(-10, this);
                              if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                                 this.getCustomers().get(b).setStatus(CustomerStatus.PISSED);
                              }

                              b++;
                           }
                        }

                        bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L + this.getCustomers().size() / 10);
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.GROUP, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.SEDUCTION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.group.win", bartender2, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                        int b = 0;

                        while (true) {
                           if (b >= this.getCustomers().size() / size) {
                              break label1226;
                           }

                           this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                           this.getCustomers()
                              .get(b)
                              .addToSatisfaction(
                                 (bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this
                              );
                           if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                              this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                           }

                           b++;
                        }
                     }

                     bartender2.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L + this.getCustomers().size() / 10);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.OBEDIENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.GROUP, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.SEDUCTION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.group.win.usedtoit", bartender2, arg2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                     int b = 0;

                     while (true) {
                        if (b >= this.getCustomers().size() / size) {
                           break label1226;
                        }

                        this.getCustomers().get(b).payFixed(Math.min(rand, this.getCustomers().get(b).getMoney()));
                        this.getCustomers()
                           .get(b)
                           .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                        if (Util.getInt(1, 4) == 1 && this.getCustomers().get(b).getStatus() != CustomerStatus.STRONGSTATUS) {
                           this.getCustomers().get(b).setStatus(CustomerStatus.TIRED);
                        }

                        b++;
                     }
                  case BIGBOOB:
                     message = TextUtil.t("barevent.bigboob", bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) <= Util.getInt(20, 120)
                        && !bartender2.getTraits().contains(Trait.MEATBUNS)
                        && !bartender2.getTraits().contains(Trait.LEWD)) {
                        message = message + "\n" + TextUtil.t("barevent.bigboob.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-10, this);
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     int boobPay = 0;
                     int boobPayTotal = 0;

                     for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 8, this);
                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                           customer.setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                           customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }

                        boobPay = Util.getInt(1, 10);
                        customer.payFixed(boobPay);
                        boobPayTotal += boobPay;
                     }

                     this.modifyIncome(boobPayTotal);
                     Object[] arg6 = new Object[]{boobPayTotal};
                     message = message + "\n" + TextUtil.t("barevent.bigboob.win", bartender2, arg6);
                     break label1226;
                  case SMALLBOOB:
                     message = TextUtil.t("barevent.smallboob", bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) <= Util.getInt(20, 70)
                        && !bartender2.getTraits().contains(Trait.OUTGOING)
                        && !bartender2.getTraits().contains(Trait.CROWDLOVER)) {
                        message = message + "\n" + TextUtil.t("barevent.smallboob.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.7F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-10, this);
                        }
                     }

                     this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     message = message + "\n" + TextUtil.t("barevent.smallboob.win", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) / 8, this);
                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.SAD || customer.getStatus() == CustomerStatus.PISSED)) {
                           customer.setStatus(CustomerStatus.HAPPY);
                        }

                        if (Util.getInt(1, 4) == 1 && (customer.getStatus() == CustomerStatus.LIVELY || customer.getStatus() == CustomerStatus.DRUNK)) {
                           customer.setStatus(CustomerStatus.HORNYSTATUS);
                        }
                     }
                  case BREAKSTUFF:
                     message = TextUtil.t("barevent.breakstuff", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, bartender2);
                     this.modifyIncome(-200);
                     break label1226;
                  case LOLI:
                     message = TextUtil.t("barevent.loli", bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.BARTENDING) > Util.getInt(20, 120)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.loli.win", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(5, this);
                        }
                     }

                     message = message + "\n" + TextUtil.t("barevent.loli.lose", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(-0.7F, EssentialAttributes.MOTIVATION, bartender2));
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        if (Util.getInt(0, 12) == 2) {
                           customer.addToSatisfaction(-5, this);
                        } else {
                           customer.addToSatisfaction(-15, this);
                        }
                     }
                  case TIRED:
                     this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.MOTIVATION, bartender2));
                     if (this.getCustomers().size() > 90) {
                        message = TextUtil.t("barevent.tired.one", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-10.0F, EssentialAttributes.ENERGY, bartender2));
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-5, this);
                        }
                     }

                     if (this.getCustomers().size() <= 50) {
                        break label1226;
                     }

                     this.getAttributeModifications().add(new AttributeModification(-10.5F, EssentialAttributes.ENERGY, bartender2));
                     message = TextUtil.t("barevent.tired.two", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(-10, this);
                     }
                  case SHY:
                     message = TextUtil.t("barevent.shy", bartender2);
                     if (Util.getInt(0, 10) > 5 && !bartender2.getTraits().contains(Trait.OUTGOING)) {
                        this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.shy.winwin", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(10, this);
                        }
                     }

                     if (Util.getInt(0, 10) > 5) {
                        message = message + "\n" + TextUtil.t("barevent.shy.win", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(0.5F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                        Iterator i$x = this.getCustomers().iterator();

                        while (true) {
                           if (!i$x.hasNext()) {
                              break label1226;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(5, this);
                        }
                     }

                     message = message + "\n" + TextUtil.t("barevent.shy.lose", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(-5, this);
                     }
                  case SMALLTALK:
                     this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, bartender2));
                     message = TextUtil.t("barevent.smalltalk", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(5, this);
                     }
                  case SEXSMELL:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, bartender2));
                     long servedToday = bartender2.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
                     Object[] ar = new Object[]{servedToday, Util.getInt(3, 7)};
                     message = TextUtil.t("barevent.sexsmell", bartender2) + "\n";
                     if (servedToday <= 20 + bartender2.getStamina() / 3
                        || !bartender2.getTraits().contains(Trait.SEXADDICT)
                           && !bartender2.getTraits().contains(Trait.SLUT)
                           && !bartender2.getTraits().contains(Trait.KEEPEMCOMING)
                           && !bartender2.getTraits().contains(Trait.NYMPHO)
                           && (!bartender2.getTraits().contains(Trait.FLIRTY) || Util.getInt(0, 100) >= 50)) {
                        if (servedToday <= 15 + bartender2.getStamina() / 5
                           || !bartender2.getTraits().contains(Trait.SEXADDICT)
                              && !bartender2.getTraits().contains(Trait.SLUT)
                              && !bartender2.getTraits().contains(Trait.KEEPEMCOMING)
                              && !bartender2.getTraits().contains(Trait.NYMPHO)
                              && (!bartender2.getTraits().contains(Trait.FLIRTY) || Util.getInt(0, 100) >= 50)) {
                           if (servedToday < 15 + bartender2.getStamina() / 5
                              && (
                                 bartender2.getTraits().contains(Trait.SEXADDICT)
                                    || bartender2.getTraits().contains(Trait.SLUT)
                                    || bartender2.getTraits().contains(Trait.NYMPHO)
                              )) {
                              message = message + TextUtil.t("barevent.sexsmell.wantedmore", bartender2, ar);
                              if (Util.getInt(0, 100) < 50) {
                                 message = message + "\n" + TextUtil.t("barevent.sexsmell.wantedmore.okay", bartender2, ar);
                                 image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
                                 this.getAttributeModifications().add(new AttributeModification(0.18F, Sextype.GROUP, bartender2));
                              }
                           } else if (servedToday > 7 + bartender2.getStamina()) {
                              message = message + TextUtil.t("barevent.sexsmell.waytoomuch", bartender2, ar);
                           } else {
                              message = message + TextUtil.t("barevent.sexsmell.notthatmuch", bartender2, ar);
                           }
                        } else {
                           message = message + TextUtil.t("barevent.sexsmell.coulddomore", bartender2, ar);
                           if (Util.getInt(0, 100) < 50) {
                              message = message + "\n" + TextUtil.t("barevent.sexsmell.coulddomore.okay", bartender2, ar);
                              image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                              this.getAttributeModifications().add(new AttributeModification(0.28F, Sextype.VAGINAL, bartender2));
                           }
                        }
                     } else {
                        message = message + TextUtil.t("barevent.sexsmell.satisfied", bartender2, ar);
                     }

                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(13, this);
                     }
                  case SNACKS:
                     message = TextUtil.t("barevent.snacks", bartender2);
                     this.getAttributeModifications().add(new AttributeModification(1.1F, SpecializationAttribute.COOKING, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label1226;
                        }

                        Customer customer = (Customer)i$x.next();
                        customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.COOKING) / 8, this);
                     }
                  case LOUDBUNCH:
                     int randomChat = Util.getInt(0, 9);
                     int chatBonus = 0;
                     this.getAttributeModifications().add(new AttributeModification(1.0F, SpecializationAttribute.BARTENDING, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.08F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                     message = TextUtil.t("barevent.loudbunch", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
                     if (bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) > 100 && randomChat == 1) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.SEDUCTION) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.whore", bartender2);
                     } else if (bartender2.getFinalValue(Sextype.GROUP) > 20 && randomChat == 1) {
                        chatBonus = bartender2.getFinalValue(Sextype.GROUP) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.group", bartender2);
                     } else if (bartender2.getFinalValue(Sextype.MONSTER) > 35 && randomChat == 2) {
                        chatBonus = bartender2.getFinalValue(Sextype.MONSTER) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.monster", bartender2);
                     } else if (bartender2.getFinalValue(SpecializationAttribute.STRIP) > 20 && randomChat == 3) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.STRIP) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.dance", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                     } else if (bartender2.getFinalValue(SpecializationAttribute.VETERAN) > 20 && randomChat == 4) {
                        chatBonus = bartender2.getFinalValue(SpecializationAttribute.VETERAN) / 10;
                        message = message + TextUtil.t("barevent.loudbunch.fight", bartender2);
                     } else if (bartender2.getFinalValue(BaseAttributeTypes.STRENGTH) > 25 && randomChat == 5) {
                        chatBonus = bartender2.getFinalValue(BaseAttributeTypes.STRENGTH) / 5;
                        message = message + TextUtil.t("barevent.loudbunch.strength", bartender2);
                     } else if (randomChat == 6) {
                        chatBonus = 10;
                        message = message + TextUtil.t("barevent.loudbunch.drink", bartender2);
                        this.modifyIncome(800);
                     } else {
                        chatBonus = 5;
                        message = message + TextUtil.t("barevent.loudbunch.fun", bartender2);
                     }

                     for (Customer customer : this.getCustomers()) {
                        customer.addToSatisfaction(1 + chatBonus, this);
                     }
                  default:
                     break label1226;
               }
            }
         }

         if (message != null) {
            this.getMessages().add(new MessageData(message, image, bartender2.getBackground()));
         }
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String messageText = TextUtil.t("cabaret.basic", this.getCharacters());
      messageText = messageText + "\n";
      this.messageData = new MessageData(messageText, null, this.getBackground());

      for (Charakter character : this.getCharacters()) {
         if (this.dancers.contains(character)) {
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
         } else {
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character));
         }
      }

      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();

      for (Charakter dancer : this.dancers) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, -30.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, -0.7F, EssentialAttributes.MOTIVATION));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 1.0F, SpecializationAttribute.STRIP));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 0.02F, BaseAttributeTypes.STAMINA));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, dancer, 0.02F, BaseAttributeTypes.CHARISMA));
      }

      for (Charakter bartender : this.bartenders) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, -15.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, -0.5F, EssentialAttributes.MOTIVATION));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 1.0F, SpecializationAttribute.BARTENDING));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 0.02F, BaseAttributeTypes.INTELLIGENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, bartender, 0.02F, BaseAttributeTypes.CHARISMA));
      }

      if (!this.getCharacter().getTraits().contains(Trait.LEGACYBARTENDER) && !this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3F, BaseAttributeTypes.COMMAND));
      }

      return modifications;
   }

   @Override
   public int getAppeal() {
      int appeal = 0;

      for (Charakter dancer : this.dancers) {
         appeal += (dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
      }

      for (Charakter bartender : this.bartenders) {
         appeal += (bartender.getCharisma() + bartender.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
      }

      return appeal;
   }

   @Override
   public int getMaxAttendees() {
      int amount = 0;

      for (Charakter bartender : this.getCharacters()) {
         amount += 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 10;
      }

      if (amount >= 40 * this.getCharacters().size()) {
         amount = 40 * this.getCharacters().size();
      }

      return amount;
   }
}
