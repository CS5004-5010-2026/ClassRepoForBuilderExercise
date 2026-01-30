# Connect-N Game: Builder Pattern Exercise

This repository contains a hands-on exercise for learning the Builder design pattern through implementing it in both Java and Python.

## Overview

The Connect-N game is a generalization of Tic-Tac-Toe and Connect Four, where two players attempt to connect N tokens in a row (horizontally, vertically, or diagonally) on a configurable board.

## Exercise Goals

This exercise teaches the Builder pattern by having you experience both the problem and the solution:

1. **Experience the Problem**: Work with code that uses multi-parameter constructors
2. **Implement the Solution**: Create a Builder class that solves the design issues
3. **Compare Approaches**: See how the same pattern works in Java and Python
4. **Write Tests**: Practice test-driven development with your Builder
5. **Learn Best Practices**: Understand fluent APIs, factory methods, and validation

## The Problem

The current `ConnectNGame` constructor takes multiple parameters:

```java
// Java
ConnectNGame game = new ConnectNGame(6, 7, 4);
```

```python
# Python
game = ConnectNGame(6, 7, 4)
```

**Issues with this approach**:
- **Unclear parameter order**: Is it (rows, columns, winLength) or (columns, rows, winLength)?
- **No default values**: Must specify all parameters every time
- **Not self-documenting**: Code doesn't explain what it creates
- **Hard to extend**: Adding optional parameters breaks existing code
- **Error-prone**: Easy to swap parameters accidentally

## Your Task

Implement a `ConnectNGameBuilder` class in both Java and Python that provides a clear, readable API:

**Java** - Create `ConnectNGameBuilder.java`:
```java
ConnectNGame game = new ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();

// Or use presets
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
```

**Python** - Create `connect_n_game_builder.py`:
```python
game = (ConnectNGameBuilder()
        .rows(6)
        .columns(7)
        .win_length(4)
        .build())

# Or use presets
tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
```

## Branches

- **main**: Starter code (you are here) - contains the game model without Builder pattern
- **solutions**: Complete reference implementation with Builder pattern

## Exercise Structure

This exercise is designed to be completed in stages:

### Stage 1: Understand the Problem (15-20 minutes)
1. Clone the repository and explore the code
2. Run the existing tests to see the game model works
3. Try creating different game configurations using the constructor
4. Notice how confusing and inflexible the constructor approach is

### Stage 2: Design Your Builder (10-15 minutes)
1. Sketch out the Builder class API
2. Decide what methods you need (rows, columns, winLength, build, etc.)
3. Plan your factory methods (ticTacToe, connectFour, etc.)
4. Think about validation and error handling

### Stage 3: Implement the Builder (30-45 minutes)
1. Create the Builder class with instance variables
2. Implement fluent setter methods that return `this`
3. Implement the `build()` method with validation
4. Add factory methods for common configurations
5. Handle edge cases and invalid inputs

### Stage 4: Write Tests (20-30 minutes)
1. Test the fluent API works correctly
2. Test default values are applied
3. Test validation catches invalid configurations
4. Test factory methods create correct games
5. Test that built games work correctly

### Stage 5: Compare Implementations (10-15 minutes)
1. If you did both Java and Python, compare your implementations
2. Check the solutions branch to see the reference implementation
3. Identify differences and improvements
4. Understand language-specific patterns

**Total Time**: 1.5 - 2.5 hours per language

## What You'll Learn

By completing this exercise, you will:

1. **Understand the Builder Pattern**:
   - When to use it (complex object creation)
   - How to implement it (fluent API, validation, factory methods)
   - Why it's better than multi-parameter constructors

2. **Practice Fluent APIs**:
   - Method chaining
   - Returning `this` from setters
   - Creating readable, self-documenting code

3. **Learn Factory Methods**:
   - Static factory methods for common configurations
   - Encapsulating complex creation logic
   - Providing convenient shortcuts

4. **Improve Testing Skills**:
   - Testing builder behavior
   - Validating object creation
   - Writing comprehensive test suites

5. **Compare Languages**:
   - See how the same pattern works in Java and Python
   - Understand language-specific idioms
   - Appreciate different approaches to the same problem

## Repository Structure

```
ClassRepoForBuilderExercise/
├── Java/                    # Java starter code
│   ├── src/
│   │   ├── main/java/connectn/
│   │   │   ├── ConnectNGame.java           # Game model (needs Builder)
│   │   │   ├── Player.java                 # Player enum (X, O)
│   │   │   └── GameStatus.java             # Game status enum
│   │   └── test/java/connectn/
│   │       ├── ConnectNGameTest.java       # 61 unit tests
│   │       └── ConnectNGamePropertyTest.java  # 19 property tests
│   ├── build.gradle         # Gradle build configuration
│   └── README.md            # Java-specific instructions
│
└── Python/                  # Python starter code
    ├── connectn/
    │   ├── connect_n_game.py              # Game model (needs Builder)
    │   ├── player.py                      # Player enum (X, O)
    │   └── game_status.py                 # Game status enum
    ├── tests/
    │   ├── test_connect_n_game.py         # 45 unit tests
    │   └── test_connect_n_game_properties.py  # 18 property tests
    ├── requirements.txt     # Python dependencies
    ├── setup.py             # Package setup
    └── README.md            # Python-specific instructions
```

## Quick Start

### Prerequisites

**Java**:
- JDK 17 or higher
- Gradle (wrapper included)

**Python**:
- Python 3.8 or higher
- pip

### Running Tests

**Java**:
```bash
cd Java
./gradlew build    # Build the project
./gradlew test     # Run tests (61 unit + 19 property tests)
```

**Python**:
```bash
cd Python
pip install -e ".[dev]"    # Install in development mode
pytest                      # Run tests (45 unit + 18 property tests)
```

All tests should pass - they verify the game model works correctly.

## Implementation Requirements

Your Builder implementation must include:

### 1. Fluent API Methods

**Java**:
```java
public ConnectNGameBuilder rows(int rows)
public ConnectNGameBuilder columns(int columns)
public ConnectNGameBuilder winLength(int winLength)
public ConnectNGameBuilder boardSize(int size)  // Square board
public ConnectNGameBuilder squareBoard(int size, int winLength)
public ConnectNGame build()
```

**Python**:
```python
def rows(self, rows: int) -> 'ConnectNGameBuilder'
def columns(self, columns: int) -> 'ConnectNGameBuilder'
def win_length(self, win_length: int) -> 'ConnectNGameBuilder'
def board_size(self, size: int) -> 'ConnectNGameBuilder'  # Square board
def square_board(self, size: int, win_length: int) -> 'ConnectNGameBuilder'
def build(self) -> ConnectNGame
```

### 2. Default Values

- Rows: 3
- Columns: 3
- Win Length: 3

### 3. Validation Rules

The `build()` method must validate:
- Rows ≥ 1
- Columns ≥ 1
- Win length ≥ 1
- Win length ≤ min(rows, columns)

Throw `IllegalArgumentException` (Java) or `ValueError` (Python) for invalid configurations.

### 4. Factory Methods (Static)

Implement at least these presets:

| Method | Board Size | Win Length | Description |
|--------|-----------|------------|-------------|
| `ticTacToe()` | 3×3 | 3 | Classic Tic-Tac-Toe |
| `connectFour()` | 6×7 | 4 | Classic Connect Four |
| `gomoku()` | 15×15 | 5 | Japanese board game |
| `smallGame()` | 5×5 | 3 | Small practice game |
| `largeGame()` | 10×10 | 5 | Large game |
| `customSquare(size, winLength)` | size×size | winLength | Custom square board |

### 5. Test Coverage

Write tests for:
- ✅ Fluent API returns builder for chaining
- ✅ Default values are applied when not specified
- ✅ Custom values override defaults
- ✅ Validation catches invalid configurations
- ✅ Factory methods create correct games
- ✅ Built games work correctly (can make moves, detect wins)

## Example Usage

### Basic Usage

**Java**:
```java
// Custom configuration
ConnectNGame game = new ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();

// Using defaults (3×3, win 3)
ConnectNGame defaultGame = new ConnectNGameBuilder().build();

// Square board shortcut
ConnectNGame square = new ConnectNGameBuilder()
    .squareBoard(5, 4)
    .build();
```

**Python**:
```python
# Custom configuration
game = (ConnectNGameBuilder()
        .rows(6)
        .columns(7)
        .win_length(4)
        .build())

# Using defaults (3×3, win 3)
default_game = ConnectNGameBuilder().build()

# Square board shortcut
square = (ConnectNGameBuilder()
          .square_board(5, 4)
          .build())
```

### Factory Methods

**Java**:
```java
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
ConnectNGame gomoku = ConnectNGameBuilder.gomoku();
ConnectNGame custom = ConnectNGameBuilder.customSquare(8, 4);
```

**Python**:
```python
tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
connect_four = ConnectNGameBuilder.connect_four()
gomoku = ConnectNGameBuilder.gomoku()
custom = ConnectNGameBuilder.custom_square(8, 4)
```

## Tips for Success

### Design Tips

1. **Start Simple**: Implement basic fluent methods first, then add factory methods
2. **Return `this`**: Each setter method should return the builder instance for chaining
3. **Store State**: Use instance variables to store rows, columns, winLength
4. **Validate Late**: Do validation in `build()`, not in setters (allows flexible order)
5. **Immutable Result**: The built game should be immutable (it already is)

### Testing Tips

1. **Test Incrementally**: Write tests as you implement features
2. **Test Edge Cases**: Try invalid configurations, boundary values
3. **Test Chaining**: Verify method chaining works correctly
4. **Test Independence**: Each test should be independent
5. **Use Assertions**: Make clear assertions about expected behavior

### Common Pitfalls

1. **Forgetting to return `this`**: Setters must return the builder for chaining
2. **Validating too early**: Validate in `build()`, not in setters
3. **Not copying defaults**: Factory methods should use the builder, not constructor directly
4. **Missing validation**: Always validate before creating the game
5. **Mutable state**: Don't let the builder be reused after `build()` (optional enhancement)

## Checking Your Work

### Self-Assessment Checklist

- [ ] Builder class created with correct name
- [ ] All required fluent methods implemented
- [ ] Methods return builder for chaining
- [ ] Default values work correctly
- [ ] Validation throws appropriate exceptions
- [ ] All 6+ factory methods implemented
- [ ] Factory methods create correct configurations
- [ ] Comprehensive tests written
- [ ] All tests pass
- [ ] Code is clean and well-documented

### Compare with Solutions

Once you've completed your implementation:

```bash
git checkout solutions
```

Compare your implementation with the reference solution:
- **Java**: 82 total tests (61 game + 21 builder)
- **Python**: 84 total tests (63 game + 21 builder)

Look for:
- Different approaches to the same problem
- Additional features or methods
- Better validation or error messages
- More comprehensive tests

## What's Provided

Both implementations include complete, tested game models:

### Java Implementation
- ✅ Complete `ConnectNGame` class with all game logic
- ✅ 61 unit tests covering all functionality
- ✅ 19 property-based tests using jqwik
- ✅ Full Javadoc documentation
- ✅ Gradle build system

### Python Implementation
- ✅ Complete `ConnectNGame` class with all game logic
- ✅ 45 unit tests covering all functionality
- ✅ 18 property-based tests using Hypothesis
- ✅ Type hints throughout
- ✅ Google-style docstrings
- ✅ pytest configuration

## Learning Resources

### Builder Pattern Resources

- **Design Patterns** (Gang of Four): Original Builder pattern description
- **Effective Java** (Joshua Bloch): Item 2 - "Consider a builder when faced with many constructor parameters"
- **Refactoring Guru**: [Builder Pattern](https://refactoring.guru/design-patterns/builder)

### Language-Specific Resources

**Java**:
- Method chaining and fluent APIs
- Static factory methods
- `IllegalArgumentException` for validation

**Python**:
- Returning `self` for method chaining
- Type hints with `-> 'ClassName'` for forward references
- `@staticmethod` decorator for factory methods
- `ValueError` for validation

## Educational Context

This exercise is designed for CS5004/CS5010 courses to teach:

1. **Design Patterns**: Practical application of the Builder pattern
2. **API Design**: Creating user-friendly, self-documenting APIs
3. **Code Quality**: Writing clean, maintainable, testable code
4. **Testing**: Comprehensive unit and property-based testing
5. **Language Comparison**: Understanding how patterns work across languages

## Support

If you get stuck:

1. **Review the Requirements**: Make sure you understand what's needed
2. **Check the Tests**: Look at existing tests for examples
3. **Read the Game Model**: Understand how `ConnectNGame` works
4. **Start Small**: Implement one method at a time
5. **Check Solutions**: The `solutions` branch has a complete implementation

## Author

CS5004/CS5010 Teaching Team

## License

Educational use only - CS5004-5010-2026
