/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relax
extends RunningActivity {
    private int actionType = 0;
    private Map<Charakter, relaxAction> characterAction = new HashMap<Charakter, relaxAction>();

    @Override
    public void init() {
        ArrayList<relaxAction> action = new ArrayList<relaxAction>();
        Charakter character = this.getCharacters().get(0);
        Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
        action.add(relaxAction.NAP);
        if ("POND".equals(this.getRoom().getRoomInfo().getId())) {
            action.add(relaxAction.DIP);
        }
        action.add(relaxAction.SING);
        action.add(relaxAction.SNACK);
        if (Jasbro.getInstance().getData().getTime() != Time.NIGHT) {
            action.add(relaxAction.TAN);
        }
        if (character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15) {
            action.add(relaxAction.NAILS);
        }
        if (character.getFinalValue(SpecializationAttribute.SEDUCTION) > 40 && this.getRoom().getAmountPeople() > 2) {
            action.add(relaxAction.NAILSEVERYONE);
        }
        if (character.getFinalValue(SpecializationAttribute.STRIP) > 15) {
            action.add(relaxAction.DANCE);
        }
        if (character.getFinalValue(SpecializationAttribute.STRIP) > 45 && this.getRoom().getAmountPeople() > 1) {
            action.add(relaxAction.DANCESHOW);
        }
        if (character.getFinalValue(SpecializationAttribute.CLEANING) > 15 || character.getTraits().contains(Trait.HELPFUL)) {
            action.add(relaxAction.CLEAN);
        }
        if ((character.getFinalValue(SpecializationAttribute.COOKING) > 40 || character.getTraits().contains(Trait.RESTAURATEUR)) && this.getRoom().getAmountPeople() > 2) {
            action.add(relaxAction.BARBECCUE);
        }
        if (character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > 15 || character.getTraits().contains(Trait.CLEVER)) {
            action.add(relaxAction.READ);
        }
        if ((character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > 15 || character.getTraits().contains(Trait.CLEVER)) && this.getRoom().getAmountPeople() > 2) {
            action.add(relaxAction.READEVERYONE);
        }
        if (character.getFinalValue(SpecializationAttribute.VETERAN) > 30) {
            action.add(relaxAction.FIGHT);
        }
        if (character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) > 25) {
            action.add(relaxAction.SMOKE);
        }
        if (servedToday < 2L && character.getTraits().contains(Trait.NYMPHO) || character.getTraits().contains(Trait.INSATIABLE)) {
            action.add(relaxAction.MASTURBATE);
        }
        if (servedToday > 7L) {
            action.add(relaxAction.COOLDOWN);
        }
        if (this.getRoom().getAmountPeople() > 1) {
            action.add(relaxAction.CHAT);
        }
        if ((character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 25 && character.getFinalValue(SpecializationAttribute.MAGIC) > 25 || character.getTraits().contains(Trait.ALTRUISTIC)) && this.getRoom().getAmountPeople() > 1) {
            action.add(relaxAction.NURSE);
        }
        this.characterAction.put(character, (relaxAction)((Object)action.get(Util.getInt(0, action.size()))));
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        List<Charakter> characters = this.getRoom().getCurrentUsage().getCharacters();
        String message = "";
        int a = Util.getInt(0, characters.size());
        message = TextUtil.t("relax.basic", character);
        message = message + "\n";
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
        for (Condition condition : character.getConditions()) {
            if (!(condition instanceof Buff) || ((Buff)condition).getNameKey() != "RoughenedUp") continue;
            character.removeCondition(condition);
        }
        switch (this.characterAction.get(character)) {
            case NAP: {
                message = message + TextUtil.t("relax.nap", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
                break;
            }
            case DIP: {
                message = message + TextUtil.t("relax.dip", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, character);
                break;
            }
            case SING: {
                message = message + TextUtil.t("relax.sing", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                break;
            }
            case SNACK: {
                message = message + TextUtil.t("relax.snack", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                break;
            }
            case TAN: {
                message = message + TextUtil.t("relax.tan", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, character);
                if (Util.getInt(0, 100) <= 60) break;
                if (!this.getCharacter().getTraits().contains(Trait.TANLINES)) {
                    boolean isTanned = false;
                    for (Condition condition : this.getCharacter().getConditions()) {
                        if (condition instanceof Buff.Tan && !this.getCharacter().getTraits().contains(Trait.SKINCARE)) {
                            this.getCharacter().removeCondition(condition);
                            this.getCharacter().addCondition(new Buff.Sunburn());
                            isTanned = true;
                            continue;
                        }
                        if (!(condition instanceof Buff.LightTan)) continue;
                        this.getCharacter().removeCondition(condition);
                        this.getCharacter().addCondition(new Buff.Tan());
                        isTanned = true;
                    }
                    isTanned = false;
                    if (false) {
                        this.getCharacter().addCondition(new Buff.LightTan());
                    }
                } else {
                    this.getCharacter().addCondition(new Buff.Tanlines());
                }
                message = message + "\n";
                message = message + TextUtil.t("relax.tanned", character);
                break;
            }
            case NAILS: {
                message = message + TextUtil.t("relax.nails", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                break;
            }
            case NAILSEVERYONE: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                while (characters.get(a = Util.getInt(0, characters.size())) == character) {
                }
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.CHARISMA, characters.get(a)));
                message = message + TextUtil.t("relax.nailseveryone", (Person)character, characters.get(a));
                break;
            }
            case DANCE: {
                message = message + TextUtil.t("relax.dance", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                break;
            }
            case DANCESHOW: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
                for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
                    if (target.getName() == character.getName()) continue;
                    this.getAttributeModifications().add(new AttributeModification(0.4f, SpecializationAttribute.STRIP, target));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, target));
                }
                message = message + TextUtil.t("relax.danceshow", character);
                break;
            }
            case CLEAN: {
                message = message + TextUtil.t("relax.clean", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);
                this.getHouse().modDirt(-15);
                break;
            }
            case BARBECCUE: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);
                for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
                    if (target == character) continue;
                    this.getAttributeModifications().add(new AttributeModification(10.1f, EssentialAttributes.HEALTH, target));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, target));
                    character.addCondition(new Buff.Satiated(30, character));
                }
                message = message + TextUtil.t("relax.barbeccue", character);
                break;
            }
            case READ: {
                message = message + TextUtil.t("relax.read", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, character);
                break;
            }
            case READEVERYONE: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
                for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
                    if (target.getName() == character.getName()) continue;
                    this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, target));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, target));
                }
                message = message + TextUtil.t("relax.readeveryone", character);
                break;
            }
            case FIGHT: {
                message = message + TextUtil.t("relax.fight", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, character);
                break;
            }
            case SMOKE: {
                message = message + TextUtil.t("relax.smoke", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
                character.addCondition(new Buff.Stoned(10, character, Util.getInt(-50, 50)));
                break;
            }
            case MASTURBATE: {
                message = message + TextUtil.t("relax.masturbate", character);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
                break;
            }
            case COOLDOWN: {
                Long servedToday = character.getCounter().get(CharacterStuffCounter.CounterNames.CUSTOMERSSERVEDTODAY.toString());
                Object[] arg = new Object[]{servedToday};
                message = message + TextUtil.t("relax.cooldown", character);
                message = servedToday > (long)character.getStamina() ? message + TextUtil.t("relax.cooldown.enough", (Person)character, arg) : message + TextUtil.t("relax.cooldown.more", (Person)character, arg);
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
                break;
            }
            case CHAT: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
                while (characters.get(a = Util.getInt(0, characters.size())).getName() == character.getName()) {
                }
                this.getAttributeModifications().add(new AttributeModification(0.1f, BaseAttributeTypes.INTELLIGENCE, characters.get(a)));
                this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, characters.get(a)));
                message = message + TextUtil.t("relax.chat", (Person)character, characters.get(a));
                break;
            }
            case NURSE: {
                image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, character);
                for (Charakter target : this.getRoom().getCurrentUsage().getCharacters()) {
                    if (target.getName() == character.getName()) continue;
                    this.getAttributeModifications().add(new AttributeModification(10.1f, EssentialAttributes.HEALTH, target));
                    this.getAttributeModifications().add(new AttributeModification(10.1f, EssentialAttributes.ENERGY, target));
                    this.getAttributeModifications().add(new AttributeModification(0.5f, EssentialAttributes.MOTIVATION, target));
                }
                message = message + TextUtil.t("relax.nurse", character);
            }
        }
        return new MessageData(message, image, this.getCharacter().getBackground());
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 40.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.MOTIVATION));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.HEALTH));
        switch (this.characterAction.get(this.getCharacter())) {
            case NAP: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.HEALTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case DIP: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.HEALTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case SNACK: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0f, EssentialAttributes.HEALTH));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case TAN: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
                break;
            }
            case NAILS: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
                break;
            }
            case NAILSEVERYONE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                break;
            }
            case DANCE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, SpecializationAttribute.STRIP));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case DANCESHOW: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, SpecializationAttribute.STRIP));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case CLEAN: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -5.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, SpecializationAttribute.CLEANING));
                break;
            }
            case BARBECCUE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.8f, SpecializationAttribute.COOKING));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case READ: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.15f, BaseAttributeTypes.INTELLIGENCE));
                break;
            }
            case READEVERYONE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case FIGHT: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -15.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.01f, SpecializationAttribute.VETERAN));
                break;
            }
            case MASTURBATE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, Sextype.FOREPLAY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case COOLDOWN: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 5.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
                break;
            }
            case NURSE: {
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -10.0f, EssentialAttributes.ENERGY));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, SpecializationAttribute.MEDICALKNOWLEDGE));
                modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, SpecializationAttribute.MAGIC));
            }
        }
        return modifications;
    }

    public int getactionType() {
        return this.actionType;
    }

    public void setactionType(int actionType) {
        this.actionType = actionType;
    }

    private static enum relaxAction {
        NAP,
        SNACK,
        SING,
        READ,
        READEVERYONE,
        MASTURBATE,
        COOLDOWN,
        DANCE,
        DANCESHOW,
        DIP,
        CAT,
        CLEAN,
        BARBECCUE,
        FIGHT,
        SMOKE,
        NAILS,
        NAILSEVERYONE,
        TAN,
        CHAT,
        NURSE;

    }
}

