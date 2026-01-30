"""
Connect-N Game Package

A fully-tested Python implementation of a configurable Connect-N game model
demonstrating the Builder pattern.
"""

from .connect_n_game import ConnectNGame
from .connect_n_game_builder import ConnectNGameBuilder
from .player import Player
from .game_status import GameStatus

__all__ = ['ConnectNGame', 'ConnectNGameBuilder', 'Player', 'GameStatus']
__version__ = '1.0.0'
