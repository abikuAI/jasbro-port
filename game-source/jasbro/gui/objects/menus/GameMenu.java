package jasbro.gui.objects.menus;

import jasbro.Jasbro;
import jasbro.texts.TextUtil;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class GameMenu extends JMenu {
   public GameMenu() {
      super(TextUtil.t("ui.menu"));
      this.setMnemonic(18);
      JMenuItem mainMenuButton = new JMenuItem(new GameMenu.MainMenuAction());
      this.add(mainMenuButton);
      JMenuItem newGameButton = new JMenuItem(new GameMenu.NewGameAction());
      newGameButton.setMnemonic(78);
      newGameButton.setAccelerator(KeyStroke.getKeyStroke(78, 2));
      this.add(newGameButton);
      this.addSeparator();
      this.add(new SaveMenu());
      this.add(new LoadMenu());
      this.addSeparator();
      JMenuItem exitButton = new JMenuItem(new GameMenu.ExitAction());
      this.add(exitButton);
   }

   private class ExitAction extends AbstractAction {
      public ExitAction() {
         super(TextUtil.t("ui.quit"));
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         JFrame frame = Jasbro.getInstance().getGui();
         frame.dispatchEvent(new WindowEvent(frame, 201));
      }
   }

   private class MainMenuAction extends AbstractAction {
      public MainMenuAction() {
         super(TextUtil.t("ui.mainmenu"));
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         Jasbro.getInstance().getGui().showMainMenu();
      }
   }

   private class NewGameAction extends AbstractAction {
      public NewGameAction() {
         super(TextUtil.t("ui.newgame"));
      }

      @Override
      public void actionPerformed(ActionEvent e) {
         Jasbro.getInstance().getGui().showStartScreen();
      }
   }
}
