"""Game status enumeration for Connect-N game."""

from enum import Enum, auto


class GameStatus(Enum):
    """
    Represents the current status of a Connect-N game.
    
    Attributes:
        IN_PROGRESS: Game is still being played
        PLAYER_ONE_WON: Player 1 has won the game
        PLAYER_TWO_WON: Player 2 has won the game
        DRAW: Game ended in a draw (board full, no winner)
    """
    
    IN_PROGRESS = auto()
    PLAYER_ONE_WON = auto()
    PLAYER_TWO_WON = auto()
    DRAW = auto()
    
    def __str__(self) -> str:
        """String representation of the game status."""
        return self.name.replace('_', ' ').title()
