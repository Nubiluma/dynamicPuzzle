package dynamicPuzzle.game;

import java.util.Scanner;

public class Launcher {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
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
        int maxSize = 11;
        Scanner scanner = new Scanner(System.in);
        System.out.println(ANSI_CYAN);
        System.out.println("Enter preferred size for the game.");
        System.out.println("Input equals amount of rows and columns. A minimum size of " + minSize +
                " and a maximum of size of " + maxSize +" will be accepted.");
        System.out.println(ANSI_WHITE + "(For all inputs press enter to confirm.)" + ANSI_RESET);
        System.out.print(ANSI_CYAN + "Preferred size: " + ANSI_RESET);

        int size = scanner.nextInt();
        if (size >= minSize && size <= maxSize){
            return size;
        }
        System.out.println(ANSI_RED + "Input invalid. Enter a number between " + minSize + " and " + maxSize + "!" + ANSI_RESET);
        return chooseGameSize();
    }

}
