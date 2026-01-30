package connectn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConnectNGame.
 * 
 * <p>These tests validate specific examples and edge cases for the Connect-N game model.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class ConnectNGameTest {
    
    // ========== Constructor Tests ==========
    
    @Test
    void testValidConstructor3x3() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertEquals(3, game.getRows());
        assertEquals(3, game.getColumns());
        assertEquals(3, game.getWinLength());
    }
    
    @Test
    void testValidConstructor6x7() {
        ConnectNGame game = new ConnectNGame(6, 7, 4);
        assertEquals(6, game.getRows());
        assertEquals(7, game.getColumns());
        assertEquals(4, game.getWinLength());
    }
    
    @Test
    void testValidConstructor1x1() {
        ConnectNGame game = new ConnectNGame(1, 1, 1);
        assertEquals(1, game.getRows());
        assertEquals(1, game.getColumns());
        assertEquals(1, game.getWinLength());
    }
    
    @Test
    void testValidConstructor10x10() {
        ConnectNGame game = new ConnectNGame(10, 10, 5);
        assertEquals(10, game.getRows());
        assertEquals(10, game.getColumns());
        assertEquals(5, game.getWinLength());
    }
    
    @Test
    void testInvalidRowsZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(0, 3, 3)
        );
        assertTrue(exception.getMessage().contains("Rows must be positive"));
    }
    
    @Test
    void testInvalidRowsNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(-1, 3, 3)
        );
        assertTrue(exception.getMessage().contains("Rows must be positive"));
    }
    
    @Test
    void testInvalidColumnsZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(3, 0, 3)
        );
        assertTrue(exception.getMessage().contains("Columns must be positive"));
    }
    
    @Test
    void testInvalidColumnsNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(3, -1, 3)
        );
        assertTrue(exception.getMessage().contains("Columns must be positive"));
    }
    
    @Test
    void testInvalidWinLengthZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(3, 3, 0)
        );
        assertTrue(exception.getMessage().contains("Win length must be positive"));
    }
    
    @Test
    void testInvalidWinLengthNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(3, 3, -1)
        );
        assertTrue(exception.getMessage().contains("Win length must be positive"));
    }
    
    @Test
    void testWinLengthTooLarge() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ConnectNGame(3, 5, 4)
        );
        assertTrue(exception.getMessage().contains("cannot exceed min(rows, columns)"));
    }
    
    @Test
    void testWinLengthExactlyMinDimension() {
        // Should succeed when win length equals min dimension
        ConnectNGame game = new ConnectNGame(3, 5, 3);
        assertEquals(3, game.getWinLength());
    }
    
    // ========== Initial State Tests ==========
    
    @Test
    void testInitialBoardEmpty() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        char[][] board = game.getBoard();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(' ', board[i][j], 
                    "Board position (" + i + ", " + j + ") should be empty");
            }
        }
    }
    
    @Test
    void testInitialPlayerIsPlayerOne() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer());
    }
    
    @Test
    void testInitialStatusInProgress() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    }
    
    @Test
    void testInitialWinnerIsNull() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertNull(game.getWinner());
    }
    
    // ========== Defensive Copy Tests ==========
    
    @Test
    void testGetBoardReturnsDefensiveCopy() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        char[][] board1 = game.getBoard();
        char[][] board2 = game.getBoard();
        
        // Should be different array instances
        assertNotSame(board1, board2, "getBoard() should return a new array each time");
    }
    
    @Test
    void testModifyingReturnedBoardDoesNotAffectGame() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        char[][] board = game.getBoard();
        
        // Modify the returned board
        board[0][0] = 'X';
        board[1][1] = 'O';
        
        // Get board again and verify it's still empty
        char[][] board2 = game.getBoard();
        assertEquals(' ', board2[0][0], "External modification should not affect internal state");
        assertEquals(' ', board2[1][1], "External modification should not affect internal state");
    }
    
    // ========== Move Validation Tests ==========
    
    @Test
    void testValidMoveReturnsTrue() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertTrue(game.isValidMove(0, 0), "Empty position should be valid");
        assertTrue(game.isValidMove(1, 1), "Empty position should be valid");
        assertTrue(game.isValidMove(2, 2), "Empty position should be valid");
    }
    
    @Test
    void testOutOfBoundsRowNegative() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertFalse(game.isValidMove(-1, 0), "Negative row should be invalid");
    }
    
    @Test
    void testOutOfBoundsRowTooLarge() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertFalse(game.isValidMove(3, 0), "Row >= rows should be invalid");
    }
    
    @Test
    void testOutOfBoundsColumnNegative() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertFalse(game.isValidMove(0, -1), "Negative column should be invalid");
    }
    
    @Test
    void testOutOfBoundsColumnTooLarge() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        assertFalse(game.isValidMove(0, 3), "Column >= columns should be invalid");
    }
    
    // ========== Move Execution Tests ==========
    
    @Test
    void testValidMovePlacesToken() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        Player player = game.getCurrentPlayer();
        
        game.makeMove(0, 0);
        
        char[][] board = game.getBoard();
        assertEquals(player.getToken(), board[0][0], "Token should be placed at specified position");
    }
    
    @Test
    void testPlayerSwitchesAfterMove() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        Player firstPlayer = game.getCurrentPlayer();
        
        game.makeMove(0, 0);
        
        assertEquals(firstPlayer.getOpponent(), game.getCurrentPlayer(), 
            "Player should switch after a move");
    }
    
    @Test
    void testInvalidMoveThrowsException() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        assertThrows(IllegalArgumentException.class, 
            () -> game.makeMove(-1, 0),
            "Out of bounds move should throw exception");
    }
    
    @Test
    void testOccupiedPositionThrowsException() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        game.makeMove(0, 0);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> game.makeMove(0, 0)
        );
        assertTrue(exception.getMessage().contains("already occupied"),
            "Exception message should indicate position is occupied");
    }
    
    @Test
    void testExceptionMessagesAreDescriptive() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Test out of bounds message
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> game.makeMove(5, 5)
        );
        assertTrue(exception1.getMessage().contains("out of bounds"),
            "Exception should describe out of bounds error");
        
        // Test occupied position message
        game.makeMove(1, 1);
        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> game.makeMove(1, 1)
        );
        assertTrue(exception2.getMessage().contains("occupied"),
            "Exception should describe occupied position error");
    }
    
    // ========== Win Detection Tests ==========
    
    @Test
    void testHorizontalWinTopRow() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (0,0), (0,1), (0,2) - horizontal win
        game.makeMove(0, 0);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(0, 1);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(0, 2);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        assertEquals(GameStatus.PLAYER_ONE_WON, game.getGameStatus());
    }
    
    @Test
    void testHorizontalWinMiddleRow() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (1,0), (1,1), (1,2) - horizontal win
        game.makeMove(1, 0);  // X
        game.makeMove(0, 0);  // O
        game.makeMove(1, 1);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(1, 2);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
    }
    
    @Test
    void testVerticalWinLeftColumn() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (0,0), (1,0), (2,0) - vertical win
        game.makeMove(0, 0);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(1, 0);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(2, 0);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        assertEquals(GameStatus.PLAYER_ONE_WON, game.getGameStatus());
    }
    
    @Test
    void testVerticalWinRightColumn() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 2: (0,2), (1,2), (2,2) - vertical win
        game.makeMove(0, 0);  // X
        game.makeMove(0, 2);  // O
        game.makeMove(1, 0);  // X
        game.makeMove(1, 2);  // O
        game.makeMove(0, 1);  // X
        game.makeMove(2, 2);  // O - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_TWO, game.getWinner(), "Player 2 should win");
        assertEquals(GameStatus.PLAYER_TWO_WON, game.getGameStatus());
    }
    
    @Test
    void testDiagonalWinTopLeftToBottomRight() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (0,0), (1,1), (2,2) - diagonal win ↘
        game.makeMove(0, 0);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(1, 1);  // X
        game.makeMove(0, 2);  // O
        game.makeMove(2, 2);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        assertEquals(GameStatus.PLAYER_ONE_WON, game.getGameStatus());
    }
    
    @Test
    void testAntiDiagonalWinTopRightToBottomLeft() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (0,2), (1,1), (2,0) - anti-diagonal win ↙
        game.makeMove(0, 2);  // X
        game.makeMove(0, 0);  // O
        game.makeMove(1, 1);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(2, 0);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
        assertEquals(GameStatus.PLAYER_ONE_WON, game.getGameStatus());
    }
    
    @Test
    void testNearWinHorizontalTwoInRow() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1: (0,0), (0,1) - only 2 in a row, not a win
        game.makeMove(0, 0);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(0, 1);  // X
        
        assertFalse(game.isGameOver(), "Game should not be over with only 2 in a row");
        assertNull(game.getWinner(), "Should be no winner yet");
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    }
    
    @Test
    void testNoWinnerWhenNoWinExists() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Make some moves without winning
        game.makeMove(0, 0);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(2, 2);  // X
        
        assertNull(game.getWinner(), "Should be no winner");
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    }
    
    @Test
    void testWinOnLargerBoard() {
        ConnectNGame game = new ConnectNGame(6, 7, 4);
        
        // Player 1 wins with 4 in a row horizontally
        game.makeMove(0, 0);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(0, 1);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(0, 2);  // X
        game.makeMove(1, 2);  // O
        game.makeMove(0, 3);  // X - wins!
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(Player.PLAYER_ONE, game.getWinner(), "Player 1 should win");
    }
    
    // ========== Draw Detection Tests ==========
    
    @Test
    void testDrawDetection() {
        // 3x3 board with win length 3
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
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
        game.makeMove(2, 1);  // X - board full, no winner
        
        assertTrue(game.isGameOver(), "Game should be over");
        assertEquals(GameStatus.DRAW, game.getGameStatus(), "Should be a draw");
        assertNull(game.getWinner(), "Draw should have no winner");
    }
    
    @Test
    void testFullBoardNoWinner() {
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
        
        assertEquals(GameStatus.DRAW, game.getGameStatus(), "Should be a draw");
        assertNull(game.getWinner(), "Draw should have no winner");
    }
    
    @Test
    void testNoMovesAfterDraw() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Create a draw
        game.makeMove(0, 0);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(0, 2);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(1, 2);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(2, 0);  // X
        game.makeMove(2, 2);  // O
        game.makeMove(2, 1);  // X - Draw
        
        assertFalse(game.isValidMove(0, 0), "No moves should be valid after draw");
        assertThrows(IllegalArgumentException.class,
            () -> game.makeMove(0, 0),
            "Should not allow moves after draw");
    }
    
    // ========== Reset Tests ==========
    
    @Test
    void testResetClearsBoard() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        game.makeMove(0, 0);
        game.makeMove(1, 1);
        game.makeMove(2, 2);
        
        game.reset();
        
        char[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(' ', board[i][j], "Board should be empty after reset");
            }
        }
    }
    
    @Test
    void testResetAfterWin() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Player 1 wins
        game.makeMove(0, 0);
        game.makeMove(1, 0);
        game.makeMove(0, 1);
        game.makeMove(1, 1);
        game.makeMove(0, 2);
        
        game.reset();
        
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer(), "Should reset to Player 1");
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus(), "Should reset to IN_PROGRESS");
        assertNull(game.getWinner(), "Should have no winner after reset");
    }
    
    @Test
    void testResetAfterDraw() {
        ConnectNGame game = new ConnectNGame(3, 3, 3);
        
        // Create a draw
        game.makeMove(0, 0);  // X
        game.makeMove(0, 1);  // O
        game.makeMove(0, 2);  // X
        game.makeMove(1, 0);  // O
        game.makeMove(1, 2);  // X
        game.makeMove(1, 1);  // O
        game.makeMove(2, 0);  // X
        game.makeMove(2, 2);  // O
        game.makeMove(2, 1);  // X - Draw
        
        game.reset();
        
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus(), "Should reset to IN_PROGRESS");
        assertNull(game.getWinner(), "Should have no winner after reset");
        assertTrue(game.isValidMove(0, 0), "Moves should be valid after reset");
    }
    
    @Test
    void testResetPreservesConfiguration() {
        ConnectNGame game = new ConnectNGame(6, 7, 4);
        
        game.makeMove(0, 0);
        game.reset();
        
        assertEquals(6, game.getRows(), "Rows should be preserved");
        assertEquals(7, game.getColumns(), "Columns should be preserved");
        assertEquals(4, game.getWinLength(), "Win length should be preserved");
    }
}
