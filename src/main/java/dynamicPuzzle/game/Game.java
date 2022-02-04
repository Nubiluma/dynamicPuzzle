package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

import java.util.Random;
import java.util.Scanner;

import static dynamicPuzzle.game.Field.field;

public class Game {

    private Field gameField;
    private final int size; //for field's size to match game size set in Launcher
    private boolean running;
    private int x, y;

    private static Scanner scanner;
    private final Random random = new Random();

    private Piece choiceA, choiceB, choiceC;
    private Piece currentPiece;

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

        while (running) {
            System.out.println();
            gameField.printField();
            updateChoices();
            choose();
            place();
            checkForLose();
        }
    }

    public int getRandomID() {
        return random.nextInt(5); //bound is tied to amount of piece ids (12) / currently reduced for testing!
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
            currentPiece = choiceA;
            choiceA = null;
            return currentPiece;
        } else if (c == 2 && choiceB != null) {
            System.out.println("Chosen 2");
            System.out.println();
            currentPiece = choiceB;
            choiceB = null;
            return currentPiece;
        } else if (c == 3 && choiceC != null) {
            System.out.println("Chosen 3");
            System.out.println();
            currentPiece = choiceC;
            choiceC = null;
            return currentPiece;
        }

        System.out.println("error");
        return choose();

    }

    public void place() {

        //this piece serves as 'filler' for the slots on the game field (which is a "Piece" array)
        Piece piece = new Piece(-1); //piece without an id and thus without shape

        System.out.println(">>Choose X and Y each from 1 to " + (size) + "<<");
        System.out.println();

        //x's and y's values will be reduced by 1 on order to be placed correctly.
        //This is necessary because of how the field's coordinates are displayed for the player, beginning by 1 instead of 0 (for optical reasons)
        //while the array's indices start with 0
        System.out.println("'◪' is the anchor for X and Y position!");
        System.out.println("X position:");
        y = (scanner.nextInt()) - 1;

        System.out.println("Y position:");
        x = (scanner.nextInt()) - 1;

        //when picking a choice piece in the choose method, the currentPiece object will point at the choice piece's id which is used here
        int id = currentPiece.getId();

        switch (id) {
            //square:
            case 0:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x][y + 1] = piece;
                        field[x + 1][y + 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //column2:
            case 1:
                if (isInBounds(x, x + 1) && isInBounds(y, y)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //column3:
            case 2:
                if (isInBounds(x, x + 2) && isInBounds(y, y)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 2][y] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //wall2:
            case 3:
                if (isInBounds(x, x) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x][y + 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //wall3:
            case 4:
                if (isInBounds(x, x) && isInBounds(y, y + 2)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x][y + 1] = piece;
                        field[x][y + 2] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
        }
        //'clear' currentPiece
        currentPiece = null;

        updateChoices();
    }

    public void checkForLose() {

        if (!gameFieldHasSpace()) {
            running = false;
            System.out.println();
            gameField.printField();
            System.out.println("You lose!");
        }
    }

    //checks if available choice pieces can fit in field by calling hasSpace method for every x and y position.
    public boolean gameFieldHasSpace() {

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (hasSpace(choiceA, i, j) || hasSpace(choiceB, i, j) || hasSpace(choiceC, i, j)) {

                    //for testing
                    if (!hasSpace(choiceA, i, j)) {
                        System.out.println("Piece 1 is either null or has no space.");
                    }
                    if (!hasSpace(choiceB, i, j)) {
                        System.out.println("Piece 2 is either null or has no space.");
                    }
                    if (!hasSpace(choiceC, i, j)) {
                        System.out.println("Piece 3 is either null or has no space.");
                    }

                    return true;
                }
            }
        }
        return false;
    }

    //checks whether the piece placement is possible within the game field and will return false if the piece had to overlap the bounds
    public boolean isInBounds(int index1, int index2) {
        return index1 >= 0 && index2 >= 0 && index1 <= field.length - 1 && index2 <= field.length - 1;
    }

    //checks if indices are in bounds via isInBounds method and if the piece to place had to overlap already occupied slots on the field.
    public boolean hasSpace(Piece piece, int x, int y) {

        //important for gameFieldHasSpace method
        if (piece == null) {
            return false;
        }

        int id = piece.getId();

        switch (id) {
            //square:
            case 0:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x][y + 1] == null && field[x + 1][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //column2:
            case 1:
                if (isInBounds(x, x + 1) && isInBounds(y, y)) {
                    if (field[x][y] == null && field[x + 1][y] == null) {
                        return true;
                    }
                }
                break;
            //column3:
            case 2:

                if (isInBounds(x, x + 2) && isInBounds(y, y)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x + 2][y] == null) {
                        return true;
                    }
                }
                break;
            //wall2:
            case 3:
                if (isInBounds(x, x) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //wall3:
            case 4:
                if (isInBounds(x, x) && isInBounds(y, y + 2)) {
                    if (field[x][y] == null && field[x][y + 1] == null && field[x][y + 2] == null) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }
}
