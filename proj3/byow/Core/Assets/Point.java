package byow.Core.Assets;

public class Point {

    private int x;
    private int y;

    /**
     * Represents a point in the world.
     * The x-value is the width of the point.
     * The y-value is the height of the point.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the point.
     */
    public int x() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of the point.
     */
    public int y() {
        return this.y;
    }
}
