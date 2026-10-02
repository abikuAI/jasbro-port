/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.conditions.Questtimer;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.quests.Reward;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class BetTrainAmountQuest
extends Quest {
    private static final int[] amount = new int[]{5, 10, 20};
    private static final int[] wages = new int[]{1000, 10000, 100000};
    private AttributeType attribute;
    private Integer targetAmount;
    private Charakter slave;
    private int timeRemaining;
    private Reward reward;

    @Override
    public List<QuestStage> getInitStages() {
        ArrayList<QuestStage> questStages = new ArrayList<QuestStage>();
        questStages.add(new PassiveStage());
        questStages.add(new RunningBetStage());
        return questStages;
    }

    @Override
    public void setResolved() {
        this.setCurrentStage(0);
        super.setResolved();
        Jasbro.getInstance().getData().getQuestManager().getInactiveQuests().add(this);
    }

    public String toString() {
        return this.getTitle();
    }

    @Override
    public boolean showInQuestLog() {
        return this.getCurrentStage().showInQuestlog(this);
    }

    private class RunningBetStage
    extends QuestStage {
        private RunningBetStage() {
        }

        @Override
        public void init(Quest quest) {
            String text = TextUtil.t("quest.startet") + " " + this.getTitle(quest) + "\n" + TextUtil.t("quests.betquest.start");
            new MessageScreen(text, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, BetTrainAmountQuest.this.slave), BetTrainAmountQuest.this.slave.getBackground());
            this.getSlave().addCondition(new Questtimer(this.getTimeRemaining(), BetTrainAmountQuest.this));
        }

        @Override
        public String getTitle(Quest quest) {
            return TextUtil.t("quests.betquest.title", this.getSlave());
        }

        @Override
        public String getDescription(Quest quest) {
            Object[] arguments = new Object[]{BetTrainAmountQuest.this.attribute.getText(), BetTrainAmountQuest.this.targetAmount};
            Object[] argumentsTime = new Object[]{this.getTimeRemaining()};
            String text = TextUtil.t("quests.betquest.description", (Person)BetTrainAmountQuest.this.slave, arguments) + "\n";
            text = text + TextUtil.t("quest.time", argumentsTime) + "\n";
            text = text + BetTrainAmountQuest.this.reward.getRewardDescription();
            return text;
        }

        public Charakter getSlave() {
            return BetTrainAmountQuest.this.slave;
        }

        public int getTimeRemaining() {
            return BetTrainAmountQuest.this.timeRemaining;
        }

        @Override
        public void handleEvent(MyEvent e, Quest quest) {
            if (e.getType() == EventType.CHARACTERLOST && e.getSource() == BetTrainAmountQuest.this.slave) {
                BetTrainAmountQuest.this.setResolved();
            } else if (e.getType() == EventType.NEXTDAY) {
                BetTrainAmountQuest.this.timeRemaining--;
                if (this.getTimeRemaining() <= 0) {
                    if (this.getSlave().getFinalValue(BetTrainAmountQuest.this.attribute) >= BetTrainAmountQuest.this.targetAmount) {
                        BetTrainAmountQuest.this.reward.applyReward(this.getSlave());
                        Object[] arguments = new Object[]{BetTrainAmountQuest.this.reward.getRewardMoney()};
                        String message = TextUtil.t("quests.betquest.won", (Person)this.getSlave(), arguments);
                        new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
                        BetTrainAmountQuest.this.setResolved();
                    } else {
                        Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(-BetTrainAmountQuest.this.reward.getPenalty());
                        Object[] arguments = new Object[]{BetTrainAmountQuest.this.reward.getRewardMoney()};
                        String message = TextUtil.t("quests.betquest.lost", (Person)this.getSlave(), arguments) + " ";
                        new MessageScreen(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getSlave()), this.getSlave().getBackground(), true);
                        BetTrainAmountQuest.this.setResolved();
                    }
                }
            }
        }
    }

    private class PassiveStage
    extends QuestStage {
        private PassiveStage() {
        }

        @Override
        public void handleEvent(MyEvent e, Quest quest) {
            if (e.getType() == EventType.NEXTDAY && Util.getInt(0, 100) < 3) {
                Charakter character = null;
                List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();
                for (int i = 0; i < 10 && ((character = characters.get(Util.getInt(0, characters.size()))).getOwnership() != Ownership.OWNED || character.getType() != CharacterType.SLAVE); ++i) {
                    character = null;
                }
                if (character != null) {
                    AttributeType attribute;
                    SpecializationType specializationType;
                    int amountRequired = 0;
                    while (true) {
                        if ((specializationType = SpecializationType.values()[Util.getRnd().nextInt(SpecializationType.values().length)]) == SpecializationType.TRAINER || specializationType == SpecializationType.CATGIRL || specializationType == SpecializationType.LEGACY || specializationType == SpecializationType.FURRY || specializationType == SpecializationType.UNDERAGE) {
                            continue;
                        }
                        attribute = specializationType.getAssociatedAttributes().get(Util.getRnd().nextInt(specializationType.getAssociatedAttributes().size()));
                        if (Jasbro.getInstance().getData().getDay() >= 30 || (specializationType == SpecializationType.SLAVE || specializationType == SpecializationType.SEX || specializationType == SpecializationType.MAID) && (specializationType != SpecializationType.MAID || attribute != SpecializationAttribute.COOKING)) break;
                    }
                    if (!character.getSpecializations().contains(specializationType)) {
                        amountRequired += amount[0];
                    }
                    int[] finalAmount = new int[]{amount[0] + amountRequired, amount[1] + amountRequired, amount[2] + amountRequired};
                    if (attribute instanceof BaseAttributeTypes) {
                        finalAmount[0] = 1;
                        finalAmount[1] = 3;
                        finalAmount[2] = 5;
                    }
                    ArrayList options = new ArrayList();
                    Object[] arguments = new Object[]{wages[0], attribute.getText(), finalAmount[0]};
                    options.add(new SelectionData<Integer>(Integer.valueOf(0), TextUtil.t("quests.betquest.lowbet", arguments) + " " + TextUtil.t("quests.betquest.details", (Person)character, arguments), Jasbro.getInstance().getData().canAfford(wages[0])));
                    arguments[0] = wages[1];
                    arguments[1] = attribute.getText();
                    arguments[2] = finalAmount[1];
                    options.add(new SelectionData<Integer>(Integer.valueOf(1), TextUtil.t("quests.betquest.mediumbet", arguments) + " " + TextUtil.t("quests.betquest.details", (Person)character, arguments), Jasbro.getInstance().getData().canAfford(wages[1])));
                    arguments[0] = wages[2];
                    arguments[1] = attribute.getText();
                    arguments[2] = finalAmount[2];
                    options.add(new SelectionData<Integer>(Integer.valueOf(2), TextUtil.t("quests.betquest.highbet", arguments) + " " + TextUtil.t("quests.betquest.details", (Person)character, arguments), Jasbro.getInstance().getData().canAfford(wages[2])));
                    options.add(new SelectionData<Integer>(3, TextUtil.t("quests.betquest.nobet")));
                    SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character), null, character.getBackground(), TextUtil.t("quests.betquest.intro", (Person)character, arguments));
                    Integer selected = (Integer)selectedOption.getSelectionObject();
                    if (selected != 3) {
                        Reward reward = new Reward(wages[selected] * 2, (Quest)BetTrainAmountQuest.this);
                        if (selected == 2) {
                            reward.setPerkPoints(1);
                        }
                        Jasbro.getInstance().getData().spendMoney(wages[selected], BetTrainAmountQuest.this);
                        BetTrainAmountQuest.this.setCurrentStage(1);
                        BetTrainAmountQuest.this.slave = character;
                        BetTrainAmountQuest.this.timeRemaining = 7;
                        BetTrainAmountQuest.this.attribute = attribute;
                        BetTrainAmountQuest.this.targetAmount = character.getFinalValue(attribute) + finalAmount[selected];
                        BetTrainAmountQuest.this.reward = reward;
                        BetTrainAmountQuest.this.setActive();
                    }
                }
            }
        }
    }
}

