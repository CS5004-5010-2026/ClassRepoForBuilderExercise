# Connect-N Game Model (Python)

A fully-tested Python implementation of a configurable Connect-N game model demonstrating the Builder pattern.

## Overview

This project implements a Connect-N game where two players attempt to connect N tokens in a row (horizontally, vertically, or diagonally). The implementation intentionally uses a multi-parameter constructor to demonstrate a design problem that the Builder pattern solves—students will later use the Builder.

## Features

- **Configurable Board**: Specify rows, columns, and win length
- **Complete Game Logic**: Move validation, win detection (4 directions), draw detection
- **Immutable Design**: Defensive copying prevents external state modification
- **Comprehensive Testing**: Both unit tests (pytest) and property-based tests (Hypothesis)
- **No I/O**: Pure model suitable for any interface (console, GUI, web)
- **Type Hints**: Full type annotations for better IDE support

## Installation

### Using pip (recommended)

```bash
# Install in development mode with test dependencies
pip install -e ".[dev]"
```

### Manual installation

```bash
# Install dependencies
pip install -r requirements.txt
```

## Running Tests

Run all tests:

```bash
pytest
```

Run with coverage:

```bash
pytest --cov=connectn --cov-report=html
```

Run only unit tests:

```bash
pytest tests/test_connect_n_game.py tests/test_connect_n_game_builder.py
```

Run only property-based tests:

```bash
pytest tests/test_connect_n_game_properties.py
```

The project includes:
- **60+ unit tests**: Specific examples and edge cases
- **15+ property-based tests**: Universal correctness properties tested with 100+ iterations each

## Running the Demo

To see a comparison of the constructor vs builder approaches:

```bash
python builder_demo.py
```

This runs the demonstration program which shows the advantages of the Builder pattern.

## Usage Examples

### Tic-Tac-Toe (3×3, win length 3)

```python
from connectn import ConnectNGame, ConnectNGameBuilder

# Using constructor (original approach)
game = ConnectNGame(3, 3, 3)

# Make moves
game.make_move(0, 0)  # Player 1 (X) at top-left
game.make_move(1, 1)  # Player 2 (O) at center
game.make_move(0, 1)  # Player 1 (X)
game.make_move(1, 0)  # Player 2 (O)
game.make_move(0, 2)  # Player 1 (X) - wins!

# Check game state
if game.is_game_over():
    winner = game.get_winner()
    if winner:
        print(f"{winner} wins!")
    else:
        print("Draw!")
```

### Connect Four-like (6×7, win length 4)

```python
# Using constructor
game = ConnectNGame(6, 7, 4)

# Players alternate placing tokens
game.make_move(0, 0)
game.make_move(0, 1)
# ... continue playing ...

# Reset for a new game
game.reset()
```

## Design Notes

### Intentional Anti-Pattern

This implementation uses a **multi-parameter constructor**:

```python
game = ConnectNGame(6, 7, 4)
```

**Problems with this approach:**
1. Parameter order is easy to confuse (rows vs columns)
2. No way to add optional parameters (e.g., starting player, custom tokens)
3. No way to provide default values
4. Difficult to create common configurations
5. Constructor calls are verbose and unclear

### Solution: Builder Pattern

The `ConnectNGameBuilder` class provides a better alternative:

```python
# Clear, readable API
game = (ConnectNGameBuilder()
        .rows(6)
        .columns(7)
        .win_length(4)
        .build())

# Or use convenient presets
tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
connect_four = ConnectNGameBuilder.connect_four()
gomoku = ConnectNGameBuilder.gomoku()

# Flexible parameter order
game2 = (ConnectNGameBuilder()
         .win_length(4)
         .columns(7)
         .rows(6)
         .build())

# Default values (3x3, win length 3)
default_game = ConnectNGameBuilder().build()
```

**Benefits of the Builder Pattern:**
- Self-documenting code (parameter names are clear)
- Flexible parameter order
- Default values for common configurations
- Easy to add optional parameters in the future
- Named factory methods for standard games
- Backward compatible when adding new features

See [BUILDER_PATTERN_DEMO.md](BUILDER_PATTERN_DEMO.md) for a detailed comparison.

## API Reference

### Constructor (Original)

```python
ConnectNGame(rows: int, columns: int, win_length: int)
```

Creates a new game with specified dimensions and win condition.

### Builder (Recommended)

```python
builder = ConnectNGameBuilder()
```

Creates a builder with default values (3x3 board, win length 3).

**Builder Methods:**
- `rows(rows: int)` - Set number of rows
- `columns(columns: int)` - Set number of columns
- `win_length(win_length: int)` - Set win length
- `board_size(rows: int, columns: int)` - Set both dimensions at once
- `square_board(size: int)` - Create square board
- `build()` - Build the ConnectNGame instance

**Preset Factory Methods:**
- `ConnectNGameBuilder.tic_tac_toe()` - 3×3, win length 3
- `ConnectNGameBuilder.connect_four()` - 6×7, win length 4
- `ConnectNGameBuilder.gomoku()` - 15×15, win length 5
- `ConnectNGameBuilder.small_game()` - 5×5, win length 4
- `ConnectNGameBuilder.large_game()` - 10×10, win length 5
- `ConnectNGameBuilder.custom_square(size, win_length)` - Custom square board

### Game Actions

- `make_move(row: int, col: int)` - Make a move for the current player
- `is_valid_move(row: int, col: int)` - Check if a move is valid
- `reset()` - Reset the game to initial state

### Game State

- `get_board()` - Get a copy of the board
- `current_player` - Get the current player (property)
- `game_status` - Get the game status (property)
- `get_winner()` - Get the winner (if any)
- `is_game_over()` - Check if the game has ended

### Configuration

- `rows` - Get the number of rows (property)
- `columns` - Get the number of columns (property)
- `win_length` - Get the win length (property)

## Testing Strategy

The implementation uses a dual testing approach:

1. **Unit Tests (pytest)**: Specific examples demonstrating correct behavior
2. **Property-Based Tests (Hypothesis)**: Universal properties tested across randomized inputs

### Correctness Properties

The implementation validates 15+ correctness properties:
- Constructor validation (5 properties)
- Initial state (1 property)
- Board updates and defensive copying (1 property)
- Move validation (2 properties)
- Move execution (3 properties)
- Win detection (4 properties)
- Draw detection (1 property)
- Game reset (1 property)

## Project Structure

```
Python/
├── connectn/                    # Main package
│   ├── __init__.py             # Package initialization
│   ├── connect_n_game.py       # Main game model
│   ├── connect_n_game_builder.py  # Builder pattern implementation
│   ├── player.py               # Player enum
│   └── game_status.py          # Game status enum
├── tests/                       # Test package
│   ├── __init__.py
│   ├── test_connect_n_game.py  # Unit tests for game
│   ├── test_connect_n_game_builder.py  # Unit tests for builder
│   └── test_connect_n_game_properties.py  # Property-based tests
├── builder_demo.py              # Demo program
├── requirements.txt             # Dependencies
├── setup.py                     # Package setup
├── README.md                    # This file
└── BUILDER_PATTERN_DEMO.md      # Detailed Builder pattern comparison
```

## Dependencies

- **Python 3.8+**: Target platform
- **pytest**: Unit testing framework
- **hypothesis**: Property-based testing library
- **pytest-cov**: Coverage reporting (optional)

## Code Quality

- **PEP 8**: All code follows Python style guidelines
- **Type Hints**: Full type annotations throughout
- **Docstrings**: All public classes and methods documented
- **High Test Coverage**: >90% line coverage
- **No I/O**: Pure model with no print statements or file operations

## Python-Specific Features

This Python implementation demonstrates:
- **Properties**: Using `@property` decorator for getters
- **Enums**: Using Python's `Enum` class for Player and GameStatus
- **Type Hints**: Full type annotations for better IDE support
- **List Comprehensions**: Pythonic iteration patterns
- **Defensive Copying**: Using list slicing for defensive copies
- **Context Managers**: (Can be added for resource management)
- **Dunder Methods**: `__str__` for string representation

## Comparison with Java Implementation

| Feature | Java | Python |
|---------|------|--------|
| **Type System** | Static, compile-time | Dynamic, with optional type hints |
| **Properties** | Getter methods | `@property` decorator |
| **Enums** | Enum class with methods | Enum class with properties |
| **Testing** | JUnit + jqwik | pytest + Hypothesis |
| **Build Tool** | Gradle | pip/setuptools |
| **Null Safety** | `null` | `None` with `Optional` |
| **Immutability** | `final` keyword | Convention (no built-in) |

## Author

CS5004 Teaching Team

## Version

1.0.0
