package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CharacterSpawner {
   private static final Logger log = LogManager.getLogger(CharacterSpawner.class);

   public static Charakter create(CharacterBase base) {
      return create(base, base.getType());
   }

   public static Charakter create(String baseId, CharacterType type) {
      if (baseId == null) {
         return null;
      }

      for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
         if (base.getId().toLowerCase().equals(baseId.toLowerCase())) {
            return create(base, type);
         }
      }

      return null;
   }

   public static Charakter create(CharacterBase base, CharacterType type) {
      Charakter character = new Charakter(base);
      if (type == null) {
         type = CharacterType.SLAVE;
      }

      if (type == CharacterType.SLAVE) {
         character.addSpecialization(SpecializationType.SLAVE);
      } else {
         character.addSpecialization(SpecializationType.TRAINER);
      }

      character.addSpecialization(SpecializationType.SEX);
      character.setName(base.getName());
      character.setGender(base.getGender());
      character.setType(type);
      character.setBaseId(base.getId());
      if (base.getInitialSpecialization() != null) {
         SpecializationType specializationType = base.getInitialSpecialization();
         character.addSpecialization(specializationType);

         for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
            character.getAttribute(attributeType).addToValue(1.0F, true);
         }
      }

      for (Trait trait : base.getTraits()) {
         character.addTrait(trait);
      }

      character.setIcon(ImageUtil.getInstance().getImageDataByTag(ImageTag.ICON, character.getImages()));

      for (Entry<BaseAttributeTypes, Integer> entry : base.getAttributes()) {
         if (entry.getKey() == BaseAttributeTypes.OBEDIENCE && type == CharacterType.TRAINER) {
            character.addAttribute(new Attribute(BaseAttributeTypes.COMMAND, entry.getValue().intValue(), character));
         } else {
            character.addAttribute(new Attribute(BaseAttributeTypes.valueOf(entry.getKey().toString()), entry.getValue().intValue(), character));
         }
      }

      character.getAttribute(EssentialAttributes.HEALTH).setInternValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue());
      character.getAttribute(EssentialAttributes.ENERGY).setInternValue(character.getAttribute(EssentialAttributes.ENERGY).getMaxValue());
      character.getAttribute(EssentialAttributes.MOTIVATION).setInternValue(Util.getInt(30, 70));
      return character;
   }

   public static Charakter spawnChild(Charakter mother, Person otherParent) {
      CharacterBase characterBase = selectCharacterBase(mother, otherParent);
      return spawnChild(mother, otherParent, characterBase);
   }

   public static Charakter spawnChild(Charakter mother, Person otherParent, CharacterBase characterBase) {
      Charakter character = create(characterBase);

      for (AttributeType attributeType : BaseAttributeTypes.values()) {
         character.getAttribute(attributeType).setInternValue(1.0F);
      }

      character.clearSpecialization();
      CharacterManipulationManager.changeBase(character, CharacterManipulationManager.getInfantBase(character));
      CharacterManipulationManager.changeType(character, CharacterType.INFANT);
      character.getAgeProgressionData().setMother(new WeakReference<>(mother));
      character.getAgeProgressionData().setNameMother(mother.getName());
      if (otherParent != null) {
         character.getAgeProgressionData().setNameFather(otherParent.getName());
      }

      for (Trait trait : character.getTraits()) {
         if (Util.getRnd().nextBoolean()) {
            character.removeTrait(trait);
         }
      }

      for (Trait trait : mother.getTraitsInternal()) {
         if (!trait.isPerk() && Util.getRnd().nextBoolean()) {
            character.addTrait(trait);
         }
      }

      if (otherParent instanceof Charakter) {
         Charakter otherParentChar = (Charakter)otherParent;
         character.getAgeProgressionData().setFather(new WeakReference<>(otherParentChar));

         for (Trait trait : otherParentChar.getTraitsInternal()) {
            if (!trait.isPerk() && Util.getRnd().nextBoolean()) {
               character.addTrait(trait);
            }
         }
      }

      for (Trait trait : mother.getTraits()) {
         if (trait == Trait.BESTIALFEATURES) {
            character.addSpecialization(SpecializationType.UNDERAGE);
            character.addSpecialization(SpecializationType.FURRY);
            if (mother.getSpecializations().size() > 3) {
               SpecializationType spec = null;

               while (true) {
                  spec = getMotherSpec(mother);
                  if (null != spec) {
                     if (SpecializationType.SEX != spec || SpecializationType.SLAVE != spec || SpecializationType.TRAINER != spec) {
                        character.addSpecialization(spec);
                        break;
                     }
                     continue;
                  }
               }
            }
         }
      }

      character.addCondition(new ItemCooldown(0, "Tome_of_time"));
      return character;
   }

   private static SpecializationType getMotherSpec(Charakter mother) {
      int i = 0;
      int selection = Util.getInt(0, mother.getSpecializations().size());

      for (SpecializationType spec : mother.getSpecializations()) {
         if (spec != SpecializationType.FURRY && spec != SpecializationType.SEX && spec != SpecializationType.SLAVE && spec != SpecializationType.TRAINER) {
            if (i == selection) {
               return spec;
            }

            i++;
         } else {
            i++;
         }
      }

      return null;
   }

   private static CharacterBase selectCharacterBase(Charakter mother, Person otherParent) {
      CharacterBase characterBase = checkPredeterminedBase(mother.getAgeProgressionData().getSonDaughterBase());
      if (characterBase == null && otherParent instanceof Charakter) {
         Charakter otherParentChar = (Charakter)otherParent;
         characterBase = checkPredeterminedBase(otherParentChar.getAgeProgressionData().getSonDaughterBase());
      }

      if (characterBase == null) {
         List<CharacterBase> bases = Jasbro.getInstance().getUnusedBases();
         characterBase = bases.get(Util.getInt(0, bases.size()));
      }

      return characterBase;
   }

   private static CharacterBase checkPredeterminedBase(String baseName) {
      if (baseName == null) {
         return null;
      }

      for (CharacterBase base : Jasbro.getInstance().getUnusedBases()) {
         if (baseName.equals(base.getId())) {
            return base;
         }
      }

      return null;
   }

   public static ComplexEnemy spawnEnemy(EnemySpawnLocation spawnLocation) {
      List<ComplexEnemyTemplate> enemyTemplates = new ArrayList<>();
      List<EnemySpawnData> spawnChances = new ArrayList<>();
      int sum = 0;

      for (ComplexEnemyTemplate enemyTemplate : Jasbro.getInstance().getEnemyTemplates().values()) {
         for (EnemySpawnData enemySpawnData : enemyTemplate.getSpawnDataList()) {
            if (enemySpawnData.getEnemySpawnLocation() == spawnLocation) {
               sum += enemySpawnData.getEncounterChanceModifier();
               spawnChances.add(enemySpawnData);
               enemyTemplates.add(enemyTemplate);
            }
         }
      }

      if (enemyTemplates.size() > 0) {
         int selected = Util.getInt(0, sum);
         int curValue = 0;

         for (int i = 0; i < spawnChances.size(); i++) {
            curValue += spawnChances.get(i).getEncounterChanceModifier();
            if (curValue >= selected) {
               return enemyTemplates.get(i).generateEnemy();
            }
         }

         log.error("Something went wrong, enemy should have been returned already");
         return enemyTemplates.get(enemyTemplates.size() - 1).generateEnemy();
      } else {
         log.error("No enemy found for location: {}", new Object[]{spawnLocation.toString()});
         return new ArrayList<>(Jasbro.getInstance().getEnemyTemplates().values()).get(0).generateEnemy();
      }
   }

   public static ComplexEnemy getEnemy(String id) {
      Map<String, ComplexEnemyTemplate> enemyTemplates = Jasbro.getInstance().getEnemyTemplates();
      return enemyTemplates.containsKey(id) ? enemyTemplates.get(id).generateEnemy() : null;
   }

   public static ComplexEnemy spawnCustomerEnemy() {
      Map<String, ComplexEnemyTemplate> enemyTemplates = Jasbro.getInstance().getEnemyTemplates();
      List<String> monsters = new ArrayList<>();

      for (ComplexEnemyTemplate enemyTemplate : enemyTemplates.values()) {
         if (enemyTemplate.isCustomerMonster()) {
            monsters.add(enemyTemplate.getId());
         }
      }

      int choice = Util.getInt(0, monsters.size());
      String monsterID = monsters.get(choice);
      return enemyTemplates.get(monsterID).generateEnemy();
   }
}
