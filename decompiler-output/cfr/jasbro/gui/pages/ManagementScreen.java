/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.warnings.Severity;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.Time;
import jasbro.game.world.Unlocks;
import jasbro.game.world.locations.DivLocations;
import jasbro.game.world.locations.DungeonLocations;
import jasbro.game.world.locations.LocationType;
import jasbro.gui.CharacterFilterListModel;
import jasbro.gui.dnd.MyCharacterTransferHandler;
import jasbro.gui.dnd.ToleranceMouseListener;
import jasbro.gui.objects.div.HouseInfoPanel;
import jasbro.gui.objects.div.MyButton;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.objects.menus.FilterMenu;
import jasbro.gui.pages.subView.AreaPanel;
import jasbro.gui.pages.subView.CharacterShortView;
import jasbro.gui.pictures.ImageData;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import jasbro.util.UserHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ManagementScreen
extends JPanel
implements MyEventListener {
    private AreaPanel areaPanel;
    private JList<Charakter> characters;
    private JPanel westernPanel;
    private CharacterShortView selectedCharacter;
    private JPanel buttonPanel;
    private JButton cityButton;
    private JButton nextShiftButton;
    private Box characterListPanel;
    private JPanel easternPanel;
    private JComboBox<AreaInterface> areaSelection;
    private static AreaInterface lastSelectedLocation;
    private JPanel topPanel;
    private JComboBox<String> helpOperations;
    private HouseInfoPanel infoPanel;
    private CharacterFilterListModel filteredModel;
    private JButton lastMessageButton;
    private JPanel filterPanel;
    private MyButton filterButton;
    private MyButton resetFilterButton;
    private MyButton moveUpButton;
    private MyButton moveDownButton;

    public ManagementScreen() {
        Jasbro.getInstance().getGui().updateStatus();
        this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow(2)"), ColumnSpec.decode("8dlu:grow(13)")}, new RowSpec[]{RowSpec.decode("1dlu:grow")}));
        this.westernPanel = new JPanel();
        this.westernPanel.setBackground(Color.WHITE);
        this.add((Component)this.westernPanel, "1, 1, fill, fill");
        this.westernPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{RowSpec.decode("1dlu:grow(6)"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("1dlu:grow(10)"), FormFactory.DEFAULT_ROWSPEC}));
        this.filterPanel = new JPanel();
        this.westernPanel.add((Component)this.filterPanel, "1, 3, fill, fill");
        this.filteredModel = Jasbro.getInstance().getGui().getFilteredModel();
        final String searchTerm = TextUtil.t("ui.search");
        final JTextField searchField = new JTextField(searchTerm);
        if (this.filteredModel.getFilter().getSearchString() != null && !this.filteredModel.getFilter().getSearchString().equals("")) {
            searchField.setText(this.filteredModel.getFilter().getSearchString());
        }
        this.filterPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("5dlu"), ColumnSpec.decode("5dlu"), ColumnSpec.decode("10dlu"), ColumnSpec.decode("10dlu")}, new RowSpec[]{RowSpec.decode("default:grow")}));
        this.filterPanel.add((Component)searchField, "1, 1");
        searchField.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(searchTerm)) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(searchTerm);
                }
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!searchField.getText().equals(searchTerm)) {
                    ManagementScreen.this.filteredModel.getFilter().setSearchString(searchField.getText());
                    ManagementScreen.this.filteredModel.filter();
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.changedUpdate(e);
            }
        });
        this.moveUpButton = new MyButton("", new ImageData("images/icons/arrow_up.png"), new ImageData("images/icons/arrow_up.png"));
        this.filterPanel.add((Component)this.moveUpButton, "2, 1, fill, fill");
        this.moveUpButton.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                GameData data;
                int index;
                Charakter selectedChar = (Charakter)ManagementScreen.this.characters.getSelectedValue();
                if (selectedChar != null && (index = (data = Jasbro.getInstance().getData()).getCharacters().indexOf(selectedChar)) > 0) {
                    data.getCharacters().remove(selectedChar);
                    data.getCharacters().add(index - 1, selectedChar);
                    ManagementScreen.this.filteredModel.filter();
                    ManagementScreen.this.characters.setSelectedValue(selectedChar, true);
                }
            }
        });
        this.moveDownButton = new MyButton("", new ImageData("images/icons/arrow_down.png"), new ImageData("images/icons/arrow_down.png"));
        this.filterPanel.add((Component)this.moveDownButton, "3, 1, fill, fill");
        this.moveDownButton.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                GameData data;
                int index;
                Charakter selectedChar = (Charakter)ManagementScreen.this.characters.getSelectedValue();
                if (selectedChar != null && (index = (data = Jasbro.getInstance().getData()).getCharacters().indexOf(selectedChar)) > -1 && index < data.getCharacters().size() - 1) {
                    data.getCharacters().remove(selectedChar);
                    data.getCharacters().add(index + 1, selectedChar);
                    ManagementScreen.this.filteredModel.filter();
                    ManagementScreen.this.characters.setSelectedValue(selectedChar, true);
                }
            }
        });
        this.filterButton = new MyButton("", new ImageData("images/icons/filter.png"), new ImageData("images/icons/filter.png"));
        this.filterPanel.add((Component)this.filterButton, "4, 1, fill, fill");
        this.filterButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                FilterMenu filterMenu = new FilterMenu();
                if (JOptionPane.showConfirmDialog(Jasbro.getInstance().getGui(), filterMenu, "Filter", 2) == 0) {
                    ManagementScreen.this.filteredModel.setFilter(filterMenu.getFilter());
                    searchField.setText(ManagementScreen.this.filteredModel.getFilter().getSearchString());
                    searchField.repaint();
                }
            }
        });
        this.resetFilterButton = new MyButton("", new ImageData("images/icons/x.png"), new ImageData("images/icons/x.png"));
        this.filterPanel.add((Component)this.resetFilterButton, "5, 1, fill, fill");
        this.resetFilterButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                ManagementScreen.this.filteredModel.setFilter(new CharacterFilterListModel.Filter());
                searchField.setText("");
            }
        });
        this.characterListPanel = new Box(3);
        this.westernPanel.add((Component)this.characterListPanel, "1, 4, fill, fill");
        JScrollPane scrollPane = new JScrollPane();
        this.characterListPanel.add(scrollPane);
        this.characters = new JList<Charakter>(this.filteredModel);
        this.characters.setSelectionMode(0);
        this.characters.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (ManagementScreen.this.selectedCharacter == null || ManagementScreen.this.characters.getSelectedValue() != ManagementScreen.this.selectedCharacter.getCharacter()) {
                    if (ManagementScreen.this.selectedCharacter != null) {
                        ManagementScreen.this.westernPanel.remove(ManagementScreen.this.selectedCharacter);
                    }
                    ManagementScreen.this.selectedCharacter = new CharacterShortView((Charakter)ManagementScreen.this.characters.getSelectedValue());
                    ManagementScreen.this.westernPanel.add((Component)ManagementScreen.this.selectedCharacter, "1 , 1, center, top");
                    ManagementScreen.this.westernPanel.validate();
                    ManagementScreen.this.selectedCharacter.setTransferHandler(new MyCharacterTransferHandler());
                    ToleranceMouseListener tml = new ToleranceMouseListener(){

                        @Override
                        public void mouseDragged(MouseEvent e) {
                            super.mouseDragged(e);
                            if (!e.isConsumed()) {
                                TransferHandler handle = ManagementScreen.this.selectedCharacter.getTransferHandler();
                                handle.exportAsDrag(ManagementScreen.this.selectedCharacter, e, 0x40000000);
                            }
                        }
                    };
                    ManagementScreen.this.selectedCharacter.addMouseMotionListener(tml);
                    ManagementScreen.this.selectedCharacter.addMouseListener(tml);
                }
            }
        });
        this.characters.setModel(this.filteredModel);
        for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
            character.addListener(this);
        }
        scrollPane.setViewportView(this.characters);
        this.characters.setCellRenderer(new MyListCellRenderer());
        this.characters.setTransferHandler(new MyCharacterTransferHandler());
        ToleranceMouseListener tml = new ToleranceMouseListener(){
            private CharacterShortView initiallySelectedCharacter;

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                this.initiallySelectedCharacter = ManagementScreen.this.selectedCharacter;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (this.initiallySelectedCharacter != null && !e.isConsumed()) {
                    ManagementScreen.this.characters.setAutoscrolls(false);
                    TransferHandler handle = ManagementScreen.this.characters.getTransferHandler();
                    handle.exportAsDrag(this.initiallySelectedCharacter, e, 0x40000000);
                    ManagementScreen.this.characters.setAutoscrolls(true);
                }
            }
        };
        this.characters.addMouseListener(tml);
        this.characters.addMouseMotionListener(tml);
        this.buttonPanel = new JPanel();
        this.buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.westernPanel.add((Component)this.buttonPanel, "1, 5, fill, fill");
        this.buttonPanel.setLayout(new GridLayout(0, 1, 3, 5));
        this.cityButton = new JButton(TextUtil.t("ui.town"));
        this.cityButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showTownScreen();
            }
        });
        if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
            this.cityButton.setEnabled(false);
        }
        this.buttonPanel.add(this.cityButton);
        JButton unlockButton = new JButton(TextUtil.t("ui.unlocks"));
        unlockButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showUnlockScreen();
            }
        });
        this.buttonPanel.add(unlockButton);
        JButton showStatsButton = new JButton(TextUtil.t("ui.showStats"));
        showStatsButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getThreadpool().execute(new Runnable(){

                    @Override
                    public void run() {
                        StatCollector statCollector = Jasbro.getInstance().getData().getStatCollector();
                        statCollector.showStatScreen();
                    }
                });
            }
        });
        this.buttonPanel.add(showStatsButton);
        if (!Jasbro.getInstance().getData().getStatCollector().getDailyData().isInitialized()) {
            showStatsButton.setEnabled(false);
        }
        this.lastMessageButton = new JButton(TextUtil.t("ui.showLastMessage"));
        this.buttonPanel.add(this.lastMessageButton);
        this.lastMessageButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Jasbro.getInstance().getGui().restoreLastMessage();
            }
        });
        JButton nextDayButton = new JButton(TextUtil.t("ui.nextDay"));
        nextDayButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getThreadpool().execute(new Runnable(){

                    @Override
                    public void run() {
                        Jasbro.getInstance().advanceDay();
                    }
                });
            }
        });
        this.buttonPanel.add(nextDayButton);
        if (Jasbro.getInstance().getData().getDay() == 1) {
            nextDayButton.setEnabled(false);
        }
        this.nextShiftButton = new JButton(TextUtil.t("ui.nextShift"));
        this.nextShiftButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getThreadpool().execute(new Runnable(){

                    @Override
                    public void run() {
                        Jasbro.getInstance().advanceShift();
                    }
                });
            }
        });
        this.buttonPanel.add(this.nextShiftButton);
        this.easternPanel = new JPanel();
        this.easternPanel.setBackground(Color.WHITE);
        this.add((Component)this.easternPanel, "2, 1, fill, fill");
        this.easternPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow")}, new RowSpec[]{FormFactory.PREF_ROWSPEC, RowSpec.decode("1dlu:grow")}));
        this.areaPanel = new AreaPanel();
        this.easternPanel.add((Component)this.areaPanel, "1, 2, fill, fill");
        this.topPanel = new JPanel();
        this.topPanel.setBackground(Color.WHITE);
        this.easternPanel.add((Component)this.topPanel, "1, 1, fill, fill");
        this.topPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("1dlu:grow(4)"), ColumnSpec.decode("1dlu:grow(2)"), ColumnSpec.decode("1dlu:grow(1)"), ColumnSpec.decode("1dlu:grow(1)"), ColumnSpec.decode("1dlu:grow(1)"), ColumnSpec.decode("1dlu:grow(4)")}, new RowSpec[]{RowSpec.decode("15dlu:grow")}));
        this.helpOperations = new JComboBox();
        this.topPanel.add(this.helpOperations, "1, 1, center, fill");
        this.helpOperations.addItem("--- Assistance functions ---");
        final ArrayList<UserHelper.HelpOption> optionList = new ArrayList<UserHelper.HelpOption>();
        Time time = Jasbro.getInstance().getData().getTime();
        if (time != Time.MORNING) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYMORNINGSHIFT.getText());
            optionList.add(UserHelper.HelpOption.COPYMORNINGSHIFT);
        }
        if (time != Time.AFTERNOON) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYAFTERNOONSHIFT.getText());
            optionList.add(UserHelper.HelpOption.COPYAFTERNOONSHIFT);
        }
        if (time != Time.NIGHT) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYNIGHTSHIFT.getText());
            optionList.add(UserHelper.HelpOption.COPYNIGHTSHIFT);
        }
        this.helpOperations.addItem(UserHelper.HelpOption.REMOVEALL.getText());
        optionList.add(UserHelper.HelpOption.REMOVEALL);
        if (time != Time.MORNING) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYMORNINGSHIFTEVERYWHERE.getText());
            optionList.add(UserHelper.HelpOption.COPYMORNINGSHIFTEVERYWHERE);
        }
        if (time != Time.AFTERNOON) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYAFTERNOONSHIFTEVERYWHERE.getText());
            optionList.add(UserHelper.HelpOption.COPYAFTERNOONSHIFTEVERYWHERE);
        }
        if (time != Time.NIGHT) {
            this.helpOperations.addItem(UserHelper.HelpOption.COPYNIGHTSHIFTEVERYWHERE.getText());
            optionList.add(UserHelper.HelpOption.COPYNIGHTSHIFTEVERYWHERE);
        }
        this.helpOperations.addItem(UserHelper.HelpOption.REMOVEALLEVERYWHERE.getText());
        optionList.add(UserHelper.HelpOption.REMOVEALLEVERYWHERE);
        this.helpOperations.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ManagementScreen.this.helpOperations.getSelectedIndex() > 0) {
                    UserHelper.HelpOption selectedOption = (UserHelper.HelpOption)((Object)optionList.get(ManagementScreen.this.helpOperations.getSelectedIndex() - 1));
                    new UserHelper().perform(ManagementScreen.this.areaPanel.getArea(), selectedOption);
                    ManagementScreen.this.helpOperations.setSelectedIndex(0);
                    try {
                        Thread.sleep(50L);
                    }
                    catch (InterruptedException e1) {
                        // empty catch block
                    }
                    AreaInterface area = (AreaInterface)ManagementScreen.this.areaSelection.getSelectedItem();
                    ManagementScreen.this.areaPanel.setArea(area);
                    ManagementScreen.this.validate();
                    ManagementScreen.this.repaint();
                }
            }
        });
        this.areaSelection = new JComboBox();
        this.topPanel.add(this.areaSelection, "2, 1, fill, fill");
        this.areaSelection.setBorder(new EmptyBorder(1, 0, 1, 0));
        this.areaSelection.setRenderer(new DefaultListCellRenderer(){

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
                JLabel label = (JLabel)super.getListCellRendererComponent((JList<?>)list, value, index, isSelected, hasFocus);
                label.setText(((AreaInterface)value).getName());
                return label;
            }
        });
        final JButton manageHouseButton = new JButton(TextUtil.t("ui.manage"));
        manageHouseButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ManagementScreen.this.areaPanel.getArea() instanceof House) {
                    House house = (House)ManagementScreen.this.areaPanel.getArea();
                    Jasbro.getInstance().getGui().showHouseScreen(house);
                }
            }
        });
        this.topPanel.add((Component)manageHouseButton, "4, 1, fill, fill");
        JButton questButton = new JButton(TextUtil.t("ui.quests"));
        questButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ManagementScreen.this.areaPanel.getArea() instanceof House) {
                    House house = (House)ManagementScreen.this.areaPanel.getArea();
                    Jasbro.getInstance().getGui().showQuestsScreen();
                }
            }
        });
        this.topPanel.add((Component)questButton, "5, 1, fill, fill");
        this.infoPanel = new HouseInfoPanel();
        this.topPanel.add((Component)this.infoPanel, "6, 1");
        if (Jasbro.getInstance().getData() != null) {
            for (House house : Jasbro.getInstance().getData().getHouses()) {
                this.areaSelection.addItem(house);
            }
            this.areaSelection.addItem(new DivLocations());
            Unlocks unlocks = Jasbro.getInstance().getData().getUnlocks();
            if (unlocks.isUnlocked(LocationType.DUNGEON1) || unlocks.isUnlocked(LocationType.DUNGEON2) || unlocks.isUnlocked(LocationType.DUNGEON3) || unlocks.isUnlocked(LocationType.DUNGEON4)) {
                this.areaSelection.addItem(new DungeonLocations());
            }
            if (lastSelectedLocation == null || !Jasbro.getInstance().getData().getHouses().contains(lastSelectedLocation)) {
                List<House> houses = Jasbro.getInstance().getData().getHouses();
                this.areaPanel.setArea(houses.get(0));
            } else {
                this.areaPanel.setArea(lastSelectedLocation);
                this.areaSelection.setSelectedItem(lastSelectedLocation);
            }
        }
        this.areaSelection.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    AreaInterface area = (AreaInterface)ManagementScreen.this.areaSelection.getSelectedItem();
                    ManagementScreen.this.areaPanel.setArea(area);
                    lastSelectedLocation = area;
                    if (area instanceof House) {
                        manageHouseButton.setVisible(true);
                    } else {
                        manageHouseButton.setVisible(false);
                    }
                    ManagementScreen.this.infoPanel.refresh(ManagementScreen.this.areaPanel.getArea());
                    ManagementScreen.this.validate();
                    ManagementScreen.this.repaint();
                }
            }
        });
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentShown(ComponentEvent e) {
                ManagementScreen.this.updateListModel();
                ManagementScreen.this.repaint();
            }
        });
        this.infoPanel.refresh(this.areaPanel.getArea());
        this.filteredModel.filter();
        this.characters.setSelectedIndex(0);
        this.validate();
        this.repaint();
    }

    public AreaPanel getHousePanel() {
        return this.areaPanel;
    }

    @Override
    public void handleEvent(MyEvent e) {
        this.characterListPanel.repaint();
    }

    public void updateListModel() {
        this.filteredModel.filter();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            this.lastMessageButton.setEnabled(Jasbro.getInstance().getGui().hasPreviousMessages());
        }
    }

    private class MyListCellRenderer
    extends DefaultListCellRenderer {
        private Map<Charakter, JPanel> panelMap = new HashMap<Charakter, JPanel>();

        private MyListCellRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            JPanel panel;
            Charakter character = (Charakter)value;
            if (this.panelMap.containsKey(character)) {
                panel = this.panelMap.get(character);
                JLabel nameLabel = (JLabel)panel.getComponent(1);
                if (!character.getName().equals(nameLabel.getText())) {
                    if (character.getUnspentPerkPoints() != 0) {
                        nameLabel.setText(character.getName() + " (" + character.getUnspentPerkPoints() + ")");
                    } else {
                        nameLabel.setText(character.getName());
                    }
                }
            } else {
                panel = new JPanel();
                panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("10dlu:none"), ColumnSpec.decode("pref:grow(4)")}, new RowSpec[]{RowSpec.decode("pref:grow")}));
                MyImage idleImage = new MyImage();
                idleImage.setOpaque(false);
                panel.add((Component)idleImage, "1, 1, fill, fill");
                panel.setToolTipText(TextUtil.htmlPreformatted(character.getWarnString()));
                JLabel label = new JLabel();
                if (character.getUnspentPerkPoints() != 0) {
                    label.setText(character.getName() + " (" + character.getUnspentPerkPoints() + ")");
                } else {
                    label.setText(character.getName());
                }
                panel.add((Component)label, "2, 1, fill, fill");
                this.panelMap.put(character, panel);
            }
            if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
            }
            Border border = null;
            if (hasFocus) {
                if (isSelected) {
                    border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getBorder("List.focusCellHighlightBorder");
                }
            } else {
                border = new EmptyBorder(1, 1, 1, 1);
            }
            panel.setBorder(border);
            if (character.getWarnLevel() == Severity.DANGER || character.getWarnLevel() == Severity.WARN) {
                panel.getComponent(0).setVisible(true);
                ((MyImage)panel.getComponent(0)).setImage(character.getWarnImage());
            } else {
                panel.getComponent(0).setVisible(false);
            }
            return panel;
        }
    }
}

