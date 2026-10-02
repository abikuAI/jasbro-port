package jasbro.game.character.activities;

import jasbro.game.character.Charakter;
import jasbro.game.events.business.Customer;
import java.util.List;

public interface BusinessMainActivity {
   int rateCustomer(Customer var1);

   void addMainCustomer(Customer var1);

   boolean hasMainCustomer();

   List<Customer> getMainCustomers();

   List<Charakter> getCharacters();

   ActivityType getType();
}
