"""Unit tests for ConnectNGameBuilder."""

import pytest
from connectn import ConnectNGame, ConnectNGameBuilder, Player, GameStatus


class TestBasicBuilder:
    """Tests for basic builder functionality."""
    
    def test_builder_with_all_parameters(self):
        """Test builder with all parameters set."""
        game = (ConnectNGameBuilder()
                .rows(6)
                .columns(7)
                .win_length(4)
                .build())
        
        assert game.rows == 6
        assert game.columns == 7
        assert game.win_length == 4
        assert game.current_player == Player.PLAYER_ONE
        assert game.game_status == GameStatus.IN_PROGRESS
    
    def test_builder_with_defaults(self):
        """Test builder with default values."""
        game = ConnectNGameBuilder().build()
        
        assert game.rows == 3
        assert game.columns == 3
        assert game.win_length == 3
    
    def test_builder_method_chaining(self):
        """Test that all methods return the builder for chaining."""
        builder = ConnectNGameBuilder()
        
        assert builder.rows(5) is builder
        assert builder.columns(6) is builder
        assert builder.win_length(4) is builder
    
    def test_builder_partial_configuration(self):
        """Test builder with partial configuration."""
        # Set only rows, use defaults for others
        game1 = ConnectNGameBuilder().rows(5).build()
        assert game1.rows == 5
        assert game1.columns == 3  # Default
        assert game1.win_length == 3  # Default
        
        # Set only columns
        game2 = ConnectNGameBuilder().columns(7).build()
        assert game2.rows == 3  # Default
        assert game2.columns == 7
        assert game2.win_length == 3  # Default


class TestConvenienceMethods:
    """Tests for convenience methods."""
    
    def test_board_size_method(self):
        """Test board_size convenience method."""
        game = (ConnectNGameBuilder()
                .board_size(8, 9)
                .win_length(5)
                .build())
        
        assert game.rows == 8
        assert game.columns == 9
        assert game.win_length == 5
    
    def test_square_board_method(self):
        """Test square_board convenience method."""
        game = (ConnectNGameBuilder()
                .square_board(7)
                .win_length(4)
                .build())
        
        assert game.rows == 7
        assert game.columns == 7
        assert game.win_length == 4


class TestPresetFactoryMethods:
    """Tests for preset factory methods."""
    
    def test_tic_tac_toe_preset(self):
        """Test tic_tac_toe preset."""
        game = ConnectNGameBuilder.tic_tac_toe()
        
        assert game.rows == 3
        assert game.columns == 3
        assert game.win_length == 3
        assert game.current_player == Player.PLAYER_ONE
        assert game.game_status == GameStatus.IN_PROGRESS
    
    def test_connect_four_preset(self):
        """Test connect_four preset."""
        game = ConnectNGameBuilder.connect_four()
        
        assert game.rows == 6
        assert game.columns == 7
        assert game.win_length == 4
    
    def test_gomoku_preset(self):
        """Test gomoku preset."""
        game = ConnectNGameBuilder.gomoku()
        
        assert game.rows == 15
        assert game.columns == 15
        assert game.win_length == 5
    
    def test_small_game_preset(self):
        """Test small_game preset."""
        game = ConnectNGameBuilder.small_game()
        
        assert game.rows == 5
        assert game.columns == 5
        assert game.win_length == 4
    
    def test_large_game_preset(self):
        """Test large_game preset."""
        game = ConnectNGameBuilder.large_game()
        
        assert game.rows == 10
        assert game.columns == 10
        assert game.win_length == 5
    
    def test_custom_square_preset(self):
        """Test custom_square preset."""
        game = ConnectNGameBuilder.custom_square(8, 5)
        
        assert game.rows == 8
        assert game.columns == 8
        assert game.win_length == 5


class TestValidation:
    """Tests for validation (delegated to ConnectNGame)."""
    
    def test_builder_validates_invalid_rows(self):
        """Test that builder validates invalid rows."""
        builder = ConnectNGameBuilder().rows(0).columns(5).win_length(3)
        with pytest.raises(ValueError):
            builder.build()
    
    def test_builder_validates_invalid_columns(self):
        """Test that builder validates invalid columns."""
        builder = ConnectNGameBuilder().rows(5).columns(-1).win_length(3)
        with pytest.raises(ValueError):
            builder.build()
    
    def test_builder_validates_invalid_win_length(self):
        """Test that builder validates invalid win length."""
        builder = ConnectNGameBuilder().rows(5).columns(5).win_length(0)
        with pytest.raises(ValueError):
            builder.build()
    
    def test_builder_validates_win_length_too_large(self):
        """Test that builder validates win length too large."""
        builder = ConnectNGameBuilder().rows(3).columns(5).win_length(6)
        with pytest.raises(ValueError):
            builder.build()


class TestFunctional:
    """Functional tests for built games."""
    
    def test_built_game_is_fully_functional(self):
        """Test that built game is fully functional."""
        game = (ConnectNGameBuilder()
                .rows(3)
                .columns(3)
                .win_length(3)
                .build())
        
        # Make some moves
        game.make_move(0, 0)  # Player 1
        game.make_move(1, 0)  # Player 2
        game.make_move(0, 1)  # Player 1
        game.make_move(1, 1)  # Player 2
        game.make_move(0, 2)  # Player 1 wins
        
        assert game.is_game_over()
        assert game.get_winner() == Player.PLAYER_ONE
        assert game.game_status == GameStatus.PLAYER_ONE_WON
    
    def test_multiple_games_from_same_builder(self):
        """Test building multiple games from the same builder."""
        builder = ConnectNGameBuilder().rows(4).columns(4).win_length(3)
        
        # Build multiple games
        game1 = builder.build()
        game2 = builder.build()
        
        # Verify they are independent instances
        assert game1 is not game2
        
        # Make a move in game1
        game1.make_move(0, 0)
        
        # Verify game2 is unaffected
        board2 = game2.get_board()
        assert board2[0][0] == ' '
    
    def test_builder_can_be_reused(self):
        """Test that builder can be reused and modified."""
        builder = ConnectNGameBuilder().rows(5).columns(5).win_length(4)
        
        game1 = builder.build()
        
        # Modify builder and create another game
        game2 = builder.rows(6).columns(7).build()
        
        # Verify first game unchanged
        assert game1.rows == 5
        assert game1.columns == 5
        
        # Verify second game has new configuration
        assert game2.rows == 6
        assert game2.columns == 7


class TestComparison:
    """Tests comparing builder with constructor."""
    
    def test_builder_vs_constructor_equivalence(self):
        """Test that builder creates equivalent game to constructor."""
        # Create game with constructor
        constructor_game = ConnectNGame(6, 7, 4)
        
        # Create equivalent game with builder
        builder_game = (ConnectNGameBuilder()
                       .rows(6)
                       .columns(7)
                       .win_length(4)
                       .build())
        
        # Verify they have the same configuration
        assert constructor_game.rows == builder_game.rows
        assert constructor_game.columns == builder_game.columns
        assert constructor_game.win_length == builder_game.win_length
        assert constructor_game.current_player == builder_game.current_player
        assert constructor_game.game_status == builder_game.game_status
    
    def test_builder_readability(self):
        """Test that builder provides better readability."""
        # Constructor approach - parameter order unclear
        constructor_game = ConnectNGame(6, 7, 4)
        
        # Builder approach - crystal clear
        builder_game = (ConnectNGameBuilder()
                       .rows(6)        # Clearly rows
                       .columns(7)     # Clearly columns
                       .win_length(4)  # Clearly win length
                       .build())
        
        # Both create the same game
        assert constructor_game.rows == builder_game.rows
        assert constructor_game.columns == builder_game.columns
        assert constructor_game.win_length == builder_game.win_length
