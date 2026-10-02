/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.character;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.gui.GuiUtil;
import jasbro.gui.objects.div.TranslucentPanel;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JLabel;

public class TraitPanel
extends TranslucentPanel {
    private Charakter character;

    public TraitPanel(Charakter character) {
        this.character = character;
        this.update();
        this.setPreferredSize(null);
    }

    @Override
    public void update() {
        this.removeAll();
        ArrayList<Trait> traits = new ArrayList<Trait>();
        for (Trait trait : this.character.getTraits()) {
            if (trait.isPerk()) continue;
            traits.add(trait);
        }
        if (traits.size() > 0) {
            FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("default:none")});
            this.setLayout(layout);
            JLabel label = new JLabel(TextUtil.t("ui.traits"));
            label.setFont(GuiUtil.DEFAULTBOLDFONT.deriveFont((float)GuiUtil.DEFAULTBOLDFONT.getSize() + 1.0f));
            this.add((Component)label, "1, 1, left, top");
            int i = 0;
            for (Trait trait : traits) {
                layout.appendRow(RowSpec.decode("min:none"));
                JLabel traitLabel = new JLabel(trait.getText());
                traitLabel.setToolTipText(trait.getDescription(this.character));
                traitLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
                this.add((Component)traitLabel, "1," + (i + 2) + ", left, top");
                ++i;
            }
            this.setVisible(true);
        } else {
            this.setVisible(false);
        }
    }
}

