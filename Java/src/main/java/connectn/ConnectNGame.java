package connectn;

/**
 * Represents a Connect-N game where two players attempt to connect N tokens in a row.
 * 
 * <p>The game is played on a rectangular board where players alternate placing tokens
 * in empty spaces. The first player to achieve N consecutive tokens horizontally,
 * vertically, or diagonally wins. If the board fills with no winner, the game is a draw.
 * 
 * <p>This implementation uses a multi-parameter constructor to demonstrate the problem
 * that the Builder pattern solves. Students will later refactor this to use a Builder.
 * 
 * <p>Example usage:
 * <pre>{@code
 * // Create a 3x3 Tic-Tac-Toe game
 * ConnectNGame game = new ConnectNGame(3, 3, 3);
 * 
 * // Make moves
 * game.makeMove(0, 0);  // Player 1 (X) at top-left
 * game.makeMove(1, 1);  // Player 2 (O) at center
 * game.makeMove(0, 1);  // Player 1 (X)
 * 
 * // Check game state
 * if (game.isGameOver()) {
 *     Player winner = game.getWinner();
 *     if (winner != null) {
 *         System.out.println(winner + " wins!");
 *     } else {
 *         System.out.println("Draw!");
 *     }
 * }
 * }</pre>
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class ConnectNGame {
    private final int rows;
    private final int columns;
    private final int winLength;
    private char[][] board;
    private Player currentPlayer;
    private GameStatus gameStatus;
    private Player winner;
    
    /**
     * Creates a new Connect-N game with specified dimensions and win condition.
     * 
     * <p>The board is initialized with all empty spaces, Player 1 starts first,
     * and the game status is set to IN_PROGRESS.
     *
     * @param rows the number of rows on the board (must be ≥ 1)
     * @param columns the number of columns on the board (must be ≥ 1)
     * @param winLength the number of consecutive tokens needed to win 
     *                  (must be ≥ 1 and ≤ min(rows, columns))
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public ConnectNGame(int rows, int columns, int winLength) {
        // Validation will be added in task 3.2
        if (rows <= 0) {
            throw new IllegalArgumentException("Rows must be positive, got: " + rows);
        }
        if (columns <= 0) {
            throw new IllegalArgumentException("Columns must be positive, got: " + columns);
        }
        if (winLength <= 0) {
            throw new IllegalArgumentException("Win length must be positive, got: " + winLength);
        }
        if (winLength > Math.min(rows, columns)) {
            throw new IllegalArgumentException(
                "Win length (" + winLength + ") cannot exceed min(rows, columns) = " 
                + Math.min(rows, columns));
        }
        
        this.rows = rows;
        this.columns = columns;
        this.winLength = winLength;
        this.board = new char[rows][columns];
        
        // Initialize board with empty spaces
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = ' ';
            }
        }
        
        this.currentPlayer = Player.PLAYER_ONE;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.winner = null;
    }
    
    /**
     * Gets the number of rows on the board.
     *
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Gets the number of columns on the board.
     *
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }
    
    /**
     * Gets the win length (N) for this game.
     *
     * @return the number of consecutive tokens needed to win
     */
    public int getWinLength() {
        return winLength;
    }
    
    /**
     * Gets the current player whose turn it is.
     *
     * @return the current Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Gets the current game status.
     *
     * @return the current GameStatus
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }
    
    /**
     * Gets the winner of the game, if any.
     *
     * @return the winning Player, or null if there is no winner yet
     */
    public Player getWinner() {
        return winner;
    }
    
    /**
     * Gets a copy of the current board state.
     * 
     * <p>Returns a defensive copy to prevent external modification of the internal board state.
     *
     * @return a 2D char array representing the board (defensive copy)
     */
    public char[][] getBoard() {
        char[][] copy = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }
    
    /**
     * Checks if a move at the specified position is valid.
     * 
     * <p>A move is valid if:
     * <ul>
     *   <li>The row and column are within bounds</li>
     *   <li>The position is empty (contains a space)</li>
     *   <li>The game is not over</li>
     * </ul>
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(int row, int col) {
        // Check if game is over
        if (isGameOver()) {
            return false;
        }
        
        // Check if position is within bounds
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            return false;
        }
        
        // Check if position is empty
        return board[row][col] == ' ';
    }
    
    /**
     * Checks if the game has ended (either by win or draw).
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameStatus != GameStatus.IN_PROGRESS;
    }
    
    /**
     * Makes a move for the current player at the specified position.
     * 
     * <p>After placing the token, the method checks for a winner. If no winner is found
     * and the game continues, the current player switches to the opponent.
     *
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @throws IllegalArgumentException if the move is invalid (out of bounds, 
     *                                  position occupied, or game over)
     */
    public void makeMove(int row, int col) {
        // Validate the move
        if (!isValidMove(row, col)) {
            if (isGameOver()) {
                throw new IllegalArgumentException("Game is over, no more moves allowed");
            }
            if (row < 0 || row >= rows || col < 0 || col >= columns) {
                throw new IllegalArgumentException(
                    "Position (" + row + ", " + col + ") is out of bounds for board size " 
                    + rows + "x" + columns);
            }
            if (board[row][col] != ' ') {
                throw new IllegalArgumentException(
                    "Position (" + row + ", " + col + ") is already occupied");
            }
        }
        
        // Place the token
        board[row][col] = currentPlayer.getToken();
        
        // Check for winner
        checkForWinner(row, col);
        
        // Switch player if game continues
        if (!isGameOver()) {
            switchPlayer();
        }
    }
    
    /**
     * Switches to the other player.
     */
    private void switchPlayer() {
        currentPlayer = currentPlayer.getOpponent();
    }
    
    /**
     * Checks if there is a winner after the last move at the specified position.
     * Updates game status and winner if a win is found.
     * 
     * @param row the row of the last move
     * @param col the column of the last move
     */
    private void checkForWinner(int row, int col) {
        char token = board[row][col];
        
        // Check all four directions
        boolean hasWon = checkDirection(row, col, 0, 1, token)   // Horizontal
                      || checkDirection(row, col, 1, 0, token)   // Vertical
                      || checkDirection(row, col, 1, 1, token)   // Diagonal ↘
                      || checkDirection(row, col, 1, -1, token); // Anti-diagonal ↙
        
        if (hasWon) {
            winner = currentPlayer;
            gameStatus = (currentPlayer == Player.PLAYER_ONE) 
                ? GameStatus.PLAYER_ONE_WON 
                : GameStatus.PLAYER_TWO_WON;
        } else if (isBoardFull()) {
            gameStatus = GameStatus.DRAW;
        }
    }
    
    /**
     * Checks for N consecutive tokens starting from a position in a given direction.
     * 
     * @param row starting row
     * @param col starting column
     * @param deltaRow row direction (-1, 0, or 1)
     * @param deltaCol column direction (-1, 0, or 1)
     * @param token the token to check for
     * @return true if N consecutive tokens found, false otherwise
     */
    private boolean checkDirection(int row, int col, int deltaRow, int deltaCol, char token) {
        int count = 1; // Count the current position
        
        // Count in positive direction
        count += countConsecutive(row, col, deltaRow, deltaCol, token);
        
        // Count in negative direction
        count += countConsecutive(row, col, -deltaRow, -deltaCol, token);
        
        return count >= winLength;
    }
    
    /**
     * Counts consecutive tokens in a specific direction from a starting position.
     * 
     * @param row starting row
     * @param col starting column
     * @param deltaRow row direction
     * @param deltaCol column direction
     * @param token the token to count
     * @return the number of consecutive tokens found (not including starting position)
     */
    private int countConsecutive(int row, int col, int deltaRow, int deltaCol, char token) {
        int count = 0;
        int r = row + deltaRow;
        int c = col + deltaCol;
        
        while (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == token) {
            count++;
            r += deltaRow;
            c += deltaCol;
        }
        
        return count;
    }
    
    /**
     * Checks if the board is completely filled.
     *
     * @return true if no empty spaces remain, false otherwise
     */
    private boolean isBoardFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Resets the game to its initial state with the same configuration.
     * 
     * <p>Clears all tokens from the board, sets Player 1 as the current player,
     * sets the game status to IN_PROGRESS, and clears any winner information.
     * The board dimensions and win length are preserved.
     */
    public void reset() {
        // Clear the board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = ' ';
            }
        }
        
        // Reset game state
        currentPlayer = Player.PLAYER_ONE;
        gameStatus = GameStatus.IN_PROGRESS;
        winner = null;
    }
}
