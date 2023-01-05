package byow.Core.Assets.WorldGeneration;
import byow.Core.Assets.Hallway;
import byow.Core.Assets.Point;
import byow.Core.Assets.Room;
import byow.Core.Assets.World;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class GenerateHallways {

    private World world;

    /**
     * Generates hallways that connect every room in the world.
     */
    public GenerateHallways(World world) {
        this.world = world;
        createAllHallways();
    }

    /**
     * Connects every room with the next room (ordered by roomID)
     */
    private void createAllHallways() {

        // Starts at room 0 and creates a hallway to the next room.
        for (int roomID = 0; roomID < world.getNumRooms() - 1; roomID++) {
            Room roomFrom = world.getIdToRoom().get(roomID);
            Room roomTo = world.getIdToRoom().get(roomID + 1);
            createHallway(roomFrom, roomTo);
        }
    }

    /**
     * Generates a hallway between the two rooms
     */
    private void createHallway(Room roomFrom, Room roomTo) {

        // Creates a hallway that connects roomFrom and roomTo
        Hallway hallway = new Hallway(this.world, roomFrom, roomTo);

        // Finds the center point of the two rooms.
        Point roomFromCenter = roomFrom.center();
        Point roomToCenter = roomTo.center();

        /*
         * Checks if the two Rooms are horizontally or vertically aligned.
         * If they are horizontally aligned, create a single horizontal hallway between the two rooms.
         * If they are vertically aligned, create a single vertical hallway between the two rooms.
         * Otherwise, create a turning hallway between the two room's center points.
         */
        Point endPoint;
        if (horizontalAlign(roomFrom, roomTo)) {
            endPoint = new Point(roomToCenter.x(), roomFromCenter.y());
            createHorizontalHallway(roomFromCenter, endPoint, hallway);
        }
        else if (verticalAlign(roomFrom, roomTo)) {
            endPoint = new Point(roomFromCenter.x(), roomToCenter.y());
            createVerticalHallway(roomFromCenter, endPoint, hallway);
        } else {
            createTurningHallway(roomFromCenter, roomToCenter, hallway);
        }
    }

    /**
     * Generates a horizontal hallway between the two points.
     */
    private void createHorizontalHallway(Point p1, Point p2, Hallway hallway) {

        /*
         * Algorithm goes from left to right.
         * startPoint should always be the leftmost point.
         * endPoint should always be the rightmost point.
         */
        Point startPoint;
        Point endPoint;
        if (p1.x() < p2.x()) {
            startPoint = p1;
            endPoint = p2;
        } else {
            startPoint = p2;
            endPoint = p1;
        }

        Point p;
        // Loop through every block between startPoint and endPoint inclusive.
        for (int x = startPoint.x(); x <= endPoint.x(); x++) {

            p = new Point(x, startPoint.y());
            world.set(p, Tileset.FLOOR);
            Point pointBelow = new Point(p.x(), p.y() - 1);
            Point pointAbove = new Point(p.x(), p.y() + 1);

            /*
             * If the point above or point below is empty, fill it with a wall tile.
             */
            if (world.get(pointBelow).equals(Tileset.GRASS)) {
                world.set(pointBelow, Tileset.TREE);
            }
            if (world.get(pointAbove).equals(Tileset.GRASS)) {
                world.set(pointAbove, Tileset.TREE);
            }
            world.setHallways(p, hallway);
        }
    }

    /**
     * Generates a vertical hallway between the two points.
     */
    private void createVerticalHallway(Point p1, Point p2, Hallway hallway) {

        /*
         * Algorithm goes from left to right.
         * startPoint should always be the leftmost point.
         * endPoint should always be the rightmost point.
         */
        Point startPoint;
        Point endPoint;
        if (p1.y() < p2.y()) {
            startPoint = p1;
            endPoint = p2;
        } else {
            startPoint = p2;
            endPoint = p1;
        }

        Point p;
        // Loop through every block between startPoint and endPoint inclusive.
        for (int y = startPoint.y(); y <= endPoint.y(); y++) {

            p = new Point(startPoint.x(), y);
            Point leftWall = new Point(p.x() - 1, p.y());
            Point rightWall = new Point(p.x() + 1, p.y());
            world.set(p, Tileset.FLOOR);

            /*
             * If the left point or right point is empty, fill it with a wall tile.
             */
            if (world.get(leftWall).equals(Tileset.GRASS)) {
                world.set(leftWall, Tileset.TREE);
            }
            if (world.get(rightWall).equals(Tileset.GRASS)) {
                world.set(rightWall, Tileset.TREE);
            }
            world.setHallways(p, hallway);
        }
    }

    /**
     * Creates a turning hallway between the two room's center points.
     */
    private void createTurningHallway(Point roomFromCenter, Point roomToCenter, Hallway hallway) {

        /*
         * There are two paths to take when creating a turning hallway from one point to another.
         * Direction determines which path the algorithm takes to generate a hallway.
         */
        boolean direction = RandomUtils.bernoulli(world.getSeed(), 0.5);
        Point turningPoint;
        if (direction) {
            turningPoint = new Point(roomFromCenter.x(), roomToCenter.y());
            createVerticalHallway(roomFromCenter, turningPoint, hallway);
            createHorizontalHallway(turningPoint, roomToCenter, hallway);
        } else {
            turningPoint = new Point(roomToCenter.x(), roomFromCenter.y());
            createHorizontalHallway(roomFromCenter, turningPoint, hallway);
            createVerticalHallway(turningPoint, roomToCenter, hallway);
        }
        // I am a perfectionist :)) (aka OCD)
        closeCorner(turningPoint);
    }

    /**
     * Checks if roomFrom's center point is within the horizontal range of roomTo.
     */
    private boolean horizontalAlign(Room roomFrom, Room roomTo) {
        Point roomFromCenter = roomFrom.center();
        return (roomFromCenter.y() > roomTo.yMin() && roomFromCenter.y() < roomTo.yMax());
    }

    /**
     * Checks if roomFrom's center point is within the vertical range of roomTo.
     */
    private boolean verticalAlign(Room roomFrom, Room roomTo) {
        Point roomFromCenter = roomFrom.center();
        return (roomFromCenter.x() > roomTo.xMin() && roomFromCenter.x() < roomTo.xMax());
    }

    /**
     * Fills in the corner that is empty when the hallway turns.
     */
    private void closeCorner(Point centerPoint) {
        for (int x = centerPoint.x() - 1; x <= centerPoint.x() + 1; x++) {
            for (int y = centerPoint.y() - 1; y <= centerPoint.y() + 1; y++) {
                Point currentPoint = new Point(x, y);
                TETile tile = world.get(currentPoint);
                if (tile.equals(Tileset.GRASS)) {
                    world.set(currentPoint, Tileset.TREE);
                }
            }
        }
    }

    /**
     * Checks if the hallway at Point p is equal to the hallway passed in.
     */
    private boolean sameHallway(Point p, Hallway hallway) {
        Hallway compareToHallway = (Hallway) world.getRoomsHallways(p);
        return hallway.equals(compareToHallway);
    }
}
