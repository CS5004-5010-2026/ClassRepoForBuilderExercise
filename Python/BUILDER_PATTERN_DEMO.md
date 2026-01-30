# Builder Pattern Demonstration

This document demonstrates the advantages of the Builder pattern by comparing the original multi-parameter constructor approach with the Builder pattern solution.

## The Problem: Multi-Parameter Constructor

The original `ConnectNGame` class uses a multi-parameter constructor:

```java
ConnectNGame game = ConnectNGame(6, 7, 4);
```

### Issues with This Approach

1. **Parameter Order Confusion**
   ```java
   // Which is correct?
   ConnectNGame(6, 7, 4);  // rows, columns, winLength?
   ConnectNGame(7, 6, 4);  // columns, rows, winLength?
   
   // Without looking at documentation, it's impossible to know!
   ```

2. **No Default Values**
   ```java
   // Want a standard Tic-Tac-Toe? Must remember all parameters
   ConnectNGame(3, 3, 3);
   
   // Want Connect Four? Must remember these specific values
   ConnectNGame(6, 7, 4);
   ```

3. **Difficult to Add Optional Parameters**
   ```java
   // What if we want to add:
   // - Starting player (Player 1 or Player 2)
   // - Custom tokens (not just 'X' and 'O')
   // - Time limits
   // - AI difficulty
   
   // Constructor becomes unwieldy:
   ConnectNGame(6, 7, 4, Player.PLAYER_TWO, 'A', 'B', 60, Difficulty.HARD);
   // This is getting out of hand!
   ```

4. **No Named Configurations**
   ```java
   // Every time you want Tic-Tac-Toe, you must write:
   ConnectNGame(3, 3, 3);
   
   // And remember the exact parameters for Connect Four:
   ConnectNGame(6, 7, 4);
   ```

5. **Poor Readability**
   ```java
   // What does this create?
   ConnectNGame(10, 10, 5);
   
   // Is it a large strategic game? Gomoku variant? Custom configuration?
   // The code doesn't tell you!
   ```

## The Solution: Builder Pattern

The `ConnectNGameBuilder` class solves all these problems:

### 1. Clear, Self-Documenting Code

```java
// Crystal clear what each parameter means
ConnectNGame game = ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();
```

### 2. Default Values

```java
// Want a default game? Just build it!
ConnectNGame game = ConnectNGameBuilder().build();
// Creates 3x3 board with win length 3 (Tic-Tac-Toe)

// Want to change just one parameter? Easy!
ConnectNGame game = ConnectNGameBuilder()
    .rows(5)  // Only change rows, use defaults for rest
    .build();
```

### 3. Named Preset Configurations

```java
// Readable, memorable factory methods
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
ConnectNGame gomoku = ConnectNGameBuilder.gomoku();
ConnectNGame smallGame = ConnectNGameBuilder.smallGame();
ConnectNGame largeGame = ConnectNGameBuilder.largeGame();

// Custom square boards
ConnectNGame custom = ConnectNGameBuilder.customSquare(8, 5);
```

### 4. Flexible Parameter Order

```java
// Order doesn't matter - all equivalent!
ConnectNGameBuilder().rows(6).columns(7).winLength(4).build();
ConnectNGameBuilder().winLength(4).rows(6).columns(7).build();
ConnectNGameBuilder().columns(7).winLength(4).rows(6).build();
```

### 5. Easy to Extend

```java
// Future: Adding optional parameters is trivial
ConnectNGame game = ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .startingPlayer(Player.PLAYER_TWO)  // New optional parameter
    .customTokens('A', 'B')              // New optional parameter
    .timeLimit(60)                       // New optional parameter
    .build();

// Old code still works! Backward compatible!
ConnectNGame oldStyle = ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();
```

### 6. Convenience Methods

```java
// Set both dimensions at once
ConnectNGame game = ConnectNGameBuilder()
    .boardSize(8, 9)
    .winLength(5)
    .build();

// Create square boards easily
ConnectNGame square = ConnectNGameBuilder()
    .squareBoard(7)
    .winLength(4)
    .build();
```

## Side-by-Side Comparison

### Creating Tic-Tac-Toe

**Constructor Approach:**
```java
ConnectNGame game = ConnectNGame(3, 3, 3);
// What is this? Must check documentation or remember parameters
```

**Builder Approach:**
```java
ConnectNGame game = ConnectNGameBuilder.ticTacToe();
// Immediately clear what this creates!
```

### Creating Connect Four

**Constructor Approach:**
```java
ConnectNGame game = ConnectNGame(6, 7, 4);
// Is it 6 rows and 7 columns, or vice versa?
```

**Builder Approach:**
```java
ConnectNGame game = ConnectNGameBuilder.connectFour();
// Or, if you want to be explicit:
ConnectNGame game = ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();
```

### Creating Custom Configuration

**Constructor Approach:**
```java
ConnectNGame game = ConnectNGame(10, 10, 5);
// What kind of game is this?
```

**Builder Approach:**
```java
ConnectNGame game = ConnectNGameBuilder()
    .squareBoard(10)
    .winLength(5)
    .build();
// Clearly a 10x10 square board with win length 5
```

## Key Benefits Summary

| Aspect | Constructor | Builder |
|--------|-------------|---------|
| **Readability** | Poor - parameters unclear | Excellent - self-documenting |
| **Parameter Order** | Fixed, easy to confuse | Flexible, any order |
| **Default Values** | None | Yes, sensible defaults |
| **Presets** | Must remember values | Named factory methods |
| **Extensibility** | Breaks existing code | Backward compatible |
| **Validation** | At construction time | At build() time |
| **Reusability** | Create new each time | Can reuse builder |

## When to Use Builder Pattern

Use the Builder pattern when:

1. **Multiple Parameters**: Class has 4+ constructor parameters
2. **Optional Parameters**: Some parameters have sensible defaults
3. **Parameter Confusion**: Parameters of same type (e.g., multiple ints)
4. **Immutable Objects**: Building complex immutable objects
5. **Preset Configurations**: Common configurations should be easy to create
6. **Future Growth**: Class likely to gain more parameters over time

## Implementation Notes

The `ConnectNGameBuilder` demonstrates several best practices:

1. **Fluent Interface**: Methods return `this` for chaining
2. **Sensible Defaults**: 3x3 board with win length 3 (Tic-Tac-Toe)
3. **Factory Methods**: Static methods for common configurations
4. **Convenience Methods**: `boardSize()`, `squareBoard()` for common patterns
5. **Validation Delegation**: Builder delegates validation to constructor
6. **Immutability**: Built objects are immutable (ConnectNGame design)

## Exercise for Students

Try refactoring the following constructor-based code to use the Builder pattern:

```java
// Original code
ConnectNGame game1 = ConnectNGame(3, 3, 3);
ConnectNGame game2 = ConnectNGame(6, 7, 4);
ConnectNGame game3 = ConnectNGame(15, 15, 5);
ConnectNGame game4 = ConnectNGame(5, 5, 4);
ConnectNGame game5 = ConnectNGame(10, 10, 5);

// Your task: Rewrite using ConnectNGameBuilder
// Hint: Use preset factory methods where appropriate!
```

<details>
<summary>Click to see solution</summary>

```java
// Refactored code using Builder pattern
ConnectNGame game1 = ConnectNGameBuilder.ticTacToe();
ConnectNGame game2 = ConnectNGameBuilder.connectFour();
ConnectNGame game3 = ConnectNGameBuilder.gomoku();
ConnectNGame game4 = ConnectNGameBuilder.smallGame();
ConnectNGame game5 = ConnectNGameBuilder.largeGame();

// Much more readable and maintainable!
```
</details>

## Conclusion

The Builder pattern transforms unclear, error-prone constructor calls into readable, self-documenting code. It provides flexibility, extensibility, and maintainability while making the codebase more pleasant to work with.

The small investment in creating a builder class pays dividends in code quality and developer experience.
