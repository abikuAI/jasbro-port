/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Orgy
extends RunningActivity {
    private Map<Gender, Integer> genderAmounts = new EnumMap<Gender, Integer>(Gender.class);
    private boolean threesome = false;

    @Override
    public void init() {
        for (Gender gender : Gender.values()) {
            this.genderAmounts.put(gender, 0);
        }
        for (Charakter character : this.getCharacters()) {
            Gender curGender = character.getGender();
            this.genderAmounts.put(curGender, this.genderAmounts.get((Object)curGender) + 1);
        }
        if (this.getCharacters().size() == 3) {
            this.threesome = true;
        }
    }

    @Override
    public List<RunningActivity.ModificationData> getStatModifications() {
        ArrayList<RunningActivity.ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        if (this.threesome) {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.VAGINAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.ANAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.ORAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.TITFUCK));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.FOREPLAY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2f, Sextype.GROUP));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.04f, BaseAttributeTypes.STAMINA));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
        } else {
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.VAGINAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.ANAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.ORAL));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.TITFUCK));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.FOREPLAY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5f, Sextype.GROUP));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.STRENGTH));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.STAMINA));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -45.0f, EssentialAttributes.ENERGY));
            modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -1.5f, EssentialAttributes.MOTIVATION));
        }
        modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01f, BaseAttributeTypes.OBEDIENCE));
        return modifications;
    }

    @Override
    public MessageData getBaseMessage() {
        List<Charakter> characters = this.getCharacters();
        ArrayList<ImageData> images = new ArrayList<ImageData>();
        for (Charakter character : characters) {
            images.addAll(character.getImages());
        }
        ArrayList<ImageTag> tags = new ArrayList<ImageTag>();
        tags.addAll(ImageTag.getAssociatedImageTags(characters.toArray(new Person[characters.size()])));
        ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, images);
        String message = this.threesome ? TextUtil.t("threesome.basic", this.getCharacters()) : TextUtil.t("orgy.basic", this.getCharacters());
        return new MessageData(message, image, characters.get(0).getBackground());
    }

    @Override
    public Sextype getSextype() {
        return Sextype.GROUP;
    }

    public Map<Gender, Integer> getGenderAmounts() {
        return this.genderAmounts;
    }

    public void setGenderAmounts(Map<Gender, Integer> genderAmounts) {
        this.genderAmounts = genderAmounts;
    }
}

