package mazebobot.mazeboot;

public enum Direction {
    UP(0, -1, -90),
    DOWN(0, 1, 90),
    LEFT(-1, 0, 180),
    RIGHT(1, 0, 0);

    private final int changeX;
    private final int changeY;
    private final double angle;

    Direction(int changeX, int changeY, double angle) {
        this.changeX = changeX;
        this.changeY = changeY;
        this.angle = angle;
    }

    public int getChangeX() {
        return changeX;
    }

    public int getChangeY() {
        return changeY;
    }

    public double getAngle() {
        return angle;
    }
}
