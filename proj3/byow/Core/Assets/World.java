package byow.Core.Assets;

import byow.Core.*;
import byow.Core.Engine;
import byow.Core.Assets.WorldGeneration.*;
import byow.TileEngine.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashMap;
import edu.princeton.cs.algs4.Graph;
import static byow.TileEngine.Tileset.*;

public class World {

    private TETile[][] arrayRep;
    // A 2D representation of the world.
    private Object[][] roomsHallways;
    // A 2D array that maps points to rooms and hallways.
    private Random seed;
    private int numRooms;
    private Graph graph;
    // TO BE USED LATER.
    private HashMap<Integer, Room> idToRoom;
    // A HashMap that maps roomIDs to Room objects.
    private Avatar player1;
    private Avatar player2;
    private ArrayList<Avatar> players;
    private int score;
    private int totalCoins;
    private static final int minRooms = 10;
    // Minimum number of rooms in the world.
    private static final int maxRooms = 20;

    private final int sightDistance = 3;
    // Maximum number of rooms in the world.
    public static final int width = Engine.WIDTH;
    // Width of the world.
    public static final int height = Engine.HEIGHT;
    // Height of the world.


    /**
     * Generates a world given a seed.
     */
    public World(long longSeed, boolean multiplayer) {
        seed = new Random(longSeed);
        arrayRep = generateWorld(GRASS);
        roomsHallways = new Object[Engine.WIDTH][Engine.HEIGHT];

        numRooms = RandomUtils.uniform(seed, minRooms, maxRooms);
        idToRoom = new HashMap<>();
        graph = new Graph(numRooms);
        players = new ArrayList<>();
        generateRoomsHallways(numRooms);
        score = 0;

        // Avatar
        player1 = new Avatar(this, startPoint(), AVATAR);
        players.add(player1);
        if (multiplayer) {
            player2 = new Avatar(this, startPoint(), AVATAR2);
            players.add(player2);
        }

        totalCoins = Coin.putCoins(this);
        Flower.putFlowers(this);
    }



    public Avatar getPlayer1() {
        return player1;
    }

    public Avatar getPlayer2() {
        return player2;
    }

    public void movePlayer1Up() {
        TETile tile = player1.moveUp();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer1Left() {
        TETile tile = player1.moveLeft();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer1Down() {
        TETile tile = player1.moveDown();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer1Right() {
        TETile tile = player1.moveRight();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer2Up() {
        TETile tile = player2.moveUp();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer2Left() {
        TETile tile = player2.moveLeft();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer2Down() {
        TETile tile = player2.moveDown();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    public void movePlayer2Right() {
        TETile tile = player2.moveRight();
        if (tile != null && tile.equals(COIN)) {
            collectedCoin();
        }
    }

    /**
     * Generates numRooms number of rooms and hallways that connect each room.
     */
    private void generateRoomsHallways(int numRooms) {
        new GenerateRooms(this, numRooms);
        new GenerateHallways(this);
    }

    /**
     * Sets the Point p to the given tile.
     */
    public void set(Point p, TETile tile) {
        arrayRep[p.x()][p.y()] = tile;
    }

    /**
     * Sets all points between Point p1 and Point p2 inclusive to a given tile.
     * Points p1 and p2 must have the same y-values.
     */
    public void setAllHorizontal(Point p1, Point p2, TETile tile) {
        for (int x = p1.x(); x <= p2.x(); x++) {
            set(new Point(x, p1.y()), tile);
        }
    }

    /**
     * Sets all points between Point p1 and Point p2 to a specific tile.
     * Points p1 and p2 must have the same x-values.
     */
    public void setAllVertical(Point p1, Point p2, TETile tile) {
        for (int y = p1.y(); y <= p2.y(); y++) {
            set(new Point(p1.x(), y), tile);
        }
    }

    /**
     * Checks if the area created by the points p1 and p2 is IN BOUND and have nothing in the area.
     * Used to check if space is available for a room.
     */
    public boolean validArea(Point bottomLeft, Point topRight) {
        if (!inWorld(bottomLeft) || !inWorld(topRight)) {
            return false;
        }
        for (int y = bottomLeft.y(); y <= topRight.y(); y++) {
            for (int x = bottomLeft.x(); x < topRight.x(); x++) {
                TETile tile = get(new Point(x, y));
                if (!tile.equals(Tileset.GRASS)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets the area from Point bottomLeft to Point topRight inclusive to a given tile in arrayRep.
     * Used mainly for laying down room floors.
     */
    public void setArea(Point bottomLeft, Point topRight, TETile tile) {
        for (int y = bottomLeft.y(); y < topRight.y(); y++) {
            for (int x = bottomLeft.x(); x < topRight.x(); x++) {
                set(new Point(x, y), tile);
            }
        }
    }

    /**
     * Sets the area from Point bottomLeft to Point topRight noninclusive to a given room in roomsHallways.
     */
    public void setRooms(Point bottomLeft, Point topRight, Room room) {
        for (int y = bottomLeft.y() + 1; y < topRight.y(); y++) {
            for (int x = bottomLeft.x() + 1; x < topRight.x(); x++) {
                roomsHallways[x][y] = room;
            }
        }
    }

    /**
     * Sets the Point p to a given hallway in roomsHallways.
     */
    public void setHallways(Point p, Hallway hallway) {
        roomsHallways[p.x()][p.y()] = hallway;
    }


    /**
     * Returns the room or hallway at Point p.
     */
    public Object getRoomsHallways(Point p) {
        return roomsHallways[p.x()][p.y()];
    }

    /**
     * Returns the tile at Point p.
     */
    public TETile get(Point p) {
        return arrayRep[p.x()][p.y()];
    }

    /**
     * Returns the state of the world in a 2D array form.
     */
    public TETile[][] state() {
        return arrayRep;
    }

    /**
     * Returns the world's seed.
     */
    public Random getSeed() {
        return seed;
    }

    /**
     * Returns the number of rooms in the world.
     */
    public int getNumRooms() {
        return numRooms;
    }

    /**
     * Connects roomOne and roomTwo in the graph
     */
    public void connect(Room roomOne, Room roomTwo) {
        int id1 = roomOne.getID();
        int id2 = roomTwo.getID();
        this.graph.addEdge(id1, id2);
    }

    /**
     * Adds room to the world by mapping room's ID to room.
     */
    public void addRoom(Room room) {
        this.idToRoom.put(room.getID(), room);
    }

    /**
     * Returns a HashMap that maps room IDs to rooms/
     */
    public HashMap<Integer, Room> getIdToRoom() {
        return idToRoom;
    }

    /**
     * Generates a random point in the world.
     */
    public Point generateRandomPoint() {
        int x = RandomUtils.uniform(seed, 1, Engine.WIDTH - 1);
        int y = RandomUtils.uniform(seed, 1, Engine.HEIGHT - 1);
        return new Point(x, y);
    }

    public Point generateRandomOutsidePoint() {
        Point p = generateRandomPoint();
        while(!get(p).equals(GRASS)) {
            p = generateRandomPoint();
        }
        return p;
    }

    /** get random point that's not obstacle **/
    private Point startPoint() {
        int ID = RandomUtils.uniform(seed, 0, numRooms - 1);
        Room room = idToRoom.get(ID);
        return room.center();
    }

    public Point generateRandomFloorPoint() {
        Point p = generateRandomPoint();
        while(!get(p).equals(FLOOR)) {
            p = generateRandomPoint();
        }
        return p;
    }

    /**
     * Checks if the Point p is a valid point in the world.
     */
    public boolean inWorld(Point p) {
        return (p.x() >= 1 && p.x() < Engine.WIDTH - 1 && p.y() >= 1 && p.y() < Engine.HEIGHT - 1);
    }

    private static TETile[][] generateWorld(TETile tile) {
        TETile[][] returnArray = new TETile[Engine.WIDTH][Engine.HEIGHT];
        for (int x = 0; x < Engine.WIDTH; x++) {
            for (int y = 0; y < Engine.HEIGHT; y++) {
                returnArray[x][y] = tile;
            }
        }
        return returnArray;
    }

    /** renders only the area within the square */
    public TETile[][] toggleLineOfSight() {
        TETile[][] lineOfSight = generateWorld(NOTHING);
        for (Avatar player : players) {
            Point location = player.getLocation();
            renderArea(location, sightDistance, lineOfSight);
        }
        return lineOfSight;
    }

    private void renderArea(Point center, int distance, TETile[][] map) {
        for (int x = center.x() - distance; x <= center.x() + distance; x++) {
            for (int y = center.y() - distance; y <= center.y() + distance; y++) {
                Point renderPoint = new Point(x, y);
                if (inWorld(renderPoint)) {
                    TETile renderTile = get(renderPoint);
                    set(renderPoint, renderTile, map);
                }
            }
        }
    }

    private static void set(Point p, TETile tile, TETile[][] map) {
        map[p.x()][p.y()] = tile;
    }

    public void collectedCoin() {
        score += 1;
    }

    public int getScore() {
        return score;
    }
    public boolean winCondition() {
        return score == totalCoins;
    }
}
