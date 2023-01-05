package byow.Core.Assets;

import byow.Core.Assets.Point;
import byow.Core.Assets.World;
import byow.TileEngine.TETile;

import static byow.TileEngine.Tileset.*;

public class Avatar {

    private Point location;
    private World world;

    private TETile skin;

    /**
     * Avatar spawns in a random room's center
     **/
    public Avatar(World world, Point p, TETile skin) {
        this.world = world;
        this.location = p;
        this.skin = skin;
        world.set(p, skin);
    }

    public TETile moveUp() {
        Point nextPos = new Point(location.x(), location.y() + 1);
        if (!world.get(nextPos).equals(TREE)) {
            world.set(location, FLOOR);
            TETile tile = world.get(nextPos);
            location = nextPos;
            world.set(location, skin);
            return tile;
        }
        return null;

    }

    public TETile moveDown() {
        Point nextPos = new Point(location.x(), location.y() - 1);
        if (!world.get(nextPos).equals(TREE)) {
            world.set(location, FLOOR);
            TETile tile = world.get(nextPos);
            location = nextPos;
            world.set(location, skin);
            return tile;
        }
        return null;
    }

    public TETile moveRight() {
        Point nextPos = new Point(location.x() + 1, location.y());
        if (!world.get(nextPos).equals(TREE)) {
            world.set(location, FLOOR);
            TETile tile = world.get(nextPos);
            location = nextPos;
            world.set(location, skin);
            return tile;
        }
        return null;
    }

    public TETile moveLeft() {
        Point nextPos = new Point(location.x() - 1, location.y());
        if (!world.get(nextPos).equals(TREE)) {
            TETile tile = world.get(nextPos);
            world.set(location, FLOOR);
            location = nextPos;
            world.set(location, skin);
            return tile;
        }
        return null;
    }

    public Point getLocation() {
        return location;
    }
}
