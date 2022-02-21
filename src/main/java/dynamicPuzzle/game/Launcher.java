package dynamicPuzzle.game;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {

        Game game = new Game(chooseGameSize());
    }

    /**
     * Lets the player choose the size of the game by requesting an input.
     * @return chosen game size
     */
    public static int chooseGameSize() {

        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter preferred size for the game.");
        System.out.println("Input equals amount of rows and columns. A minimum size of 5 is recommended.");
        System.out.println("For all inputs press enter to confirm.");

        return scanner.nextInt();
    }

}
