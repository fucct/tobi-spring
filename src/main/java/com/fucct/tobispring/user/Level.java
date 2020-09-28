package com.fucct.tobispring.user;

public enum Level {
    BASIC(1, 2), SILVER(2, 3), GOLD(3, 0);

    private final int level;
    private final int next;

    Level(final int level, final int next) {
        this.level = level;
        this.next = next;
    }

    public int intValue() {
        return level;
    }

    public Level nextLevel() {
        if (next == 0) {
            return null;
        }
        return valueOf(next);
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1:
                return BASIC;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
            default:
                throw new AssertionError("Unknown value: " + value);
        }
    }
}
