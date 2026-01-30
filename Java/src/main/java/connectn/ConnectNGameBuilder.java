package connectn;

/**
 * Builder for creating ConnectNGame instances with a fluent, readable API.
 * 
 * <p>This builder solves several problems with the multi-parameter constructor:
 * <ul>
 *   <li>Parameter order confusion (rows vs columns)</li>
 *   <li>Lack of default values</li>
 *   <li>Difficulty adding optional parameters</li>
 *   <li>Unclear constructor calls</li>
 *   <li>No way to create common configurations easily</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create a custom game with fluent API
 * ConnectNGame game = new ConnectNGameBuilder()
 *     .rows(6)
 *     .columns(7)
 *     .winLength(4)
 *     .build();
 * 
 * // Use preset configurations
 * ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
 * ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
 * 
 * // Create with defaults (3x3, win length 3)
 * ConnectNGame defaultGame = new ConnectNGameBuilder().build();
 * }</pre>
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class ConnectNGameBuilder {
    // Default values for common configurations
    private static final int DEFAULT_ROWS = 3;
    private static final int DEFAULT_COLUMNS = 3;
    private static final int DEFAULT_WIN_LENGTH = 3;
    
    // Builder fields with defaults
    private int rows = DEFAULT_ROWS;
    private int columns = DEFAULT_COLUMNS;
    private int winLength = DEFAULT_WIN_LENGTH;
    
    /**
     * Creates a new builder with default values (3x3 board, win length 3).
     */
    public ConnectNGameBuilder() {
        // Use default values
    }
    
    /**
     * Sets the number of rows for the game board.
     *
     * @param rows the number of rows (must be ≥ 1)
     * @return this builder for method chaining
     */
    public ConnectNGameBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }
    
    /**
     * Sets the number of columns for the game board.
     *
     * @param columns the number of columns (must be ≥ 1)
     * @return this builder for method chaining
     */
    public ConnectNGameBuilder columns(int columns) {
        this.columns = columns;
        return this;
    }
    
    /**
     * Sets the win length (number of consecutive tokens needed to win).
     *
     * @param winLength the win length (must be ≥ 1 and ≤ min(rows, columns))
     * @return this builder for method chaining
     */
    public ConnectNGameBuilder winLength(int winLength) {
        this.winLength = winLength;
        return this;
    }
    
    /**
     * Sets the board dimensions (rows and columns) in one call.
     * 
     * <p>This is a convenience method for setting both dimensions at once.
     *
     * @param rows the number of rows (must be ≥ 1)
     * @param columns the number of columns (must be ≥ 1)
     * @return this builder for method chaining
     */
    public ConnectNGameBuilder boardSize(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        return this;
    }
    
    /**
     * Creates a square board with the specified size.
     * 
     * <p>This is a convenience method for creating square boards (rows = columns).
     *
     * @param size the size of the square board (must be ≥ 1)
     * @return this builder for method chaining
     */
    public ConnectNGameBuilder squareBoard(int size) {
        this.rows = size;
        this.columns = size;
        return this;
    }
    
    /**
     * Builds and returns a new ConnectNGame instance with the configured parameters.
     * 
     * <p>This method delegates to the ConnectNGame constructor, which performs
     * all validation. If the parameters are invalid, the constructor will throw
     * an IllegalArgumentException.
     *
     * @return a new ConnectNGame instance
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public ConnectNGame build() {
        return new ConnectNGame(rows, columns, winLength);
    }
    
    // ==================== Preset Factory Methods ====================
    
    /**
     * Creates a standard Tic-Tac-Toe game (3x3 board, win length 3).
     * 
     * <p>This is equivalent to:
     * <pre>{@code
     * new ConnectNGameBuilder()
     *     .rows(3)
     *     .columns(3)
     *     .winLength(3)
     *     .build();
     * }</pre>
     *
     * @return a new ConnectNGame configured for Tic-Tac-Toe
     */
    public static ConnectNGame ticTacToe() {
        return new ConnectNGameBuilder()
            .rows(3)
            .columns(3)
            .winLength(3)
            .build();
    }
    
    /**
     * Creates a standard Connect Four game (6x7 board, win length 4).
     * 
     * <p>This is equivalent to:
     * <pre>{@code
     * new ConnectNGameBuilder()
     *     .rows(6)
     *     .columns(7)
     *     .winLength(4)
     *     .build();
     * }</pre>
     *
     * @return a new ConnectNGame configured for Connect Four
     */
    public static ConnectNGame connectFour() {
        return new ConnectNGameBuilder()
            .rows(6)
            .columns(7)
            .winLength(4)
            .build();
    }
    
    /**
     * Creates a Gomoku game (15x15 board, win length 5).
     * 
     * <p>Gomoku is a traditional Japanese board game also known as Five in a Row.
     * This is equivalent to:
     * <pre>{@code
     * new ConnectNGameBuilder()
     *     .rows(15)
     *     .columns(15)
     *     .winLength(5)
     *     .build();
     * }</pre>
     *
     * @return a new ConnectNGame configured for Gomoku
     */
    public static ConnectNGame gomoku() {
        return new ConnectNGameBuilder()
            .rows(15)
            .columns(15)
            .winLength(5)
            .build();
    }
    
    /**
     * Creates a small practice game (5x5 board, win length 4).
     * 
     * <p>This configuration is good for quick games and testing.
     *
     * @return a new ConnectNGame configured for a small practice game
     */
    public static ConnectNGame smallGame() {
        return new ConnectNGameBuilder()
            .rows(5)
            .columns(5)
            .winLength(4)
            .build();
    }
    
    /**
     * Creates a large strategic game (10x10 board, win length 5).
     * 
     * <p>This configuration provides more strategic depth and longer gameplay.
     *
     * @return a new ConnectNGame configured for a large strategic game
     */
    public static ConnectNGame largeGame() {
        return new ConnectNGameBuilder()
            .rows(10)
            .columns(10)
            .winLength(5)
            .build();
    }
    
    /**
     * Creates a custom square board game with the specified size and win length.
     * 
     * <p>This is a convenience factory method for creating square boards.
     *
     * @param size the size of the square board (must be ≥ 1)
     * @param winLength the win length (must be ≥ 1 and ≤ size)
     * @return a new ConnectNGame with a square board
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static ConnectNGame customSquare(int size, int winLength) {
        return new ConnectNGameBuilder()
            .squareBoard(size)
            .winLength(winLength)
            .build();
    }
}
