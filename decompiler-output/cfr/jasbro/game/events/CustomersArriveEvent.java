/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events;

import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import java.util.List;

public class CustomersArriveEvent
extends MyEvent {
    private List<Customer> customers;

    public CustomersArriveEvent(House house, List<Customer> customers) {
        super(EventType.CUSTOMERSARRIVE, house);
        this.customers = customers;
    }

    public House getHouse() {
        return (House)this.getSource();
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}

