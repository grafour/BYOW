package byow.Core.Assets;

import byow.Core.RandomUtils;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Flower {
    public static final int minFlowers = 60;
    public static final int maxFlowers = 80;
    public static final double chance = 0.5;

    public static void putFlowers(World world) {
        int numFlowers = RandomUtils.uniform(world.getSeed(), minFlowers, maxFlowers);
        Random seed = world.getSeed();
        boolean whichFlower;
        Point flowerPoint;
        for (int i = 0; i < numFlowers; i++) {
            flowerPoint = world.generateRandomOutsidePoint();
            whichFlower = RandomUtils.bernoulli(seed, chance);
            if (whichFlower) {
                world.set(flowerPoint, Tileset.SUNFLOWER);
            } else {
                world.set(flowerPoint, Tileset.DANDELION);
            }
        }
    }

}
