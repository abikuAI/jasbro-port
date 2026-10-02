/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.character;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.pages.CharacterScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class CharacterScreenCenterPanel
extends JPanel {
    private MyButton leftButton;
    private MyButton rightButton;

    public CharacterScreenCenterPanel(final Charakter character, final CharacterScreen parent) {
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("30dlu:none"), ColumnSpec.decode("default:grow(30)"), ColumnSpec.decode("30dlu:none"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow(15)"), RowSpec.decode("30dlu:none"), RowSpec.decode("default:grow")}));
        this.setPreferredSize(new Dimension(9999, 9999));
        this.setOpaque(false);
        final List<Charakter> characters = Jasbro.getInstance().getData().getCharacters();
        if (characters.contains(character)) {
            if (characters.indexOf(character) > 0) {
                this.leftButton = new MyButton("", new ImageData("images/icons/arrow_left.png"), new ImageData("images/icons/arrow_left.png"));
                if (!ConfigHandler.isHideArrowKeys()) {
                    this.add((Component)this.leftButton, "2, 2, fill, fill");
                } else {
                    this.add((Component)this.leftButton, "2, 2, left, center");
                }
                this.leftButton.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView((Charakter)characters.get(characters.indexOf(character) - 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
                this.leftButton.getInputMap(2).put(KeyStroke.getKeyStroke(37, 0), "pressed");
                this.leftButton.getActionMap().put("pressed", new AbstractAction(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView((Charakter)characters.get(characters.indexOf(character) - 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
            }
            if (characters.indexOf(character) < characters.size() - 1) {
                this.rightButton = new MyButton("", new ImageData("images/icons/arrow_right.png"), new ImageData("images/icons/arrow_right.png"));
                if (!ConfigHandler.isHideArrowKeys()) {
                    this.add((Component)this.rightButton, "4, 2, fill, fill");
                } else {
                    this.add((Component)this.rightButton, "4, 2, left, center");
                }
                this.rightButton.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView((Charakter)characters.get(characters.indexOf(character) + 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
                this.rightButton.getInputMap(2).put(KeyStroke.getKeyStroke(39, 0), "pressed");
                this.rightButton.getActionMap().put("pressed", new AbstractAction(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Jasbro.getInstance().getGui().showCharacterView((Charakter)characters.get(characters.indexOf(character) + 1));
                        Jasbro.getInstance().getGui().removeLayer(parent);
                    }
                });
            }
        }
    }
}

