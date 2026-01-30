"""Unit tests for ConnectNGame."""

import pytest
from connectn import ConnectNGame, Player, GameStatus


class TestConstructor:
    """Tests for ConnectNGame constructor."""
    
    def test_valid_constructor_3x3(self):
        """Test creating a 3x3 game."""
        game = ConnectNGame(3, 3, 3)
        assert game.rows == 3
        assert game.columns == 3
        assert game.win_length == 3
    
    def test_valid_constructor_6x7(self):
        """Test creating a 6x7 game."""
        game = ConnectNGame(6, 7, 4)
        assert game.rows == 6
        assert game.columns == 7
        assert game.win_length == 4
    
    def test_valid_constructor_1x1(self):
        """Test creating a 1x1 game."""
        game = ConnectNGame(1, 1, 1)
        assert game.rows == 1
        assert game.columns == 1
        assert game.win_length == 1
    
    def test_valid_constructor_10x10(self):
        """Test creating a 10x10 game."""
        game = ConnectNGame(10, 10, 5)
        assert game.rows == 10
        assert game.columns == 10
        assert game.win_length == 5
    
    def test_invalid_rows_zero(self):
        """Test that zero rows raises ValueError."""
        with pytest.raises(ValueError, match="Rows must be positive"):
            ConnectNGame(0, 5, 3)
    
    def test_invalid_rows_negative(self):
        """Test that negative rows raises ValueError."""
        with pytest.raises(ValueError, match="Rows must be positive"):
            ConnectNGame(-1, 5, 3)
    
    def test_invalid_columns_zero(self):
        """Test that zero columns raises ValueError."""
        with pytest.raises(ValueError, match="Columns must be positive"):
            ConnectNGame(5, 0, 3)
    
    def test_invalid_columns_negative(self):
        """Test that negative columns raises ValueError."""
        with pytest.raises(ValueError, match="Columns must be positive"):
            ConnectNGame(5, -1, 3)
    
    def test_invalid_win_length_zero(self):
        """Test that zero win length raises ValueError."""
        with pytest.raises(ValueError, match="Win length must be positive"):
            ConnectNGame(5, 5, 0)
    
    def test_invalid_win_length_negative(self):
        """Test that negative win length raises ValueError."""
        with pytest.raises(ValueError, match="Win length must be positive"):
            ConnectNGame(5, 5, -1)
    
    def test_win_length_too_large(self):
        """Test that win length exceeding min dimension raises ValueError."""
        with pytest.raises(ValueError, match="cannot exceed"):
            ConnectNGame(3, 5, 6)
    
    def test_win_length_exactly_min_dimension(self):
        """Test that win length equal to min dimension is valid."""
        game = ConnectNGame(3, 5, 3)
        assert game.win_length == 3


class TestInitialState:
    """Tests for initial game state."""
    
    def test_initial_board_empty(self):
        """Test that initial board is empty."""
        game = ConnectNGame(3, 3, 3)
        board = game.get_board()
        assert all(cell == ' ' for row in board for cell in row)
    
    def test_initial_player_is_player_one(self):
        """Test that Player 1 starts first."""
        game = ConnectNGame(3, 3, 3)
        assert game.current_player == Player.PLAYER_ONE
    
    def test_initial_status_in_progress(self):
        """Test that initial status is IN_PROGRESS."""
        game = ConnectNGame(3, 3, 3)
        assert game.game_status == GameStatus.IN_PROGRESS
    
    def test_initial_winner_is_none(self):
        """Test that initial winner is None."""
        game = ConnectNGame(3, 3, 3)
        assert game.get_winner() is None


class TestBoardState:
    """Tests for board state management."""
    
    def test_get_board_returns_defensive_copy(self):
        """Test that get_board returns a defensive copy."""
        game = ConnectNGame(3, 3, 3)
        board1 = game.get_board()
        board2 = game.get_board()
        assert board1 is not board2
        assert board1 == board2
    
    def test_modifying_returned_board_does_not_affect_game(self):
        """Test that modifying returned board doesn't affect game."""
        game = ConnectNGame(3, 3, 3)
        board = game.get_board()
        board[0][0] = 'X'
        
        # Game board should still be empty
        game_board = game.get_board()
        assert game_board[0][0] == ' '


class TestMoveValidation:
    """Tests for move validation."""
    
    def test_valid_move_returns_true(self):
        """Test that valid moves return True."""
        game = ConnectNGame(3, 3, 3)
        assert game.is_valid_move(0, 0)
        assert game.is_valid_move(1, 1)
        assert game.is_valid_move(2, 2)
    
    def test_out_of_bounds_row_negative(self):
        """Test that negative row is invalid."""
        game = ConnectNGame(3, 3, 3)
        assert not game.is_valid_move(-1, 0)
    
    def test_out_of_bounds_row_too_large(self):
        """Test that row >= rows is invalid."""
        game = ConnectNGame(3, 3, 3)
        assert not game.is_valid_move(3, 0)
    
    def test_out_of_bounds_column_negative(self):
        """Test that negative column is invalid."""
        game = ConnectNGame(3, 3, 3)
        assert not game.is_valid_move(0, -1)
    
    def test_out_of_bounds_column_too_large(self):
        """Test that column >= columns is invalid."""
        game = ConnectNGame(3, 3, 3)
        assert not game.is_valid_move(0, 3)
    
    def test_occupied_position_invalid(self):
        """Test that occupied positions are invalid."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)
        assert not game.is_valid_move(0, 0)


class TestMoveExecution:
    """Tests for move execution."""
    
    def test_valid_move_places_token(self):
        """Test that valid move places token."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)
        board = game.get_board()
        assert board[0][0] == 'X'
    
    def test_player_switches_after_move(self):
        """Test that player switches after move."""
        game = ConnectNGame(3, 3, 3)
        assert game.current_player == Player.PLAYER_ONE
        game.make_move(0, 0)
        assert game.current_player == Player.PLAYER_TWO
        game.make_move(1, 1)
        assert game.current_player == Player.PLAYER_ONE
    
    def test_invalid_move_throws_exception(self):
        """Test that invalid move raises ValueError."""
        game = ConnectNGame(3, 3, 3)
        with pytest.raises(ValueError):
            game.make_move(-1, 0)
    
    def test_occupied_position_throws_exception(self):
        """Test that move to occupied position raises ValueError."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)
        with pytest.raises(ValueError, match="already occupied"):
            game.make_move(0, 0)
    
    def test_exception_messages_are_descriptive(self):
        """Test that exception messages are descriptive."""
        game = ConnectNGame(3, 3, 3)
        
        # Out of bounds
        with pytest.raises(ValueError, match="out of bounds"):
            game.make_move(5, 5)
        
        # Occupied
        game.make_move(0, 0)
        with pytest.raises(ValueError, match="occupied"):
            game.make_move(0, 0)


class TestWinDetection:
    """Tests for win detection."""
    
    def test_horizontal_win_top_row(self):
        """Test horizontal win in top row."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)  # X
        game.make_move(1, 0)  # O
        game.make_move(0, 1)  # X
        game.make_move(1, 1)  # O
        game.make_move(0, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
        assert game.game_status == GameStatus.PLAYER_ONE_WON
    
    def test_horizontal_win_middle_row(self):
        """Test horizontal win in middle row."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(1, 0)  # X
        game.make_move(0, 0)  # O
        game.make_move(1, 1)  # X
        game.make_move(0, 1)  # O
        game.make_move(1, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    def test_vertical_win_left_column(self):
        """Test vertical win in left column."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)  # X
        game.make_move(0, 1)  # O
        game.make_move(1, 0)  # X
        game.make_move(1, 1)  # O
        game.make_move(2, 0)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    def test_vertical_win_right_column(self):
        """Test vertical win in right column."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 2)  # X
        game.make_move(0, 0)  # O
        game.make_move(1, 2)  # X
        game.make_move(1, 0)  # O
        game.make_move(2, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    def test_diagonal_win_top_left_to_bottom_right(self):
        """Test diagonal win from top-left to bottom-right."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)  # X
        game.make_move(0, 1)  # O
        game.make_move(1, 1)  # X
        game.make_move(0, 2)  # O
        game.make_move(2, 2)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    def test_anti_diagonal_win_top_right_to_bottom_left(self):
        """Test anti-diagonal win from top-right to bottom-left."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 2)  # X
        game.make_move(0, 0)  # O
        game.make_move(1, 1)  # X
        game.make_move(0, 1)  # O
        game.make_move(2, 0)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
    
    def test_no_winner_when_no_win_exists(self):
        """Test that no winner exists when no win condition is met."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)  # X
        game.make_move(0, 1)  # O
        
        assert not game.is_game_over()
        assert game.get_winner() is None
    
    def test_near_win_horizontal_two_in_row(self):
        """Test that two in a row is not a win."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)  # X
        game.make_move(1, 0)  # O
        game.make_move(0, 1)  # X
        
        assert not game.is_game_over()
        assert game.get_winner() is None
    
    def test_win_on_larger_board(self):
        """Test win detection on larger board."""
        game = ConnectNGame(6, 7, 4)
        # Create horizontal win
        game.make_move(0, 0)  # X
        game.make_move(1, 0)  # O
        game.make_move(0, 1)  # X
        game.make_move(1, 1)  # O
        game.make_move(0, 2)  # X
        game.make_move(1, 2)  # O
        game.make_move(0, 3)  # X wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE


class TestDrawDetection:
    """Tests for draw detection."""
    
    def test_draw_detection(self):
        """Test that draw is detected when board is full."""
        game = ConnectNGame(3, 3, 3)
        # Fill board without winner
        # X O X
        # O X X
        # O X O
        moves = [
            (0, 0), (0, 1), (0, 2),  # X, O, X
            (1, 1), (1, 0), (2, 0),  # X, O, O
            (1, 2), (2, 2), (2, 1)   # X, O, X
        ]
        for row, col in moves:
            game.make_move(row, col)
        
        assert game.is_game_over()
        assert game.get_winner() is None
        assert game.game_status == GameStatus.DRAW
    
    def test_full_board_no_winner(self):
        """Test full board with no winner results in draw."""
        game = ConnectNGame(3, 3, 3)
        moves = [
            (0, 0), (0, 1), (0, 2),
            (1, 1), (1, 0), (2, 0),
            (1, 2), (2, 2), (2, 1)
        ]
        for row, col in moves:
            game.make_move(row, col)
        
        assert game.game_status == GameStatus.DRAW
    
    def test_no_moves_after_draw(self):
        """Test that no moves are allowed after draw."""
        game = ConnectNGame(3, 3, 3)
        moves = [
            (0, 0), (0, 1), (0, 2),
            (1, 1), (1, 0), (2, 0),
            (1, 2), (2, 2), (2, 1)
        ]
        for row, col in moves:
            game.make_move(row, col)
        
        with pytest.raises(ValueError, match="Game is over"):
            game.make_move(0, 0)


class TestReset:
    """Tests for game reset."""
    
    def test_reset_clears_board(self):
        """Test that reset clears the board."""
        game = ConnectNGame(3, 3, 3)
        game.make_move(0, 0)
        game.make_move(1, 1)
        game.reset()
        
        board = game.get_board()
        assert all(cell == ' ' for row in board for cell in row)
    
    def test_reset_after_win(self):
        """Test reset after a win."""
        game = ConnectNGame(3, 3, 3)
        # Create a win
        game.make_move(0, 0)
        game.make_move(1, 0)
        game.make_move(0, 1)
        game.make_move(1, 1)
        game.make_move(0, 2)
        
        game.reset()
        
        assert game.current_player == Player.PLAYER_ONE
        assert game.game_status == GameStatus.IN_PROGRESS
        assert game.get_winner() is None
    
    def test_reset_after_draw(self):
        """Test reset after a draw."""
        game = ConnectNGame(3, 3, 3)
        moves = [
            (0, 0), (0, 1), (0, 2),
            (1, 1), (1, 0), (2, 0),
            (1, 2), (2, 2), (2, 1)
        ]
        for row, col in moves:
            game.make_move(row, col)
        
        game.reset()
        
        assert game.current_player == Player.PLAYER_ONE
        assert game.game_status == GameStatus.IN_PROGRESS
        assert game.get_winner() is None
    
    def test_reset_preserves_configuration(self):
        """Test that reset preserves board configuration."""
        game = ConnectNGame(6, 7, 4)
        game.make_move(0, 0)
        game.reset()
        
        assert game.rows == 6
        assert game.columns == 7
        assert game.win_length == 4
