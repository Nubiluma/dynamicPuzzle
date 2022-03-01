package dynamicPuzzle.game;

import dynamicPuzzle.utilities.Color;
import dynamicPuzzle.utilities.Input;
import dynamicPuzzle.utilities.Logger;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {

        Game game = new Game(chooseGameSize());
    }

    /**
     * Lets the player choose the size of the game by requesting an input.
     *
     * @return chosen game size
     */
    public static int chooseGameSize() {

        int minSize = 5;
        int maxSize = 9;
        Scanner scanner = new Scanner(System.in);

        Logger.logLine(Color.CYAN.colorCode, "Enter preferred size for the game.\n" +
                "Input equals amount of rows and columns. A minimum size of " + minSize + " and a maximum of size of " + maxSize + " will be accepted.");
        Logger.logLine(Color.WHITE.colorCode, "(For all inputs press enter to confirm.)");
        Logger.log(Color.CYAN.colorCode, "Preferred size: ");

        int size = Input.inputInt(scanner);

        if (size >= minSize && size <= maxSize) {
            return size;
        }

        Logger.logLine(Color.RED.colorCode, "Input invalid. Enter a number between " + minSize + " and " + maxSize + "!");
        return chooseGameSize();
    }

}
