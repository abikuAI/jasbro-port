/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.Ownership;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.gui.character.CharacterScreenCenterPanel;
import jasbro.gui.character.CharacterScreenInfoPanel;
import jasbro.gui.character.CharacterScreenInventoryPanel;
import jasbro.gui.character.CharacterScreenNamePanel;
import jasbro.gui.character.CharacterScreenOptionsPanel;
import jasbro.gui.character.SpecializationPanel;
import jasbro.gui.character.TraitPanel;
import jasbro.gui.objects.div.AllowedServicesPanel;
import jasbro.gui.objects.div.EquipmentSlotPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CharacterScreen
extends MyImage
implements MyEventListener {
    private Charakter character;
    private List<MyPanel> leftColumns = new ArrayList<MyPanel>();
    private List<MyPanel> rightColumns = new ArrayList<MyPanel>();

    private CharacterScreen() {
        this(new Charakter(null));
    }

    public CharacterScreen(final Charakter character) {
        this.character = character;
        this.setBackgroundImage(character.getBackground());
        this.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
        this.leftColumns.add(new CharacterScreenNamePanel(character));
        this.leftColumns.add(new CharacterScreenInfoPanel(character));
        if (!character.getType().isChildType()) {
            this.leftColumns.add(new TraitPanel(character));
        }
        for (SpecializationType specializationType : character.getSpecializations()) {
            this.leftColumns.add(new SpecializationPanel(specializationType, this.character));
        }
        MyPanel closePanel = new MyPanel();
        JButton btnClose = new JButton("Close");
        closePanel.addSingle(btnClose);
        this.leftColumns.add(closePanel);
        btnClose.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().removeLayer(CharacterScreen.this);
            }
        });
        if (!character.getType().isChildType() && character.getOwnership() == Ownership.OWNED && (character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY))) {
            this.rightColumns.add(new CharacterScreenOptionsPanel(character));
        }
        if (!character.getType().isChildType() && Jasbro.getInstance().getData().getCharacters().contains(character)) {
            this.rightColumns.add(new AllowedServicesPanel(character));
        }
        if (character.getType() == CharacterType.TRAINER && character != Jasbro.getInstance().getData().getProtagonist() && Jasbro.getInstance().getData().getTrainers().contains(character)) {
            MyPanel firePanel = new MyPanel();
            firePanel.setPreferredSize(null);
            JButton fireButton = new JButton(TextUtil.t("ui.fire"));
            firePanel.addSingle(fireButton);
            this.rightColumns.add(firePanel);
            fireButton.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(CharacterScreen.this, TextUtil.t("ui.firetrainer", character), "fire", 0);
                    if (confirm == 0) {
                        new MessageData(TextUtil.t("ui.trainerfired", character), ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character), character.getBackground()).createMessageScreen();
                        Jasbro.getInstance().getGui().removeLayer(CharacterScreen.this);
                        Jasbro.getInstance().removeCharacter(character);
                    }
                }
            });
        }
        if (Jasbro.getInstance().getData().getCharacters().contains(character)) {
            this.rightColumns.add(new EquipmentSlotPanel(character, this));
            this.rightColumns.add(new CharacterScreenInventoryPanel(character, this));
        }
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                CharacterScreen.this.setInsetX(CharacterScreen.this.getWidth() / 8);
                CharacterScreen.this.redoLayout();
            }
        });
        character.addListener(this);
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentShown(ComponentEvent e) {
                CharacterScreen.this.update();
            }
        });
    }

    public void redoLayout() {
        FormLayout columnLayout;
        JPanel columnPanel;
        this.removeAll();
        this.invalidate();
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("3dlu:none"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(20)"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("3dlu:none")}, new RowSpec[]{RowSpec.decode("3dlu:none"), RowSpec.decode("default:grow"), RowSpec.decode("3dlu:none")});
        layout.setColumnGroups(new int[][]{{2, 3, 5, 6}});
        this.setLayout(layout);
        ArrayList<JPanel> columnPanels = new ArrayList<JPanel>();
        for (int i = 1; i < 6; ++i) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setBorder(GuiUtil.DEFAULTEMPTYBORDER);
            columnPanels.add(panel);
            panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow(200)")}));
            this.add((Component)panel, i + 1 + ", 2, fill, fill");
        }
        this.validate();
        if (((JPanel)columnPanels.get(0)).getHeight() == 0) {
            System.gc();
            return;
        }
        int columnSize = 5;
        int column = 0;
        for (JComponent jComponent : this.leftColumns) {
            columnPanel = (JPanel)columnPanels.get(column);
            columnLayout = (FormLayout)columnPanel.getLayout();
            if (columnLayout.preferredLayoutSize((Container)this).height + jComponent.getPreferredSize().height > columnPanel.getHeight()) {
                if (++column == 2) {
                    ++column;
                }
                columnPanel = (JPanel)columnPanels.get(column);
                columnLayout = (FormLayout)columnPanel.getLayout();
            }
            if (jComponent instanceof JButton && ((FormLayout)((JPanel)columnPanels.get((int)0)).getLayout()).preferredLayoutSize((Container)this).height + jComponent.getPreferredSize().height <= ((JPanel)columnPanels.get(0)).getHeight()) {
                this.addToColumnPanelTop(jComponent, (JPanel)columnPanels.get(0));
                continue;
            }
            this.addToColumnPanelTop(jComponent, columnPanel);
        }
        column = columnSize - 1;
        for (JComponent jComponent : this.rightColumns) {
            columnPanel = (JPanel)columnPanels.get(column);
            columnLayout = (FormLayout)columnPanel.getLayout();
            if (columnLayout.minimumLayoutSize((Container)this).height + jComponent.getMinimumSize().height > columnPanel.getHeight()) {
                columnPanel = (JPanel)columnPanels.get(--column);
                columnLayout = (FormLayout)columnPanel.getLayout();
            }
            if (column == columnSize - 1) {
                this.addToColumnPanelTop(jComponent, columnPanel);
                continue;
            }
            this.addToColumnPanelBottom(jComponent, columnPanel);
        }
        this.addToColumnPanelBottom(new CharacterScreenCenterPanel(this.character, this), (JPanel)columnPanels.get(columnSize / 2));
        this.requestFocus();
        this.validate();
        this.repaint();
    }

    private void addToColumnPanelTop(Component component, JPanel panel) {
        FormLayout layout = (FormLayout)panel.getLayout();
        int row = layout.getRowCount();
        if (component instanceof CharacterScreenInventoryPanel) {
            layout.insertRow(row, RowSpec.decode("default:grow(999999)"));
            panel.add(component, "1," + row + ", fill, fill");
        } else {
            layout.insertRow(row, RowSpec.decode("default:grow"));
            layout.insertRow(row + 1, RowSpec.decode("2dlu:none"));
            panel.add(component, "1," + row + ", fill, top");
        }
    }

    private void addToColumnPanelBottom(Component component, JPanel panel) {
        FormLayout layout = (FormLayout)panel.getLayout();
        int row = layout.getRowCount();
        layout.appendRow(RowSpec.decode("default:grow"));
        layout.appendRow(RowSpec.decode("2dlu:none"));
        panel.add(component, "1," + (row + 1) + ", fill, top");
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.STATUSCHANGE) {
            this.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this.character));
            this.update();
        }
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                for (MyPanel myPanel : CharacterScreen.this.leftColumns) {
                    myPanel.update();
                }
                for (MyPanel myPanel : CharacterScreen.this.rightColumns) {
                    myPanel.update();
                }
                CharacterScreen.this.redoLayout();
            }
        });
    }
}

