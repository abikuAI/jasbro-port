/*
 * Decompiled with CFR 0.152.
 */
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

public class StatCollector
implements CentralEventlistener {
    private transient StatScreen statScreen;
    private transient Map<Time, ShiftData> shiftDataMap = new HashMap<Time, ShiftData>();
    private transient ShiftData dailyData;
    private transient ShiftData previousDayData;

    public StatCollector() {
        this.reset();
    }

    @Override
    public void handleCentralEvent(MyEvent e) {
        Time shift = Jasbro.getInstance().getData().getTime();
        if (this.shiftDataMap.get((Object)shift).isClosed() && (e.getType() == EventType.ACTIVITYPERFORMED || e.getType() == EventType.MONEYEARNED || e.getType() == EventType.MONEYSPENT)) {
            this.reset();
        }
        if (e.getType() == EventType.SHIFTSTART) {
            this.initMoneyBeforeShift();
        }
        this.dailyData.recordEvent(e);
        this.shiftDataMap.get((Object)shift).recordEvent(e);
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
        this.dailyData = new ShiftData();
        this.shiftDataMap.clear();
        for (Time time : Time.values()) {
            this.shiftDataMap.put(time, new ShiftData());
        }
        Time shift = Jasbro.getInstance().getData().getTime();
        if (shift != Time.MORNING) {
            this.shiftDataMap.get((Object)Time.MORNING).setMoneyAfterShift(0L);
        }
    }

    private void initMoneyBeforeShift() {
        Time shift = Jasbro.getInstance().getData().getTime();
        this.dailyData.initMoneyBeforeShift();
        this.shiftDataMap.get((Object)shift).initMoneyBeforeShift();
    }

    public void setMoneyAfterShift(long money) {
        Time shift = Jasbro.getInstance().getData().getTime().getPreviousTimeOfDay();
        this.dailyData.setMoneyAfterShift(money);
        this.shiftDataMap.get((Object)shift).setMoneyAfterShift(money);
    }

    public ShiftData getShiftDataMap(Time time) {
        return this.shiftDataMap.get((Object)time);
    }

    public ShiftData getDailyData() {
        return this.dailyData;
    }

    public ShiftData getPreviousDayData() {
        return this.previousDayData;
    }

    public class MoneyChangeData
    implements Comparable<MoneyChangeData> {
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

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + this.getOuterType().hashCode();
            result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
            return result;
        }

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
            MoneyChangeData other = (MoneyChangeData)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            return !(this.source == null ? other.source != null : !this.source.equals(other.source));
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

        @Override
        public int compareTo(MoneyChangeData o) {
            if (o == null) {
                return 0;
            }
            return Long.valueOf(this.amount).compareTo(o.getAmount());
        }
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

        public ActivityData(RunningActivity activity, ShiftData currentShift) {
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
            if (this.amountMainCustomers == 0) {
                return 0;
            }
            return this.satisfactionMainCustomer / this.amountMainCustomers;
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

        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = 31 * result + this.getOuterType().hashCode();
            result = 31 * result + (this.characters == null ? 0 : this.characters.hashCode());
            result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
            return result;
        }

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
            ActivityData other = (ActivityData)obj;
            if (!this.getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (this.characters == null ? other.characters != null : !this.characters.equals(other.characters)) {
                return false;
            }
            return this.type == other.type;
        }

        private StatCollector getOuterType() {
            return StatCollector.this;
        }
    }

    public class ShiftData {
        private Long moneyBeforeShift = null;
        private Long moneyAfterShift;
        private List<ActivityData> activityDataList = new ArrayList<ActivityData>();
        private List<MoneyChangeData> moneyEarnedList = new ArrayList<MoneyChangeData>();
        private List<MoneyChangeData> moneySpentList = new ArrayList<MoneyChangeData>();

        public void recordEvent(MyEvent e) {
            if (e.getType() == EventType.ACTIVITYPERFORMED) {
                this.initMoneyBeforeShift();
                RunningActivity activity = (RunningActivity)e.getSource();
                ActivityData activityData = new ActivityData(activity, this);
                if (!this.activityDataList.contains(activityData)) {
                    if (activityData.getType() != ActivityType.WHORESTREETS && activityData.getType() != ActivityType.EVENT) {
                        this.activityDataList.add(activityData);
                    }
                } else {
                    ActivityData origActivityData = this.activityDataList.get(this.activityDataList.indexOf(activityData));
                    origActivityData.addToIncome(activityData.getIncome());
                    origActivityData.amountCustomers += activityData.amountCustomers;
                    origActivityData.amountMainCustomers += activityData.amountMainCustomers;
                    origActivityData.satisfactionModifier = origActivityData.satisfactionModifier + activityData.satisfactionModifier;
                    origActivityData.amountModifiers++;
                    origActivityData.satisfactionMainCustomer = origActivityData.satisfactionMainCustomer + activityData.satisfactionMainCustomer;
                }
            } else if (e.getType() == EventType.MONEYEARNED) {
                this.initMoneyBeforeShift();
                MoneyChangeData moneyChangeData = new MoneyChangeData(e);
                if (!this.moneyEarnedList.contains(moneyChangeData)) {
                    this.moneyEarnedList.add(moneyChangeData);
                } else {
                    MoneyChangeData existingMoneyChangeData = this.moneyEarnedList.get(this.moneyEarnedList.indexOf(moneyChangeData));
                    existingMoneyChangeData.addToValue(moneyChangeData.getAmount());
                }
            } else if (e.getType() == EventType.MONEYSPENT) {
                this.initMoneyBeforeShift();
                MoneyChangeData moneyChangeData = new MoneyChangeData(e);
                if (!this.moneySpentList.contains(moneyChangeData)) {
                    this.moneySpentList.add(moneyChangeData);
                } else {
                    MoneyChangeData existingMoneyChangeData = this.moneySpentList.get(this.moneySpentList.indexOf(moneyChangeData));
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
            if (this.moneyBeforeShift == null) {
                return 0L;
            }
            return this.moneyBeforeShift;
        }

        public long getMoneyAfterShift() {
            if (this.moneyAfterShift == null) {
                return Jasbro.getInstance().getData().getMoney();
            }
            return this.moneyAfterShift;
        }

        public void setMoneyAfterShift(long moneyAfterShift) {
            this.moneyAfterShift = moneyAfterShift;
        }

        public List<ActivityData> getActivityDataList() {
            if (this.activityDataList == null) {
                this.activityDataList = new ArrayList<ActivityData>();
            }
            Comparator<ActivityData> comparator = new Comparator<ActivityData>(){

                @Override
                public int compare(ActivityData o1, ActivityData o2) {
                    return Integer.valueOf(o1.getIncome()).compareTo(o2.getIncome());
                }
            };
            Collections.sort(this.activityDataList, Collections.reverseOrder(comparator));
            return this.activityDataList;
        }

        public int getAverageSatisfactionModifier(RunningActivity activity) {
            ArrayList<Customer> customers = new ArrayList<Customer>();
            customers.addAll(activity.getCustomers());
            if (customers.size() == 0) {
                return 0;
            }
            int sum = 0;
            for (Customer customer : customers) {
                for (Customer.SatisfactionModifier satisfactionModifier : customer.getSatisfactionModifiers()) {
                    if (satisfactionModifier.getSource() != activity) continue;
                    sum += satisfactionModifier.getModifiedBy();
                }
            }
            return sum / customers.size();
        }

        public int getAverageFinalSatisfaction(RunningActivity activity) {
            ArrayList<Customer> customers = new ArrayList<Customer>();
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

        public long calculateSum(List<MoneyChangeData> moneyChangeDataList) {
            int sum = 0;
            for (MoneyChangeData moneyChangeData : moneyChangeDataList) {
                sum = (int)((long)sum + moneyChangeData.getAmount());
            }
            return sum;
        }

        public List<MoneyChangeData> getMoneyEarnedList() {
            if (this.moneyEarnedList == null) {
                this.moneyEarnedList = new ArrayList<MoneyChangeData>();
            }
            Collections.sort(this.moneyEarnedList);
            Collections.reverse(this.moneyEarnedList);
            return this.moneyEarnedList;
        }

        public List<MoneyChangeData> getMoneySpentList() {
            if (this.moneySpentList == null) {
                this.moneySpentList = new ArrayList<MoneyChangeData>();
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

