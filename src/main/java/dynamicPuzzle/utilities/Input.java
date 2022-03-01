package dynamicPuzzle.utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {

    /**
     * Prevents crashing if input is not an Integer
     * @param keyboard user input object
     * @return input value (int)
     */
    public static int inputInt(Scanner keyboard) {
        try {
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            Logger.logLine(Color.RED.colorCode,"Input invalid. You need to input a number!");
            keyboard.next();
            return inputInt(keyboard);
        }
    }
}
