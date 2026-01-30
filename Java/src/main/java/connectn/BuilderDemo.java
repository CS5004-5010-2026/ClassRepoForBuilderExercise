package connectn;

/**
 * Demonstration program comparing constructor-based and builder-based game creation.
 * 
 * <p>This class shows the practical differences between using the multi-parameter
 * constructor versus the Builder pattern for creating ConnectNGame instances.
 * 
 * @author CS5004 Teaching Team
 * @version 1.0
 */
public class BuilderDemo {
    
    /**
     * Runs demonstrations of both approaches.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== Connect-N Game: Constructor vs Builder Pattern ===\n");
        
        demonstrateConstructorApproach();
        System.out.println();
        demonstrateBuilderApproach();
        System.out.println();
        demonstratePresetFactories();
        System.out.println();
        demonstrateFlexibility();
    }
    
    /**
     * Demonstrates the original constructor approach.
     */
    private static void demonstrateConstructorApproach() {
        System.out.println("--- Constructor Approach ---");
        
        // Creating games with constructor
        ConnectNGame ticTacToe = new ConnectNGame(3, 3, 3);
        ConnectNGame connectFour = new ConnectNGame(6, 7, 4);
        ConnectNGame custom = new ConnectNGame(10, 10, 5);
        
        System.out.println("Tic-Tac-Toe: new ConnectNGame(3, 3, 3)");
        System.out.println("  → " + describeGame(ticTacToe));
        
        System.out.println("Connect Four: new ConnectNGame(6, 7, 4)");
        System.out.println("  → " + describeGame(connectFour));
        
        System.out.println("Custom: new ConnectNGame(10, 10, 5)");
        System.out.println("  → " + describeGame(custom));
        
        System.out.println("\nProblems:");
        System.out.println("  • Parameter order unclear (rows vs columns?)");
        System.out.println("  • Must remember exact values for common games");
        System.out.println("  • No default values");
        System.out.println("  • Code doesn't explain what it creates");
    }
    
    /**
     * Demonstrates the Builder pattern approach.
     */
    private static void demonstrateBuilderApproach() {
        System.out.println("--- Builder Pattern Approach ---");
        
        // Creating games with builder
        ConnectNGame ticTacToe = new ConnectNGameBuilder()
            .rows(3)
            .columns(3)
            .winLength(3)
            .build();
        
        ConnectNGame connectFour = new ConnectNGameBuilder()
            .rows(6)
            .columns(7)
            .winLength(4)
            .build();
        
        ConnectNGame custom = new ConnectNGameBuilder()
            .squareBoard(10)
            .winLength(5)
            .build();
        
        System.out.println("Tic-Tac-Toe: new ConnectNGameBuilder()");
        System.out.println("               .rows(3).columns(3).winLength(3).build()");
        System.out.println("  → " + describeGame(ticTacToe));
        
        System.out.println("Connect Four: new ConnectNGameBuilder()");
        System.out.println("               .rows(6).columns(7).winLength(4).build()");
        System.out.println("  → " + describeGame(connectFour));
        
        System.out.println("Custom: new ConnectNGameBuilder()");
        System.out.println("         .squareBoard(10).winLength(5).build()");
        System.out.println("  → " + describeGame(custom));
        
        System.out.println("\nBenefits:");
        System.out.println("  ✓ Self-documenting (parameter names visible)");
        System.out.println("  ✓ Flexible parameter order");
        System.out.println("  ✓ Convenience methods (squareBoard)");
        System.out.println("  ✓ Clear intent");
    }
    
    /**
     * Demonstrates preset factory methods.
     */
    private static void demonstratePresetFactories() {
        System.out.println("--- Preset Factory Methods ---");
        
        ConnectNGame ticTacToe = ConnectNGameBuilder.ticTacToe();
        ConnectNGame connectFour = ConnectNGameBuilder.connectFour();
        ConnectNGame gomoku = ConnectNGameBuilder.gomoku();
        ConnectNGame smallGame = ConnectNGameBuilder.smallGame();
        ConnectNGame largeGame = ConnectNGameBuilder.largeGame();
        
        System.out.println("ConnectNGameBuilder.ticTacToe()");
        System.out.println("  → " + describeGame(ticTacToe));
        
        System.out.println("ConnectNGameBuilder.connectFour()");
        System.out.println("  → " + describeGame(connectFour));
        
        System.out.println("ConnectNGameBuilder.gomoku()");
        System.out.println("  → " + describeGame(gomoku));
        
        System.out.println("ConnectNGameBuilder.smallGame()");
        System.out.println("  → " + describeGame(smallGame));
        
        System.out.println("ConnectNGameBuilder.largeGame()");
        System.out.println("  → " + describeGame(largeGame));
        
        System.out.println("\nBenefits:");
        System.out.println("  ✓ Memorable names");
        System.out.println("  ✓ No need to remember parameters");
        System.out.println("  ✓ One line of code");
        System.out.println("  ✓ Immediately clear what game is created");
    }
    
    /**
     * Demonstrates the flexibility of the Builder pattern.
     */
    private static void demonstrateFlexibility() {
        System.out.println("--- Builder Flexibility ---");
        
        // Default values
        ConnectNGame defaultGame = new ConnectNGameBuilder().build();
        System.out.println("Default game: new ConnectNGameBuilder().build()");
        System.out.println("  → " + describeGame(defaultGame));
        
        // Partial configuration
        ConnectNGame tallBoard = new ConnectNGameBuilder()
            .rows(10)  // Only change rows
            .build();
        System.out.println("\nTall board: .rows(10).build()");
        System.out.println("  → " + describeGame(tallBoard));
        
        // Any parameter order
        ConnectNGame anyOrder = new ConnectNGameBuilder()
            .winLength(4)
            .columns(7)
            .rows(6)
            .build();
        System.out.println("\nAny order: .winLength(4).columns(7).rows(6).build()");
        System.out.println("  → " + describeGame(anyOrder));
        
        System.out.println("\nBenefits:");
        System.out.println("  ✓ Sensible defaults");
        System.out.println("  ✓ Change only what you need");
        System.out.println("  ✓ Parameter order doesn't matter");
    }
    
    /**
     * Creates a human-readable description of a game.
     *
     * @param game the game to describe
     * @return a string describing the game configuration
     */
    private static String describeGame(ConnectNGame game) {
        return String.format("%dx%d board, win length %d",
            game.getRows(), game.getColumns(), game.getWinLength());
    }
}
