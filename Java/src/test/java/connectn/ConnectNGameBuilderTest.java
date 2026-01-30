package connectn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConnectNGameBuilder.
 * 
 * <p>Tests verify that the builder correctly creates games with various configurations,
 * including custom settings, defaults, and preset factory methods.
 */
class ConnectNGameBuilderTest {
    
    // ==================== Basic Builder Tests ====================
    
    @Test
    void testBuilderWithAllParameters() {
        ConnectNGame game = new ConnectNGameBuilder()
            .rows(6)
            .columns(7)
            .winLength(4)
            .build();
        
        assertEquals(6, game.getRows());
        assertEquals(7, game.getColumns());
        assertEquals(4, game.getWinLength());
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    }
    
    @Test
    void testBuilderWithDefaults() {
        ConnectNGame game = new ConnectNGameBuilder().build();
        
        assertEquals(3, game.getRows());
        assertEquals(3, game.getColumns());
        assertEquals(3, game.getWinLength());
    }
    
    @Test
    void testBuilderMethodChaining() {
        // Verify that all methods return the builder for chaining
        ConnectNGameBuilder builder = new ConnectNGameBuilder();
        
        assertSame(builder, builder.rows(5));
        assertSame(builder, builder.columns(6));
        assertSame(builder, builder.winLength(4));
    }
    
    @Test
    void testBuilderPartialConfiguration() {
        // Set only rows, use defaults for others
        ConnectNGame game1 = new ConnectNGameBuilder()
            .rows(5)
            .build();
        
        assertEquals(5, game1.getRows());
        assertEquals(3, game1.getColumns());  // Default
        assertEquals(3, game1.getWinLength()); // Default
        
        // Set only columns
        ConnectNGame game2 = new ConnectNGameBuilder()
            .columns(7)
            .build();
        
        assertEquals(3, game2.getRows());      // Default
        assertEquals(7, game2.getColumns());
        assertEquals(3, game2.getWinLength()); // Default
    }
    
    // ==================== Convenience Method Tests ====================
    
    @Test
    void testBoardSizeMethod() {
        ConnectNGame game = new ConnectNGameBuilder()
            .boardSize(8, 9)
            .winLength(5)
            .build();
        
        assertEquals(8, game.getRows());
        assertEquals(9, game.getColumns());
        assertEquals(5, game.getWinLength());
    }
    
    @Test
    void testSquareBoardMethod() {
        ConnectNGame game = new ConnectNGameBuilder()
            .squareBoard(7)
            .winLength(4)
            .build();
        
        assertEquals(7, game.getRows());
        assertEquals(7, game.getColumns());
        assertEquals(4, game.getWinLength());
    }
    
    // ==================== Preset Factory Method Tests ====================
    
    @Test
    void testTicTacToePreset() {
        ConnectNGame game = ConnectNGameBuilder.ticTacToe();
        
        assertEquals(3, game.getRows());
        assertEquals(3, game.getColumns());
        assertEquals(3, game.getWinLength());
        assertEquals(Player.PLAYER_ONE, game.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, game.getGameStatus());
    }
    
    @Test
    void testConnectFourPreset() {
        ConnectNGame game = ConnectNGameBuilder.connectFour();
        
        assertEquals(6, game.getRows());
        assertEquals(7, game.getColumns());
        assertEquals(4, game.getWinLength());
    }
    
    @Test
    void testGomokuPreset() {
        ConnectNGame game = ConnectNGameBuilder.gomoku();
        
        assertEquals(15, game.getRows());
        assertEquals(15, game.getColumns());
        assertEquals(5, game.getWinLength());
    }
    
    @Test
    void testSmallGamePreset() {
        ConnectNGame game = ConnectNGameBuilder.smallGame();
        
        assertEquals(5, game.getRows());
        assertEquals(5, game.getColumns());
        assertEquals(4, game.getWinLength());
    }
    
    @Test
    void testLargeGamePreset() {
        ConnectNGame game = ConnectNGameBuilder.largeGame();
        
        assertEquals(10, game.getRows());
        assertEquals(10, game.getColumns());
        assertEquals(5, game.getWinLength());
    }
    
    @Test
    void testCustomSquarePreset() {
        ConnectNGame game = ConnectNGameBuilder.customSquare(8, 5);
        
        assertEquals(8, game.getRows());
        assertEquals(8, game.getColumns());
        assertEquals(5, game.getWinLength());
    }
    
    // ==================== Validation Tests ====================
    
    @Test
    void testBuilderValidatesInvalidRows() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(0)
            .columns(5)
            .winLength(3);
        
        assertThrows(IllegalArgumentException.class, builder::build);
    }
    
    @Test
    void testBuilderValidatesInvalidColumns() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(5)
            .columns(-1)
            .winLength(3);
        
        assertThrows(IllegalArgumentException.class, builder::build);
    }
    
    @Test
    void testBuilderValidatesInvalidWinLength() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(5)
            .columns(5)
            .winLength(0);
        
        assertThrows(IllegalArgumentException.class, builder::build);
    }
    
    @Test
    void testBuilderValidatesWinLengthTooLarge() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(3)
            .columns(5)
            .winLength(6);  // Exceeds min(3, 5) = 3
        
        assertThrows(IllegalArgumentException.class, builder::build);
    }
    
    // ==================== Functional Tests ====================
    
    @Test
    void testBuiltGameIsFullyFunctional() {
        ConnectNGame game = new ConnectNGameBuilder()
            .rows(3)
            .columns(3)
            .winLength(3)
            .build();
        
        // Make some moves
        game.makeMove(0, 0);  // Player 1
        game.makeMove(1, 0);  // Player 2
        game.makeMove(0, 1);  // Player 1
        game.makeMove(1, 1);  // Player 2
        game.makeMove(0, 2);  // Player 1 wins
        
        assertTrue(game.isGameOver());
        assertEquals(Player.PLAYER_ONE, game.getWinner());
        assertEquals(GameStatus.PLAYER_ONE_WON, game.getGameStatus());
    }
    
    @Test
    void testMultipleGamesFromSameBuilder() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(4)
            .columns(4)
            .winLength(3);
        
        // Build multiple games from the same builder
        ConnectNGame game1 = builder.build();
        ConnectNGame game2 = builder.build();
        
        // Verify they are independent instances
        assertNotSame(game1, game2);
        
        // Make a move in game1
        game1.makeMove(0, 0);
        
        // Verify game2 is unaffected
        char[][] board2 = game2.getBoard();
        assertEquals(' ', board2[0][0]);
    }
    
    @Test
    void testBuilderCanBeReused() {
        ConnectNGameBuilder builder = new ConnectNGameBuilder()
            .rows(5)
            .columns(5)
            .winLength(4);
        
        ConnectNGame game1 = builder.build();
        
        // Modify builder and create another game
        ConnectNGame game2 = builder
            .rows(6)
            .columns(7)
            .build();
        
        // Verify first game unchanged
        assertEquals(5, game1.getRows());
        assertEquals(5, game1.getColumns());
        
        // Verify second game has new configuration
        assertEquals(6, game2.getRows());
        assertEquals(7, game2.getColumns());
    }
    
    // ==================== Comparison Tests ====================
    
    @Test
    void testBuilderVsConstructorEquivalence() {
        // Create game with constructor
        ConnectNGame constructorGame = new ConnectNGame(6, 7, 4);
        
        // Create equivalent game with builder
        ConnectNGame builderGame = new ConnectNGameBuilder()
            .rows(6)
            .columns(7)
            .winLength(4)
            .build();
        
        // Verify they have the same configuration
        assertEquals(constructorGame.getRows(), builderGame.getRows());
        assertEquals(constructorGame.getColumns(), builderGame.getColumns());
        assertEquals(constructorGame.getWinLength(), builderGame.getWinLength());
        assertEquals(constructorGame.getCurrentPlayer(), builderGame.getCurrentPlayer());
        assertEquals(constructorGame.getGameStatus(), builderGame.getGameStatus());
    }
    
    @Test
    void testBuilderReadability() {
        // This test demonstrates the readability advantage of the builder
        
        // Constructor approach - parameter order unclear
        ConnectNGame constructorGame = new ConnectNGame(6, 7, 4);
        // Is it (rows, cols, win) or (cols, rows, win)? Hard to tell!
        
        // Builder approach - crystal clear
        ConnectNGame builderGame = new ConnectNGameBuilder()
            .rows(6)        // Clearly rows
            .columns(7)     // Clearly columns
            .winLength(4)   // Clearly win length
            .build();
        
        // Both create the same game
        assertEquals(constructorGame.getRows(), builderGame.getRows());
        assertEquals(constructorGame.getColumns(), builderGame.getColumns());
        assertEquals(constructorGame.getWinLength(), builderGame.getWinLength());
    }
}
