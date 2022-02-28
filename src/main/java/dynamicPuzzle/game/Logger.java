package dynamicPuzzle.game;

public class Logger {


    public static void logLine(String color, String msg){
        System.out.println(color + msg  + "\u001B[0m"); //Ansi Reset
    }

    public static void log(String color, String msg){
        System.out.print(color + msg  + "\u001B[0m"); //Ansi Reset
    }

    public static void nextLine(){
        System.out.println();
    }
}
