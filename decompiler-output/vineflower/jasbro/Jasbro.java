package jasbro;

import bsh.EvalError;
import bsh.Interpreter;
import jasbro.game.GameData;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterFileLoader;
import jasbro.game.character.CharacterManipulationManager;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.StartingAtTheBottom;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemFileLoader;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.NpcFileLoader;
import jasbro.gui.CharacterFilterListModel;
import jasbro.gui.RPGView;
import jasbro.gui.pages.ManagementScreen;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import jasbro.util.Comparators;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Jasbro {
   private static final Logger log = LogManager.getLogger(Jasbro.class);
   private static Jasbro instance;
   private static final ExecutorService threadPool = Executors.newCachedThreadPool();
   private RPGView gui;
   private GameData data;
   private List<CharacterBase> bases;
   private Map<String, Item> items;
   private Map<String, CustomQuestTemplate> customQuestTemplates;
   private Map<String, WorldEvent> worldEvents;
   private Map<String, ComplexEnemyTemplate> enemyTemplates;
   private Interpreter interpreter;
   public static int maxTrees = 100;

   public Jasbro() {
      instance = this;
   }

   protected void startup() {
      Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
         @Override
         public void uncaughtException(Thread t, Throwable e) {
            Jasbro.log.error("Uncaught Exception", e);
         }
      });
      this.gui = new RPGView();
      this.gui.setVisible(true);
      this.gui.showMainMenu();
      maxTrees = ConfigHandler.getSetting(Settings.TREES, 100);
   }

   public static Jasbro getInstance() {
      if (instance == null) {
         instance = new Jasbro();
      }

      return instance;
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               ConfigHandler.loadConfig();
               if (ConfigHandler.isUseSystemLookAndFeel()) {
                  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               }

               new Jasbro().startup();
            } catch (Exception e) {
               Jasbro.log.error("Error on startup", e);
            }
         }
      });
   }

   public synchronized void save(File file) {
      new SaveAndLoadPerformer().save(file, this.getData());
   }

   public void continueLastGame() {
      File[] files = new File(".").listFiles();
      File lastSave = null;

      for (File file : files) {
         if (file.getName().matches("save\\-?\\d+\\.xml") && (lastSave == null || lastSave.lastModified() < file.lastModified())) {
            lastSave = file;
         }
      }

      if (lastSave != null) {
         try {
            this.load(lastSave);
         } catch (IOException e) {
            log.error("Failed to load last save file", e);
         }
      }
   }

   public synchronized void load(File selectedFile) throws IOException {
      this.data = new SaveAndLoadPerformer().load(selectedFile);
      List<Charakter> characters = new ArrayList<>();
      characters.addAll(this.data.getCharacters());
      characters.addAll(this.data.getAuctionHouse().getSlaves());
      List<CharacterBase> bases = this.getCharacterBases();

      for (Charakter character : characters) {
         for (CharacterBase base : bases) {
            if (character.getBaseId().equals(base.getId())) {
               character.setBase(base);
               break;
            }
         }
      }

      for (Charakter character : characters) {
         if (character.getBase() == null) {
            if (!log.isDebugEnabled()) {
               log.error("Character base not found, character removed {}", new Object[]{character.getBaseId()});
               this.removeCharacter(character);
            } else {
               while (true) {
                  character.setBase(bases.get(Util.getInt(0, bases.size())));
                  if (character.getBase().getType() == null || character.getBase().getType().isChildType() == character.getType().isChildType()) {
                     break;
                  }
               }
            }
         }
      }

      this.getData().getUnlocks().init();
      this.gui.setFilteredModel(new CharacterFilterListModel());
      this.gui.removeAllLayers();
      this.gui.addLayer(new ManagementScreen());
      this.gui.repaint();
   }

   public GameData getData() {
      return this.data;
   }

   public RPGView getGui() {
      return this.gui;
   }

   public void setGui(RPGView view) {
      this.gui = view;
   }

   public void advanceShift() {
      this.gui.removeAllLayers();
      System.gc();
      if (log.isDebugEnabled()) {
         log.error(
            "Before shift: Current memory(mb): {} Max memory: {}",
            new Object[]{Runtime.getRuntime().totalMemory() / 1024L / 1024L, Runtime.getRuntime().maxMemory() / 1024L / 1024L}
         );
      }

      try {
         Thread.currentThread().setPriority(10);
         GameData gameData = this.getData();
         StatCollector statCollector = gameData.getStatCollector();
         gameData.getEventManager().performShift(this.getData());
         Thread.sleep(200L);
         statCollector.setMoneyAfterShift(gameData.getMoney());
         statCollector.showStatScreen(gameData.getTime().getPreviousTimeOfDay(), true);
         Thread.currentThread().setPriority(5);
      } catch (Exception e) {
         log.error("Error on performing shift", e);
      }

      this.gui.setFilteredModel(new CharacterFilterListModel());
      this.gui.addLayerBottom(new ManagementScreen());
      this.gui.validate();
      this.gui.repaint();
      System.gc();
      if (log.isDebugEnabled()) {
         log.error(
            "After shift: Current memory(mb): "
               + Runtime.getRuntime().totalMemory() / 1024L / 1024L
               + " Max memory: "
               + Runtime.getRuntime().maxMemory() / 1024L / 1024L
         );
      }
   }

   public void advanceDay() {
      this.gui.removeAllLayers();
      System.gc();

      try {
         GameData gameData = this.getData();
         Thread.currentThread().setPriority(10);

         do {
            gameData.getEventManager().performShift(this.getData());
            Thread.sleep(200L);
            gameData.getStatCollector().setMoneyAfterShift(gameData.getMoney());
            if (gameData.getTime().isNewDay()) {
               gameData.getStatCollector().showStatScreen(null, true);
            } else {
               gameData.getStatCollector().showStatScreen(gameData.getTime().getPreviousTimeOfDay(), false);
            }
         } while (!gameData.getTime().isNewDay());

         Thread.currentThread().setPriority(5);
      } catch (Exception e) {
         log.error("Error on performing shift", e);
      }

      this.gui.setFilteredModel(new CharacterFilterListModel());
      this.gui.addLayerBottom(new ManagementScreen());
      this.gui.validate();
      this.gui.repaint();
      System.gc();
      if (log.isDebugEnabled()) {
         System.out
            .println(
               "After day: Current memory(mb): "
                  + Runtime.getRuntime().totalMemory() / 1024L / 1024L
                  + " Max memory: "
                  + Runtime.getRuntime().maxMemory() / 1024L / 1024L
            );
      }
   }

   public void startNewGame(CharacterBase trainerBase, CharacterBase firstSlave) {
      this.data = new GameData();
      this.data.init();
      Charakter trainer = CharacterSpawner.create(trainerBase, CharacterType.TRAINER);
      this.data.getCharacters().add(trainer);
      if (trainer == this.data.getProtagonist()) {
         trainer.addSpecialization(SpecializationType.LEGACY);
         if (trainer.getAttribute(SpecializationAttribute.EXPERIENCE).getValue() <= 10) {
            trainer.getAttribute(SpecializationAttribute.EXPERIENCE).setInternValue(10.0F);
            trainer.addBonusPerk();
         }
      }

      trainer.setOwnership(Ownership.OWNED);
      trainer.addTrait(Trait.CERTIFIEDTRAINER);
      trainer.addCondition(new StartingAtTheBottom());
      trainer.getAttribute(EssentialAttributes.MOTIVATION).setInternValue(65.0F);
      Charakter slave = CharacterSpawner.create(firstSlave, CharacterType.SLAVE);
      this.data.getCharacters().add(slave);
      slave.setOwnership(Ownership.OWNED);
      slave.getAttribute(EssentialAttributes.MOTIVATION).setInternValue(65.0F);
      House house = HouseUtil.newHouse(HouseType.HUT);
      this.getData().getHouses().add(house);
      this.getData().getUnlocks().init();
      this.gui.showHouseManagementScreen();
      this.getData().getEventManager().notifyAll(new MyEvent(EventType.GAMESTART, null));
   }

   public synchronized List<CharacterBase> getCharacterBases() {
      if (this.bases == null) {
         this.loadBases();
         Collections.sort(this.bases, new Comparators.CharacterBaseNameComparator());
         Collections.sort(this.bases, new Comparators.CharacterTypeComparator());
      }

      return this.bases;
   }

   public List<CharacterBase> getSlaveBases() {
      List<CharacterBase> slaveBases = new ArrayList<>();

      for (CharacterBase characterBase : this.getCharacterBases()) {
         if (characterBase.getType() == CharacterType.SLAVE || characterBase.getType() == null) {
            slaveBases.add(characterBase);
         }
      }

      return slaveBases;
   }

   public List<CharacterBase> getTrainerBases() {
      List<CharacterBase> trainerBases = new ArrayList<>();

      for (CharacterBase characterBase : this.getCharacterBases()) {
         if (characterBase.getType() == CharacterType.TRAINER || characterBase.getType() == null) {
            trainerBases.add(characterBase);
         }
      }

      return trainerBases;
   }

   public List<CharacterBase> getUnusedBases() {
      List<CharacterBase> unusedBases = new ArrayList<>();
      unusedBases.addAll(this.getCharacterBases());

      for (Charakter character : this.getData().getCharacters()) {
         unusedBases.remove(character.getBase());
      }

      for (Charakter character : this.getData().getAuctionHouse().getSlaves()) {
         unusedBases.remove(character.getBase());
      }

      if (unusedBases.size() == 0) {
         unusedBases.addAll(this.getSlaveBases());
      }

      return unusedBases;
   }

   public List<CharacterBase> getUnusedSlaveBases() {
      List<CharacterBase> slaveBases = new ArrayList<>();
      slaveBases.addAll(this.getSlaveBases());

      for (Charakter character : this.getData().getCharacters()) {
         slaveBases.remove(character.getBase());
      }

      for (Charakter character : this.getData().getAuctionHouse().getSlaves()) {
         slaveBases.remove(character.getBase());
      }

      if (slaveBases.size() == 0) {
         slaveBases.addAll(this.getSlaveBases());
      }

      return slaveBases;
   }

   public List<CharacterBase> getUnusedTrainerBases() {
      List<CharacterBase> bases = new ArrayList<>();
      bases.addAll(this.getTrainerBases());

      for (Charakter character : this.getData().getCharacters()) {
         bases.remove(character.getBase());
      }

      if (bases.size() == 0) {
         bases.addAll(this.getTrainerBases());
      }

      return bases;
   }

   public Charakter generateBasicSlave() {
      List<CharacterBase> bases = getInstance().getUnusedSlaveBases();
      if (bases.size() == 0) {
         bases = this.getSlaveBases();
      }

      CharacterBase base = bases.get(Util.getRnd().nextInt(bases.size()));
      Charakter character = CharacterSpawner.create(base, CharacterType.SLAVE);
      character.setOwnership(Ownership.NOTOWNED);
      return character;
   }

   public Charakter generateBasicTrainer() {
      List<CharacterBase> bases = getInstance().getUnusedTrainerBases();
      if (bases.size() == 0) {
         bases = this.getTrainerBases();
      }

      CharacterBase base = bases.get(Util.getRnd().nextInt(bases.size()));
      Charakter character = CharacterSpawner.create(base, CharacterType.TRAINER);
      character.setOwnership(Ownership.NOTOWNED);
      return character;
   }

   private synchronized void loadBases() {
      this.bases = CharacterFileLoader.getInstance().loadAllCharacters(true);
      List<ImageTag> additionalTags = new ArrayList<>();

      for (CharacterBase characterBase : this.bases) {
         for (ImageData image : characterBase.getImages()) {
            for (ImageTag tag : image.getTags()) {
               if (tag.getIncludedTag() != null) {
                  additionalTags.add(tag.getIncludedTag());
               }
            }

            image.getTags().addAll(additionalTags);
            additionalTags.clear();
         }
      }
   }

   private synchronized void loadItems() {
      this.items = new HashMap<>();

      for (Item item : ItemFileLoader.getInstance().loadAllItems()) {
         this.items.put(item.getId(), item);
      }
   }

   public void removeCharacter(Charakter character) {
      this.data.getCharacters().remove(character);

      for (PlannedActivity activity : character.getActivities().values()) {
         if (activity != null) {
            activity.removeCharacter(character);
         }
      }

      for (Equipment equipment : character.getCharacterInventory().listEquipment()) {
         if (!equipment.getId().equals("RegularClothes")) {
            this.getData().getInventory().addItem(equipment);
         }
      }

      MyEvent event = new MyEvent(EventType.CHARACTERLOST, character);
      this.data.getEventManager().handleEvent(event);
      if (character == this.data.getProtagonist()) {
         List<Charakter> candidates = new ArrayList<>();

         for (Charakter characterTmp : this.data.getCharacters()) {
            if (characterTmp.getOwnership() == Ownership.OWNED || characterTmp.getOwnership() == Ownership.CONTRACT) {
               candidates.add(characterTmp);
            }
         }

         if (candidates.size() == 0) {
            this.gui.showGameOverScreen();
         } else {
            List<SelectionData<Charakter>> options = new ArrayList<>();

            for (Charakter curCharacter : candidates) {
               SelectionData<Charakter> option = new SelectionData<>();
               option.setSelectionObject(curCharacter);
               option.setButtonText(curCharacter.getName());
               options.add(option);
            }

            SelectionData<Charakter> selectedOption = new SelectionScreen<Charakter>()
               .select(
                  options,
                  new ImageData("images/backgrounds/coffin.png"),
                  null,
                  new ImageData("images/backgrounds/sky.jpg"),
                  TextUtil.t("events.protagonistDead", character)
               );
            Charakter newMainChar = selectedOption.getSelectionObject();
            newMainChar.setOwnership(Ownership.OWNED);
            if (newMainChar.getType() == CharacterType.TRAINER) {
               new MessageScreen(
                  TextUtil.t("events.newProtagonist.trainer", newMainChar),
                  ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, newMainChar),
                  newMainChar.getBackground()
               );
            } else {
               CharacterManipulationManager.changeType(newMainChar, CharacterType.TRAINER);
               new MessageScreen(
                  TextUtil.t("events.newProtagonist.slave", newMainChar, character),
                  ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, newMainChar),
                  newMainChar.getBackground()
               );
            }

            this.data.setProtagonist(newMainChar);
         }
      }
   }

   public void addCentralListener(CentralEventlistener listener) {
      this.getData().getEventManager().addListener(listener);
   }

   public static ExecutorService getThreadpool() {
      return threadPool;
   }

   public Map<String, Item> getItems() {
      if (this.items == null) {
         this.loadItems();
      }

      return this.items;
   }

   public synchronized void setItems(Map<String, Item> items) {
      this.items = items;
   }

   public Map<String, CustomQuestTemplate> getCustomQuestTemplates() {
      if (this.customQuestTemplates == null) {
         this.customQuestTemplates = new HashMap<>();

         for (CustomQuestTemplate questTemplate : EventAndQuestFileLoader.getInstance().loadAllCustomQuests()) {
            this.customQuestTemplates.put(questTemplate.getId(), questTemplate);
         }
      }

      return this.customQuestTemplates;
   }

   public Map<String, WorldEvent> getWorldEvents() {
      if (this.worldEvents == null) {
         this.worldEvents = new HashMap<>();

         for (WorldEvent worldEvent : EventAndQuestFileLoader.getInstance().loadAllCustomEvents()) {
            this.worldEvents.put(worldEvent.getId(), worldEvent);
         }
      }

      return this.worldEvents;
   }

   public Map<String, ComplexEnemyTemplate> getEnemyTemplates() {
      if (this.enemyTemplates == null) {
         this.enemyTemplates = new HashMap<>();

         for (ComplexEnemyTemplate complexEnemyTemplate : NpcFileLoader.getInstance().loadAllEnemies()) {
            this.enemyTemplates.put(complexEnemyTemplate.getId(), complexEnemyTemplate);
         }
      }

      return this.enemyTemplates;
   }

   public void performEvent(String eventId) {
      if (this.getWorldEvents().containsKey(eventId)) {
         try {
            WorldEvent event = this.getWorldEvents().get(eventId);
            event.execute();
         } catch (Exception e) {
            log.error("Error while performing event {}", new Object[]{eventId});
            log.throwing(e);
         }
      }
   }

   public List<Item> getAvailableItemsByLocation(ItemLocation itemLocation) {
      List<Item> items = new ArrayList<>();

      for (Item item : getInstance().getItems().values()) {
         for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
            if (itemSpawnData.getItemLocation() == itemLocation) {
               if (item.getType() == ItemType.UNLOCK) {
                  if (!this.getData().getUnlocks().getUnlockedObjects().contains(((UnlockItem)item).getUnlockObject())) {
                     items.add(item);
                  }
               } else {
                  items.add(item);
               }
            }
         }
      }

      return items;
   }

   public List<Item> getAvailableItemsByType(ItemType itemType) {
      List<Item> items = new ArrayList<>();

      for (Item item : getInstance().getItems().values()) {
         if (item.getType() == itemType) {
            items.add(item);
         }
      }

      return items;
   }

   public Interpreter getInterpreter() {
      if (this.interpreter == null) {
         this.interpreter = new Interpreter();

         try {
            this.interpreter
               .eval(
                  "import jasbro.*;import jasbro.game.*;import jasbro.game.character.*;import jasbro.game.character.activities.*;import jasbro.game.character.activities.requirements.*;import jasbro.game.character.activities.sub.*;import jasbro.game.character.activities.sub.business.*;import jasbro.game.character.activities.sub.childcare.*;import jasbro.game.character.activities.sub.whore.*;import jasbro.game.character.attributes.*;import jasbro.game.character.battle.*;import jasbro.game.character.conditions.*;import jasbro.game.character.specialization.*;import jasbro.game.character.traits.*;import jasbro.game.character.warnings.*;import jasbro.game.events.*;import jasbro.game.events.business.*;import jasbro.game.events.rooms.*;import jasbro.game.housing.*;import jasbro.game.interfaces.*;import jasbro.game.items.*;import jasbro.game.quests.*;import jasbro.game.world.*;import jasbro.game.world.customContent.*;import jasbro.game.world.customContent.effects.*;import jasbro.game.world.customContent.npc.*;import jasbro.game.world.locations.*;import jasbro.game.world.market.*;import jasbro.gui.*;import jasbro.gui.pages.*;import jasbro.gui.pictures.*;import jasbro.stats.*;import jasbro.texts.*;import jasbro.util.*;import java.util.*;"
               );
         } catch (EvalError e) {
            log.error("Error when initializing interpreter", e);
         }
      }

      return this.interpreter;
   }

   public void setInterpreter(Interpreter interpreter) {
      this.interpreter = interpreter;
   }

   public void setCustomQuestTemplates(Map<String, CustomQuestTemplate> customQuestTemplates) {
      this.customQuestTemplates = customQuestTemplates;
   }

   public void setWorldEvents(Map<String, WorldEvent> worldEvents) {
      this.worldEvents = worldEvents;
   }

   public void cleanupInterpreter() {
      log.debug("Reset Interpreter!");

      try {
         for (String variable : (String[])this.interpreter.eval("return this.variables;")) {
            this.interpreter.unset(variable);
         }
      } catch (EvalError e) {
         log.error("Error when resetting interpreter", e);
      }
   }

   public void setCharacterBases(List<CharacterBase> characters) {
      this.bases = characters;
   }

   public void setEnemyTemplates(Map<String, ComplexEnemyTemplate> enemyTemplates) {
      this.enemyTemplates = enemyTemplates;
   }
}
