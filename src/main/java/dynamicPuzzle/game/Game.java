package dynamicPuzzle.game;

public class Game {

    private Field gameField;
    private final int size; //for field's size to match game size

    public Game(int size){
        this.size = size;
        init();
    }

    private void init() {
        gameField = new Field(size);
        gameField.printField();
    }
}
