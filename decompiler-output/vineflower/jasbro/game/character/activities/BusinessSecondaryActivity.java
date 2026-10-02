package jasbro.game.character.activities;

import jasbro.game.events.business.Customer;
import java.util.List;

public interface BusinessSecondaryActivity {
   void addAttendingCustomer(Customer var1);

   int getAppeal();

   int getMaxAttendees();

   List<Customer> getCustomers();
}
