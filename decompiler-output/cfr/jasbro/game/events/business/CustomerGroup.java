/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerGroup
extends Customer {
    private Logger log = LogManager.getLogger(CustomerGroup.class);
    private List<Customer> customers = new ArrayList<Customer>();

    @Override
    public String getName() {
        String text;
        Object[] attributesTmp = new Object[]{};
        Object[] attributes = new Object[]{this.customers.size(), TextUtil.t("groupgender", (Person)this.customers.get(0), attributesTmp), this.customers.get(0).getName()};
        String key = this.getGender() == Gender.MALE ? "nocheck.groupname." + this.customers.get(0).getType().toString() + ".male" : "nocheck.groupname." + this.customers.get(0).getType().toString() + ".female";
        if (!key.equals(text = TextUtil.t(key, attributes))) {
            return text;
        }
        return TextUtil.t("groupname", attributes);
    }

    @Override
    public CustomerType getType() {
        return CustomerType.GROUP;
    }

    @Override
    public Gender getGender() {
        Util.GenderAmounts genderAmounts = Util.getGenderAmounts(new ArrayList<Person>(this.customers));
        if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
            return Gender.FEMALE;
        }
        if (genderAmounts.getGenderAmount(Gender.FEMALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
            return Gender.MALE;
        }
        if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FEMALE) == 0) {
            return Gender.FUTA;
        }
        this.log.error("No valid group gender found.");
        return Gender.MALE;
    }

    public List<Customer> getCustomers() {
        return this.customers;
    }

    @Override
    public int getInitialSatisfaction() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getInitialSatisfaction();
        }
        return sum;
    }

    @Override
    public int getMoney() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getMoney();
        }
        return sum;
    }

    @Override
    public int getSatisfactionAmount() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getSatisfactionAmount();
        }
        return sum / this.customers.size();
    }

    @Override
    public int getMaxSecondaryActivities() {
        return 0;
    }

    @Override
    public float getImportance() {
        float sum = 0.0f;
        for (Customer customer : this.customers) {
            sum += customer.getImportance();
        }
        return sum / (float)this.customers.size();
    }

    @Override
    public void addToSatisfaction(int mod, Object satisfactionModifier) {
        for (Customer customer : this.customers) {
            customer.addToSatisfaction(mod, satisfactionModifier);
        }
    }

    @Override
    public int payFixed(int amount) {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.payFixed(amount / this.customers.size());
        }
        return sum;
    }

    @Override
    public int pay(int amount, float modifier) {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.pay(amount / this.customers.size(), modifier);
        }
        return sum;
    }

    @Override
    public int pay(float modifier) {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.pay(modifier / (float)this.customers.size());
        }
        return sum;
    }

    @Override
    public List<Customer.SatisfactionModifier> getSatisfactionModifiers() {
        ArrayList<Customer.SatisfactionModifier> satisfactionModifiers = new ArrayList<Customer.SatisfactionModifier>();
        for (Customer customer : this.customers) {
            satisfactionModifiers.addAll(customer.getSatisfactionModifiers());
        }
        return satisfactionModifiers;
    }

    @Override
    public void changePayModifier(float modifier) {
        for (Customer customer : this.customers) {
            customer.changePayModifier(modifier);
        }
    }

    @Override
    public int getHitpoints() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getHitpoints();
        }
        return sum;
    }

    @Override
    public int getMaxHitpoints() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getMaxHitpoints();
        }
        return sum;
    }

    @Override
    public float modifyHitpoints(float modifier) {
        for (Customer customer : this.customers) {
            if (customer.getHitpoints() <= 0 || customer.getMaxHitpoints() <= customer.getHitpoints()) continue;
            return customer.modifyHitpoints(modifier);
        }
        return this.customers.get(0).modifyHitpoints(modifier);
    }

    @Override
    public float getDamage() {
        float sum = 0.0f;
        for (Customer customer : this.customers) {
            if (customer.getHitpoints() <= 0) continue;
            sum += customer.getDamage();
        }
        return sum;
    }

    @Override
    public int getArmor() {
        for (Customer customer : this.customers) {
            if (customer.getHitpoints() <= 0) continue;
            return customer.getArmor();
        }
        return 0;
    }

    @Override
    public float takeDamage(float power) {
        for (Customer customer : this.customers) {
            if (customer.getHitpoints() <= 0) continue;
            return customer.takeDamage(power);
        }
        return 0.0f;
    }

    @Override
    public int getInitialMoney() {
        int sum = 0;
        for (Customer customer : this.customers) {
            sum += customer.getInitialMoney();
        }
        return sum;
    }

    @Override
    public List<Inventory.ItemData> spawnItems() {
        ArrayList<Inventory.ItemData> items = new ArrayList<Inventory.ItemData>();
        for (Customer customer : this.getCustomers()) {
            items.addAll(customer.spawnItems());
        }
        return Util.getItemListNormalized(items);
    }

    @Override
    public Inventory.ItemData getItem() {
        if (this.getCustomers().size() > 0) {
            return this.getCustomers().get(Util.getInt(0, this.getCustomers().size())).getItem();
        }
        return null;
    }
}

