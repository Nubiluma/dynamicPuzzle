package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private Field gameField;
    private final int size; //for field's size to match game size set in Launcher
    private boolean running;
    private static Scanner scanner;

    private final Random random = new Random();

    private Piece choiceA, choiceB, choiceC;

    public Game(int size) {
        this.size = size;
        init();
        tick();
    }

    //method for setting up the game
    private void init() {
        running = true;
        scanner = new Scanner(System.in);
        gameField = new Field(size);
    }

    private void tick() {
        System.out.println();
        gameField.printField();
        updateChoices();
        choose();
    }

    public int getRandomID() {
        return random.nextInt(13); //bound is tied to amount of piece ids (12)
    }

    public void updateChoices() {

        if (choiceA == null && choiceB == null && choiceC == null) {

            choiceA = new Piece(getRandomID());
            choiceB = new Piece(getRandomID());
            choiceC = new Piece(getRandomID());
        }

        System.out.println("1: ");
        if (choiceA != null) {
            choiceA.printPiece(choiceA);
        } else {
            System.out.println("N/A");
            System.out.println();
        }

        System.out.println("2: ");
        if (choiceB != null) {
            choiceB.printPiece(choiceB);
        } else {
            System.out.println("N/A");
            System.out.println();
        }

        System.out.println("3: ");
        if (choiceC != null) {
            choiceC.printPiece(choiceC);
        } else {
            System.out.println("N/A");
            System.out.println();
        }

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
