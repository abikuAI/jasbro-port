/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.business;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.events.business.SpawnData;
import jasbro.game.housing.House;
import jasbro.texts.TextUtil;
import java.io.Serializable;

public class BuildingAdvertising
implements Serializable {
    private boolean flyers = false;
    private boolean barkers = false;
    private boolean newspapers = false;
    private boolean posters = false;
    private boolean sponsorship = false;

    public void performAdvertising(House house) {
        int price;
        SpawnData spawnData = house.getSpawnData();
        GameData gameData = Jasbro.getInstance().getData();
        if (this.flyers && gameData.canAfford(price = 20)) {
            spawnData.addBonusCustomers(1);
            gameData.spendMoney(price, TextUtil.t("flyers"));
        }
        if (this.barkers && gameData.canAfford(price = 100)) {
            spawnData.addBonusCustomers(2);
            gameData.spendMoney(price, TextUtil.t("barkers"));
        }
        if (this.posters && gameData.canAfford(price = 500)) {
            spawnData.addBonusCustomers(5);
            gameData.spendMoney(price, TextUtil.t("posters"));
        }
        if (this.newspapers && gameData.canAfford(price = 2000)) {
            spawnData.addBonusCustomers(10);
            gameData.spendMoney(price, TextUtil.t("newspapers"));
        }
        if (this.sponsorship && gameData.canAfford(price = 10000)) {
            spawnData.addBonusCustomers(15);
            gameData.spendMoney(price, TextUtil.t("sponsorship"));
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

