"""Property-based tests for ConnectNGame using Hypothesis."""

import pytest
from hypothesis import given, strategies as st, assume
from connectn import ConnectNGame, Player, GameStatus


# Strategy for valid board dimensions
valid_dimensions = st.integers(min_value=1, max_value=20)

# Strategy for valid game configurations
@st.composite
def valid_game_config(draw):
    """Generate valid game configurations."""
    rows = draw(st.integers(min_value=1, max_value=15))
    columns = draw(st.integers(min_value=1, max_value=15))
    win_length = draw(st.integers(min_value=1, max_value=min(rows, columns)))
    return rows, columns, win_length


class TestConstructorProperties:
    """Property-based tests for constructor validation."""
    
    @given(valid_game_config())
    def test_constructor_validates_positive_dimensions(self, config):
        """Property: Constructor accepts positive dimensions."""
        rows, columns, win_length = config
        game = ConnectNGame(rows, columns, win_length)
        assert game.rows == rows
        assert game.columns == columns
        assert game.win_length == win_length
    
    @given(st.integers(max_value=0), valid_dimensions, valid_dimensions)
    def test_constructor_validates_positive_rows(self, rows, columns, win_length):
        """Property: Constructor rejects non-positive rows."""
        assume(win_length <= min(abs(rows) if rows != 0 else 1, columns))
        with pytest.raises(ValueError):
            ConnectNGame(rows, columns, win_length)
    
    @given(valid_dimensions, st.integers(max_value=0), valid_dimensions)
    def test_constructor_validates_positive_columns(self, rows, columns, win_length):
        """Property: Constructor rejects non-positive columns."""
        assume(win_length <= min(rows, abs(columns) if columns != 0 else 1))
        with pytest.raises(ValueError):
            ConnectNGame(rows, columns, win_length)
    
    @given(valid_dimensions, valid_dimensions, st.integers(max_value=0))
    def test_constructor_validates_positive_win_length(self, rows, columns, win_length):
        """Property: Constructor rejects non-positive win length."""
        with pytest.raises(ValueError):
            ConnectNGame(rows, columns, win_length)
    
    @given(valid_dimensions, valid_dimensions, valid_dimensions)
    def test_constructor_validates_win_length_feasibility(self, rows, columns, win_length):
        """Property: Constructor validates win length <= min(rows, columns)."""
        assume(win_length > min(rows, columns))
        with pytest.raises(ValueError):
            ConnectNGame(rows, columns, win_length)


class TestInitialStateProperties:
    """Property-based tests for initial state."""
    
    @given(valid_game_config())
    def test_new_games_start_with_empty_board_and_player_one(self, config):
        """Property: New games start with empty board and Player 1."""
        rows, columns, win_length = config
        game = ConnectNGame(rows, columns, win_length)
        
        # Board should be empty
        board = game.get_board()
        assert all(cell == ' ' for row in board for cell in row)
        
        # Player 1 starts
        assert game.current_player == Player.PLAYER_ONE
        
        # Status is IN_PROGRESS
        assert game.game_status == GameStatus.IN_PROGRESS
        
        # No winner yet
        assert game.get_winner() is None


class TestBoardStateProperties:
    """Property-based tests for board state."""
    
    @given(valid_game_config())
    def test_board_defensive_copy_prevents_external_modification(self, config):
        """Property: Board defensive copy prevents external modification."""
        rows, columns, win_length = config
        game = ConnectNGame(rows, columns, win_length)
        
        # Get board and modify it
        board = game.get_board()
        if rows > 0 and columns > 0:
            board[0][0] = 'X'
        
        # Game board should be unchanged
        game_board = game.get_board()
        assert game_board[0][0] == ' '


class TestMoveValidationProperties:
    """Property-based tests for move validation."""
    
    @given(valid_game_config(), st.integers(), st.integers())
    def test_out_of_bounds_positions_are_invalid(self, config, row, col):
        """Property: Out-of-bounds positions are invalid."""
        rows, columns, win_length = config
        game = ConnectNGame(rows, columns, win_length)
        
        # Assume position is out of bounds
        assume(row < 0 or row >= rows or col < 0 or col >= columns)
        
        assert not game.is_valid_move(row, col)
    
    @given(valid_game_config())
    def test_occupied_positions_are_invalid(self, config):
        """Property: Occupied positions are invalid."""
        rows, columns, win_length = config
        assume(rows >= 1 and columns >= 1)
        
        game = ConnectNGame(rows, columns, win_length)
        
        # Make a move
        game.make_move(0, 0)
        
        # Position should now be invalid
        assert not game.is_valid_move(0, 0)


class TestMoveExecutionProperties:
    """Property-based tests for move execution."""
    
    @given(valid_game_config())
    def test_valid_moves_update_board_state(self, config):
        """Property: Valid moves update board state."""
        rows, columns, win_length = config
        assume(rows >= 1 and columns >= 1)
        
        game = ConnectNGame(rows, columns, win_length)
        
        # Make a move
        game.make_move(0, 0)
        
        # Board should be updated
        board = game.get_board()
        assert board[0][0] == 'X'
    
    @given(valid_game_config())
    def test_valid_moves_switch_players(self, config):
        """Property: Valid moves switch players (when game continues)."""
        rows, columns, win_length = config
        assume(rows >= 2 and columns >= 2 and win_length >= 2)
        
        game = ConnectNGame(rows, columns, win_length)
        
        # Make moves that don't win
        game.make_move(0, 0)
        assert game.current_player == Player.PLAYER_TWO
        
        game.make_move(1, 1)
        assert game.current_player == Player.PLAYER_ONE
    
    @given(valid_game_config(), st.integers(), st.integers())
    def test_invalid_moves_throw_value_error(self, config, row, col):
        """Property: Invalid moves throw ValueError."""
        rows, columns, win_length = config
        game = ConnectNGame(rows, columns, win_length)
        
        # Assume invalid position
        assume(row < 0 or row >= rows or col < 0 or col >= columns)
        
        with pytest.raises(ValueError):
            game.make_move(row, col)


class TestResetProperties:
    """Property-based tests for reset."""
    
    @given(valid_game_config())
    def test_reset_restores_initial_state(self, config):
        """Property: Reset restores initial state."""
        rows, columns, win_length = config
        assume(rows >= 2 and columns >= 2)
        
        game = ConnectNGame(rows, columns, win_length)
        
        # Make some moves
        game.make_move(0, 0)
        if not game.is_game_over():
            game.make_move(1, 1)
        
        # Reset
        game.reset()
        
        # Should be back to initial state
        board = game.get_board()
        assert all(cell == ' ' for row in board for cell in row)
        assert game.current_player == Player.PLAYER_ONE
        assert game.game_status == GameStatus.IN_PROGRESS
        assert game.get_winner() is None
        
        # Configuration should be preserved
        assert game.rows == rows
        assert game.columns == columns
        assert game.win_length == win_length


class TestWinDetectionProperties:
    """Property-based tests for win detection."""
    
    @given(st.integers(min_value=3, max_value=10))
    def test_horizontal_wins_are_detected(self, size):
        """Property: Horizontal wins are detected."""
        game = ConnectNGame(size, size, 3)
        
        # Create horizontal win
        game.make_move(0, 0)  # X
        game.make_move(1, 0)  # O
        game.make_move(0, 1)  # X
        game.make_move(1, 1)  # O
        game.make_move(0, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    @given(st.integers(min_value=3, max_value=10))
    def test_vertical_wins_are_detected(self, size):
        """Property: Vertical wins are detected."""
        game = ConnectNGame(size, size, 3)
        
        # Create vertical win
        game.make_move(0, 0)  # X
        game.make_move(0, 1)  # O
        game.make_move(1, 0)  # X
        game.make_move(1, 1)  # O
        game.make_move(2, 0)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    @given(st.integers(min_value=3, max_value=10))
    def test_diagonal_wins_are_detected(self, size):
        """Property: Diagonal wins are detected."""
        game = ConnectNGame(size, size, 3)
        
        # Create diagonal win
        game.make_move(0, 0)  # X
        game.make_move(0, 1)  # O
        game.make_move(1, 1)  # X
        game.make_move(0, 2)  # O
        game.make_move(2, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    @given(valid_game_config())
    def test_non_winning_games_have_no_winner(self, config):
        """Property: Non-winning games have no winner."""
        rows, columns, win_length = config
        assume(rows >= 2 and columns >= 2)
        
        game = ConnectNGame(rows, columns, win_length)
        
        # Make a single move (can't win)
        game.make_move(0, 0)
        
        if not game.is_game_over():
            assert game.get_winner() is None


class TestDrawDetectionProperties:
    """Property-based tests for draw detection."""
    
    def test_full_board_without_winner_is_draw(self):
        """Property: Full boards without winners are draws."""
        game = ConnectNGame(3, 3, 3)
        
        # Fill board without winner
        # X O X
        # O X X
        # O X O
        moves = [
            (0, 0), (0, 1), (0, 2),
            (1, 1), (1, 0), (2, 0),
            (1, 2), (2, 2), (2, 1)
        ]
        for row, col in moves:
            game.make_move(row, col)
        
        assert game.is_game_over()
        assert game.get_winner() is None
        assert game.game_status == GameStatus.DRAW
