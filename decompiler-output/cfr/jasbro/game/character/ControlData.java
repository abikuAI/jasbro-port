/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character;

public class ControlData {
    private long controlGenerated = 0L;
    private long controlUsed = 0L;
    private long diff = 0L;

    public void add(int value) {
        this.diff += (long)value;
        if (value < 0) {
            this.controlUsed -= (long)value;
        } else {
            this.controlGenerated += (long)value;
        }
    }

    public long getControlGenerated() {
        return this.controlGenerated;
    }

    public void setControlGenerated(long controlGenerated) {
        this.controlGenerated = controlGenerated;
    }

    public long getControlUsed() {
        return this.controlUsed;
    }

    public void setControlUsed(long controlUsed) {
        this.controlUsed = controlUsed;
    }

    public long getDiff() {
        return this.diff;
    }

    public void setDiff(long diff) {
        this.diff = diff;
    }
}

