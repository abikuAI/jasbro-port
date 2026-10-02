package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.DefaultPreferences;
import jasbro.game.GameObject;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.StartingAtTheBottom;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.PerkHandler;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.warnings.Severity;
import jasbro.game.character.warnings.Warning;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.AllowedServices;
import jasbro.game.events.business.Fame;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.items.CharacterInventory;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.world.Time;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlTransient;

public class Charakter extends GameObject implements Unit, HasImagesInterface {
   private String name;
   private ImageData icon;
   private Map<AttributeType, Attribute> attributes = new HashMap<>();
   private Map<Time, PlannedActivity> activities = new EnumMap<>(Time.class);
   private Set<SpecializationType> specializations = EnumSet.noneOf(SpecializationType.class);
   private List<Condition> conditions = new ArrayList<>();
   private CharacterType type;
   private Gender gender;
   private String slaveJob;
   private Fame fame = new Fame();
   private Ownership ownership = Ownership.OWNED;
   private List<Trait> traits;
   private String baseId;
   private AllowedServices allowedServices;
   private CharacterInventory characterInventory;
   private CharacterStuffCounter counter;
   private int bonusPerks = 0;
   private AgeProgressionData ageProgressionData;
   private Boolean usesContraceptives;
   private transient CharacterBase base;
   private transient Map<Object, Object> cache;
   private int numberTrees = 0;

   public Charakter(CharacterBase base) {
      this.base = base;
   }

   public CharacterBase getBase() {
      if (this.base == null) {
         for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
            if (this.getBaseId().equals(base.getId())) {
               this.setBase(base);
               break;
            }
         }
      }

      return this.base;
   }

   public void setBase(CharacterBase base) {
      this.base = base;
   }

   public ImageData getIcon() {
      if (!ImageUtil.getInstance().exists(this.icon)) {
         this.icon = ImageUtil.getInstance().getImageDataByTag(ImageTag.ICON, this.getImages());
      }

      return this.icon;
   }

   public void setIcon(ImageData icon) {
      this.icon = icon;
   }

   @Override
   public String getName() {
      return this.name;
   }

   public CharacterType getType() {
      return this.type;
   }

   public boolean hasAttribute(String attributeName) {
      return this.attributes.containsKey(attributeName);
   }

   protected void addAttribute(Attribute attribute) {
      this.attributes.put(attribute.getAttributeType(), attribute);
   }

   public PlannedActivity getActivity() {
      Time time = Jasbro.getInstance().getData().getTime();
      return this.activities.containsKey(time) ? this.activities.get(time) : null;
   }

   public Attribute getAttribute(AttributeType attributeType) {
      if (!this.getAttributes().containsKey(attributeType)) {
         if (attributeType instanceof CalculatedAttribute) {
            return null;
         }

         Attribute attribute = new Attribute(this, attributeType);
         this.attributes.put(attributeType, attribute);
      }

      return this.attributes.get(attributeType);
   }

   public void setActivity(PlannedActivity activity) {
      Time time = Jasbro.getInstance().getData().getTime();
      PlannedActivity curActivity = this.getActivity();
      this.getActivities().put(time, activity);
      MyEvent event = new MyEvent(EventType.ACTIVITYCHANGE, this);
      if (curActivity != null && curActivity != activity && curActivity.getCharacters().contains(this)) {
         curActivity.removeCharacter(this);
      }

      this.handleEvent(event);
      if (activity != null && activity != curActivity) {
         activity.getSource().fireEvent(event);
      }
   }

   public void removeActivity(PlannedActivity activity) {
      for (Time time : Time.values()) {
         if (this.activities.get(time) == activity) {
            this.activities.put(time, null);
         }
      }
   }

   public Map<Time, PlannedActivity> getActivities() {
      return this.activities;
   }

   public void setActivities(Map<Time, PlannedActivity> activities) {
      this.activities = activities;
   }

   public Map<AttributeType, Attribute> getAttributes() {
      return this.attributes;
   }

   @Transient
   @XmlTransient
   @Override
   public List<ImageData> getImages() {
      return this.getBase().getImages();
   }

   public void setName(String name) {
      this.name = name;
   }

   @Override
   public Gender getGender() {
      return this.gender;
   }

   public void setGender(Gender gender) {
      this.gender = gender;
   }

   public void setType(CharacterType type) {
      this.type = type;
   }

   public String getBaseId() {
      return this.baseId;
   }

   public void setBaseId(String baseId) {
      this.baseId = baseId;
   }

   public Set<SpecializationType> getSpecializations() {
      return this.specializations;
   }

   public void setSpecializations(Collection<SpecializationType> specializations) {
      this.specializations.clear();
      this.specializations.addAll(specializations);
   }

   public List<Condition> getConditions() {
      return this.conditions;
   }

   public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
   }

   public CharacterStuffCounter getCounter() {
      if (this.counter == null) {
         this.counter = new CharacterStuffCounter();
      }

      return this.counter;
   }

   public void addTrait(Trait trait) {
      this.getTraits();
      if (!this.traits.contains(trait) && trait.addTrait(this, this.traits)) {
         this.traits.add(trait);
         this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
      }

      this.getCache().clear();
   }

   public void removeTrait(Trait trait) {
      this.getTraits();
      boolean contains = this.traits.contains(trait);
      if (contains && trait.removeTrait(this)) {
         this.traits.remove(trait);
         this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
      }

      this.getCache().clear();
   }

   public int getHealth() {
      return this.getFinalValue(EssentialAttributes.HEALTH);
   }

   public int getEnergy() {
      return this.getFinalValue(EssentialAttributes.ENERGY);
   }

   public int getMotivation() {
      return this.getFinalValue(EssentialAttributes.MOTIVATION);
   }

   public int getStrength() {
      return this.getFinalValue(BaseAttributeTypes.STRENGTH);
   }

   public int getCharisma() {
      return this.getFinalValue(BaseAttributeTypes.CHARISMA);
   }

   public int getObedience() {
      return this.getFinalValue(BaseAttributeTypes.OBEDIENCE);
   }

   public int getStamina() {
      return this.getFinalValue(BaseAttributeTypes.STAMINA);
   }

   public int getIntelligence() {
      return this.getFinalValue(BaseAttributeTypes.INTELLIGENCE);
   }

   public int getCommand() {
      return this.getFinalValue(BaseAttributeTypes.COMMAND);
   }

   public Fame getFame() {
      if (this.fame == null) {
         this.fame = new Fame();
      }

      return this.fame;
   }

   public int getFinalValue(AttributeType attributeType) {
      Integer cachedValue = (Integer)this.getCachedValue(attributeType);
      if (cachedValue != null) {
         return cachedValue;
      }

      Attribute attribute = this.getAttribute(attributeType);
      float value = attribute.getInternValue();

      for (Trait trait : this.getTraits()) {
         value += trait.getAttributeModifier(attribute);
      }

      for (Condition condition : this.getConditions()) {
         value += condition.getAttributeModifier(attribute);
      }

      for (Equipment equipment : this.getCharacterInventory().listEquipment()) {
         value += equipment.getAttributeModifier(attribute);
      }

      this.getCache().put(attributeType, (int)value);
      if (value < attribute.getMinValue()) {
         value = attribute.getMinValue();
      }

      return (int)value;
   }

   public double getAnyAttributeValue(AttributeType attributeType) {
      if (attributeType instanceof CalculatedAttribute) {
         switch ((CalculatedAttribute)attributeType) {
            case SKILLPOINTS:
               return this.getUnspentPerkPoints();
            case ARMORPERCENT:
               return this.getArmor();
            case ARMORVALUE:
               return this.getArmorValue();
            case BLOCKAMOUNT:
               return this.getBlockAmount();
            case BLOCKCHANCE:
               return this.getBlockChance();
            case CRITDAMAGEAMOUNT:
               return this.getCritDamageBonus();
            case CRITCHANCE:
               return this.getCritChance();
            case DAMAGE:
               return this.getDamage();
            case HIT:
               return this.getHit();
            case DODGE:
               return this.getDodge();
            case SPEED:
               return this.getSpeed();
            case ITEMLOOTCHANCEMODIFIER:
               return this.getItemLootChanceModifier();
            case STEALCHANCE:
               return this.getStealChance();
            case STEALAMOUNTMODIFIER:
               return this.getStealAmountModifier();
            case STEALITEMCHANCE:
               return this.getStealItemChance();
            case PREGNANCYCHANCE:
               return this.getPregnancyChance();
            case MINCHILDREN:
               return this.getMinChildren();
            case MAXCHILDREN:
               return this.getMaxChildren();
            case CONTROL:
               return this.getControl();
            case CHANCEADDITIONALCHILD:
               return this.getChanceAdditionalChild();
            case PREGNANCYDURATIONMODIFIER:
               return this.getPregnancyDurationModifier();
            case AMOUNTCUSTOMERSPERSHIFT:
               return this.getPossibleAmountCustomers().floatValue();
            case HOLYRESISTANCE:
               return this.getResistance(DamageType.HOLY);
            case DARKNESSRESISTANCE:
               return this.getResistance(DamageType.DARKNESS);
            case FIRERESISTANCE:
               return this.getResistance(DamageType.FIRE);
            case WATERRESISTANCE:
               return this.getResistance(DamageType.WATER);
            case WINDRESISTANCE:
               return this.getResistance(DamageType.WIND);
            case EARTHRESISTANCE:
               return this.getResistance(DamageType.EARTH);
            case MAGICRESISTANCE:
               return this.getResistance(DamageType.MAGIC);
            case LIGHTNINGRESISTANCE:
               return this.getResistance(DamageType.LIGHTNING);
            default:
               return 0.0;
         }
      } else {
         return this.getFinalValue(attributeType);
      }
   }

   public AgeProgressionData getAgeProgressionData() {
      if (this.ageProgressionData == null) {
         this.ageProgressionData = new AgeProgressionData(this.base.getAgeProgressionData());
         if (!this.getType().isChildType() && !this.ageProgressionData.getAdultBases().contains(this.baseId)) {
            this.ageProgressionData.getAdultBases().add(this.baseId);
         }
      }

      return this.ageProgressionData;
   }

   public int getBonus(AttributeType attributeType) {
      Attribute attribute = this.getAttribute(attributeType);
      return this.getFinalValue(attributeType) - (int)attribute.getInternValue();
   }

   public ImageData getBackground() {
      return this.getActivity() != null ? this.getActivity().getSource().getImage() : new ImageData("images/backgrounds/sky.jpg");
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.ACTIVITYCHANGE) {
         this.getCache().remove(Warning.class);
         this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
      } else {
         if (e.getType() == EventType.ATTRIBUTECHANGED) {
            AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent)e;
            Attribute attribute = attributeChangedEvent.getAttribute();
            this.resetAttributeCache(attribute.getAttributeType());
         } else if (e.getType() == EventType.ITEMUSED) {
            this.getCache().clear();
            this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
         } else if (e.getType() == EventType.SHIFTSTART) {
            this.getCache().clear();
            this.clearGuiListeners();
         } else if (e.getType() == EventType.NEXTSHIFTSTARTED && this.getActivity() != null) {
            this.getActivity().checkPossibleActivities();
         }

         List<Condition> conditions = new ArrayList<>();
         conditions.addAll(this.getConditions());

         for (Condition condition : conditions) {
            condition.handleEvent(e);
         }

         for (Trait trait : this.getTraits()) {
            trait.handleEvent(e, this);
         }

         for (SpecializationType specialization : this.getSpecializations()) {
            specialization.handleEvent(e, this);
         }

         for (Equipment item : this.getCharacterInventory().listEquipment()) {
            item.handleEvent(e, this);
         }

         this.getCounter().handleEvent(e, this);
         if (e.getType() == EventType.ATTRIBUTECHANGED) {
            AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent)e;
            Attribute attribute = attributeChangedEvent.getAttribute();
            if (attribute.getCharacter() == this) {
               if (attributeChangedEvent.getAttribute().getAttributeType().equals(EssentialAttributes.ENERGY) && this.getEnergy() + attribute.getValue() <= 0) {
                  MyEvent event = new AttributeChangedEvent(
                     EventType.ENERGYZERO, attribute, attributeChangedEvent.getAmount(), attributeChangedEvent.getActivity()
                  );
                  this.handleEvent(event);
                  this.fireEvent(event);
               } else if (attribute.getAttributeType().equals(EssentialAttributes.HEALTH) && this.getHealth() + attribute.getValue() <= 0) {
                  MyEvent event = new AttributeChangedEvent(
                     EventType.HEALTHZERO, attribute, attributeChangedEvent.getAmount(), attributeChangedEvent.getActivity()
                  );
                  this.handleEvent(event);
                  this.fireEvent(event);
               } else {
                  this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
               }
            }
         } else if (e.getType() == EventType.NEXTSHIFT) {
            if (this.getFinalValue(EssentialAttributes.MOTIVATION) > 80) {
               this.addCondition(new Buff.MotivatedTwo(this));
            } else if (this.getFinalValue(EssentialAttributes.MOTIVATION) < 30) {
               this.addCondition(new Buff.Unmotivated(this));
            } else {
               for (Condition condition : this.getConditions()) {
                  if (condition.getClass().equals(new Buff.Unmotivated(this)) || condition.getClass().equals(new Buff.MotivatedTwo(this))) {
                     this.removeCondition(condition);
                  }
               }
            }
         } else if (e.getType() == EventType.NEXTDAY) {
            if (this.ownership == Ownership.CONTRACT) {
               int wage = 50 + (int)Math.sqrt(this.calculateValue());
               if (this.getTraits().contains(Trait.RAPACIOUS)) {
                  wage = (int)(wage + wage * 0.3F);
               } else if (this.getTraits().contains(Trait.LOYAL)) {
                  wage = (int)(wage - wage * 0.3F);
               }

               if (Jasbro.getInstance().getData().canAfford(wage)) {
                  Jasbro.getInstance().getData().spendMoney(wage, this);
               } else {
                  Jasbro.getInstance().removeCharacter(this);
                  new MessageScreen(
                     TextUtil.t("trainer.cannotafford", this), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), this.getBackground()
                  );
               }

               if (this.getType() == CharacterType.TRAINER && this.getAttribute(BaseAttributeTypes.COMMAND).getInternValue() < 1.0F) {
                  List<AttributeModification> modifications = new ArrayList<>();
                  if (this.getActivity() == null) {
                     for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                        if (character.getType() == CharacterType.SLAVE) {
                           AttributeModification attributeModification = new AttributeModification(-0.1F, BaseAttributeTypes.OBEDIENCE, character);
                           attributeModification.applyModification();
                           modifications.add(attributeModification);
                        }
                     }
                  } else {
                     for (Charakter character : Util.getAllCharactersSuperLocation(this.getActivity().getSource())) {
                        if (character.getType() == CharacterType.SLAVE) {
                           AttributeModification attributeModification = new AttributeModification(-0.1F, BaseAttributeTypes.OBEDIENCE, character);
                           attributeModification.applyModification();
                           modifications.add(attributeModification);
                        }
                     }
                  }

                  MessageData messageData = new MessageData(
                     TextUtil.t("trainer.lowCommand", this), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), this.getBackground()
                  );
                  messageData.setAttributeModifications(modifications);
                  new MessageScreen(messageData);
               }
            } else if (this.ownership == Ownership.OWNED && this.getType() == CharacterType.TRAINER) {
               int wage = 20 + (int)Math.sqrt(this.calculateValue()) / 2;
               if (this.getTraits().contains(Trait.RAPACIOUS)) {
                  wage = (int)(wage + wage * 0.3F);
               } else if (this.getTraits().contains(Trait.LOYAL)) {
                  wage = (int)(wage - wage * 0.3F);
               }

               Jasbro.getInstance().getData().spendMoney(wage, this);
               if (this.getType() == CharacterType.TRAINER && this.getAttribute(BaseAttributeTypes.COMMAND).getInternValue() < 1.0F) {
                  List<AttributeModification> modifications = new ArrayList<>();

                  for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                     if (character.getType() == CharacterType.SLAVE) {
                        AttributeModification attributeModification = new AttributeModification(-0.25F, BaseAttributeTypes.OBEDIENCE, character);
                        attributeModification.applyModification();
                        modifications.add(attributeModification);
                     }
                  }

                  MessageData messageData = new MessageData(
                     TextUtil.t("trainer.avatarLowCommand", this), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), this.getBackground()
                  );
                  messageData.setAttributeModifications(modifications);
                  new MessageScreen(messageData);
               }
            } else {
               Jasbro.getInstance().getData().spendMoney(20L, TextUtil.t("slaveupkeep"));
            }
         }
      }
   }

   @Override
   public String toString() {
      return this.getName();
   }

   @Override
   public void addCondition(Condition condition) {
      condition.setCharacter(this);
      this.addListener(condition);
      this.getConditions().add(condition);
      condition.init();
      this.getCache().clear();
      this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
   }

   @Override
   public void removeCondition(Condition condition) {
      this.getConditions().remove(condition);
      this.getCache().clear();
      this.fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
   }

   public long calculateValue() {
      long baseValue = 0L;
      double expValue = 0.1;

      for (SpecializationType specializationType : this.getSpecializations()) {
         expValue *= 1.005;
         baseValue += 150L;
         float divideBy = specializationType.getAssociatedAttributes().size() - (specializationType.getAssociatedAttributes().size() - 1) * 0.2F;

         for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
            Attribute attribute = this.getAttribute(attributeType);
            double mod;
            if (specializationType != SpecializationType.SLAVE && specializationType != SpecializationType.TRAINER) {
               baseValue = (long)((float)baseValue + attribute.getInternValue() * 30.0F);
               mod = attribute.getInternValue() / 80.0F / divideBy + 1.0F;
            } else {
               baseValue = (long)((float)baseValue + (attribute.getInternValue() - 5.0F) * 150.0F);
               mod = attribute.getInternValue() / 40.0F + 1.0F;
            }

            expValue *= mod;
         }
      }

      long value = (long)(baseValue + baseValue * expValue) - 250L;
      this.getTraits();

      for (Trait trait : this.traits) {
         value += trait.getValueModifier();
      }

      if (value < 100L) {
         value = 100L;
      }

      return value;
   }

   public List<Trait> getTraits() {
      if (this.traits == null) {
         this.traits = new ArrayList<>();

         for (Trait trait : this.getBase().getTraits()) {
            this.addTrait(trait);
         }
      }

      List<Trait> traitsLocal = new ArrayList<>(this.traits);

      for (Equipment equipment : this.getCharacterInventory().listEquipment()) {
         equipment.modifyTraits(traitsLocal, this);
      }

      return traitsLocal;
   }

   public List<Trait> getTraitsInternal() {
      this.getTraits();
      return new ArrayList<>(this.traits);
   }

   public Ownership getOwnership() {
      if (this.ownership == null) {
         this.ownership = Ownership.OWNED;
      }

      return this.ownership;
   }

   public void setOwnership(Ownership ownership) {
      this.ownership = ownership;
   }

   public boolean canSell() {
      return this.getOwnership().isSellable();
   }

   public void setUsesContraceptives(boolean usesContraceptives) {
      this.usesContraceptives = usesContraceptives;
   }

   public Boolean isUsesContraceptives() {
      if (this.usesContraceptives == null) {
         this.usesContraceptives = true;
      }

      return this.usesContraceptives;
   }

   public CharacterInventory getCharacterInventory() {
      if (this.characterInventory == null) {
         this.characterInventory = new CharacterInventory(this);
      }

      return this.characterInventory;
   }

   public float getMoneyModifier() {
      float modifier = 1.0F;
      modifier = this.getActivity().getSource().getMoneyModifier(modifier, this);

      for (SpecializationType specializationType : this.getSpecializations()) {
         modifier = specializationType.getMoneyModifier(modifier, this);
      }

      for (Condition condition : this.getConditions()) {
         modifier = condition.getMoneyModifier(modifier, this);
      }

      return modifier;
   }

   public ArrayList<ImageTag> getBaseTags() {
      ArrayList<ImageTag> imageTags = new ArrayList<>();
      if (this.specializations.contains(SpecializationType.CATGIRL)) {
         imageTags.add(ImageTag.CATGIRL);
      }

      if (this.getCharacterInventory().getItem(EquipmentSlot.DRESS) == null && this.getCharacterInventory().getItem(EquipmentSlot.UNDERWEAR) == null) {
         imageTags.add(ImageTag.NAKED);
      } else {
         imageTags.add(ImageTag.CLOTHED);
      }

      for (Condition condition : this.getConditions()) {
         condition.modifyImageTags(imageTags);
      }

      for (Equipment equipment : this.getCharacterInventory().listEquipment()) {
         equipment.modifyImageTags(imageTags);
      }

      return imageTags;
   }

   public int getRealMinObedience(int minObedience, RunningActivity activity) {
      if (this.getType() != CharacterType.TRAINER && minObedience != 0) {
         for (SpecializationType specializationType : this.getSpecializations()) {
            minObedience = specializationType.getMinObedienceModified(minObedience, this, activity);
         }

         for (Condition condition : this.getConditions()) {
            minObedience = condition.getMinObedienceModified(minObedience, this, activity);
         }

         for (Trait trait : this.getTraits()) {
            minObedience = trait.getMinObedienceModified(minObedience, this, activity);
         }

         return minObedience;
      } else {
         return 0;
      }
   }

   @Override
   public int getHitpoints() {
      return this.getHealth();
   }

   @Override
   public int getMaxHitpoints() {
      return this.getAttribute(EssentialAttributes.HEALTH).getMaxValue();
   }

   @Override
   public float modifyHitpoints(float modifier) {
      return this.getAttribute(EssentialAttributes.HEALTH).addToValue(modifier);
   }

   @Override
   public float getDamage() {
      Float cachedValue = (Float)this.getCachedValue(CalculatedAttribute.DAMAGE);
      if (cachedValue != null) {
         return cachedValue;
      }

      float power = 0.5F + this.getStrength() / 40.0F;
      power += this.getFinalValue(SpecializationAttribute.VETERAN) / 50.0F;
      power = (float)this.getAttributeModified(CalculatedAttribute.DAMAGE, power);
      this.getCache().put(CalculatedAttribute.DAMAGE, power);
      return power;
   }

   public int getArmorValue() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.ARMORVALUE, 0);
   }

   @Override
   public int getArmor() {
      Integer cachedValue = (Integer)this.getCachedValue(CalculatedAttribute.ARMORPERCENT);
      if (cachedValue != null) {
         return cachedValue;
      }

      int amount = (int)(this.getStrength() / 5 + this.getFinalValue(SpecializationAttribute.VETERAN) / 10.0F);
      amount += this.getArmorValue();
      float value = 0.25F;
      float mitigation = 1.0F;

      do {
         mitigation += value;
         amount--;
         value /= 1.0075F;
      } while (amount > 0);

      value = (int)this.getAttributeModified(CalculatedAttribute.ARMORPERCENT, mitigation);
      this.getCache().put(CalculatedAttribute.ARMORPERCENT, (int)mitigation);
      return (int)mitigation;
   }

   @Override
   public Attack getAttack(Battle battle) {
      List<Attack> attacks = this.getPossibleAttacks(battle);
      int sum = 0;

      for (Attack attack : attacks) {
         sum += attack.getSelectionModifier();
      }

      int rnd = Util.getInt(0, sum);
      sum = 0;

      for (Attack attack : attacks) {
         sum += attack.getSelectionModifier();
         if (sum >= rnd) {
            return attack;
         }
      }

      return attacks.get(0);
   }

   public List<Attack> getPossibleAttacks(Battle battle) {
      List<Attack> attacks = new ArrayList<>();
      attacks.add(new Attack.StandardAttack(this));
      if (this.getFinalValue(SpecializationAttribute.VETERAN) > 10) {
         attacks.add(new Attack.StrongStrike(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 10) {
         attacks.add(new Attack.EtherStrike(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 25) {
         attacks.add(new Attack.FireWave(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 35) {
         attacks.add(new Attack.IceStingers(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 45) {
         attacks.add(new Attack.RollingThunder(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 60) {
         attacks.add(new Attack.StarlightBreaker(this));
      }

      if (this.getFinalValue(SpecializationAttribute.MAGIC) > 75) {
         attacks.add(new Attack.Midnight(this));
      }

      if (this.getFinalValue(SpecializationAttribute.VETERAN) > 30 || this.getFinalValue(SpecializationAttribute.AGILITY) > 20) {
         attacks.add(new Attack.SwiftStrike(this));
      }

      if (this.getFinalValue(SpecializationAttribute.VETERAN) > 40 || this.getFinalValue(SpecializationAttribute.AGILITY) > 30) {
         attacks.add(new Attack.PreciseStrike(this));
      }

      if (this.getFinalValue(SpecializationAttribute.SEDUCTION) > 25 || this.getFinalValue(SpecializationAttribute.STRIP) > 25) {
         attacks.add(new Attack.ButtSmash(this));
      }

      if (this.getFinalValue(SpecializationAttribute.STRIP) > 40) {
         attacks.add(new Attack.GracefulKick(this));
      }

      if (this.getFinalValue(SpecializationAttribute.AGILITY) > 45) {
         attacks.add(new Attack.ShadowSlicer(this));
      }

      if (this.getTraits().contains(Trait.CASTCURE)) {
         attacks.add(new Attack.Heal(this));
      }

      for (Trait trait : this.getTraits()) {
         trait.modifyPossibleAttacks(attacks, this);
      }

      if (attacks.size() == 0) {
         attacks.add(new Attack.StandardAttack(this));
      }

      return attacks;
   }

   @Override
   public int getHit() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.HIT, 0);
   }

   @Override
   public int getDodge() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.DODGE, 1 + this.getFinalValue(SpecializationAttribute.AGILITY) / 5);
   }

   @Override
   public int getSpeed() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.SPEED, 10 + this.getFinalValue(SpecializationAttribute.AGILITY) / 10);
   }

   @Override
   public int getCritChance() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.CRITCHANCE, this.getIntelligence() / 10);
   }

   @Override
   public int getCritDamageBonus() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 30);
   }

   @Override
   public int getBlockChance() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.BLOCKCHANCE, 0);
   }

   @Override
   public int getBlockAmount() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.BLOCKAMOUNT, 30);
   }

   public int getMinChildren() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.MINCHILDREN, 1);
   }

   public int getMaxChildren() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.MAXCHILDREN, 8);
   }

   public int getPregnancyChance() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.PREGNANCYCHANCE, 10);
   }

   public int getChanceAdditionalChild() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.CHANCEADDITIONALCHILD, 10);
   }

   public int getPregnancyDurationModifier() {
      int preg = this.getIntCalculatedAttribute(CalculatedAttribute.PREGNANCYDURATIONMODIFIER, 100);
      return this.getTraits().contains(Trait.HEARTOFTHESWARM) ? preg / 3 : preg;
   }

   @Override
   public float takeDamage(float damage) {
      return this.modifyHitpoints(-damage);
   }

   public int getStealChance() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.STEALCHANCE, 1 + this.getFinalValue(SpecializationAttribute.PICKPOCKETING) / 5);
   }

   public int getStealAmountModifier() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.STEALAMOUNTMODIFIER, 20 + this.getFinalValue(SpecializationAttribute.PICKPOCKETING) / 5);
   }

   public int getStealItemChance() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.STEALITEMCHANCE, 1 + this.getFinalValue(SpecializationAttribute.PICKPOCKETING) / 20);
   }

   public int getItemLootChanceModifier() {
      return this.getIntCalculatedAttribute(CalculatedAttribute.ITEMLOOTCHANCEMODIFIER, 30);
   }

   @Override
   public int getResistance(DamageType damageType) {
      CalculatedAttribute calculatedAttribute = null;
      switch (damageType) {
         case FIRE:
            calculatedAttribute = CalculatedAttribute.FIRERESISTANCE;
            break;
         case WATER:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
         case WIND:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
         case EARTH:
            calculatedAttribute = CalculatedAttribute.EARTHRESISTANCE;
            break;
         case MAGIC:
            calculatedAttribute = CalculatedAttribute.MAGICRESISTANCE;
            break;
         case HOLY:
            calculatedAttribute = CalculatedAttribute.HOLYRESISTANCE;
            break;
         case DARKNESS:
            calculatedAttribute = CalculatedAttribute.DARKNESSRESISTANCE;
      }

      return calculatedAttribute != null ? this.getIntCalculatedAttribute(calculatedAttribute, 0) : 0;
   }

   private int getIntCalculatedAttribute(CalculatedAttribute calculatedAttribute, int startValue) {
      Integer cachedValue = (Integer)this.getCachedValue(calculatedAttribute);
      if (cachedValue != null) {
         return cachedValue;
      }

      int value = Math.max(0, (int)this.getAttributeModified(calculatedAttribute, startValue));
      this.getCache().put(calculatedAttribute, value);
      return value;
   }

   public Float getPossibleAmountCustomers() {
      Float cachedValue = (Float)this.getCachedValue(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT);
      if (cachedValue != null) {
         return cachedValue;
      }

      float amount = 100.0F;
      amount = (float)this.getAttributeModified(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT, amount);
      this.getCache().put(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT, amount);
      return amount;
   }

   public int getControl() {
      Integer cachedValue = (Integer)this.getCachedValue(CalculatedAttribute.CONTROL);
      if (cachedValue != null) {
         return cachedValue;
      }

      int startValue = 0;
      int value = 0;
      if (this.getOwnership() != Ownership.NOTOWNED && this.getOwnership() != Ownership.NOTOWNEDCANSELL) {
         if (this.getType() == CharacterType.TRAINER) {
            int var4 = 10;
            var4 += this.getCommand();
            if (Jasbro.getInstance().getData().getProtagonist() == this) {
               var4 *= 2;
            }

            value = (int)this.getAttributeModified(CalculatedAttribute.CONTROL, var4, false);
         } else if (this.getType() == CharacterType.SLAVE) {
            startValue = (this.getObedience() - 110) / 10;
            value = (int)this.getAttributeModified(CalculatedAttribute.CONTROL, startValue, false);
         }
      }

      this.getCache().put(CalculatedAttribute.CONTROL, value);
      return value;
   }

   public AllowedServices getAllowedServices() {
      if (this.allowedServices == null) {
         DefaultPreferences preferences = Jasbro.getInstance().getData().getDefaultPreferences();
         if (this.getGender() == Gender.FEMALE) {
            this.allowedServices = new AllowedServices(preferences.getAllowedServicesFemale());
         } else if (this.getGender() == Gender.MALE) {
            this.allowedServices = new AllowedServices(preferences.getAllowedServicesMale());
         } else {
            this.allowedServices = new AllowedServices(preferences.getAllowedServicesFuta());
         }
      }

      return this.allowedServices;
   }

   public void setAllowedServices(AllowedServices allowedServices) {
      this.allowedServices = allowedServices;
   }

   public void resetAttributeCache(AttributeType attributeType) {
      this.getCache().remove(attributeType);
   }

   private Object getCachedValue(AttributeType attributeType) {
      return this.getCache().containsKey(attributeType) ? this.getCache().get(attributeType) : null;
   }

   private Map<Object, Object> getCache() {
      if (this.cache == null) {
         this.cache = new HashMap<>();
      }

      return this.cache;
   }

   public int getBonusPerks() {
      return this.bonusPerks;
   }

   public void addBonusPerk() {
      this.bonusPerks++;
   }

   public int getPerkPoints() {
      return PerkHandler.getSkillPoints(this);
   }

   public int getUnspentPerkPoints() {
      return PerkHandler.getSkillPoints(this) - PerkHandler.getUsedSkillPoints(this);
   }

   public List<SkillTree> getSkillTrees() {
      return PerkHandler.getSkillTrees(this);
   }

   private double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue) {
      return this.getAttributeModified(calculatedAttribute, currentValue, true);
   }

   private double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, boolean zeroIsMin) {
      for (Trait trait : this.getTraits()) {
         currentValue = trait.getAttributeModified(calculatedAttribute, currentValue, this);
      }

      for (Condition condition : this.getConditions()) {
         currentValue = condition.modifyCalculatedAttribute(calculatedAttribute, currentValue, this);
      }

      for (Equipment equipment : this.getCharacterInventory().listEquipment()) {
         currentValue = equipment.modifyCalculatedAttribute(calculatedAttribute, currentValue, this);
      }

      return currentValue < 0.0 && zeroIsMin ? 0.0 : currentValue;
   }

   public void addSpecialization(SpecializationType spec) {
      if (!this.getSpecializations().contains(spec) && this.numberTrees < Jasbro.maxTrees) {
         this.specializations.add(spec);
         this.numberTrees++;
      }
   }

   public void addSpecializationDespiteLimit(SpecializationType spec) {
      if (!this.getSpecializations().contains(spec)) {
         this.specializations.add(spec);
         this.numberTrees++;
      }
   }

   public void removeSpecialization(SpecializationType spec) {
      if (this.getSpecializations().contains(spec)) {
         this.specializations.remove(spec);
         this.numberTrees--;
      }
   }

   public void clearSpecialization() {
      this.specializations.clear();
      this.numberTrees = 0;
   }

   public int getNumberTrees() {
      return this.numberTrees;
   }

   public ImageData getWarnImage() {
      List<Warning> warnings = this.getWarnings();
      return warnings.get(0).getIcon();
   }

   public String getWarnString() {
      List<Warning> warnings = this.getWarnings();
      String warningString = "";

      for (Warning warning : warnings) {
         warningString = warningString + warning.getMessage() + "\n";
      }

      return warningString;
   }

   public Severity getWarnLevel() {
      return this.getWarnings().get(0).getSeverity();
   }

   public List<Warning> getWarnings() {
      if (this.getCache().containsKey(Warning.class)) {
         return (List<Warning>)this.cache.get(Warning.class);
      }

      List<Warning> warnings = new ArrayList<>();
      PlannedActivity activity = this.getActivity();
      if (activity != null) {
         if (activity.getType() == ActivityType.IDLE) {
            warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.idle", this)));
         }

         Object[] arguments = new Object[]{activity.getActivityDetails().getText(), null};
         if (activity.getSource() instanceof Room) {
            Room room = (Room)activity.getSource();
            arguments[1] = room.getHouse();
         } else if (activity.getSource() != null) {
            arguments[1] = activity.getSource().getName();
         }

         warnings.add(new Warning(Severity.ACTIVITY, TextUtil.t("warnings.activity", this, arguments)));
         if (this.getType() == CharacterType.TRAINER) {
            if (activity.getType() == ActivityType.BATHATTENDANT && !this.getTraits().contains(Trait.LEGACYMASSEUR)) {
               boolean start = false;

               for (Condition condition : this.getConditions()) {
                  if (condition instanceof StartingAtTheBottom) {
                     start = true;
                     break;
                  }
               }

               if (!start) {
                  warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.detrimentalCommand", this, arguments)));
               }
            }

            if (activity.getType() == ActivityType.BARTEND && !this.getTraits().contains(Trait.LEGACYBARTENDER)) {
               boolean start = false;

               for (Condition condition : this.getConditions()) {
                  if (condition instanceof StartingAtTheBottom) {
                     start = true;
                     break;
                  }
               }

               if (!start) {
                  warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.detrimentalCommand", this, arguments)));
               }
            }

            if (activity.getType() == ActivityType.CLEAN && !this.getTraits().contains(Trait.LEGACYMAID)) {
               boolean start = false;

               for (Condition condition : this.getConditions()) {
                  if (condition instanceof StartingAtTheBottom) {
                     start = true;
                     break;
                  }
               }

               if (!start) {
                  warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.detrimentalCommand", this, arguments)));
               }
            } else if ((activity.getType() == ActivityType.SUBMITTOMONSTER || activity.getType() == ActivityType.MONSTERFIGHT)
               && !this.getTraits().contains(Trait.LEGACYADVENTURER)) {
               warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
            } else if (activity.getType() != ActivityType.ATTEND
               || this.getTraits().contains(Trait.LEGACYSTRIPPER) && this.getTraits().contains(Trait.LEGACYBARTENDER)) {
               if (activity.getType() == ActivityType.STRIP && !this.getTraits().contains(Trait.LEGACYSTRIPPER)) {
                  warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
               } else if ((activity.getType() == ActivityType.WHORE || activity.getType() == ActivityType.SUBMIT || activity.getType() == ActivityType.TEASE)
                  && !this.getTraits().contains(Trait.LEGACYWHORE)) {
                  warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
               }
            } else {
               warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
            }
         }
      } else {
         warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.idle", this)));
      }

      if (this.getHealth() < 20) {
         warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.healthCritical", this)));
      } else if (this.getHealth() < 40) {
         warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.healthLow", this)));
      }

      for (Condition condition : this.getConditions()) {
         condition.modifyWarnings(warnings);
      }

      Collections.sort(warnings, Collections.reverseOrder());
      this.getCache().put(Warning.class, warnings);
      return warnings;
   }

   public String getSlaveJob() {
      return this.slaveJob;
   }

   public void setSlaveJob(String slaveJob) {
      this.slaveJob = slaveJob;
   }
}
