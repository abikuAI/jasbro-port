package jasbro.gui.objects.menus;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PreferencePanel extends JPanel {
   private JSpinner saveSlotsSpinner;
   private JCheckBox cheatModeCheckbox;
   private JCheckBox protagonistIsPlayerCheckbox;
   private JCheckBox showLoliImagesCheckbox;
   private JCheckBox showBlackAndWhiteImagesCheckbox;
   private JCheckBox showFutaOnMaleImagesCheckbox;
   private JCheckBox show3dImagesCheckbox;
   private JCheckBox showCosplayCheckbox;
   private JCheckBox showTimeTakenCheckbox;
   private JCheckBox showSleepCheckbox;
   private JCheckBox newTownsceenCheckbox;
   private JLabel lblReloadItemData;
   private JButton btnReloadItems;
   private JLabel lblDevelopment;
   private JLabel lblImageFilter;
   private GuiPreferencePanel guiPreferencePanel;
   private JSpinner treeSpinner;
   private String resolutionWidth = "" + ConfigHandler.getSetting(Settings.RESOLUTIONWIDTH, 100);
   private String resolutionHeight = "" + ConfigHandler.getSetting(Settings.RESOLUTIONHEIGHT, 100);

   public PreferencePanel() {
      this.setLayout(
         new FormLayout(
            new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")},
            new RowSpec[]{
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("default:grow"),
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("10dlu"),
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               RowSpec.decode("10dlu"),
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC,
               FormFactory.DEFAULT_ROWSPEC
            }
         )
      );
      this.guiPreferencePanel = new GuiPreferencePanel();
      this.add(this.guiPreferencePanel, "1, 2, 2, 1, fill, fill");
      JLabel protagonistIsPlayerLabel = new JLabel("Address protagonist as 'you'");
      this.add(protagonistIsPlayerLabel, "1, 3");
      this.protagonistIsPlayerCheckbox = new JCheckBox("", ConfigHandler.isProtagonistPlayer());
      this.add(this.protagonistIsPlayerCheckbox, "2, 3");
      JLabel cheatModeLabel = new JLabel(TextUtil.t("ui.preferences.cheatmode"));
      cheatModeLabel.setToolTipText(TextUtil.t("ui.preferences.cheatmode.tooltip"));
      this.add(cheatModeLabel, "1, 4, left, center");
      this.cheatModeCheckbox = new JCheckBox("", ConfigHandler.isCheat());
      this.add(this.cheatModeCheckbox, "2, 4, left, top");
      JLabel saveSlotLabel = new JLabel(TextUtil.t("ui.preferences.saveslots"));
      saveSlotLabel.setToolTipText(TextUtil.t("ui.preferences.saveslots.tooltip"));
      this.add(saveSlotLabel, "1, 5, left, center");
      this.saveSlotsSpinner = new JSpinner(new SpinnerNumberModel(ConfigHandler.getSetting(Settings.SAVESLOTS, 3), 1, 32767, 1));
      this.add(this.saveSlotsSpinner, "2, 5, left, default");
      this.lblImageFilter = new JLabel("Image filter");
      this.add(this.lblImageFilter, "1, 7");
      this.lblImageFilter.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      JLabel showBlackAndWhiteImagesLabel = new JLabel(TextUtil.t("ui.preferences.showBlackAndWhiteImages"));
      this.add(showBlackAndWhiteImagesLabel, "1, 8, left, center");
      this.showBlackAndWhiteImagesCheckbox = new JCheckBox("", ConfigHandler.isShowBlackAndWhiteImages());
      this.add(this.showBlackAndWhiteImagesCheckbox, "2, 8, left, top");
      JLabel showLoliImagesLabel = new JLabel(TextUtil.t("ui.preferences.showLoliImages"));
      this.add(showLoliImagesLabel, "1, 9, left, center");
      this.showLoliImagesCheckbox = new JCheckBox("", ConfigHandler.isShowLoliImages());
      this.add(this.showLoliImagesCheckbox, "2, 9, left, top");
      JLabel showFutaOnMaleImagesLabel = new JLabel(TextUtil.t("ui.preferences.showFutaOnMaleImages"));
      this.add(showFutaOnMaleImagesLabel, "1, 10, left, center");
      this.showFutaOnMaleImagesCheckbox = new JCheckBox("", ConfigHandler.isShowFutaOnMaleImages());
      this.add(this.showFutaOnMaleImagesCheckbox, "2, 10, left, top");
      JLabel show3dImagesLabel = new JLabel(TextUtil.t("ui.preferences.show3dImages"));
      this.add(show3dImagesLabel, "1, 11, left, center");
      this.show3dImagesCheckbox = new JCheckBox("", ConfigHandler.isShow3dImages());
      this.add(this.show3dImagesCheckbox, "2, 11, left, top");
      JLabel showCosplayLabel = new JLabel(TextUtil.t("ui.preferences.showCosplayImages"));
      this.add(showCosplayLabel, "1, 12, left, center");
      this.showCosplayCheckbox = new JCheckBox("", ConfigHandler.isShowCosplay());
      this.add(this.showCosplayCheckbox, "2, 12, left, top");
      JLabel showSleepLabel = new JLabel(TextUtil.t("ui.preferences.showSleep"));
      this.add(showSleepLabel, "1, 13, left, center");
      this.showSleepCheckbox = new JCheckBox("", ConfigHandler.isShowSleep());
      this.add(this.showSleepCheckbox, "2, 13, left, top");
      this.lblDevelopment = new JLabel("Development");
      this.add(this.lblDevelopment, "1, 14");
      this.lblDevelopment.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
      this.lblReloadItemData = new JLabel("Reload items, quests, events, npcs (for testing purposes)");
      this.add(this.lblReloadItemData, "1, 15");
      this.btnReloadItems = new JButton("Reload All foreign files");
      this.btnReloadItems.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Jasbro.getInstance().setItems(null);
            Jasbro.getInstance().getData().getUnlocks().init();
            Jasbro.getInstance().setCustomQuestTemplates(null);
            Jasbro.getInstance().setWorldEvents(null);
            Jasbro.getInstance().getData().getQuestManager().setInactiveQuests(null);
            Jasbro.getInstance().setEnemyTemplates(null);
         }
      });
      this.add(this.btnReloadItems, "2, 15");
      JLabel treeLabel = new JLabel(TextUtil.t("ui.preferences.trees"));
      treeLabel.setToolTipText(TextUtil.t("ui.preferences.trees.tooltip"));
      this.add(treeLabel, "1, 16, left, center");
      this.treeSpinner = new JSpinner(new SpinnerNumberModel(ConfigHandler.getSetting(Settings.TREES, 100), 1, 32767, 1));
      this.add(this.treeSpinner, "2, 16, left, default");
      this.treeSpinner.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            Jasbro.maxTrees = (Integer)((JSpinner)e.getSource()).getValue();
         }
      });
      JLabel resolutionLabel = new JLabel(TextUtil.t("ui.preferences.resolution"));
      resolutionLabel.setToolTipText(TextUtil.t("ui.preferences.resolution.tooltip"));
      this.add(resolutionLabel, "1, 17, left, center");
      String[] resolutionChoice = new String[]{"1280x720", "1366x768", "1600x900", "1920x1080", "2560x1440"};
      final Integer[] resHeight = new Integer[]{720, 768, 900, 1080, 1440};
      final Integer[] resWidth = new Integer[]{1280, 1366, 1600, 1920, 2560};
      final JComboBox<String> resolutionCB = new JComboBox<>(resolutionChoice);
      int index = 0;
      if (this.resolutionWidth.equals("1280")) {
         index = 0;
      }

      if (this.resolutionWidth.equals("1366")) {
         index = 1;
      }

      if (this.resolutionWidth.equals("1600")) {
         index = 2;
      }

      if (this.resolutionWidth.equals("1920")) {
         index = 3;
      }

      if (this.resolutionWidth.equals("2560")) {
         index = 4;
      }

      resolutionCB.setSelectedIndex(index);
      this.resolutionWidth = resWidth[index].toString();
      this.resolutionHeight = resHeight[index].toString();
      this.add(resolutionCB, "2, 17, left, default");
      resolutionCB.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            int res = resolutionCB.getSelectedIndex();
            PreferencePanel.this.resolutionWidth = resWidth[res].toString();
            PreferencePanel.this.resolutionHeight = resHeight[res].toString();
         }
      });
      JLabel newTownscreenLabel = new JLabel(TextUtil.t("ui.preferences.showTownscreenNew"));
      this.add(newTownscreenLabel, "1, 18, left, center");
      this.newTownsceenCheckbox = new JCheckBox("", ConfigHandler.isTownScreenNew());
      this.add(this.newTownsceenCheckbox, "2, 18, left, top");
      JLabel showTimeTakenLabel = new JLabel(TextUtil.t("ui.preferences.showTimeTaken"));
      this.add(showTimeTakenLabel, "1, 19, left, center");
      this.showTimeTakenCheckbox = new JCheckBox("", ConfigHandler.isShowTimeTaken());
      this.add(this.showSleepCheckbox, "2, 19, left, top");
   }

   public void applyChanges() throws FileNotFoundException, IOException {
      if (ConfigHandler.changeSetting(Settings.SAVESLOTS, this.saveSlotsSpinner.getValue().toString())) {
         Jasbro.getInstance().getGui().getMainMenuBar().rebuildSaveMenu();
      }

      ConfigHandler.setCheat(this.cheatModeCheckbox.isSelected());
      ConfigHandler.setShowBlackAndWhiteImages(this.showBlackAndWhiteImagesCheckbox.isSelected());
      ConfigHandler.setShowLoliImages(this.showLoliImagesCheckbox.isSelected());
      ConfigHandler.setShowFutaOnMaleImages(this.showFutaOnMaleImagesCheckbox.isSelected());
      ConfigHandler.setShow3DImages(this.show3dImagesCheckbox.isSelected());
      ConfigHandler.setTownScreenNew(this.newTownsceenCheckbox.isSelected());
      ConfigHandler.setShowCosplay(this.showCosplayCheckbox.isSelected());
      ConfigHandler.setShowCosplay(this.showCosplayCheckbox.isSelected());
      ConfigHandler.setShowSleep(this.showSleepCheckbox.isSelected());
      ConfigHandler.setShowTimeTaken(this.showTimeTakenCheckbox.isSelected());
      ConfigHandler.setHideArrowKeys(this.guiPreferencePanel.getHideArrowKeysCheckbox().isSelected());
      ConfigHandler.setProtagonistIsPlayer(this.protagonistIsPlayerCheckbox.isSelected());
      ConfigHandler.setUseSystemLookAndFeel(this.guiPreferencePanel.getUseSystemLookAndFellCheckbox().isSelected());
      ConfigHandler.setShowNumbersOnBars(this.guiPreferencePanel.getShowNumbersOnBarsCheckbox().isSelected());
      ConfigHandler.changeSetting(Settings.TREES, this.treeSpinner.getValue().toString());
      ConfigHandler.changeResolution(Settings.RESOLUTIONHEIGHT, this.resolutionHeight);
      ConfigHandler.changeResolution(Settings.RESOLUTIONWIDTH, this.resolutionWidth);
      Jasbro.getInstance().getGui().changeResolution(new Integer(this.resolutionWidth), new Integer(this.resolutionHeight));
   }
}
