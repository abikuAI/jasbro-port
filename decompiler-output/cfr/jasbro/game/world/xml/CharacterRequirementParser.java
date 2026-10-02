/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.world.xml;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import org.w3c.dom.Element;

public interface CharacterRequirementParser {
    public CharacterRequirement parse(Element var1);
}

