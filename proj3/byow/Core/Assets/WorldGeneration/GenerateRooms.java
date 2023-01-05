package byow.Core.Assets.WorldGeneration;

import byow.Core.Assets.Point;
import byow.Core.Assets.Room;
import byow.Core.Assets.World;
import byow.Core.Engine;
import byow.Core.RandomUtils;
import byow.TileEngine.Tileset;
import java.util.Random;

public class GenerateRooms {

    private World world;
    private Random seed;
    private static final int roomMinSize = 5;
    // Ensures that a room's dimensions (including walls) is at least a 5x5
    private int roomMaxHeight;
    private int roomMaxWidth;
    private static final double maxSizeMultiplier = 0.15;
    // Multiplier is used to determine a room's maximum size.

    /**
     * Generates a random number of rooms in the world.
     */
    public GenerateRooms(World world, int numRooms) {

        this.world = world;
        this.seed = world.getSeed();

        /*
         * A room's maximum dimensions are based on the world size (i.e. the larger a world is, the higher the maximum dimensions are).
         * Also ensures that a room's dimensions never exceed the world size.
         */
        this.roomMaxWidth = (int) Math.max(roomMinSize + 1, Math.round(Engine.WIDTH * maxSizeMultiplier));
        this.roomMaxHeight = (int) Math.max(roomMinSize + 1, Math.round(Engine.HEIGHT * maxSizeMultiplier));

        createAllRooms(numRooms);
    }

    /**
     * Create numRooms number of rooms.
     */
    private void createAllRooms(int numRooms) {
        for (int id = 0; id < numRooms; id++) {
            createRandomRoom(id);
        }
    }

    /**
     * Creates and returns a random room in the world.
     */
    private Room createRandomRoom(int roomID) {

        // Generates a random pair of corners for the room.
        RandomPair pairOfPoints = new RandomPair();
        Point bottomLeft = pairOfPoints.bottomLeft;
        Point topRight = pairOfPoints.topRight;

        // Lays down the floor of the room.
        world.setArea(bottomLeft, topRight, Tileset.FLOOR);

        Point bottomRight = new Point(topRight.x(), bottomLeft.y());
        Point topLeft = new Point(bottomLeft.x(), topRight.y());

        // Creates the four walls surrounding the room
        createWalls(bottomLeft, bottomRight, topLeft, topRight);

        // Adds the room to the world
        Room room = new Room(bottomLeft, topRight, roomID);
        world.addRoom(room);
        world.setRooms(bottomLeft, topRight, room);

        return room;
    }

    /**
     * Returns a random increment amount.
     */
    private int generateIncrement(int maxDimension) {
        return RandomUtils.uniform(seed, roomMinSize, maxDimension);
    }

    /**
     * Calculates the other corner point of a room.
     */
    private Point otherCorner(Point p, int widthIncrement, int heightIncrement) {
        return new Point(p.x() + widthIncrement, p.y() + heightIncrement);
    }

    /**
     * A pair class that generates two random points.
     * Meant to be used to calculate the bottom left and top right corners of a room.
     */
    private class RandomPair {
        public Point bottomLeft;
        public Point topRight;

        public RandomPair() {
            int width;
            int height;
            boolean validCorners = false;

            // Generate points until we get 2 valid points for a room.
            while (!validCorners) {
                bottomLeft = world.generateRandomPoint();
                width = generateIncrement(roomMaxWidth);
                height = generateIncrement(roomMaxHeight);
                topRight = otherCorner(bottomLeft, width, height);
                validCorners = world.validArea(bottomLeft, topRight);
            }
        }
    }

    /**
     * Creates the four walls surrounding the room.
     */
    private void createWalls(Point bottomLeft, Point bottomRight, Point topLeft, Point topRight) {
        world.setAllHorizontal(topLeft, topRight, Tileset.TREE); // top wall
        world.setAllHorizontal(bottomLeft, bottomRight, Tileset.TREE); // bottom wall
        world.setAllVertical(bottomLeft, topLeft, Tileset.TREE); // left wall
        world.setAllVertical(bottomRight, topRight, Tileset.TREE); // right wall
    }
}
