/*
 * Decompiled with CFR 0.152.
 */
package jasbro.gui.pages.subView;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import jasbro.game.events.business.Satisfaction;
import jasbro.gui.DelegateMouseListener;
import jasbro.gui.GuiUtil;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

public class StatPanel
extends JPanel {
    public StatPanel(StatCollector.ShiftData shiftData) {
        this.setOpaque(false);
        FormLayout layout = new FormLayout(new ColumnSpec[]{FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("center:min:grow(7)"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("center:min:grow(2)"), FormFactory.RELATED_GAP_COLSPEC}, new RowSpec[]{FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC});
        this.setLayout(layout);
        layout.setColumnGroups(new int[][]{{2, 4}});
        JPanel column1Panel = new JPanel();
        column1Panel.addMouseListener(new DelegateMouseListener());
        column1Panel.setOpaque(false);
        this.add((Component)column1Panel, "2, 2, fill, fill");
        column1Panel.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(3)")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("3dlu:none"), RowSpec.decode("default:grow")}));
        JLabel lblNewLabel = new JLabel(TextUtil.t("stats.title"));
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT.deriveFont(20.0f));
        column1Panel.add((Component)lblNewLabel, "1, 1");
        Object[] arguments = new Object[1];
        lblNewLabel = new JLabel(TextUtil.t("stats.moneybefore"));
        column1Panel.add((Component)lblNewLabel, "1, 2");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        arguments[0] = shiftData.getMoneyBeforeShift();
        lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
        column1Panel.add((Component)lblNewLabel, "2, 2");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        lblNewLabel = new JLabel(TextUtil.t("stats.moneyafter"));
        column1Panel.add((Component)lblNewLabel, "1, 3");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        arguments[0] = shiftData.getMoneyAfterShift();
        lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
        column1Panel.add((Component)lblNewLabel, "2, 3");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        lblNewLabel = new JLabel(TextUtil.t("stats.change"));
        column1Panel.add((Component)lblNewLabel, "1, 4");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        arguments[0] = shiftData.getMoneyAfterShift() - shiftData.getMoneyBeforeShift();
        lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
        column1Panel.add((Component)lblNewLabel, "2, 4");
        lblNewLabel.setFont(GuiUtil.DEFAULTSMALLBOLDFONT);
        JScrollPane scrollPane = new JScrollPane();
        column1Panel.add((Component)scrollPane, "1, 6, 3, 1, fill, fill");
        scrollPane.setOpaque(false);
        scrollPane.getHorizontalScrollBar().setOpaque(false);
        scrollPane.getVerticalScrollBar().setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.addMouseListener(new DelegateMouseListener());
        scrollPane.getViewport().addMouseListener(new DelegateMouseListener());
        JPanel panel = new JPanel();
        scrollPane.add(panel);
        scrollPane.setViewportView(panel);
        scrollPane.getPreferredSize().width = 1;
        scrollPane.getViewport().getPreferredSize().width = 1;
        panel.setOpaque(false);
        layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow")});
        panel.setLayout(layout);
        panel.addMouseListener(new DelegateMouseListener());
        panel.getPreferredSize().width = 1;
        lblNewLabel = new JLabel(TextUtil.t("stats.activity"));
        panel.add((Component)lblNewLabel, "1, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        lblNewLabel = new JLabel(TextUtil.t("stats.characters", arguments));
        panel.add((Component)lblNewLabel, "2, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        lblNewLabel = new JLabel(TextUtil.t("stats.amountcustomers", arguments));
        panel.add((Component)lblNewLabel, "3, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        lblNewLabel.setHorizontalAlignment(0);
        lblNewLabel = new JLabel(TextUtil.t("stats.avgSatisfaction", arguments));
        panel.add((Component)lblNewLabel, "4, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        lblNewLabel = new JLabel(TextUtil.t("stats.amountmaincustomers", arguments));
        panel.add((Component)lblNewLabel, "5, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        lblNewLabel.setHorizontalAlignment(0);
        lblNewLabel = new JLabel(TextUtil.t("stats.avgSatisfactionMain", arguments));
        panel.add((Component)lblNewLabel, "6, 1");
        lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
        List<StatCollector.ActivityData> activityDataList = shiftData.getActivityDataList();
        for (int i = 0; i < activityDataList.size(); ++i) {
            StatCollector.ActivityData activityData = activityDataList.get(i);
            int row = i + 2;
            layout.insertRow(row, FormFactory.DEFAULT_ROWSPEC);
            lblNewLabel = new JLabel(activityData.getType().getText());
            panel.add((Component)lblNewLabel, "1, " + row);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            lblNewLabel = new JLabel(TextUtil.listCharacters(activityData.getCharacters()));
            panel.add((Component)lblNewLabel, "2, " + row);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            lblNewLabel = new JLabel("" + activityData.getAmountCustomers());
            panel.add((Component)lblNewLabel, "3, " + row);
            lblNewLabel.setHorizontalAlignment(0);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            lblNewLabel = new JLabel(" ");
            if (activityData.getAvgSatisfactionModifier() != 0) {
                lblNewLabel.setText("" + activityData.getAvgSatisfactionModifier());
            }
            panel.add((Component)lblNewLabel, "4, " + row);
            lblNewLabel.setHorizontalAlignment(0);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            lblNewLabel = new JLabel("" + activityData.getAmountMainCustomers());
            panel.add((Component)lblNewLabel, "5, " + row);
            lblNewLabel.setHorizontalAlignment(0);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            lblNewLabel = new JLabel(" ");
            if (activityData.getAvgSatisfactionMainCustomer() != 0) {
                lblNewLabel.setText("" + Satisfaction.getSatisfaction(activityData.getAvgSatisfactionMainCustomer()).getText());
            }
            panel.add((Component)lblNewLabel, "6, " + row);
            lblNewLabel.setHorizontalAlignment(0);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            arguments[0] = activityData.getIncome();
            lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
            panel.add((Component)lblNewLabel, "7, " + row);
            lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
        }
        JPanel column2Panel = new JPanel();
        this.add((Component)column2Panel, "4, 2, fill, fill");
        column2Panel.setOpaque(false);
        column2Panel.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        column2Panel.setLayout(new GridLayout(2, 1, 0, 5));
        column2Panel.add(new MoneyPanel(TextUtil.t("stats.earnings"), shiftData.getMoneyEarnedList(), shiftData));
        column2Panel.add(new MoneyPanel(TextUtil.t("stats.expenses"), shiftData.getMoneySpentList(), shiftData));
    }

    public class MoneyPanel
    extends JPanel {
        public MoneyPanel(String title, List<StatCollector.MoneyChangeData> moneyChangeDataList, StatCollector.ShiftData shiftData) {
            this.addMouseListener(new DelegateMouseListener());
            this.setOpaque(false);
            this.setLayout(new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow"), ColumnSpec.decode("default:grow(3)")}, new RowSpec[]{FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("3dlu:none"), RowSpec.decode("default:grow")}));
            JLabel lblNewLabel = new JLabel(title);
            lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT.deriveFont(20.0f));
            this.add((Component)lblNewLabel, "1, 1");
            Object[] arguments = new Object[]{shiftData.calculateSum(moneyChangeDataList)};
            lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
            this.add((Component)lblNewLabel, "2, 1");
            lblNewLabel.setFont(GuiUtil.DEFAULTBOLDFONT);
            JScrollPane scrollPane = new JScrollPane();
            this.add((Component)scrollPane, "1, 3, 3, 1, fill, fill");
            scrollPane.setOpaque(false);
            scrollPane.getHorizontalScrollBar().setOpaque(false);
            scrollPane.getVerticalScrollBar().setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.addMouseListener(new DelegateMouseListener());
            scrollPane.getViewport().addMouseListener(new DelegateMouseListener());
            JPanel panel = new JPanel();
            scrollPane.add(panel);
            scrollPane.setViewportView(panel);
            scrollPane.getPreferredSize().width = 1;
            scrollPane.getViewport().getPreferredSize().width = 1;
            panel.setOpaque(false);
            FormLayout layout = new FormLayout(new ColumnSpec[]{ColumnSpec.decode("default:grow(4)"), ColumnSpec.decode("default:grow(4)")}, new RowSpec[]{RowSpec.decode("default:grow")});
            panel.setLayout(layout);
            panel.addMouseListener(new DelegateMouseListener());
            panel.getPreferredSize().width = 1;
            for (int i = 0; i < moneyChangeDataList.size(); ++i) {
                StatCollector.MoneyChangeData moneyChangeData = moneyChangeDataList.get(i);
                int row = i + 1;
                layout.insertRow(row, FormFactory.DEFAULT_ROWSPEC);
                lblNewLabel = new JLabel(moneyChangeData.getSource());
                panel.add((Component)lblNewLabel, "1, " + row);
                lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
                arguments[0] = moneyChangeData.getAmount();
                lblNewLabel = new JLabel(TextUtil.t("stats.money", arguments));
                panel.add((Component)lblNewLabel, "2, " + row);
                lblNewLabel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));
            }
        }
    }
}

