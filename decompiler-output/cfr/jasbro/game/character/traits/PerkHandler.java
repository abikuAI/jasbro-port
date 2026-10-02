/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.SkillTreeItem;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerkHandler
implements Serializable {
    public static List<SkillTree> getSkillTrees(Charakter character) {
        ArrayList<SkillTree> skillTrees = new ArrayList<SkillTree>();
        for (SpecializationType specializationType : character.getSpecializations()) {
            if (specializationType.getAssociatedSkillTree() == null) continue;
            skillTrees.add(specializationType.getAssociatedSkillTree());
        }
        for (Trait trait : character.getTraits()) {
            if (trait.getAssociatedSkillTree() == null) continue;
            skillTrees.add(trait.getAssociatedSkillTree());
        }
        return skillTrees;
    }

    public static int getSkillPoints(Charakter character) {
        int amount = character.getBonusPerks();
        for (SpecializationType specializationType : character.getSpecializations()) {
            List<AttributeType> attributeTypes = specializationType.getAssociatedAttributes();
            if (attributeTypes.size() <= 0) continue;
            float sum = 0.0f;
            for (AttributeType attributeType : attributeTypes) {
                if (attributeType instanceof BaseAttributeTypes) {
                    sum += (float)((int)character.getAttribute(attributeType).getInternValue());
                    continue;
                }
                sum += character.getAttribute(attributeType).getInternValue();
            }
            if (specializationType != SpecializationType.SLAVE && specializationType != SpecializationType.TRAINER) {
                amount += (int)(sum / (float)attributeTypes.size() / 10.0f);
                continue;
            }
            amount += (int)(sum / 15.0f);
        }
        return amount;
    }

    public static int getUsedSkillPoints(Charakter character) {
        int amount = 0;
        for (Trait trait : character.getTraitsInternal()) {
            if (!trait.isPerk()) continue;
            ++amount;
        }
        return amount;
    }

    public static int getLevel(SkillTreeItem skillTreeItem, SkillTree skillTree) {
        int level = 1;
        while (skillTreeItem.getParentItems().size() != 0) {
            skillTreeItem = skillTreeItem.getParentItems().get(0);
            ++level;
        }
        return level;
    }

    public static int requiredSkill(SkillTreeItem skillTreeItem, SkillTree skillTree) {
        return (PerkHandler.getLevel(skillTreeItem, skillTree) - 1) * 10;
    }

    public static int getBaseRequirement(SkillTreeItem skillTreeItem, SkillTree skillTree, Charakter character) {
        int level = PerkHandler.getLevel(skillTreeItem, skillTree);
        int requirement = skillTree != SkillTree.TRAINER && skillTree != SkillTree.SLAVE ? (level - 1) * 10 : (level - 1) * 10;
        ArrayList<SkillTreeItem> items = new ArrayList<SkillTreeItem>();
        items.add(skillTree.getFirstItem());
        if (level > 1) {
            for (int i = 0; i < level - 1; ++i) {
                ArrayList<SkillTreeItem> nextItems = new ArrayList<SkillTreeItem>();
                for (SkillTreeItem curSkillTreeItem : items) {
                    nextItems.addAll(curSkillTreeItem.getNextItems());
                }
                items = nextItems;
            }
            List<Trait> traits = character.getTraits();
            for (SkillTreeItem item : items) {
                if (!traits.contains(item.getPerk())) continue;
                if (skillTree == SkillTree.LEGACY) {
                    requirement = requirement * 110 / 100;
                    break;
                }
                requirement *= 2;
                break;
            }
        }
        return requirement;
    }

    public static SpecializationType getConnectedSpecializationType(SkillTree skillTree) {
        SpecializationType specializationType = null;
        for (SpecializationType curSpecializationType : SpecializationType.values()) {
            if (!curSpecializationType.toString().equals(skillTree.toString())) continue;
            specializationType = curSpecializationType;
            break;
        }
        return specializationType;
    }

    public static void resetPerks(Charakter character) {
        ArrayList<Trait> traits = new ArrayList<Trait>(character.getTraits());
        for (Trait trait : traits) {
            if (!trait.isPerk()) continue;
            character.removeTrait(trait);
        }
    }
}

