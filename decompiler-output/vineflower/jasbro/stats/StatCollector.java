package jasbro.stats;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MoneyChangedEvent;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import jasbro.game.world.Time;
import jasbro.gui.pages.StatScreen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatCollector implements CentralEventlistener {
   private transient StatScreen statScreen;
   private transient Map<Time, StatCollector.ShiftData> shiftDataMap = new HashMap<>();
   private transient StatCollector.ShiftData dailyData;
   private transient StatCollector.ShiftData previousDayData;

   public StatCollector() {
      this.reset();
   }

   @Override
   public void handleCentralEvent(MyEvent e) {
      Time shift = Jasbro.getInstance().getData().getTime();
      if (this.shiftDataMap.get(shift).isClosed()
         && (e.getType() == EventType.ACTIVITYPERFORMED || e.getType() == EventType.MONEYEARNED || e.getType() == EventType.MONEYSPENT)) {
         this.reset();
      }

      if (e.getType() == EventType.SHIFTSTART) {
         this.initMoneyBeforeShift();
      }

      this.dailyData.recordEvent(e);
      this.shiftDataMap.get(shift).recordEvent(e);
   }

   public void showStatScreen() {
      this.showStatScreen(null, true);
   }

   public void showStatScreen(Time time, boolean important) {
      this.statScreen = new StatScreen(this, time, important);
      Jasbro.getInstance().getGui().addMessage(this.statScreen);
   }

   public void reset() {
      this.previousDayData = this.dailyData;
      this.dailyData = new StatCollector.ShiftData();
      this.shiftDataMap.clear();

      for (Time time : Time.values()) {
         this.shiftDataMap.put(time, new StatCollector.ShiftData());
      }

      Time shift = Jasbro.getInstance().getData().getTime();
      if (shift != Time.MORNING) {
         this.shiftDataMap.get(Time.MORNING).setMoneyAfterShift(0L);
      }
   }

   private void initMoneyBeforeShift() {
      Time shift = Jasbro.getInstance().getData().getTime();
      this.dailyData.initMoneyBeforeShift();
      this.shiftDataMap.get(shift).initMoneyBeforeShift();
   }

   public void setMoneyAfterShift(long money) {
      Time shift = Jasbro.getInstance().getData().getTime().getPreviousTimeOfDay();
      this.dailyData.setMoneyAfterShift(money);
      this.shiftDataMap.get(shift).setMoneyAfterShift(money);
   }

   public StatCollector.ShiftData getShiftDataMap(Time time) {
      return this.shiftDataMap.get(time);
   }

   public StatCollector.ShiftData getDailyData() {
      return this.dailyData;
   }

   public StatCollector.ShiftData getPreviousDayData() {
      return this.previousDayData;
   }

   public class ActivityData {
      private ActivityType type;
      private List<Charakter> characters;
      private int income;
      private int amountCustomers;
      private int satisfactionModifier;
      private int amountModifiers;
      private House house;
      private int satisfactionMainCustomer;
      private int amountMainCustomers;

      public ActivityData(RunningActivity activity, StatCollector.ShiftData currentShift) {
         this.type = activity.getType();
         this.characters = activity.getCharacters();
         this.income = activity.getIncome();
         this.amountCustomers = activity.getCustomers().size();
         this.satisfactionModifier = currentShift.getAverageSatisfactionModifier(activity);
         this.amountModifiers = 1;
         this.house = activity.getHouse();
         this.amountMainCustomers = activity.getMainCustomers().size();
         this.satisfactionMainCustomer = currentShift.getAverageFinalSatisfaction(activity);
      }

      public void addToIncome(int amount) {
         this.income += amount;
      }

      public ActivityType getType() {
         return this.type;
      }

      public void setType(ActivityType type) {
         this.type = type;
      }

      public List<Charakter> getCharacters() {
         return this.characters;
      }

      public void setCharacters(List<Charakter> characters) {
         this.characters = characters;
      }

      public int getIncome() {
         return this.income;
      }

      public void setIncome(int income) {
         this.income = income;
      }

      public int getAmountCustomers() {
         return this.amountCustomers;
      }

      public void setAmountCustomers(int amountCustomers) {
         this.amountCustomers = amountCustomers;
      }

      public int getAvgSatisfactionModifier() {
         return this.satisfactionModifier / this.amountModifiers;
      }

      public House getHouse() {
         return this.house;
      }

      public int getAvgSatisfactionMainCustomer() {
         return this.amountMainCustomers == 0 ? 0 : this.satisfactionMainCustomer / this.amountMainCustomers;
      }

      public int getAmountMainCustomers() {
         return this.amountMainCustomers;
      }

      public void setAmountMainCustomers(int amountMainCustomers) {
         this.amountMainCustomers = amountMainCustomers;
      }

      public void setHouse(House house) {
         this.house = house;
      }

      public void setSatisfactionMainCustomer(int satisfactionMainCustomer) {
         this.satisfactionMainCustomer = satisfactionMainCustomer;
      }

      @Override
      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         result = 31 * result + (this.characters == null ? 0 : this.characters.hashCode());
         return 31 * result + (this.type == null ? 0 : this.type.hashCode());
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         }

         if (obj == null) {
            return false;
         }

         if (this.getClass() != obj.getClass()) {
            return false;
         }

         StatCollector.ActivityData other = (StatCollector.ActivityData)obj;
         if (!this.getOuterType().equals(other.getOuterType())) {
            return false;
         }

         if (this.characters == null) {
            if (other.characters != null) {
               return false;
            }
         } else if (!this.characters.equals(other.characters)) {
            return false;
         }

         return this.type == other.type;
      }

      private StatCollector getOuterType() {
         return StatCollector.this;
      }
   }

   public class MoneyChangeData implements Comparable<StatCollector.MoneyChangeData> {
      private String source;
      private long amount;

      public MoneyChangeData(String source, long amount) {
         this.source = source;
         this.amount = amount;
      }

      public void addToValue(long amount) {
         this.amount += amount;
      }

      public MoneyChangeData(MyEvent event) {
         MoneyChangedEvent moneyChangedEvent = (MoneyChangedEvent)event;
         this.amount = moneyChangedEvent.getAmount();
         this.source = moneyChangedEvent.getSource().toString();
      }

      @Override
      public int hashCode() {
         int prime = 31;
         int result = 1;
         result = 31 * result + this.getOuterType().hashCode();
         return 31 * result + (this.source == null ? 0 : this.source.hashCode());
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         }

         if (obj == null) {
            return false;
         }

         if (this.getClass() != obj.getClass()) {
            return false;
         }

         StatCollector.MoneyChangeData other = (StatCollector.MoneyChangeData)obj;
         if (!this.getOuterType().equals(other.getOuterType())) {
            return false;
         }

         if (this.source == null) {
            if (other.source != null) {
               return false;
            }
         } else if (!this.source.equals(other.source)) {
            return false;
         }

         return true;
      }

      public String getSource() {
         return this.source;
      }

      public void setSource(String source) {
         this.source = source;
      }

      public long getAmount() {
         return this.amount;
      }

      public void setAmount(long amount) {
         this.amount = amount;
      }

      private StatCollector getOuterType() {
         return StatCollector.this;
      }

      public int compareTo(StatCollector.MoneyChangeData o) {
         return o == null ? 0 : Long.valueOf(this.amount).compareTo(o.getAmount());
      }
   }

   public class ShiftData {
      private Long moneyBeforeShift = null;
      private Long moneyAfterShift;
      private List<StatCollector.ActivityData> activityDataList = new ArrayList<>();
      private List<StatCollector.MoneyChangeData> moneyEarnedList = new ArrayList<>();
      private List<StatCollector.MoneyChangeData> moneySpentList = new ArrayList<>();

      public void recordEvent(MyEvent e) {
         if (e.getType() == EventType.ACTIVITYPERFORMED) {
            this.initMoneyBeforeShift();
            RunningActivity activity = (RunningActivity)e.getSource();
            StatCollector.ActivityData activityData = StatCollector.this.new ActivityData(activity, this);
            if (!this.activityDataList.contains(activityData)) {
               if (activityData.getType() != ActivityType.WHORESTREETS && activityData.getType() != ActivityType.EVENT) {
                  this.activityDataList.add(activityData);
               }
            } else {
               StatCollector.ActivityData origActivityData = this.activityDataList.get(this.activityDataList.indexOf(activityData));
               origActivityData.addToIncome(activityData.getIncome());
               origActivityData.amountCustomers += activityData.amountCustomers;
               origActivityData.amountMainCustomers += activityData.amountMainCustomers;
               origActivityData.satisfactionModifier = origActivityData.satisfactionModifier + activityData.satisfactionModifier;
               origActivityData.amountModifiers++;
               origActivityData.satisfactionMainCustomer = origActivityData.satisfactionMainCustomer + activityData.satisfactionMainCustomer;
            }
         } else if (e.getType() == EventType.MONEYEARNED) {
            this.initMoneyBeforeShift();
            StatCollector.MoneyChangeData moneyChangeData = StatCollector.this.new MoneyChangeData(e);
            if (!this.moneyEarnedList.contains(moneyChangeData)) {
               this.moneyEarnedList.add(moneyChangeData);
            } else {
               StatCollector.MoneyChangeData existingMoneyChangeData = this.moneyEarnedList.get(this.moneyEarnedList.indexOf(moneyChangeData));
               existingMoneyChangeData.addToValue(moneyChangeData.getAmount());
            }
         } else if (e.getType() == EventType.MONEYSPENT) {
            this.initMoneyBeforeShift();
            StatCollector.MoneyChangeData moneyChangeData = StatCollector.this.new MoneyChangeData(e);
            if (!this.moneySpentList.contains(moneyChangeData)) {
               this.moneySpentList.add(moneyChangeData);
            } else {
               StatCollector.MoneyChangeData existingMoneyChangeData = this.moneySpentList.get(this.moneySpentList.indexOf(moneyChangeData));
               existingMoneyChangeData.addToValue(moneyChangeData.getAmount());
            }
         }
      }

      public void initMoneyBeforeShift(MyEvent event) {
         if (this.moneyBeforeShift == null) {
            this.moneyBeforeShift = Jasbro.getInstance().getData().getMoney();
         }
      }

      public void initMoneyBeforeShift() {
         if (this.moneyBeforeShift == null) {
            this.moneyBeforeShift = Jasbro.getInstance().getData().getMoney();
         }
      }

      public long getMoneyBeforeShift() {
         return this.moneyBeforeShift == null ? 0L : this.moneyBeforeShift;
      }

      public long getMoneyAfterShift() {
         return this.moneyAfterShift == null ? Jasbro.getInstance().getData().getMoney() : this.moneyAfterShift;
      }

      public void setMoneyAfterShift(long moneyAfterShift) {
         this.moneyAfterShift = moneyAfterShift;
      }

      public List<StatCollector.ActivityData> getActivityDataList() {
         if (this.activityDataList == null) {
            this.activityDataList = new ArrayList<>();
         }

         Comparator<StatCollector.ActivityData> comparator = new Comparator<StatCollector.ActivityData>() {
            public int compare(StatCollector.ActivityData o1, StatCollector.ActivityData o2) {
               return Integer.valueOf(o1.getIncome()).compareTo(o2.getIncome());
            }
         };
         Collections.sort(this.activityDataList, Collections.reverseOrder(comparator));
         return this.activityDataList;
      }

      public int getAverageSatisfactionModifier(RunningActivity activity) {
         List<Customer> customers = new ArrayList<>();
         customers.addAll(activity.getCustomers());
         if (customers.size() == 0) {
            return 0;
         }

         int sum = 0;

         for (Customer customer : customers) {
            for (Customer.SatisfactionModifier satisfactionModifier : customer.getSatisfactionModifiers()) {
               if (satisfactionModifier.getSource() == activity) {
                  sum += satisfactionModifier.getModifiedBy();
               }
            }
         }

         return sum / customers.size();
      }

      public int getAverageFinalSatisfaction(RunningActivity activity) {
         List<Customer> customers = new ArrayList<>();
         customers.addAll(activity.getMainCustomers());
         if (customers.size() == 0) {
            return 0;
         }

         int sum = 0;

         for (Customer customer : customers) {
            sum += customer.getSatisfactionAmount();
         }

         return sum / customers.size();
      }

      public long calculateSum(List<StatCollector.MoneyChangeData> moneyChangeDataList) {
         int sum = 0;

         for (StatCollector.MoneyChangeData moneyChangeData : moneyChangeDataList) {
            sum = (int)(sum + moneyChangeData.getAmount());
         }

         return sum;
      }

      public List<StatCollector.MoneyChangeData> getMoneyEarnedList() {
         if (this.moneyEarnedList == null) {
            this.moneyEarnedList = new ArrayList<>();
         }

         Collections.sort(this.moneyEarnedList);
         Collections.reverse(this.moneyEarnedList);
         return this.moneyEarnedList;
      }

      public List<StatCollector.MoneyChangeData> getMoneySpentList() {
         if (this.moneySpentList == null) {
            this.moneySpentList = new ArrayList<>();
         }

         Collections.sort(this.moneySpentList);
         Collections.reverse(this.moneySpentList);
         return this.moneySpentList;
      }

      public boolean isClosed() {
         return this.moneyAfterShift != null;
      }

      public boolean isInitialized() {
         return this.moneyBeforeShift != null;
      }
   }
}
