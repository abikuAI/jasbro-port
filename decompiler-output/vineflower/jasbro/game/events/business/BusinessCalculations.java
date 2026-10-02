package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.BathAttendant;
import jasbro.game.character.activities.sub.business.Offerings;
import jasbro.game.character.activities.sub.business.PublicUse;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.traits.Trait;
import jasbro.game.housing.House;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BusinessCalculations {
   public int calculateCustomerAmount(List<Charakter> relevantCharacters, Fame fame) {
      int amountCharacters = relevantCharacters.size();
      int step = (int)Math.sqrt(amountCharacters);
      if (step == 0) {
         int var12 = true;
      }

      int baseAmount = 3;
      long fameValue = fame.getFame();
      int fameModifier = 0;
      float famePerCustomer = 2.0F;

      for (int i = 0; fameValue > 0L; i++) {
         fameModifier++;
         fameValue = (long)((float)fameValue - famePerCustomer);
         if (i != 0) {
            famePerCustomer *= 1.75F;
         }
      }

      int finalAmount = baseAmount + fameModifier;
      int rndFactor = Util.getInt(-1 - finalAmount / 10, 2 + finalAmount / 10);
      return finalAmount + rndFactor;
   }

   public Fame calculateFame(House house, List<Charakter> relevantCharacters) {
      int fameAmount = 0;

      for (Charakter character : relevantCharacters) {
         fameAmount = (int)(fameAmount + character.getFame().getFame());
      }

      fameAmount = (int)(fameAmount + house.getFame().getFame() + fameAmount);
      Fame fame = new Fame();
      fame.modifyFame(fameAmount);
      return fame;
   }

   public List<SpawnData.CustomerData> getBaseChances(Fame fame) {
      List<SpawnData.CustomerData> customerDataList = new ArrayList<>();
      customerDataList.add(new SpawnData.CustomerData(CustomerType.CELEBRITY, 0 + this.getModifier(fame.getFame(), 2, 50000)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.LORD, 0 + this.getModifier(fame.getFame(), 10, 5000)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.MINORNOBLE, 0 + this.getModifier(fame.getFame(), 15, 2000)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.BUSINESSMAN, 1 + this.getModifier(fame.getFame(), 25, 800)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.GROUP, 5 + this.getModifier(fame.getFame(), 15, 1000)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.MERCHANT, 2 + this.getModifier(fame.getFame(), 40, 400)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.SOLDIER, 5 + this.getModifier(fame.getFame(), 50, 200)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.PEASANT, 15 + this.getModifier(fame.getFame(), 90, 50)));
      customerDataList.add(new SpawnData.CustomerData(CustomerType.BUM, 100));
      return customerDataList;
   }

   public int getModifier(long fame, int max, int fameToIncrease) {
      long modifier = fame / fameToIncrease;
      if (modifier > max) {
         modifier = max;
         fame -= max * fameToIncrease;

         for (int var8 = fameToIncrease * 5; fame >= var8; fame -= var8) {
            modifier++;
            var8 = (int)(var8 * Math.sqrt(var8));
         }
      }

      return (int)modifier;
   }

   public void randomizeCharacters(List<Charakter> characters) {
      Collections.shuffle(characters);
   }

   public void assignCustomers(List<BusinessMainActivity> mainActivities, List<Customer> customers, List<Customer> remainingCustomers) {
      int bestValue;
      do {
         BusinessMainActivity activity = null;
         Customer customer = null;
         bestValue = 0;

         for (int i = 0; i < mainActivities.size(); i++) {
            for (int j = 0; j < customers.size(); j++) {
               BusinessMainActivity curActivity = mainActivities.get(i);
               Customer curCustomer = customers.get(j);
               if (curCustomer.getMoney() <= 0) {
                  j--;
                  customers.remove(curCustomer);
               } else {
                  int curValue = curActivity.rateCustomer(curCustomer);
                  if (activity != null) {
                     for (Charakter character : activity.getCharacters()) {
                        for (Trait trait : character.getTraits()) {
                           curValue = trait.modifyCustomerRating(curValue, curCustomer, activity);
                        }
                     }
                  }

                  if (curValue > bestValue) {
                     bestValue = curValue;
                     activity = curActivity;
                     customer = curCustomer;
                  }
               }
            }
         }

         if (bestValue > 0 && activity != null && customer != null) {
            activity.addMainCustomer(customer);
            customers.remove(customer);
            mainActivities.remove(activity);
            if (remainingCustomers != null) {
               remainingCustomers.remove(customer);
            }
         }
      } while (customers.size() > 0 && mainActivities.size() > 0 && bestValue != 0);
   }

   public void assignCustomersToSecondaryActivities(List<BusinessSecondaryActivity> secondaryActivities, List<Customer> initialCustomers) {
      List<Customer> customers = new ArrayList<>();

      for (Customer customer : initialCustomers) {
         if (customer instanceof CustomerGroup) {
            CustomerGroup customerGroup = (CustomerGroup)customer;
            customers.addAll(customerGroup.getCustomers());
         } else {
            customers.add(customer);
         }
      }

      Collections.sort(secondaryActivities, Collections.reverseOrder(new Comparator<BusinessSecondaryActivity>() {
         public int compare(BusinessSecondaryActivity o1, BusinessSecondaryActivity o2) {
            return Integer.valueOf(o1.getAppeal()).compareTo(o2.getAppeal());
         }
      }));
      Collections.sort(customers, Collections.reverseOrder(new Comparator<Customer>() {
         public int compare(Customer o1, Customer o2) {
            return Integer.valueOf(o1.getMoney()).compareTo(o2.getMoney());
         }
      }));
      List<Class<? extends BusinessSecondaryActivity>> unwantedActivities = new ArrayList<>();

      for (Customer customer : customers) {
         if (customer.getMaxSecondaryActivities() != 0) {
            int amountActivities = 0;
            int chance = 66;
            unwantedActivities.clear();

            for (BusinessSecondaryActivity curActivity : secondaryActivities) {
               if (!unwantedActivities.contains(curActivity.getClass())) {
                  if (curActivity instanceof Strip && (customer.getType() == CustomerType.BUM || customer.getType() == CustomerType.PEASANT)) {
                     chance = 33;
                  }

                  if (curActivity instanceof Bartend && (customer.getType() == CustomerType.BUM || customer.getType() == CustomerType.PEASANT)) {
                     chance = 85;
                  }

                  if (curActivity instanceof Attend && (customer.getType() == CustomerType.LORD || customer.getType() == CustomerType.CELEBRITY)) {
                     chance = 85;
                  }

                  if (curActivity instanceof BathAttendant && customer.getType() == CustomerType.BUM) {
                     chance = 33;
                  }

                  if (curActivity instanceof PublicUse) {
                     if (customer.getType() == CustomerType.BUM || customer.getType() == CustomerType.PEASANT) {
                        chance = 90;
                     } else if (customer.getType() == CustomerType.SOLDIER) {
                        chance = 70;
                     } else {
                        chance = 10;
                     }
                  }

                  if (curActivity instanceof SellFood && customer.getType() != CustomerType.BUM) {
                     chance = 75;
                  }

                  if (curActivity instanceof Offerings && (customer.getType() == CustomerType.SOLDIER || customer.getType() == CustomerType.MERCHANT)) {
                     chance = 75;
                  }

                  if (Util.getInt(0, 100) < chance) {
                     if (curActivity.getCustomers().size() < curActivity.getMaxAttendees()) {
                        amountActivities++;
                        curActivity.addAttendingCustomer(customer);
                        unwantedActivities.add((Class<? extends BusinessSecondaryActivity>)curActivity.getClass());
                        if (amountActivities >= customer.getMaxSecondaryActivities()) {
                           break;
                        }
                     }
                  } else {
                     unwantedActivities.add((Class<? extends BusinessSecondaryActivity>)curActivity.getClass());
                  }
               }
            }
         }
      }
   }
}
