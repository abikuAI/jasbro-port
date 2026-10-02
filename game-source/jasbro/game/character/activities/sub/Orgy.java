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

public class Orgy extends RunningActivity {
   private Map<Gender, Integer> genderAmounts = new EnumMap<>(Gender.class);
   private boolean threesome = false;

   @Override
   public void init() {
      for (Gender gender : Gender.values()) {
         this.genderAmounts.put(gender, 0);
      }

      for (Charakter character : this.getCharacters()) {
         Gender curGender = character.getGender();
         this.genderAmounts.put(curGender, this.genderAmounts.get(curGender) + 1);
      }

      if (this.getCharacters().size() == 3) {
         this.threesome = true;
      }
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      if (this.threesome) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.VAGINAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.ANAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.ORAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.TITFUCK));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.FOREPLAY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.2F, Sextype.GROUP));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.04F, BaseAttributeTypes.STAMINA));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -25.0F, EssentialAttributes.ENERGY));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.VAGINAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.ANAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.ORAL));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.TITFUCK));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.FOREPLAY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 2.5F, Sextype.GROUP));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.STRENGTH));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.STAMINA));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -45.0F, EssentialAttributes.ENERGY));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -1.5F, EssentialAttributes.MOTIVATION));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.OBEDIENCE));
      return modifications;
   }

   @Override
   public MessageData getBaseMessage() {
      List<Charakter> characters = this.getCharacters();
      List<ImageData> images = new ArrayList<>();

      for (Charakter character : characters) {
         images.addAll(character.getImages());
      }

      List<ImageTag> tags = new ArrayList<>();
      tags.addAll(ImageTag.getAssociatedImageTags(characters.toArray(new Person[characters.size()])));
      ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, images);
      String message;
      if (this.threesome) {
         message = TextUtil.t("threesome.basic", this.getCharacters());
      } else {
         message = TextUtil.t("orgy.basic", this.getCharacters());
      }

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
