"""Builder for creating ConnectNGame instances with a fluent, readable API."""

from typing import Optional
from .connect_n_game import ConnectNGame


class ConnectNGameBuilder:
    """
    Builder for creating ConnectNGame instances with a fluent, readable API.
    
    This builder solves several problems with the multi-parameter constructor:
    - Parameter order confusion (rows vs columns)
    - Lack of default values
    - Difficulty adding optional parameters
    - Unclear constructor calls
    - No way to create common configurations easily
    
    Example usage:
        >>> # Create a custom game with fluent API
        >>> game = (ConnectNGameBuilder()
        ...         .rows(6)
        ...         .columns(7)
        ...         .win_length(4)
        ...         .build())
        >>> 
        >>> # Use preset configurations
        >>> tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
        >>> connect_four = ConnectNGameBuilder.connect_four()
        >>> 
        >>> # Create with defaults (3x3, win length 3)
        >>> default_game = ConnectNGameBuilder().build()
    """
    
    # Default values for common configurations
    DEFAULT_ROWS = 3
    DEFAULT_COLUMNS = 3
    DEFAULT_WIN_LENGTH = 3
    
    def __init__(self):
        """Create a new builder with default values (3x3 board, win length 3)."""
        self._rows = self.DEFAULT_ROWS
        self._columns = self.DEFAULT_COLUMNS
        self._win_length = self.DEFAULT_WIN_LENGTH
    
    def rows(self, rows: int) -> 'ConnectNGameBuilder':
        """
        Set the number of rows for the game board.
        
        Args:
            rows: The number of rows (must be >= 1)
        
        Returns:
            This builder for method chaining
        """
        self._rows = rows
        return self
    
    def columns(self, columns: int) -> 'ConnectNGameBuilder':
        """
        Set the number of columns for the game board.
        
        Args:
            columns: The number of columns (must be >= 1)
        
        Returns:
            This builder for method chaining
        """
        self._columns = columns
        return self
    
    def win_length(self, win_length: int) -> 'ConnectNGameBuilder':
        """
        Set the win length (number of consecutive tokens needed to win).
        
        Args:
            win_length: The win length (must be >= 1 and <= min(rows, columns))
        
        Returns:
            This builder for method chaining
        """
        self._win_length = win_length
        return self
    
    def board_size(self, rows: int, columns: int) -> 'ConnectNGameBuilder':
        """
        Set the board dimensions (rows and columns) in one call.
        
        This is a convenience method for setting both dimensions at once.
        
        Args:
            rows: The number of rows (must be >= 1)
            columns: The number of columns (must be >= 1)
        
        Returns:
            This builder for method chaining
        """
        self._rows = rows
        self._columns = columns
        return self
    
    def square_board(self, size: int) -> 'ConnectNGameBuilder':
        """
        Create a square board with the specified size.
        
        This is a convenience method for creating square boards (rows = columns).
        
        Args:
            size: The size of the square board (must be >= 1)
        
        Returns:
            This builder for method chaining
        """
        self._rows = size
        self._columns = size
        return self
    
    def build(self) -> ConnectNGame:
        """
        Build and return a new ConnectNGame instance with the configured parameters.
        
        This method delegates to the ConnectNGame constructor, which performs
        all validation. If the parameters are invalid, the constructor will raise
        a ValueError.
        
        Returns:
            A new ConnectNGame instance
        
        Raises:
            ValueError: If any parameter is invalid
        """
        return ConnectNGame(self._rows, self._columns, self._win_length)
    
    # ==================== Preset Factory Methods ====================
    
    @staticmethod
    def tic_tac_toe() -> ConnectNGame:
        """
        Create a standard Tic-Tac-Toe game (3x3 board, win length 3).
        
        This is equivalent to:
            ConnectNGameBuilder().rows(3).columns(3).win_length(3).build()
        
        Returns:
            A new ConnectNGame configured for Tic-Tac-Toe
        """
        return ConnectNGameBuilder().rows(3).columns(3).win_length(3).build()
    
    @staticmethod
    def connect_four() -> ConnectNGame:
        """
        Create a standard Connect Four game (6x7 board, win length 4).
        
        This is equivalent to:
            ConnectNGameBuilder().rows(6).columns(7).win_length(4).build()
        
        Returns:
            A new ConnectNGame configured for Connect Four
        """
        return ConnectNGameBuilder().rows(6).columns(7).win_length(4).build()
    
    @staticmethod
    def gomoku() -> ConnectNGame:
        """
        Create a Gomoku game (15x15 board, win length 5).
        
        Gomoku is a traditional Japanese board game also known as Five in a Row.
        This is equivalent to:
            ConnectNGameBuilder().rows(15).columns(15).win_length(5).build()
        
        Returns:
            A new ConnectNGame configured for Gomoku
        """
        return ConnectNGameBuilder().rows(15).columns(15).win_length(5).build()
    
    @staticmethod
    def small_game() -> ConnectNGame:
        """
        Create a small practice game (5x5 board, win length 4).
        
        This configuration is good for quick games and testing.
        
        Returns:
            A new ConnectNGame configured for a small practice game
        """
        return ConnectNGameBuilder().rows(5).columns(5).win_length(4).build()
    
    @staticmethod
    def large_game() -> ConnectNGame:
        """
        Create a large strategic game (10x10 board, win length 5).
        
        This configuration provides more strategic depth and longer gameplay.
        
        Returns:
            A new ConnectNGame configured for a large strategic game
        """
        return ConnectNGameBuilder().rows(10).columns(10).win_length(5).build()
    
    @staticmethod
    def custom_square(size: int, win_length: int) -> ConnectNGame:
        """
        Create a custom square board game with the specified size and win length.
        
        This is a convenience factory method for creating square boards.
        
        Args:
            size: The size of the square board (must be >= 1)
            win_length: The win length (must be >= 1 and <= size)
        
        Returns:
            A new ConnectNGame with a square board
        
        Raises:
            ValueError: If parameters are invalid
        """
        return ConnectNGameBuilder().square_board(size).win_length(win_length).build()
