/*
 * Decompiled with CFR 0.152.
 */
package jasbro.texts;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.battle.Monster;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;

public class TextWrapper {
    private Person object;
    private Boolean personIsPlayer = null;

    public TextWrapper(Person person) {
        this.object = person;
    }

    public String getGender() {
        return this.object.getGender().getText();
    }

    public String getName() {
        if (this.checkPersonIsPlayer()) {
            return TextUtil.t("You");
        }
        return this.object.getName();
    }

    public String getType() {
        if (this.object instanceof Charakter) {
            Charakter character = (Charakter)this.object;
            return character.getType().getText();
        }
        if (this.object instanceof CustomerGroup) {
            return TextUtil.t("customerGroup");
        }
        if (this.object instanceof Monster) {
            return TextUtil.t("monster");
        }
        return TextUtil.t("customer");
    }

    public Person getObject() {
        return this.object;
    }

    public String getHeshe() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("you");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("it");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("he");
            }
            return TextUtil.t("she");
        }
        return TextUtil.t("they");
    }

    public String getAss() {
        int random = Util.getInt(1, 6);
        if (random == 1) {
            return TextUtil.t("backdoor");
        }
        if (random == 2) {
            return TextUtil.t("arse");
        }
        if (random == 3) {
            return TextUtil.t("fuckhole");
        }
        if (random == 4 && this.object.getGender() == Gender.MALE) {
            return TextUtil.t("boycunt");
        }
        return TextUtil.t("ass");
    }

    public String getPenis() {
        int random = Util.getInt(1, 8);
        if (random == 1) {
            return TextUtil.t("penis");
        }
        if (random == 2) {
            return TextUtil.t("cock");
        }
        if (random == 3) {
            return TextUtil.t("errection");
        }
        if (random == 4) {
            return TextUtil.t("fleshrod");
        }
        if (random == 5) {
            return TextUtil.t("dick");
        }
        if (random == 6) {
            return TextUtil.t("member");
        }
        return TextUtil.t("meatstick");
    }

    public String getPenispussy() {
        if (this.object.getGender() == Gender.MALE) {
            int random = Util.getInt(1, 8);
            if (random == 1) {
                return TextUtil.t("penis");
            }
            if (random == 2) {
                return TextUtil.t("cock");
            }
            if (random == 3) {
                return TextUtil.t("erection");
            }
            if (random == 4) {
                return TextUtil.t("fleshrod");
            }
            if (random == 5) {
                return TextUtil.t("dick");
            }
            if (random == 6) {
                return TextUtil.t("member");
            }
            return TextUtil.t("meatstick");
        }
        int random = Util.getInt(1, 4);
        if (random == 1) {
            return TextUtil.t("pussy");
        }
        if (random == 2) {
            return TextUtil.t("vagina");
        }
        if (random == 3) {
            return TextUtil.t("fuckhole");
        }
        return TextUtil.t("cunt");
    }

    public String getSemen() {
        int random = Util.getInt(1, 8);
        if (random == 1) {
            return TextUtil.t("cum");
        }
        if (random == 2) {
            return TextUtil.t("cream");
        }
        if (random == 3) {
            return TextUtil.t("spunk");
        }
        if (random == 4) {
            return TextUtil.t("spooge");
        }
        if (random == 5) {
            return TextUtil.t("juice");
        }
        if (random == 6) {
            return TextUtil.t("semen");
        }
        return TextUtil.t("jism");
    }

    public String getAsspussy() {
        if (this.object.getGender() == Gender.MALE) {
            int random = Util.getInt(1, 4);
            if (random == 1) {
                return TextUtil.t("backdoor");
            }
            if (random == 2) {
                return TextUtil.t("arse");
            }
            if (random == 3) {
                return TextUtil.t("fuckhole");
            }
            return TextUtil.t("ass");
        }
        int random = Util.getInt(1, 4);
        if (random == 1) {
            return TextUtil.t("pussy");
        }
        if (random == 2) {
            return TextUtil.t("vagina");
        }
        if (random == 3) {
            return TextUtil.t("fuckhole");
        }
        return TextUtil.t("cunt");
    }

    public String getBoygirl() {
        if (this.object.getGender() == Gender.MALE) {
            return TextUtil.t("boy");
        }
        return TextUtil.t("girl");
    }

    public String getHeShe() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("You");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("It");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("He");
            }
            return TextUtil.t("She");
        }
        return TextUtil.t("They");
    }

    public String getHisher() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("your");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("its");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("his");
            }
            return TextUtil.t("her");
        }
        return TextUtil.t("their");
    }

    public String getHisHer() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("Your");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("Its");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("His");
            }
            return TextUtil.t("Her");
        }
        return TextUtil.t("Their");
    }

    public String getHimher() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("you");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("it");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("him");
            }
            return TextUtil.t("her");
        }
        return TextUtil.t("them");
    }

    public String getHimHer() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("You");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("It");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("Him");
            }
            return TextUtil.t("Her");
        }
        return TextUtil.t("Them");
    }

    public String getHimselfherself() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("yourself");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("itself");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("himself");
            }
            return TextUtil.t("herself");
        }
        return TextUtil.t("themselfes");
    }

    public String getHimselfHerself() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("Yourself");
            }
            if (this.object instanceof Monster) {
                return TextUtil.t("Itself");
            }
            if (this.object.getGender() == Gender.MALE) {
                return TextUtil.t("Himself");
            }
            return TextUtil.t("Herself");
        }
        return TextUtil.t("Themselfes");
    }

    public String getIsare() {
        if (!(this.object instanceof CustomerGroup)) {
            if (this.checkPersonIsPlayer()) {
                return TextUtil.t("are");
            }
            return TextUtil.t("is");
        }
        return TextUtil.t("are");
    }

    public void setObject(Person object) {
        this.object = object;
    }

    private boolean checkPersonIsPlayer() {
        if (this.personIsPlayer == null) {
            this.personIsPlayer = !ConfigHandler.isProtagonistPlayer() || this.object != Jasbro.getInstance().getData().getProtagonist() ? Boolean.valueOf(false) : Boolean.valueOf(true);
        }
        return this.personIsPlayer;
    }

    public boolean isMale() {
        return this.object.getGender() == Gender.MALE;
    }

    public boolean isFemale() {
        return this.object.getGender() != Gender.MALE;
    }

    public boolean isFuta() {
        return this.object.getGender() == Gender.FUTA;
    }
}

