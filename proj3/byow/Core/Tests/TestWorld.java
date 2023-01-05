package byow.Core.Tests;
import byow.Core.Assets.World;
import byow.TileEngine.*;

public class TestWorld {

    /**
     * Used to test world generation given a seed.
     */
    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        long seed = 1263;
        ter.initialize(World.width, World.height);
        World world = new World(seed, false);
        ter.renderFrame(world.state());

    }
}
