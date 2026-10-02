/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.objects.div;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.objects.div.MyImage;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.swing.JPanel;

public class CharacterConditionsPanel
extends JPanel {
    private Charakter character;
    private MyImage workView;
    private JPanel conditionIcons;

    public CharacterConditionsPanel() {
        DelegateMouseListener listener = new DelegateMouseListener();
        this.addMouseMotionListener(listener);
        this.addMouseListener(listener);
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("1dlu:grow"), RowSpec.decode("1dlu:grow")}));
        this.setOpaque(false);
        this.workView = new MyImage();
        this.workView.addMouseMotionListener(listener);
        this.workView.addMouseListener(listener);
        this.add((Component)this.workView, "1, 1, fill, fill");
        this.conditionIcons = new JPanel();
        this.add((Component)this.conditionIcons, "1, 2, fill, fill");
        this.conditionIcons.setOpaque(false);
    }

    public Charakter getCharacter() {
        return this.character;
    }

    public void setCharacter(Charakter character) {
        this.character = character;
        this.init();
    }

    public void init() {
        this.conditionIcons.removeAll();
        if (this.character.getConditions().size() > 0) {
            List<Condition> conditions = this.character.getConditions();
            if (conditions.size() < 3) {
                this.conditionIcons.setLayout(new GridLayout(1, 3, 5, 5));
            } else if (conditions.size() < 4) {
                this.conditionIcons.setLayout(new GridLayout(1, 3, 2, 2));
            } else if (conditions.size() < 9) {
                this.conditionIcons.setLayout(new GridLayout(2, 4, 1, 1));
            } else {
                this.conditionIcons.setLayout(new GridLayout(0, 5, 1, 1));
            }
            while (true) {
                try {
                    this.conditionIcons.removeAll();
                    for (Condition condition : this.character.getConditions()) {
                        MyImage myImage = new MyImage(condition.getIcon());
                        this.conditionIcons.add(myImage);
                        myImage.setToolTipText(TextUtil.htmlPreformatted(condition.getDescription()));
                    }
                }
                catch (ConcurrentModificationException e) {
                    try {
                        Thread.sleep(20L);
                    }
                    catch (InterruptedException interruptedException) {}
                    continue;
                }
                break;
            }
        }
        this.update();
    }

    public void update() {
        if (this.character != null) {
            this.workView.setImage(this.character.getWarnImage());
            this.workView.setToolTipText(TextUtil.htmlPreformatted(this.character.getWarnString()));
        }
    }
}

