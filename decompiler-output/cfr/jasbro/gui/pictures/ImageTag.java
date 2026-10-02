/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pictures;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum ImageTag {
    ICON(true, ImageTagGroup.GENERAL),
    CLEANED(ImageTagGroup.GENERAL),
    STANDARD(ImageTagGroup.HIDDEN),
    CLOTHED(STANDARD, STANDARD, ImageTagGroup.GENERAL),
    NAKED(STANDARD, STANDARD, ImageTagGroup.GENERAL),
    MAID(STANDARD, ImageTagGroup.GENERAL),
    ADVERTISE(STANDARD, ImageTagGroup.GENERAL),
    SLEEP(STANDARD, ImageTagGroup.GENERAL),
    DANCE(STANDARD, ImageTagGroup.ACTIVITY),
    FUTA(true, ImageTagGroup.GENERAL),
    CATGIRL(true, ImageTagGroup.GENERAL),
    PREGNANT(STANDARD, true, ImageTagGroup.GENERAL),
    TANNED(true, ImageTagGroup.GENERAL),
    LESBIAN(NAKED, true, ImageTagGroup.SEX),
    VAGINAL(NAKED, ImageTagGroup.SEX),
    ANAL(VAGINAL, ImageTagGroup.SEX),
    FOREPLAY(NAKED, ImageTagGroup.SEX),
    ORAL(FOREPLAY, ImageTagGroup.SEX),
    BLOWJOB(ORAL, ORAL, ImageTagGroup.SEX),
    SIXTYNINE(BLOWJOB, ORAL, ImageTagGroup.SEX),
    CUNNILINGUS(ORAL, ORAL, ImageTagGroup.SEX),
    GLORYHOLE(ORAL, ORAL, ImageTagGroup.SEX),
    TITFUCK(BLOWJOB, ImageTagGroup.SEX),
    BONDAGE(VAGINAL, ImageTagGroup.SEX),
    TRIBADISM(NAKED, VAGINAL, ImageTagGroup.SEX),
    FORCED(VAGINAL, true, ImageTagGroup.SEX),
    GROUP(VAGINAL, true, ImageTagGroup.SEX),
    LAPDANCE(DANCE, ImageTagGroup.SEX),
    DOMINATRIX(BONDAGE, ImageTagGroup.SEX),
    STRAPON(VAGINAL, ImageTagGroup.SEX),
    AFTERSEX(NAKED, ImageTagGroup.SEX),
    GANGBANG(GROUP, GROUP, ImageTagGroup.GROUP),
    BUKKAKE(GROUP, GROUP, ImageTagGroup.GROUP),
    MONSTER(FORCED, true, ImageTagGroup.MONSTER),
    MONSTERBIRTH(SLEEP, ImageTagGroup.MONSTER),
    TENTACLE(MONSTER, ImageTagGroup.MONSTER),
    NORMALDICK(MONSTER, ImageTagGroup.MONSTER),
    FLUID(MONSTER, ImageTagGroup.MONSTER),
    REGULARCLOTHES(ImageTagGroup.CLOTHING),
    SWIMSUIT(NAKED, ImageTagGroup.CLOTHING),
    NOBLE(ImageTagGroup.CLOTHING),
    SCHOOLUNIFORM(ImageTagGroup.CLOTHING),
    SIMPLEARMOR(ImageTagGroup.CLOTHING),
    QUALITYARMOR(SIMPLEARMOR, ImageTagGroup.CLOTHING),
    LINGERIE(ImageTagGroup.CLOTHING),
    BUNNYEARS(ImageTagGroup.CLOTHING),
    APRON(ImageTagGroup.CLOTHING),
    MAIDOUTFIT(ImageTagGroup.CLOTHING),
    NURSEOUTFIT(ImageTagGroup.CLOTHING),
    COOK(MAID, MAID, ImageTagGroup.ACTIVITY),
    CLEAN(MAID, MAID, ImageTagGroup.ACTIVITY),
    FIGHT(CLOTHED, ImageTagGroup.ACTIVITY),
    HURT(CLOTHED, ImageTagGroup.ACTIVITY),
    VICTORIOUS(CLOTHED, ImageTagGroup.ACTIVITY),
    BARTEND(MAID, ImageTagGroup.ACTIVITY),
    STUDY(SCHOOLUNIFORM, ImageTagGroup.ACTIVITY),
    TEACH(STUDY, ImageTagGroup.ACTIVITY),
    SWIM(SWIMSUIT, ImageTagGroup.ACTIVITY),
    NURSE(NURSEOUTFIT, ImageTagGroup.ACTIVITY),
    BATHE(SWIMSUIT, ImageTagGroup.ACTIVITY),
    SUNBATHE(SWIMSUIT, ImageTagGroup.ACTIVITY),
    PLAY(STANDARD, ImageTagGroup.ACTIVITY),
    MASTURBATION(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    HANDJOB(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    FOOTJOB(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    KISS(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    LICKING(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    FINGERING(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    FISTING(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    DILDO(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    TOUCHING(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    FROTTING(FOREPLAY, FOREPLAY, ImageTagGroup.FOREPLAY),
    FUTAONMALE(true, ImageTagGroup.FILTERTAGS),
    LOLI(true, ImageTagGroup.FILTERTAGS),
    BLACKANDWHITE(true, ImageTagGroup.FILTERTAGS),
    THREE_D(true, ImageTagGroup.FILTERTAGS),
    COSPLAY(true, ImageTagGroup.FILTERTAGS),
    CHARACTERNOFUTA(null, ImageTagGroup.CHARACTERGENDER),
    FUTAGIRL(null, FUTA, ImageTagGroup.CHARACTERGENDER),
    MALEOTHER(ImageTagGroup.OTHERGENDER),
    FEMALEOTHER(ImageTagGroup.OTHERGENDER),
    FUTAOTHER(null, FUTA, ImageTagGroup.OTHERGENDER),
    MALEFEMALEGROUPOTHER(ImageTagGroup.OTHERGENDER),
    MALEFUTAGROUPOTHER(null, FUTA, ImageTagGroup.OTHERGENDER),
    FEMALEFUTAGROUPOTHER(null, FUTA, ImageTagGroup.OTHERGENDER),
    MIXEDGROUPOTHER(null, FUTA, ImageTagGroup.OTHERGENDER),
    NOOTHER(ImageTagGroup.AMOUNTPEOPLE),
    ONEOTHER(ImageTagGroup.AMOUNTPEOPLE),
    TWOOTHER(null, GROUP, ImageTagGroup.AMOUNTPEOPLE),
    THREEOTHER(null, GROUP, ImageTagGroup.AMOUNTPEOPLE),
    FOUROTHER(null, GROUP, ImageTagGroup.AMOUNTPEOPLE),
    FIVEOTHER(null, GROUP, ImageTagGroup.AMOUNTPEOPLE),
    MANYOTHER(null, GROUP, ImageTagGroup.AMOUNTPEOPLE),
    DOMINANTPOSITION(null, ImageTagGroup.DOMINANCE),
    SUBMISSIVEPOSITION(null, ImageTagGroup.DOMINANCE),
    SELF(null, ImageTagGroup.DOMINANCE);

    private ImageTag replacementTag;
    private ImageTag includedTag;
    private boolean excludeTag = false;
    private ImageTagGroup imageTagGroup;

    private ImageTag(ImageTagGroup imageTagGroup) {
        this.imageTagGroup = imageTagGroup;
    }

    private ImageTag(ImageTag alternateTag, ImageTagGroup imageTagGroup) {
        this.replacementTag = alternateTag;
        this.imageTagGroup = imageTagGroup;
    }

    private ImageTag(ImageTag alternateTag, boolean excludeTag, ImageTagGroup imageTagGroup) {
        this.replacementTag = alternateTag;
        this.excludeTag = excludeTag;
        this.imageTagGroup = imageTagGroup;
    }

    private ImageTag(ImageTag alternateTag, ImageTag includedTag, ImageTagGroup imageTagGroup) {
        this.replacementTag = alternateTag;
        this.includedTag = includedTag;
        this.imageTagGroup = imageTagGroup;
    }

    private ImageTag(ImageTag alternateTag, boolean excludeTag, ImageTag includedTag, ImageTagGroup imageTagGroup) {
        this.replacementTag = alternateTag;
        this.excludeTag = excludeTag;
        this.includedTag = includedTag;
        this.imageTagGroup = imageTagGroup;
    }

    private ImageTag(boolean excludeTag, ImageTagGroup imageTagGroup) {
        this.excludeTag = excludeTag;
        this.imageTagGroup = imageTagGroup;
    }

    public ImageTag getReplacementTag() {
        return this.replacementTag;
    }

    public void setReplacementTag(ImageTag alternateTag) {
        this.replacementTag = alternateTag;
    }

    public boolean isOfType(ImageTag imageTag) {
        if (this == imageTag) {
            return true;
        }
        for (ImageTag parentTag = this.getReplacementTag(); parentTag != null; parentTag = parentTag.getReplacementTag()) {
            if (parentTag != imageTag) continue;
            return true;
        }
        return false;
    }

    public ImageTag getIncludedTag() {
        return this.includedTag;
    }

    public ImageTagGroup getImageTagGroup() {
        return this.imageTagGroup;
    }

    public String getText() {
        String text = TextUtil.tNoCheck("imagetag." + this.toString());
        if (text == null) {
            text = this.toString();
            text = text.charAt(0) + text.substring(1).toLowerCase();
        }
        if (this.includedTag != null) {
            Object[] arguments = new Object[]{this.includedTag.getText()};
            text = TextUtil.html(text + " " + TextUtil.t("imagetag.includestag", arguments));
        }
        return text;
    }

    public String getDescription() {
        String text = TextUtil.tNoCheck("imagetag." + this.toString() + ".description");
        if (!(this.toString() + ".description").equals(text)) {
            return text;
        }
        return null;
    }

    public static List<ImageTag> getImageTags(ImageTagGroup imageTagGroup) {
        ArrayList<ImageTag> tags = new ArrayList<ImageTag>();
        for (ImageTag imageTag : ImageTag.values()) {
            if (imageTag.getImageTagGroup() != imageTagGroup) continue;
            tags.add(imageTag);
        }
        return tags;
    }

    public boolean isExcludeTag() {
        return this.excludeTag;
    }

    public void setExcludeTag(boolean excludeTag) {
        this.excludeTag = excludeTag;
    }

    public static List<ImageTag> getAssociatedImageTags(Person ... person) {
        ArrayList<ImageTag> imageTags = new ArrayList<ImageTag>();
        if (person.length == 0) {
            return imageTags;
        }
        List<Person> people = Arrays.asList(person);
        Util.GenderAmounts amounts = Util.getGenderAmounts(people);
        int amount = people.size();
        if (amount > 2) {
            imageTags.add(GROUP);
        }
        if (amounts.getGenderAmount(Gender.FUTA) > 0) {
            imageTags.add(FUTA);
        }
        if (amount > 1 && amounts.getGenderAmount(Gender.MALE) == 0) {
            imageTags.add(LESBIAN);
        } else if (amount == amounts.getGenderAmount(Gender.MALE)) {
            imageTags.add(LESBIAN);
        }
        imageTags.add(ImageTag.getAmountOthersImageTag(amount));
        imageTags.add(ImageTag.getGenderOthersImageTag(person));
        if (person[0].getGender() == Gender.FUTA) {
            imageTags.add(FUTAGIRL);
        } else {
            imageTags.add(CHARACTERNOFUTA);
        }
        while (imageTags.contains(null)) {
            imageTags.remove(null);
        }
        return imageTags;
    }

    public static ImageTag getAmountOthersImageTag(int amountPeople) {
        switch (amountPeople) {
            case 1: {
                return NOOTHER;
            }
            case 2: {
                return ONEOTHER;
            }
            case 3: {
                return TWOOTHER;
            }
            case 4: {
                return THREEOTHER;
            }
            case 5: {
                return FOUROTHER;
            }
            case 6: {
                return FIVEOTHER;
            }
        }
        return MANYOTHER;
    }

    public static ImageTag getGenderOthersImageTag(Person ... person) {
        boolean futaPresent;
        if (person.length == 0) {
            return null;
        }
        ArrayList<Person> people = new ArrayList<Person>(Arrays.asList(person));
        people.remove(0);
        Util.GenderAmounts amounts = Util.getGenderAmounts(people);
        boolean malePresent = amounts.getGenderAmount(Gender.MALE) > 0;
        boolean femalePresent = amounts.getGenderAmount(Gender.FEMALE) > 0;
        boolean bl = futaPresent = amounts.getGenderAmount(Gender.FUTA) > 0;
        if (!(malePresent || femalePresent || futaPresent)) {
            return null;
        }
        if (malePresent && !femalePresent && !futaPresent) {
            return MALEOTHER;
        }
        if (!malePresent && femalePresent && !futaPresent) {
            return FEMALEOTHER;
        }
        if (!malePresent && !femalePresent && futaPresent) {
            return FUTAOTHER;
        }
        if (malePresent && femalePresent && !futaPresent) {
            return MALEFEMALEGROUPOTHER;
        }
        if (malePresent && !femalePresent && futaPresent) {
            return MALEFUTAGROUPOTHER;
        }
        if (!malePresent && femalePresent && futaPresent) {
            return FEMALEFUTAGROUPOTHER;
        }
        return MIXEDGROUPOTHER;
    }

    public static ImageTag getSpecificTag(ImageTag imageTag, Set<ImageTag> imageTags) {
        boolean changed;
        block0: do {
            changed = false;
            for (ImageTag currentTag : imageTags) {
                if (currentTag.getReplacementTag() != imageTag) continue;
                changed = true;
                imageTag = currentTag;
                continue block0;
            }
        } while (changed);
        if ((imageTag == VAGINAL || imageTag == ANAL) && imageTags.contains((Object)STRAPON)) {
            return STRAPON;
        }
        return imageTag;
    }
}

