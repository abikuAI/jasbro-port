package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterSchool {
   public List<CharacterSchool.Training> getTrainingOpportunities(Charakter character) {
      List<CharacterSchool.Training> possibleTraining = new ArrayList<>();

      for (SpecializationType specializationType : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
         if (specializationType != SpecializationType.TRAINER
            && specializationType != SpecializationType.SLAVE
            && specializationType != SpecializationType.UNDERAGE
            && specializationType.isTeachable()
            && !character.getSpecializations().contains(specializationType)
            && character.getNumberTrees() < Jasbro.maxTrees) {
            possibleTraining.add(new CharacterSchool.SpecializationBasicTraining(specializationType, character));
         }
      }

      for (SpecializationType specializationType : character.getSpecializations()) {
         if (specializationType.isTeachable()) {
            CharacterSchool.SpecializationTraining training = new CharacterSchool.SpecializationTraining(specializationType, character);
            possibleTraining.add(training);
            if (!training.fulfillsRequirements() && specializationType.getAssociatedAttributes().size() > 1) {
               for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
                  CharacterSchool.AttributeSpecializationTraining attributeSpecializationTraining = new CharacterSchool.AttributeSpecializationTraining(
                     attributeType, character
                  );
                  if (attributeSpecializationTraining.fulfillsRequirements()) {
                     boolean containsTraining = false;

                     for (CharacterSchool.Training curTraining : possibleTraining) {
                        if (curTraining instanceof CharacterSchool.AttributeSpecializationTraining) {
                           CharacterSchool.AttributeSpecializationTraining curAttributeSpecializationTraining = (CharacterSchool.AttributeSpecializationTraining)curTraining;
                           if (curAttributeSpecializationTraining.getAttributeType() == attributeType) {
                              containsTraining = true;
                              break;
                           }
                        }
                     }

                     if (!containsTraining) {
                        possibleTraining.add(attributeSpecializationTraining);
                     }
                  }
               }
            }
         }
      }

      Collections.sort(possibleTraining);
      return possibleTraining;
   }

   public List<CharacterSchool.Training> getTrainingOpportunitiesHideUnavailable(Charakter selectedCharacter) {
      List<CharacterSchool.Training> trainingOptions = this.getTrainingOpportunities(selectedCharacter);

      for (int i = 0; i < trainingOptions.size(); i++) {
         CharacterSchool.Training training = trainingOptions.get(i);
         if (!training.fulfillsRequirements()) {
            trainingOptions.remove(training);
            i--;
         }
      }

      return trainingOptions;
   }

   public static class AttributeSpecializationTraining extends CharacterSchool.Training {
      private AttributeType attributeType;
      private Charakter character;

      public AttributeSpecializationTraining(AttributeType attributeType, Charakter character) {
         this.attributeType = attributeType;
         this.character = character;
      }

      @Override
      public String getName() {
         Object[] arguments = new Object[]{this.attributeType.getText()};
         return TextUtil.t("school.specialize", arguments);
      }

      @Override
      public String getDescription() {
         Object[] arguments = new Object[]{this.attributeType.getText()};
         String description = TextUtil.t("school.specialice.description", arguments);
         if (!this.fulfillsRequirements()) {
            description = description + " " + this.character.getName() + " needs more training to learn this";
         }

         return description;
      }

      @Override
      public void apply() {
         Jasbro.getInstance().getData().spendMoney(this.getPrice(), "School");
         Attribute attribute = this.character.getAttribute(this.attributeType);
         attribute.setMaxValue(attribute.getMaxValue() + this.attributeType.getRaiseMaxBy());
      }

      @Override
      public long getPrice() {
         Attribute attribute = this.character.getAttribute(this.attributeType);
         int d = 1;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.GENIUS)) {
            d = 2;
         }

         int level = 1 + (attribute.getMaxValue() - this.attributeType.getDefaultMax()) / this.attributeType.getRaiseMaxBy();
         switch (level) {
            case 1:
               return 500 / d;
            case 2:
               return 2500 / d;
            case 3:
               return 5000 / d;
            case 4:
               return 15000 / d;
            case 5:
               return 35000 / d;
            case 6:
               return 70000 / d;
            case 7:
               return 150000 / d;
            default:
               return 100L * (level * level * level * level / 1000 * 1000);
         }
      }

      @Override
      public boolean fulfillsRequirements() {
         Attribute attribute = this.character.getAttribute(this.attributeType);
         return attribute.getMaxValue() == attribute.getInternValue();
      }

      public AttributeType getAttributeType() {
         return this.attributeType;
      }
   }

   public static class SpecializationBasicTraining extends CharacterSchool.Training {
      private SpecializationType specializationType;
      private Charakter character;

      public SpecializationBasicTraining(SpecializationType specializationType, Charakter character) {
         this.specializationType = specializationType;
         this.character = character;
      }

      @Override
      public String getName() {
         return TextUtil.t("school.basic." + this.specializationType.toString() + ".title", this.character);
      }

      @Override
      public String getDescription() {
         String description = TextUtil.t("school.basic." + this.specializationType.toString() + ".description", this.character);
         if (!this.fulfillsRequirements()) {
            description = description + " " + TextUtil.t("school.requirementsnotmet", this.character);
         }

         return description;
      }

      @Override
      public void apply() {
         Jasbro.getInstance().getData().spendMoney(this.getPrice(), "School");
         this.character.addSpecialization(this.specializationType);
      }

      @Override
      public long getPrice() {
         int amountSpecialisations = this.character.getSpecializations().size() - 1;
         int d = 1;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSCHOOL)) {
            d = 2;
         }

         switch (amountSpecialisations) {
            case 1:
               return 100 / d;
            case 2:
               return 1000 / d;
            case 3:
               return 10000 / d;
            case 4:
               return 20000 / d;
            case 5:
               return 50000 / d;
            case 6:
               return 100000 / d;
            case 7:
               return 150000 / d;
            default:
               return 10L
                  * (amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations * amountSpecialisations / 1000 * 1000);
         }
      }

      @Override
      public boolean fulfillsRequirements() {
         int amount = 0;
         if (this.specializationType == SpecializationType.DOMINATRIX) {
            return this.character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 0.5F;
         }

         for (AttributeType attribute : this.specializationType.getAssociatedAttributes()) {
            if (this.character.getAttribute(attribute).getInternValue() > 0.5F) {
               return true;
            }

            amount++;
         }

         return amount == 0;
      }
   }

   public static class SpecializationTraining extends CharacterSchool.Training {
      private SpecializationType specializationType;
      private Charakter character;

      public SpecializationTraining(SpecializationType specializationType, Charakter character) {
         this.specializationType = specializationType;
         this.character = character;
      }

      @Override
      public String getName() {
         Object[] arguments = new Object[]{this.specializationType.getText(), this.specializationType.getTrainingLevel(this.character)};
         return TextUtil.t("school.specializationtraining.name", this.character, arguments);
      }

      @Override
      public String getDescription() {
         AttributeType attributeType = this.character.getAttribute(this.specializationType.getAssociatedAttributes().get(0)).getAttributeType();
         int level = this.specializationType.getTrainingLevel(this.character);
         Object[] arguments = new Object[]{
            TextUtil.listAttributes(this.specializationType.getAssociatedAttributes()), attributeType.getDefaultMax() + level * attributeType.getRaiseMaxBy()
         };
         String description = TextUtil.t("school.specializationtraining.description", this.character, arguments);
         if (!this.fulfillsRequirements()) {
            arguments[0] = attributeType.getDefaultMax()
               - attributeType.getRaiseMaxBy()
               + attributeType.getRaiseMaxBy() * (this.specializationType.getTrainingLevel(this.character) - 1);
            description = description + " " + TextUtil.t("school.specializationtraining.requirementsnotmet", this.character, arguments);
         }

         return description;
      }

      @Override
      public void apply() {
         Jasbro.getInstance().getData().spendMoney(this.getPrice(), "School");
         int level = this.specializationType.getTrainingLevel(this.character);

         for (AttributeType attributeType : this.specializationType.getAssociatedAttributes()) {
            int newMax = attributeType.getDefaultMax() + level * attributeType.getRaiseMaxBy();
            Attribute attribute = this.character.getAttribute(attributeType);

            while (attribute.getMaxValue() < newMax) {
               attribute.setMaxValue(attribute.getMaxValue() + attributeType.getRaiseMaxBy());
            }
         }
      }

      @Override
      public long getPrice() {
         int level = this.specializationType.getTrainingLevel(this.character);
         int d = 1;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.DISCOUNTSCHOOL)) {
            d = 2;
         }

         switch (level) {
            case 1:
               return 1000 / d;
            case 2:
               return 5000 / d;
            case 3:
               return 25000 / d;
            case 4:
               return 50000 / d;
            case 5:
               return 100000 / d;
            case 6:
               return 150000 / d;
            case 7:
               return 200000 / d;
            default:
               return 100L * (level * level * level * level / 1000 * 1000);
         }
      }

      @Override
      public boolean fulfillsRequirements() {
         int sum = 0;
         int amount = 0;
         Attribute attribute = null;

         for (AttributeType attributeType : this.specializationType.getAssociatedAttributes()) {
            attribute = this.character.getAttribute(attributeType);
            sum = (int)(sum + attribute.getInternValue());
            amount++;
         }

         return sum / amount
            >= attribute.getAttributeType().getDefaultMax()
               - attribute.getAttributeType().getRaiseMaxBy()
               + attribute.getAttributeType().getRaiseMaxBy() * (this.specializationType.getTrainingLevel(this.character) - 1);
      }

      public SpecializationType getSpecializationType() {
         return this.specializationType;
      }
   }

   public abstract static class Training implements Comparable<CharacterSchool.Training> {
      public abstract String getName();

      public abstract String getDescription();

      public abstract void apply();

      public abstract long getPrice();

      public abstract boolean fulfillsRequirements();

      public int compareTo(CharacterSchool.Training o) {
         if (this instanceof CharacterSchool.AttributeSpecializationTraining) {
            return 1;
         }

         if (o instanceof CharacterSchool.AttributeSpecializationTraining) {
            return -1;
         }

         if (this instanceof CharacterSchool.SpecializationBasicTraining && o instanceof CharacterSchool.SpecializationBasicTraining) {
            if (!this.fulfillsRequirements()) {
               return 1;
            } else {
               return !o.fulfillsRequirements() ? -1 : 0;
            }
         } else if (!(this instanceof CharacterSchool.SpecializationTraining) || !(o instanceof CharacterSchool.SpecializationTraining)) {
            return 0;
         } else if (!this.fulfillsRequirements()) {
            return 1;
         } else {
            return !o.fulfillsRequirements() ? -1 : 0;
         }
      }
   }
}
