package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.business.Fame;
import jasbro.texts.TextUtil;

public class Reward {
   private int rewardMoney;
   private Charakter rewardCharacter;
   private Quest quest;
   private int perkPoints;

   public Reward(int amountMoney, Quest quest) {
      this.rewardMoney = amountMoney;
      this.quest = quest;
   }

   public Reward(int amountMoney, Quest quest, int skillPoints) {
      this.rewardMoney = amountMoney;
      this.quest = quest;
   }

   public Reward(Charakter character, Quest quest) {
      this.rewardCharacter = character;
      this.quest = quest;
   }

   public void applyReward(Charakter character) {
      Fame fame = Jasbro.getInstance().getData().getProtagonist().getFame();
      if (character != null && this.perkPoints > 0) {
         for (int i = 0; i < this.perkPoints; i++) {
            fame.modifyFame(1000.0);
            character.addBonusPerk();
         }
      }

      GameData gameData = Jasbro.getInstance().getData();
      fame.modifyFame(this.rewardMoney / 10);
      if (gameData.getProtagonist().getAttribute(SpecializationAttribute.EXPERIENCE).getMaxValue() < 100) {
         gameData.getProtagonist()
            .getAttribute(SpecializationAttribute.EXPERIENCE)
            .setMaxValue(gameData.getProtagonist().getAttribute(SpecializationAttribute.EXPERIENCE).getMaxValue() + 2);
      }

      if (this.quest != null) {
         gameData.earnMoney(this.rewardMoney, this.quest.getTitle());
      } else {
         gameData.earnMoney(this.rewardMoney, "Quest");
      }

      if (this.rewardCharacter != null) {
         fame.modifyFame(1000.0);
         gameData.getCharacters().add(this.rewardCharacter);
         this.rewardCharacter.setOwnership(Ownership.OWNED);
      }
   }

   public long getPenalty() {
      if (this.rewardMoney != 0) {
         return this.rewardMoney / 4;
      } else {
         return this.rewardCharacter != null ? this.rewardCharacter.calculateValue() / 4L : 500L;
      }
   }

   public String getRewardDescription() {
      Object[] arguments = new Object[]{this.rewardMoney};
      if (this.rewardMoney != 0 && this.rewardCharacter == null) {
         return TextUtil.t("quest.reward.description.money", arguments);
      } else if (this.rewardMoney == 0 && this.rewardCharacter != null) {
         return TextUtil.t("quest.reward.description.character", this.rewardCharacter);
      } else if (this.rewardMoney == 0 && this.rewardCharacter == null) {
         return TextUtil.t("quest.reward.description.characterAndMoney", this.rewardCharacter, arguments);
      } else {
         return this.rewardMoney != 0 && this.perkPoints > 0 ? TextUtil.t("quest.reward.description.moneyPerk", arguments) : "";
      }
   }

   public String getSuccessMessage() {
      if (this.rewardMoney != 0 && this.rewardCharacter == null) {
         return TextUtil.t("quest.reward.money", this.rewardMoney);
      } else if (this.rewardMoney == 0 && this.rewardCharacter != null) {
         return TextUtil.t("quest.reward.character", this.rewardCharacter);
      } else {
         return this.rewardMoney == 0 && this.rewardCharacter == null
            ? TextUtil.t("quest.reward.characterAndMoney", this.rewardCharacter, this.rewardMoney)
            : "";
      }
   }

   public int getRewardMoney() {
      return this.rewardMoney;
   }

   public Charakter getRewardCharacter() {
      return this.rewardCharacter;
   }

   public Quest getQuest() {
      return this.quest;
   }

   public void setQuest(Quest quest) {
      this.quest = quest;
   }

   public int getPerkPoints() {
      return this.perkPoints;
   }

   public void setPerkPoints(int perkPoints) {
      this.perkPoints = perkPoints;
   }
}
