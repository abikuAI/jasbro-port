/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Garden;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Harvest
extends RunningActivity {
    private static final float BASEMODIFICATION = 1.0f;
    private static final float OBEDIENCEMODIFICATION = 0.01f;
    private int efficiency = 1;
    private int amount = 0;
    private static final String daisies = "Daisies";
    private static final String tulips = "Tulips";
    private static final String roses = "Roses";
    private static final String orchids = "Orchids";
    private static final String catnip = "Catnip";
    private static final String strongCatnip = "Strong Catnip";
    private static final String weed = "Weed";
    private static final String hqWeed = "High Quality Weed";
    private static final String peach = "Peach";
    private static final String hqpeach = "RoundPeach";
    private static final String orange = "Orange";
    private static final String hqorange = "AnnoyingOrange";
    private static final String cherry = "Cherry";
    private static final String hqcherry = "Cherrypop";
    private static final String berry = "Berries";
    private static final String hqberry = "Plant_Cumberry";
    private static final String shroom = "Mushroom";
    private static final String hqshroom = "Plant_Penishroom";
    private static final String vine = "Vines";
    private static final String hqvine = "Plant_TentacleVine";

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        float modification = 1.0f;
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0f, EssentialAttributes.ENERGY));
        return modifications;
    }

    @Override
    public void init() {
        this.efficiency += this.getCharacters().get(0).getIntelligence();
    }

    @Override
    public void perform() {
    }

    @Override
    public MessageData getBaseMessage() {
        Charakter character = this.getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
        Garden room = (Garden)this.getCharacterLocation();
        Item item = Jasbro.getInstance().getItems().get(daisies);
        String itemString = "";
        if (room.getPlant() == Garden.Plant.FLOWERS) {
            if (room.getQuality() < 30) {
                this.amount = room.getGrowth() / 5;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(daisies);
                itemString = "bouquets of daisies";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else if (room.getQuality() < 60) {
                this.amount = room.getGrowth() / 10;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(tulips);
                itemString = "bouquets of tulips";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else if (room.getQuality() < 90) {
                this.amount = room.getGrowth() / 15;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(roses);
                itemString = "bouquets of roses";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 20;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(orchids);
                itemString = "bouquets of orchids";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.CATNIP) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 7;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(catnip);
                itemString = "boxes of catnip";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 14;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(strongCatnip);
                itemString = "boxes of strong catnip";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.WEED) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 20;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(weed);
                itemString = "patches of weed";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 30;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqWeed);
                itemString = "patchers of high quality weed";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.CHERRIES) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 16;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(cherry);
                itemString = "box of cherries";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 18;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqcherry);
                itemString = "boxes of cherrypops";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.BERRIES) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 15;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(berry);
                itemString = "bags of berries";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 20;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqberry);
                itemString = "bags of cumberries";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.ORANGES) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 5;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(orange);
                itemString = "oranges";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 15;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqorange);
                itemString = "annoying oranges";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.PEACHES) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 18;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(peach);
                itemString = "peaches";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 24;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqpeach);
                itemString = "deliciously round peaches.";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.VINES) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 30;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(vine);
                itemString = "bundles of weird vines.";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 40;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqvine);
                itemString = "wiggly bundles of tentacle vines";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        if (room.getPlant() == Garden.Plant.SHROOMS) {
            if (room.getQuality() < 50) {
                this.amount = room.getGrowth() / 20;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(shroom);
                itemString = "shrooms";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            } else {
                this.amount = room.getGrowth() / 30;
                if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
                    this.amount *= 2;
                }
                item = Jasbro.getInstance().getItems().get(hqshroom);
                itemString = "penishrooms";
                Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
            }
        }
        Object[] arguments = new Object[]{this.amount, itemString};
        String message = "";
        message = this.amount > 0 && room.getPlant() != Garden.Plant.GRASS ? TextUtil.t("harvest.basic", (Person)character, arguments) : (room.getPlant() == Garden.Plant.GRASS ? TextUtil.t("harvest.grass", character) : TextUtil.t("harvest.none", (Person)character, arguments));
        room.setGrowth(0);
        room.setPlant(Garden.Plant.GRASS);
        room.setPlantName("Grass");
        return new MessageData(message, image, null);
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public void setEfficiencyn(int efficiency) {
        this.efficiency = efficiency;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

