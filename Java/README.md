# Connect-N Game Model

A fully-tested Java implementation of a configurable Connect-N game model demonstrating the need for the Builder pattern.

## Overview

This project implements a Connect-N game where two players attempt to connect N tokens in a row (horizontally, vertically, or diagonally). The implementation intentionally uses a multi-parameter constructor to demonstrate a design problem that the Builder pattern solves—students will later refactor this code.

## Features

- **Configurable Board**: Specify rows, columns, and win length
- **Complete Game Logic**: Move validation, win detection (4 directions), draw detection
- **Immutable Design**: Defensive copying prevents external state modification
- **Comprehensive Testing**: Both unit tests (JUnit 5) and property-based tests (jqwik)
- **No I/O**: Pure model suitable for any interface (console, GUI, web)

## Building

This project uses Gradle. To build:

```bash
./gradlew build
```

## Running the Demo

To see a comparison of the constructor vs builder approaches:

```bash
./gradlew run
```

This runs the `BuilderDemo` program which demonstrates the advantages of the Builder pattern.

## Testing

Run all tests:

```bash
./gradlew test
```

The project includes:
- **82 unit tests**: Specific examples and edge cases (61 for ConnectNGame + 21 for Builder)
- **19 property-based tests**: Universal correctness properties tested with 100+ iterations each

## Usage Examples

### Tic-Tac-Toe (3×3, win length 3)

```java
ConnectNGame game = new ConnectNGame(3, 3, 3);

// Make moves
game.makeMove(0, 0);  // Player 1 (X) at top-left
game.makeMove(1, 1);  // Player 2 (O) at center
game.makeMove(0, 1);  // Player 1 (X)
game.makeMove(1, 0);  // Player 2 (O)
game.makeMove(0, 2);  // Player 1 (X) - wins!

// Check game state
if (game.isGameOver()) {
    Player winner = game.getWinner();
    if (winner != null) {
        System.out.println(winner + " wins!");
    } else {
        System.out.println("Draw!");
    }
}
```

### Connect Four-like (6×7, win length 4)

```java
ConnectNGame game = new ConnectNGame(6, 7, 4);

// Players alternate placing tokens
game.makeMove(0, 0);
game.makeMove(0, 1);
// ... continue playing ...

// Reset for a new game
game.reset();
```

## Design Notes

### Intentional Anti-Pattern

This implementation uses a **multi-parameter constructor**:

```java
ConnectNGame game = new ConnectNGame(6, 7, 4);
```

**Problems with this approach:**
1. Parameter order is easy to confuse (rows vs columns)
2. No way to add optional parameters (e.g., starting player, custom tokens)
3. No way to provide default values
4. Difficult to create common configurations
5. Constructor calls are verbose and unclear

### Solution: Builder Pattern

The `ConnectNGameBuilder` class provides a better alternative:

```java
// Clear, readable API
ConnectNGame game = new ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();

// Or use convenient presets
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
ConnectNGame gomoku = ConnectNGameBuilder.gomoku();

// Flexible parameter order
ConnectNGame game2 = new ConnectNGameBuilder()
    .winLength(4)
    .columns(7)
    .rows(6)
    .build();

// Default values (3x3, win length 3)
ConnectNGame defaultGame = new ConnectNGameBuilder().build();
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

```java
public ConnectNGame(int rows, int columns, int winLength)
```

Creates a new game with specified dimensions and win condition.

### Builder (Recommended)

```java
ConnectNGameBuilder builder = new ConnectNGameBuilder()
```

Creates a builder with default values (3x3 board, win length 3).

**Builder Methods:**
- `rows(int rows)` - Set number of rows
- `columns(int columns)` - Set number of columns
- `winLength(int winLength)` - Set win length
- `boardSize(int rows, int columns)` - Set both dimensions at once
- `squareBoard(int size)` - Create square board
- `build()` - Build the ConnectNGame instance

**Preset Factory Methods:**
- `ConnectNGameBuilder.ticTacToe()` - 3×3, win length 3
- `ConnectNGameBuilder.connectFour()` - 6×7, win length 4
- `ConnectNGameBuilder.gomoku()` - 15×15, win length 5
- `ConnectNGameBuilder.smallGame()` - 5×5, win length 4
- `ConnectNGameBuilder.largeGame()` - 10×10, win length 5
- `ConnectNGameBuilder.customSquare(int size, int winLength)` - Custom square board

### Game Actions

- `void makeMove(int row, int col)` - Make a move for the current player
- `boolean isValidMove(int row, int col)` - Check if a move is valid
- `void reset()` - Reset the game to initial state

### Game State

- `char[][] getBoard()` - Get a copy of the board
- `Player getCurrentPlayer()` - Get the current player
- `GameStatus getGameStatus()` - Get the game status
- `Player getWinner()` - Get the winner (if any)
- `boolean isGameOver()` - Check if the game has ended

### Configuration

- `int getRows()` - Get the number of rows
- `int getColumns()` - Get the number of columns
- `int getWinLength()` - Get the win length

## Testing Strategy

The implementation uses a dual testing approach:

1. **Unit Tests (JUnit 5)**: Specific examples demonstrating correct behavior
2. **Property-Based Tests (jqwik)**: Universal properties tested across randomized inputs

### Correctness Properties

The implementation validates 19 correctness properties:
- Constructor validation (4 properties)
- Initial state (1 property)
- Board updates and defensive copying (2 properties)
- Player switching (1 property)
- Move validation (4 properties)
- Win detection (4 properties)
- Draw detection (1 property)
- Game reset (1 property)
- No winner for non-winning games (1 property)

## Project Structure

```
ClassRepoForBuilderExercise/
├── build.gradle              # Gradle build configuration
├── settings.gradle           # Project settings
├── gradlew                   # Gradle wrapper (Unix)
├── gradlew.bat              # Gradle wrapper (Windows)
├── gradle/                  # Gradle wrapper files
├── README.md                # This file
├── BUILDER_PATTERN_DEMO.md  # Detailed Builder pattern comparison
└── src/
    ├── main/java/connectn/
    │   ├── ConnectNGame.java        # Main game model
    │   ├── ConnectNGameBuilder.java # Builder pattern implementation
    │   ├── Player.java              # Player enum
    │   ├── GameStatus.java          # Game status enum
    │   └── BuilderDemo.java         # Demo program
    └── test/java/connectn/
        ├── ConnectNGameTest.java            # Unit tests for game
        ├── ConnectNGamePropertyTest.java    # Property-based tests for game
        └── ConnectNGameBuilderTest.java     # Unit tests for builder
```

## Dependencies

- **Java 17**: Target platform
- **JUnit 5**: Unit testing framework
- **jqwik 1.8.2**: Property-based testing library

## Code Quality

- **Google Java Style Guide**: All code follows standard conventions
- **Comprehensive Javadoc**: All public classes and methods documented
- **High Test Coverage**: >90% line coverage
- **No I/O**: Pure model with no System.out, Scanner, or file operations

## Author

CS5004 Teaching Team

## Version

1.0
