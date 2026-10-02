/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import org.w3c.dom.Element;

public interface ActivityRequirementParser {
    public ActivityRequirement parse(Element var1);
}

