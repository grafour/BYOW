package byow.Core.Assets;

import byow.Core.Assets.Point;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;

import static byow.TileEngine.Tileset.COIN;

public class Coin {

    private static final int minCoins = 10;
    private static final int maxCoins = 20;

    public static int putCoins(World world) {
        int numCoins = RandomUtils.uniform(world.getSeed(), minCoins, maxCoins);
        for (int i = 0; i < numCoins; i++) {
            Point p = world.generateRandomFloorPoint();
            world.set(p, Tileset.COIN);
        }
        return numCoins;
    }
}
