/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.SummoningItem;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class MonsterFight
extends RunningActivity
implements BusinessMainActivity,
BusinessSecondaryActivity {
    private List<Charakter> fighters;
    private ComplexEnemy monster;
    private MessageData message;
    private boolean playerWon;
    String rapeText;
    private SummoningItem item;
    private String itemName;
    private String monsterID = null;

    @Override
    public void init() {
        this.fighters = this.getCharacters();
        List options = this.getItemOptions(ItemType.SUMMONING);
        if (this.getPlannedActivity().getSelectedOption() == null && options.size() > 0) {
            if (this.fighters.size() > 1) {
                SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.fighters.get(0)), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.fighters.get(1)), this.fighters.get(0).getBackground(), TextUtil.t("fight.monster.option.description", this.fighters));
                this.item = (SummoningItem)selectedOption.getSelectionObject();
            } else {
                SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.fighters.get(0)), null, this.fighters.get(0).getBackground(), TextUtil.t("fight.monster.option.description", this.fighters.get(0)));
                this.item = (SummoningItem)selectedOption.getSelectionObject();
            }
            this.spawnEnemy(true);
        } else if (this.getPlannedActivity().getSelectedOption() != null) {
            this.item = (SummoningItem)this.getPlannedActivity().getSelectedOption().getSelectionObject();
            this.spawnEnemy(true);
        } else {
            this.spawnEnemy(false);
        }
    }

    @Override
    public MessageData getBaseMessage() {
        this.message = new MessageData();
        this.message.setBackground(this.getCharacter().getBackground());
        if (this.getMainCustomers().size() > 0) {
            Object[] arguments = new Object[]{this.monster.getName(), this.getCustomers().size()};
            if (this.fighters.size() == 1) {
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.getCharacter());
                this.message.setImage(image);
                this.message.setMessage(TextUtil.t("fight.basic2", (Person)this.fighters.get(0), (Person)this.monster, arguments));
            } else {
                for (Charakter fighter : this.fighters) {
                    this.message.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, fighter));
                }
                this.message.setMessage(TextUtil.t("fight.basicmultivsmonster", this.fighters, arguments));
            }
            ImageData imageM = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.monster);
            this.message.addImage(imageM);
        } else {
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter());
            this.message.setImage(image);
            this.message.setMessage(TextUtil.t("fight.noEnemy", this.getCharacter()));
        }
        return this.message;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.STRENGTH));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.STAMINA));
        if (!this.getCharacter().getTraits().contains(Trait.LEGACYADVENTURER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
        }
        return modifications;
    }

    @Override
    public int getAppeal() {
        return Util.getInt(2, 8);
    }

    @Override
    public void perform() {
        Battle battle = new Battle();
        if (this.getMainCustomers().size() > 0) {
            int i = 0;
            ArrayList<Integer> startingHealths = new ArrayList<Integer>();
            battle.getSideA().addAll(this.fighters);
            battle.getSideB().add(this.monster);
            for (Charakter fighter : this.fighters) {
                startingHealths.add(fighter.getHitpoints());
            }
            boolean stop = false;
            block1: do {
                battle.doRound();
                ++i;
                for (Charakter character : this.fighters) {
                    if (character.getHitpoints() > 10) continue;
                    stop = true;
                    continue block1;
                }
            } while (this.monster.getHitpoints() > 10 && !stop);
            float entertainmentRating = (float)i / 10.0f;
            int j = 0;
            for (Charakter fighter : this.fighters) {
                entertainmentRating += fighter.getDamage() / 25.0f + (float)fighter.getArmor() / 1500.0f;
                this.addAttributeModification(this, (Integer)startingHealths.get(j) - fighter.getHitpoints(), fighter, EssentialAttributes.HEALTH);
                ++j;
            }
            for (Customer customer : this.getCustomers()) {
                customer.addToSatisfaction((int)(entertainmentRating * 30.0f), this);
            }
            boolean charactersWon = false;
            if (this.monster.getHitpoints() <= 10) {
                charactersWon = true;
                int a = 0;
                for (Charakter fighter : this.fighters) {
                    if (this.fighters.get(a).getTraits().contains(Trait.REPTILIANMOTIVATION)) {
                        this.addAttributeModification(this, 2.0f, this.fighters.get(a), EssentialAttributes.MOTIVATION);
                    }
                    ++a;
                }
            }
            if (charactersWon) {
                this.getMainCustomer().addToSatisfaction(40, this);
            } else {
                this.getMainCustomer().addToSatisfaction(150, this);
            }
            this.message.addToMessage("\n\n" + battle.getCombatText());
            if (charactersWon) {
                entertainmentRating *= 2.0f;
            }
            int winnings = 0;
            for (Customer customer : this.getCustomers()) {
                winnings += customer.pay(entertainmentRating);
            }
            this.modifyIncome(winnings);
            Object[] arguments = new Object[]{winnings};
            String message = TextUtil.t("fight.result", arguments);
            if (charactersWon) {
                message = i < 8 ? message + "\n\n" + TextUtil.t("fight.onesided", TextUtil.listCharacters(this.fighters)) : message + "\n\n" + TextUtil.t("fight.longMatch", TextUtil.listCharacters(this.fighters));
            }
            ImageData image = null;
            MessageData messageData = new MessageData(null, null, this.getCharacter().getBackground());
            if (charactersWon) {
                for (Charakter fighter : this.fighters) {
                    List tags = this.getCharacter().getBaseTags();
                    tags.add(0, ImageTag.MONSTER);
                    tags.add(ImageTag.FORCED);
                    messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
                    this.addAttributeModification(this, 1.0f, fighter, Sextype.MONSTER);
                    messageData.addImage(image);
                }
                messageData.setMessage(message);
                this.getMessages().add(messageData);
            } else {
                if (Util.getInt(1, 100) <= 100) {
                    if (this.fighters.size() == 1) {
                        if (this.fighters.get(0).getGender() == Gender.MALE) {
                            this.rapeText = this.monster.getMaleRape();
                            if (this.rapeText == "") {
                                this.rapeText = TextUtil.t("fight.monster.rape.male.generic", (Person)this.getCharacter(), this.monster.getName());
                            }
                        } else {
                            this.rapeText = this.monster.getFemaleRape();
                            if (this.rapeText == "") {
                                this.rapeText = TextUtil.t("fight.monster.rape.female.generic", (Person)this.getCharacter(), this.monster.getName());
                            }
                        }
                        message = message + "\n\n" + TextUtil.t("fight.monster.lost", (Person)this.getCharacter(), this.monster);
                        message = message + "\n\n" + TextUtil.getInstance().applyTemplates(this.rapeText, this.fighters);
                    } else {
                        Object[] monsterName = new Object[]{this.monster.getName()};
                        int primaryFighter = Util.getInt(0, 2);
                        if (this.fighters.get(primaryFighter).getGender() == Gender.MALE) {
                            this.rapeText = this.monster.getMaleRape();
                            if (this.rapeText == "") {
                                this.rapeText = TextUtil.t("fight.monster.rape.male.generic", (Person)this.fighters.get(primaryFighter), this.monster.getName());
                            }
                        } else {
                            this.rapeText = this.monster.getFemaleRape();
                            if (this.rapeText == "") {
                                this.rapeText = TextUtil.t("fight.monster.rape.female.generic", (Person)this.fighters.get(primaryFighter), this.monster.getName());
                            }
                        }
                        message = this.monster.getGender() == Gender.MALE ? (primaryFighter == 0 ? message + "\n\n" + TextUtil.t("fight.monster.lost2.char1.maleMonster", this.fighters, monsterName) : message + "\n\n" + TextUtil.t("fight.monster.lost2.char2.maleMonster", this.fighters, monsterName)) : (primaryFighter == 0 ? message + "\n\n" + TextUtil.t("fight.monster.lost2.char1.femaleMonster", this.fighters, monsterName) : message + "\n\n" + TextUtil.t("fight.monster.lost2.char2.femaleMonster", this.fighters, monsterName));
                        message = message + "\n\n" + TextUtil.getInstance().applyTemplates(this.rapeText, this.fighters);
                    }
                    for (Charakter fighter : this.fighters) {
                        List tags = this.getCharacter().getBaseTags();
                        tags.add(0, ImageTag.MONSTER);
                        tags.add(ImageTag.FORCED);
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
                        this.addAttributeModification(this, 1.0f, fighter, Sextype.MONSTER);
                    }
                    this.setSextype(Sextype.MONSTER);
                } else {
                    for (Charakter fighter : this.fighters) {
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, fighter));
                    }
                }
                ImageData imageM = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, this.monster);
                messageData.addImage(imageM);
                messageData.setMessage(message);
                this.getMessages().add(messageData);
            }
        }
    }

    public void addAttributeModification(RunningActivity activity, float amount, Charakter character, AttributeType attributeType) {
        AttributeModification attributeModification = new AttributeModification(0.0f, attributeType, character);
        attributeModification.setRealModification(amount);
        this.getAttributeModifications().add(attributeModification);
    }

    @Override
    public int rateCustomer(Customer customer) {
        if (customer.getMoney() > 500) {
            return customer.getMoney();
        }
        return 0;
    }

    @Override
    public int getMaxAttendees() {
        return 40;
    }

    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        return new ArrayList(this.getItemOptions(ItemType.SUMMONING));
    }

    public List<SelectionData<SummoningItem>> getItemOptions(ItemType itemtype) {
        ArrayList<SelectionData<SummoningItem>> options = new ArrayList<SelectionData<SummoningItem>>();
        for (Item item : Jasbro.getInstance().getData().getInventory().getExistingItems()) {
            if (item.getType() != itemtype) continue;
            this.itemName = item.getName();
            SelectionData<SummoningItem> option = new SelectionData<SummoningItem>();
            option.setSelectionObject((SummoningItem)item);
            option.setButtonText(TextUtil.t("fight.monster.name", this.itemName));
            option.setShortText(item.getText());
            options.add(option);
        }
        return options;
    }

    public void spawnEnemy(boolean ownMonster) {
        if (ownMonster) {
            this.monsterID = this.item.getSummonedMonster();
            this.monster = CharacterSpawner.getEnemy(this.monsterID);
        } else {
            this.monster = CharacterSpawner.spawnCustomerEnemy();
        }
        if (this.fighters.size() > 1) {
            this.monster.setHitpoints(this.monster.getHitpoints() * 2);
            this.monster.setAttribute(CalculatedAttribute.SPEED, this.monster.getAttribute(CalculatedAttribute.SPEED) * 2.0);
            this.monster.setAttribute(CalculatedAttribute.DAMAGE, this.monster.getAttribute(CalculatedAttribute.DAMAGE) * 1.5);
            this.monster.setAttribute(CalculatedAttribute.CRITCHANCE, this.monster.getAttribute(CalculatedAttribute.CRITCHANCE) * 1.5);
            this.monster.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, this.monster.getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT) * 2.0);
        }
    }

    public class MonsterTextUtil {
        public String MonsterRape(String text, List<? extends Person> people) {
            return text;
        }
    }
}

