package jasbro.game.events;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.WeakList;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Illness;
import jasbro.game.character.conditions.MonsterPregnancy;
import jasbro.game.character.conditions.Pregnancy;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.business.BusinessCalculations;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.Fame;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.housing.SecurityState;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventManager implements MyEventListener, Serializable {
   private static final Logger log = LogManager.getLogger(EventManager.class);
   private transient boolean shiftInProgress = false;
   private List<CentralEventlistener> listeners = new WeakList<>();

   public boolean performShift(GameData gameData) {
      this.shiftInProgress = true;

      try {
         this.addListener(gameData.getStatCollector());
         this.addListener(gameData.getQuestManager());
         List<RunningActivity> activities = new ArrayList<>();
         this.notifyAll(new MyEvent(EventType.SHIFTSTART, null));

         for (Charakter character : gameData.getCharacters()) {
            if (character.getActivity() == null) {
               PlannedActivity plannedActivity = new PlannedActivity();
               plannedActivity.setType(ActivityType.IDLE);
               plannedActivity.getCharacters().add(character);
               activities.add(plannedActivity.getRunningActivity());
            }
         }

         for (int i = activities.size() - 1; i >= 0; i--) {
            activities.get(i).performActivity();
         }

         activities.clear();

         for (CharacterLocation location : gameData.getOtherLocations()) {
            ActivityType activity = location.getSelectedActivity();
            if (activity == ActivityType.CUSTOMEVENT) {
               this.triggerCustomEvent(location);
            } else if (activity != null && activity.isGroupActivity()) {
               if (location.getCurrentUsage().getCharacters().size() > 0) {
                  activities.add(location.getCurrentUsage().getRunningActivity());
               }
            } else {
               for (Charakter character : location.getCurrentUsage().getCharacters()) {
                  PlannedActivity plannedActivity = new PlannedActivity(location.getCurrentUsage(), character);
                  activities.add(plannedActivity.getRunningActivity());
               }
            }

            for (RunningActivity curActivity : activities) {
               curActivity.performActivity();
            }

            activities.clear();
         }

         for (House house : gameData.getHouses()) {
            List<Charakter> whoresAndSupports = new ArrayList<>();

            for (Room room : house.getRooms()) {
               ActivityType activity = room.getSelectedActivity();
               if (activity.isCustomerDependent()) {
                  whoresAndSupports.addAll(room.getCurrentUsage().getCharacters());
               } else if (activity == ActivityType.CUSTOMEVENT) {
                  this.triggerCustomEvent(room);
               } else if (activity.isGroupActivity()) {
                  if (room.getCurrentUsage().getCharacters().size() > 0) {
                     activities.add(room.getCurrentUsage().getRunningActivity());
                  }
               } else {
                  for (Charakter character : room.getCurrentUsage().getCharacters()) {
                     PlannedActivity plannedActivity = new PlannedActivity(room.getCurrentUsage(), character);
                     activities.add(plannedActivity.getRunningActivity());
                  }
               }
            }

            for (RunningActivity activity : activities) {
               activity.performActivity();
            }

            activities.clear();
            if (whoresAndSupports.size() > 0) {
               this.doBusiness(whoresAndSupports, house);
            }
         }

         for (House house : gameData.getHouses()) {
            house.modDirt(house.getAmountPeople() * 3);
            house.applyDirtLimit();
         }

         return this.advanceTime(gameData);
      } finally {
         this.shiftInProgress = false;
      }
   }

   private void doBusiness(List<Charakter> whoresAndSupport, House house) {
      house.getAdvertising().performAdvertising(house);
      house.getMercSecurity().perform(house);
      BusinessCalculations businessUtil = new BusinessCalculations();
      Fame fame = businessUtil.calculateFame(house, whoresAndSupport);
      int amountCustomers = businessUtil.calculateCustomerAmount(whoresAndSupport, fame);
      List<Customer> remainingCustomers = new ArrayList<>();
      List<Charakter> whores = new ArrayList<>();
      List<RunningActivity> mainActivities = new ArrayList<>();
      List<RunningActivity> secondaryActivities = new ArrayList<>();
      HashMap<Charakter, Float> remainingPossibleCustomers = new HashMap<>();

      for (int i = 0; i < whoresAndSupport.size(); i++) {
         Charakter character = whoresAndSupport.get(i);
         if (Whore.class.isAssignableFrom(character.getActivity().getType().getActivityClass())) {
            whores.add(character);
         } else {
            ActivityType activityType = character.getActivity().getType();
            RunningActivity activity;
            if (activityType.isGroupActivity()) {
               activity = character.getActivity().getRunningActivity();

               for (Charakter curCharacter : character.getActivity().getCharacters()) {
                  if (whoresAndSupport.indexOf(curCharacter) <= i) {
                     i--;
                  }

                  whoresAndSupport.remove(curCharacter);
               }
            } else {
               PlannedActivity plannedActivity = new PlannedActivity(character.getActivity(), character);
               activity = plannedActivity.getRunningActivity();
            }

            RunningActivity RunningActivity = activity;
            if (RunningActivity instanceof BusinessMainActivity) {
               mainActivities.add(activity);
            }

            if (RunningActivity instanceof BusinessSecondaryActivity) {
               secondaryActivities.add(activity);
            }
         }
      }

      for (Charakter whore : whores) {
         remainingPossibleCustomers.put(whore, whore.getPossibleAmountCustomers());
      }

      List<Customer> allCustomers = house.getSpawnData().spawn(amountCustomers, fame);
      amountCustomers = allCustomers.size();
      remainingCustomers.addAll(allCustomers);
      if (SecurityState.isImplemented()) {
         for (Customer cust : allCustomers) {
            if (cust.getType() == CustomerType.BUM) {
               house.setSecurity(house.getSecurity() - 1);
            }
         }
      }

      MyEvent customersArriveEvent = new CustomersArriveEvent(house, allCustomers);
      this.handleEvent(customersArriveEvent);

      for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
         character.handleEvent(customersArriveEvent);
      }

      if (amountCustomers != 0) {
         this.notifyCustomersArrival(house, allCustomers);
      }

      List<BusinessMainActivity> curMainActivities = new ArrayList<>();

      for (RunningActivity activity : mainActivities) {
         BusinessMainActivity RunningActivity = (BusinessMainActivity)activity;
         curMainActivities.add(RunningActivity);
      }

      businessUtil.assignCustomers(curMainActivities, remainingCustomers, null);
      curMainActivities = new ArrayList<>();

      for (RunningActivity activity : secondaryActivities) {
         BusinessSecondaryActivity RunningActivity = (BusinessSecondaryActivity)activity;
         curMainActivities.add(RunningActivity);
      }

      businessUtil.assignCustomersToSecondaryActivities(curMainActivities, allCustomers);

      for (RunningActivity activity : secondaryActivities) {
         if (!(activity instanceof BusinessMainActivity)) {
            activity.performActivity();
         }
      }

      for (RunningActivity activity : mainActivities) {
         activity.performActivity();
      }

      for (int i = 0; i < remainingCustomers.size(); i++) {
         Customer customer = remainingCustomers.get(i);
         if (customer.getMoney() < 5) {
            remainingCustomers.remove(customer);
         }
      }

      int i = 0;

      boolean assigned;
      do {
         mainActivities = new ArrayList<>();

         for (Charakter whore : whores) {
            PlannedActivity plannedActivity = new PlannedActivity(whore.getActivity(), whore);
            mainActivities.add(plannedActivity.getRunningActivity());
         }

         List<BusinessMainActivity> curMainActivitiesx = new ArrayList<>();

         for (RunningActivity activity : mainActivities) {
            curMainActivitiesx.add((BusinessMainActivity)activity);
         }

         List<Customer> curCustomers = new ArrayList<>();
         int selectionSize = curMainActivitiesx.size() * 2;
         int alternative = remainingCustomers.size() / Math.max(1, 8 - i);
         if (selectionSize < alternative) {
            selectionSize = alternative;
         }

         if (selectionSize >= remainingCustomers.size()) {
            curCustomers.addAll(remainingCustomers);
         } else {
            List<Customer> remainingCustomersCopy = new ArrayList<>(remainingCustomers);

            for (int j = 0; j < selectionSize && remainingCustomersCopy.size() > 0; j++) {
               Customer customer;
               do {
                  customer = remainingCustomersCopy.get(Util.getInt(0, remainingCustomersCopy.size()));
               } while (curCustomers.contains(customer));

               curCustomers.add(customer);
               remainingCustomersCopy.remove(customer);
            }
         }

         businessUtil.assignCustomers(curMainActivitiesx, curCustomers, remainingCustomers);
         Collections.shuffle(mainActivities);
         assigned = false;

         for (RunningActivity activity : mainActivities) {
            BusinessMainActivity businessMainActivity = (BusinessMainActivity)activity;
            if (businessMainActivity.hasMainCustomer()) {
               assigned = true;
               activity.performActivity();
               Charakter whore = activity.getCharacter();
               Whore whoreActivity = (Whore)businessMainActivity;
               remainingPossibleCustomers.put(whore, remainingPossibleCustomers.get(whore) - whoreActivity.getAmountActions());
               if (remainingPossibleCustomers.get(whore) < 5.0F || whore.getEnergy() < 1) {
                  whores.remove(whore);
               }
            }
         }
      } while (assigned && remainingCustomers.size() > 0 && whores.size() > 0);
   }

   private void notifyCustomersArrival(House house, List<Customer> allCustomers) {
      List<Customer> normalCustomers = new ArrayList<>();
      List<Customer> groups = new ArrayList<>();

      for (Customer customer : allCustomers) {
         if (customer instanceof CustomerGroup) {
            groups.add(customer);
         } else {
            normalCustomers.add(customer);
         }
      }

      Object[] arguments = new Object[]{normalCustomers.size()};
      String customerString = TextUtil.t("business.msgNormalCustomers", arguments);
      if (groups.size() > 0) {
         List<String> customerStringList = new ArrayList<>();
         customerStringList.add(customerString);

         for (Customer customer : groups) {
            arguments[0] = customer.getName();
            customerStringList.add(TextUtil.t("grouplistitem", arguments));
         }

         customerString = TextUtil.listStrings(customerStringList);
      }

      Object[] arguments2 = new Object[]{customerString, house.getName()};
      MessageData messageData;
      if (house.getInternName() != null && !house.getInternName().trim().equals("")) {
         messageData = new MessageData(TextUtil.t("business.msgCustomers2", arguments2), house.getImage(), null);
      } else {
         messageData = new MessageData(TextUtil.t("business.msgCustomers1", arguments2), house.getImage(), null);
      }

      messageData.setMessageGroupObject(house);
      messageData.createMessageScreen();
   }

   public boolean advanceTime(GameData gameData) {
      this.notifyAll(new MyEvent(EventType.NEXTSHIFT, null));
      if (gameData.getTime() == Time.NIGHT) {
         this.notifyAll(new MyEvent(EventType.NEXTDAY, null));
         gameData.setTime(gameData.getTime().getNextTimeOfDay());
         gameData.setDay(gameData.getDay() + 1);
         this.shiftInProgress = false;
         this.notifyAll(new MyEvent(EventType.NEXTSHIFTSTARTED, null));
         return true;
      } else {
         gameData.setTime(gameData.getTime().getNextTimeOfDay());
         this.shiftInProgress = false;
         this.notifyAll(new MyEvent(EventType.NEXTSHIFTSTARTED, null));
         return false;
      }
   }

   public List<CentralEventlistener> getListeners() {
      return this.listeners;
   }

   public void setListeners(List<CentralEventlistener> listeners) {
      this.listeners = listeners;
   }

   public void addListener(CentralEventlistener listener) {
      if (!this.listeners.contains(listener)) {
         this.listeners.add(listener);
      }
   }

   public void notifyAll(MyEvent event) {
      for (House house : Jasbro.getInstance().getData().getHouses()) {
         house.handleEvent(event);
      }

      Charakter c = null;

      try {
         List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();

         for (int i = 0; i < characters.size(); i++) {
            characters.get(i).handleEvent(event);
         }
      } catch (ConcurrentModificationException e) {
         System.err.println("Event causing error (Type): " + event.getType());
         System.err.println("Event causing error (Source): " + event.getSource());
         System.err.println("Last character before error: " + c.getName());
         throw e;
      }

      this.handleEvent(event);
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY && !(this.listeners.get(this.listeners.size() - 1) instanceof QuestManager)) {
         QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
         this.removeListener(questManager);
         this.addListener(questManager);
      }

      for (CentralEventlistener eventListener : new ArrayList<>(this.listeners)) {
         try {
            eventListener.handleCentralEvent(e);
         } catch (NullPointerException ex) {
         }
      }

      if (e.getType() == EventType.ACTIVITYPERFORMED) {
         RunningActivity activity = (RunningActivity)e.getSource();
         if (activity.getType() == ActivityType.SEX) {
            this.checkPregnancyPossible(activity.getSextype(), activity.getCharacters(), e);
         } else if (activity.getType() == ActivityType.WHORE) {
            List<Person> people = new ArrayList<>();
            people.add(activity.getCharacter());
            people.add(activity.getMainCustomer());
            this.checkPregnancyPossible(((Whore)activity).getSexType(), people, e);
         } else if (activity.getType() == ActivityType.ORGY || activity.getType() == ActivityType.THREESOME) {
            this.checkPregnancyPossible(Sextype.GROUP, activity.getCharacters(), e);
         } else if (activity.getSextype() == Sextype.MONSTER) {
            this.checkMonsterPregnancyPossible(activity.getCharacters(), e);
         }
      } else if (e.getType() == EventType.ENERGYZERO && !e.isCancelled()) {
         Charakter character = ((AttributeChangedEvent)e).getAttribute().getCharacter();
         if (Jasbro.getInstance().getData().getDay() > 150 && Util.getInt(0, 100) < 2) {
            character.addCondition(new Illness.Smallpox());
         } else {
            character.addCondition(new Illness.Flu());
         }
      } else if (e.getType() == EventType.HEALTHZERO && !e.isCancelled()) {
         Charakter character = ((AttributeChangedEvent)e).getAttribute().getCharacter();
         Jasbro.getInstance().removeCharacter(character);
         Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(-character.getFame().getFame() - 500L);
         if (!character.getType().isChildType()) {
            this.handleEvent(new MyEvent(EventType.CHARACTERDEATH, character));
            MessageData message = new MessageData(character.getName() + " died.", new ImageData("images/backgrounds/coffin.png"), null, true);
            GuiUtil.addMessageToEvent(message, e);
         } else {
            MessageData message = new MessageData(
               TextUtil.t("childcare.ill", character), ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character), character.getBackground(), true
            );
            GuiUtil.addMessageToEvent(message, e);
         }
      } else if (e.getType() == EventType.STATUSCHANGE && !this.shiftInProgress) {
         Jasbro.getInstance().getGui().updateStatus();
      }
   }

   public void removeListener(CentralEventlistener centralEventlistener) {
      this.listeners.remove(centralEventlistener);
   }

   public void checkPregnancyPossible(Sextype sexType, List<? extends Person> people, MyEvent event) {
      for (Person person : people) {
         if (person instanceof Charakter) {
            Charakter character = (Charakter)person;
            if (!character.isUsesContraceptives() && (character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY))) {
               List<Person> otherPeople = new ArrayList<>(people);
               otherPeople.remove(character);

               for (Person otherPerson : otherPeople) {
                  if (Sextype.isPregnancyPossible(sexType, character, otherPerson)) {
                     int chance = character.getPregnancyChance();
                     if (otherPerson instanceof Charakter) {
                        chance += ((Charakter)otherPerson).getPregnancyChance();
                     }

                     chance /= 2;
                     if (Util.getInt(0, 100) < chance) {
                        character.addCondition(new Pregnancy(character, otherPerson, event));
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   public void checkMonsterPregnancyPossible(List<Charakter> characters, MyEvent event) {
      for (Charakter character : characters) {
         if (character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY)) {
            int chance = character.getPregnancyChance() / 2;

            for (Condition condition : character.getConditions()) {
               if (condition instanceof PregnancyInterface) {
                  chance = 0;
                  break;
               }
            }

            if (character.isUsesContraceptives()) {
               chance -= 30;
            }

            if (Util.getInt(0, 100) < chance) {
               character.addCondition(new MonsterPregnancy(character, event));
            }
         }
      }
   }

   private void triggerCustomEvent(CharacterLocation location) {
      if (location.getCurrentUsage().getCharacters().size() > 0) {
         if (Jasbro.getInstance().getWorldEvents().containsKey(location.getCurrentUsage().getEventId())) {
            WorldEvent event = Jasbro.getInstance().getWorldEvents().get(location.getCurrentUsage().getEventId());

            try {
               event.putAttribute(WorldEvent.WorldEventVariables.questInstance, location.getCurrentUsage().getQuest());
               event.putAttribute(WorldEvent.WorldEventVariables.characters, location.getCurrentUsage().getCharacters());
               event.putAttribute(WorldEvent.WorldEventVariables.character, location.getCurrentUsage().getCharacters().get(0));
               event.putAttribute(WorldEvent.WorldEventVariables.location, location.getLocationType());
               event.putAttribute(WorldEvent.WorldEventVariables.people, location.getCurrentUsage().getCharacters());
               event.execute();
            } finally {
               event.reset();
            }
         } else {
            log.error("Event id not found: {}", new Object[]{location.getCurrentUsage().getEventId()});
         }
      }
   }

   public boolean isShiftInProgress() {
      return this.shiftInProgress;
   }
}
