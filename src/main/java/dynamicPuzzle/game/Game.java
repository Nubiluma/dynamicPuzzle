package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

import java.util.Scanner;

public class Game {

    private Field gameField;
    private final int size; //for field's size to match game size set in Launcher
    private boolean running;
    private static Scanner scanner;

    private Piece choiceA, choiceB, choiceC;

    public Game(int size) {
        this.size = size;
        init();
        tick();
    }

    //method for setting up the game
    private void init() {
        running = true;
        gameField = new Field(size);
    }

        private void tick() {
            System.out.println();
            gameField.printField();
            choose();
    }

    public Piece choose() {

        System.out.println("Choose between 1 and 3!");
        int c = scanner.nextInt();

        if (c == 1 && choiceA != null) {
            System.out.println("Chosen 1");
            System.out.println();
            return choiceA;
        } else if (c == 2 && choiceB != null) {
            System.out.println("Chosen 2");
            System.out.println();
            return choiceB;
        } else if (c == 3 && choiceC != null) {
            System.out.println("Chosen 3");
            System.out.println();
            return choiceC;
        }

        System.out.println("error");
        return choose();

    }
}
