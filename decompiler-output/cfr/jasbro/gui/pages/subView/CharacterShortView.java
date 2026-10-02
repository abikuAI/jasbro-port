/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages.subView;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.MyEventListener;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.gui.RPGView;
import jasbro.gui.objects.div.CharacterConditionsPanel;
import jasbro.gui.objects.div.IconAttributePanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.div.VerticalAttributeBar;
import jasbro.gui.pages.CharacterScreen;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class CharacterShortView
extends JPanel
implements MyEventListener {
    private Charakter character;
    private List<IconAttributePanel> attributePanels;
    private JPanel attributePane;
    private MyImage characterIcon;
    private JPanel barPanel;
    private VerticalAttributeBar energyBar;
    private VerticalAttributeBar healthBar;
    private VerticalAttributeBar motivationBar;
    private CharacterConditionsPanel characterConditionsPanel;

    public CharacterShortView() {
        this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        this.initComponents();
    }

    public CharacterShortView(Charakter character) {
        this();
        this.setCharacter(character);
    }

    public CharacterShortView(Charakter character, boolean conditionsPanel) {
        this(character);
        if (!conditionsPanel) {
            this.characterConditionsPanel.setVisible(false);
        }
    }

    private void initComponents() {
        this.characterIcon = new MyImage();
        this.attributePane = new JPanel();
        this.attributePanels = new ArrayList<IconAttributePanel>();
        this.setBackground(new Color(232, 203, 142));
        this.setMinimumSize(new Dimension(12, 24));
        this.setPreferredSize(new Dimension(120, 240));
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow"), ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("1dlu:grow"), RowSpec.decode("1dlu:grow(3)")}));
        this.characterIcon.setMaximumSize(new Dimension(99999, 999999));
        this.characterIcon.setMinimumSize(new Dimension(20, 20));
        this.characterIcon.setPreferredSize(new Dimension(20, 20));
        this.add((Component)this.characterIcon, "1, 1, fill, fill");
        this.characterConditionsPanel = new CharacterConditionsPanel();
        this.add((Component)this.characterConditionsPanel, "2, 1, fill, fill");
        this.barPanel = new JPanel();
        this.barPanel.setOpaque(false);
        this.add((Component)this.barPanel, "1, 2, fill, fill");
        this.barPanel.setLayout(new GridLayout(0, 3, 0, 0));
        this.energyBar = new VerticalAttributeBar();
        this.barPanel.add(this.energyBar);
        this.healthBar = new VerticalAttributeBar();
        this.barPanel.add(this.healthBar);
        this.motivationBar = new VerticalAttributeBar();
        this.barPanel.add(this.motivationBar);
        this.attributePane.setMinimumSize(new Dimension(20, 60));
        this.attributePane.setOpaque(false);
        this.attributePane.setPreferredSize(new Dimension(20, 60));
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)"), RowSpec.decode("default:grow"), RowSpec.decode("default:grow(20)")});
        this.attributePane.setLayout(layout);
        layout.setRowGroups(new int[][]{{1, 3, 5, 7, 9}});
        for (int i = 0; i < 5; ++i) {
            IconAttributePanel attributePanel = new IconAttributePanel();
            this.attributePanels.add(attributePanel);
            this.attributePane.add((Component)attributePanel, "1, " + (i * 2 + 1) + ", fill, fill");
        }
        this.add((Component)this.attributePane, "2, 2, fill, fill");
        DelegateMouseListener listener = new DelegateMouseListener();
        this.characterIcon.addMouseMotionListener(listener);
        this.characterIcon.addMouseListener(listener);
        this.attributePane.addMouseMotionListener(listener);
        this.attributePane.addMouseListener(listener);
        this.barPanel.addMouseMotionListener(listener);
        this.barPanel.addMouseListener(listener);
        DelegateMouseListener iconDelegateMouseListener = new DelegateMouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!(Jasbro.getInstance().getGui().getLayerPane().getComponent(0) instanceof CharacterScreen)) {
                    Jasbro.getInstance().getGui().showCharacterView(CharacterShortView.this.character);
                }
            }
        };
        this.characterIcon.addMouseListener(iconDelegateMouseListener);
        this.characterIcon.addMouseMotionListener(iconDelegateMouseListener);
        this.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                CharacterShortView.this.updateFontSize();
            }
        });
    }

    public Charakter getCharacter() {
        return this.character;
    }

    public final void setCharacter(Charakter character) {
        this.characterIcon.setToolTipText(character.getName());
        this.characterIcon.setImage(character.getIcon());
        this.healthBar.setAttribute(character.getAttribute(EssentialAttributes.HEALTH));
        this.energyBar.setAttribute(character.getAttribute(EssentialAttributes.ENERGY));
        this.motivationBar.setAttribute(character.getAttribute(EssentialAttributes.MOTIVATION));
        this.characterConditionsPanel.setCharacter(character);
        character.addListener(this);
        this.character = character;
        this.updateAttributeDisplay();
        this.characterConditionsPanel.update();
        this.validate();
        this.repaint();
    }

    public void updateAttributeDisplay() {
        if (this.character != null) {
            List<Object> attributeList;
            if (this.character.getSpecializations().size() > 0) {
                attributeList = this.character.getSpecializations().iterator().next().getAssociatedAttributes();
            } else {
                attributeList = new ArrayList<BaseAttributeTypes>();
                attributeList.add(BaseAttributeTypes.CHARISMA);
                attributeList.add(BaseAttributeTypes.INTELLIGENCE);
                attributeList.add(BaseAttributeTypes.STAMINA);
                attributeList.add(BaseAttributeTypes.STRENGTH);
            }
            for (int i = 0; i < attributeList.size() && i < this.attributePanels.size(); ++i) {
                IconAttributePanel attributePanel = this.attributePanels.get(i);
                attributePanel.setAttribute(this.character.getAttribute((AttributeType)attributeList.get(i)));
            }
            this.updateFontSize();
        }
    }

    public void updateFontSize() {
        Font font = this.getFont();
        int maxFontSize = 16;
        for (IconAttributePanel attributePanel : this.attributePanels) {
            int curMaxFontSize = attributePanel.getMaxFittingFontSize(font);
            if (curMaxFontSize >= maxFontSize) continue;
            maxFontSize = curMaxFontSize;
        }
        if (maxFontSize > 14) {
            maxFontSize = 14;
        }
        font = font.deriveFont((float)maxFontSize);
        for (IconAttributePanel attributePanel : this.attributePanels) {
            attributePanel.setFont(font);
        }
        this.repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        RPGView view = Jasbro.getInstance().getGui();
        if (view != null) {
            int heightGui = view.getHeight();
            return new Dimension(heightGui / 7, heightGui / 3);
        }
        return super.getPreferredSize();
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.STATUSCHANGE) {
            this.setCharacter(this.character);
        }
    }
}

