import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Point;
import java.awt.Color;

public class SharedTetrominoGenerator {
    private final List<Tetromino> sequence = new ArrayList<>();
    private int currentIndex = 0;

    public SharedTetrominoGenerator() {
        generateTetrominoSequence();
    }

    private void generateTetrominoSequence() {
        Random random = new Random(1234); // Use a fixed seed for consistency
        for (int i = 0; i < 1000; i++) {
            int pieceType = random.nextInt(7); // Generate a random piece type (0 to 6)
            sequence.add(createTetrominoByType(pieceType)); // Use helper method to create Tetromino
        }
    }

    public Tetromino getNextTetromino() {
        if (currentIndex >= sequence.size()) {
            generateTetrominoSequence(); // Regenerate if running out of pieces
        }
        return sequence.get(currentIndex++);
    }

    // Helper method to create a Tetromino by its type
    private Tetromino createTetrominoByType(int pieceType) {
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
}
