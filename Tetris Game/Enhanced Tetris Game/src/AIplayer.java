import java.awt.*;
import java.util.Arrays;

public class AIplayer {
    private final GameState gameState;
    private final GameBoard gameBoard;

    public AIplayer(GameState gameState, GameBoard gameBoard) {
        this.gameState = gameState;
        this.gameBoard = gameBoard;
    }

    // Method to make the best possible move
    public void makeBestMove() {
        Tetromino bestPiece = null;
        int bestX = 0;
        int bestRotation = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        Tetromino originalPiece = gameState.getCurrentPiece();
        Color[][] grid = gameBoard.getGrid();

        // Loop through all rotations and column placements
        for (int rotation = 0; rotation < 4; rotation++) {
            Tetromino rotatedPiece = simulateRotation(originalPiece, rotation);

            for (int x = -getMinX(rotatedPiece); x <= gameBoard.getBoardWidth() - getMaxX(rotatedPiece); x++) {
                if (x < 0 || x >= gameBoard.getBoardWidth()) {
                    continue; // Skip invalid x positions
                }

                // Use isValidPlacement to ensure the piece can be placed at this x
                if (!isValidPlacement(rotatedPiece, x, grid)) {
                    continue; // Skip if the placement is not valid
                }

                // Proceed with placing and evaluating the piece
                Tetromino placedPiece = copyPiece(rotatedPiece);
                movePieceToX(placedPiece, x);

                while (canMoveDown(placedPiece, grid)) {
                    placedPiece.moveDown();
                }

                double score = evaluateBoard(placedPiece, grid);
                if (score > bestScore) {
                    bestScore = score;
                    bestPiece = placedPiece;
                    bestX = x;
                    bestRotation = rotation;
                }
            }
        }

        // Apply the best move (rotate and move piece)
        if (bestPiece != null) {
            for (int i = 0; i < bestRotation; i++) {
                gameState.rotate();  // Rotate to the best orientation
            }
            gameState.movePieceTo(bestX);  // Move to the best position
        }
    }

    // Heuristic evaluation of the board state after placing a piece
    private double evaluateBoard(Tetromino piece, Color[][] grid) {
        Color[][] tempGrid = cloneGrid(grid);
        settlePiece(piece, tempGrid);

        // Calculate evaluation score based on heuristics
        int aggregateHeight = getAggregateHeight(tempGrid);
        int linesCleared = getLinesCleared(tempGrid);
        int holes = getHoles(tempGrid);
        int bumpiness = getBumpiness(tempGrid);

        // Define weights for the evaluation function
        double aggregateHeightWeight = -0.50;
        double linesClearedWeight = 1.0;  // Increase weight for clearing lines
        double holesWeight = -0.70;       // Increase penalty for holes
        double bumpinessWeight = -0.20;

        return (aggregateHeightWeight * aggregateHeight) +
                (linesClearedWeight * linesCleared) +
                (holesWeight * holes) +
                (bumpinessWeight * bumpiness);
    }

    // Simulate rotating the Tetromino
    private Tetromino simulateRotation(Tetromino piece, int times) {
        Tetromino rotatedPiece = copyPiece(piece);
        for (int i = 0; i < times; i++) {
            rotatedPiece.rotate(gameBoard.getGrid());
        }
        return rotatedPiece;
    }

    // Method to check if the placement is valid within the grid
    private boolean isValidPlacement(Tetromino piece, int x, Color[][] grid) {
        for (Point p : piece.getCoordinates()) {
            int newX = p.x + x;
            int newY = p.y;

            // Ensure the new coordinates are within bounds
            if (newX < 0 || newX >= grid[0].length || newY < 0 || newY >= grid.length) {
                System.out.println("Invalid placement: (" + newX + ", " + newY + ")");
                return false; // Invalid position
            }
            if (newY >= 0 && grid[newY][newX] != null) {
                return false; // Collision detected
            }
        }
        return true;
    }

    // Clone grid for simulation
    private Color[][] cloneGrid(Color[][] grid) {
        Color[][] newGrid = new Color[grid.length][grid[0].length];
        for (int y = 0; y < grid.length; y++) {
            System.arraycopy(grid[y], 0, newGrid[y], 0, grid[0].length);
        }
        return newGrid;
    }

    // Settle the piece into the grid
    private void settlePiece(Tetromino piece, Color[][] grid) {
        for (Point p : piece.getCoordinates()) {
            if (p.x >= 0 && p.x < grid[0].length && p.y >= 0 && p.y < grid.length) {
                grid[p.y][p.x] = piece.getColor();
            } else {
                System.out.println("Error: Trying to settle Tetromino out of bounds at (" + p.x + ", " + p.y + ")");
            }
        }
    }

    // Helper function to copy a Tetromino
    private Tetromino copyPiece(Tetromino piece) {
        Point[] newCoords = Arrays.copyOf(piece.getCoordinates(), piece.getCoordinates().length);
        for (int i = 0; i < newCoords.length; i++) {
            newCoords[i] = new Point(newCoords[i].x, newCoords[i].y);
        }
        return new Tetromino(newCoords, piece.getColor());
    }

    // Move Tetromino to a specific X position
    private void movePieceToX(Tetromino piece, int x) {
        int deltaX = x - piece.getCoordinates()[0].x;
        for (int i = 0; i < Math.abs(deltaX); i++) {
            if (deltaX > 0) {
                piece.moveRight();
            } else {
                piece.moveLeft();
            }
        }
    }

    // Check if Tetromino can move down without exceeding the bounds
    private boolean canMoveDown(Tetromino piece, Color[][] grid) {
        for (Point p : piece.getCoordinates()) {
            int newY = p.y + 1;
            System.out.println("Trying to move Tetromino down to (" + p.x + ", " + newY + ")");
            if (newY >= grid.length || (newY >= 0 && (p.x < 0 || p.x >= grid[0].length) || grid[newY][p.x] != null)) {
                return false;
            }
        }
        return true;
    }

    // Calculate Aggregate Height
    private int getAggregateHeight(Color[][] grid) {
        int totalHeight = 0;
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] != null) {
                    totalHeight += (grid.length - y);
                    break;
                }
            }
        }
        return totalHeight;
    }

    // Get the number of lines cleared
    private int getLinesCleared(Color[][] grid) {
        int cleared = 0;
        for (int y = 0; y < grid.length; y++) {
            boolean fullRow = true;
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == null) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                cleared++;
            }
        }
        return cleared;
    }

    // Get the number of holes in the grid
    private int getHoles(Color[][] grid) {
        int holes = 0;
        for (int x = 0; x < grid[0].length; x++) {
            boolean blockFound = false;
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] != null) {
                    blockFound = true;
                } else if (blockFound) {
                    holes++;
                }
            }
        }
        return holes;
    }

    // Calculate bumpiness of the grid
    private int getBumpiness(Color[][] grid) {
        int bumpiness = 0;
        int[] heights = new int[grid[0].length];
        for (int x = 0; x < grid[0].length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if (grid[y][x] != null) {
                    heights[x] = grid.length - y;
                    break;
                }
            }
        }
        for (int x = 0; x < heights.length - 1; x++) {
            bumpiness += Math.abs(heights[x] - heights[x + 1]);
        }
        return bumpiness;
    }

    // Get min and max X values to control movement range
    private int getMinX(Tetromino piece) {
        return Arrays.stream(piece.getCoordinates()).mapToInt(p -> p.x).min().orElse(0);
    }

    private int getMaxX(Tetromino piece) {
        return Arrays.stream(piece.getCoordinates()).mapToInt(p -> p.x).max().orElse(0);
    }
}
