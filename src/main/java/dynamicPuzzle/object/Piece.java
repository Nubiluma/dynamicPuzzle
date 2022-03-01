package dynamicPuzzle.object;

public class Piece {

    public static Piece square = new Piece(0);
    public static Piece column2 = new Piece(1);
    public static Piece column3 = new Piece(2);
    public static Piece wall2 = new Piece(3);
    public static Piece wall3 = new Piece(4);
    public static Piece lLeft = new Piece(5);
    public static Piece lRight = new Piece(6);
    public static Piece lUpLeft = new Piece(7);
    public static Piece lUpRight = new Piece(8);
    public static Piece zLeft = new Piece(9);
    public static Piece zRight = new Piece(10);
    public static Piece zUpLeft = new Piece(11);
    public static Piece zUpRight = new Piece(12);

    private final int id;

    public Piece(int id) {
        this.id = id;
    }

    /* (this method was replaced by printChoicePieces method!)

    public void printPiece(Piece piece) {

        if (piece.getId() == square.getId()) {
            System.out.println("◪ ■");
            System.out.println("■ ■");
            System.out.println();
        } else if (piece.getId() == column2.getId()) {
            System.out.println("◪");
            System.out.println("■");
            System.out.println();
        } else if (piece.getId() == column3.getId()) {
            System.out.println("◪");
            System.out.println("■");
            System.out.println("■");
            System.out.println();
        } else if (piece.getId() == wall2.getId()) {
            System.out.println("◪ ■");
            System.out.println();
        } else if (piece.getId() == wall3.getId()) {
            System.out.println("◪ ■ ■");
            System.out.println();
        } else if (piece.getId() == lLeft.getId()) {
            System.out.println("  ◪");
            System.out.println("  ■");
            System.out.println("■ ■");
            System.out.println();
        } else if (piece.getId() == lRight.getId()) {
            System.out.println("◪");
            System.out.println("■");
            System.out.println("■ ■");
            System.out.println();
        } else if (piece.getId() == lUpLeft.getId()) {
            System.out.println("◪ ■");
            System.out.println("  ■");
            System.out.println("  ■");
            System.out.println();
        } else if (piece.getId() == lUpRight.getId()) {
            System.out.println("◪ ■");
            System.out.println("■");
            System.out.println("■");
            System.out.println();
        } else if (piece.getId() == zLeft.getId()) {
            System.out.println("◪ ■");
            System.out.println("  ■ ■");
            System.out.println();
        } else if (piece.getId() == zRight.getId()) {
            System.out.println("  ◪ ■");
            System.out.println("■ ■");
            System.out.println();
        } else if (piece.getId() == zUpLeft.getId()) {
            System.out.println("◪");
            System.out.println("■ ■");
            System.out.println("  ■");
            System.out.println();
        } else if (piece.getId() == zUpRight.getId()) {
            System.out.println("  ◪");
            System.out.println("■ ■");
            System.out.println("■");
            System.out.println();
        }

    }
    */

    public int getId() {
        return id;
    }

}
