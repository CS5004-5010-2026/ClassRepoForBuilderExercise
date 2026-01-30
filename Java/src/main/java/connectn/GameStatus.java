package connectn;

/**
 * Represents the current status of a Connect-N game.
 * 
 * <p>The game can be in progress, won by either player, or ended in a draw.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public enum GameStatus {
    /**
     * The game is still in progress and moves can be made.
     */
    IN_PROGRESS,
    
    /**
     * Player 1 has won the game by connecting N tokens.
     */
    PLAYER_ONE_WON,
    
    /**
     * Player 2 has won the game by connecting N tokens.
     */
    PLAYER_TWO_WON,
    
    /**
     * The game has ended in a draw (board is full with no winner).
     */
    DRAW
}
