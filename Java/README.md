# Connect-N Game Model - Java Starter Code

A fully-tested Java implementation of a configurable Connect-N game model. Your task is to implement the Builder pattern to improve the API.

## Overview

This project implements a Connect-N game where two players attempt to connect N tokens in a row (horizontally, vertically, or diagonally). The implementation uses a multi-parameter constructor that demonstrates a design problem—you will implement the Builder pattern to solve it.

## What's Provided

- **Complete Game Model**: `ConnectNGame` class with full game logic
- **Supporting Classes**: `Player` enum, `GameStatus` enum
- **Comprehensive Tests**: 61 unit tests + 19 property-based tests (all passing)
- **Build System**: Gradle configuration

## Your Task

Implement a `ConnectNGameBuilder` class that provides a fluent API for creating game instances.

### Requirements

1. **Fluent API with method chaining**:
   - `rows(int)` - set number of rows
   - `columns(int)` - set number of columns
   - `winLength(int)` - set win length
   - `boardSize(int)` - set square board (rows = columns)
   - `squareBoard(int, int)` - set square board with win length
   - `build()` - create the game instance

2. **Default values**: 3×3 board, win length 3

3. **Validation**: Ensure valid configurations before building

4. **Factory methods** (static methods that return pre-configured games):
   - `ticTacToe()` - 3×3, win 3
   - `connectFour()` - 6×7, win 4
   - `gomoku()` - 15×15, win 5
   - `smallGame()` - 5×5, win 3
   - `largeGame()` - 10×10, win 5
   - `customSquare(int size, int winLength)` - custom square board

5. **Tests**: Write comprehensive tests in `ConnectNGameBuilderTest.java`

### Example Usage (What You Should Implement)

```java
// Using fluent API
ConnectNGame game = new ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();

// Using factory methods
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
ConnectNGame connectFour = ConnectNGameBuilder.connectFour();

// Using convenience methods
ConnectNGame square = new ConnectNGameBuilder()
    .squareBoard(5, 4)  // 5×5 board, win length 4
    .build();
```

## Building

```bash
./gradlew build
```

## Testing

Run the existing tests to verify the game model works:

```bash
./gradlew test
```

The project includes:
- **61 unit tests**: Specific examples and edge cases for ConnectNGame
- **19 property-based tests**: Universal correctness properties

After implementing your Builder, add tests for it and run again.

## The Problem: Multi-Parameter Constructor

The current `ConnectNGame` constructor has several issues:

```java
ConnectNGame game = new ConnectNGame(6, 7, 4);
// Is it (rows, columns, winLength) or (columns, rows, winLength)?
// What if I want default values?
// How do I create common configurations easily?
```

**Problems**:
- Parameter order is confusing
- No default values
- Can't skip parameters
- Not self-documenting
- Hard to add optional parameters later

## The Solution: Builder Pattern

Your Builder implementation should solve these problems by providing:
- Clear, self-documenting API
- Flexible parameter order
- Default values
- Easy-to-use factory methods
- Extensibility for future parameters

## Project Structure

```
Java/
├── src/
│   ├── main/java/connectn/
│   │   ├── ConnectNGame.java      # Complete game model
│   │   ├── Player.java            # Player enum
│   │   └── GameStatus.java        # Game status enum
│   └── test/java/connectn/
│       ├── ConnectNGameTest.java          # Unit tests
│       └── ConnectNGamePropertyTest.java  # Property-based tests
├── build.gradle
└── README.md
```

## Game Model API

### ConnectNGame Constructor

```java
public ConnectNGame(int rows, int columns, int winLength)
```

### Key Methods

- `makeMove(int column)` - Make a move in the specified column
- `getStatus()` - Get current game status (IN_PROGRESS, X_WON, O_WON, DRAW)
- `getCurrentPlayer()` - Get current player (X or O)
- `getBoard()` - Get copy of current board state
- `getRows()`, `getColumns()`, `getWinLength()` - Get configuration

## Testing Your Implementation

When you implement your Builder, write tests that verify:

1. **Fluent API works**: Method chaining returns builder
2. **Default values**: Building without setting values uses defaults
3. **Validation**: Invalid configurations throw exceptions
4. **Factory methods**: All presets create correct configurations
5. **Build creates correct game**: Resulting game has expected configuration

## Solution

Check the `solutions` branch to see a complete reference implementation:

```bash
git checkout solutions
```

## License

Educational use only - CS5004-5010-2026
