/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.world.market;

import jasbro.Util;
import jasbro.game.world.market.Auction;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Bidder
extends Thread
implements Serializable {
    private static final Logger log = LogManager.getLogger(Bidder.class);
    public static String[] BIDDERNAMES = new String[]{"Markus Aurelius", "Lady Margaret", "Count Hensen", "Vlad", "Slave Trainer Daisy", "Vincent Adelburn", "Merchant"};
    private boolean stopped = false;
    private Auction auction;
    private String bidderName;

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public String getBidderName() {
        return this.bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public Auction getAuction() {
        return this.auction;
    }

    public void stopBidding() {
        this.stopped = true;
    }

    public static class UiBidder
    extends Bidder {
        public UiBidder() {
            this.setBidderName("You");
        }

        public void bid(long amount) {
            this.getAuction().bid(amount, this);
        }
    }

    public static class PersistentBidder
    extends Bidder {
        private long lastBid = 0L;

        public PersistentBidder() {
            this.setBidderName("Prince Joffrick");
        }

        @Override
        public void run() {
            try {
                this.setStopped(false);
                while (!this.isStopped()) {
                    try {
                        Thread.sleep(Util.getInt(4000, 6000));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    long maxBid = this.getAuction().getMaxBid();
                    if (this.lastBid == 0L || this.getAuction().getMaxBidder() != this && maxBid - this.lastBid < this.getAuction().getSlaveValue() / 3L && maxBid < this.getAuction().getSlaveValue() * 2L) {
                        long bid = maxBid + maxBid / 10L;
                        this.getAuction().bid(bid, this);
                        this.lastBid = bid;
                        continue;
                    }
                    if (this.getAuction().getMaxBidder() == this) continue;
                    this.getAuction().addMsg(this.getBidderName() + " sighs and resigns.");
                    this.setStopped(true);
                }
            }
            catch (Exception e) {
                log.error("Error in bidder", (Throwable)e);
            }
        }
    }

    public static class ShockBidder
    extends Bidder {
        public ShockBidder() {
            this.setBidderName("Lord Baratheon");
        }

        @Override
        public void run() {
            try {
                this.setStopped(false);
                while (!this.isStopped()) {
                    try {
                        Thread.sleep(Util.getInt(4000, 12000));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    long maxBid = this.getAuction().getMaxBid();
                    if (this.getAuction().getMaxBidder() != this && maxBid < this.getAuction().getSlaveValue() * 2L) {
                        long bid = maxBid * 2L;
                        bid = bid < 1000L ? bid / 10L * 10L : bid / 100L * 100L;
                        this.getAuction().bid(bid, this);
                    }
                    try {
                        Thread.sleep(10000L);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e) {
                log.error("Error in bidder", (Throwable)e);
            }
        }
    }

    public static class MinimumBidder
    extends Bidder {
        public MinimumBidder() {
            this.setBidderName("Treasurer Cortez");
        }

        @Override
        public void run() {
            this.setStopped(false);
            while (!this.isStopped()) {
                try {
                    Thread.sleep(1000L);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                long maxBid = this.getAuction().getMaxBid();
                if (this.getAuction().getMaxBidder() == this || maxBid >= this.getAuction().getSlaveValue()) continue;
                this.getAuction().bid(maxBid + 1L, this);
            }
        }
    }

    public static class LimitBidder
    extends Bidder {
        private int maxValue;

        public LimitBidder(String bidderName) {
            this.setBidderName(bidderName);
        }

        @Override
        public void run() {
            try {
                this.setStopped(false);
                while (!this.isStopped()) {
                    long maxBid;
                    try {
                        Thread.sleep(Util.getInt(2000, 12000));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if ((maxBid = this.getAuction().getMaxBid()) >= (long)this.getMaxValue() || this.getAuction().getMaxBidder() == this) continue;
                    this.getAuction().bid(maxBid + (long)Util.getInt(1, 15) + this.getAuction().getSlaveValue() / 20L, this);
                }
            }
            catch (Exception e) {
                log.error("Error in bidder", (Throwable)e);
            }
        }

        public int getMaxValue() {
            if (this.maxValue == 0 && this.getAuction() != null) {
                int percent = Util.getRnd().nextInt(21) + 70;
                this.maxValue = (int)((double)(this.getAuction().getSlave().calculateValue() * (long)percent) / 100.0);
            }
            return this.maxValue;
        }
    }
}

