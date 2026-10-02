package jasbro.game.character.traits;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.business.SubmitToMonster;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class TraitEffect {
   private static final float ATTRIBUTEMODIFICATOR = 0.3F;

   public void handleEvent(MyEvent e, Charakter character, Trait trait) {
   }

   public boolean removeTrait(Charakter character) {
      return true;
   }

   public boolean addTrait(Charakter character) {
      return true;
   }

   public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
      return curMinObedience;
   }

   public SkillTree getSkillTree() {
      return null;
   }

   public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
      return currentValue;
   }

   public int modifyCustomerRating(int initialRating, Customer customer, BusinessMainActivity businessMainActivity) {
      return initialRating;
   }

   public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
   }

   public float getAttributeModifier(Attribute attribute) {
      return 0.0F;
   }

   public void morphRandom(Charakter character) {
      List<String> morphType = new ArrayList<>();
      character.addTrait(Trait.BESTIALFEATURES);
      if (character.getTraits().contains(Trait.MORPHCANINE)) {
         morphType.add("CANINE");
      }

      if (character.getTraits().contains(Trait.MORPHFELINE)) {
         morphType.add("FELINE");
      }

      if (character.getTraits().contains(Trait.MORPHVULPINE)) {
         morphType.add("VULPINE");
      }

      if (character.getTraits().contains(Trait.MORPHREPTILIAN)) {
         morphType.add("REPTILIAN");
      }

      if (character.getTraits().contains(Trait.MORPHAVIAN)) {
         morphType.add("AVIAN");
      }

      if (character.getTraits().contains(Trait.MORPHAQUATIC)) {
         morphType.add("AQUATIC");
      }

      if (character.getTraits().contains(Trait.MORPHINSECT)) {
         morphType.add("INSECT");
      }

      if (character.getTraits().contains(Trait.MORPHLAGOMORPH)) {
         morphType.add("LAGOMORPH");
      }

      if (morphType.size() > 0) {
         int random = morphType.size();
         int choice = Util.getInt(0, random);
         String chosenType = morphType.get(choice);
         if (chosenType == "FELINE") {
            character.addTrait(Trait.FELINE);
         } else if (chosenType == "CANINE") {
            character.addTrait(Trait.CANINE);
         } else if (chosenType == "VULPINE") {
            character.addTrait(Trait.VULPINE);
         } else if (chosenType == "REPTILIAN") {
            character.addTrait(Trait.REPTILIAN);
         } else if (chosenType == "AVIAN") {
            character.addTrait(Trait.AVIAN);
         } else if (chosenType == "AQUATIC") {
            character.addTrait(Trait.AQUATIC);
         } else if (chosenType == "INSECT") {
            character.addTrait(Trait.INSECT);
         } else if (chosenType == "ARACHNID") {
            character.addTrait(Trait.ARACHNID);
         } else if (chosenType == "LAGOMORPH") {
            character.addTrait(Trait.LAGOMORPH);
         }

         character.removeTrait(Trait.MORPHFELINE);
         character.removeTrait(Trait.MORPHCANINE);
         character.removeTrait(Trait.MORPHVULPINE);
         character.removeTrait(Trait.MORPHREPTILIAN);
         character.removeTrait(Trait.MORPHAVIAN);
         character.removeTrait(Trait.MORPHAQUATIC);
         character.removeTrait(Trait.MORPHINSECT);
         character.removeTrait(Trait.MORPHLAGOMORPH);
      }
   }

   public static final class Absorption extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.getType() == ActivityType.ORGY
               || activity instanceof Whore && activity.getMainCustomer() != null && activity.getMainCustomer().getType() == CustomerType.GROUP) {
               MessageData message = activity.getMessages().get(0);
               message.addToMessage("\n" + TextUtil.t("ABSORPTION.perform", character));
               int amountPeople;
               if (activity.getType() == ActivityType.ORGY) {
                  amountPeople = activity.getCharacters().size();
               } else {
                  amountPeople = ((CustomerGroup)activity.getMainCustomer()).getCustomers().size();
               }

               activity.getAttributeModifications().add(new AttributeModification(2.5F * amountPeople, EssentialAttributes.ENERGY, character));
            }
         }
      }
   }

   public static class AllAttributeChangeInfluence extends TraitEffect {
      private float attributeModifier;

      public AllAttributeChangeInfluence(float attributeModifier) {
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (!(attributeModification.getAttributeType() instanceof EssentialAttributes) && attributeModification.getBaseAmount() > 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class AmbitousLover extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.VAGINAL || attributeModification.getAttributeType() == Sextype.ANAL) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.15F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class AttributeChangeInfluence extends TraitEffect {
      private AttributeType attributeType;
      private float attributeModifier;

      public AttributeChangeInfluence(AttributeType attributeType, float attributeModifier) {
         this.attributeType = attributeType;
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.attributeType) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class AttributeMaxModifier extends TraitEffect {
      private AttributeType attributeType;
      private int attributeMaxModifier;

      public AttributeMaxModifier(AttributeType attributeType, int attributeMaxModifier) {
         this.attributeType = attributeType;
         this.attributeMaxModifier = attributeMaxModifier;
      }

      @Override
      public boolean addTrait(Charakter character) {
         Attribute attribute = character.getAttribute(this.attributeType);
         attribute.setMaxValue(attribute.getMaxValue() + this.attributeMaxModifier);
         return true;
      }

      @Override
      public boolean removeTrait(Charakter character) {
         Attribute attribute = character.getAttribute(this.attributeType);
         attribute.setMaxValue(attribute.getMaxValue() - this.attributeMaxModifier);
         return true;
      }
   }

   public static class BaseAttributeChangeInfluence extends TraitEffect {
      private float attributeModifier;

      public BaseAttributeChangeInfluence(float attributeModifier) {
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() instanceof BaseAttributeTypes && attributeModification.getBaseAmount() > 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Bestial extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         character.addSpecialization(SpecializationType.FURRY);
         if (character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue() < 10.0F) {
            character.getAttribute(SpecializationAttribute.TRANSFORMATION).setInternValue(10.0F);
            character.getAttribute(SpecializationAttribute.TRANSFORMATION).setMaxValue(50);
         }
      }
   }

   public static final class BigBoobs extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;

               for (Customer customer : businessMainActivity.getMainCustomers()) {
                  customer.addToSatisfaction(3, trait);
               }
            }

            if (activity instanceof BusinessSecondaryActivity) {
               BusinessSecondaryActivity businessMainActivity = (BusinessSecondaryActivity)activity;

               for (Customer customer : businessMainActivity.getCustomers()) {
                  customer.addToSatisfaction(1, trait);
               }
            }
         }
      }
   }

   public static final class Biter extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.ORAL) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class CancelAttributeLoss extends TraitEffect {
      private AttributeType attributeType;

      public CancelAttributeLoss(AttributeType attributeType) {
         this.attributeType = attributeType;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.attributeType && attributeModification.getBaseAmount() < 0.0F) {
               attributeModification.setCancelled(true);
            }
         }
      }
   }

   public static final class DeadFish extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.VAGINAL || attributeModification.getAttributeType() == Sextype.ANAL) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.15F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Feisty extends TraitEffect {
      @Override
      public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
         return curMinObedience + 2;
      }
   }

   public static final class Fragile extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (activity instanceof SubmitToMonster) {
                  if (businessMainActivity.getMainCustomers().size() == 1) {
                     character.getAttribute(EssentialAttributes.HEALTH).addToValue(-15.0F, activity);
                     character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0F, activity);
                     activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.monster", character));
                     character.addCondition(new Buff.RoughenedUp());
                  }
               } else if (activity instanceof Whore) {
                  Whore whoreActivity = (Whore)activity;
                  ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2F);
                  int rnd = Util.getInt(0, 100);
                  if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.STRONGSTATUS && rnd < 50) {
                     activity.getMessages()
                        .get(0)
                        .addToMessage("\n" + TextUtil.t("FRAGILE.strongcustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                     character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0F, activity);
                     character.addCondition(new Buff.RoughenedUp());
                     ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2F);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY && rnd < 40) {
                     activity.getMessages()
                        .get(0)
                        .addToMessage("\n" + TextUtil.t("FRAGILE.livelycustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                     character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0F, activity);
                     ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.1F);
                  }

                  if ((whoreActivity.getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP)
                     && businessMainActivity.getMainCustomers().size() > 0) {
                     character.getAttribute(EssentialAttributes.HEALTH).addToValue(-7.0F, activity);
                     character.getAttribute(EssentialAttributes.ENERGY).addToValue(-7.0F, activity);
                     activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("FRAGILE.kinky", character));
                     character.addCondition(new Buff.RoughenedUp());
                     ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2F);
                  }
               }
            }
         }
      }
   }

   public static final class Furry extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (businessMainActivity.getMainCustomers().size() == 1) {
                  int rnd = Util.getInt(0, 100);
                  MessageData message = activity.getMessages().get(0);
                  if (rnd < 40) {
                     message.addToMessage("\n" + TextUtil.t("FURRY.like", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (rnd > 80) {
                     message.addToMessage("\n" + TextUtil.t("FURRY.dislike", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  }
               }
            }
         }

         character.addSpecialization(SpecializationType.FURRY);
      }
   }

   public static class InfluenceAttributeLoss extends TraitEffect {
      private AttributeType attributeType;
      private float attributeModifier;

      public InfluenceAttributeLoss(AttributeType attributeType, float attributeModifier) {
         this.attributeType = attributeType;
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == this.attributeType && attributeModification.getBaseAmount() < 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = modification * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Loli extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (businessMainActivity.getMainCustomers().size() == 1) {
                  int rnd = Util.getInt(0, 100);
                  MessageData message = activity.getMessages().get(0);
                  if (rnd < 30) {
                     message.addToMessage("\n" + TextUtil.t("LOLI.like", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (rnd > 70) {
                     message.addToMessage("\n" + TextUtil.t("LOLI.dislike", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  }
               }
            }
         }
      }
   }

   public static final class MorphAquatic extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHAQUATIC);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphAvian extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHAVIAN);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphCanine extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHCANINE);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphFeline extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHFELINE);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphInsect extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHINSECT);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphLagomorph extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHLAGOMORPH);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphReptilian extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHREPTILIAN);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class MorphVulpine extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (!character.getTraits().contains(Trait.FURRY) && !character.getTraits().contains(Trait.BESTIAL)) {
            character.removeTrait(Trait.MORPHVULPINE);
         } else {
            this.morphRandom(character);
         }
      }
   }

   public static final class Multifaceted extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.GROUP) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class MultipleAttributeChangeInfluence extends TraitEffect {
      private AttributeType[] attributeTypes;
      private float attributeModifier;

      public MultipleAttributeChangeInfluence(float attributeModifier, AttributeType... attributeTypes) {
         this.attributeTypes = attributeTypes;
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();

            for (AttributeType attributeType : this.attributeTypes) {
               if (attributeModification.getAttributeType() == attributeType) {
                  float modification = attributeModification.getBaseAmount();
                  float change = Math.abs(modification) * this.attributeModifier;
                  attributeModification.addModificator(change);
                  break;
               }
            }
         }
      }
   }

   public static final class Nicebody extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == SpecializationAttribute.STRIP) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Numb extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.FOREPLAY) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Nympho extends TraitEffect {
      @Override
      public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
         if (activity != null) {
            return !(activity instanceof Whore) && !(activity instanceof Sex) && !(activity instanceof Orgy) ? curMinObedience : curMinObedience - 2;
         } else {
            return curMinObedience;
         }
      }

      @Override
      public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
         return calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT ? currentValue + 10.0 : currentValue;
      }
   }

   public static final class Obedient extends TraitEffect {
      @Override
      public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
         return curMinObedience - 2;
      }
   }

   public static final class OpenMinded extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.GROUP
               || attributeModification.getAttributeType() == Sextype.MONSTER
               || attributeModification.getAttributeType() == Sextype.BONDAGE) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Reserved extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.GROUP
               || attributeModification.getAttributeType() == Sextype.MONSTER
               || attributeModification.getAttributeType() == Sextype.BONDAGE) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Sensitive extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.FOREPLAY) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class SensualTongue extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.ORAL) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static class SexAttributeChangeInfluence extends TraitEffect {
      private float attributeModifier;

      public SexAttributeChangeInfluence(float attributeModifier) {
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() instanceof Sextype && attributeModification.getBaseAmount() > 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Shy extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == SpecializationAttribute.SEDUCTION) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }

         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof Whore) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                  int rnd = Util.getInt(0, 100);
                  MessageData message = activity.getMessages().get(0);
                  if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd > 30) {
                     message.addToMessage("\n" + TextUtil.t("SHY.shycustomer.like", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd < 20) {
                     message.addToMessage("\n" + TextUtil.t("SHY.shycustomer.dislike", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.HORNYSTATUS && rnd < 80) {
                     message.addToMessage("\n" + TextUtil.t("SHY.hornycustomer.dislike", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.DRUNK && rnd < 50) {
                     message.addToMessage("\n" + TextUtil.t("SHY.drunkcustomer", character, businessMainActivity.getMainCustomers().get(0)));
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.PISSED && rnd < 50) {
                     message.addToMessage("\n" + TextUtil.t("SHY.pissedcustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (rnd < 15) {
                     message.addToMessage("\n" + TextUtil.t("SHY.like", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (rnd > 85) {
                     message.addToMessage("\n" + TextUtil.t("SHY.dislike", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  }
               }
            }
         }
      }
   }

   public static final class SingleMinded extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.GROUP) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Slut extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (activity instanceof Whore) {
                  if (activity.getMainCustomer().getType() == CustomerType.GROUP
                     || activity.getMainCustomer().getType() == CustomerType.CELEBRITY
                     || activity.getMainCustomer().getType() == CustomerType.LORD
                     || activity.getMainCustomer().getType() == CustomerType.MINORNOBLE
                     || activity.getMainCustomer().getType() == CustomerType.BUSINESSMAN) {
                     ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() + 0.2F);
                     ((Whore)activity).setExecutionModifier(((Whore)activity).getExecutionModifier() + 0.1F);
                     MessageData message = activity.getMessages().get(0);
                     message.addToMessage("\n" + TextUtil.t("SLUT.rich", character, activity.getMainCustomer()));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(10, trait);
                  } else if (activity.getMainCustomer().getType() == CustomerType.BUM || activity.getMainCustomer().getType() == CustomerType.PEASANT) {
                     MessageData message = activity.getMessages().get(0);
                     ((Whore)activity).setCooldownModifier(((Whore)activity).getCooldownModifier() - 0.2F);
                     ((Whore)activity).setExecutionModifier(((Whore)activity).getExecutionModifier() - 0.1F);
                     message.addToMessage("\n" + TextUtil.t("SLUT.poor", character, activity.getMainCustomer()));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-20, trait);
                  }
               }
            } else if (activity instanceof Strip && activity.getCustomers().size() >= 20) {
               activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("SLUT.strip", character));

               for (Customer customer : activity.getCustomers()) {
                  customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 15, activity);
               }
            }
         }
      }
   }

   public static final class SmallBoobs extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                  int rnd = Util.getInt(0, 100);
                  MessageData message = activity.getMessages().get(0);
                  if (rnd < 20) {
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(3, trait);
                  } else if (rnd > 70) {
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-3, trait);
                  }
               }
            }
         }
      }
   }

   public static class SpecializationAttributeChangeInfluence extends TraitEffect {
      private float attributeModifier;

      public SpecializationAttributeChangeInfluence(float attributeModifier) {
         this.attributeModifier = attributeModifier;
      }

      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() instanceof SpecializationAttribute && attributeModification.getBaseAmount() > 0.0F) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * this.attributeModifier;
               attributeModification.addModificator(change);
            }
         }
      }
   }

   public static final class Tsundere extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity.isAbort()) {
               return;
            }

            if (activity instanceof Whore && activity.getType() != ActivityType.TEASE) {
               if (activity instanceof BusinessMainActivity) {
                  BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
                  if (businessMainActivity.getMainCustomers().size() > 0) {
                     int rnd = Util.getInt(0, 100);
                     int chance = 25 - character.getObedience() * 2;
                     if (businessMainActivity instanceof Whore) {
                        chance += 10;
                     }

                     if (chance < 1) {
                        chance = 1;
                     }

                     if (rnd < chance) {
                        MessageData message = activity.getMessages().get(0);
                        message.addToMessage(TextUtil.t("TSUNDERE.hit", character, businessMainActivity.getMainCustomers().get(0)));
                        businessMainActivity.getMainCustomers().get(0).addToSatisfaction((int)(-character.getDamage() * 10.0F * 2.0F), trait);
                        activity.getAttributeModifications().add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                        int chanceKo = (int)(character.getDamage() * 10.0F);
                        rnd = Util.getInt(0, 100);
                        if (rnd < chanceKo) {
                           activity.setAbort(true);
                           List<AttributeModification> attributeModifications = new ArrayList<>();
                           attributeModifications.add(new AttributeModification(-20.0F, EssentialAttributes.ENERGY, character));
                           attributeModifications.add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                           message.addToMessage(TextUtil.t("TSUNDERE.hitKO", character, businessMainActivity.getMainCustomers().get(0)));
                           message.setBackground(character.getBackground());
                           message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                           message.setAttributeModifications(attributeModifications);
                           message.createMessageScreen();
                        } else {
                           rnd = Util.getInt(0, 100);
                           int chanceAbort = -businessMainActivity.getMainCustomers().get(0).getSatisfactionAmount()
                              - (int)(character.getDamage() * 4.0F)
                              - character.getCharisma() / 2;
                           if (rnd < chanceAbort) {
                              activity.setAbort(true);
                              List<AttributeModification> attributeModifications = new ArrayList<>();
                              attributeModifications.add(new AttributeModification(-20.0F, EssentialAttributes.ENERGY, character));
                              attributeModifications.add(new AttributeModification(-10.0F, EssentialAttributes.HEALTH, character));
                              attributeModifications.add(new AttributeModification(-0.05F, BaseAttributeTypes.OBEDIENCE, character));
                              message.addToMessage(TextUtil.t("TSUNDERE.hitBack", character, businessMainActivity.getMainCustomers().get(0)));
                              message.setBackground(character.getBackground());
                              message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
                              message.setAttributeModifications(attributeModifications);
                              message.createMessageScreen();
                           } else {
                              message.addToMessage(TextUtil.t("TSUNDERE.continue", character, businessMainActivity.getMainCustomers().get(0)));
                           }
                        }
                     }
                  }
               } else if (activity instanceof BusinessSecondaryActivity) {
               }
            }
         }
      }
   }

   public static final class Uninhibited extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == SpecializationAttribute.SEDUCTION) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         }

         if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity && activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (!(activity instanceof SubmitToMonster) && businessMainActivity.getMainCustomers().size() == 1) {
                  int rnd = Util.getInt(0, 100);
                  MessageData message = activity.getMessages().get(0);
                  if ((
                        businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY
                           || businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.HORNYSTATUS
                           || businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.VERYHORNY
                     )
                     && rnd > 30) {
                     message.addToMessage("\n" + TextUtil.t("UNINHIBITED.hot.like", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.SHYSTATUS && rnd < 20) {
                     message.addToMessage("\n" + TextUtil.t("UNINHIBITED.shycustomer.dislike", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-10, trait);
                  } else if (rnd < 15) {
                     message.addToMessage("\n" + TextUtil.t("UNINHIBITED.like", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  } else if (rnd > 85) {
                     message.addToMessage("\n" + TextUtil.t("UNINHIBITED.dislike", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(-5, trait);
                  }
               }
            }
         }
      }
   }

   public static final class Unsellable extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (character.getTraits().contains(Trait.UNSELLABLE)) {
            character.removeTrait(Trait.UNSELLABLE);
         }
      }
   }

   public static final class Wild extends TraitEffect {
      @Override
      public void handleEvent(MyEvent e, Charakter character, Trait trait) {
         if (e.getType() == EventType.ATTRIBUTECHANGE) {
            AttributeModification attributeModification = (AttributeModification)e.getSource();
            if (attributeModification.getAttributeType() == Sextype.MONSTER
               || attributeModification.getAttributeType() == BaseAttributeTypes.STAMINA
               || attributeModification.getAttributeType() == BaseAttributeTypes.STRENGTH) {
               float modification = attributeModification.getBaseAmount();
               float change = Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }

            if (attributeModification.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
               float modification = attributeModification.getBaseAmount();
               float change = -Math.abs(modification) * 0.3F;
               attributeModification.addModificator(change);
            }
         } else if (e.getType() == EventType.ACTIVITY) {
            RunningActivity activity = (RunningActivity)e.getSource();
            if (activity instanceof BusinessMainActivity) {
               BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
               if (activity instanceof SubmitToMonster) {
                  if (businessMainActivity.getMainCustomers().size() == 1) {
                     MessageData message = activity.getMessages().get(0);
                     message.addToMessage("\n" + TextUtil.t("WILD.like", character));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(10, trait);

                     for (AttributeModification attributeModification : activity.getAttributeModifications()) {
                        if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
                           float modification = attributeModification.getBaseAmount();
                           float change = Math.abs(modification) * 0.2F;
                           attributeModification.addModificator(change);
                        }

                        if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
                           float modification = attributeModification.getBaseAmount();
                           float change = Math.abs(modification) * 0.1F;
                           attributeModification.addModificator(change);
                        }
                     }
                  }
               } else if (activity instanceof Whore) {
                  Whore whoreActivity = (Whore)activity;
                  whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.2F);
                  int rnd = Util.getInt(0, 100);
                  if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.STRONGSTATUS
                     && rnd < 40
                     && whoreActivity.getSextype() != Sextype.ORAL
                     && whoreActivity.getSextype() != Sextype.FOREPLAY
                     && whoreActivity.getSextype() != Sextype.TITFUCK) {
                     whoreActivity.getMessages()
                        .get(0)
                        .addToMessage("\n" + TextUtil.t("WILD.strongcustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(7, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.LIVELY && rnd < 40) {
                     whoreActivity.getMessages()
                        .get(0)
                        .addToMessage("\n" + TextUtil.t("WILD.livelycustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(7, trait);
                  } else if (businessMainActivity.getMainCustomers().get(0).getStatus() == CustomerStatus.TIRED && rnd < 40) {
                     whoreActivity.getMessages()
                        .get(0)
                        .addToMessage("\n" + TextUtil.t("WILD.tiredcustomer", character, businessMainActivity.getMainCustomers().get(0)));
                     businessMainActivity.getMainCustomers().get(0).addToSatisfaction(15, trait);
                     whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier() - 0.1F);
                  }
               }
            }
         }
      }
   }
}
