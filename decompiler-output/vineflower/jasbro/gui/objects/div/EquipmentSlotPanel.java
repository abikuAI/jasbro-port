package jasbro.gui.objects.div;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.game.items.EquipmentSlot;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class EquipmentSlotPanel extends MyImage {
   private EquippedItemPanel headSlotPanel;
   private EquippedItemPanel outfitSlotPanel;
   private EquippedItemPanel underwearSlotPanel;
   private EquippedItemPanel shoesSlotPanel;
   private EquippedItemPanel accessory1SlotPanel;
   private EquippedItemPanel accessory2SlotPanel;
   private EquippedItemPanel accessory3SlotPanel;
   private EquippedItemPanel accessory4SlotPanel;

   public EquipmentSlotPanel(Charakter character, CharacterScreen characterScreen) {
      this.setBackground(Color.WHITE);
      this.setBorder(new LineBorder(Color.BLACK));
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{
               FormFactory.UNRELATED_GAP_COLSPEC,
               ColumnSpec.decode("23dlu"),
               ColumnSpec.decode("default:grow"),
               ColumnSpec.decode("23dlu"),
               ColumnSpec.decode("default:grow"),
               ColumnSpec.decode("23dlu"),
               FormFactory.UNRELATED_GAP_COLSPEC
            },
            new RowSpec[]{
               RowSpec.decode("4dlu:grow"),
               RowSpec.decode("23dlu"),
               FormFactory.UNRELATED_GAP_ROWSPEC,
               RowSpec.decode("23dlu"),
               FormFactory.RELATED_GAP_ROWSPEC,
               RowSpec.decode("23dlu"),
               FormFactory.RELATED_GAP_ROWSPEC,
               RowSpec.decode("23dlu"),
               FormFactory.RELATED_GAP_ROWSPEC
            }
         )
      );
      this.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEANED, character));
      this.headSlotPanel = new EquippedItemPanel(character, EquipmentSlot.HEAD, characterScreen);
      this.add(this.headSlotPanel, "4, 2, fill, fill");
      this.outfitSlotPanel = new EquippedItemPanel(character, EquipmentSlot.DRESS, characterScreen);
      this.add(this.outfitSlotPanel, "4, 4, fill, fill");
      this.underwearSlotPanel = new EquippedItemPanel(character, EquipmentSlot.UNDERWEAR, characterScreen);
      this.add(this.underwearSlotPanel, "4, 6, fill, fill");
      this.shoesSlotPanel = new EquippedItemPanel(character, EquipmentSlot.SHOES, characterScreen);
      this.add(this.shoesSlotPanel, "4, 8, fill, fill");
      this.accessory1SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY1, characterScreen);
      this.add(this.accessory1SlotPanel, "2, 6, fill, fill");
      this.accessory2SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY2, characterScreen);
      this.add(this.accessory2SlotPanel, "2, 8, fill, fill");
      this.accessory3SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY3, characterScreen);
      this.add(this.accessory3SlotPanel, "6, 6, fill, fill");
      this.accessory4SlotPanel = new EquippedItemPanel(character, EquipmentSlot.ACCESSORY4, characterScreen);
      this.add(this.accessory4SlotPanel, "6, 8, fill, fill");
   }

   @Override
   public void update() {
      this.headSlotPanel.updateEquipmentIcon();
      this.outfitSlotPanel.updateEquipmentIcon();
      this.underwearSlotPanel.updateEquipmentIcon();
      this.shoesSlotPanel.updateEquipmentIcon();
      this.accessory1SlotPanel.updateEquipmentIcon();
      this.accessory2SlotPanel.updateEquipmentIcon();
      this.accessory3SlotPanel.updateEquipmentIcon();
      this.accessory4SlotPanel.updateEquipmentIcon();
   }
}
