package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;
import dynamicPuzzle.utilities.Color;
import dynamicPuzzle.utilities.Input;
import dynamicPuzzle.utilities.Logger;

import static dynamicPuzzle.game.Field.getField;

import java.util.*;

public class Game {

    private final Field gameField;
    final int size;
    private final boolean[][] marker;
    private boolean running;
    private int score = 0;

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
        placeObstacles();
        tick();
    }

    /**
     * "Game loop" will run until 'running' is set to false when loosing
     */
    private void tick() {

        while (running) {
            printScore();
            gameField.printField();
            printChoices();
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

    private void printScore() {
        Logger.logLine(Color.YELLOW.colorCode, ">> Score: " + score + " <<");
        Logger.nextLine();
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

    }

    /**
     * this method will display the choice pieces for the player when choosing turn comes
     * '◪' represents anchor point of the piece and will not be shown on the grid when piece is placed
     */
    private void printChoices() {

        updateChoices();

        List<Piece> choices = Arrays.asList(choiceA, choiceB, choiceC);

        int lengthI = 11;
        int lengthJ = 3;
        int index = 0;
        int j = 0;

        boolean[][] isAnchor = new boolean[lengthI][lengthJ];
        Piece[][] choicePieces = new Piece[lengthI][lengthJ];

        for (int i = 0; i < lengthI; i += 4) {

            if (choices.get(index) != null) {

                switch (choices.get(index).getId()) {
                    case 0 -> { //square
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                    }
                    case 1 -> { //column2
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i][j + 1] = piece;
                    }
                    case 2 -> { //column3
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i][j + 2] = piece;
                    }
                    case 3 -> { //wall2
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i + 1][j] = piece;
                    }
                    case 4 -> { //wall3
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 2][j] = piece;
                    }
                    case 5 -> { //lLeft
                        isAnchor[i + 1][j] = true;

                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i][j + 2] = piece;
                    }
                    case 6 -> { //lRight
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i][j + 2] = piece;
                        choicePieces[i + 1][j + 2] = piece;
                    }
                    case 7 -> { //lUpLeft
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i + 1][j + 2] = piece;
                    }
                    case 8 -> { //lUpRight
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i + 1][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i][j + 2] = piece;
                    }
                    case 9 -> { //zLeft
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i + 2][j + 1] = piece;
                    }
                    case 10 -> { //zRight
                        isAnchor[i + 1][j] = true;

                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 2][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                    }
                    case 11 -> { //zUpLeft
                        isAnchor[i][j] = true;

                        choicePieces[i][j] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i + 1][j + 2] = piece;
                    }
                    case 12 -> { //zUpRight
                        isAnchor[i + 1][j] = true;

                        choicePieces[i + 1][j] = piece;
                        choicePieces[i + 1][j + 1] = piece;
                        choicePieces[i][j + 1] = piece;
                        choicePieces[i][j + 2] = piece;
                    }
                }

            }

            index++;
        }

        Logger.nextLine();
        Logger.logLine("1:      2:      3:");

        for (j = 0; j < lengthJ; j++) {

            for (int i = 0; i < lengthI; i++) {
                if (choicePieces[i][j] != null && isAnchor[i][j]) {
                    Logger.log("◪ ");
                } else if (choicePieces[i][j] != null) {
                    Logger.log("■ ");
                } else if (choicePieces[i][j] == null) {
                    Logger.log("  ");
                }

            }
            Logger.nextLine();
        }

        Logger.nextLine();
    }

    /**
     * Lets the player pick one of the three choice pieces by demanding user input through scanner. Will fail if player inputs an integer value other than 1, 2 or 3
     * or if the choice piece is not available (null).
     *
     * @return chosen piece
     */
    private Piece choose() {

        Logger.log(Color.PURPLE.colorCode, "Choose between 1 and 3");
        Logger.log(Color.WHITE.colorCode, " (or enter 0 to exit the game): ");

        int input = Input.inputInt(scanner);
        Logger.nextLine();

        if (input == 0) {
            Logger.logLine(Color.CYAN.colorCode, "You chose to exit the game");
            running = false;
            return null;
        }

        if (input == 1 && choiceA != null) {
            Logger.logLine(Color.GREEN.colorCode, ">>You chose 1<<");
            currentPiece = choiceA;
            choiceA = null;
            return currentPiece;
        } else if (input == 1) {
            Logger.logLine(Color.RED.colorCode, "This choice piece is not available right now!");
        }
        if (input == 2 && choiceB != null) {
            Logger.logLine(Color.GREEN.colorCode, ">>You chose 2<<");
            currentPiece = choiceB;
            choiceB = null;
            return currentPiece;
        } else if (input == 2) {
            Logger.logLine(Color.RED.colorCode, "This choice piece is not available right now!");
        }
        if (input == 3 && choiceC != null) {
            Logger.logLine(Color.GREEN.colorCode, ">>You chose 3<<");
            currentPiece = choiceC;
            choiceC = null;
            return currentPiece;
        } else if (input == 3) {
            Logger.logLine(Color.RED.colorCode, "This choice piece is not available right now!");
        }

        Logger.logLine(Color.RED.colorCode, "You need to choose between 1 and 3!");
        Logger.nextLine();
        return choose();

    }

    /**
     * 'Places' pieces on gameField by putting piece objects into array's indices.
     */
    private void place() {

        if (!running) {
            return;
        }

        Logger.logLine(Color.PURPLE.colorCode, "Choose X and Y each from 1 to " + (size) + "!");

        //x's and y's values will be reduced by 1 on order to be placed correctly.
        //This is necessary because of how the field's coordinates are displayed for the player, beginning by 1 instead of 0 (for optical reasons)
        //while the array's indices start with 0
        Logger.logLine(Color.WHITE.colorCode, "('◪' is the anchor for X and Y position)");
        Logger.nextLine();
        Logger.log(Color.PURPLE.colorCode, "X position: ");

        int y = Input.inputInt(scanner) - 1;

        Logger.log(Color.PURPLE.colorCode, "Y position: ");
        int x = Input.inputInt(scanner) - 1;

        //when picking a choice piece in the choose method, the currentPiece object will point at the choice piece's id which is used here
        int id = currentPiece.getId();

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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
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
                        Logger.logLine(Color.RED.colorCode, "Not enough space!");
                        place();
                    }
                } else {
                    Logger.logLine(Color.RED.colorCode, "Out of bounds or not enough space!");
                    place();
                }
                break;
        }

        //'clear' currentPiece
        currentPiece = null;

        updateChoices();
    }

    private void checkForLose() {

        if (!gameFieldHasSpace() && running) {
            running = false;
            Logger.nextLine();
            gameField.printField();
            Logger.logLine(Color.RED.colorCode, "Game over!");
            Logger.logLine(Color.YELLOW.colorCode, "Final score: " + score + " points!");
        } else if (!running) {
            Logger.logLine(Color.YELLOW.colorCode, "Final score: " + score + " points!");
        }
    }

    /**
     * Counting methods commit amount of full rows and columns.
     * The more rows/columns are counted at once, the higher the score (through Math.pow method)
     */
    private void evaluate() {
        if (!running) {
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
