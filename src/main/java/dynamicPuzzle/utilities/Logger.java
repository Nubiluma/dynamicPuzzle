package dynamicPuzzle.utilities;

public class Logger {

    public static void logLine(String msg){
        System.out.println(msg + Color.RESET.colorCode);
    }
    public static void logLine(String color, String msg){
        System.out.println(color + msg + Color.RESET.colorCode);
    }

    public static void log(String color, String msg){
        System.out.print(color + msg  + Color.RESET.colorCode);
    }

    public static void nextLine(){
        System.out.println();
    }
}
