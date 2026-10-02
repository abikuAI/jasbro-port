/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.market.Auction;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class SellSlaveQuest
extends Quest {
    private Charakter slave;
    private int timeRemaining;
    private int targetAmount;
    private String clientType;

    public SellSlaveQuest(Charakter slave, int time, int targetAmount) {
        this.slave = slave;
        this.timeRemaining = time;
        this.targetAmount = targetAmount;
    }

    @Override
    public List<QuestStage> getInitStages() {
        ArrayList<QuestStage> questStages = new ArrayList<QuestStage>();
        questStages.add(new SellGirlQuestStage());
        return questStages;
    }

    public static SellSlaveQuest generate(int difficultyModifier) {
        Charakter character = Jasbro.getInstance().generateBasicSlave();
        character.setOwnership(Ownership.NOTOWNEDCANSELL);
        int targetAmount = 900 + difficultyModifier / 20 * 200;
        int time = 30;
        return new SellSlaveQuest(character, time, targetAmount);
    }

    private class SellGirlQuestStage
    extends QuestStage {
        private SellGirlQuestStage() {
        }

        @Override
        public void init(Quest quest) {
            String text = TextUtil.t("quest.startet") + " " + this.getTitle(quest) + "\n" + this.getDescription(quest);
            new MessageScreen(text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, SellSlaveQuest.this.slave), SellSlaveQuest.this.slave.getBackground());
            Jasbro.getInstance().getData().getCharacters().add(this.getSlave());
            this.getSlave().getConditions().add(new Questtimer(this.getTimeRemaining(), SellSlaveQuest.this));
        }

        @Override
        public String getTitle(Quest quest) {
            return TextUtil.t("quests.sellgirlquest.title", this.getSlave());
        }

        @Override
        public String getDescription(Quest quest) {
            String text = TextUtil.t("quests.sellgirlquest.description", (Person)this.getSlave(), this.getClientType()) + "\n";
            text = text + TextUtil.t("quest.time", this.getTimeRemaining()) + "\n";
            text = text + TextUtil.t("quests.sellgirlquest.targetamount", (Person)this.getSlave(), SellSlaveQuest.this.targetAmount) + "\n";
            return text;
        }

        public Charakter getSlave() {
            return SellSlaveQuest.this.slave;
        }

        public int getTimeRemaining() {
            return SellSlaveQuest.this.timeRemaining;
        }

        @Override
        public void handleEvent(MyEvent e, Quest quest) {
            Auction auction;
            GameData gameData = Jasbro.getInstance().getData();
            if (e.getType() == EventType.CHARACTERDEATH && e.getSource() == SellSlaveQuest.this.slave) {
                long value = SellSlaveQuest.this.slave.calculateValue();
                long cost = value * 2L / 100L * 100L;
                gameData.spendMoney(cost, SellSlaveQuest.this);
                gameData.getProtagonist().getFame().modifyFame(-100.0);
                new MessageScreen(TextUtil.t("quests.standardgirlquest.death", cost), new ImageData("images/backgrounds/coffin.jpg"), null, true);
            } else if (e.getType() == EventType.NEXTDAY) {
                SellSlaveQuest.this.timeRemaining--;
                if (this.getTimeRemaining() <= 0) {
                    Jasbro.getInstance().removeCharacter(SellSlaveQuest.this.slave);
                    gameData.spendMoney(500L, SellSlaveQuest.this);
                    new MessageScreen(TextUtil.t("quests.sellgirlquest.timeup", (Person)this.getSlave(), 500), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
                    Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, -100);
                }
            } else if (e.getType() == EventType.SLAVESOLD && e.getSource() instanceof Auction && (auction = (Auction)e.getSource()).getSlave() == SellSlaveQuest.this.slave) {
                if (auction.getMaxBid() >= (long)SellSlaveQuest.this.targetAmount) {
                    int reward = auction.getProfit() / 2;
                    new MessageScreen(TextUtil.t("quests.sellgirlquest.success", (Person)this.getSlave(), reward), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
                    gameData.spendMoney(auction.getProfit() - reward, SellSlaveQuest.this);
                    gameData.getProtagonist().getFame().modifyFame(reward / 5);
                    Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, 10);
                } else {
                    int penalty = 500;
                    gameData.getProtagonist().getFame().modifyFame(-500.0);
                    new MessageScreen(TextUtil.t("quests.sellgirlquest.failed", (Person)this.getSlave(), auction.getProfit(), penalty), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
                    gameData.spendMoney(auction.getProfit() + penalty, SellSlaveQuest.this);
                    Jasbro.getInstance().getData().getQuestManager().setSolved(SellSlaveQuest.this, -5);
                }
            }
        }

        public String getClientType() {
            if (SellSlaveQuest.this.clientType == null) {
                SpawnData spawnData = new SpawnData();
                Customer customer = spawnData.spawn(1, new Fame()).get(0);
                SellSlaveQuest.this.clientType = customer.getName();
            }
            return SellSlaveQuest.this.clientType;
        }
    }
}

