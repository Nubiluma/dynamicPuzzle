package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;
import static dynamicPuzzle.game.Field.field;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private Field gameField;
    private final int size; //for field's size to match game size set in Launcher
    private boolean[][] marker;
    private boolean running;
    private int score = 0;

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
        marker = new boolean[size][size];
    }

    /**
     * "Game loop" will run until 'running' is set to false, which will be set in 'checkForLose' method if conditions are met
     */
    private void tick() {

        while (running) {
            System.out.println(">> Score: "+ score + " <<");
            System.out.println();
            gameField.printField();
            updateChoices();
            choose();
            place();
            evaluate();
            clearField();
            checkForLose();
        }
    }

    /**
     * Picks a random integer in boundary's scope. Integer will be set as piece's id, which is unique and defined in Piece class (i.e. id '0' = 'square')
     * @return
     */
    public int getRandomID() {
        return random.nextInt(13); //bound is tied to amount of piece ids (12)
    }

    /**
     * Generates Piece objects to choose from if all choice pieces are null. A choice piece will become null if it has been picked before by choose().
     * So, after every third round, this method will replace all three choice pieces, because they will all be null by then.
     */
    public void updateChoices() {

        if (choiceA == null && choiceB == null && choiceC == null) {

            //currently testing!
            /*choiceA = new Piece(getRandomID());
            choiceB = new Piece(getRandomID());
            choiceC = new Piece(getRandomID());*/

            choiceA = new Piece(5);
            choiceB = new Piece(7);
            choiceC = new Piece(9);
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

    /**
     * Lets the player pick one of the three choice pieces by demanding user input through scanner. Will fail if player inputs an Integer other than 1, 2 or 3
     * or if the choice piece is not available (null).
     * @return currentPiece: piece's id is needed for place() method.
     */
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

    /**
     *
     */
    public void place() {

        //this piece serves as 'filler' for the slots on the game field (which is a "Piece" array)
        Piece piece = new Piece(-1); //piece without an id and thus without shape

        System.out.println(">>Choose X and Y each from 1 to " + (size) + "<<");

        //x's and y's values will be reduced by 1 on order to be placed correctly.
        //This is necessary because of how the field's coordinates are displayed for the player, beginning by 1 instead of 0 (for optical reasons)
        //while the array's indices start with 0
        System.out.println("'◪' is the anchor for X and Y position!");
        System.out.println();
        System.out.println("X position:");
        int y = (scanner.nextInt()) - 1;

        System.out.println("Y position:");
        int x = (scanner.nextInt()) - 1;

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
            //lLeft:
            case 5:
                if (isInBounds(x, x + 2) && isInBounds(y, y - 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 2][y] = piece;
                        field[x + 2][y - 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //lRight:
            case 6:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 2][y] = piece;
                        field[x + 2][y + 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //lUpLeft:
            case 7:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x][y + 1] = piece;
                        field[x + 1][y + 1] = piece;
                        field[x + 2][y + 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //lUpRight:
            case 8:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 2][y] = piece;
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
            //zLeft:
            case 9:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 2)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x][y + 1] = piece;
                        field[x + 1][y + 1] = piece;
                        field[x + 1][y + 2] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //zRight:
            case 10:
                if (isInBounds(x, x + 1) && isInBounds(y - 1, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x][y + 1] = piece;
                        field[x + 1][y - 1] = piece;
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
            //zUpLeft:
            case 11:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 1][y + 1] = piece;
                        field[x + 2][y + 1] = piece;
                    } else {
                        System.out.println("Not enough space!");
                        place();
                    }
                } else {
                    System.out.println("Out of bounds or not enough space!");
                    place();
                }
                break;
            //zUpRight:
            case 12:
                if (isInBounds(x, x + 2) && isInBounds(y - 1, y)) {
                    if (hasSpace(currentPiece, x, y)) {
                        field[x][y] = piece;
                        field[x + 1][y] = piece;
                        field[x + 1][y - 1] = piece;
                        field[x + 2][y - 1] = piece;
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
            System.out.println("Total score: " + score);
        }
    }

    /**
     * Will be called at the end of every round of the game. Counting methods commit amount of full rows and columns.
     * The more rows/columns are counted at once, the higher the score (through Math.pow method)
     */
    public void evaluate() {
        int counter = countColumns() + countRows();
        if (counter != 0) {
            score += 100 * Math.pow(counter, counter);
            //example: 100 * 3^3 (power) => 100 * 27
        }
    }

    /**
     * Returns amount of columns counted to evaluate() method.
     *
     * @return
     */
    public int countColumns() {

        int counter = 0;
        int j = 0;
        int i;

        while (j < field.length) {

            for (i = 0; i < field.length; i++) {
                if (field[i][j] == null) {
                    //jump into next row as the current column has an empty slot
                    break;
                } else if (i == (field.length) - 1) {
                    //if the for loop continues until the last slot of a column, all the slots had to be filled until then and thus the column can be declared as full
                    counter += 1;

                    mark(false, i, j);
                }
            }
            j++;
        }
        return counter;
    }

    /**
     * Returns amount of rows counted to evaluate() method.
     *
     * @return
     */
    public int countRows() {

        int counter = 0;
        int i = 0;
        int j;

        while (i < field.length) {

            for (j = 0; j < field.length; j++) {
                if (field[i][j] == null) {
                    //jump into next column as the current row has an empty slot
                    break;
                } else if (j == (field.length) - 1) {
                    //if the for loop continues until the last slot of a row, all the slots had to be filled until then and thus the row can be declared as full
                    counter += 1;

                    mark(true, i, j);
                }
            }
            i++;
        }
        return counter;
    }

    /**
     * Will be called by the two counting methods. 'Marks' the indices in field which have to be cleared before the next round of the game (see clearField()).
     * @param row
     * @param a
     * @param b
     */
    public void mark(boolean row, int a, int b) {

        if (row) {
            while (b >= 0) {
                marker[a][b] = true;
                b--;
            }
        } else {
            while (a >= 0) {
                marker[a][b] = true;
                a--;
            }
        }
    }

    /**
     * Method will clear 'marked' indices if full rows and/or columns were counted in a round.
     *
     */
    public void clearField() {

        for (int i = 0; i < marker.length; i++) {
            for (int j = 0; j < marker.length; j++) {
                if (marker[i][j]) {
                    field[i][j] = null;
                    marker[i][j] = false;
                }
            }
        }
    }

    /**
     * checks if available choice pieces can fit in field by calling hasSpace method for every x and y position.
     */
    public boolean gameFieldHasSpace() {

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                if (hasSpace(choiceA, i, j) || hasSpace(choiceB, i, j) || hasSpace(choiceC, i, j)) {

                    /*for testing
                    if (!hasSpace(choiceA, i, j)) {
                        System.out.println("Piece 1 is either null or has no space.");
                    }
                    if (!hasSpace(choiceB, i, j)) {
                        System.out.println("Piece 2 is either null or has no space.");
                    }
                    if (!hasSpace(choiceC, i, j)) {
                        System.out.println("Piece 3 is either null or has no space.");
                    }*/

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks whether the piece placement is possible within the game field and will return false if the piece had to overlap the bounds
     */
    public boolean isInBounds(int index1, int index2) {
        return index1 >= 0 && index2 >= 0 && index1 <= field.length - 1 && index2 <= field.length - 1;
    }

    /**
     * checks if indices are in bounds via isInBounds method and if the piece to place had to overlap already occupied slots on the field in order to be placed
     * @param piece
     * @param x
     * @param y
     * @return
     */
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
            //lLeft:
            case 5:
                if (isInBounds(x, x + 2) && isInBounds(y, y - 1)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x + 2][y] == null && field[x + 2][y - 1] == null) {
                        return true;
                    }
                }
                break;
            //lRight:
            case 6:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x + 2][y] == null && field[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //lUpLeft:
            case 7:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x][y + 1] == null && field[x + 1][y + 1] == null && field[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //lUpRight:
            case 8:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x][y + 1] == null && field[x + 1][y] == null && field[x + 2][y] == null) {
                        return true;
                    }
                }
                break;
            //zLeft:
            case 9:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 2)) {
                    if (field[x][y] == null && field[x][y + 1] == null && field[x + 1][y + 1] == null && field[x + 1][y + 2] == null) {
                        return true;
                    }
                }
                break;
            //zRight:
            case 10:
                if (isInBounds(x, x + 1) && isInBounds(y - 1, y + 1)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x][y + 1] == null && field[x + 1][y - 1] == null) {
                        return true;
                    }
                }
                break;
            //zUpLeft:
            case 11:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x + 1][y + 1] == null && field[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //zUpRight:
            case 12:
                if (isInBounds(x, x + 2) && isInBounds(y - 1, y)) {
                    if (field[x][y] == null && field[x + 1][y] == null && field[x + 1][y - 1] == null && field[x + 2][y - 1] == null) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

}
