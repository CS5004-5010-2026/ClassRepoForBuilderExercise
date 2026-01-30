package connectn;

import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Property-based tests for ConnectNGame using jqwik.
 * 
 * <p>These tests validate universal properties that should hold across all valid inputs.
 * Each property is tested with 100+ randomized iterations to ensure comprehensive coverage.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class ConnectNGamePropertyTest {
    
    // Feature: connect-n-model-for-builder-exercise, Property 1: Constructor validates positive dimensions
    @Property(tries = 100)
    void constructorValidatesPositiveDimensions(
            @ForAll @IntRange(min = -10, max = 0) int invalidRows,
            @ForAll @IntRange(min = 1, max = 10) int validColumns,
            @ForAll @IntRange(min = 1, max = 10) int validWinLength) {
        
        // Test invalid rows
        assertThrows(IllegalArgumentException.class, 
            () -> new ConnectNGame(invalidRows, validColumns, validWinLength),
            "Constructor should reject non-positive rows");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 1: Constructor validates positive dimensions (columns)
    @Property(tries = 100)
    void constructorValidatesPositiveColumns(
            @ForAll @IntRange(min = 1, max = 10) int validRows,
            @ForAll @IntRange(min = -10, max = 0) int invalidColumns,
            @ForAll @IntRange(min = 1, max = 10) int validWinLength) {
        
        // Test invalid columns
        assertThrows(IllegalArgumentException.class, 
            () -> new ConnectNGame(validRows, invalidColumns, validWinLength),
            "Constructor should reject non-positive columns");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 2: Constructor validates positive win length
    @Property(tries = 100)
    void constructorValidatesPositiveWinLength(
            @ForAll @IntRange(min = 1, max = 10) int validRows,
            @ForAll @IntRange(min = 1, max = 10) int validColumns,
            @ForAll @IntRange(min = -10, max = 0) int invalidWinLength) {
        
        assertThrows(IllegalArgumentException.class, 
            () -> new ConnectNGame(validRows, validColumns, invalidWinLength),
            "Constructor should reject non-positive win length");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 3: Constructor validates win length feasibility
    @Property(tries = 100)
    void constructorValidatesWinLengthFeasibility(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int minDimension = Math.min(rows, columns);
        int invalidWinLength = minDimension + 1;
        
        assertThrows(IllegalArgumentException.class, 
            () -> new ConnectNGame(rows, columns, invalidWinLength),
            "Constructor should reject win length greater than min(rows, columns)");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 4: Invalid parameters throw IllegalArgumentException
    @Property(tries = 100)
    void invalidParametersThrowIllegalArgumentException(
            @ForAll @IntRange(min = -10, max = 10) int rows,
            @ForAll @IntRange(min = -10, max = 10) int columns,
            @ForAll @IntRange(min = -10, max = 10) int winLength) {
        
        // Only test invalid combinations
        boolean isValid = rows > 0 && columns > 0 && winLength > 0 
                         && winLength <= Math.min(rows, columns);
        
        if (!isValid) {
            assertThrows(IllegalArgumentException.class, 
                () -> new ConnectNGame(rows, columns, winLength),
                "Constructor should throw IllegalArgumentException for invalid parameters");
        }
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 5: New games start with empty board and Player 1
    @Property(tries = 100)
    void newGamesStartWithEmptyBoardAndPlayerOne(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Check all positions are empty
        char[][] board = game.getBoard();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(' ', board[i][j], 
                    "All board positions should be empty in a new game");
            }
        }
        
        // Check Player 1 starts
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer(),
            "New games should start with Player 1");
        
        // Check status is IN_PROGRESS
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus(),
            "New games should have status IN_PROGRESS");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 7: Board defensive copy prevents external modification
    @Property(tries = 100)
    void boardDefensiveCopyPreventsExternalModification(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Get board and modify it
        char[][] board = game.getBoard();
        board[0][0] = 'X';
        
        // Get board again and verify it wasn't affected
        char[][] board2 = game.getBoard();
        assertEquals(' ', board2[0][0],
            "Modifying returned board should not affect internal state");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 9: Out-of-bounds positions are invalid
    @Property(tries = 100)
    void outOfBoundsPositionsAreInvalid(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Test negative row
        assertFalse(game.isValidMove(-1, 0), "Negative row should be invalid");
        
        // Test row >= rows
        assertFalse(game.isValidMove(rows, 0), "Row >= rows should be invalid");
        
        // Test negative column
        assertFalse(game.isValidMove(0, -1), "Negative column should be invalid");
        
        // Test column >= columns
        assertFalse(game.isValidMove(0, columns), "Column >= columns should be invalid");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 6: Valid moves update board state
    @Property(tries = 100)
    void validMovesUpdateBoardState(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns,
            @ForAll @IntRange(min = 0, max = 9) int row,
            @ForAll @IntRange(min = 0, max = 9) int col) {
        
        // Only test if position is within bounds
        if (row >= rows || col >= columns) {
            return;
        }
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        Player player = game.getCurrentPlayer();
        game.makeMove(row, col);
        
        char[][] board = game.getBoard();
        assertEquals(player.getToken(), board[row][col],
            "Board should contain the player's token after a valid move");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 8: Valid moves switch players (when game continues)
    @Property(tries = 100)
    void validMovesSwitchPlayers(
            @ForAll @IntRange(min = 2, max = 10) int rows,
            @ForAll @IntRange(min = 2, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Make a move that won't win (in corner)
        Player firstPlayer = game.getCurrentPlayer();
        game.makeMove(0, 0);
        
        // If game is still in progress, player should have switched
        if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
            assertEquals(firstPlayer.getOpponent(), game.getCurrentPlayer(),
                "Player should switch after a move that doesn't end the game");
        }
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 10: Occupied positions are invalid
    @Property(tries = 100)
    void occupiedPositionsAreInvalid(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns,
            @ForAll @IntRange(min = 0, max = 9) int row,
            @ForAll @IntRange(min = 0, max = 9) int col) {
        
        // Only test if position is within bounds
        if (row >= rows || col >= columns) {
            return;
        }
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Make a move
        game.makeMove(row, col);
        
        // Position should now be invalid
        assertFalse(game.isValidMove(row, col),
            "Occupied position should be invalid");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 12: Invalid moves throw IllegalArgumentException
    @Property(tries = 100)
    void invalidMovesThrowIllegalArgumentException(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Test out of bounds
        assertThrows(IllegalArgumentException.class,
            () -> game.makeMove(-1, 0),
            "Out of bounds move should throw IllegalArgumentException");
        
        // Test occupied position
        game.makeMove(0, 0);
        assertThrows(IllegalArgumentException.class,
            () -> game.makeMove(0, 0),
            "Move to occupied position should throw IllegalArgumentException");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 13: Horizontal wins are detected
    @Property(tries = 100)
    void horizontalWinsAreDetected(
            @ForAll @IntRange(min = 3, max = 10) int rows,
            @ForAll @IntRange(min = 3, max = 10) int columns,
            @ForAll @IntRange(min = 0, max = 7) int startRow,
            @ForAll @IntRange(min = 0, max = 7) int startCol) {
        
        int winLength = 3;
        if (winLength > Math.min(rows, columns)) {
            return;
        }
        
        // Check if we have space for a horizontal win
        if (startRow >= rows || startCol + winLength > columns || startRow + 1 >= rows) {
            return;
        }
        
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Player 1 makes horizontal moves
        for (int i = 0; i < winLength; i++) {
            if (game.isGameOver()) break;
            game.makeMove(startRow, startCol + i);
            // Player 2 makes moves in a different row to not interfere
            if (i < winLength - 1 && !game.isGameOver()) {
                game.makeMove(startRow + 1, startCol + i);
            }
        }
        
        // Check if Player 1 won
        if (game.isGameOver()) {
            assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        }
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 14: Vertical wins are detected
    @Property(tries = 100)
    void verticalWinsAreDetected(
            @ForAll @IntRange(min = 3, max = 10) int rows,
            @ForAll @IntRange(min = 3, max = 10) int columns,
            @ForAll @IntRange(min = 0, max = 7) int startRow,
            @ForAll @IntRange(min = 0, max = 7) int startCol) {
        
        int winLength = 3;
        if (winLength > Math.min(rows, columns)) {
            return;
        }
        
        // Check if we have space for a vertical win
        if (startRow + winLength > rows || startCol >= columns || startCol + 1 >= columns) {
            return;
        }
        
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Player 1 makes vertical moves
        for (int i = 0; i < winLength; i++) {
            if (game.isGameOver()) break;
            game.makeMove(startRow + i, startCol);
            // Player 2 makes moves in a different column to not interfere
            if (i < winLength - 1 && !game.isGameOver()) {
                game.makeMove(startRow + i, startCol + 1);
            }
        }
        
        // Check if Player 1 won
        if (game.isGameOver()) {
            assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        }
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 17: Non-winning games have no winner
    @Property(tries = 100)
    void nonWinningGamesHaveNoWinner(
            @ForAll @IntRange(min = 3, max = 10) int rows,
            @ForAll @IntRange(min = 3, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Make a few moves that don't result in a win
        if (rows >= 2 && columns >= 2) {
            game.makeMove(0, 0);
            if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                assertNull(game.getWinner(), "Game in progress should have no winner");
            }
        }
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 18: Full boards without winners are draws
    @Property(tries = 50)
    void fullBoardsWithoutWinnersAreDraws() {
        // Use a 3x3 board with win length 3
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Fill board in a pattern that doesn't create a win
        // X O X
        // O O X
        // X X O
        game.makeMove(0, 0);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(0, 2);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(1, 2);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(2, 0);  // X
        game.makeMove(2, 2);  // O
        game.makeMove(2, 1);  // X - board full
        
        assertEquals(GameStatus.DRAW, game.getGameStatus(), "Full board without winner should be a draw");
        assertNull(game.getWinner(), "Draw should have no winner");
        assertTrue(game.isGameOver(), "Draw should end the game");
    }
    
    // Feature: connect-n-model-for-builder-exercise, Property 19: Reset restores initial state
    @Property(tries = 100)
    void resetRestoresInitialState(
            @ForAll @IntRange(min = 1, max = 10) int rows,
            @ForAll @IntRange(min = 1, max = 10) int columns) {
        
        int winLength = Math.min(rows, columns);
        ConnectNGame game = new ConnectNGame(rows, columns, winLength);
        
        // Make some moves
        if (rows > 0 && columns > 0) {
            game.makeMove(0, 0);
        }
        
        // Reset the game
        game.reset();
        
        // Verify initial state
        char[][] board = game.getBoard();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(' ', board[i][j], "Board should be empty after reset");
            }
        }
        
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer(), "Should reset to Player 1");
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus(), "Should reset to IN_PROGRESS");
        assertNull(game.getWinner(), "Should have no winner after reset");
        assertEquals(rows, game.getRows(), "Rows should be preserved");
        assertEquals(columns, game.getColumns(), "Columns should be preserved");
        assertEquals(winLength, game.getWinLength(), "Win length should be preserved");
    }
}
