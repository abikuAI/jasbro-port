package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.Item;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.List;

public class MonsterPregnancy extends Condition implements PregnancyInterface {
   private int days = 7;
   private int eggs = 2;
   private static final String itemString = "MonsterEgg";

   public MonsterPregnancy(Charakter mother, MyEvent event) {
      this(mother, event, true);
   }

   public MonsterPregnancy(Charakter mother, MyEvent event, boolean message) {
      int chanceAdditionalChild = mother.getChanceAdditionalChild() + 20;
      this.days = (int)(this.days * mother.getPregnancyDurationModifier() / 100.0F);
      int minChildren = mother.getMinChildren();
      int maxChildren = mother.getMaxChildren() + 12;
      if (mother.getTraits().contains(Trait.INHUMANPREGNANCY)) {
         chanceAdditionalChild *= 2;
         maxChildren *= 2;
      }

      if (minChildren < 1) {
         minChildren = 1;
      }

      this.eggs = minChildren;

      for (int i = minChildren; i < maxChildren && Util.getInt(0, 100) < chanceAdditionalChild; i++) {
         this.eggs++;
      }

      if (message) {
         MessageData messageData = new MessageData();
         messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.PREGNANT, mother));
         messageData.addToMessage(TextUtil.t("monsterpregnancy.pregnant", mother));
         messageData.setBackground(mother.getBackground());
         messageData.setPriorityMessage(true);
         GuiUtil.addMessageToEvent(messageData, event);
      }

      mother.addCondition(new ItemCooldown(7, "Elixir_of_growth"));
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY) {
         this.days--;
         if (this.days <= 0 && Jasbro.getInstance().getItems().containsKey("MonsterEgg")) {
            Item item = Jasbro.getInstance().getItems().get("MonsterEgg");
            this.getCharacter().getConditions().remove(this);
            MessageData messageData = new MessageData();
            messageData.addToMessage(TextUtil.t("monsterpregnancy.birth", this.getCharacter(), this.eggs));
            messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTERBIRTH, this.getCharacter()));
            this.getCharacter().getCounter().add(CharacterStuffCounter.CounterNames.CHILDREN);
            messageData.setBackground(this.getCharacter().getBackground());
            messageData.setPriorityMessage(true);
            GuiUtil.addMessageToEvent(messageData, e);
            Jasbro.getInstance().getData().getInventory().addItems(item, this.eggs);
         }
      }
   }

   @Override
   public ImageData getIcon() {
      return new ImageData("images/icons/perks/beastbreeder.png");
   }

   @Override
   public int getDays() {
      return this.days;
   }

   @Override
   public void reduceDays(int amount) {
      this.days -= amount;
   }

   @Override
   public String getName() {
      return TextUtil.t("monsterpregnancy", this.getCharacter());
   }

   @Override
   public String getDescription() {
      return this.getName() + "\n" + TextUtil.t("monsterpregnancy.description", this.days);
   }

   @Override
   public void modifyDays(int amount) {
      this.days += amount;
   }

   @Override
   public void modifyImageTags(List<ImageTag> imageTags) {
      if (this.days < 55) {
         imageTags.add(ImageTag.PREGNANT);
      }
   }
}
