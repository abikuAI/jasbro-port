package jasbro.texts;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.battle.Monster;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.Person;
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
      return this.checkPersonIsPlayer() ? TextUtil.t("You") : this.object.getName();
   }

   public String getType() {
      if (this.object instanceof Charakter) {
         Charakter character = (Charakter)this.object;
         return character.getType().getText();
      } else if (this.object instanceof CustomerGroup) {
         return TextUtil.t("customerGroup");
      } else {
         return this.object instanceof Monster ? TextUtil.t("monster") : TextUtil.t("customer");
      }
   }

   public Person getObject() {
      return this.object;
   }

   public String getHeshe() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("you");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("it");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("he") : TextUtil.t("she");
         }
      } else {
         return TextUtil.t("they");
      }
   }

   public String getAss() {
      int random = Util.getInt(1, 6);
      if (random == 1) {
         return TextUtil.t("backdoor");
      } else if (random == 2) {
         return TextUtil.t("arse");
      } else if (random == 3) {
         return TextUtil.t("fuckhole");
      } else {
         return random == 4 && this.object.getGender() == Gender.MALE ? TextUtil.t("boycunt") : TextUtil.t("ass");
      }
   }

   public String getPenis() {
      int random = Util.getInt(1, 8);
      if (random == 1) {
         return TextUtil.t("penis");
      } else if (random == 2) {
         return TextUtil.t("cock");
      } else if (random == 3) {
         return TextUtil.t("errection");
      } else if (random == 4) {
         return TextUtil.t("fleshrod");
      } else if (random == 5) {
         return TextUtil.t("dick");
      } else {
         return random == 6 ? TextUtil.t("member") : TextUtil.t("meatstick");
      }
   }

   public String getPenispussy() {
      if (this.object.getGender() == Gender.MALE) {
         int random = Util.getInt(1, 8);
         if (random == 1) {
            return TextUtil.t("penis");
         } else if (random == 2) {
            return TextUtil.t("cock");
         } else if (random == 3) {
            return TextUtil.t("erection");
         } else if (random == 4) {
            return TextUtil.t("fleshrod");
         } else if (random == 5) {
            return TextUtil.t("dick");
         } else {
            return random == 6 ? TextUtil.t("member") : TextUtil.t("meatstick");
         }
      } else {
         int random = Util.getInt(1, 4);
         if (random == 1) {
            return TextUtil.t("pussy");
         } else if (random == 2) {
            return TextUtil.t("vagina");
         } else {
            return random == 3 ? TextUtil.t("fuckhole") : TextUtil.t("cunt");
         }
      }
   }

   public String getSemen() {
      int random = Util.getInt(1, 8);
      if (random == 1) {
         return TextUtil.t("cum");
      } else if (random == 2) {
         return TextUtil.t("cream");
      } else if (random == 3) {
         return TextUtil.t("spunk");
      } else if (random == 4) {
         return TextUtil.t("spooge");
      } else if (random == 5) {
         return TextUtil.t("juice");
      } else {
         return random == 6 ? TextUtil.t("semen") : TextUtil.t("jism");
      }
   }

   public String getAsspussy() {
      if (this.object.getGender() == Gender.MALE) {
         int random = Util.getInt(1, 4);
         if (random == 1) {
            return TextUtil.t("backdoor");
         } else if (random == 2) {
            return TextUtil.t("arse");
         } else {
            return random == 3 ? TextUtil.t("fuckhole") : TextUtil.t("ass");
         }
      } else {
         int random = Util.getInt(1, 4);
         if (random == 1) {
            return TextUtil.t("pussy");
         } else if (random == 2) {
            return TextUtil.t("vagina");
         } else {
            return random == 3 ? TextUtil.t("fuckhole") : TextUtil.t("cunt");
         }
      }
   }

   public String getBoygirl() {
      return this.object.getGender() == Gender.MALE ? TextUtil.t("boy") : TextUtil.t("girl");
   }

   public String getHeShe() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("You");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("It");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("He") : TextUtil.t("She");
         }
      } else {
         return TextUtil.t("They");
      }
   }

   public String getHisher() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("your");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("its");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("his") : TextUtil.t("her");
         }
      } else {
         return TextUtil.t("their");
      }
   }

   public String getHisHer() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("Your");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("Its");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("His") : TextUtil.t("Her");
         }
      } else {
         return TextUtil.t("Their");
      }
   }

   public String getHimher() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("you");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("it");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("him") : TextUtil.t("her");
         }
      } else {
         return TextUtil.t("them");
      }
   }

   public String getHimHer() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("You");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("It");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("Him") : TextUtil.t("Her");
         }
      } else {
         return TextUtil.t("Them");
      }
   }

   public String getHimselfherself() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("yourself");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("itself");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("himself") : TextUtil.t("herself");
         }
      } else {
         return TextUtil.t("themselfes");
      }
   }

   public String getHimselfHerself() {
      if (!(this.object instanceof CustomerGroup)) {
         if (this.checkPersonIsPlayer()) {
            return TextUtil.t("Yourself");
         } else if (this.object instanceof Monster) {
            return TextUtil.t("Itself");
         } else {
            return this.object.getGender() == Gender.MALE ? TextUtil.t("Himself") : TextUtil.t("Herself");
         }
      } else {
         return TextUtil.t("Themselfes");
      }
   }

   public String getIsare() {
      if (!(this.object instanceof CustomerGroup)) {
         return this.checkPersonIsPlayer() ? TextUtil.t("are") : TextUtil.t("is");
      } else {
         return TextUtil.t("are");
      }
   }

   public void setObject(Person object) {
      this.object = object;
   }

   private boolean checkPersonIsPlayer() {
      if (this.personIsPlayer == null) {
         if (ConfigHandler.isProtagonistPlayer() && this.object == Jasbro.getInstance().getData().getProtagonist()) {
            this.personIsPlayer = true;
         } else {
            this.personIsPlayer = false;
         }
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
