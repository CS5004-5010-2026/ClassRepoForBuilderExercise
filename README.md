# Connect-N Game: Builder Pattern Exercise

This repository contains implementations of a Connect-N game in both Java and Python, demonstrating the Builder design pattern.

## Overview

The Connect-N game is a generalization of Tic-Tac-Toe and Connect Four, where two players attempt to connect N tokens in a row (horizontally, vertically, or diagonally) on a configurable board.

This project intentionally demonstrates both:
1. **The Problem**: Multi-parameter constructors that are confusing and inflexible
2. **The Solution**: The Builder pattern for creating objects with clear, readable code

## Repository Structure

```
ClassRepoForBuilderExercise/
├── Java/                    # Java implementation
│   ├── src/
│   │   ├── main/java/connectn/
│   │   │   ├── ConnectNGame.java           # Game model (with constructor)
│   │   │   ├── ConnectNGameBuilder.java    # Builder pattern solution
│   │   │   ├── Player.java
│   │   │   ├── GameStatus.java
│   │   │   └── BuilderDemo.java
│   │   └── test/java/connectn/
│   │       ├── ConnectNGameTest.java
│   │       ├── ConnectNGamePropertyTest.java
│   │       └── ConnectNGameBuilderTest.java
│   ├── build.gradle
│   ├── README.md
│   └── BUILDER_PATTERN_DEMO.md
│
└── Python/                  # Python implementation
    ├── connectn/
    │   ├── connect_n_game.py              # Game model (with constructor)
    │   ├── connect_n_game_builder.py      # Builder pattern solution
    │   ├── player.py
    │   └── game_status.py
    ├── tests/
    │   ├── test_connect_n_game.py
    │   ├── test_connect_n_game_properties.py
    │   └── test_connect_n_game_builder.py
    ├── builder_demo.py
    ├── requirements.txt
    ├── setup.py
    ├── README.md
    └── BUILDER_PATTERN_DEMO.md
```

## Quick Start

### Java

```bash
cd Java
./gradlew build
./gradlew test
./gradlew run
```

### Python

```bash
cd Python
pip install -e ".[dev]"
pytest
python builder_demo.py
```

## The Problem: Multi-Parameter Constructor

Both implementations start with a constructor that takes multiple parameters:

**Java:**
```java
ConnectNGame game = new ConnectNGame(6, 7, 4);
// Is it (rows, columns, winLength) or (columns, rows, winLength)?
```

**Python:**
```python
game = ConnectNGame(6, 7, 4)
# Same confusion - parameter order is unclear
```

**Problems:**
- Parameter order is easy to confuse
- No default values
- Difficult to add optional parameters
- Code doesn't explain what it creates
- No way to create common configurations easily

## The Solution: Builder Pattern

The Builder pattern provides a clear, readable API:

**Java:**
```java
ConnectNGame game = new ConnectNGameBuilder()
    .rows(6)
    .columns(7)
    .winLength(4)
    .build();

// Or use presets
ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
```

**Python:**
```python
game = (ConnectNGameBuilder()
        .rows(6)
        .columns(7)
        .win_length(4)
        .build())

# Or use presets
tic_tac_toe = ConnectNGameBuilder.tic_tac_toe()
connect_four = ConnectNGameBuilder.connect_four()
```

**Benefits:**
- Self-documenting code (parameter names visible)
- Flexible parameter order
- Default values (3×3, win length 3)
- Easy to add optional parameters
- Named factory methods for common configurations
- Backward compatible when adding features

## Features

Both implementations include:
- ✅ Complete game logic (move validation, win detection, draw detection)
- ✅ Comprehensive test suites (unit + property-based tests)
- ✅ Builder pattern with 6 preset factory methods
- ✅ Full documentation and examples
- ✅ Demo programs showing the comparison

### Java Implementation
- 82 tests (JUnit 5 + jqwik)
- Gradle build system
- Full Javadoc documentation

### Python Implementation
- 84 tests (pytest + Hypothesis)
- Type hints throughout
- Google-style docstrings

## Learning Objectives

Students will learn:
1. **Design Patterns**: Understanding the Builder pattern
2. **Code Readability**: Writing self-documenting code
3. **API Design**: Creating fluent, user-friendly APIs
4. **Testing**: Both unit and property-based testing strategies
5. **Language Comparison**: Comparing Java and Python implementations

## Documentation

Each implementation has detailed documentation:
- `README.md` - Getting started and API reference
- `BUILDER_PATTERN_DEMO.md` - Detailed pattern comparison
- `BUILDER_IMPLEMENTATION_SUMMARY.md` - Implementation details

## Running the Demos

Both implementations include demo programs that show the comparison:

**Java:**
```bash
cd Java
./gradlew run
```

**Python:**
```bash
cd Python
python builder_demo.py
```

## Testing

### Java
```bash
cd Java
./gradlew test                    # Run all tests
./gradlew test --tests "*Builder*"  # Run only builder tests
```

### Python
```bash
cd Python
pytest                            # Run all tests
pytest tests/test_connect_n_game_builder.py  # Run only builder tests
pytest --cov=connectn            # Run with coverage
```

## Educational Use

This repository is designed for teaching the Builder pattern in CS5004/CS5010 courses. Students can:
1. Study the problem (multi-parameter constructor)
2. Understand the solution (Builder pattern)
3. Compare implementations across languages
4. Run comprehensive test suites
5. Experiment with the code

## Author

CS5004/CS5010 Teaching Team

## License

Educational use only - CS5004-5010-2026
