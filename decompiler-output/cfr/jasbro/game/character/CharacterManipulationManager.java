/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.PerkHandler;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class CharacterManipulationManager {
    public static boolean advanceAge(Charakter character) {
        if (character.getType() == CharacterType.INFANT) {
            CharacterManipulationManager.changeBase(character, CharacterManipulationManager.getChildBase(character));
            CharacterManipulationManager.changeType(character, CharacterType.CHILD);
        } else if (character.getType() == CharacterType.CHILD) {
            CharacterManipulationManager.changeBase(character, CharacterManipulationManager.getTeenagerBase(character));
            CharacterManipulationManager.changeType(character, CharacterType.TEENAGER);
        } else if (character.getType() == CharacterType.TEENAGER) {
            ArrayList options = new ArrayList();
            CharacterManipulationManager.changeBase(character, CharacterManipulationManager.getAdultBase(character));
            options.add(new SelectionData<CharacterType>(CharacterType.TRAINER, CharacterType.TRAINER.getText()));
            options.add(new SelectionData<CharacterType>(CharacterType.SLAVE, CharacterType.SLAVE.getText()));
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character), null, new ImageData("images/backgrounds/sky.jpg"), TextUtil.t("child.ofAge", character));
            CharacterType newType = (CharacterType)((Object)selectedOption.getSelectionObject());
            CharacterManipulationManager.changeType(character, newType);
            character.setOwnership(Ownership.OWNED);
        }
        return false;
    }

    public static boolean reduceAge(Charakter character) {
        return false;
    }

    public static boolean changeType(Charakter character, CharacterType newType) {
        if (character.getType() == newType) {
            return false;
        }
        character.setType(newType);
        character.removeSpecialization(SpecializationType.SLAVE);
        character.removeSpecialization(SpecializationType.TRAINER);
        character.removeSpecialization(SpecializationType.UNDERAGE);
        switch (newType) {
            case INFANT: 
            case CHILD: 
            case TEENAGER: {
                character.addSpecialization(SpecializationType.UNDERAGE);
                break;
            }
            case SLAVE: {
                character.addSpecialization(SpecializationType.SLAVE);
                if (character.getObedience() < 1) {
                    character.getAttribute(BaseAttributeTypes.OBEDIENCE).setInternValue(1.0f);
                }
                if (character.getSpecializations().contains(SpecializationType.SEX)) break;
                character.addSpecialization(SpecializationType.SEX);
                break;
            }
            case TRAINER: {
                character.addSpecialization(SpecializationType.TRAINER);
                if (character.getCommand() < 1) {
                    character.getAttribute(BaseAttributeTypes.COMMAND).setInternValue(1.0f);
                }
                if (character.getSpecializations().contains(SpecializationType.SEX)) break;
                character.addSpecialization(SpecializationType.SEX);
                break;
            }
        }
        PerkHandler.resetPerks(character);
        return true;
    }

    public static void changeBase(Charakter character, CharacterBase base) {
        character.setBase(base);
        character.setBaseId(base.getId());
        character.setIcon(null);
    }

    public static CharacterBase getInfantBase(Charakter character) {
        CharacterBase base2;
        String baseId = character.getAgeProgressionData().getInfantBase();
        if (baseId != null) {
            for (CharacterBase base2 : Jasbro.getInstance().getCharacterBases()) {
                if (!base2.getId().equals(baseId)) continue;
                return base2;
            }
        }
        List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.INFANT, character.getGender(), Jasbro.getInstance().getCharacterBases());
        base2 = options.get(Util.getInt(0, options.size()));
        character.getAgeProgressionData().setInfantBase(base2.getId());
        return base2;
    }

    public static CharacterBase getChildBase(Charakter character) {
        CharacterBase base2;
        String baseId = character.getAgeProgressionData().getChildBase();
        if (baseId != null) {
            for (CharacterBase base2 : Jasbro.getInstance().getCharacterBases()) {
                if (!base2.getId().equals(baseId)) continue;
                return base2;
            }
        }
        List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.CHILD, character.getGender(), Jasbro.getInstance().getCharacterBases());
        base2 = options.get(Util.getInt(0, options.size()));
        character.getAgeProgressionData().setChildBase(base2.getId());
        return base2;
    }

    public static CharacterBase getTeenagerBase(Charakter character) {
        CharacterBase base2;
        String baseId = character.getAgeProgressionData().getTeenagerBase();
        if (baseId != null) {
            for (CharacterBase base2 : Jasbro.getInstance().getCharacterBases()) {
                if (!base2.getId().equals(baseId)) continue;
                return base2;
            }
        }
        List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.TEENAGER, character.getGender(), Jasbro.getInstance().getCharacterBases());
        base2 = options.get(Util.getInt(0, options.size()));
        character.getAgeProgressionData().setTeenagerBase(base2.getId());
        return base2;
    }

    public static CharacterBase getAdultBase(Charakter character) {
        for (String baseId : character.getAgeProgressionData().getAdultBases()) {
            for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
                if (!base.getId().equals(baseId)) continue;
                return base;
            }
        }
        List<CharacterBase> options = Util.getBasesByTypeAndGender(null, character.getGender(), Jasbro.getInstance().getCharacterBases());
        CharacterBase base = options.get(Util.getInt(0, options.size()));
        character.getAgeProgressionData().getAdultBases().clear();
        character.getAgeProgressionData().getAdultBases().add(base.getId());
        return base;
    }
}

