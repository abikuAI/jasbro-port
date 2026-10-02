/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.quests;

import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterGoal {
    private HashMap<AttributeType, Integer> attributeValueMap = new HashMap();
    private List<SpecializationType> specializations = new ArrayList<SpecializationType>();

    public boolean goalReached(Charakter character) {
        for (SpecializationType specialization : this.specializations) {
            if (character.getSpecializations().contains(specialization)) continue;
            return false;
        }
        for (AttributeType attributeType : this.attributeValueMap.keySet()) {
            if (character.getFinalValue(attributeType) >= this.attributeValueMap.get(attributeType)) continue;
            return false;
        }
        return true;
    }

    public HashMap<AttributeType, Integer> getAttributeValueMap() {
        return this.attributeValueMap;
    }

    public List<SpecializationType> getSpecializations() {
        return this.specializations;
    }

    public String getDescription() {
        String message = TextUtil.t("quest.requirements") + "\n";
        for (SpecializationType specializationType : this.getSpecializations()) {
            message = message + TextUtil.t("quest.requirementSpecialization") + " " + specializationType.getText() + "\n";
        }
        for (AttributeType attributeType : this.attributeValueMap.keySet()) {
            message = message + attributeType.getText() + ": " + this.attributeValueMap.get(attributeType) + " ";
        }
        message = message + "\n";
        return message;
    }
}

