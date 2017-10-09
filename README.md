# Reversi-game

A simple Reversi game built in java with swing graphic api.
This game supports many features, including:
- Player vs. player / player vs. ai / ai vs. ai.
- Three difficulty levels: easy (Penny), normal (Lenny), hard (Sheldon).
- Save/Load game.
- Generic game sizes (default is 8X8).

This game has been done in pairs as part of an assignment in "Principles of Object Oriented Programming" course at Ben-Gurion University in 2015.

### Minimax

This game uses Minimax as a decision rule for the computer ai.
It finds the best move by evaluating the board for every available move.
Although Reversi looks pretty easy to master at first glance, 
this game found to be one of the trickiest.
There are some known strategies you can use to play the game,
checkin all available moves a head for you and for your opponent found to be the most useful.
This type of algorithm found to be massive, both in space and time calculating it,
because of that, a heuristic approach must be used to some extent.
There are three difficulties in the game: easy, normal and hard,
each of them correspond to the maximum depth the algorithm will try to reach until heuristics have to be used (which is less precise):
accordinately, maximal depth of 3, 4 and 5.
You can read much more about this approach in the links attached below.

## Getting Started
### Prerequisites

1. Java SE Runtime Environment 8 (at least), 
can be found: http://www.oracle.com/technetwork/java/javase/downloads/index.html

## Running game

On Terminal/cmd type:
```
java -jar path_of_clone/bin/Reversi.jar
```

## Useful links

* https://en.wikipedia.org/wiki/Reversi
* https://en.wikipedia.org/wiki/Minimax
