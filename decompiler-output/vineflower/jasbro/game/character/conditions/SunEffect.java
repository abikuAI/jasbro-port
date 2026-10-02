package jasbro.game.character.conditions;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.util.List;

public class SunEffect extends Buff {
   private boolean sunburn = false;
   private int intensity;

   public SunEffect() {
      super("buff.lightTan", new ImageData(), 10);
   }

   @Override
   public void init() {
      Buff existingBuff = this.getExistingBuff();
      if (existingBuff != null) {
         ((SunEffect)existingBuff).increaseIntensity();
         this.getCharacter().getConditions().remove(this);
      } else {
         this.updateEffect();
      }
   }

   public void increaseIntensity() {
      int intensity = this.getIntensity() + 10;
      this.setIntensity(intensity);
      this.setRemainingTime(this.getRemainingTime() + 10);
      this.updateEffect();
      if (this.sunburn) {
         this.getCharacter().getAttribute(EssentialAttributes.HEALTH).addToValue(-5.0F);
      }
   }

   @Override
   public void handleEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTSHIFT) {
         this.setRemainingTime(this.getRemainingTime() - 1);
         this.setIntensity(this.getIntensity() - 1);
         this.updateEffect();
         if (this.getRemainingTime() <= 0) {
            this.getCharacter().getConditions().remove(this);
         }
      }
   }

   public void updateEffect() {
      if (!this.sunburn) {
         int intensity = this.getIntensity();
         if (intensity <= 10) {
            this.setNameKey("buff.lightTan");
            this.getAttributeModifiers().clear();
            this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 3);
         } else if (intensity <= 20) {
            this.setNameKey("buff.tan");
            this.getAttributeModifiers().clear();
            this.addAttributeBuff(BaseAttributeTypes.CHARISMA, 10);
         } else {
            this.sunburn = true;
            this.setNameKey("buff.sunburn");
            this.getAttributeModifiers().clear();
            this.addAttributeBuff(BaseAttributeTypes.CHARISMA, -10);
            this.setRemainingTime(15);
         }
      }
   }

   @Override
   public ImageData getIcon() {
      if (this.getNameKey().equals("buff.lightTan")) {
         return new ImageData("images/icons/light_tan.png");
      } else {
         return this.getNameKey().equals("buff.tan") ? new ImageData("images/icons/tan.png") : new ImageData("images/icons/sunburn.png");
      }
   }

   @Override
   public String getDescription() {
      if (!this.sunburn) {
         return super.getDescription();
      }

      Object[] arguments = new Object[]{this.getRemainingTime()};
      return TextUtil.t("buff.sunburn.description", this.getCharacter(), arguments);
   }

   public int getIntensity() {
      return this.intensity;
   }

   public void setIntensity(int intensity) {
      this.intensity = intensity;
   }

   public boolean isSunburn() {
      return this.sunburn;
   }

   @Override
   public void modifyImageTags(List<ImageTag> imageTags) {
      if (!this.sunburn) {
         imageTags.add(ImageTag.TANNED);
      }
   }
}
