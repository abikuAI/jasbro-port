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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Bartend extends RunningActivity implements BusinessSecondaryActivity {
   private int bonus;
   private Map<Charakter, Bartend.BarAction> characterAction = new HashMap<>();
   private MessageData messageData;
   private List<Charakter> bartenders = new ArrayList<>();

   @Override
   public void init() {
      this.bartenders.addAll(this.getCharacters());
   }

   @Override
   public void perform() {
      int amountEarned = 0;
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
               break;
            case SOLDIER:
               portionPay = 0.02F;
               break;
            case MERCHANT:
               portionPay = 0.015F;
               break;
            case BUSINESSMAN:
               portionPay = 0.01F;
               break;
            case MINORNOBLE:
               portionPay = 0.005F;
               break;
            case LORD:
               portionPay = 0.001F;
               break;
            case CELEBRITY:
               portionPay = 5.0E-4F;
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
      if (this.bartenders.size() < 2) {
         this.messageData.addToMessage("\n\n" + TextUtil.t("bartend.result", this.getCharacter(), this.getCustomers().size(), this.getIncome()));
      } else {
         this.messageData.addToMessage("\n\n" + TextUtil.t("bartend.result.group", this.getCustomers().size(), this.getIncome()));
      }

      Iterator i$ = this.getCharacters().iterator();

      while (true) {
         String message;
         ImageData image;
         Charakter bartender2;
         label679:
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
               this.characterAction.put(bartender2, actions.get(Util.getInt(0, actions.size())));
               message = null;
               image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter());
               int a = Util.getInt(0, this.getCustomers().size() - 1);
               Object[] arg = new Object[]{this.getCustomers().get(a).getName()};
               int rnd = 0;
               switch ((Bartend.BarAction)this.characterAction.get(bartender2)) {
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
                        break label679;
                     }

                     if (Util.getInt(0, 100) > 50) {
                        this.getAttributeModifications().add(new AttributeModification(0.05F, BaseAttributeTypes.OBEDIENCE, bartender2));
                        this.getAttributeModifications().add(new AttributeModification(0.8F, EssentialAttributes.MOTIVATION, bartender2));
                        message = message + "\n" + TextUtil.t("barevent.flip.win", bartender2);
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
                        this.getCustomers().get(Util.getInt(1, this.getCustomers().size() - 1)).addToSatisfaction(5, this);
                        break label679;
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
                     break label679;
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
                        break label679;
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
                              break label679;
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
                           break label679;
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
                              break label679;
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
                           break label679;
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
                              break label679;
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
                           break label679;
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
                        break label679;
                     }

                     message = message + "\n" + TextUtil.t("barevent.sitlap.lose", bartender2);
                     this.getAttributeModifications().add(new AttributeModification(-0.2F, EssentialAttributes.MOTIVATION, bartender2));
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                     break label679;
                  case CHAT:
                     message = TextUtil.t("barevent.chat", bartender2, arg);
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.CHARISMA, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.1F, BaseAttributeTypes.INTELLIGENCE, bartender2));
                     this.getAttributeModifications().add(new AttributeModification(0.85F, SpecializationAttribute.BARTENDING, bartender2));
                     if (Util.getInt(0, 20) <= 10) {
                        message = message + "\n" + TextUtil.t("barevent.chat.lose", bartender2);
                        this.getAttributeModifications().add(new AttributeModification(-0.1F, EssentialAttributes.MOTIVATION, bartender2));
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
                        break label679;
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
                     break label679;
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
                              break label679;
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
                           break label679;
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
                              break label679;
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
                           break label679;
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
                        break label679;
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
                     break label679;
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
                           && (Util.getInt(0, 100) < 30 || quickieAmount < 4)
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
                     break label679;
                  case DUMBFUCK:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.VAGINAL, bartender2));
                     this.getCustomers()
                        .get(Util.getInt(0, this.getCustomers().size() - 1))
                        .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                     message = TextUtil.t("barevent.dumbfuck", bartender2);
                     break label679;
                  case DUMBBJ:
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
                     this.getAttributeModifications().add(new AttributeModification(1.1F, Sextype.ORAL, bartender2));
                     this.getCustomers()
                        .get(Util.getInt(0, this.getCustomers().size() - 1))
                        .addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA) + bartender2.getFinalValue(Sextype.GROUP) / 8) / 2, this);
                     message = TextUtil.t("barevent.bj", bartender2);
                     break label679;
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
                           break label679;
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
                           break label679;
                        }

                        int z = 0;

                        while (true) {
                           if (z >= this.getCustomers().size()) {
                              break label679;
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
                        break label679;
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
                     break label679;
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
                              break label679;
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
                              break label679;
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
                                 break label679;
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
                              break label679;
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
                           break label679;
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
                              break label679;
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
                     break label679;
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
                              break label679;
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
                           break label679;
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
                     break label679;
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
                              break label679;
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
                           break label679;
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
                              break label679;
                           }

                           Customer customer = (Customer)i$x.next();
                           customer.addToSatisfaction(-5, this);
                        }
                     }

                     if (this.getCustomers().size() <= 50) {
                        break label679;
                     }

                     this.getAttributeModifications().add(new AttributeModification(-10.5F, EssentialAttributes.ENERGY, bartender2));
                     message = TextUtil.t("barevent.tired.two", bartender2);
                     image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
                     Iterator i$x = this.getCustomers().iterator();

                     while (true) {
                        if (!i$x.hasNext()) {
                           break label679;
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
                              break label679;
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
                              break label679;
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
                           break label679;
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
                           break label679;
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
                           break label679;
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
                           break label679;
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
                     break label679;
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
      Object[] arguments = new Object[]{TextUtil.listCharacters(this.bartenders)};
      String messageText = TextUtil.t("bartend.basic", arguments);
      this.messageData = new MessageData(messageText, null, this.getBackground());

      for (Charakter character : this.bartenders) {
         this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character.getImages()));
      }

      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.5F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.6F, SpecializationAttribute.BARTENDING));
      if (!Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.LEGACYBARTENDER)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.15F, BaseAttributeTypes.COMMAND));
      }

      return modifications;
   }

   @Override
   public int getAppeal() {
      int app = 0;

      for (Charakter chara : this.getCharacters()) {
         app += chara.getCharisma() + chara.getFinalValue(SpecializationAttribute.BARTENDING);
         app /= 2;
      }

      return app;
   }

   @Override
   public int getMaxAttendees() {
      int amount = 0;

      for (Charakter bartender : this.getCharacters()) {
         amount += 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 3;
      }

      return amount + this.bonus;
   }

   public int getBonus() {
      return this.bonus;
   }

   public void setBonus(int bonus) {
      this.bonus = bonus;
   }

   public enum BarAction {
      FLIP,
      GROPE,
      LOOK,
      CROWD,
      SLAP,
      DODGE,
      SITLAP,
      CHAT,
      SITGROUP,
      TEASELOT,
      BLOWJOB,
      FUCK,
      GROUP,
      DUMBBJ,
      DUMBFUCK,
      SMALLTALK,
      LOUDBUNCH,
      SEXSMELL,
      BREAKSTUFF,
      BIGBOOB,
      SMALLBOOB,
      LOLI,
      TIRED,
      SHY,
      SNACKS,
      LAGOMORPHQUICKIE,
      LAGOMORPHORGY;
   }
}
