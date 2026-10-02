package jasbro.game.events.business;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.housing.House;
import jasbro.texts.TextUtil;
import java.io.Serializable;

public class BuildingAdvertising implements Serializable {
   private boolean flyers = false;
   private boolean barkers = false;
   private boolean newspapers = false;
   private boolean posters = false;
   private boolean sponsorship = false;

   public void performAdvertising(House house) {
      SpawnData spawnData = house.getSpawnData();
      GameData gameData = Jasbro.getInstance().getData();
      if (this.flyers) {
         int price = 20;
         if (gameData.canAfford(price)) {
            spawnData.addBonusCustomers(1);
            gameData.spendMoney(price, TextUtil.t("flyers"));
         }
      }

      if (this.barkers) {
         int price = 100;
         if (gameData.canAfford(price)) {
            spawnData.addBonusCustomers(2);
            gameData.spendMoney(price, TextUtil.t("barkers"));
         }
      }

      if (this.posters) {
         int price = 500;
         if (gameData.canAfford(price)) {
            spawnData.addBonusCustomers(5);
            gameData.spendMoney(price, TextUtil.t("posters"));
         }
      }

      if (this.newspapers) {
         int price = 2000;
         if (gameData.canAfford(price)) {
            spawnData.addBonusCustomers(10);
            gameData.spendMoney(price, TextUtil.t("newspapers"));
         }
      }

      if (this.sponsorship) {
         int price = 10000;
         if (gameData.canAfford(price)) {
            spawnData.addBonusCustomers(15);
            gameData.spendMoney(price, TextUtil.t("sponsorship"));
         }
      }
   }

   public boolean isFlyers() {
      return this.flyers;
   }

   public void setFlyers(boolean flyers) {
      this.flyers = flyers;
   }

   public boolean isBarkers() {
      return this.barkers;
   }

   public void setBarkers(boolean barkers) {
      this.barkers = barkers;
   }

   public boolean isNewspapers() {
      return this.newspapers;
   }

   public void setNewspapers(boolean newspapers) {
      this.newspapers = newspapers;
   }

   public boolean isPosters() {
      return this.posters;
   }

   public void setPosters(boolean posters) {
      this.posters = posters;
   }

   public boolean isSponsorship() {
      return this.sponsorship;
   }

   public void setSponsorship(boolean sponsorship) {
      this.sponsorship = sponsorship;
   }
}
