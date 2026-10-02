/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game;

import jasbro.Jasbro;
import jasbro.game.DefaultPreferences;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.ControlData;
import jasbro.game.character.Ownership;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventManager;
import jasbro.game.events.EventType;
import jasbro.game.events.MoneyChangedEvent;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.items.Inventory;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.Unlocks;
import jasbro.game.world.locations.LocationType;
import jasbro.game.world.market.AuctionHouse;
import jasbro.game.world.market.QuestManager;
import jasbro.game.world.market.Shop;
import jasbro.game.world.market.SlaveMarket;
import jasbro.stats.StatCollector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="gamedata")
public class GameData
implements Serializable {
    private int day = 1;
    private Time time = Time.MORNING;
    private long money = 500L;
    private List<House> houses;
    private AuctionHouse auctionHouse;
    private SlaveMarket slaveMarket;
    private Shop shop;
    private Inventory inventory;
    private EventManager eventManager;
    private QuestManager questManager;
    private transient StatCollector statCollector;
    private List<Charakter> characters;
    private Charakter protagonist;
    private Map<LocationType, CharacterLocation> otherLocationMap;
    private DefaultPreferences defaultPreferences;
    private Unlocks unlocks;

    public void init() {
        this.houses = new ArrayList<House>();
        this.characters = new ArrayList<Charakter>();
        this.eventManager = new EventManager();
        this.otherLocationMap = new EnumMap<LocationType, CharacterLocation>(LocationType.class);
        this.questManager = new QuestManager();
        this.inventory = new Inventory();
        this.shop = new Shop();
    }

    public List<House> getHouses() {
        return this.houses;
    }

    public void setHouses(List<House> houses) {
        this.houses = houses;
    }

    public long getMoney() {
        return this.money;
    }

    public List<Charakter> getSlaves() {
        ArrayList<Charakter> slaveList = new ArrayList<Charakter>();
        for (Charakter character : this.getCharacters()) {
            if (character.getType() != CharacterType.SLAVE) continue;
            slaveList.add(character);
        }
        return slaveList;
    }

    public List<Charakter> getTrainers() {
        ArrayList<Charakter> trainerList = new ArrayList<Charakter>();
        for (Charakter character : this.getCharacters()) {
            if (character.getType() != CharacterType.TRAINER) continue;
            trainerList.add(character);
        }
        return trainerList;
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getTime() {
        return this.time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean canAfford(long price) {
        return price <= this.money;
    }

    public void earnMoney(long amount, Object source) {
        this.getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYEARNED, source, amount));
        this.money += amount;
        Jasbro.getInstance().getGui().updateStatus();
    }

    public void spendMoney(long amount, Object source) {
        this.getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYSPENT, source, amount));
        this.money -= amount;
        Jasbro.getInstance().getGui().updateStatus();
        if (this.money < 0L) {
            this.eventManager.handleEvent(new MyEvent(EventType.BROKE, null));
        }
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public List<Charakter> getCharacters() {
        if (this.characters == null) {
            this.characters = new ArrayList<Charakter>();
        }
        return this.characters;
    }

    public List<Charakter> getSlavesForSale() {
        ArrayList<Charakter> availableCharacters = new ArrayList<Charakter>();
        availableCharacters.addAll(this.getSlaves());
        for (int i = 0; i < availableCharacters.size(); ++i) {
            if (((Charakter)availableCharacters.get(i)).canSell()) continue;
            availableCharacters.remove(i);
            --i;
        }
        return availableCharacters;
    }

    public AuctionHouse getAuctionHouse() {
        if (this.auctionHouse == null) {
            this.auctionHouse = new AuctionHouse();
        }
        return this.auctionHouse;
    }

    public SlaveMarket getSlaveMarket() {
        if (this.slaveMarket == null) {
            this.slaveMarket = new SlaveMarket();
        }
        return this.slaveMarket;
    }

    public void setAuctionHouse(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
    }

    public void setSlaveMarket(SlaveMarket slaveMarket) {
        this.slaveMarket = slaveMarket;
    }

    public Map<LocationType, CharacterLocation> getOtherLocationMap() {
        return this.otherLocationMap;
    }

    public void setOtherLocationMap(Map<LocationType, CharacterLocation> otherLocationMap) {
        this.otherLocationMap = otherLocationMap;
    }

    public Collection<CharacterLocation> getOtherLocations() {
        return this.otherLocationMap.values();
    }

    public QuestManager getQuestManager() {
        if (this.questManager == null) {
            this.questManager = new QuestManager();
        }
        return this.questManager;
    }

    public StatCollector getStatCollector() {
        if (this.statCollector == null) {
            this.statCollector = new StatCollector();
            this.getEventManager().addListener(this.statCollector);
        }
        return this.statCollector;
    }

    public void setStatCollector(StatCollector statCollector) {
        this.statCollector = statCollector;
    }

    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }

    public DefaultPreferences getDefaultPreferences() {
        if (this.defaultPreferences == null) {
            this.defaultPreferences = new DefaultPreferences();
        }
        return this.defaultPreferences;
    }

    public Shop getShop() {
        if (this.shop == null) {
            this.shop = new Shop();
        }
        return this.shop;
    }

    public Inventory getInventory() {
        if (this.inventory == null) {
            this.inventory = new Inventory();
        }
        return this.inventory;
    }

    public Unlocks getUnlocks() {
        if (this.unlocks == null) {
            this.unlocks = new Unlocks();
        }
        return this.unlocks;
    }

    public Charakter getProtagonist() {
        if (this.protagonist == null) {
            Iterator<Charakter> i$ = this.getTrainers().iterator();
            if (i$.hasNext()) {
                Charakter character = i$.next();
                character.setOwnership(Ownership.OWNED);
                this.protagonist = character;
            }
            if (this.protagonist == null) {
                this.protagonist = this.getSlaves().get(0);
            }
        }
        return this.protagonist;
    }

    public void setProtagonist(Charakter protagonist) {
        protagonist.addSpecialization(SpecializationType.LEGACY);
        if (protagonist.getAttribute(SpecializationAttribute.EXPERIENCE).getValue() <= 50) {
            protagonist.getAttribute(SpecializationAttribute.EXPERIENCE).setInternValue(50.0f);
            protagonist.addBonusPerk();
        }
        this.protagonist = protagonist;
    }

    public ControlData calculateControl() {
        ControlData controlData = new ControlData();
        for (Charakter character : this.getCharacters()) {
            controlData.add(character.getControl());
        }
        return controlData;
    }
}

