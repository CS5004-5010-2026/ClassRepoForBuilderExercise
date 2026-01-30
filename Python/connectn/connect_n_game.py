"""Connect-N game model implementation."""

from typing import List, Optional
from .player import Player
from .game_status import GameStatus


class ConnectNGame:
    """
    Represents a Connect-N game where two players attempt to connect N tokens in a row.
    
    The game is played on a rectangular board where players alternate placing tokens
    in empty spaces. The first player to achieve N consecutive tokens horizontally,
    vertically, or diagonally wins. If the board fills with no winner, the game is a draw.
    
    This implementation uses a multi-parameter constructor to demonstrate the problem
    that the Builder pattern solves. Students will later use ConnectNGameBuilder.
    
    Example usage:
        >>> # Create a 3x3 Tic-Tac-Toe game
        >>> game = ConnectNGame(3, 3, 3)
        >>> 
        >>> # Make moves
        >>> game.make_move(0, 0)  # Player 1 (X) at top-left
        >>> game.make_move(1, 1)  # Player 2 (O) at center
        >>> game.make_move(0, 1)  # Player 1 (X)
        >>> 
        >>> # Check game state
        >>> if game.is_game_over():
        ...     winner = game.get_winner()
        ...     if winner:
        ...         print(f"{winner} wins!")
        ...     else:
        ...         print("Draw!")
    """
    
    def __init__(self, rows: int, columns: int, win_length: int):
        """
        Create a new Connect-N game with specified dimensions and win condition.
        
        The board is initialized with all empty spaces, Player 1 starts first,
        and the game status is set to IN_PROGRESS.
        
        Args:
            rows: The number of rows on the board (must be >= 1)
            columns: The number of columns on the board (must be >= 1)
            win_length: The number of consecutive tokens needed to win
                       (must be >= 1 and <= min(rows, columns))
        
        Raises:
            ValueError: If any parameter is invalid
        """
        # Validation
        if rows <= 0:
            raise ValueError(f"Rows must be positive, got: {rows}")
        if columns <= 0:
            raise ValueError(f"Columns must be positive, got: {columns}")
        if win_length <= 0:
            raise ValueError(f"Win length must be positive, got: {win_length}")
        if win_length > min(rows, columns):
            raise ValueError(
                f"Win length ({win_length}) cannot exceed min(rows, columns) = {min(rows, columns)}"
            )
        
        self._rows = rows
        self._columns = columns
        self._win_length = win_length
        self._board = [[' ' for _ in range(columns)] for _ in range(rows)]
        self._current_player = Player.PLAYER_ONE
        self._game_status = GameStatus.IN_PROGRESS
        self._winner: Optional[Player] = None
    
    @property
    def rows(self) -> int:
        """Get the number of rows on the board."""
        return self._rows
    
    @property
    def columns(self) -> int:
        """Get the number of columns on the board."""
        return self._columns
    
    @property
    def win_length(self) -> int:
        """Get the win length (N) for this game."""
        return self._win_length
    
    @property
    def current_player(self) -> Player:
        """Get the current player whose turn it is."""
        return self._current_player
    
    @property
    def game_status(self) -> GameStatus:
        """Get the current game status."""
        return self._game_status
    
    def get_winner(self) -> Optional[Player]:
        """
        Get the winner of the game, if any.
        
        Returns:
            The winning Player, or None if there is no winner yet
        """
        return self._winner
    
    def get_board(self) -> List[List[str]]:
        """
        Get a copy of the current board state.
        
        Returns a defensive copy to prevent external modification of the internal board state.
        
        Returns:
            A 2D list representing the board (defensive copy)
        """
        return [row[:] for row in self._board]
    
    def is_valid_move(self, row: int, col: int) -> bool:
        """
        Check if a move at the specified position is valid.
        
        A move is valid if:
        - The row and column are within bounds
        - The position is empty (contains a space)
        - The game is not over
        
        Args:
            row: The row index (0-based)
            col: The column index (0-based)
        
        Returns:
            True if the move is valid, False otherwise
        """
        # Check if game is over
        if self.is_game_over():
            return False
        
        # Check if position is within bounds
        if row < 0 or row >= self._rows or col < 0 or col >= self._columns:
            return False
        
        # Check if position is empty
        return self._board[row][col] == ' '
    
    def is_game_over(self) -> bool:
        """
        Check if the game has ended (either by win or draw).
        
        Returns:
            True if the game is over, False otherwise
        """
        return self._game_status != GameStatus.IN_PROGRESS
    
    def make_move(self, row: int, col: int) -> None:
        """
        Make a move for the current player at the specified position.
        
        After placing the token, the method checks for a winner. If no winner is found
        and the game continues, the current player switches to the opponent.
        
        Args:
            row: The row index (0-based)
            col: The column index (0-based)
        
        Raises:
            ValueError: If the move is invalid (out of bounds, position occupied, or game over)
        """
        # Validate the move
        if not self.is_valid_move(row, col):
            if self.is_game_over():
                raise ValueError("Game is over, no more moves allowed")
            if row < 0 or row >= self._rows or col < 0 or col >= self._columns:
                raise ValueError(
                    f"Position ({row}, {col}) is out of bounds for board size {self._rows}x{self._columns}"
                )
            if self._board[row][col] != ' ':
                raise ValueError(f"Position ({row}, {col}) is already occupied")
        
        # Place the token
        self._board[row][col] = self._current_player.token
        
        # Check for winner
        self._check_for_winner(row, col)
        
        # Switch player if game continues
        if not self.is_game_over():
            self._switch_player()
    
    def reset(self) -> None:
        """
        Reset the game to its initial state with the same configuration.
        
        Clears all tokens from the board, sets Player 1 as the current player,
        sets the game status to IN_PROGRESS, and clears any winner information.
        The board dimensions and win length are preserved.
        """
        # Clear the board
        self._board = [[' ' for _ in range(self._columns)] for _ in range(self._rows)]
        
        # Reset game state
        self._current_player = Player.PLAYER_ONE
        self._game_status = GameStatus.IN_PROGRESS
        self._winner = None
    
    def _switch_player(self) -> None:
        """Switch to the other player."""
        self._current_player = self._current_player.get_opponent()
    
    def _check_for_winner(self, row: int, col: int) -> None:
        """
        Check if there is a winner after the last move at the specified position.
        Updates game status and winner if a win is found.
        
        Args:
            row: The row of the last move
            col: The column of the last move
        """
        token = self._board[row][col]
        
        # Check all four directions
        has_won = (
            self._check_direction(row, col, 0, 1, token) or   # Horizontal
            self._check_direction(row, col, 1, 0, token) or   # Vertical
            self._check_direction(row, col, 1, 1, token) or   # Diagonal ↘
            self._check_direction(row, col, 1, -1, token)     # Anti-diagonal ↙
        )
        
        if has_won:
            self._winner = self._current_player
            self._game_status = (
                GameStatus.PLAYER_ONE_WON if self._current_player == Player.PLAYER_ONE
                else GameStatus.PLAYER_TWO_WON
            )
        elif self._is_board_full():
            self._game_status = GameStatus.DRAW
    
    def _check_direction(self, row: int, col: int, delta_row: int, delta_col: int, token: str) -> bool:
        """
        Check for N consecutive tokens starting from a position in a given direction.
        
        Args:
            row: Starting row
            col: Starting column
            delta_row: Row direction (-1, 0, or 1)
            delta_col: Column direction (-1, 0, or 1)
            token: The token to check for
        
        Returns:
            True if N consecutive tokens found, False otherwise
        """
        count = 1  # Count the current position
        
        # Count in positive direction
        count += self._count_consecutive(row, col, delta_row, delta_col, token)
        
        # Count in negative direction
        count += self._count_consecutive(row, col, -delta_row, -delta_col, token)
        
        return count >= self._win_length
    
    def _count_consecutive(self, row: int, col: int, delta_row: int, delta_col: int, token: str) -> int:
        """
        Count consecutive tokens in a specific direction from a starting position.
        
        Args:
            row: Starting row
            col: Starting column
            delta_row: Row direction
            delta_col: Column direction
            token: The token to count
        
        Returns:
            The number of consecutive tokens found (not including starting position)
        """
        count = 0
        r = row + delta_row
        c = col + delta_col
        
        while (0 <= r < self._rows and 0 <= c < self._columns and 
               self._board[r][c] == token):
            count += 1
            r += delta_row
            c += delta_col
        
        return count
    
    def _is_board_full(self) -> bool:
        """
        Check if the board is completely filled.
        
        Returns:
            True if no empty spaces remain, False otherwise
        """
        return all(cell != ' ' for row in self._board for cell in row)
    
    def __str__(self) -> str:
        """String representation of the game board."""
        lines = []
        lines.append(f"Connect-{self._win_length} ({self._rows}x{self._columns})")
        lines.append(f"Status: {self._game_status}")
        lines.append(f"Current Player: {self._current_player}")
        lines.append("")
        
        # Column numbers
        lines.append("  " + " ".join(str(i) for i in range(self._columns)))
        lines.append(" +" + "-" * (self._columns * 2 - 1) + "+")
        
        # Board rows
        for i, row in enumerate(self._board):
            lines.append(f"{i}|" + "|".join(row) + "|")
        
        lines.append(" +" + "-" * (self._columns * 2 - 1) + "+")
        
        return "\n".join(lines)
