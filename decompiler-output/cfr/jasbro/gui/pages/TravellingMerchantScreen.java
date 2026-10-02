/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.ItemLocation;
import jasbro.gui.objects.div.MessageInterface;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pages.subView.ShopPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class TravellingMerchantScreen
extends MyImage
implements MessageInterface {
    public TravellingMerchantScreen() {
        this.setLayout(new FormLayout(new ColumnSpec[]{FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.UNRELATED_GAP_COLSPEC}, new RowSpec[]{FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC}));
        this.add((Component)new ShopPanel(ItemLocation.TRAVELLINGMERCHANT), "2, 2, fill, fill");
        JButton btnNewButton = new JButton(TextUtil.t("ui.close"));
        this.add((Component)btnNewButton, "2, 3, left, default");
        btnNewButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().removeLayer(TravellingMerchantScreen.this);
            }
        });
    }

    @Override
    public void init() {
        this.setBackgroundImage(new ImageData("images/backgrounds/travellingMerchant.png"));
    }

    @Override
    public boolean isPriorityMessage() {
        return true;
    }

    @Override
    public void setMessageGroupObject(Object groupObject) {
    }

    @Override
    public Object getMessageGroupObject() {
        return null;
    }
}

