/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Ritual
extends RunningActivity {
    private static final float OBEDIENCEMODIFICATION = 0.01f;
    private MessageData messageData;
    private String demon = "";

    public String getDemon() {
        return this.demon;
    }

    public void setDemon(String demon) {
        this.demon = demon;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.5f, SpecializationAttribute.MAGIC));
        return modifications;
    }

    @Override
    public void init() {
    }

    @Override
    public void perform() {
        block44: {
            block41: {
                Crypt room;
                block43: {
                    block42: {
                        room = (Crypt)this.getCharacterLocation();
                        this.setDemon(room.getDemonName());
                        if (room.getDemonName() == "nobody" || room.getRitualAdvancment() == 0) {
                            ArrayList options = new ArrayList();
                            options.add(new SelectionData<Integer>(0, TextUtil.t("crypt.selectdemon.communication")));
                            options.add(new SelectionData<Integer>(1, TextUtil.t("crypt.selectdemon.debauchery")));
                            options.add(new SelectionData<Integer>(2, TextUtil.t("crypt.selectdemon.trade")));
                            options.add(new SelectionData<Integer>(3, TextUtil.t("crypt.selectdemon.war")));
                            options.add(new SelectionData<Integer>(4, TextUtil.t("crypt.selectdemon.deception")));
                            options.add(new SelectionData<Integer>(5, TextUtil.t("crypt.selectdemon.parties")));
                            options.add(new SelectionData<Integer>(6, TextUtil.t("crypt.selectdemon.arcanes")));
                            Object selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()), null, this.getCharacters().get(0).getBackground(), TextUtil.t("crypt.selectdemon", this.getCharacters().get(0)));
                            Integer selected = (Integer)((SelectionData)selectedOption).getSelectionObject();
                            if (selected == 0) {
                                room.setDemonName("communication");
                            }
                            if (selected == 1) {
                                room.setDemonName("debauchery");
                            }
                            if (selected == 2) {
                                room.setDemonName("trade");
                            }
                            if (selected == 3) {
                                room.setDemonName("war");
                            }
                            if (selected == 4) {
                                room.setDemonName("deception");
                            }
                            if (selected == 5) {
                                room.setDemonName("parties");
                            }
                            if (selected == 6) {
                                room.setDemonName("arcanes");
                            }
                        }
                        room.setRitualAdvancment(room.getRitualAdvancment() + 1);
                        if (this.getCharacter().getTraits().contains(Trait.DARKRITUAL)) {
                            room.setRitualAdvancment(room.getRitualAdvancment() + 1);
                        }
                        if (room.getRitualAdvancment() < 13) break block41;
                        if (Util.getInt(0, 100) >= 66) break block42;
                        Object[] args = new Object[]{room.getDemonName()};
                        this.messageData.addToMessage("\n" + TextUtil.t("ritual.success", (Person)this.getCharacter(), args));
                        switch (room.getDemonName()) {
                            case "communication": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.communication", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.BARTENDING).addToValue(1.0f, true);
                                    character.getAttribute(SpecializationAttribute.ADVERTISING).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
                                }
                                break block43;
                            }
                            case "debauchery": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.debauchery", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.SEDUCTION).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.STAMINA).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.VAGINAL).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.ANAL).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.ORAL).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.TITFUCK).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.FOREPLAY).addToValue(1.0f, true);
                                    character.getAttribute(Sextype.GROUP).addToValue(1.0f, true);
                                    character.addCondition(new Buff.HornyBuff(character));
                                }
                                break block43;
                            }
                            case "trade": {
                                int amount = 0;
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    amount += character.getCharisma() + character.getObedience() + character.getCommand() + character.getStamina() + character.getIntelligence() + character.getStrength();
                                }
                                Object[] arg = new Object[]{amount *= Util.getInt(10, 15)};
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.trade", (Person)this.getCharacter(), arg));
                                Jasbro.getInstance().getData().earnMoney(amount, this);
                                break;
                            }
                            case "war": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.war", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.VETERAN).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(1.0f, true);
                                }
                                break block43;
                            }
                            case "deception": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.deception", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.PICKPOCKETING).addToValue(1.0f, true);
                                    character.getAttribute(SpecializationAttribute.AGILITY).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
                                }
                                break block43;
                            }
                            case "parties": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.parties", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.STRIP).addToValue(1.0f, true);
                                    character.getAttribute(SpecializationAttribute.BARTENDING).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
                                }
                                break block43;
                            }
                            case "arcanes": {
                                this.messageData.addToMessage("\n" + TextUtil.t("ritual.arcanes", this.getCharacter()));
                                for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                                    character.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).addToValue(1.0f, true);
                                    character.getAttribute(SpecializationAttribute.MAGIC).addToValue(1.0f, true);
                                    character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
                                }
                                break;
                            }
                        }
                        break block43;
                    }
                    if (!this.getCharacter().getTraits().contains(Trait.PERSONNALOFFERING)) {
                        this.messageData.addToMessage("\n" + TextUtil.t("ritual.failure.lose", this.getCharacter()));
                    } else {
                        this.messageData.addToMessage("\n" + TextUtil.t("ritual.failure.win", this.getCharacter()));
                    }
                    this.messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTER, this.getCharacter()));
                    ArrayList<Charakter> potentialVictims = new ArrayList<Charakter>();
                    for (Room room2 : this.getHouse().getRooms()) {
                        for (Charakter character2 : room2.getCurrentUsage().getCharacters()) {
                            potentialVictims.add(character2);
                        }
                    }
                    Charakter character = (Charakter)potentialVictims.get(Util.getInt(0, potentialVictims.size()));
                    if (character.getName() != this.getCharacter().getName() && Util.getInt(0, 100) < 70 && !this.getCharacter().getTraits().contains(Trait.PERSONNALOFFERING)) {
                        this.messageData.addToMessage("\n" + TextUtil.t("ritual.failure.othervictim", (Person)character, this.getCharacter()));
                        this.messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTER, character));
                        this.getAttributeModifications().add(new AttributeModification(1.07f, Sextype.MONSTER, character));
                        this.getAttributeModifications().add(new AttributeModification(-41.07f, EssentialAttributes.ENERGY, character));
                        this.getAttributeModifications().add(new AttributeModification(-11.07f, EssentialAttributes.HEALTH, character));
                    }
                    if (!this.getCharacter().getTraits().contains(Trait.PERSONNALOFFERING)) {
                        this.getAttributeModifications().add(new AttributeModification(1.07f, Sextype.MONSTER, this.getCharacter()));
                        this.getAttributeModifications().add(new AttributeModification(-41.07f, EssentialAttributes.ENERGY, this.getCharacter()));
                        this.getAttributeModifications().add(new AttributeModification(-11.07f, EssentialAttributes.HEALTH, this.getCharacter()));
                    } else {
                        this.getAttributeModifications().add(new AttributeModification(3.07f, Sextype.MONSTER, this.getCharacter()));
                        this.getAttributeModifications().add(new AttributeModification(-21.07f, EssentialAttributes.ENERGY, this.getCharacter()));
                        this.getCharacter().addCondition(new Buff.DarkGodSeed());
                    }
                }
                room.setRitualAdvancment(0);
                break block44;
            }
            this.messageData.addToMessage("\n" + TextUtil.t("ritual.advancment", this.getCharacter()));
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Crypt room = (Crypt)this.getCharacterLocation();
        Charakter character = this.getCharacters().get(0);
        Object[] argument = new Object[]{room.getDemonName()};
        String message = TextUtil.t("ritual.basic", (Person)character, argument);
        this.messageData = new MessageData(message, null, this.getBackground());
        this.messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter()));
        return this.messageData;
    }
}

