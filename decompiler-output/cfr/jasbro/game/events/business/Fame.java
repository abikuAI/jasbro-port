/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.events.business;

import jasbro.texts.TextUtil;
import java.io.Serializable;

public class Fame
implements Serializable {
    private double fame = 0.0;

    public void modifyFame(double fameModifier) {
        this.fame += fameModifier;
        if (this.fame < 0.0) {
            this.fame = 0.0;
        }
    }

    public long getFame() {
        return (long)this.fame;
    }

    public FameState getFameCharacter() {
        return FameState.getFameStateCharacter(this.getFame());
    }

    public FameState getFameBuilding() {
        return FameState.getFameStateBuilding(this.getFame());
    }

    public static enum FameState {
        OBSCURE(-1000L, -1000L),
        UNKNOWN(5L, 50L),
        UNFAMILIAR(50L, 500L),
        UNREMARKABLE(200L, 2000L),
        NOTED(1000L, 20000L),
        RUMORED(5000L, 100000L),
        REPUTABLE(20000L, 500000L),
        RENOWNED(50000L, 1000000L),
        INFLUENTIAL(100000L, 5000000L),
        ACCLAIMED(200000L, 10000000L),
        EMINENT(300000L, 50000000L),
        CELEBRIOUS(500000L, 100000000L),
        LEGENDARY(1000000L, 1000000000L);

        private long minFameCharacter;
        private long minFameBuilding;

        private FameState(long minFameCharacter, long minFameBuilding) {
            this.minFameCharacter = minFameCharacter;
            this.minFameBuilding = minFameBuilding;
        }

        public long getMinFameCharacter() {
            return this.minFameCharacter;
        }

        public long getMinFameBuilding() {
            return this.minFameBuilding;
        }

        public String getText() {
            String text = TextUtil.tNoCheck("fame." + this.toString());
            if (text == null) {
                text = this.toString();
                text = text.charAt(0) + text.substring(1).toLowerCase();
            }
            return text;
        }

        public static FameState getFameStateCharacter(long fame) {
            FameState previousValue = null;
            for (FameState satisfaction : FameState.values()) {
                if (previousValue != null && satisfaction.getMinFameCharacter() > fame) {
                    return previousValue;
                }
                previousValue = satisfaction;
            }
            return LEGENDARY;
        }

        public static FameState getFameStateBuilding(long fame) {
            FameState previousValue = null;
            for (FameState satisfaction : FameState.values()) {
                if (previousValue != null && satisfaction.getMinFameBuilding() > fame) {
                    return previousValue;
                }
                previousValue = satisfaction;
            }
            return LEGENDARY;
        }
    }
}

