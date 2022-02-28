package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

public class Field {

    public static Piece[][] field;

    public static final String ANSI_WHITE = "\u001B[37m";

    public Field(int size) {
        field = new Piece[size][size];
    }

    public static Piece[][] getField() {
        return field;
    }

    /**
     * prints index numbers horizontally and vertically for the player to navigate
     * prints grid like field consisting of □s and ■s while '□' indicates an empty slot on the field
     */
    public void printField() {

        //for formatting reasons
        System.out.print("  ");

        for (int i = 0; i < field.length; i++) {
            System.out.print(i + 1 + " ");
        }

        //coordinate 'x'
        Logger.logLine(ANSI_WHITE, "x");

        for (int i = 0; i < field.length; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == null) {
                    System.out.print("□ ");
                } else {
                    System.out.print("■ ");
                }

            }
            //jump into next line
            Logger.nextLine();
        }
        //coordinate 'y'
        Logger.logLine(ANSI_WHITE, "y");
        Logger.nextLine();
    }

}
