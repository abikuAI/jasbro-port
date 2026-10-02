/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities;

import jasbro.game.events.business.Customer;
import java.util.List;

public interface BusinessSecondaryActivity {
    public void addAttendingCustomer(Customer var1);

    public int getAppeal();

    public int getMaxAttendees();

    public List<Customer> getCustomers();
}

