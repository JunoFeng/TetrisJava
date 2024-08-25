import java.awt.*;
import java.util.Arrays;
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

    // Rotate the Tetromino 90 degrees clockwise around its center
    public void rotate(Color[][] grid) {
        if (color == Color.YELLOW) {
            return; // O shape does not rotate
        }

        // Backup the current coordinates in case the rotation fails
        Point[] originalCoordinates = Arrays.copyOf(coordinates, coordinates.length);

        // Calculate the center of the Tetromino (average of all points)
        Point center = getCenter();

        // Rotate the Tetromino around its center
        for (Point p : coordinates) {
            int newX = center.x + (p.y - center.y);
            int newY = center.y - (p.x - center.x);
            p.x = newX;
            p.y = newY;
        }

        // Adjust Tetromino to stay within the grid bounds after rotation
        if (!adjustWithinBounds(grid)) {
            // If the adjustment fails (collision), revert to the original coordinates
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] = originalCoordinates[i];
            }
        }
    }

    // Get the center of the Tetromino
    private Point getCenter() {
        int sumX = 0, sumY = 0;
        for (Point p : coordinates) {
            sumX += p.x;
            sumY += p.y;
        }
        return new Point(sumX / coordinates.length, sumY / coordinates.length);
    }

    // Adjust Tetromino to stay within the grid bounds after rotation and apply wall kicks
    private boolean adjustWithinBounds(Color[][] grid) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Find the minimum and maximum X and Y positions of the Tetromino
        for (Point p : coordinates) {
            if (p.x < minX) {
                minX = p.x;
            }
            if (p.x > maxX) {
                maxX = p.x;
            }
            if (p.y < minY) {
                minY = p.y;
            }
            if (p.y > maxY) {
                maxY = p.y;
            }
        }

        // Shift right if out of left bounds
        if (minX < 0) {
            for (Point p : coordinates) {
                p.x -= minX;
            }
        }

        // Shift left if out of right bounds
        if (maxX >= grid[0].length) {
            for (Point p : coordinates) {
                p.x -= (maxX - grid[0].length + 1);
            }
        }

        // Shift down if out of top bounds
        if (minY < 0) {
            for (Point p : coordinates) {
                p.y -= minY;
            }
        }

        // Shift up if out of bottom bounds
        if (maxY >= grid.length) {
            for (Point p : coordinates) {
                p.y -= (maxY - grid.length + 1);
            }
        }

        // Check for collisions with existing blocks on the grid
        for (Point p : coordinates) {
            if (p.y >= 0 && (p.x < 0 || p.x >= grid[0].length || grid[p.y][p.x] != null)) {
                return false;  // Collision detected, invalid rotation
            }
        }

        return true;  // Valid rotation
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
