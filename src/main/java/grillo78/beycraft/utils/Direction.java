package grillo78.beycraft.utils;

public enum Direction {
    RIGHT(1), LEFT(-1);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public static Direction getFromValue(int direction) {
        return direction == 1? RIGHT : LEFT;
    }

    public int getValue() {
        return value;
    }

    public Direction inverted() {
        return this == RIGHT? LEFT: RIGHT;
    }
}
