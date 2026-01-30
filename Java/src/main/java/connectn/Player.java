package connectn;

/**
 * Represents a player in the Connect-N game.
 * 
 * <p>Each player has a unique token character used to mark their positions on the board.
 * Player 1 uses 'X' and Player 2 uses 'O'.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public enum Player {
    /**
     * The first player, represented by 'X'.
     */
    PLAYER_ONE('X'),
    
    /**
     * The second player, represented by 'O'.
     */
    PLAYER_TWO('O');
    
    private final char token;
    
    /**
     * Constructs a Player with the specified token character.
     * 
     * @param token the character representing this player on the board
     */
    Player(char token) {
        this.token = token;
    }
    
    /**
     * Gets the character token representing this player.
     * 
     * @return the player's token character ('X' for Player 1, 'O' for Player 2)
     */
    public char getToken() {
        return token;
    }
    
    /**
     * Gets the opponent of this player.
     * 
     * @return the other player (Player 2 if this is Player 1, and vice versa)
     */
    public Player getOpponent() {
        return this == PLAYER_ONE ? PLAYER_TWO : PLAYER_ONE;
    }
}
