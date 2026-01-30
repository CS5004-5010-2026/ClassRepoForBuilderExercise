"""Player enumeration for Connect-N game."""

from enum import Enum


class Player(Enum):
    """
    Represents a player in the Connect-N game.
    
    Attributes:
        PLAYER_ONE: First player (token 'X')
        PLAYER_TWO: Second player (token 'O')
    """
    
    PLAYER_ONE = ('X', 'Player 1')
    PLAYER_TWO = ('O', 'Player 2')
    
    def __init__(self, token: str, display_name: str):
        """
        Initialize a Player.
        
        Args:
            token: The character token for this player ('X' or 'O')
            display_name: Human-readable name for this player
        """
        self._token = token
        self._display_name = display_name
    
    @property
    def token(self) -> str:
        """
        Get the token character for this player.
        
        Returns:
            The player's token ('X' or 'O')
        """
        return self._token
    
    @property
    def display_name(self) -> str:
        """
        Get the display name for this player.
        
        Returns:
            Human-readable player name
        """
        return self._display_name
    
    def get_opponent(self) -> 'Player':
        """
        Get the opponent of this player.
        
        Returns:
            The other player
        """
        return Player.PLAYER_TWO if self == Player.PLAYER_ONE else Player.PLAYER_ONE
    
    def __str__(self) -> str:
        """String representation of the player."""
        return self._display_name
