package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Garden;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public class Harvest extends RunningActivity {
   private static final float BASEMODIFICATION = 1.0F;
   private static final float OBEDIENCEMODIFICATION = 0.01F;
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
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      float modification = 1.0F;
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SLAVE, 0.01F, BaseAttributeTypes.OBEDIENCE));
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      return modifications;
   }

   @Override
   public void init() {
      this.efficiency = this.efficiency + this.getCharacters().get(0).getIntelligence();
   }

   @Override
   public void perform() {
   }

   @Override
   public MessageData getBaseMessage() {
      Charakter character = this.getCharacters().get(0);
      ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);
      Garden room = (Garden)this.getCharacterLocation();
      Item item = Jasbro.getInstance().getItems().get("Daisies");
      String itemString = "";
      if (room.getPlant() == Garden.Plant.FLOWERS) {
         if (room.getQuality() < 30) {
            this.amount = room.getGrowth() / 5;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Daisies");
            itemString = "bouquets of daisies";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else if (room.getQuality() < 60) {
            this.amount = room.getGrowth() / 10;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Tulips");
            itemString = "bouquets of tulips";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else if (room.getQuality() < 90) {
            this.amount = room.getGrowth() / 15;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Roses");
            itemString = "bouquets of roses";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 20;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Orchids");
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

            item = Jasbro.getInstance().getItems().get("Catnip");
            itemString = "boxes of catnip";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 14;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Strong Catnip");
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

            item = Jasbro.getInstance().getItems().get("Weed");
            itemString = "patches of weed";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 30;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("High Quality Weed");
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

            item = Jasbro.getInstance().getItems().get("Cherry");
            itemString = "box of cherries";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 18;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Cherrypop");
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

            item = Jasbro.getInstance().getItems().get("Berries");
            itemString = "bags of berries";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 20;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Plant_Cumberry");
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

            item = Jasbro.getInstance().getItems().get("Orange");
            itemString = "oranges";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 15;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("AnnoyingOrange");
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

            item = Jasbro.getInstance().getItems().get("Peach");
            itemString = "peaches";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 24;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("RoundPeach");
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

            item = Jasbro.getInstance().getItems().get("Vines");
            itemString = "bundles of weird vines.";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 40;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Plant_TentacleVine");
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

            item = Jasbro.getInstance().getItems().get("Mushroom");
            itemString = "shrooms";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         } else {
            this.amount = room.getGrowth() / 30;
            if (character.getTraits().contains(Trait.FLOWERARRANGEMENT)) {
               this.amount *= 2;
            }

            item = Jasbro.getInstance().getItems().get("Plant_Penishroom");
            itemString = "penishrooms";
            Jasbro.getInstance().getData().getInventory().addItems(item, this.amount);
         }
      }

      Object[] arguments = new Object[]{this.amount, itemString};
      String message = "";
      if (this.amount > 0 && room.getPlant() != Garden.Plant.GRASS) {
         message = TextUtil.t("harvest.basic", character, arguments);
      } else if (room.getPlant() == Garden.Plant.GRASS) {
         message = TextUtil.t("harvest.grass", character);
      } else {
         message = TextUtil.t("harvest.none", character, arguments);
      }

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
