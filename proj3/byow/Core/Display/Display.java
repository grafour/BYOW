package byow.Core.Display;

import byow.Core.Assets.Point;
import byow.Core.Assets.World;
import byow.Core.Engine;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;

public class Display {

    private static final int width = Engine.WIDTH * 16;
    // Width of the StdDraw window.
    private static final int height = Engine.HEIGHT * 16;
    // Height of the StdDraw window.
    private static final int fontSizeBig = 40;
    private static final int fontSizeSmall = 20;
    private static final Font fontBig = new Font("Futura", Font.PLAIN, fontSizeBig);
    private static final Font fontSmall = new Font("Futura", Font.PLAIN, fontSizeSmall);
    private static final String objective = "Collect All The Coins!";
    private TERenderer ter;

    public Display() {
        StdDraw.setCanvasSize(width, height);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();
    }

    public void displayMenu() {
        StdDraw.clear(Color.black);
        StdDraw.setFont(fontBig);
        StdDraw.text(0.5, 0.65, "CS61B: THE GAME");
        StdDraw.setFont(fontSmall);
        StdDraw.text(0.5, 0.45, "New Game [N]");
        StdDraw.text(0.5,0.4, "New Multiplayer Game [M]");
        StdDraw.text(0.5, 0.35, "Load Game [L]");
        StdDraw.text(0.5, 0.3, "Controls [C]");
        StdDraw.text(0.5, 0.25, "Quit [Q]");
        StdDraw.show();
    }

    public void displaySeed(String seed) {
        StdDraw.clear(Color.black);
        StdDraw.setFont(fontBig);
        StdDraw.text(0.5, 0.6, "Enter Seed:");
        StdDraw.text(0.5, 0.5, seed);
        StdDraw.text(0.5, 0.4, "Press [S] to continue.");
        StdDraw.show();
    }

    public void displayControls() {
        StdDraw.clear(Color.black);
        StdDraw.setFont(fontBig);
        StdDraw.text(0.5, 0.85, "Controls");
        StdDraw.setFont(fontSmall);
        StdDraw.text(0.3, 0.7, "Player 1");
        StdDraw.text(0.7, 0.7, "Player 2");
        StdDraw.text(0.3, 0.6, "W - Move Up");
        StdDraw.text(0.3, 0.55, "A - Move Left");
        StdDraw.text(0.3, 0.5, "S - Move Down");
        StdDraw.text(0.3, 0.45, "D - Move Right");
        StdDraw.text(0.3, 0.4, "O - Toggle View");
        StdDraw.text(0.3, 0.35, ":Q - Quit");
        StdDraw.text(0.7, 0.6, "T - Move Up");
        StdDraw.text(0.7, 0.55, "F - Move Left");
        StdDraw.text(0.7, 0.5, "G - Move Down");
        StdDraw.text(0.7, 0.45, "H - Move Right");
        StdDraw.text(0.5, 0.25, "Press [Q] to return.");
        StdDraw.show();

    }

    public void displayVictory() {
        StdDraw.setCanvasSize(width, height);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.black);
        StdDraw.setFont(fontBig);
        StdDraw.setYscale(0, 1);
        StdDraw.setXscale(0, 1);
        StdDraw.text(0.5, 0.5, "You Win!");
        StdDraw.show();
        StdDraw.pause(10000);
    }

    public void renderInitialWorld(World world) {
        ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        ter.renderFrame(world.state());
    }

    public void renderFrame(World world, boolean light) {
        if (light) {
            renderHUD(world);
            ter.renderFrame(world.state());
        } else {
            ter.renderFrame(world.toggleLineOfSight());
        }
    }

    private String mouseHover(World world) {
        int x = (int) Math.round(StdDraw.mouseX());
        int y = (int) Math.round(StdDraw.mouseY());
        Point p = new Point(x, y);
        if (world.inWorld(p)) {
            TETile tile = world.get(new Point(x, y));
            return tile.description();
        }
        return "";
    }

    private void renderHUD(World world) {
        String tileHover = mouseHover(world);
        String totalCoins = "Coins Collected: " + world.getScore();
        int index;
        TETile tile;
        for (int x = 0; x < Engine.WIDTH; x++) {
            if (x < tileHover.length()) {
                tile = Tileset.HUDCharTile(tileHover.charAt(x));
            } else if (x >= (Engine.WIDTH / 2) - objective.length() / 2 && x < (Engine.WIDTH / 2) + objective.length() / 2) {
                index = x - ((Engine.WIDTH / 2) - objective.length() / 2);
                tile = Tileset.HUDCharTile(objective.charAt(index));
            } else if (x >= (Engine.WIDTH - totalCoins.length())) {
                index = x - (Engine.WIDTH - totalCoins.length());
                tile = Tileset.HUDCharTile(totalCoins.charAt(index));
            } else {
                tile = Tileset.HUD;
            }
            Point p = new Point(x, Engine.HEIGHT - 1);
            world.set(p, tile);
        }
    }
}
