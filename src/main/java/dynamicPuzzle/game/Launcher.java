package dynamicPuzzle.game;

import java.util.Scanner;

public class Launcher {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {

        Game game = new Game(chooseGameSize());
    }

    /**
     * Lets the player choose the size of the game by requesting an input.
     *
     * @return chosen game size
     */
    public static int chooseGameSize() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(ANSI_CYAN);
        System.out.println("Enter preferred size for the game.");
        System.out.println("Input equals amount of rows and columns. A minimum size of 5 is recommended.");
        System.out.println(ANSI_WHITE + "(For all inputs press enter to confirm.)" + ANSI_RESET);
        System.out.print(ANSI_CYAN + "Preferred size: " + ANSI_RESET);

        return scanner.nextInt();
    }

}
