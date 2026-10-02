package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Sex extends RunningActivity {
   private Sextype sexType;
   private Charakter character1;
   private Charakter character2;

   @Override
   public void init() {
      if (this.getCharacters().get(0).getGender() != Gender.MALE
         && this.getCharacters().get(0).getGender() != this.getCharacters().get(1).getGender()
         && (this.getCharacters().get(0).getGender() != Gender.FUTA || this.getCharacters().get(1).getGender() == Gender.MALE)) {
         this.character1 = this.getCharacters().get(1);
         this.character2 = this.getCharacters().get(0);
      } else {
         this.character1 = this.getCharacters().get(0);
         this.character2 = this.getCharacters().get(1);
      }

      if (this.getPlannedActivity().getSelectedOption() != null) {
         this.sexType = (Sextype)this.getPlannedActivity().getSelectedOption().getSelectionObject();
      } else {
         List<SelectionData<Sextype>> options = this.getSextypeOptions(this.character1, this.character2);
         List<ImageTag> tags1 = this.character1.getBaseTags();
         List<ImageTag> tags2 = this.character2.getBaseTags();
         tags1.add(0, ImageTag.NAKED);
         tags1.add(1, ImageTag.CLEANED);
         tags2.add(0, ImageTag.NAKED);
         tags2.add(1, ImageTag.CLEANED);
         SelectionData<Sextype> selectedOption = new SelectionScreen<Sextype>()
            .select(
               options,
               ImageUtil.getInstance().getImageDataByTags(tags1, this.character1.getImages()),
               ImageUtil.getInstance().getImageDataByTags(tags2, this.character2.getImages()),
               this.character1.getBackground(),
               TextUtil.t("sex.option.description", this.character1, this.character2)
            );
         this.sexType = selectedOption.getSelectionObject();
      }
   }

   @Override
   public MessageData getBaseMessage() {
      this.setMinimumObedience(this.sexType.getObedienceRequired());
      MessageData messageData = new MessageData();
      Future<MessageData> future = Jasbro.getThreadpool().submit(new ImageUtil.BestImageSelection(this.sexType, this.character1, this.character2));
      messageData.addFuture(future);
      return messageData;
   }

   @Override
   public List<RunningActivity.ModificationData> getStatModifications() {
      List<RunningActivity.ModificationData> modifications = new ArrayList<>();
      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 1.0F, this.sexType));
      if (this.sexType != Sextype.BONDAGE) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.OBEDIENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.03F, BaseAttributeTypes.STAMINA));
      } else {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.03F, BaseAttributeTypes.OBEDIENCE));
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, 0.01F, BaseAttributeTypes.STAMINA));
      }

      modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.ALL, -20.0F, EssentialAttributes.ENERGY));
      if (this.character1.getTraits().contains(Trait.NATURAL)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character1, 0.8F, EssentialAttributes.MOTIVATION));
      }

      if (this.character2.getTraits().contains(Trait.NATURAL)) {
         modifications.add(new RunningActivity.ModificationData(RunningActivity.TargetType.SINGLE, this.character2, 0.8F, EssentialAttributes.MOTIVATION));
      }

      return modifications;
   }

   @Override
   public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
      return plannedActivity.getCharacters().size() < 2
         ? null
         : new ArrayList<>(this.getSextypeOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
   }

   public List<SelectionData<Sextype>> getSextypeOptions(Charakter character1, Charakter character2) {
      List<Sextype> possibleSextypes = Sextype.getPossibleSextypes(character1.getGender(), character2.getGender());
      List<SelectionData<Sextype>> options = new ArrayList<>();

      for (Sextype curSexType : possibleSextypes) {
         SelectionData<Sextype> option = new SelectionData<>();
         option.setSelectionObject(curSexType);
         option.setButtonText(TextUtil.t("sex.option." + curSexType.toString(), character1, character2));
         option.setShortText(curSexType.getText());
         options.add(option);
      }

      return options;
   }

   @Override
   public Sextype getSextype() {
      return this.sexType;
   }
}
