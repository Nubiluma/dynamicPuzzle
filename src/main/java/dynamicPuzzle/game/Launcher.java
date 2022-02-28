package dynamicPuzzle.game;

import java.util.Scanner;

public class Launcher {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RED = "\u001B[31m";

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

        Logger.logLine(ANSI_CYAN, "Enter preferred size for the game.\n" +
                "Input equals amount of rows and columns. A minimum size of " + minSize + " and a maximum of size of " + maxSize +" will be accepted.");
        Logger.logLine(ANSI_WHITE, "(For all inputs press enter to confirm.)");
        Logger.log(ANSI_CYAN, "Preferred size: ");

        int size = scanner.nextInt();
        if (size >= minSize && size <= maxSize){
            return size;
        }

        Logger.logLine(ANSI_RED, "Input invalid. Enter a number between " + minSize + " and " + maxSize + "!");
        return chooseGameSize();
    }

}
