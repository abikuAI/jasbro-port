/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class AttributeChangedEvent
extends MyEvent {
    private float amount;
    private RunningActivity activity;

    public AttributeChangedEvent(EventType eventType, Attribute attribute, float amount, RunningActivity activity) {
        super(eventType, attribute);
        this.amount = amount;
        this.activity = activity;
    }

    public float getAmount() {
        return this.amount;
    }

    public RunningActivity getActivity() {
        return this.activity;
    }

    public Attribute getAttribute() {
        return (Attribute)this.getSource();
    }
}

