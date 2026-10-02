/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;

public enum CustomerType {
    LORD,
    MINORNOBLE,
    BUSINESSMAN,
    MERCHANT,
    SOLDIER,
    PEASANT,
    BUM,
    GROUP,
    CELEBRITY;

    private int maxSecondaryActivities = 2;

    private CustomerType() {
    }

    private CustomerType(int maxSecondaryActivities) {
        this.maxSecondaryActivities = maxSecondaryActivities;
    }

    public String getText() {
        return TextUtil.firstCharUpper(TextUtil.t(this.toString(), new Person(){

            @Override
            public Gender getGender() {
                return Gender.MALE;
            }

            @Override
            public String getName() {
                return CustomerType.this.toString();
            }
        }));
    }

    public String getText(final Customer customer) {
        return TextUtil.t(this.toString(), new Person(){

            @Override
            public Gender getGender() {
                return customer.getGender();
            }

            @Override
            public String getName() {
                return CustomerType.this.toString();
            }
        });
    }

    public int getMaxSecondaryActivities() {
        return this.maxSecondaryActivities;
    }

    public static Customer generateCustomer(CustomerType customerType) {
        Customer customer;
        switch (customerType) {
            case CELEBRITY: {
                customer = new Customer(CELEBRITY, Util.getInt(12000, 15000), -35, 50.0f);
                break;
            }
            case LORD: {
                customer = new Customer(LORD, Util.getInt(2000, 5000), -25, 10.0f);
                break;
            }
            case MINORNOBLE: {
                customer = new Customer(MINORNOBLE, Util.getInt(900, 2000), -20, 3.0f);
                break;
            }
            case BUSINESSMAN: {
                customer = new Customer(BUSINESSMAN, Util.getInt(500, 1000), -15, 2.0f);
                break;
            }
            case MERCHANT: {
                customer = new Customer(MERCHANT, Util.getInt(350, 600), -10, 1.2f);
                break;
            }
            case SOLDIER: {
                customer = new Customer(SOLDIER, Util.getInt(200, 400), -5, 1.0f);
                break;
            }
            case PEASANT: {
                customer = new Customer(PEASANT, Util.getInt(80, 250), 5, 0.8f);
                break;
            }
            case GROUP: {
                customer = new CustomerGroup();
                break;
            }
            default: {
                customer = new Customer(BUM, Util.getInt(30, 80), 50, 0.2f);
            }
        }
        return customer;
    }
}

