package jasbro.game.character.activities;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.sub.RefusedToWork;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RunningActivity {
   private static final Logger log = LogManager.getLogger(RunningActivity.class);
   private List<AttributeModification> attributeModifications = new ArrayList<>();
   private int income = 0;
   private PlannedActivity plannedActivity;
   private boolean abort = false;
   private List<MessageData> messages = new ArrayList<>();
   private float fameModifier = 1.0F;
   private List<Customer.SatisfactionModifier> initialSatisfactionModifiers = new ArrayList<>();
   private List<Customer> mainCustomers = new ArrayList<>();
   private List<Customer> customers = new ArrayList<>();
   private int maxMainCustomers = 1;
   private Integer minimumObedience = null;
   private Sextype sextype;

   public abstract MessageData getBaseMessage();

   public void init() {
   }

   public void perform() {
   }

   public List<RunningActivity.ModificationData> getStatModifications() {
      return new ArrayList<>();
   }

   private void initActivity() {
      List<Charakter> slaves = Util.getSlaves(this.plannedActivity.getCharacters());
      List<Charakter> trainers = Util.getTrainers(this.plannedActivity.getCharacters());

      for (Customer customer : this.getCustomers()) {
         this.initialSatisfactionModifiers.addAll(customer.getSatisfactionModifiers());
      }

      for (Customer customer : this.getMainCustomers()) {
         this.initialSatisfactionModifiers.addAll(customer.getSatisfactionModifiers());
      }

      this.init();

      for (RunningActivity.ModificationData modificationData : this.getStatModifications()) {
         List<Charakter> targets;
         if (modificationData.getTargetType() == RunningActivity.TargetType.ALL) {
            targets = this.plannedActivity.getCharacters();
         } else if (modificationData.getTargetType() == RunningActivity.TargetType.SLAVE) {
            targets = slaves;
         } else if (modificationData.getTargetType() == RunningActivity.TargetType.TRAINER) {
            targets = trainers;
         } else {
            if (modificationData.getTargetType() == RunningActivity.TargetType.SINGLE) {
               AttributeModification modificator = new AttributeModification(
                  modificationData.getAmount(), modificationData.getAttributeType(), modificationData.getTarget()
               );
               this.attributeModifications.add(modificator);
               continue;
            }

            if (modificationData.getTargetType() == RunningActivity.TargetType.ALLHOUSE) {
               targets = new ArrayList<>();
               House house = ((Room)this.plannedActivity.getSource()).getHouse();

               for (Room room : house.getRooms()) {
                  targets.addAll(room.getCurrentUsage().getCharacters());
               }
            } else {
               targets = new ArrayList<>();
               log.error("No target for modification");
            }
         }

         for (Charakter target : targets) {
            AttributeModification modificator = new AttributeModification(modificationData.getAmount(), modificationData.getAttributeType(), target);
            this.attributeModifications.add(modificator);
         }
      }

      this.messages.add(this.getBaseMessage());
   }

   public void performActivity() {
      this.initActivity();
      MyEvent event = new MyEvent(EventType.ACTIVITY, this);
      Jasbro.getInstance().getData().getEventManager().handleEvent(event);
      if (this.getPlannedActivity().getSource() instanceof Room) {
         House house = ((Room)this.getPlannedActivity().getSource()).getHouse();
         house.handleEvent(event);
      }

      if (this.getPlannedActivity().getSource() != null) {
         this.getPlannedActivity().getSource().handleEvent(event);
      }

      for (Charakter character : this.getCharacters()) {
         character.handleEvent(event);
      }

      Charakter obedienceTooLowCharacter = null;

      for (Charakter character : this.getCharacters()) {
         if (character.getType() == CharacterType.SLAVE && character.getObedience() < this.getMinimumObedience()) {
            int diff = character.getRealMinObedience(this.getMinimumObedience(), this) - character.getObedience();
            if (diff == 1 && Util.getInt(0, 100) > 75) {
               obedienceTooLowCharacter = character;
            } else if (diff == 2 && Util.getInt(0, 100) > 50) {
               obedienceTooLowCharacter = character;
            } else if (diff == 3 && Util.getInt(0, 100) > 25) {
               obedienceTooLowCharacter = character;
            } else if (diff > 3) {
               obedienceTooLowCharacter = character;
            }
         }
      }

      if (obedienceTooLowCharacter != null) {
         PlannedActivity plannedActivity = new PlannedActivity(ActivityType.REFUSEDTOWORK, this.getPlannedActivity());
         plannedActivity.getCharacters().clear();
         plannedActivity.getCharacters().addAll(this.getCharacters());
         RunningActivity activity = plannedActivity.getRunningActivity();
         RefusedToWork refusedToWorkActivity = (RefusedToWork)activity;
         refusedToWorkActivity.setCausedByCharacter(obedienceTooLowCharacter);
         refusedToWorkActivity.setOriginalActivity(this.getType());
         activity.performActivity();
         this.abort = true;
      }

      if (!this.abort) {
         this.perform();
         event = new MyEvent(EventType.ACTIVITYPERFORMED, this);
         Jasbro.getInstance().getData().getEventManager().handleEvent(event);
         if (this.getPlannedActivity().getSource() instanceof Room) {
            House house = ((Room)this.getPlannedActivity().getSource()).getHouse();
            house.handleEvent(event);
         }

         if (this.getPlannedActivity().getSource() != null) {
            this.getPlannedActivity().getSource().handleEvent(event);
         }

         for (Charakter character : this.getCharacters()) {
            character.handleEvent(event);
         }

         if (this.income > 0) {
            Jasbro.getInstance().getData().earnMoney(this.income, this);
         } else if (this.income < 0) {
            Jasbro.getInstance().getData().spendMoney(-this.income, this);
         }

         for (int i = 0; i < this.attributeModifications.size(); i++) {
            AttributeModification attributeModification = this.attributeModifications.get(i);

            for (int j = 0; j < this.attributeModifications.size(); j++) {
               if (i != j) {
                  AttributeModification attributeModification2 = this.attributeModifications.get(j);
                  if (attributeModification.getAttributeType() == attributeModification2.getAttributeType()
                     && attributeModification.getTargetCharacter() == attributeModification2.getTargetCharacter()) {
                     attributeModification.addModificator(attributeModification2.getBaseAmount());
                     this.attributeModifications.remove(attributeModification2);
                     j--;
                  }
               }
            }
         }

         for (int i = 0; i < this.attributeModifications.size(); i++) {
            AttributeModification modification = this.attributeModifications.get(i);
            modification.applyModification(this);
         }

         for (int i = 0; i < this.messages.size(); i++) {
            MessageData message = this.messages.get(i);
            if (message != null) {
               if (message.getBackground() == null) {
                  if (this.plannedActivity.getSource() != null) {
                     message.setBackground(this.plannedActivity.getSource().getImage());
                  } else {
                     message.setBackground(this.getCharacters().get(0).getBackground());
                  }
               }

               if (i == this.messages.size() - 1) {
                  message.setAttributeModifications(this.attributeModifications);
               }

               if (this.getHouse() != null) {
                  message.setMessageGroupObject(this.getHouse());
               } else {
                  message.setMessageGroupObject(this.getCharacterLocation());
               }

               if (this.getPlannedActivity().getType() != ActivityType.SLEEP || ConfigHandler.isShowSleep()) {
                  message.createMessageScreen();
               }
            }
         }

         if (this instanceof BusinessMainActivity) {
            for (Customer customer : this.getMainCustomers()) {
               float fameMod = customer.getImportance() * customer.getSatisfaction().getFameModifier() * 10.0F * this.fameModifier;
               if (this.getHouse() != null) {
                  this.getHouse().getFame().modifyFame(fameMod);
               }

               for (Charakter character : this.getCharacters()) {
                  character.getFame().modifyFame(fameMod / this.getCharacters().size());
               }
            }
         }

         if (this instanceof BusinessSecondaryActivity) {
            for (Customer customer : this.getCustomers()) {
               int satisfactionModifiedBy = 0;

               for (Customer.SatisfactionModifier satisfactionModifier : customer.getSatisfactionModifiers()) {
                  if (!this.initialSatisfactionModifiers.contains(satisfactionModifier)) {
                     satisfactionModifiedBy += satisfactionModifier.getModifiedBy();
                  }
               }

               float fameMod = customer.getImportance() * satisfactionModifiedBy * this.fameModifier / 10.0F;
               if (this.getHouse() != null) {
                  this.getHouse().getFame().modifyFame(fameMod);
               }

               for (Charakter character : this.getCharacters()) {
                  character.getFame().modifyFame(fameMod / this.getCharacters().size());
               }
            }
         }

         event = new MyEvent(EventType.ACTIVITYFINISHED, this);
         Jasbro.getInstance().getData().getEventManager().handleEvent(event);
         if (this.getPlannedActivity().getSource() instanceof Room) {
            House house = ((Room)this.getPlannedActivity().getSource()).getHouse();
            house.handleEvent(event);
         }

         if (this.getPlannedActivity().getSource() != null) {
            this.getPlannedActivity().getSource().handleEvent(event);
         }

         for (Charakter character : this.getCharacters()) {
            character.handleEvent(event);
         }
      } else if (this.getType() != ActivityType.WHORE) {
         for (Charakter character : this.getCharacters()) {
            character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15.0F);
         }
      }
   }

   public void applyChanges() {
   }

   public House getHouse() {
      CharacterLocation CharacterLocation = this.plannedActivity.getSource();
      return CharacterLocation instanceof Room ? ((Room)CharacterLocation).getHouse() : null;
   }

   public Room getRoom() {
      CharacterLocation CharacterLocation = this.plannedActivity.getSource();
      return CharacterLocation instanceof Room ? (Room)CharacterLocation : null;
   }

   public void setPlannedActivity(PlannedActivity plannedActivity) {
      this.plannedActivity = plannedActivity;
      if (plannedActivity != null) {
         MyEvent event = new MyEvent(EventType.ACTIVITYCREATED, this);

         for (Charakter character : this.getCharacters()) {
            character.handleEvent(event);
         }
      }
   }

   public ActivityType getType() {
      return this.plannedActivity.getType();
   }

   public List<AttributeModification> getAttributeModifications() {
      return this.attributeModifications;
   }

   public PlannedActivity getPlannedActivity() {
      return this.plannedActivity;
   }

   public Charakter getCharacter() {
      return this.plannedActivity.getCharacters().get(0);
   }

   public List<Charakter> getCharacters() {
      return this.plannedActivity.getCharacters();
   }

   public CharacterLocation getCharacterLocation() {
      return this.plannedActivity.getSource();
   }

   public boolean isAbort() {
      return this.abort;
   }

   public void setAbort(boolean abort) {
      this.abort = abort;
   }

   public List<MessageData> getMessages() {
      return this.messages;
   }

   public void setMessages(List<MessageData> messages) {
      this.messages = messages;
   }

   public int getIncome() {
      return this.income;
   }

   public void setIncome(int income) {
      this.income = income;
   }

   public void modifyIncome(int modValue) {
      this.income += modValue;
   }

   @Override
   public String toString() {
      return this.getType().getText();
   }

   public float getFameModifier() {
      return this.fameModifier;
   }

   public void setFameModifier(float fameModifier) {
      this.fameModifier = fameModifier;
   }

   public Customer getMainCustomer() {
      return this.mainCustomers.size() > 0 ? this.mainCustomers.get(0) : null;
   }

   public void addMainCustomer(Customer mainCustomer) {
      this.mainCustomers.add(mainCustomer);
   }

   public List<Customer> getCustomers() {
      return this.customers;
   }

   public void setCustomers(List<Customer> customers) {
      this.customers = customers;
   }

   public boolean hasMainCustomer() {
      return this.getMainCustomers().size() > 0;
   }

   public void addAttendingCustomer(Customer customer) {
      this.customers.add(customer);
   }

   public List<Customer> getMainCustomers() {
      return this.mainCustomers;
   }

   public void setMainCustomers(List<Customer> mainCustomers) {
      this.mainCustomers = mainCustomers;
   }

   public int getMaxMainCustomers() {
      return this.maxMainCustomers;
   }

   public void setMaxMainCustomers(int maxMainCustomers) {
      this.maxMainCustomers = maxMainCustomers;
   }

   public int getMinimumObedience() {
      if (this.minimumObedience == null) {
         if (this.getType() == null) {
            return 0;
         }

         this.minimumObedience = this.getType().getMinimumObedience();
      }

      return this.minimumObedience;
   }

   public void setMinimumObedience(Integer minimumObedience) {
      this.minimumObedience = minimumObedience;
   }

   public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
      return null;
   }

   public ImageData getBackground() {
      return this.getCharacter().getBackground();
   }

   public Sextype getSextype() {
      return this.sextype;
   }

   public void setSextype(Sextype sextype) {
      this.sextype = sextype;
   }

   public class ModificationData {
      private RunningActivity.TargetType targetType;
      private Charakter target;
      private float amount;
      private AttributeType attributeType;

      public ModificationData() {
      }

      public ModificationData(RunningActivity.TargetType targetType, float amount, AttributeType attributeType) {
         this.targetType = targetType;
         this.amount = amount;
         this.attributeType = attributeType;
      }

      public ModificationData(RunningActivity.TargetType targetType, Charakter target, float amount, AttributeType attributeType) {
         this.targetType = targetType;
         this.target = target;
         this.amount = amount;
         this.attributeType = attributeType;
      }

      public RunningActivity.TargetType getTargetType() {
         return this.targetType;
      }

      public void setTargetType(RunningActivity.TargetType target) {
         this.targetType = target;
      }

      public float getAmount() {
         return this.amount;
      }

      public void setAmount(float amount) {
         this.amount = amount;
      }

      public AttributeType getAttributeType() {
         return this.attributeType;
      }

      public void setAttributeType(AttributeType attributeType) {
         this.attributeType = attributeType;
      }

      public Charakter getTarget() {
         return this.target;
      }

      public void setTarget(Charakter target) {
         this.target = target;
      }

      public List<RunningActivity.ModificationData> getModificationForSpecialization(
         SpecializationType specialization, float amount, RunningActivity.TargetType target
      ) {
         List<RunningActivity.ModificationData> modificationData = new ArrayList<>();

         for (AttributeType specializationAttribute : specialization.getAssociatedAttributes()) {
            modificationData.add(RunningActivity.this.new ModificationData(target, amount, specializationAttribute));
         }

         return modificationData;
      }
   }

   public enum TargetType {
      SLAVE,
      TRAINER,
      ALL,
      SINGLE,
      ALLHOUSE;
   }
}
