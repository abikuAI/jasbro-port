/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  scala.concurrent.forkjoin.ThreadLocalRandom
 */
package jasbro;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.game.world.CharacterLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import scala.concurrent.forkjoin.ThreadLocalRandom;

public class Util {
    private static List<AttributeType> attributeTypes;

    public static Random getRnd() {
        return ThreadLocalRandom.current();
    }

    public static int getInt(int start, int end) {
        int number = Util.getRnd().nextInt(end - start);
        return number += start;
    }

    public static double getPercent(double amount, int percent) {
        return amount * ((double)percent / 100.0);
    }

    public static List<AttributeType> getAttributeTypes() {
        if (attributeTypes == null) {
            attributeTypes = new ArrayList<AttributeType>();
            for (EssentialAttributes essentialAttributes : EssentialAttributes.values()) {
                attributeTypes.add(essentialAttributes);
            }
            for (Enum enum_ : BaseAttributeTypes.values()) {
                attributeTypes.add((AttributeType)((Object)enum_));
            }
            for (Enum enum_ : Sextype.values()) {
                attributeTypes.add((AttributeType)((Object)enum_));
            }
            for (Enum enum_ : SpecializationAttribute.values()) {
                attributeTypes.add((AttributeType)((Object)enum_));
            }
        }
        return attributeTypes;
    }

    public static List<Charakter> getSlaves(List<Charakter> characters) {
        ArrayList<Charakter> slaves = new ArrayList<Charakter>();
        for (Charakter character : characters) {
            if (character.getType() != CharacterType.SLAVE) continue;
            slaves.add(character);
        }
        return slaves;
    }

    public static List<Charakter> getTrainers(List<Charakter> characters) {
        ArrayList<Charakter> trainers = new ArrayList<Charakter>();
        for (Charakter character : characters) {
            if (character.getType() != CharacterType.TRAINER) continue;
            trainers.add(character);
        }
        return trainers;
    }

    public static GenderAmounts getGenderAmounts(List<Person> people) {
        GenderAmounts genderAmounts = new GenderAmounts();
        for (Person person : people) {
            if (!(person instanceof CustomerGroup)) {
                genderAmounts.add(person.getGender());
                continue;
            }
            CustomerGroup customerGroup = (CustomerGroup)person;
            for (Person person2 : customerGroup.getCustomers()) {
                genderAmounts.add(person2.getGender());
            }
        }
        return genderAmounts;
    }

    public static List<Inventory.ItemData> getItemListNormalized(List<Inventory.ItemData> items) {
        for (int i = 0; i < items.size(); ++i) {
            for (int j = i + 1; j < items.size(); ++j) {
                if (!items.get(i).getItem().getId().equals(items.get(j).getItem().getId())) continue;
                items.get(i).setAmount(items.get(i).getAmount() + items.get(j).getAmount());
                items.remove(j);
                --j;
            }
        }
        return items;
    }

    public static float getAverage(SpecializationType specializationType, Charakter character) {
        List<AttributeType> attributeTypes = specializationType.getAssociatedAttributes();
        if (attributeTypes.size() > 0) {
            float sum = 0.0f;
            for (AttributeType attributeType : attributeTypes) {
                sum += character.getAttribute(attributeType).getInternValue();
            }
            return sum / (float)attributeTypes.size();
        }
        return 0.0f;
    }

    public static boolean isValidFileName(String filename) {
        if (filename != null) {
            return filename.matches("[a-zA-Z0-9_\\-]+");
        }
        return false;
    }

    public static List<Charakter> getAllCharactersSuperLocation(CharacterLocation location) {
        ArrayList<Charakter> characters = new ArrayList<Charakter>();
        if (location instanceof Room) {
            List<Room> rooms = ((Room)location).getHouse().getRooms();
            for (Room room : rooms) {
                characters.addAll(room.getCurrentUsage().getCharacters());
            }
        } else {
            characters.addAll(location.getCurrentUsage().getCharacters());
        }
        return characters;
    }

    public static TypeAmounts getTypeAmounts(List<Charakter> people) {
        TypeAmounts typeAmounts = new TypeAmounts();
        for (Charakter person : people) {
            typeAmounts.add(person.getType());
            if (person.getType().isChildType()) {
                typeAmounts.setChildPresent(true);
                continue;
            }
            typeAmounts.setAdultPresent(true);
        }
        return typeAmounts;
    }

    public static List<CharacterBase> getBasesByTypeAndGender(CharacterType characterType, Gender gender, List<CharacterBase> bases) {
        if (gender == Gender.FUTA) {
            gender = Gender.FEMALE;
        }
        ArrayList<CharacterBase> retBases = new ArrayList<CharacterBase>();
        for (CharacterBase base : bases) {
            if (characterType == base.getType() && gender == base.getGender()) {
                retBases.add(base);
                continue;
            }
            if (characterType != null || gender != base.getGender() || base.getType() != CharacterType.SLAVE && base.getType() != CharacterType.TRAINER) continue;
            retBases.add(base);
        }
        return retBases;
    }

    public static class TypeAmounts {
        private Map<CharacterType, Integer> typeAmountMap = new HashMap<CharacterType, Integer>();
        private boolean childPresent = false;
        private boolean adultPresent = false;

        public TypeAmounts() {
            for (CharacterType type : CharacterType.values()) {
                this.typeAmountMap.put(type, 0);
            }
        }

        public void add(CharacterType type) {
            this.typeAmountMap.put(type, this.typeAmountMap.get((Object)type) + 1);
        }

        public boolean isChildPresent() {
            return this.childPresent;
        }

        public void setChildPresent(boolean childPresent) {
            this.childPresent = childPresent;
        }

        public Map<CharacterType, Integer> getTypeAmountMap() {
            return this.typeAmountMap;
        }

        public int getTrainerAmount() {
            return this.typeAmountMap.get((Object)CharacterType.TRAINER);
        }

        public int getSlaveAmount() {
            return this.typeAmountMap.get((Object)CharacterType.SLAVE);
        }

        public int getInfantAmount() {
            return this.typeAmountMap.get((Object)CharacterType.INFANT);
        }

        public int getChildAmount() {
            return this.typeAmountMap.get((Object)CharacterType.CHILD);
        }

        public int getTeenAmount() {
            return this.typeAmountMap.get((Object)CharacterType.TEENAGER);
        }

        public boolean isAdultPresent() {
            return this.adultPresent;
        }

        public void setAdultPresent(boolean adultPresent) {
            this.adultPresent = adultPresent;
        }
    }

    public static class GenderAmounts {
        private Map<Gender, Integer> genderAmountMap = new HashMap<Gender, Integer>();

        public GenderAmounts() {
            for (Gender gender : Gender.values()) {
                this.genderAmountMap.put(gender, 0);
            }
        }

        public int getGenderAmount(Gender gender) {
            return this.genderAmountMap.get((Object)gender);
        }

        public void add(Gender gender) {
            this.genderAmountMap.put(gender, this.genderAmountMap.get((Object)gender) + 1);
        }
    }
}

