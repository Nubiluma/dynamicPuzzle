package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;

public class Field {

    public static Piece[][] field;

    public static final String ANSI_RESET = "\u001B[0m";
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
        System.out.print(ANSI_WHITE + "x" + ANSI_RESET);
        System.out.println();

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
            System.out.println();
        }
        //coordinate 'y'
        System.out.println(ANSI_WHITE + "y" + ANSI_RESET);
        System.out.println();
    }

}
