import java.awt.*;
import java.util.Random;

public class Tetromino {
    private Point[] coordinates;
    private Color color;

    public Tetromino(Point[] coords, Color color) {
        this.coordinates = coords;
        this.color = color;
    }

    public Point[] getCoordinates() {
        return coordinates;
    }

    public Color getColor() {
        return color;
    }

    public void moveDown() {
        for (Point p : coordinates) {
            p.y += 1;
        }
    }

    public void moveLeft() {
        for (Point p : coordinates) {
            p.x -= 1;
        }
    }

    public void moveRight() {
        for (Point p : coordinates) {
            p.x += 1;
        }
    }

    // Rotate the Tetromino 90 degrees clockwise
    public void rotate(Color[][] grid) {
        if (color == Color.YELLOW) {
            return; // O shape does not rotate
        }

        // Apply rotation
        for (Point p : coordinates) {
            int temp = p.x;
            p.x = -p.y;
            p.y = temp;
        }

        // Check bounds and apply wall kick if necessary
        applyWallKick(grid);
    }

    private void applyWallKick(Color[][] grid) {
        int gridWidth = grid[0].length;
        int gridHeight = grid.length;

        for (Point p : coordinates) {
            // Check if the piece is outside the left boundary
            if (p.x < 0) {
                shiftRight(-p.x);  // Shift right to bring it into the grid
            }
            // Check if the piece is outside the right boundary
            else if (p.x >= gridWidth) {
                shiftLeft(p.x - (gridWidth - 1));  // Shift left to bring it into the grid
            }
            // Check if the piece is outside the top boundary (which is allowed but can be adjusted if needed)
            if (p.y < 0) {
                shiftDown(-p.y);  // Shift down to bring it into the grid
            }
            // Check if the piece is outside the bottom boundary
            else if (p.y >= gridHeight) {
                shiftUp(p.y - (gridHeight - 1));  // Shift up to bring it into the grid
            }
        }
    }

    private void shiftRight(int steps) {
        for (Point p : coordinates) {
            p.x += steps;
        }
    }

    private void shiftLeft(int steps) {
        for (Point p : coordinates) {
            p.x -= steps;
        }
    }

    private void shiftDown(int steps) {
        for (Point p : coordinates) {
            p.y += steps;
        }
    }

    private void shiftUp(int steps) {
        for (Point p : coordinates) {
            p.y -= steps;
        }
    }


    public static Tetromino getRandomPiece() {
        Random random = new Random();
        int pieceType = random.nextInt(7);  // Randomly select a shape (7 types)

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
                return null;
        }
    }
}
