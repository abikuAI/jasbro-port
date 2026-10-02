/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.world.market.Bidder;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Auction
extends TimerTask {
    private static final Logger log = LogManager.getLogger(Auction.class);
    private long maxBid = 0L;
    private Bidder maxBidder;
    private Charakter slave;
    private Long slaveValue;
    private AuctionGui gui;
    private boolean ownSlave = false;
    private List<Bidder> bidders;
    private Timer timer = new Timer();
    private static final int soldSeconds = 10;
    private int auctionTime = 0;
    private int remainingTime = 10;
    private String messages = "";
    private int profit;
    private boolean abort = false;

    public void startAuction() {
        GameData gameData = Jasbro.getInstance().getData();
        if (gameData.getCharacters().contains(this.slave)) {
            this.ownSlave = true;
        }
        this.printBidderParticipants(this.getBidders());
        this.addMsg("The auction of " + this.slave.getName() + " starts!");
        for (Bidder bidder : this.getBidders()) {
            try {
                bidder.setAuction(this);
                if (!(bidder instanceof Thread)) continue;
                bidder.start();
            }
            catch (Exception e) {
                log.error("Bidder not started", (Throwable)e);
            }
        }
        this.timer.scheduleAtFixedRate((TimerTask)this, 1000L, 1000L);
    }

    private void printBidderParticipants(List<Bidder> bidders) {
        String msg = "The other bidders are: ";
        for (int i = 0; i < bidders.size(); ++i) {
            msg = msg + bidders.get(i).getBidderName();
            if (i >= bidders.size() - 1) continue;
            msg = i == bidders.size() - 2 ? msg + " and " : msg + ", ";
        }
        msg = msg + ".";
        this.addMsg(msg);
    }

    public synchronized boolean bid(long amount, Bidder bidder) {
        if (bidder instanceof Bidder.UiBidder && !Jasbro.getInstance().getData().canAfford(amount)) {
            this.addMsg("You can not affort to bid " + amount + " gold");
            return false;
        }
        if (this.remainingTime > 0 && amount > this.maxBid) {
            this.maxBid = amount;
            this.maxBidder = bidder;
            this.resetTimer();
            if (bidder instanceof Bidder.UiBidder) {
                Object[] arguments = new Object[]{amount};
                this.addMsg(TextUtil.t("auction.bid", arguments));
            } else {
                Object[] arguments = new Object[]{bidder.getBidderName(), amount};
                this.addMsg(TextUtil.t("auction.bidCompetitor", arguments));
            }
            return true;
        }
        return false;
    }

    private void resetTimer() {
        this.remainingTime = 10;
        this.remainingTime -= this.auctionTime / 20;
        if (this.remainingTime < 5) {
            this.remainingTime = 5;
        }
    }

    public synchronized long getMaxBid() {
        return this.maxBid;
    }

    public synchronized Bidder getMaxBidder() {
        return this.maxBidder;
    }

    public Charakter getSlave() {
        return this.slave;
    }

    public void setSlave(Charakter slave) {
        this.slave = slave;
    }

    @Override
    public synchronized void run() {
        ++this.auctionTime;
        if (!this.abort) {
            if (this.maxBid > 0L) {
                --this.remainingTime;
            }
            if (this.remainingTime == 4) {
                this.addMsg("Going once.");
            } else if (this.remainingTime == 2) {
                this.addMsg("Going twice.");
            } else if (this.remainingTime <= 0) {
                Object[] arguments;
                this.timer.cancel();
                this.addMsg("Sold!");
                for (Bidder bidder : this.getBidders()) {
                    bidder.stopBidding();
                }
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException e) {
                    log.error("InterruptedException", (Throwable)e);
                }
                GameData gameData = Jasbro.getInstance().getData();
                this.profit = 0;
                if (this.ownSlave) {
                    this.profit = (int)Util.getPercent(this.maxBid, 80);
                    arguments = new Object[]{this.slave.getName()};
                    Jasbro.getInstance().getData().earnMoney(this.profit, TextUtil.t("auction.stats", arguments));
                }
                if (this.maxBidder instanceof Bidder.UiBidder) {
                    arguments = new Object[]{this.slave.getName()};
                    gameData.spendMoney(this.maxBid, TextUtil.t("auction.stats", arguments));
                    if (!this.ownSlave) {
                        gameData.getCharacters().add(this.slave);
                        this.slave.setOwnership(Ownership.OWNED);
                        new MessageScreen("Congratulations! You bought " + this.slave.getName() + " for " + this.maxBid + " gold.", ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.slave), this.slave.getBackground());
                        gameData.getEventManager().notifyAll(new MyEvent(EventType.CHARACTERGAINED, this.slave));
                    } else {
                        new MessageScreen("You bought back your own slave! Due to commission and taxes you lose " + (this.maxBid - (long)this.profit) + " gold.", ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.slave), this.slave.getBackground());
                    }
                } else if (!this.ownSlave) {
                    new MessageScreen(this.maxBidder.getBidderName() + " bought " + this.slave.getName() + " for " + this.maxBid + " gold.", ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.slave), this.slave.getBackground());
                } else {
                    new MessageScreen(TextUtil.t("auction.soldOwned", (Person)this.slave, this.maxBidder.getBidderName(), this.profit), ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, this.slave), this.slave.getBackground());
                    MyEvent event = new MyEvent(EventType.SLAVESOLD, this);
                    Jasbro.getInstance().getData().getEventManager().handleEvent(event);
                    Jasbro.getInstance().removeCharacter(this.slave);
                    double bonusFame = Math.round(Math.sqrt(this.profit) * 10.0 - 500.0);
                    Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(bonusFame);
                }
                this.gui.close();
            }
        } else {
            this.timer.cancel();
            for (Bidder bidder : this.getBidders()) {
                bidder.stopBidding();
            }
            this.gui.close();
        }
    }

    public void addMsg(String msg) {
        this.messages = this.messages + msg + "\n";
        this.gui.update();
    }

    public String getMessages() {
        return this.messages;
    }

    public int getAuctionTime() {
        return this.auctionTime;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void setBidders(List<Bidder> bidders) {
        this.bidders = bidders;
    }

    public AuctionGui getGui() {
        return this.gui;
    }

    public void setGui(AuctionGui gui) {
        this.gui = gui;
    }

    public List<Bidder> getBidders() {
        if (this.bidders == null) {
            this.bidders = new ArrayList<Bidder>();
            int amount = Util.getInt(3, 6);
            for (int i = 0; i < amount; ++i) {
                this.bidders.add(this.createBidder(this.bidders));
            }
        }
        return this.bidders;
    }

    public Bidder createBidder(List<Bidder> bidders) {
        String name;
        if (!this.contains(bidders, Bidder.MinimumBidder.class) && Util.getRnd().nextInt(100) < 5) {
            return new Bidder.MinimumBidder();
        }
        if (!this.contains(bidders, Bidder.ShockBidder.class) && Util.getRnd().nextInt(100) < 5) {
            return new Bidder.ShockBidder();
        }
        if (!this.contains(bidders, Bidder.PersistentBidder.class) && Util.getRnd().nextInt(100) < 5) {
            return new Bidder.PersistentBidder();
        }
        while (this.containsName(bidders, name = Bidder.BIDDERNAMES[Util.getRnd().nextInt(Bidder.BIDDERNAMES.length)])) {
        }
        return new Bidder.LimitBidder(name);
    }

    private boolean contains(List<Bidder> bidders, Class checkClass) {
        for (Bidder bidder : bidders) {
            if (!checkClass.isInstance(bidder)) continue;
            return true;
        }
        return false;
    }

    private boolean containsName(List<Bidder> bidders, String name) {
        for (Bidder bidder : bidders) {
            if (!name.equals(bidder.getBidderName())) continue;
            return true;
        }
        return false;
    }

    public long getSlaveValue() {
        if (this.slaveValue == null && this.slave != null) {
            this.slaveValue = this.slave.calculateValue();
        }
        return this.slaveValue;
    }

    public int getProfit() {
        return this.profit;
    }

    public boolean isAbort() {
        return this.abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public boolean isOwnSlave() {
        return this.ownSlave;
    }

    public static interface AuctionGui {
        public void update();

        public void close();
    }
}

