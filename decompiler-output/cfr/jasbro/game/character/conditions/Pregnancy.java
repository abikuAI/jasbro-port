/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Pregnancy
extends Condition
implements PregnancyInterface {
    private static final Logger log = LogManager.getLogger(Pregnancy.class);
    private int days = 30;
    private List<Charakter> children = new ArrayList<Charakter>();
    private Charakter mother;
    private String father;

    public Pregnancy() {
    }

    public Pregnancy(Charakter mother, Person otherParent, MyEvent event) {
        this(mother, otherParent, event, true);
    }

    public Pregnancy(Charakter mother, Person otherParent, MyEvent event, boolean message) {
        int i;
        this.mother = mother;
        if (otherParent != null) {
            this.father = otherParent.getName();
        }
        int minChildren = mother.getMinChildren();
        int maxChildren = mother.getMaxChildren();
        int chanceAdditionalChild = mother.getChanceAdditionalChild();
        if (otherParent instanceof Charakter) {
            Charakter otherParentCharakter = (Charakter)otherParent;
            minChildren = (minChildren + otherParentCharakter.getMinChildren()) / 2;
            maxChildren = (maxChildren + otherParentCharakter.getMaxChildren()) / 2;
            chanceAdditionalChild = (chanceAdditionalChild + otherParentCharakter.getChanceAdditionalChild()) / 2;
            this.days = 60;
        } else {
            this.days = 60;
        }
        if (mother.getTraits().contains(Trait.HEARTOFTHESWARM)) {
            this.days = 30;
        }
        if (minChildren < 1) {
            minChildren = 1;
        }
        for (i = 0; i < minChildren; ++i) {
            this.children.add(CharacterSpawner.spawnChild(mother, otherParent));
        }
        for (i = minChildren; i < maxChildren && Util.getInt(0, 100) < chanceAdditionalChild; ++i) {
            this.children.add(CharacterSpawner.spawnChild(mother, otherParent));
        }
        if (message) {
            MessageData messageData = new MessageData();
            messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, mother));
            messageData.addToMessage(TextUtil.t("pregnancy.pregnant", (Person)mother, otherParent));
            if (otherParent instanceof Charakter) {
                messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, (Charakter)otherParent));
            }
            messageData.setBackground(mother.getBackground());
            messageData.setPriorityMessage(true);
            GuiUtil.addMessageToEvent(messageData, event);
        }
        mother.addCondition(new ItemCooldown(7, "Elixir_of_growth"));
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY) {
            --this.days;
            if (this.days <= 0) {
                this.getCharacter().getConditions().remove(this);
                Jasbro.getInstance().getData().getCharacters().addAll(this.children);
                MessageData messageData = new MessageData();
                Object[] parameters = new Object[]{TextUtil.listCharacters(this.children)};
                messageData.addToMessage(TextUtil.t("pregnancy.birth", (Person)this.mother, parameters));
                messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, this.mother));
                this.mother.getCounter().add(CharacterStuffCounter.CounterNames.CHILDREN, Long.valueOf(this.children.size()));
                try {
                    if (this.children.get(0).getAgeProgressionData().getFather() != null) {
                        Charakter father = (Charakter)this.children.get(0).getAgeProgressionData().getFather().get();
                        father.getCounter().add(CharacterStuffCounter.CounterNames.CHILDREN, Long.valueOf(this.children.size()));
                        messageData.addToMessage(TextUtil.t("pregnancy.fatherKnown", father));
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, father));
                    } else if (this.father != null) {
                        parameters[0] = this.children.get(0).getAgeProgressionData().getNameFather();
                        if (this.children.size() < 2) {
                            messageData.addToMessage(TextUtil.t("pregnancy.fatherUnknown.singleChild", (Person)this.children.get(0), parameters));
                        } else {
                            messageData.addToMessage(TextUtil.t("pregnancy.fatherUnknown.children", parameters));
                        }
                    } else if (this.children.size() < 2) {
                        messageData.addToMessage(TextUtil.t("pregnancy.nofather.singleChild", this.children.get(0)));
                    } else {
                        messageData.addToMessage(TextUtil.t("pregnancy.nofather.children", parameters));
                    }
                }
                catch (Exception ex) {
                    log.error("Error when creating children message", (Throwable)ex);
                }
                for (Charakter child : this.children) {
                    messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, child));
                }
                messageData.setBackground(this.mother.getBackground());
                messageData.setPriorityMessage(true);
                GuiUtil.addMessageToEvent(messageData, e);
            }
        }
    }

    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/pregnant.png");
    }

    @Override
    public int getDays() {
        return this.days;
    }

    public List<Charakter> getChildren() {
        return this.children;
    }

    @Override
    public void reduceDays(int amount) {
        this.days -= amount;
    }

    @Override
    public String getName() {
        return TextUtil.t("pregnancy", this.mother);
    }

    @Override
    public String getDescription() {
        return this.getName() + "\n" + TextUtil.t("pregnancy.description", this.getMonthsRemaining(), this.father);
    }

    public int getMonthsRemaining() {
        return Math.max(0, this.days / 30);
    }

    @Override
    public void modifyDays(int amount) {
        this.days += amount;
    }

    public Charakter getMother() {
        return this.mother;
    }

    public void setMother(Charakter mother) {
        this.mother = mother;
    }

    public String getFather() {
        return this.father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    @Override
    public void modifyImageTags(List<ImageTag> imageTags) {
        if (this.days < 180) {
            imageTags.add(ImageTag.PREGNANT);
        }
    }
}

