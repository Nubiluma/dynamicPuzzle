package dynamicPuzzle.game;

import dynamicPuzzle.object.Piece;
import dynamicPuzzle.utilities.Color;
import dynamicPuzzle.utilities.Logger;

public class Field {

    public static Piece[][] field;

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
        Logger.log("  ");

        for (int i = 0; i < field.length; i++) {
            Logger.log(i + 1 + " ");
        }

        //coordinate 'x'
        Logger.logLine(Color.WHITE.colorCode, "x");

        for (int i = 0; i < field.length; i++) {
            Logger.log(i + 1 + " ");
            for (int j = 0; j < field.length; j++) {
                if (field[i][j] == null) {
                    Logger.log("□ ");
                } else {
                    Logger.log("■ ");
                }

            }
            //jump into next line
            Logger.nextLine();
        }
        //coordinate 'y'
        Logger.logLine(Color.WHITE.colorCode, "y");
    }

}
