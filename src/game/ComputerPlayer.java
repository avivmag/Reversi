package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class which represents computer player which extends Player class
 */
public class ComputerPlayer extends Player {
	private int MaxDepth;
	
	/**
	 * constructor
	 * @param notAllowedHue just like {@link Player}
	 * @param noImageID just like {@link Player}
	 * @param name just like {@link Player}
	 */
	public ComputerPlayer(float notAllowedHue, int noImageID, String name) {
		super(notAllowedHue, noImageID, name);
		switch(Stats.getDifficulty())
		{
		case Penny:
			MaxDepth = 3;
			break;
		case Lenard:
			MaxDepth = 4;
			break;
		case Sheldon:
			MaxDepth = 5;
			break;
		}
	}

	/**
	 * Gets the next movement of the computer player.
	 * @param boardPlayer A replication of the board.
	 * @param rowColumnIndexes An indexes which represents the x and y movement of the computer that will be changed in the algorithm. 
	 * @param realOpponent The opponent of this player.
	 */
	public void GetNextMove(Player[][] boardPlayer, Integer[] rowColumnIndexes, Player realOpponent)
	{
		GetNextMove(boardPlayer, realOpponent, true, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, rowColumnIndexes);
	}

	/**
	 * Gets the next movement of the computer player in recursion.
	 * @param boardPlayer A replication of the board.
	 * @param rowColumnIndexes An indexes which represents the x and y movement of the computer that will be changed in the algorithm. 
	 * @param realOpponent The opponent of this player.	 * @param isMaximizing
	 * @param currentDepth We don't want to past over the maximum Depth which has been chosen.
	 * @param alpha Represents the alpha variable in MinMax algorithm.
	 * @param beta Represents the beta variable in MinMax algorithm.
	 * @param rowColumnIndexes Represents the wanted x and y which will be returned at the end.
	 * @return
	 */
	private int GetNextMove(Player[][] boardPlayer, Player realOpponent, boolean isMaximizing, int currentDepth, int alpha, int beta, Integer[] rowColumnIndexes)
	{
		rowColumnIndexes[0] = 0;
		rowColumnIndexes[1] = 0;

		Player currentPlayer = isMaximizing ? this : realOpponent;
		boolean playerSkipsMove = false;
		List<int[]> possibleMoves = new ArrayList<int[]>();

		boolean isFinalMove = currentDepth >= MaxDepth;

		if (!isFinalMove)
		{             
			possibleMoves = GetPossibleMoves(boardPlayer, currentPlayer, currentPlayer == this ? realOpponent : this);
			if (possibleMoves.size() == 0)
			{
				playerSkipsMove = true;
				possibleMoves = this.GetPossibleMoves(boardPlayer, currentPlayer == this ? realOpponent : this, currentPlayer);
			}

			isFinalMove = (possibleMoves.size() == 0);
		}

		// if it is the final move
		if (isFinalMove)
		{
			rowColumnIndexes[0] = -1;
			rowColumnIndexes[1] = -1;
			return EvaluateBoard(boardPlayer, this, realOpponent);
		}
		else
		{
			int bestBoardValue = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			int bestMoveRowIndex = -1;
			int bestMoveColumnIndex = -1;

			for(int[] nextMove : possibleMoves)
			{
				int rowIndex = nextMove[0];
				int columnIndex = nextMove[1];

				Player[][] nextBoard = new Player[boardPlayer.length][boardPlayer[0].length];
				for (int i = 0; i < boardPlayer.length; i++)
					for (int j = 0; j < boardPlayer[0].length; j++)
						nextBoard[i][j] = boardPlayer[i][j];

				SetPlayer(nextBoard, rowIndex, columnIndex, currentPlayer, currentPlayer == this ? realOpponent : this);

				boolean nextIsMaximizing = playerSkipsMove ? isMaximizing : !isMaximizing;

				int currentBoardValue = GetNextMove(nextBoard, realOpponent, nextIsMaximizing, currentDepth + 1, alpha, beta, new Integer[] {0, 0}); // the Integer is not important..
				// are we trying to get maximum or minimum specific choise?
				if (isMaximizing)
				{
					if (currentBoardValue > bestBoardValue)
					{
						bestBoardValue = currentBoardValue;
						bestMoveRowIndex = rowIndex;
						bestMoveColumnIndex = columnIndex;

						if (bestBoardValue > alpha)
							alpha = bestBoardValue;
						
						if (bestBoardValue >= beta)
							break;
					}
				}
				else
				{
					if (currentBoardValue < bestBoardValue)
					{
						bestBoardValue = currentBoardValue;
						bestMoveRowIndex = rowIndex;
						bestMoveColumnIndex = columnIndex;

						if (bestBoardValue < beta)
							beta = bestBoardValue;

						if (bestBoardValue <= alpha)
							break;
					}
				}
			}

			rowColumnIndexes[0] = bestMoveRowIndex;
			rowColumnIndexes[1] = bestMoveColumnIndex;
			return bestBoardValue;
		}
	}

	/**
	 * Gets player possible movements.
	 * @param boardPlayer Replication of specific board.
	 * @param player This Computer player.
	 * @param opponent Opponent.
	 * @return returns list of possible movements.
	 */
	private List<int[]> GetPossibleMoves(Player[][] boardPlayer, Player player, Player opponent)
	{
		List<int[]> possibleMoves = new ArrayList<int[]>();
		for (int rowIndex = 0; rowIndex < boardPlayer.length; rowIndex++)
			for (int columnIndex = 0; columnIndex < boardPlayer[0].length; columnIndex++)
				if (CanSetPlayer(boardPlayer, rowIndex, columnIndex, player, opponent))
					possibleMoves.add(new int[] { rowIndex, columnIndex });       


		List<Integer> indexes = GetRandomIndexes(possibleMoves.size());
		List<int[]> result = new ArrayList<int[]>();
		for(int index : indexes)
			result.add(possibleMoves.get(index));

		return result;
	}

	/**
	 * shuffles variables between 0 and count.
	 * @param count
	 * @return Shuffled Integer list.
	 */
	private List<Integer> GetRandomIndexes(int count)
	{
		int minValue = 0;
		int maxValue = count;
		List<Integer> result = new ArrayList<Integer>();
		Random random = new Random();

		while (result.size() < count)
		{
			int next = random.nextInt(maxValue - minValue) + minValue;
			if (!result.contains(next))
				result.add(next);

			if (next == minValue)
				minValue++;
			else if (next == maxValue - 1)
				maxValue--;
		}

		return result;
	}

	/**
	 * Evaluate specific replication of board by algorithm.
	 * @param boardPlayer 
	 * @param player
	 * @param opponent
	 * @return
	 */
	private int EvaluateBoard(Player[][] boardPlayer, Player player, Player opponent)
	{
		List<int[]> oppositePlayerPossibleMoves = this.GetPossibleMoves(boardPlayer, opponent, player);
		List<int[]> possibleMoves = this.GetPossibleMoves(boardPlayer, player, opponent);

		if ((possibleMoves.size() == 0) && (oppositePlayerPossibleMoves.size() == 0))
		{
			int result = GetPlayerCount(boardPlayer, player) - GetPlayerCount(boardPlayer, opponent);
			int addend = (int)Math.pow(boardPlayer.length, 4) + (int)Math.pow(boardPlayer.length, 3);
			if (result < 0)
				addend = -addend;

			return result + addend;
		}
		else
		{
			int mobility = GetPossibleConvertions(boardPlayer, player, opponent, possibleMoves) - GetPossibleConvertions(boardPlayer, opponent, player, oppositePlayerPossibleMoves);
			int stability = (GetStablePlayerPositions(boardPlayer, player) - GetStablePlayerPositions(boardPlayer, opponent)) * boardPlayer.length * 2 / 3;

			return mobility + stability;
		}
	}

	/**
	 * gets board corner and edge evaluation.
	 * @param boardPlayer
	 * @param player
	 * @return
	 */
	private int GetStablePlayerPositions(Player[][] boardPlayer, Player player)
	{
		return this.GetStablePlayerFromCorner(boardPlayer, player, 0, 0)
				+ this.GetStablePlayerFromCorner(boardPlayer, player, 0, boardPlayer[0].length - 1)
				+ this.GetStablePlayerFromCorner(boardPlayer, player, boardPlayer.length - 1, 0)
				+ this.GetStablePlayerFromCorner(boardPlayer, player, boardPlayer.length - 1, boardPlayer[0].length - 1)
				+ this.GetStablePlayerFromEdge(boardPlayer, player, 0, true)
				+ this.GetStablePlayerFromEdge(boardPlayer, player, boardPlayer.length - 1, true)
				+ this.GetStablePlayerFromEdge(boardPlayer, player, 0, false)
				+ this.GetStablePlayerFromEdge(boardPlayer, player, boardPlayer[0].length - 1, false);
	}

	/**
	 * gets board corner evaluation.
	 * @param boardPlayer
	 * @param player
	 * @param cornerRowIndex
	 * @param cornerColumnIndex
	 * @return
	 */
	private int GetStablePlayerFromCorner(Player[][] boardPlayer, Player player, int cornerRowIndex, int cornerColumnIndex)
	{
		int result = 0;

		int rowIndexChange = (cornerRowIndex == 0) ? 1 : -1;
		int columnIndexChange = (cornerColumnIndex == 0) ? 1 : -1;

		int rowIndex = cornerRowIndex;
		int rowIndexLimit = (cornerRowIndex == 0) ? boardPlayer.length : 0;
		int columnIndexLimit = (cornerColumnIndex == 0) ? boardPlayer[0].length : 0;
		for (rowIndex = cornerRowIndex; rowIndex != rowIndexLimit; rowIndex += rowIndexChange)
		{
			int columnIndex;
			for (columnIndex = cornerColumnIndex; columnIndex != columnIndexLimit; columnIndex += columnIndexChange)
			{
				if (boardPlayer[rowIndex][columnIndex] == player)
					result++;
				else
					break;
			}

			if ((columnIndexChange > 0 && columnIndex < boardPlayer[0].length) || (columnIndexChange < 0 && columnIndex > 0))
			{
				columnIndexLimit = columnIndex - columnIndexChange;

				if (columnIndexChange > 0 && columnIndexLimit == 0)
					columnIndexLimit++;
				else if (columnIndexChange < 0 && columnIndexLimit == boardPlayer[0].length - 1)
					columnIndexLimit--;

				if ((columnIndexChange > 0 && columnIndexLimit < 0) || (columnIndexChange < 0 && columnIndexLimit > boardPlayer[0].length - 1))
					break;
			}
		}

		return result;
	}

	/**
	 * gets board edge evaluation.
	 * @param boardPlayer
	 * @param player
	 * @param edgeCoordinate
	 * @param isHorizontal
	 * @return
	 */
	private int GetStablePlayerFromEdge(Player[][] boardPlayer, Player player, int edgeCoordinate, boolean isHorizontal)
	{
		int result = 0;

		if (IsEdgeFull(boardPlayer, player, edgeCoordinate, isHorizontal))
		{
			boolean oppositeColorDiscsPassed = false;
			for (int otherCoordinate = 0; otherCoordinate < ( isHorizontal ? boardPlayer[0].length : boardPlayer.length ); otherCoordinate++)
			{                
				Player fieldPlayer = (isHorizontal) ? boardPlayer[edgeCoordinate][otherCoordinate] : boardPlayer[otherCoordinate][edgeCoordinate];
				if (fieldPlayer != player)
				{
					oppositeColorDiscsPassed = true;
				}
				else if (oppositeColorDiscsPassed)
				{
					int consecutiveDiscsCount = 0;
					while ((otherCoordinate < ( isHorizontal ? boardPlayer[0].length : boardPlayer.length )) && (fieldPlayer == player))
					{
						consecutiveDiscsCount++;

						otherCoordinate++;
						if (otherCoordinate < ( isHorizontal ? boardPlayer[0].length : boardPlayer.length ))
						{
							fieldPlayer = (isHorizontal) ? boardPlayer[edgeCoordinate][otherCoordinate] : boardPlayer[otherCoordinate][edgeCoordinate];
						}
					}
					if (otherCoordinate != ( isHorizontal ? boardPlayer[0].length : boardPlayer.length ))
					{
						result += consecutiveDiscsCount;
						oppositeColorDiscsPassed = true;
					}                                             
				}
			}
		}

		return result;
	}

	private boolean IsEdgeFull(Player[][] boardPlayer, Player player, int edgeCoordinate, boolean isHorizontal)
	{
		for (int otherCoordinate = 0; otherCoordinate < (isHorizontal ? boardPlayer[0].length : boardPlayer.length) ; otherCoordinate++)
			if (isHorizontal && (boardPlayer[edgeCoordinate][otherCoordinate] == player) || !isHorizontal && (boardPlayer[otherCoordinate][edgeCoordinate] == null))
				return false;
		return true;
	}

	private boolean CanSetPlayer(Player[][] boardPlayer, int rowIndex, int columnIndex, Player player, Player opponent)
	{
		if (boardPlayer[rowIndex][columnIndex] == null)
			if (player == null)
				return true;
			else
				for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++)
					for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++)
						if ((rowIndexChange != 0) || (columnIndexChange != 0))
							if (CheckDirection(boardPlayer, rowIndex, columnIndex, rowIndexChange, columnIndexChange, player, opponent))
								return true;
		return false;
	}

	/**
	 * Checks specific direction.
	 * @param boardPlayer
	 * @param rowIndex
	 * @param columnIndex
	 * @param rowIndexChange
	 * @param columnIndexChange
	 * @param player
	 * @param opponent
	 * @return
	 */
	private boolean CheckDirection(Player[][] boardPlayer, int rowIndex, int columnIndex, int rowIndexChange, int columnIndexChange, Player player, Player opponent)
	{
		boolean areOppositePlayerFound = false;
		rowIndex += rowIndexChange;
		columnIndex += columnIndexChange;
		while ((rowIndex >= 0) && (rowIndex < boardPlayer.length) && (columnIndex >= 0) && (columnIndex < boardPlayer[0].length))
		{
			if (areOppositePlayerFound)
			{
				if (boardPlayer[rowIndex][columnIndex] == player)
					return true;
				else if (boardPlayer[rowIndex][columnIndex] == null)
					return false;
			}
			else
			{
				if (boardPlayer[rowIndex][columnIndex] == opponent)
					areOppositePlayerFound = true;
				else
					return false;
			}

			rowIndex += rowIndexChange;
			columnIndex += columnIndexChange;
		}

		return false;
	}

	private int GetPlayerCount(Player[][] boardPlayer, Player player)
	{
		int result = 0;
		for (int rowIndex = 0; rowIndex < boardPlayer.length; rowIndex++)
			for (int columnIndex = 0; columnIndex < boardPlayer[0].length; columnIndex++)
				if (boardPlayer[rowIndex][columnIndex] == player)
					result++;
		return result;
	}

	private int GetPossibleConvertions(Player[][] boardPlayer, Player player, Player opponent, List<int[]> possibleMoves)
	{
		int result = 0;
		for(int[] move : possibleMoves)
		{
			Player[][] boardPlayerCopy = new Player[boardPlayer.length][boardPlayer[0].length];
			for (int i = 0; i < boardPlayer.length; i++)
				for (int j = 0; j < boardPlayer[0].length; j++)
					boardPlayerCopy[i][j] = boardPlayer[i][j];

			int rowIndex = move[0];
			int columnIndex = move[1];

			int temp = SetPlayer(boardPlayerCopy, rowIndex, columnIndex, player, opponent);
			result += temp;
		}
		return result;
	}

	/**
	 * put player checker on replication of some board.
	 * @param boardPlayer
	 * @param rowIndex
	 * @param columnIndex
	 * @param player
	 * @param opponent
	 * @return
	 */
	private int SetPlayer(Player[][] boardPlayer, int rowIndex, int columnIndex, Player player, Player opponent)
	{
		int invertedLastMove = 0;
		if (CanSetPlayer(boardPlayer, rowIndex, columnIndex, player, opponent))
		{
			boardPlayer[rowIndex][columnIndex] = player;
			invertedLastMove = InvertOpponent(boardPlayer, rowIndex, columnIndex, player, opponent);
		}
		return invertedLastMove;
	}

	/**
	 * inverts opponent checkers to player checkers on replication of board.
	 * @param boardPlayer
	 * @param rowIndex
	 * @param columnIndex
	 * @param player
	 * @param opponent
	 * @return
	 */
	private int InvertOpponent(Player[][] boardPlayer, int rowIndex, int columnIndex, Player player, Player opponent)
	{
		int invertedCount = 0;
		for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++)
			for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++)
				if ((rowIndexChange != 0) || (columnIndexChange != 0))
					if (CheckDirection(boardPlayer, rowIndex, columnIndex, rowIndexChange, columnIndexChange, player, opponent))
						InvertDirection(boardPlayer, rowIndex, columnIndex, rowIndexChange, columnIndexChange, player, opponent, invertedCount);

		return invertedCount;
	}

	private int InvertDirection(Player[][] boardPlayer, int rowIndex, int columnIndex, int rowIndexChange, int columnIndexChange, Player player, Player opponent, int invertedCount)
	{
		rowIndex += rowIndexChange;
		columnIndex += columnIndexChange;            
		while (boardPlayer[rowIndex][columnIndex] == opponent)
		{
			boardPlayer[rowIndex][columnIndex] = player;
			invertedCount++;

			rowIndex += rowIndexChange;
			columnIndex += columnIndexChange;                
		}
		return invertedCount;
	}
}
