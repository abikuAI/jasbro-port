/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.town;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.Jasbro;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.gui.objects.div.InventoryPanel;
import jasbro.gui.objects.div.MyImage;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;
import jasbro.util.Settings;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatter;

public class ShopMenu
extends MyImage {
    private InventoryPanel playerInventoryPanel;
    private InventoryPanel shopInventoryPanel;
    private JButton sellButton;
    private JButton buyButton;
    private JPanel sellPanel;
    private JPanel buyPanel;
    private JSpinner sellSpinner;
    private JSpinner buySpinner;
    private Inventory playerInventory;
    private Inventory shopInventory;
    private ItemLocation shop;

    public ShopMenu(ItemLocation shop) {
        this.shop = shop;
        this.setOpaque(false);
        this.setBackgroundImage(this.getShopImage());
        double width = ConfigHandler.getResolution(Settings.RESOLUTIONWIDTH);
        double height = ConfigHandler.getResolution(Settings.RESOLUTIONHEIGHT);
        int backHomeBtnWidth = (int)(150.0 * width / 1280.0);
        int backHomeBtnHeight = (int)(50.0 * width / 1280.0);
        ImageIcon homeIcon1 = new ImageIcon("images/buttons/home.png");
        Image homeImage1 = homeIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        homeIcon1 = new ImageIcon(homeImage1);
        ImageIcon homeIcon2 = new ImageIcon("images/buttons/home hover.png");
        Image homeImage2 = homeIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        homeIcon2 = new ImageIcon(homeImage2);
        JButton homeButton = new JButton(homeIcon1);
        homeButton.setRolloverIcon(homeIcon2);
        homeButton.setPressedIcon(homeIcon1);
        homeButton.setBounds((int)(15.0 * width / 1280.0), (int)(550.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setFocusPainted(false);
        homeButton.setOpaque(false);
        this.add(homeButton);
        homeButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Jasbro.getInstance().getGui().showHouseManagementScreen();
            }
        });
        ImageIcon backIcon1 = new ImageIcon("images/buttons/back.png");
        Image backImage1 = backIcon1.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        backIcon1 = new ImageIcon(backImage1);
        ImageIcon backIcon2 = new ImageIcon("images/buttons/back hover.png");
        Image backImage2 = backIcon2.getImage().getScaledInstance(backHomeBtnWidth, backHomeBtnHeight, 4);
        backIcon2 = new ImageIcon(backImage2);
        JButton backButton = new JButton(backIcon1);
        backButton.setRolloverIcon(backIcon2);
        backButton.setPressedIcon(backIcon1);
        backButton.setBounds((int)(15.0 * width / 1280.0), (int)(620.0 * height / 720.0), backHomeBtnWidth, backHomeBtnHeight);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setOpaque(false);
        this.add(backButton);
        backButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ShopMenu.this.getShop() == ItemLocation.ARCHITECT) {
                    Jasbro.getInstance().getGui().showBuildersGuildScreen();
                } else if (ShopMenu.this.getShop() == ItemLocation.APOTHECARY) {
                    Jasbro.getInstance().getGui().showAlchemist();
                } else if (ShopMenu.this.getShop() == ItemLocation.CLOTHINGSTORE || ShopMenu.this.getShop() == ItemLocation.ADVENTURERSSHOP || ShopMenu.this.getShop() == ItemLocation.BLACKMARKET) {
                    Jasbro.getInstance().getGui().showGeneralMarketScreen(2);
                } else {
                    Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
                }
            }
        });
        this.playerInventory = Jasbro.getInstance().getData().getInventory();
        this.shopInventory = Jasbro.getInstance().getData().getShop().getInventory(shop);
        FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("150dlu"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("150dlu"), ColumnSpec.decode("default:grow")}, new RowSpec[]{RowSpec.decode("50dlu"), RowSpec.decode("default:grow(3)"), RowSpec.decode("50dlu")});
        this.setLayout(layout);
        this.playerInventoryPanel = new InventoryPanel(this.playerInventory);
        this.add((Component)this.playerInventoryPanel, "2, 2, fill, fill");
        this.playerInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (ShopMenu.this.playerInventoryPanel.getSelectedItem() != null) {
                    int value = (Integer)ShopMenu.this.sellSpinner.getValue();
                    Integer itemAmount = new Integer(ShopMenu.this.playerInventory.getAmount(ShopMenu.this.playerInventoryPanel.getSelectedItem()));
                    ShopMenu.this.sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), itemAmount, new Integer(1)));
                    ShopMenu.this.sellSpinner.setValue(value);
                    ShopMenu.this.sellSpinner.setValue(Math.min(value, itemAmount));
                    JComponent comp = ShopMenu.this.sellSpinner.getEditor();
                    JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
                    DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
                    formatter.setCommitsOnValidEdit(true);
                    ShopMenu.this.updateSellButton();
                }
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (ShopMenu.this.getShop() == ItemLocation.ARCHITECT) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        Jasbro.getInstance().getGui().showBuildersGuildScreen();
                    }
                } else if (ShopMenu.this.getShop() == ItemLocation.APOTHECARY) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        Jasbro.getInstance().getGui().showAlchemist();
                    }
                } else if (ShopMenu.this.getShop() == ItemLocation.CLOTHINGSTORE || ShopMenu.this.getShop() == ItemLocation.ADVENTURERSSHOP || ShopMenu.this.getShop() == ItemLocation.BLACKMARKET) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        Jasbro.getInstance().getGui().showGeneralMarketScreen(2);
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    Jasbro.getInstance().getGui().showGeneralMarketScreen(1);
                }
            }
        });
        this.shopInventoryPanel = new InventoryPanel(this.shopInventory);
        this.add((Component)this.shopInventoryPanel, "4, 2, fill, fill");
        this.shopInventoryPanel.getItemList().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (ShopMenu.this.shopInventoryPanel.getSelectedItem() != null) {
                    int value = (Integer)ShopMenu.this.buySpinner.getValue();
                    Integer itemAmount = ShopMenu.this.shopInventory.getAmount(ShopMenu.this.shopInventoryPanel.getSelectedItem());
                    ShopMenu.this.buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), itemAmount, new Integer(1)));
                    ShopMenu.this.buySpinner.setValue(Math.min(value, itemAmount));
                    JComponent comp = ShopMenu.this.buySpinner.getEditor();
                    JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
                    DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
                    formatter.setCommitsOnValidEdit(true);
                    ShopMenu.this.updateBuyButton();
                }
            }
        });
        this.sellPanel = new JPanel();
        this.sellPanel.setOpaque(false);
        this.add((Component)this.sellPanel, "2, 3, fill, fill");
        this.sellPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("right:default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:default:grow")}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"), RowSpec.decode("default:grow")}));
        this.sellSpinner = new JSpinner();
        this.sellSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        this.sellSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                ShopMenu.this.updateSellButton();
            }
        });
        this.sellPanel.add((Component)this.sellSpinner, "1, 2");
        this.sellButton = new JButton("Sell");
        this.sellPanel.add((Component)this.sellButton, "3, 2, left, top");
        this.sellButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Item item = ShopMenu.this.playerInventoryPanel.getSelectedItem();
                if (item != null) {
                    int amount = (Integer)ShopMenu.this.sellSpinner.getValue();
                    if (ShopMenu.this.playerInventory.getAmount(item) >= amount) {
                        Jasbro.getInstance().getData().earnMoney(item.getValue() / 2 * amount, item.getName());
                        ShopMenu.this.playerInventory.removeItems(item, amount);
                        ShopMenu.this.shopInventory.addItems(item, amount);
                        ShopMenu.this.updateLists();
                    }
                }
            }
        });
        this.sellButton.setEnabled(false);
        this.buyPanel = new JPanel();
        this.buyPanel.setOpaque(false);
        this.add((Component)this.buyPanel, "4, 3, fill, fill");
        this.buyPanel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("right:default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("left:default:grow")}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("fill:default"), RowSpec.decode("default:grow")}));
        this.buySpinner = new JSpinner();
        this.buySpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        this.buySpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                ShopMenu.this.updateBuyButton();
            }
        });
        this.buyPanel.add((Component)this.buySpinner, "1, 2");
        this.buyButton = new JButton("Buy");
        this.buyPanel.add((Component)this.buyButton, "3, 2, left, top");
        this.buyButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Item item = ShopMenu.this.shopInventoryPanel.getSelectedItem();
                int amount = (Integer)ShopMenu.this.buySpinner.getValue();
                if (ShopMenu.this.shopInventory.getAmount(item) >= amount && Jasbro.getInstance().getData().getMoney() >= (long)(amount * item.getValue())) {
                    Jasbro.getInstance().getData().spendMoney(item.getValue() * amount, item.getName());
                    ShopMenu.this.shopInventory.removeItems(item, amount);
                    ShopMenu.this.playerInventory.addItems(item, amount);
                    ShopMenu.this.updateLists();
                }
            }
        });
        this.buyButton.setEnabled(false);
        MouseAdapter mouseAdapter = new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    JList<Inventory.ItemData> itemList = ShopMenu.this.playerInventoryPanel.getItemList();
                    int index = itemList.locationToIndex(e.getPoint());
                    itemList.setSelectedIndex(index);
                    ShopMenu.this.sellButton.doClick();
                }
            }
        };
        this.playerInventoryPanel.getItemList().addMouseListener(mouseAdapter);
        MouseAdapter mouseAdapter2 = new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    JList<Inventory.ItemData> itemList = ShopMenu.this.shopInventoryPanel.getItemList();
                    int index = itemList.locationToIndex(e.getPoint());
                    itemList.setSelectedIndex(index);
                    ShopMenu.this.buyButton.doClick();
                }
            }
        };
        this.shopInventoryPanel.getItemList().addMouseListener(mouseAdapter2);
    }

    public void updateLists() {
        this.playerInventoryPanel.updateView();
        this.shopInventoryPanel.updateView();
    }

    public void updateBuyButton() {
        Item item = this.shopInventoryPanel.getSelectedItem();
        int amount = (Integer)this.buySpinner.getValue();
        if (item != null) {
            this.buyButton.setText("Buy (" + this.shopInventoryPanel.getSelectedItem().getValue() * amount + ")");
            if ((long)(item.getValue() * amount) <= Jasbro.getInstance().getData().getMoney()) {
                this.buyButton.setEnabled(true);
            } else {
                this.buyButton.setEnabled(false);
            }
        } else {
            this.buyButton.setText("Buy");
            this.buyButton.setEnabled(false);
        }
        this.repaint();
    }

    public void updateSellButton() {
        Item item = this.playerInventoryPanel.getSelectedItem();
        if (item != null) {
            if (item.getValue() != 0) {
                int amount = (Integer)this.sellSpinner.getValue();
                this.sellButton.setText("Sell (" + item.getValue() / 2 * amount + ")");
                this.sellButton.setEnabled(true);
            }
        } else {
            this.sellButton.setText("Sell");
            this.sellButton.setEnabled(false);
        }
        this.repaint();
    }

    public ImageData getShopImage() {
        if (this.shop == ItemLocation.ARCHITECT) {
            return new ImageData("images/backgrounds/architect.jpg");
        }
        if (this.shop == ItemLocation.CLOTHINGSTORE) {
            return new ImageData("images/backgrounds/clothingStore.jpg");
        }
        if (this.shop == ItemLocation.ADVENTURERSSHOP) {
            return new ImageData("images/backgrounds/adventurerstore.jpg");
        }
        if (this.shop == ItemLocation.APOTHECARY) {
            return new ImageData("images/backgrounds/apothecary.jpg");
        }
        if (this.shop == ItemLocation.ADULTSTORE) {
            return new ImageData("images/backgrounds/adultshop.jpg");
        }
        if (this.shop == ItemLocation.TRAVELLINGMERCHANT) {
            return new ImageData("images/backgrounds/travellingMerchant.jpg");
        }
        if (this.shop == ItemLocation.BLACKMARKET) {
            return new ImageData("images/backgrounds/blackMarket.jpg");
        }
        if (this.shop == ItemLocation.BOOKSTORE) {
            return new ImageData("images/backgrounds/bookstore.jpg");
        }
        return new ImageData("images/backgrounds/shop.jpg");
    }

    public ItemLocation getShop() {
        return this.shop;
    }
}

