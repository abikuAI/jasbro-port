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
import java.util.List;
import java.util.Map;

public class Strip extends RunningActivity implements BusinessSecondaryActivity {
   private MessageData messageData;
   private int bonus;
   private Map<Charakter, Strip.StripAction> characterAction = new HashMap<>();

   @Override
   public void perform() {
      Charakter character = this.getCharacter();
      int skill = character.getCharisma() / 5 + character.getFinalValue(SpecializationAttribute.STRIP) / 5 + 1;
      int amountEarned = 0;
      int amountHappy = 0;
      int overalltips = 0;
      int tip = 0;
      int chanceOfTip = 25 + character.getCharisma() + character.getFinalValue(SpecializationAttribute.STRIP);
      float chanceModifier = 1.0F;

      for (Customer customer : this.getCustomers()) {
         switch (customer.getType()) {
            case PEASANT:
               chanceModifier = 1.7F;
               break;
            case SOLDIER:
               chanceModifier = 1.2F;
               break;
            case MERCHANT:
               chanceModifier = 1.0F;
               break;
            case BUSINESSMAN:
               chanceModifier = 0.8F;
               break;
            case MINORNOBLE:
               chanceModifier = 0.6F;
               break;
            case LORD:
               chanceModifier = 0.4F;
               break;
            case CELEBRITY:
               chanceModifier = 0.2F;
               break;
            default:
               chanceModifier = 2.0F;
         }

         if (Util.getInt(0, 100) < chanceModifier * chanceOfTip) {
            tip = customer.getMoney() * Util.getInt(6, 12) / 100;
            tip = customer.pay(tip, this.getCharacter().getMoneyModifier());
            amountHappy++;
            customer.addToSatisfaction(skill, this);
            overalltips += tip;
            amountEarned += tip;
         } else {
            customer.addToSatisfaction(skill / 4, this);
         }
      }

      this.modifyIncome(amountEarned);
      if (amountEarned > 0) {
         this.messageData
            .addToMessage(
               "\n\n" + TextUtil.t("strip.result.owned", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips)
            );
      } else {
         this.messageData
            .addToMessage(
               "\n\n" + TextUtil.t("strip.result.basic", this.getCharacter(), this.getCustomers().size(), amountHappy, this.getIncome(), overalltips)
            );
      }

      List<Strip.StripAction> actions = new ArrayList<>();
      if (character.getTraits().contains(Trait.EXTRAS)) {
         actions.add(Strip.StripAction.EXTRAS);
         actions.add(Strip.StripAction.EXTRAS);
         actions.add(Strip.StripAction.EXTRAS);
         actions.add(Strip.StripAction.EXTRAS);
      }

      if (character.getTraits().contains(Trait.SHY)) {
         actions.add(Strip.StripAction.SHY);
      }

      if (character.getTraits().contains(Trait.UNINHIBITED)) {
         actions.add(Strip.StripAction.UNHINIBITED);
         actions.add(Strip.StripAction.LICKPOLE);
      }

      if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 7L) {
         actions.add(Strip.StripAction.SEXSMELL);
      }

      if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
         actions.add(Strip.StripAction.SEXSMELL);
      }

      if (character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString()) > 14L) {
         actions.add(Strip.StripAction.SEXSMELL);
      }

      if (character.getTraits().contains(Trait.STIFF)) {
         actions.add(Strip.StripAction.STIFF);
      }

      if (character.getTraits().contains(Trait.HORNY)) {
         actions.add(Strip.StripAction.RUBCLIT);
      }

      if (character.getTraits().contains(Trait.FIT)) {
         actions.add(Strip.StripAction.FIT);
      }

      if (character.getTraits().contains(Trait.FRAGILE)) {
         actions.add(Strip.StripAction.FRAGILE);
      }

      if (character.getTraits().contains(Trait.OILY)) {
         actions.add(Strip.StripAction.OILY);
      }

      if (character.getTraits().contains(Trait.SUBMISSIVE)) {
         actions.add(Strip.StripAction.SUBMISSIVE);
      }

      if (character.getTraits().contains(Trait.CUMSLUT)) {
         actions.add(Strip.StripAction.CUMDRINK);
      }

      if (character.getTraits().contains(Trait.AFLEURDEPEAU)) {
         actions.add(Strip.StripAction.FONDLE);
      }

      if (character.getTraits().contains(Trait.FELINEHEAT) && Jasbro.getInstance().getData().getDay() % 15 == 0) {
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
         actions.add(Strip.StripAction.FELINEORGY);
      }

      if (character.getFinalValue(SpecializationAttribute.COOKING) > 255) {
         actions.add(Strip.StripAction.CREAM);
      }

      if (Util.getInt(0, 100) < 10 + actions.size() * 6 && this.getCustomers().size() > 10 && actions.size() > 0) {
         this.characterAction.put(character, actions.get(Util.getInt(0, actions.size())));
         String message = null;
         ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
         int a = Util.getInt(0, this.getCustomers().size() - 1);
         Object[] arg = new Object[]{this.getCustomers().get(a).getName()};
         int rnd = 0;
         switch ((Strip.StripAction)this.characterAction.get(character)) {
            case EXTRAS:
               if (this.getCustomers().get(a).getType() == CustomerType.BUM) {
                  break;
               }

               character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
               int extra = this.getCustomers().get(a).getMoney() * character.getFinalValue(SpecializationAttribute.STRIP) / 300;
               this.modifyIncome(extra);
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
               message = TextUtil.t("STRIP.extras", character, this.getCustomers().get(a).getStatusName(), this.getCustomers().get(a).getName(), extra);
               if (this.getCustomers().get(a).getPreferredSextype() == Sextype.VAGINAL && character.getAllowedServices().isAllowed(Sextype.VAGINAL)) {
                  if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                     if (Util.getInt(0, 10) > 5
                        && (
                           character.getTraits().contains(Trait.NYMPHO)
                              || character.getTraits().contains(Trait.SLUT)
                              || character.getTraits().contains(Trait.HORNY)
                        )) {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.VAGINAL, character));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.two", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 5, this);
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

                        character.getFame().modifyFame(50.0);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 10, this);
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
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.VAGINAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.male.one", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
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

                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 7, this);
                     }
                  }

                  if (this.getCustomers().get(a).getGender() != Gender.FEMALE && this.getCustomers().get(a).getGender() != Gender.FUTA) {
                     break;
                  }

                  if (Util.getInt(0, 10) <= 5
                     || !character.getTraits().contains(Trait.NYMPHO)
                        && !character.getTraits().contains(Trait.KINKY)
                        && !character.getTraits().contains(Trait.SENSITIVE)) {
                     this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.VAGINAL, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.one", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
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
                     this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.VAGINAL, character));
                     this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.vaginal.female.two", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                     character.getFame().modifyFame(50.0);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
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
               } else if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ANAL && character.getAllowedServices().isAllowed(Sextype.ANAL)) {
                  if (this.getCustomers().get(a).getGender() == Gender.MALE && Util.getInt(0, 10) > 5) {
                     if (Util.getInt(0, 10) <= 5
                        || !character.getTraits().contains(Trait.ROWDYRUMP)
                           && !character.getTraits().contains(Trait.AMBITOUSLOVER)
                           && !character.getTraits().contains(Trait.HORNY)) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.one", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
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
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);
                        }
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, character));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.anal.male.two", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 5, this);
                        character.getFame().modifyFame(50.0);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 10, this);
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
                     int analBead = 2 + character.getFinalValue(Sextype.ANAL) / 10;
                     if (character.getTraits().contains(Trait.DEEPLOVE)) {
                        analBead = (int)(analBead * 1.5);
                     }

                     Object[] arg2 = new Object[]{analBead, this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getName()};
                     if (Util.getInt(0, 10) <= 5
                        || !character.getTraits().contains(Trait.KINKY)
                           && !character.getTraits().contains(Trait.SUBMISSIVE)
                           && !character.getTraits().contains(Trait.UNINHIBITED)) {
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ANAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.one", character, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 7, this);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 12, this);
                        }
                     } else {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ANAL, character));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.anal.female.two", character, arg2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                        character.getFame().modifyFame(50.0);

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(analBead + character.getFinalValue(Sextype.ANAL) / 12, this);
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
                  }
               } else if (this.getCustomers().get(a).getPreferredSextype() == Sextype.ORAL && character.getAllowedServices().isAllowed(Sextype.ORAL)) {
                  if (this.getCustomers().get(a).getGender() == Gender.MALE) {
                     if (Util.getInt(0, 10) > 5
                        && (
                           character.getTraits().contains(Trait.CUMSLUT)
                              || character.getTraits().contains(Trait.SENSUALTONGUE)
                              || character.getTraits().contains(Trait.ABSORPTION)
                        )) {
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ORAL, character));
                        this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.two", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 5, this);
                        character.getFame().modifyFame(50.0);
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
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 10, this);
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
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ORAL, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.oral.male.one", character);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 7, this);
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
                     || !character.getTraits().contains(Trait.SENSUALTONGUE)
                        && !character.getTraits().contains(Trait.OPENMINDED)
                        && !character.getTraits().contains(Trait.HORNY)) {
                     this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.ORAL, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.one", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 7, this);
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
                     this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.ORAL, character));
                     this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.oral.female.two", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                     character.getFame().modifyFame(50.0);
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
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
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
                  || !character.getAllowedServices().isAllowed(Sextype.TITFUCK)
                  || character.getGender() != Gender.FEMALE
                  || !character.getTraits().contains(Trait.SMALLBOOBS) && !character.getTraits().contains(Trait.BIGBOOBS)) {
                  if (Util.getInt(0, 10) <= 4 && this.getCustomers().size() >= 8) {
                     if (Util.getInt(0, 10) <= 5
                        || !character.getTraits().contains(Trait.GANGBANGQUEEN)
                           && !character.getTraits().contains(Trait.INSATIABLE)
                           && !character.getTraits().contains(Trait.MULTIFACETED)) {
                        character.getCounter()
                           .add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long)Util.getInt(1, 2 + this.getCustomers().size() / 4));
                        this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.GROUP, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.one", character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                        character.getFame().modifyFame(100.0);

                        for (int cust = 0; cust < 8; cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.GROUP) / 7, this);
                        }

                        for (int cust = 8; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(-15, this);
                        }
                     } else {
                        character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), this.getCustomers().size() - 1L);
                        this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.GROUP, character));
                        this.getAttributeModifications().add(new AttributeModification(2.0F, EssentialAttributes.MOTIVATION, character));
                        message = message + "\n" + TextUtil.t("STRIP.extras.group.two", character, this.getCustomers().get(a));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
                        this.getHouse().modDirt(70);
                        character.getFame().modifyFame(450.0);
                        character.addCondition(new Buff.RoughenedUp());

                        for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                           this.getCustomers().get(cust).addToSatisfaction(10 + character.getFinalValue(Sextype.GROUP) / 5, this);
                           if (Util.getInt(1, 3) == 1 && this.getCustomers().get(cust).getStatus() != CustomerStatus.STRONGSTATUS) {
                              this.getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
                           }
                        }
                     }
                  } else if (Util.getInt(0, 10) <= 5
                     || !character.getTraits().contains(Trait.SENSITIVE)
                        && !character.getTraits().contains(Trait.NICEBODY)
                        && !character.getTraits().contains(Trait.TOUCHYFEELY)) {
                     this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.FOREPLAY, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.one", character, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
                     character.getFame().modifyFame(20.0);
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
                           .addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY) + character.getFinalValue(SpecializationAttribute.STRIP)) / 15, this);
                     }
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.FOREPLAY, character));
                     this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.lapdance.two", character, this.getCustomers().get(a));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
                     character.getFame().modifyFame(50.0);
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
                           .addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY) + character.getFinalValue(SpecializationAttribute.STRIP)) / 10, this);
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
                     if (character.getTraits().contains(Trait.SMALLBOOBS)) {
                        if (Util.getInt(0, 10) > 5
                           && (
                              character.getTraits().contains(Trait.CUMSLUT)
                                 || character.getTraits().contains(Trait.WILD)
                                 || character.getTraits().contains(Trait.OILY)
                           )) {
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.TITFUCK, character));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                           message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", character);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                           this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 5, this);
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
                              this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 10, this);
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

                           character.getFame().modifyFame(50.0);
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, character));
                           message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", character);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                           this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
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
                     } else if (character.getTraits().contains(Trait.BIGBOOBS)) {
                        if (Util.getInt(0, 10) > 5
                           && (
                              character.getTraits().contains(Trait.WENCH)
                                 || character.getTraits().contains(Trait.WENCH)
                                 || character.getTraits().contains(Trait.COMETOMOMMY)
                           )) {
                           this.getAttributeModifications().add(new AttributeModification(2.5F, Sextype.TITFUCK, character));
                           this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                           message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", character);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                           this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 5, this);
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
                              this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 10, this);
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

                           character.getFame().modifyFame(50.0);
                        } else {
                           this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, character));
                           message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", character);
                           image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
                           this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
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
                        character.getTraits().contains(Trait.EXHIBITIONIST)
                           || character.getTraits().contains(Trait.UNINHIBITED)
                           || character.getTraits().contains(Trait.TOUCHYFEELY)
                     )) {
                     this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, character));
                     this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.two", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 10, this);
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

                     character.getFame().modifyFame(50.0);
                  } else {
                     this.getAttributeModifications().add(new AttributeModification(1.5F, Sextype.TITFUCK, character));
                     message = message + "\n" + TextUtil.t("STRIP.extras.titfuck.female.one", character);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);

                     for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                        this.getCustomers().get(cust).addToSatisfaction(10 + character.getFinalValue(Sextype.FOREPLAY), this);
                     }
                  }
               }
               break;
            case SHY:
               if (character.getTraits().contains(Trait.PURE)) {
                  this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.shy.win", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(10, this);
                  }
               } else {
                  this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.shy.lose", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(-10, this);
                  }
               }
               break;
            case UNHINIBITED:
               if (!character.getTraits().contains(Trait.LEWD) && Util.getInt(0, 3) != 2) {
                  message = TextUtil.t("STRIP.event.lewd.lose", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 5), this);
                  }
               } else {
                  this.getAttributeModifications().add(new AttributeModification(0.1F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.lewd.win", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(10, this);
                  }
               }
               break;
            case FRAGILE:
               if (character.getTraits().contains(Trait.LASCIVIOUS)) {
                  this.getAttributeModifications().add(new AttributeModification(0.1F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.fragile.win", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(7, this);
                  }
               } else {
                  this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.fragile.lose", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(-15, this);
                  }
               }
               break;
            case FIT:
               if (character.getTraits().contains(Trait.PERFECTCONDITION) && Util.getInt(0, 3) == 1) {
                  this.getAttributeModifications().add(new AttributeModification(0.2F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.fit.win", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(skill / 5, this);
                  }
               } else {
                  message = TextUtil.t("STRIP.event.fit.winwin", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

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
               message = TextUtil.t("STRIP.event.stiff", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(-skill / 2, this);
               }
               break;
            case BIGTITS:
               message = TextUtil.t("STRIP.event.bigtits", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK) / 7, this);
               }
               break;
            case CLUMSY:
               if (Util.getInt(0, 3) == 2) {
                  this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.clumsy.win", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 7), this);
                  }
               } else {
                  this.getAttributeModifications().add(new AttributeModification(-0.3F, EssentialAttributes.MOTIVATION, character));
                  message = TextUtil.t("STRIP.event.clumsy.lose", character, this.getCustomers().get(a));
                  image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

                  for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                     this.getCustomers().get(cust).addToSatisfaction(-15, this);
                  }
               }
               break;
            case RUBCLIT:
               message = TextUtil.t("STRIP.event.rubclit", character, this.getCustomers().get(a));
               this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(5 + character.getFinalValue(Sextype.FOREPLAY) / 10, this);
               }
               break;
            case LICKPOLE:
               message = TextUtil.t("STRIP.event.lickpole", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(5 + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 10, this);
               }
               break;
            case OILY:
               message = TextUtil.t("STRIP.event.oily", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(5 + character.getCharisma() / 5, this);
               }
               break;
            case FELINEORGY:
               message = TextUtil.t("STRIP.event.felineorgy", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(character.getCharisma() / 5, this);
                  this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL) / 5, this);
                  this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL) / 5, this);
                  this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL) / 5, this);
                  this.getAttributeModifications().add(new AttributeModification(-0.5F, EssentialAttributes.ENERGY, character));
                  this.getAttributeModifications().add(new AttributeModification(0.5F, Sextype.GROUP, character));
                  if (Util.getInt(0, 100) < this.getCustomers().size()) {
                     character.addCondition(new Buff.Exhausted());
                  }

                  character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
               }
               break;
            case CUMDRINK:
               message = TextUtil.t("STRIP.event.cumdrink", character, this.getCustomers().get(a));
               this.getAttributeModifications().add(new AttributeModification(1.0F, EssentialAttributes.MOTIVATION, character));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character);
               character.getFame().modifyFame(250.0);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers()
                     .get(cust)
                     .addToSatisfaction(10 + (character.getFinalValue(Sextype.ORAL) + character.getFinalValue(Sextype.FOREPLAY)) / 12, this);
               }
               break;
            case SEXSMELL:
               if (Util.getInt(0, 2) == 0) {
                  message = TextUtil.t("STRIP.event.sexsmell.smell", character, this.getCustomers().get(a));
               } else {
                  message = TextUtil.t("STRIP.event.sexsmell.move", character, this.getCustomers().get(a));
               }

               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
               character.getFame().modifyFame(150.0);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(15, this);
               }
               break;
            case FONDLE:
               rnd = Util.getInt(0, 3);
               if (character.getTraits().contains(Trait.BIGBOOBS) && rnd == 0) {
                  message = TextUtil.t("STRIP.event.fondle.bigbreasts", character, this.getCustomers().get(a));
               }

               if (character.getTraits().contains(Trait.SMALLBOOBS) && rnd == 1) {
                  message = TextUtil.t("STRIP.event.fondle.smallbreasts", character, this.getCustomers().get(a));
               } else {
                  message = TextUtil.t("STRIP.event.fondle.butt", character, this.getCustomers().get(a));
               }

               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  if (this.getCustomers().get(cust).getType() == CustomerType.MINORNOBLE
                     || this.getCustomers().get(cust).getType() == CustomerType.BUSINESSMAN
                     || this.getCustomers().get(cust).getType() == CustomerType.LORD
                     || this.getCustomers().get(cust).getType() == CustomerType.CELEBRITY) {
                     this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 5, this);
                  }
               }
               break;
            case CREAM:
               message = TextUtil.t("STRIP.event.cream", character, this.getCustomers().get(a));
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);

               for (int cust = 0; cust < this.getCustomers().size(); cust++) {
                  this.getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
               }
               break;
            case SUBMISSIVE:
               message = TextUtil.t("STRIP.event.submissive", character, arg) + "\n";
               character.getCounter().add(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString(), 1L);
               rnd = Util.getInt(0, 3);
               switch (rnd) {
                  case 0:
                     if (character.getGender() != Gender.MALE) {
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
                        message = message + TextUtil.t("STRIP.event.submissive.vaginal", character, arg);
                        this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                     }
                     break;
                  case 1:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
                     message = message + TextUtil.t("STRIP.event.submissive.anal", character, arg);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
                     break;
                  default:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
                     message = message + TextUtil.t("STRIP.event.submissive.oral", character, arg);
                     this.getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY) / 7, this);
               }
         }

         if (message != null) {
            this.getMessages().add(new MessageData(message, image, character.getBackground()));
         }
      }
   }

   @Override
   public MessageData getBaseMessage() {
      String messageText = TextUtil.t("strip.basic", this.getCharacter());
      this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, this.getCharacter()), this.getBackground());
      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -30.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.7F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, SpecializationAttribute.STRIP));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.02F, BaseAttributeTypes.STRENGTH));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1F, BaseAttributeTypes.STAMINA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.05F, BaseAttributeTypes.CHARISMA));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.02F, BaseAttributeTypes.OBEDIENCE));
      if (!this.getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.3F, BaseAttributeTypes.COMMAND));
      }

      return modifications;
   }

   @Override
   public int getAppeal() {
      return this.getCharacter().getCharisma() + this.getCharacter().getFinalValue(SpecializationAttribute.STRIP);
   }

   @Override
   public int getMaxAttendees() {
      return 20 + this.bonus;
   }

   public int getBonus() {
      return this.bonus;
   }

   public void setBonus(int bonus) {
      this.bonus = bonus;
   }

   public enum StripAction {
      EXTRAS,
      SHY,
      FRAGILE,
      STIFF,
      CLUMSY,
      SUBMISSIVE,
      UNHINIBITED,
      FIT,
      WILD,
      OILY,
      BIGTITS,
      RUBCLIT,
      LICKPOLE,
      CUMDRINK,
      FONDLE,
      CREAM,
      SEXSMELL,
      FELINEORGY;
   }
}
