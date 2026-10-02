/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Explore
extends RunningActivity {
    public List<ComplexEnemy> defeatedEnemies = new ArrayList<ComplexEnemy>();

    @Override
    public void init() {
    }

    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("explore.basic", this.getCharacter());
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacter());
        MessageData messageData = new MessageData(message, image, this.getCharacter().getBackground());
        return messageData;
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -30.0f, EssentialAttributes.ENERGY));
        if (this.getCharacter().getSpecializations().contains(SpecializationType.FIGHTER)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.1f, SpecializationAttribute.VETERAN));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.1f, SpecializationAttribute.VETERAN));
        }
        return modifications;
    }

    @Override
    public void perform() {
    }

    public EnemySpawnLocation getEnemySpawnLocation() {
        if (this.getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON1) {
            return EnemySpawnLocation.DUNGEON1;
        }
        if (this.getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON2) {
            return EnemySpawnLocation.DUNGEON2;
        }
        if (this.getPlannedActivity().getSource().getLocationType() == LocationType.DUNGEON3) {
            return EnemySpawnLocation.DUNGEON3;
        }
        return EnemySpawnLocation.DUNGEON4;
    }

    public List<ComplexEnemy> getDefeatedEnemies() {
        return this.defeatedEnemies;
    }

    public void setDefeatedEnemies(List<ComplexEnemy> defeatedEnemies) {
        this.defeatedEnemies = defeatedEnemies;
    }
}

