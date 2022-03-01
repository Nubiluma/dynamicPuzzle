package dynamicPuzzle.utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {

    public static int inputInt(Scanner keyboard) {
        try {
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            Logger.logLine("Input invalid. You need to input a number!");
            keyboard.next();
            return inputInt(keyboard);
        }
    }
}
