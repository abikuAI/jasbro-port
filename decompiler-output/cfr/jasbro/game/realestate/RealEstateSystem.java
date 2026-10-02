/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.realestate;

import jasbro.game.GameData;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;
import jasbro.game.realestate.Plot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealEstateSystem {
    private static final transient Logger LOG = LogManager.getLogger(RealEstateSystem.class);
    private final Map<String, Plot> plots = new HashMap<String, Plot>();
    private final List<Plot> ownedPlots = new ArrayList<Plot>();
    private final List<Plot> freePlots = new ArrayList<Plot>();

    public RealEstateSystem() {
        this.plots.put("starting-plot", new Plot("starting-plot", 4, 0, 0, HouseUtil.newHouse(HouseType.HUT)));
        this.ownedPlots.add(this.plots.get("starting-plot"));
    }

    public Plot createPlot(String id, int maxSize, int cost, int quality, House house) {
        Validate.notBlank((CharSequence)id, (String)"Attempted to create a plot with a blank ID", (Object[])new Object[0]);
        if (this.plots.containsKey(id)) {
            LOG.error("Attempted to create plot with duplicate id '%s'", new Object[]{id});
            return null;
        }
        Plot p = new Plot(id, maxSize, cost, quality, house);
        this.plots.put(id, p);
        return p;
    }

    public Plot createPlot(String id, int maxSize, int cost, int quality) {
        return this.createPlot(id, maxSize, cost, quality, null);
    }

    public Collection<Plot> getOwnedPlots() {
        return Collections.unmodifiableList(this.ownedPlots);
    }

    public Collection<Plot> getFreePlots() {
        return Collections.unmodifiableList(this.freePlots);
    }

    public Collection<Plot> getAllPlots() {
        return Collections.unmodifiableCollection(this.plots.values());
    }

    public Plot getPlot(String id) {
        return this.plots.get(id);
    }

    public Plot getOwnedPlot(String id) {
        Plot p = this.plots.get(id);
        if (this.ownedPlots.contains(p)) {
            return p;
        }
        return null;
    }

    public Plot getFreePlot(String id) {
        Plot p = this.plots.get(id);
        if (this.freePlots.contains(p)) {
            return p;
        }
        return null;
    }

    public void buyPlot(String id, GameData data) {
        Plot plot = this.getRequiredFreePlot(id);
        if (data.canAfford(plot.getCost())) {
            data.spendMoney(plot.getCost(), plot);
            this.ownedPlots.add(plot);
            this.freePlots.remove(plot);
        } else {
            LOG.warn("Cannot afford plot {}", new Object[]{id});
        }
    }

    public void sellPlot(String id, GameData data) {
        Plot plot = this.getRequiredOwnedPlot(id);
        data.earnMoney(plot.getCost(), plot);
        this.ownedPlots.remove(plot);
        this.freePlots.add(plot);
    }

    public void buildHouse(String plotId, House house, GameData data) {
        Plot plot = this.getOwnedPlot(plotId);
        if (this.canPlaceHouse(plotId, house)) {
            plot.setHouse(house);
        } else {
            LOG.warn("Can't place house '%s' on plot '%s'. This probably shouldn't have been reached.", new Object[]{house.getHouseType(), plotId});
        }
    }

    public void demolishHouse(String plotId, GameData data) {
    }

    private Plot getRequiredPlot(String id) {
        Plot p = this.plots.get(id);
        Validate.notNull((Object)p, (String)"Given plot ID '%s' does not exist", (Object[])new Object[]{id});
        return p;
    }

    private Plot getRequiredOwnedPlot(String id) {
        Plot p = this.getRequiredPlot(id);
        Validate.isTrue((boolean)this.ownedPlots.contains(p), (String)"Given ID '%s' does not match an owned plot of land", (Object[])new Object[]{id});
        return p;
    }

    private Plot getRequiredFreePlot(String id) {
        Plot p = this.getRequiredPlot(id);
        Validate.isTrue((boolean)this.freePlots.contains(p), (String)"Given ID '%s' does not match a free plot of land", (Object[])new Object[]{id});
        return p;
    }

    private boolean canPlaceHouse(String plotId, House house) {
        return this.getOwnedPlot(plotId).getMaxSize() >= house.getRoomAmount();
    }
}

