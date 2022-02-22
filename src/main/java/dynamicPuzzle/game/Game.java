package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

import static dynamicPuzzle.game.Field.getField;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private final Field gameField;
    final int size;
    private final boolean[][] marker;
    private boolean running;
    private int score = 0;

    //For console output coloration
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static Scanner scanner;
    private final Random random = new Random();

    private Piece choiceA, choiceB, choiceC;
    private Piece currentPiece;

    //this piece serves as 'filler' for the slots on the game field (which is a "Piece" array)
    Piece piece = new Piece(-1); //piece without an id and thus without shape

    public Game(int size) {
        this.size = size;
        running = true;
        scanner = new Scanner(System.in);
        gameField = new Field(size);
        marker = new boolean[size][size];
        tick();
    }

    /**
     * "Game loop" will run until 'running' is set to false when loosing
     */
    private void tick() {

        while (running) {
            System.out.println(ANSI_YELLOW + ">> Score: " + score + " <<" + ANSI_RESET);
            System.out.println();
            placeObstacles();
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
     * Method places Piece objects into random indices of Field array when starting the game. The amount of these 'obstacles' is bound to size of the array.
     */
    private void placeObstacles() {

        Random random = new Random();

        for (int i = 0; i < getField().length; i++) {
            getField()[random.nextInt(getField().length)][random.nextInt(getField().length)] = piece;
        }

    }

    /**
     * Picks a random integer in boundary's scope. Integer will be set as piece's id, which is unique and defined in Piece class (i.e. id '0' = 'square')
     *
     * @return random integer for piece id
     */
    private int getRandomID() {
        return random.nextInt(13); //bound is tied to amount of piece ids (12)
    }

    /**
     * Generates Piece objects to choose from if all choice pieces are null. A choice piece will become null if it has been picked before.
     * So, after every third round, this method will replace all three choice pieces, because they will all be null by then.
     */
    private void updateChoices() {

        if (choiceA == null && choiceB == null && choiceC == null) {

            choiceA = new Piece(getRandomID());
            choiceB = new Piece(getRandomID());
            choiceC = new Piece(getRandomID());

        }

        System.out.println("1: ");
        if (choiceA != null) {
            choiceA.printPiece(choiceA);
        } else {
            System.out.println("-");
            System.out.println();
        }

        System.out.println("2: ");
        if (choiceB != null) {
            choiceB.printPiece(choiceB);
        } else {
            System.out.println("-");
            System.out.println();
        }

        System.out.println("3: ");
        if (choiceC != null) {
            choiceC.printPiece(choiceC);
        } else {
            System.out.println("-");
            System.out.println();
        }

    }

    /**
     * Lets the player pick one of the three choice pieces by demanding user input through scanner. Will fail if player inputs an integer value other than 1, 2 or 3
     * or if the choice piece is not available (null).
     *
     * @return chosen piece
     */
    private Piece choose() {

        System.out.print(ANSI_PURPLE + "Choose between 1 and 3" + ANSI_RESET);
        System.out.print(ANSI_WHITE + " (or enter 0 to exit the game) " + ANSI_RESET);
        int c = scanner.nextInt();
        System.out.println(ANSI_GREEN);

        if (c == 0){
            System.out.println(ANSI_GREEN + "Exiting game..." + ANSI_RESET);
            running = false;
            return null;
        }

        if (c == 1 && choiceA != null) {
            System.out.println(">>You chose 1<<");
            System.out.println(ANSI_RESET);
            currentPiece = choiceA;
            choiceA = null;
            return currentPiece;
        } else if (c == 2 && choiceB != null) {
            System.out.println(">>You chose 2<<");
            System.out.println(ANSI_RESET);
            currentPiece = choiceB;
            choiceB = null;
            return currentPiece;
        } else if (c == 3 && choiceC != null) {
            System.out.println(">>You chose 3<<");
            System.out.println(ANSI_RESET);
            currentPiece = choiceC;
            choiceC = null;
            return currentPiece;
        }

        return choose();

    }

    /**
     * 'Places' pieces on gameField by putting piece objects into array's indices.
     */
    private void place() {

        if (!running){
            return;
        }

        System.out.println(ANSI_PURPLE + "Choose X and Y each from 1 to " + (size) + " !" + ANSI_RESET);

        //x's and y's values will be reduced by 1 on order to be placed correctly.
        //This is necessary because of how the field's coordinates are displayed for the player, beginning by 1 instead of 0 (for optical reasons)
        //while the array's indices start with 0
        System.out.println(ANSI_WHITE + "('◪' is the anchor for X and Y position)" + ANSI_RESET);
        System.out.println();
        System.out.print(ANSI_PURPLE + "X position: ");
        int y = (scanner.nextInt()) - 1;

        System.out.print("Y position: " + ANSI_RESET);
        int x = (scanner.nextInt()) - 1;

        //when picking a choice piece in the choose method, the currentPiece object will point at the choice piece's id which is used here
        int id = currentPiece.getId();

        System.out.println(ANSI_RED); //following system outputs in this method will only show when running into an error (can all be formatted the same)

        switch (id) {
            //square:
            case 0:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 1)) {
                    if (hasSpace(currentPiece, x, y)) {
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x][y + 1] = piece;
                        getField()[x + 1][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 2][y] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x][y + 1] = piece;
                        getField()[x][y + 2] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 2][y] = piece;
                        getField()[x + 2][y - 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 2][y] = piece;
                        getField()[x + 2][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x][y + 1] = piece;
                        getField()[x + 1][y + 1] = piece;
                        getField()[x + 2][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 2][y] = piece;
                        getField()[x][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x][y + 1] = piece;
                        getField()[x + 1][y + 1] = piece;
                        getField()[x + 1][y + 2] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x][y + 1] = piece;
                        getField()[x + 1][y - 1] = piece;
                        getField()[x + 1][y] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 1][y + 1] = piece;
                        getField()[x + 2][y + 1] = piece;
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
                        getField()[x][y] = piece;
                        getField()[x + 1][y] = piece;
                        getField()[x + 1][y - 1] = piece;
                        getField()[x + 2][y - 1] = piece;
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

        System.out.print(ANSI_RESET);
        updateChoices();
    }

    private void checkForLose() {

        if (!gameFieldHasSpace() && running) {
            running = false;
            System.out.println();
            gameField.printField();
            System.out.println(ANSI_RED + "Game over!" + ANSI_RESET);
            System.out.println(ANSI_YELLOW + "Total score: " + score + " points!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_YELLOW + "Total score: " + score + " points!" + ANSI_RESET);
        }
    }

    /**
     * Counting methods commit amount of full rows and columns.
     * The more rows/columns are counted at once, the higher the score (through Math.pow method)
     */
    private void evaluate() {
        if (!running){
            return;
        }
        int counter = countColumns() + countRows();
        if (counter != 0) {
            score += 100 * Math.pow(counter, counter);
            //example: 100 * 3^3 (power) => 100 * 27
        }
    }

    /**
     * Checks if gameField array has pieces set in every slot of a column. While loop's variable 'j' is the number of a column,
     * for loop's 'i' is current column's indices.
     *
     * @return counted columns
     */
    private int countColumns() {

        int counter = 0;
        int j = 0;
        int i;

        while (j < getField().length) {

            for (i = 0; i < getField().length; i++) {
                if (getField()[i][j] == null) {
                    //jump into next row as the current column has an empty slot
                    break;
                } else if (i == (getField().length) - 1) {
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
     * Checks if gameField array has pieces set in every slot of a row. While loop's variable 'j' is the number of a row,
     * for loop's 'i' is current row's indices.
     *
     * @return counted rows
     */
    private int countRows() {

        int counter = 0;
        int i = 0;
        int j;

        while (i < getField().length) {

            for (j = 0; j < getField().length; j++) {
                if (getField()[i][j] == null) {
                    //jump into next column as the current row has an empty slot
                    break;
                } else if (j == (getField().length) - 1) {
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
     * 'Marks' the indices in gameField which have to be cleared before the next round of the game.
     *
     * @param row rows and columns need to be handled differently
     * @param a   i variable
     * @param b   j variable
     */
    private void mark(boolean row, int a, int b) {

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
     */
    private void clearField() {

        for (int i = 0; i < marker.length; i++) {
            for (int j = 0; j < marker.length; j++) {
                if (marker[i][j]) {
                    getField()[i][j] = null;
                    marker[i][j] = false;
                }
            }
        }
    }

    /**
     * checks if available choice pieces can fit in field by calling hasSpace method for every x and y position.
     */
    private boolean gameFieldHasSpace() {

        for (int i = 0; i < getField().length; i++) {
            for (int j = 0; j < getField().length; j++) {
                if (hasSpace(choiceA, i, j) || hasSpace(choiceB, i, j) || hasSpace(choiceC, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * checks whether the piece placement is possible within the gameField and will return false if the piece had to overlap the bounds
     */
    private boolean isInBounds(int index1, int index2) {
        return index1 >= 0 && index2 >= 0 && index1 <= getField().length - 1 && index2 <= getField().length - 1;
    }

    /**
     * checks if indices are in bounds and if the piece to place had to overlap occupied slots on the field in order to be placed
     *
     * @param piece id is needed because of piece's shape
     * @param x     first chosen coordinate
     * @param y     second chosen coordinate
     * @return true if piece can be placed in chosen position
     */
    private boolean hasSpace(Piece piece, int x, int y) {

        if (piece == null) {
            return false;
        }

        int id = piece.getId();

        switch (id) {
            //square:
            case 0:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x][y + 1] == null && getField()[x + 1][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //column2:
            case 1:
                if (isInBounds(x, x + 1) && isInBounds(y, y)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null) {
                        return true;
                    }
                }
                break;
            //column3:
            case 2:

                if (isInBounds(x, x + 2) && isInBounds(y, y)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x + 2][y] == null) {
                        return true;
                    }
                }
                break;
            //wall2:
            case 3:
                if (isInBounds(x, x) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //wall3:
            case 4:
                if (isInBounds(x, x) && isInBounds(y, y + 2)) {
                    if (getField()[x][y] == null && getField()[x][y + 1] == null && getField()[x][y + 2] == null) {
                        return true;
                    }
                }
                break;
            //lLeft:
            case 5:
                if (isInBounds(x, x + 2) && isInBounds(y, y - 1)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x + 2][y] == null && getField()[x + 2][y - 1] == null) {
                        return true;
                    }
                }
                break;
            //lRight:
            case 6:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x + 2][y] == null && getField()[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //lUpLeft:
            case 7:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x][y + 1] == null && getField()[x + 1][y + 1] == null && getField()[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //lUpRight:
            case 8:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x][y + 1] == null && getField()[x + 1][y] == null && getField()[x + 2][y] == null) {
                        return true;
                    }
                }
                break;
            //zLeft:
            case 9:
                if (isInBounds(x, x + 1) && isInBounds(y, y + 2)) {
                    if (getField()[x][y] == null && getField()[x][y + 1] == null && getField()[x + 1][y + 1] == null && getField()[x + 1][y + 2] == null) {
                        return true;
                    }
                }
                break;
            //zRight:
            case 10:
                if (isInBounds(x, x + 1) && isInBounds(y - 1, y + 1)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x][y + 1] == null && getField()[x + 1][y - 1] == null) {
                        return true;
                    }
                }
                break;
            //zUpLeft:
            case 11:
                if (isInBounds(x, x + 2) && isInBounds(y, y + 1)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x + 1][y + 1] == null && getField()[x + 2][y + 1] == null) {
                        return true;
                    }
                }
                break;
            //zUpRight:
            case 12:
                if (isInBounds(x, x + 2) && isInBounds(y - 1, y)) {
                    if (getField()[x][y] == null && getField()[x + 1][y] == null && getField()[x + 1][y - 1] == null && getField()[x + 2][y - 1] == null) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

}
