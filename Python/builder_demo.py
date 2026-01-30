"""
Demonstration program comparing constructor-based and builder-based game creation.

This module shows the practical differences between using the multi-parameter
constructor versus the Builder pattern for creating ConnectNGame instances.
"""

from connectn import ConnectNGameBuilder, ConnectNGame


def describe_game(game: ConnectNGame) -> str:
    """
    Create a human-readable description of a game.
    
    Args:
        game: The game to describe
    
    Returns:
        A string describing the game configuration
    """
    return f"{game.rows}x{game.columns} board, win length {game.win_length}"


def demonstrate_constructor_approach():
    """Demonstrate the original constructor approach."""
    print("--- Constructor Approach ---")
    
    # Creating games with constructor
    tic_tac_toe = ConnectNGame(3, 3, 3)
    connect_four = ConnectNGame(6, 7, 4)
    custom = ConnectNGame(10, 10, 5)
    
    print("Tic-Tac-Toe: ConnectNGame(3, 3, 3)")
    print(f"  → {describe_game(tic_tac_toe)}")
    
    print("Connect Four: ConnectNGame(6, 7, 4)")
    print(f"  → {describe_game(connect_four)}")
    
    print("Custom: ConnectNGame(10, 10, 5)")
    print(f"  → {describe_game(custom)}")
    
    print("\nProblems:")
    print("  • Parameter order unclear (rows vs columns?)")
    print("  • Must remember exact values for common games")
    print("  • No default values")
    print("  • Code doesn't explain what it creates")


def demonstrate_builder_approach():
    """Demonstrate the Builder pattern approach."""
    print("--- Builder Pattern Approach ---")
    
    # Creating games with builder
    tic_tac_toe = (ConnectNGameBuilder()
                   .rows(3)
                   .columns(3)
                   .win_length(3)
                   .build())
    
    connect_four = (ConnectNGameBuilder()
                    .rows(6)
                    .columns(7)
                    .win_length(4)
                    .build())
    
    custom = (ConnectNGameBuilder()
              .square_board(10)
              .win_length(5)
              .build())
    
    print("Tic-Tac-Toe: (ConnectNGameBuilder()")
    print("               .rows(3).columns(3).win_length(3).build())")
    print(f"  → {describe_game(tic_tac_toe)}")
    
    print("Connect Four: (ConnectNGameBuilder()")
    print("               .rows(6).columns(7).win_length(4).build())")
    print(f"  → {describe_game(connect_four)}")
    
    print("Custom: (ConnectNGameBuilder()")
    print("         .square_board(10).win_length(5).build())")
    print(f"  → {describe_game(custom)}")
    
    print("\nBenefits:")
    print("  ✓ Self-documenting (parameter names visible)")
    print("  ✓ Flexible parameter order")
    print("  ✓ Convenience methods (square_board)")
    print("  ✓ Clear intent")


def demonstrate_preset_factories():
    """Demonstrate preset factory methods."""
    print("--- Preset Factory Methods ---")
    
    tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
    connect_four = ConnectNGameBuilder.connect_four()
    gomoku = ConnectNGameBuilder.gomoku()
    small_game = ConnectNGameBuilder.small_game()
    large_game = ConnectNGameBuilder.large_game()
    
    print("ConnectNGameBuilder.tic_tac_toe()")
    print(f"  → {describe_game(tic_tac_toe)}")
    
    print("ConnectNGameBuilder.connect_four()")
    print(f"  → {describe_game(connect_four)}")
    
    print("ConnectNGameBuilder.gomoku()")
    print(f"  → {describe_game(gomoku)}")
    
    print("ConnectNGameBuilder.small_game()")
    print(f"  → {describe_game(small_game)}")
    
    print("ConnectNGameBuilder.large_game()")
    print(f"  → {describe_game(large_game)}")
    
    print("\nBenefits:")
    print("  ✓ Memorable names")
    print("  ✓ No need to remember parameters")
    print("  ✓ One line of code")
    print("  ✓ Immediately clear what game is created")


def demonstrate_flexibility():
    """Demonstrate the flexibility of the Builder pattern."""
    print("--- Builder Flexibility ---")
    
    # Default values
    default_game = ConnectNGameBuilder().build()
    print("Default game: ConnectNGameBuilder().build()")
    print(f"  → {describe_game(default_game)}")
    
    # Partial configuration
    tall_board = ConnectNGameBuilder().rows(10).build()
    print("\nTall board: .rows(10).build()")
    print(f"  → {describe_game(tall_board)}")
    
    # Any parameter order
    any_order = (ConnectNGameBuilder()
                 .win_length(4)
                 .columns(7)
                 .rows(6)
                 .build())
    print("\nAny order: .win_length(4).columns(7).rows(6).build()")
    print(f"  → {describe_game(any_order)}")
    
    print("\nBenefits:")
    print("  ✓ Sensible defaults")
    print("  ✓ Change only what you need")
    print("  ✓ Parameter order doesn't matter")


def main():
    """Run demonstrations of both approaches."""
    print("=== Connect-N Game: Constructor vs Builder Pattern ===\n")
    
    demonstrate_constructor_approach()
    print()
    demonstrate_builder_approach()
    print()
    demonstrate_preset_factories()
    print()
    demonstrate_flexibility()


if __name__ == "__main__":
    main()
