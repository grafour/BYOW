package byow.Core.Assets;

import byow.Core.Assets.Point;

public class Room {

    private Point bottomLeft;
    private Point topRight;
    private int ID;

    /**
     * A room object with the bottom left and top right points.
     * Room is kept track through an ID number.
     */
    public Room(Point bottomLeft, Point topRight, int ID) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.ID = ID;
    }

    /**
     * Calculates the center point of the room.
     */
    public Point center() {
        int x = (topRight.x() + bottomLeft.x()) / 2;
        int y = (topRight.y() + bottomLeft.y()) / 2;
        return new Point(x, y);
    }

    /**
     * Returns the ID number of the room.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Returns the left-most x-value of the room.
     */
    public int xMin() {
        return bottomLeft.x();
    }

    /**
     * Returns the right-most x-value of the room.
     */
    public int xMax() {
        return topRight.x();
    }

    /**
     * Returns the highest y-value of the room.
     */
    public int yMin() {
        return bottomLeft.y();
    }

    /**
     * Returns the lowest y-value of the room.
     */
    public int yMax() {
        return topRight.y();
    }
}
