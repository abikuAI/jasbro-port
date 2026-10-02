/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.events.business.Customer;
import java.util.List;

public interface BusinessMainActivity {
    public int rateCustomer(Customer var1);

    public void addMainCustomer(Customer var1);

    public boolean hasMainCustomer();

    public List<Customer> getMainCustomers();

    public List<Charakter> getCharacters();

    public ActivityType getType();
}

