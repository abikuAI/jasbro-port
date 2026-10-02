package jasbro.game.character.specialization;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.MinObedienceModifier;
import jasbro.game.interfaces.MoneyEarnedModifier;
import jasbro.game.interfaces.MyCharacterEventListener;
import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum SpecializationType implements MoneyEarnedModifier, MinObedienceModifier, UnlockObject, MyCharacterEventListener {
   UNDERAGE,
   TRAINER,
   LEGACY(new LegacyEventHandler(), false),
   SLAVE,
   SEX,
   KINKYSEX,
   WHORE,
   MAID,
   FIGHTER,
   BARTENDER,
   NURSE,
   DANCER,
   DOMINATRIX,
   THIEF(new ThiefEventHandler()),
   CATGIRL,
   MARKETINGEXPERT,
   ALCHEMIST,
   FURRY(new MutantEventHandler(), false);

   private static final Logger log = LogManager.getLogger(SpecializationType.class);
   private boolean locked = false;
   private boolean teachable = true;
   private MyCharacterEventListener eventListener = null;

   SpecializationType() {
   }

   SpecializationType(boolean teachable) {
      this.teachable = teachable;
   }

   SpecializationType(MyCharacterEventListener eventListener) {
      this.eventListener = eventListener;
   }

   SpecializationType(MyCharacterEventListener eventListener, boolean teachable) {
      this.teachable = teachable;
      this.eventListener = eventListener;
   }

   public List<AttributeType> getAssociatedAttributes() {
      List<AttributeType> attributes = new ArrayList<>();
      if (this == SEX) {
         attributes.add(Sextype.VAGINAL);
         attributes.add(Sextype.ANAL);
         attributes.add(Sextype.ORAL);
         attributes.add(Sextype.TITFUCK);
         attributes.add(Sextype.FOREPLAY);
      } else if (this == KINKYSEX) {
         attributes.add(Sextype.MONSTER);
         attributes.add(Sextype.GROUP);
      } else if (this == TRAINER) {
         attributes.add(BaseAttributeTypes.CHARISMA);
         attributes.add(BaseAttributeTypes.COMMAND);
         attributes.add(BaseAttributeTypes.STAMINA);
         attributes.add(BaseAttributeTypes.INTELLIGENCE);
         attributes.add(BaseAttributeTypes.STRENGTH);
      } else if (this == SLAVE) {
         attributes.add(BaseAttributeTypes.CHARISMA);
         attributes.add(BaseAttributeTypes.OBEDIENCE);
         attributes.add(BaseAttributeTypes.STAMINA);
         attributes.add(BaseAttributeTypes.INTELLIGENCE);
         attributes.add(BaseAttributeTypes.STRENGTH);
      } else if (this == UNDERAGE) {
         attributes.add(BaseAttributeTypes.CHARISMA);
         attributes.add(BaseAttributeTypes.STAMINA);
         attributes.add(BaseAttributeTypes.INTELLIGENCE);
         attributes.add(BaseAttributeTypes.STRENGTH);
      } else if (this == MAID) {
         attributes.add(SpecializationAttribute.CLEANING);
         attributes.add(SpecializationAttribute.COOKING);
      } else if (this == WHORE) {
         attributes.add(SpecializationAttribute.SEDUCTION);
      } else if (this == FIGHTER) {
         attributes.add(SpecializationAttribute.VETERAN);
         attributes.add(SpecializationAttribute.AGILITY);
         attributes.add(SpecializationAttribute.MAGIC);
      } else if (this == BARTENDER) {
         attributes.add(SpecializationAttribute.BARTENDING);
      } else if (this == NURSE) {
         attributes.add(SpecializationAttribute.MEDICALKNOWLEDGE);
         attributes.add(SpecializationAttribute.MAGIC);
      } else if (this == DANCER) {
         attributes.add(SpecializationAttribute.STRIP);
      } else if (this == DOMINATRIX) {
         attributes.add(SpecializationAttribute.DOMINATE);
         attributes.add(Sextype.BONDAGE);
      } else if (this == THIEF) {
         attributes.add(SpecializationAttribute.PICKPOCKETING);
         attributes.add(SpecializationAttribute.AGILITY);
      } else if (this == CATGIRL) {
         attributes.add(SpecializationAttribute.CATGIRL);
      } else if (this == MARKETINGEXPERT) {
         attributes.add(SpecializationAttribute.ADVERTISING);
      } else if (this == FURRY) {
         attributes.add(SpecializationAttribute.TRANSFORMATION);
      } else if (this == ALCHEMIST) {
         attributes.add(SpecializationAttribute.PLANTKNOWLEDGE);
         attributes.add(SpecializationAttribute.MAGIC);
      } else if (this == LEGACY) {
         attributes.add(SpecializationAttribute.EXPERIENCE);
      } else {
         log.error("No associated attributes: {}", new Object[]{this.toString()});
      }

      return attributes;
   }

   @Override
   public String getText() {
      return TextUtil.t(this.toString());
   }

   public int getTrainingLevel(Charakter character) {
      int sumMaxChange = 0;

      for (AttributeType attributeType : this.getAssociatedAttributes()) {
         sumMaxChange += character.getAttribute(attributeType).getMaxValue() - attributeType.getDefaultMax();
      }

      return 1 + sumMaxChange / this.getAssociatedAttributes().size() / this.getAssociatedAttributes().get(0).getRaiseMaxBy();
   }

   @Override
   public float getMoneyModifier(float currentModifier, Charakter character) {
      return this == CATGIRL ? currentModifier * (1.0F + character.getFinalValue(SpecializationAttribute.CATGIRL) / 100.0F) : currentModifier;
   }

   @Override
   public int getMinObedienceModified(int minObedience, Charakter character, RunningActivity activity) {
      if (this == CATGIRL) {
         float f = character.getFinalValue(SpecializationAttribute.CATGIRL) / 5;
         int i = (int)f;
         return minObedience + i;
      } else {
         return minObedience;
      }
   }

   @Override
   public void handleEvent(MyEvent e, Charakter character) {
      if (this.eventListener != null) {
         this.eventListener.handleEvent(e, character);
      }
   }

   public SkillTree getAssociatedSkillTree() {
      for (SkillTree skillTree : SkillTree.values()) {
         if (skillTree.toString().equals(this.toString())) {
            return skillTree;
         }
      }

      return null;
   }

   @Override
   public String getDescription() {
      return "";
   }

   @Override
   public boolean isLocked() {
      return this.locked;
   }

   @Override
   public ImageData getImage() {
      return this.getAssociatedSkillTree().getIcon();
   }

   @Override
   public void setLocked(boolean locked) {
      this.locked = locked;
   }

   public boolean isTeachable() {
      return this.teachable;
   }

   public void setTeachable(boolean teachable) {
      this.teachable = teachable;
   }
}
