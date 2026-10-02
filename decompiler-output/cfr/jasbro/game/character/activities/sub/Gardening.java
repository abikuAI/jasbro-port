/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Garden;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Gardening
extends RunningActivity {
    private static final float OBEDIENCEMODIFICATION = 0.01f;
    private int efficiency = 3;

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0f, SpecializationAttribute.PLANTKNOWLEDGE));
        if (this.getCharacters().get(0).getTraits().contains(Trait.GREENTHUMB)) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.7f, EssentialAttributes.MOTIVATION));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
        }
        return modifications;
    }

    @Override
    public void init() {
    }

    @Override
    public void perform() {
        Garden room = (Garden)this.getCharacterLocation();
        this.efficiency += this.getCharacters().get(0).getIntelligence() / 2 + this.getCharacters().get(0).getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE) / 20;
        if (room.getPlant() == Garden.Plant.FLOWERS) {
            this.efficiency *= 1;
        }
        if (room.getPlant() == Garden.Plant.CATNIP) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.WEED) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.BERRIES) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.CHERRIES) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.ORANGES) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.PEACHES) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.SHROOMS) {
            this.efficiency *= 0;
        }
        if (room.getPlant() == Garden.Plant.VINES) {
            this.efficiency *= 0;
        }
        if (room.getGrowth() != 0) {
            room.setGrowth(room.getGrowth() + this.efficiency + 1);
            room.setQuality(room.getQuality() + this.efficiency / 2);
        }
        if (room.getGrowth() == 0) {
            ArrayList options = new ArrayList();
            options.add(new SelectionData<Integer>(0, TextUtil.t("gardening.plant.flowers")));
            options.add(new SelectionData<Integer>(1, TextUtil.t("gardening.plant.cherries")));
            options.add(new SelectionData<Integer>(2, TextUtil.t("gardening.plant.berries")));
            options.add(new SelectionData<Integer>(3, TextUtil.t("gardening.plant.catnip")));
            options.add(new SelectionData<Integer>(4, TextUtil.t("gardening.plant.oranges")));
            options.add(new SelectionData<Integer>(5, TextUtil.t("gardening.plant.shrooms")));
            options.add(new SelectionData<Integer>(6, TextUtil.t("gardening.plant.peaches")));
            options.add(new SelectionData<Integer>(7, TextUtil.t("gardening.plant.vines")));
            options.add(new SelectionData<Integer>(8, TextUtil.t("gardening.plant.weed")));
            SelectionData selectedOption = new SelectionScreen().select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.getCharacters().get(0)), null, this.getCharacters().get(0).getBackground(), TextUtil.t("gardening.plant.select", this.getCharacters().get(0)));
            Integer selected = (Integer)selectedOption.getSelectionObject();
            if (selected == 0) {
                room.setPlant(Garden.Plant.FLOWERS);
                room.setGrowth(10);
                room.setPlantName("Flowers");
            }
            if (selected == 1) {
                room.setPlant(Garden.Plant.CHERRIES);
                room.setGrowth(10);
                room.setPlantName("Cherries");
            }
            if (selected == 2) {
                room.setPlant(Garden.Plant.BERRIES);
                room.setGrowth(10);
                room.setPlantName("Berries");
            }
            if (selected == 3) {
                room.setPlant(Garden.Plant.CATNIP);
                room.setGrowth(10);
                room.setPlantName("Catnip");
            }
            if (selected == 4) {
                room.setPlant(Garden.Plant.ORANGES);
                room.setGrowth(10);
                room.setPlantName("Oranges");
            }
            if (selected == 5) {
                room.setPlant(Garden.Plant.SHROOMS);
                room.setGrowth(10);
                room.setPlantName("Shrooms");
            }
            if (selected == 6) {
                room.setPlant(Garden.Plant.PEACHES);
                room.setGrowth(10);
                room.setPlantName("Peaches");
            }
            if (selected == 7) {
                room.setPlant(Garden.Plant.VINES);
                room.setGrowth(10);
                room.setPlantName("Vines");
            }
            if (selected == 8) {
                room.setPlant(Garden.Plant.WEED);
                room.setGrowth(10);
                room.setPlantName("Weed");
            }
            System.out.println((Object)room.getPlant());
        }
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
        String message = TextUtil.t("garden.basic", character);
        return new MessageData(message, image, null);
    }

    public int getEfficiency() {
        return this.efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }
}

