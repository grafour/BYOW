package byow.Core.Display;


import byow.Core.Assets.World;
import byow.Core.Input.InputSource;
import byow.Core.Input.KeyboardSource;
import byow.Core.Assets.WorldSaving.ReadFile;
import byow.Core.Assets.WorldSaving.SaveFile;

public class Source {

    private Display screen;
    private World world;
    private boolean createdWorld;
    private boolean keyboard;
    private boolean light;
    private boolean multiplayer;
    private int totalCharacters;
    private String soFar;

    public Source(InputSource input) {

        soFar = "";
        totalCharacters = 0;
        keyboard = false;
        light = true;

        if (input instanceof KeyboardSource) {
            keyboard = true;
            screen = new Display();
        }
        menuOptions(input);
        controlCharacter(input);
    }

    public void menuOptions(InputSource input) {
        screen.displayMenu();
        while (input.hasNextKey()) {
            screen.displayMenu();
            char c = input.getNextKey();
            if (c != '\0') {
                soFar += c;
                totalCharacters += 1;
            }
            if (c == 'N') {
                makeNewWorld(input, false);
                break;
            } else if (c == 'M') {
                makeNewWorld(input, true);
                break;
            } else if (c == 'L') {
                loadPreviousWorld();
                break;
            } else if (c == 'C') {
                displayControls(input);
            }else if (c == 'Q') {
                System.exit(0);
            }
        }
    }

    private void makeNewWorld(InputSource input, boolean multi) {

        String stringSeed = "";
        if (keyboard) {
            screen.displaySeed(stringSeed);
        }

        while (input.hasNextKey()) {
            char c = input.getNextKey();
            if (c != '\0') {
                soFar += c;
                totalCharacters += 1;
            }
            if (c == 'S') {
                break;
            } else if (Character.isDigit(c) && keyboard) {
                stringSeed += c;
                screen.displaySeed(stringSeed);
            } else if (Character.isDigit(c)) {
                stringSeed += c;
            }
        }
        long seed = Long.parseLong(stringSeed);
        multiplayer = multi;
        world = new World(seed, multiplayer);
        if (keyboard) {
            screen.renderInitialWorld(world);
        }
    }

    private void loadPreviousWorld() {
        ReadFile file = new ReadFile();
        Source toBeCopied = file.sourceToBeCopied();
        world = toBeCopied.getWorld();
        createdWorld = toBeCopied.getCreatedWorld();
        soFar = toBeCopied.getStringSoFar();
        totalCharacters = toBeCopied.getTotalCharacters();
        multiplayer = toBeCopied.getMultiplayer();
        if (keyboard) {
            screen.renderInitialWorld(world);
        }
    }

    private void displayControls(InputSource input) {
        if (keyboard) {
            screen.displayControls();
        }
        while (input.hasNextKey()) {
            char c = input.getNextKey();
            if (c != '\0') {
                soFar += c;
                totalCharacters += 1;
            }
            if (c == 'Q') {
                break;
            }
        }
    }

    private void controlCharacter(InputSource input) {
        while (input.hasNextKey()) {

            char c = input.getNextKey();
            if (c != '\0') {
                soFar += c;
                totalCharacters += 1;
            }

            if (c == 'W') {
                world.movePlayer1Up();
            } else if (c == 'A') {
                world.movePlayer1Left();
            } else if (c == 'S') {
                world.movePlayer1Down();
            } else if (c == 'D') {
                world.movePlayer1Right();
            } else if (c == 'T' && multiplayer) {
                world.movePlayer2Up();
            } else if (c == 'F' && multiplayer) {
                world.movePlayer2Left();
            } else if (c == 'G' && multiplayer) {
                world.movePlayer2Down();
            } else if (c == 'H' && multiplayer) {
                world.movePlayer2Right();
            } else if (c == 'O') {
                light = !light;
            } else if (c == 'Q' && soFar.charAt(totalCharacters - 2) == ':') {
                String moves = soFar.substring(0, soFar.length() - 2);
                SaveFile.save(moves);
                System.exit(0);
            }
            if (world.winCondition()) {
                screen.displayVictory();
                System.exit(0);
            }
            if (keyboard) {
                screen.renderFrame(world, light);
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public boolean getCreatedWorld() {
        return createdWorld;
    }

    public String getStringSoFar() {
        return soFar;
    }

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public boolean getMultiplayer() {
        return multiplayer;
    }

}
