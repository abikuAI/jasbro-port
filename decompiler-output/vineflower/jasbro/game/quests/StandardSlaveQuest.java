package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class StandardSlaveQuest extends Quest {
   private Charakter slave;
   private int timeRemaining;
   private CharacterGoal goal;
   private Reward reward;
   private String clientType;

   public StandardSlaveQuest(Charakter slave, int time, CharacterGoal goal, Reward reward) {
      reward.setQuest(this);
      this.slave = slave;
      this.timeRemaining = time;
      this.goal = goal;
      this.reward = reward;
   }

   @Override
   public List<QuestStage> getInitStages() {
      List<QuestStage> questStages = new ArrayList<>();
      questStages.add(new StandardSlaveQuest.GirlQuestStage());
      return questStages;
   }

   public static StandardSlaveQuest generate(int difficultyModifier) {
      Charakter character = Jasbro.getInstance().generateBasicSlave();
      CharacterGoal goal = new CharacterGoal();
      SpecializationType[] specializationTypes = SpecializationType.values();
      int reward = 0;
      int time = 0;
      int statGoal = 0;
      int baseDifficulty = 0;
      new ArrayList();
      List<SpecializationType> possibleRequests = new ArrayList<>();
      if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.TOUGHERMISSIONS4)) {
         baseDifficulty = Util.getInt(4, 6);
         possibleRequests.add(SpecializationType.SEX);
         possibleRequests.add(SpecializationType.MAID);
         possibleRequests.add(SpecializationType.KINKYSEX);
         possibleRequests.add(SpecializationType.WHORE);
         possibleRequests.add(SpecializationType.DANCER);
         possibleRequests.add(SpecializationType.BARTENDER);
         possibleRequests.add(SpecializationType.NURSE);
         possibleRequests.add(SpecializationType.FIGHTER);
         possibleRequests.add(SpecializationType.ALCHEMIST);
         possibleRequests.add(SpecializationType.THIEF);
      } else if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.TOUGHERMISSIONS3)) {
         baseDifficulty = Util.getInt(3, 5);
         possibleRequests.add(SpecializationType.SEX);
         possibleRequests.add(SpecializationType.MAID);
         possibleRequests.add(SpecializationType.KINKYSEX);
         possibleRequests.add(SpecializationType.WHORE);
         possibleRequests.add(SpecializationType.DANCER);
         possibleRequests.add(SpecializationType.BARTENDER);
         possibleRequests.add(SpecializationType.NURSE);
         possibleRequests.add(SpecializationType.FIGHTER);
         possibleRequests.add(SpecializationType.ALCHEMIST);
         possibleRequests.add(SpecializationType.THIEF);
      } else if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.TOUGHERMISSIONS2)) {
         baseDifficulty = Util.getInt(1, 4);
         possibleRequests.add(SpecializationType.ALCHEMIST);
         possibleRequests.add(SpecializationType.SEX);
         possibleRequests.add(SpecializationType.MAID);
         possibleRequests.add(SpecializationType.KINKYSEX);
         possibleRequests.add(SpecializationType.WHORE);
         possibleRequests.add(SpecializationType.DANCER);
         possibleRequests.add(SpecializationType.BARTENDER);
         possibleRequests.add(SpecializationType.NURSE);
         possibleRequests.add(SpecializationType.FIGHTER);
      } else if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.TOUGHERMISSIONS1)) {
         baseDifficulty = Util.getInt(0, 3);
         possibleRequests.add(SpecializationType.DANCER);
         possibleRequests.add(SpecializationType.BARTENDER);
         possibleRequests.add(SpecializationType.SEX);
         possibleRequests.add(SpecializationType.MAID);
         possibleRequests.add(SpecializationType.KINKYSEX);
         possibleRequests.add(SpecializationType.WHORE);
      } else {
         baseDifficulty = Util.getInt(0, 2);
         possibleRequests.add(SpecializationType.SEX);
         possibleRequests.add(SpecializationType.MAID);
         possibleRequests.add(SpecializationType.BARTENDER);
         possibleRequests.add(SpecializationType.WHORE);
      }

      time = 7 + baseDifficulty * 15 + Util.getInt(-1, 7);
      reward = 100;
      int rand = 0;

      for (int i = 0; i < baseDifficulty / 2 + 1; i++) {
         rand = Util.getInt(0, possibleRequests.size());
         if (!goal.getSpecializations().contains(possibleRequests.get(rand))) {
            goal.getSpecializations().add(possibleRequests.get(rand));
         } else {
            i--;
         }
      }

      AttributeType attributeType = null;
      int attributeValue = 0;

      for (SpecializationType spec : goal.getSpecializations()) {
         rand = Util.getInt(0, spec.getAssociatedAttributes().size());
         attributeType = spec.getAssociatedAttributes().get(rand);
         attributeValue = (baseDifficulty + 1) * (baseDifficulty + 1) * Util.getInt(2, 3) + Util.getInt(5, 9);
         attributeValue += attributeValue * Util.getInt(-10, 10) / 100;
         goal.getAttributeValueMap().put(attributeType, attributeValue);
         reward += attributeValue * attributeValue * (baseDifficulty + 2);
         time += Math.min(attributeValue / 10, 7);
         if (!character.getSpecializations().contains(spec)) {
            reward += 500;
            time += 3;
            if (character.getSpecializations().size() != 0) {
               reward += 1000;
               time += 5;
            }
         }
      }

      return new StandardSlaveQuest(character, time, goal, new Reward(reward, null));
   }

   private class GirlQuestStage extends QuestStage {
      private GirlQuestStage() {
      }

      @Override
      public void init(Quest quest) {
         String text = TextUtil.t("quest.startet") + " " + this.getTitle(quest) + "\n" + this.getDescription(quest);
         new MessageScreen(
            text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, StandardSlaveQuest.this.slave), StandardSlaveQuest.this.slave.getBackground()
         );
         Jasbro.getInstance().getData().getCharacters().add(this.getSlave());
         this.getSlave().addCondition(new Questtimer(this.getTimeRemaining(), StandardSlaveQuest.this));
      }

      @Override
      public String getTitle(Quest quest) {
         return TextUtil.t("quests.standardgirlquest.title", this.getSlave());
      }

      @Override
      public String getDescription(Quest quest) {
         String text = TextUtil.t("quests.standardgirlquest.description", this.getClientType()) + "\n";
         text = text + TextUtil.t("quest.time", this.getTimeRemaining()) + "\n";
         text = text + StandardSlaveQuest.this.goal.getDescription();
         return text + StandardSlaveQuest.this.reward.getRewardDescription();
      }

      public Charakter getSlave() {
         return StandardSlaveQuest.this.slave;
      }

      public int getTimeRemaining() {
         return StandardSlaveQuest.this.timeRemaining;
      }

      @Override
      public void handleEvent(MyEvent e, Quest quest) {
         GameData gameData = Jasbro.getInstance().getData();
         if (e.getType() == EventType.CHARACTERDEATH && e.getSource() == StandardSlaveQuest.this.slave) {
            long value = StandardSlaveQuest.this.slave.calculateValue();
            long cost = value * 2L / 100L * 100L;
            gameData.spendMoney(cost, this);
            gameData.getProtagonist().getFame().modifyFame(-1000.0);
            new MessageScreen(TextUtil.t("quests.standardgirlquest.death", cost), new ImageData("images/backgrounds/coffin.jpg"), null);
         } else if (e.getType() == EventType.NEXTDAY) {
            StandardSlaveQuest.this.timeRemaining--;
            if (this.getTimeRemaining() <= 0) {
               this.finish(quest);
            }
         }
      }

      @Override
      public void finish(Quest quest) {
         GameData gameData = Jasbro.getInstance().getData();
         Jasbro.getInstance().removeCharacter(StandardSlaveQuest.this.slave);
         if (this.getGoal().goalReached(this.getSlave())) {
            StandardSlaveQuest.this.reward.applyReward(null);
            gameData.getProtagonist()
               .getAttribute(SpecializationAttribute.EXPERIENCE)
               .addToValue(5.0F - gameData.getProtagonist().getAttribute(SpecializationAttribute.EXPERIENCE).getInternValue() / 20.0F);
            if (gameData.getProtagonist().getAttribute(SpecializationAttribute.EXPERIENCE).getMaxValue() < 100) {
               gameData.getProtagonist()
                  .getAttribute(SpecializationAttribute.EXPERIENCE)
                  .setMaxValue(gameData.getProtagonist().getAttribute(SpecializationAttribute.EXPERIENCE).getMaxValue() + 5);
            }

            String message = TextUtil.t("quests.standardgirlquest.success", this.getSlave());
            message = message + " " + StandardSlaveQuest.this.reward.getSuccessMessage();
            if (!gameData.getCharacters().contains(StandardSlaveQuest.this.slave)) {
               message = message + " " + TextUtil.t("quests.standardgirlquest.slavereturned", this.getSlave());
            }

            new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
            Jasbro.getInstance().getData().getQuestManager().setSolved(StandardSlaveQuest.this, 5);
         } else {
            long penalty = StandardSlaveQuest.this.reward.getPenalty();
            gameData.getProtagonist().getFame().modifyFame(-penalty);
            gameData.spendMoney(penalty, StandardSlaveQuest.this.getTitle());
            String message = TextUtil.t("quests.standardgirlquest.failure", this.getSlave(), penalty) + " ";
            message = message + TextUtil.t("quests.standardgirlquest.slavereturned.female", this.getSlave());
            new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
            Jasbro.getInstance().getData().getQuestManager().setSolved(StandardSlaveQuest.this, -15);
         }
      }

      public String getClientType() {
         if (StandardSlaveQuest.this.clientType == null) {
            SpawnData spawnData = new SpawnData();
            Customer customer = spawnData.spawn(1, new Fame()).get(0);
            StandardSlaveQuest.this.clientType = customer.getName();
         }

         return StandardSlaveQuest.this.clientType;
      }

      public CharacterGoal getGoal() {
         return StandardSlaveQuest.this.goal;
      }

      @Override
      public boolean canFinishEarly(Quest quest) {
         return this.getGoal().goalReached(this.getSlave());
      }
   }
}
