package dynamicPuzzle.utilities;

public enum Color {

    RESET("\u001B[0m"),
    WHITE("\u001B[37m"),
    RED("\u001B[31m"),
    CYAN("\u001B[36m");

    public final String colorCode;

    Color(String colorCode) {
        this.colorCode = colorCode;
    }
}
