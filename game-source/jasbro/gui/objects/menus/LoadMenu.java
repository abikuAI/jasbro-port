package jasbro.gui.objects.menus;

import jasbro.gui.objects.menus.actions.SlotLoadAction;
import jasbro.texts.TextUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class LoadMenu extends JMenu {
   public LoadMenu() {
      super(TextUtil.t("ui.load"));
      JMenuItem loadQuickButton = new JMenuItem(new SlotLoadAction(TextUtil.t("ui.quickload"), -1));
      loadQuickButton.setMnemonic(81);
      this.add(loadQuickButton);
      File[] saveGames = this.getSaveGames();
      if (saveGames != null) {
         Pattern p = Pattern.compile("(?<=save)\\d*(?=.xml)");

         for (File f : saveGames) {
            Matcher m = p.matcher(f.getName());
            int i = 0;
            if (m.find()) {
               try {
                  i = Integer.parseInt(m.group());
               } catch (NumberFormatException e) {
               }
            }

            if (i > 0) {
               JMenuItem loadSlotButton = new JMenuItem(new SlotLoadAction(i));
               this.add(loadSlotButton);
               if (i < 10) {
                  loadSlotButton.setMnemonic(48 + i);
                  loadSlotButton.setAccelerator(KeyStroke.getKeyStroke(48 + i, 8));
               }
            }
         }
      }
   }

   public File[] getSaveGames() {
      return new File(".").listFiles(new FilenameFilter() {
         @Override
         public boolean accept(File directory, String name) {
            return name.endsWith(".xml");
         }
      });
   }
}
