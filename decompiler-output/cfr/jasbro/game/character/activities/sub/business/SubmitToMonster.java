/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.MonsterDickType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class SubmitToMonster
extends RunningActivity
implements BusinessMainActivity,
BusinessSecondaryActivity {
    private MessageData messageData;
    private monsterKind monster;
    private float HpAndEnCost;

    public float getHpAndEnCost() {
        return this.HpAndEnCost;
    }

    public void setHpAndEnCost(float HpAndEnCost) {
        this.HpAndEnCost = HpAndEnCost;
    }

    public monsterKind getMonster() {
        return this.monster;
    }

    public void setMonster(monsterKind monster) {
        this.monster = monster;
    }

    @Override
    public void init() {
        ArrayList<monsterKind> possibleMonsters = new ArrayList<monsterKind>();
        if (this.getMainCustomer() != null) {
            for (monsterKind mon : monsterKind.values()) {
                if (this.getMainCustomer().getMoney() <= mon.monsterRank * 2 * mon.monsterRank) continue;
                possibleMonsters.add(mon);
            }
            this.setMonster((monsterKind)((Object)possibleMonsters.get(Util.getInt(0, possibleMonsters.size()))));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        if (this.getMainCustomer() != null) {
            List tags = this.getCharacter().getBaseTags();
            tags.add(0, ImageTag.MONSTER);
            tags.add(ImageTag.FORCED);
            Object[] arguments = new Object[]{this.getMainCustomer().getName(), this.getCustomers().size()};
            this.messageData = new MessageData(TextUtil.t("submitToMonster.basic", (Person)this.getCharacter(), arguments), null, this.getBackground());
        } else {
            this.messageData = new MessageData(TextUtil.t("submitToMonster.nocustomer", this.getCharacter()), null, this.getBackground());
        }
        return this.messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.getMainCustomer() != null) {
            if (this.getCharacter().getTraits().contains(Trait.MONSTERSOW)) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
            } else {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -2.3f, EssentialAttributes.MOTIVATION));
            }
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
            if (!this.getCharacter().getTraits().contains(Trait.LEGACYADVENTURER)) {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
            }
        } else {
            return new Idle().getStatModifications();
        }
        return modifications;
    }

    @Override
    public void perform() {
        if (this.getMainCustomer() != null) {
            monsterKind monster = this.getMonster();
            int monsterRank = monster.monsterRank;
            ArrayList<ImageTag> tags = new ArrayList<ImageTag>();
            MonsterDickType monsterDick = monster.monsterDick;
            Charakter character = this.getCharacter();
            String monsterName = TextUtil.t(monster.toString(), character);
            Object[] argument = new Object[]{monsterName, 2 * monsterRank * monsterRank + 500};
            this.messageData.addToMessage("\n" + TextUtil.t("submitToMonster.monsterkind", (Person)character, (Person)this.getMainCustomer(), argument));
            int skill = character.getFinalValue(Sextype.MONSTER) + character.getStamina() + character.getStrength();
            if ((skill /= 3) < monsterRank / 3) {
                this.messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.tooscary", (Person)character, argument));
                this.getMainCustomer().addToSatisfaction(-50, this);
                this.setHpAndEnCost(0.0f);
                tags.add(ImageTag.STANDARD);
                for (Customer customer : this.getCustomers()) {
                    customer.addToSatisfaction(-15, this);
                }
            } else if (skill < monsterRank / 2) {
                tags.add(ImageTag.MONSTER);
                this.messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.try", (Person)character, argument));
                this.getMainCustomer().addToSatisfaction(15 + monsterRank, this);
                this.setHpAndEnCost(60.0f);
                this.setIncome(2 * monsterRank * monsterRank + 500);
                for (Customer customer : this.getCustomers()) {
                    customer.addToSatisfaction(10 + monsterRank / 2, this);
                }
            } else if (skill > monsterRank * 3) {
                tags.add(ImageTag.MONSTER);
                this.setIncome(2 * monsterRank * monsterRank + 500);
                this.messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.dry", (Person)character, argument));
                this.getMainCustomer().addToSatisfaction(25 + monsterRank, this);
                this.setHpAndEnCost(15.0f);
                for (Customer customer : this.getCustomers()) {
                    customer.addToSatisfaction(20 + monsterRank / 2, this);
                }
            } else if (skill > monsterRank * 2) {
                tags.add(ImageTag.MONSTER);
                this.setIncome(2 * monsterRank * monsterRank + 500);
                this.messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.cake", (Person)character, argument));
                this.getMainCustomer().addToSatisfaction(20 + monsterRank, this);
                this.setHpAndEnCost(30.0f);
                for (Customer customer : this.getCustomers()) {
                    customer.addToSatisfaction(15 + monsterRank / 2, this);
                }
            } else {
                tags.add(ImageTag.MONSTER);
                this.setIncome(2 * monsterRank * monsterRank + 500);
                this.messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.okay", (Person)character, argument));
                this.getMainCustomer().addToSatisfaction(15 + monsterRank, this);
                this.setHpAndEnCost(45.0f);
                for (Customer customer : this.getCustomers()) {
                    customer.addToSatisfaction(10 + monsterRank / 2, this);
                }
            }
            if (!character.getTraits().contains(Trait.MONSTERSOW)) {
                this.getAttributeModifications().add(new AttributeModification((float)(-monsterRank) * this.getHpAndEnCost() / 100.0f, EssentialAttributes.ENERGY, character));
                this.getAttributeModifications().add(new AttributeModification((float)(-monsterRank) * this.getHpAndEnCost() / 100.0f, EssentialAttributes.HEALTH, character));
            } else {
                this.getAttributeModifications().add(new AttributeModification((float)(-monsterRank) * this.getHpAndEnCost() / 200.0f, EssentialAttributes.ENERGY, character));
            }
            this.getAttributeModifications().add(new AttributeModification(2.0f, Sextype.MONSTER, character));
            this.getAttributeModifications().add(new AttributeModification(0.09f, BaseAttributeTypes.STAMINA, character));
            switch (monsterDick) {
                case NORMAL: {
                    tags.add(ImageTag.NORMALDICK);
                    break;
                }
                case FLUID: {
                    tags.add(ImageTag.FLUID);
                    break;
                }
                case TENTACLE: {
                    tags.add(ImageTag.TENTACLE);
                    break;
                }
            }
            this.messageData.setImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
        }
    }

    @Override
    public int rateCustomer(Customer customer) {
        if (customer.getMoney() > 500) {
            return customer.getMoney();
        }
        return 0;
    }

    @Override
    public int getAppeal() {
        return Util.getInt(1, 7);
    }

    @Override
    public int getMaxAttendees() {
        return 40;
    }

    private static enum monsterKind {
        GOBELIN(5, MonsterDickType.NORMAL),
        PACKOF3GOBELINS(15, MonsterDickType.NORMAL),
        PACKOF10GOBELINS(50, MonsterDickType.NORMAL),
        TENTACLE(10, MonsterDickType.TENTACLE),
        WIGGLER(15, MonsterDickType.TENTACLE),
        FATTENTACLE(30, MonsterDickType.TENTACLE),
        FATWIGGLER(60, MonsterDickType.TENTACLE),
        MEATCATCHER(80, MonsterDickType.TENTACLE),
        OGRE(15, MonsterDickType.NORMAL),
        TWOOGRES(30, MonsterDickType.NORMAL),
        GIANT(20, MonsterDickType.NORMAL),
        TWOGIANTS(40, MonsterDickType.NORMAL),
        SLIME(7, MonsterDickType.FLUID),
        KINGSLIME(28, MonsterDickType.FLUID),
        BREEDERSLIME(56, MonsterDickType.FLUID),
        MINORAUR(45, MonsterDickType.NORMAL),
        REDMINOTAUR(90, MonsterDickType.NORMAL),
        WOLF(6, MonsterDickType.NORMAL),
        ALPHAWOLF(18, MonsterDickType.NORMAL);

        private int monsterRank;
        private MonsterDickType monsterDick;

        private monsterKind(int monsterRank, MonsterDickType monsterDick) {
            this.monsterRank = monsterRank;
            this.monsterDick = monsterDick;
        }
    }
}

