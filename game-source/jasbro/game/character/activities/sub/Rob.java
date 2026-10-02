package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.items.Inventory;
import jasbro.game.items.ItemType;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rob extends RunningActivity {
   private MessageData messageData;
   private Map<Charakter, Rob.EscapeRoutes> characterAction = new HashMap<>();

   @Override
   public void perform() {
      Charakter character = this.getCharacter();
      List<Rob.EscapeRoutes> actions = new ArrayList<>();
      int stealChance = 25 + character.getStealChance();
      int stealAmount = 15 + character.getStealAmountModifier();
      int stealItemChance = 10 + character.getStealItemChance();
      int targetLevel = character.getFinalValue(SpecializationAttribute.PICKPOCKETING)
         + character.getFinalValue(SpecializationAttribute.AGILITY)
         + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)
         + Util.getInt(-20, 20);
      targetLevel *= Util.getInt(40, 110) / 100;
      int failureChance = (int)(
         100.0 - character.getFinalValue(SpecializationAttribute.AGILITY) / 1.5 - character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 2
      );

      for (Condition condition : character.getConditions()) {
         if (condition instanceof Buff.Watched) {
            failureChance *= 1;
         }
      }

      Customer target = null;
      if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
         failureChance /= 4;
         target = this.generateTarget(targetLevel);
         Object[] argument = new Object[]{target.getName()};
         this.messageData.addToMessage("\n" + TextUtil.t("rob.target.day", character, target, argument));
      } else {
         stealChance *= 1;
         stealAmount *= 1;
         stealItemChance *= 1;
         if (character.getTraits().contains(Trait.PHANTOMTHIEF)) {
            stealChance *= 2;
            stealAmount *= 2;
            stealItemChance *= 2;
            targetLevel *= 2;
         }

         target = this.generateTarget(targetLevel + 40);
         Object[] argument = new Object[]{target.getName()};
         this.messageData.addToMessage("\n" + TextUtil.t("rob.target.night", character, target, argument));
      }

      target.setMaxHitpoints(target.getMaxHitpoints() + targetLevel);
      target.setHitpoints(target.getMaxHitpoints());
      if (target != null) {
         failureChance += target.getInitialSatisfaction() / 5;
         if (Util.getInt(0, 100) < failureChance) {
            if (Util.getInt(0, 100) < 10) {
               character.addCondition(new Buff.Watched(character.getFinalValue(SpecializationAttribute.PICKPOCKETING)));
            }

            this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed", character, target));
            if (character.getTraits().contains(Trait.CLEVER)) {
               actions.add(Rob.EscapeRoutes.RUN);
            }

            if (character.getTraits().contains(Trait.FIT)) {
               actions.add(Rob.EscapeRoutes.RUN);
            }

            if (character.getTraits().contains(Trait.STUPID)) {
               actions.add(Rob.EscapeRoutes.CAUGHTANDFIGHT);
            }

            actions.add(Rob.EscapeRoutes.CAUGHTANDFIGHT);
            actions.add(Rob.EscapeRoutes.RUN);
            actions.add(Rob.EscapeRoutes.RUN);
            if (target.getType() != CustomerType.BUM) {
               actions.add(Rob.EscapeRoutes.CALLGUARDS);
            }

            if (target.getType() != CustomerType.BUM) {
               actions.add(Rob.EscapeRoutes.CALLGUARDS);
            }

            if (character.getTraits().contains(Trait.NYMPHO) && character.getGender() != target.getGender()) {
               actions.add(Rob.EscapeRoutes.SEDUCEANDROB);
               actions.add(Rob.EscapeRoutes.SEDUCEANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDROB);
            }

            if (character.getTraits().contains(Trait.SLUT) && character.getGender() != target.getGender()) {
               actions.add(Rob.EscapeRoutes.SEDUCEANDROB);
               actions.add(Rob.EscapeRoutes.SEDUCEANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDROB);
            }

            if (character.getTraits().contains(Trait.LIAISONSDANGEREUSES) && character.getGender() != target.getGender()) {
               actions.add(Rob.EscapeRoutes.SEDUCEANDROB);
               actions.add(Rob.EscapeRoutes.SEDUCEANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDROB);
               actions.add(Rob.EscapeRoutes.SEDUCEANDROB);
               actions.add(Rob.EscapeRoutes.SEDUCEANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDFLEE);
               actions.add(Rob.EscapeRoutes.FUCKANDROB);
            }

            this.characterAction.put(character, actions.get(Util.getInt(0, actions.size())));
         } else {
            this.characterAction.put(character, Rob.EscapeRoutes.STEAL);
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.CAUGHTANDFIGHT) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed.fight", character, target));
            this.getAttributeModifications().add(new AttributeModification(1.07F, SpecializationAttribute.VETERAN, character));
            Unit fighter1 = character;
            Unit fighter2 = target;
            int i = 0;
            int startHitPoints1 = fighter1.getHitpoints();
            int startHitPoints2 = fighter2.getHitpoints();
            Battle battle = new Battle(fighter1, fighter2);

            do {
               battle.doRound();
               i++;
            } while (i < 1000 && fighter1.getHitpoints() > startHitPoints1 - 30 && fighter2.getHitpoints() > 10);

            this.getMessages().get(0).addToMessage("\n\n" + battle.getCombatText());
            if (character.getFinalValue(SpecializationAttribute.MAGIC) > 10) {
               this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.MAGIC, character));
            }

            if (fighter1.getHitpoints() / startHitPoints1 > fighter2.getHitpoints() / startHitPoints2) {
               this.messageData.addToMessage(TextUtil.t("rob.fight.run", character, target));
               if (Util.getInt(0, 100) < 50) {
                  this.characterAction.put(character, Rob.EscapeRoutes.RUN);
               } else {
                  this.characterAction.put(character, Rob.EscapeRoutes.STEAL);
               }
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("rob.fight.defeat", character, target));
               if (Util.getInt(0, 100) < 50) {
                  this.characterAction.put(character, Rob.EscapeRoutes.CALLGUARDS);
               } else if (Util.getInt(0, 100) < 50) {
                  this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.meh", character, target));
                  this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, this.getCharacter()));
               } else {
                  this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.rape", character, target));
                  this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, this.getCharacter()));
                  this.getAttributeModifications().add(new AttributeModification(0.28F, Sextype.ANAL, character));
               }
            }

            if (character.getTraits().contains(Trait.FIRSTAID)) {
               character.getAttribute(EssentialAttributes.HEALTH).addToValue(10.0F);
               this.messageData.addToMessage("\n" + TextUtil.t("nurse.selfheal", character));
               this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.MEDICALKNOWLEDGE, character));
            }
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.CALLGUARDS) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.callguards", character, target));
            if ((
                  character.getTraits().contains(Trait.SLUT)
                     || character.getTraits().contains(Trait.LIAISONSDANGEREUSES)
                     || character.getTraits().contains(Trait.NYMPHO)
               )
               && Util.getInt(0, 110) < character.getFinalValue(SpecializationAttribute.SEDUCTION)) {
               this.messageData.addToMessage("\n" + TextUtil.t("rob.fuckguards", character, target));
               this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.getCharacter()));
               this.getAttributeModifications().add(new AttributeModification(0.5F, Sextype.GROUP, character));
            } else if (Util.getInt(0, 100) < 30) {
               this.messageData.addToMessage("\n" + TextUtil.t("rob.police.pay", character, target));
               this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, this.getCharacter()));
               this.setIncome(-1500);
            } else {
               this.messageData.addToMessage("\n" + TextUtil.t("rob.defeat.police.lesson", character, target));
               this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, this.getCharacter()));
               this.getAttributeModifications().add(new AttributeModification(0.5F, Sextype.GROUP, character));
            }
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.FUCKANDROB) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.fuckandrob", character, target));
            this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.getCharacter()));
            this.getAttributeModifications().add(new AttributeModification(0.48F, Sextype.VAGINAL, character));
            stealItemChance += 5;
            stealChance += 5;
            this.characterAction.put(character, Rob.EscapeRoutes.STEAL);
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.SEDUCEANDROB) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.seduceandrob", character, target));
            this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.getCharacter()));
            this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.SEDUCTION, character));
            this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.STRIP, character));
            stealItemChance += 5;
            stealChance += 5;
            this.characterAction.put(character, Rob.EscapeRoutes.STEAL);
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.SEDUCEANDFLEE) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.seduceandflee", character, target));
            this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.getCharacter()));
            this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.SEDUCTION, character));
            this.getAttributeModifications().add(new AttributeModification(0.28F, SpecializationAttribute.STRIP, character));
            this.characterAction.put(character, Rob.EscapeRoutes.RUN);
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.FUCKANDFLEE) {
            this.messageData.addToMessage(TextUtil.t("rob.fuckandflee", character, target));
            this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, this.getCharacter()));
            this.getAttributeModifications().add(new AttributeModification(0.48F, Sextype.VAGINAL, character));
            this.characterAction.put(character, Rob.EscapeRoutes.RUN);
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.STEAL) {
            int stolenAmount = 0;
            Inventory.ItemData stolenItem = null;
            if (Util.getInt(0, 100) < stealItemChance) {
               stolenItem = target.getItem();
               if (stolenItem != null) {
                  this.getAttributeModifications().add(new AttributeModification(1.07F, EssentialAttributes.MOTIVATION, character));
                  if (character.getTraits().contains(Trait.RESELLER) && stolenItem.getItem().getType() != ItemType.UNLOCK) {
                     Jasbro.getInstance().getData().earnMoney(stolenItem.getItem().getValue() / 2, stolenItem);
                  } else {
                     Jasbro.getInstance().getData().getInventory().addItems(stolenItem.getItem(), stolenItem.getAmount());
                  }

                  Object[] arg2 = new Object[]{stolenItem.getItem().getName(), stolenItem.getAmount()};
                  this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.item", character, target, arg2));
               }
            }

            if (Util.getInt(0, 100) < stealChance) {
               this.getAttributeModifications().add(new AttributeModification(1.07F, EssentialAttributes.MOTIVATION, character));
               stolenAmount += target.payFixed((int)(stealAmount * 10 + target.getMoney() * stealAmount / 100.0F));
               Object[] arg = new Object[]{stolenAmount};
               Jasbro.getInstance().getData().earnMoney(stolenAmount, this);
               this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.money", character, target, arg));
            }

            if (stolenAmount == 0 && stolenItem == null) {
               this.messageData.addToMessage("\n" + TextUtil.t("rob.steal.nothing", character, target));
            }
         }

         if (this.characterAction.get(character) == Rob.EscapeRoutes.RUN) {
            this.messageData.addToMessage("\n" + TextUtil.t("rob.noticed.run", character, target));
         }
      }
   }

   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      String message = TextUtil.t("rob.basic", character);
      this.messageData = new MessageData(message, null, this.getBackground());
      this.messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()));
      return this.messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.2F, EssentialAttributes.MOTIVATION));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0F, EssentialAttributes.ENERGY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, SpecializationAttribute.AGILITY));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5F, SpecializationAttribute.PICKPOCKETING));
      return modifications;
   }

   private Customer generateTarget(int skill) {
      SpawnData spawnData = new SpawnData();
      if (skill < 20) {
         return spawnData.createCustomer(CustomerType.BUM);
      } else if (skill < 40) {
         return spawnData.createCustomer(CustomerType.PEASANT);
      } else if (skill < 60) {
         return spawnData.createCustomer(CustomerType.MERCHANT);
      } else if (skill < 80) {
         return spawnData.createCustomer(CustomerType.BUSINESSMAN);
      } else if (skill < 100) {
         return spawnData.createCustomer(CustomerType.MINORNOBLE);
      } else if (skill < 150) {
         return spawnData.createCustomer(CustomerType.LORD);
      } else {
         return skill < 190 ? spawnData.createCustomer(CustomerType.CELEBRITY) : spawnData.createCustomer(CustomerType.BUM);
      }
   }

   private enum EscapeRoutes {
      STEAL,
      CAUGHTANDFIGHT,
      RUN,
      FUCKANDFLEE,
      SEDUCEANDROB,
      FUCKANDROB,
      SEDUCEANDFLEE,
      CALLGUARDS;
   }
}
