import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class TetrominoFactory {

    public static Tetromino createTetromino(int pieceType) {
        switch (pieceType) {
            case 0: // I Shape
                return new Tetromino(new Point[]{
                        new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0)
                }, Color.CYAN);
            case 1: // O Shape (does not rotate)
                return new Tetromino(new Point[]{
                        new Point(4, 0), new Point(5, 0), new Point(4, 1), new Point(5, 1)
                }, Color.YELLOW);
            case 2: // T Shape
                return new Tetromino(new Point[]{
                        new Point(4, 0), new Point(3, 1), new Point(4, 1), new Point(5, 1)
                }, Color.MAGENTA);
            case 3: // S Shape
                return new Tetromino(new Point[]{
                        new Point(5, 0), new Point(6, 0), new Point(4, 1), new Point(5, 1)
                }, Color.GREEN);
            case 4: // Z Shape
                return new Tetromino(new Point[]{
                        new Point(4, 0), new Point(5, 0), new Point(5, 1), new Point(6, 1)
                }, Color.RED);
            case 5: // J Shape
                return new Tetromino(new Point[]{
                        new Point(4, 0), new Point(4, 1), new Point(5, 1), new Point(6, 1)
                }, Color.BLUE);
            case 6: // L Shape
                return new Tetromino(new Point[]{
                        new Point(6, 0), new Point(4, 1), new Point(5, 1), new Point(6, 1)
                }, Color.ORANGE);
            default:
                throw new IllegalArgumentException("Invalid piece type: " + pieceType);
        }
    }

    public static Tetromino getRandomTetromino() {
        Random random = new Random();
        int pieceType = random.nextInt(7);
        return createTetromino(pieceType);
    }
}
