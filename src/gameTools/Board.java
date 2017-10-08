package gameTools;

import game.Player;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

/**
 * Class which represents the board of the game, it basically includes everything involving the board with exception to Mouse events which Game class implements. 
 */
public class Board extends JLayeredPane {
	private JPanel gridBoard;
	private float cellWidth;
	private float cellHeight;
	private Checker[][] boardCheckers;

	private Player player1;
	private Player player2;
	private HashMap<Player, Aim> mapPlayerToAim;
	private HashMap<Player, Timer> mapPlayerToTimer;
	private HashMap<Player, List<Checker>> mapPlayerToCheckers;
	private List<Checker> markedCheckers;

	/**
	 * Constructor.
	 * @param n The amount of rows.
	 * @param m The amount of columns.
	 * @param gridWidth
	 * @param gridHeight
	 * @param player1
	 * @param player2
	 */
	public void setBoardStuff(int n, int m, float gridWidth, float gridHeight, Player player1, Player player2)
	{
		setOpaque(true);
		gridBoard = new JPanel(new GridLayout(n,m));
		setBounds(0, 0, (int) gridWidth, (int) gridHeight);
		
		this.player1 = player1;
		this.player2 = player2;

		mapPlayerToAim = new HashMap<Player, Aim>();
		mapPlayerToTimer = new HashMap<Player, Timer>();
		markedCheckers = new ArrayList<Checker>();

		mapPlayerToAim.put(player1, new Aim(player1.getColor(), cellWidth / 2, cellHeight / 2));
		mapPlayerToAim.put(player2, new Aim(player2.getColor(), cellWidth / 2, cellHeight / 2));

		mapPlayerToTimer.put(player1, new Timer(80, new ActionListener() {
			float colorHue = 0;
			public void actionPerformed(ActionEvent ae) {
				colorHue += 0.05f;
				for (Checker checkers : markedCheckers) {
					checkers.initializeImage(Color.getHSBColor(colorHue, 1f, 1f), player2.getImageID());
					checkers.repaint();
				}
			}}));

		mapPlayerToTimer.put(player2, new Timer(80, new ActionListener() {
			float colorHue = 0;
			public void actionPerformed(ActionEvent ae) {
				colorHue += 0.05f;
				for (Checker checkers : markedCheckers) {
					checkers.initializeImage(Color.getHSBColor(colorHue, 1f, 1f), player1.getImageID());
					checkers.repaint();
				}
			}}));
		
		JPanel temp = new JPanel();
		for(int i = 0; i < n; i++)
			for (int j = 0; j < m; j++) {
				temp = new JPanel();
				temp.setBackground(((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) ? Color.WHITE : Color.BLACK);
				gridBoard.add(temp);
			}

		add(gridBoard, new Integer(0));
		add(mapPlayerToAim.get(player1), new Integer(1));
		add(mapPlayerToAim.get(player2), new Integer(1));
		
		for(List<Checker> checkerList : mapPlayerToCheckers.values())
			for(Checker checker : checkerList)
				add(checker, new Integer(2));

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				cellWidth = getWidth() / (float) m;
				cellHeight = getHeight() / (float) n;
				
				gridBoard.setBounds(getBounds());
				
				for(List<Checker> checkerList : mapPlayerToCheckers.values())
					for(Checker checker : checkerList)
						checker.resizeImage(cellWidth, cellHeight);
				mapPlayerToAim.get(player1).resizeImage(cellWidth / 2, cellHeight / 2);
				mapPlayerToAim.get(player2).resizeImage(cellWidth / 2, cellHeight / 2);
			}
		});
	}
	
	/**
	 * Constructor
	 * @param n The amount of rows.
	 * @param m The amount of columns.
	 * @param gridWidth
	 * @param gridHeight
	 * @param player1
	 * @param player2
	 */
	public Board(int n, int m, float gridWidth, float gridHeight, Player player1, Player player2)
	{
		cellWidth = gridWidth / m;
		cellHeight = gridHeight / n;
		boardCheckers = new Checker[n][m];

		mapPlayerToCheckers = new HashMap<Player, List<Checker>>();
		mapPlayerToCheckers.put(player1, new ArrayList<Checker>());
		mapPlayerToCheckers.put(player2, new ArrayList<Checker>());
		
		mapPlayerToCheckers.get(player1).add(boardCheckers[n/2 + n%2 - 1][m/2 + m%2 - 1] = new Checker(player1, this, m/2 + m%2 - 1, n/2 + n%2 - 1, cellWidth, cellHeight));
		mapPlayerToCheckers.get(player1).add(boardCheckers[n/2 + n%2][m/2 + m%2] = new Checker(player1, this, m/2 + m%2, n/2 + n%2, cellWidth, cellHeight));
		mapPlayerToCheckers.get(player2).add(boardCheckers[n/2 + n%2 - 1][m/2 + m%2] = new Checker(player2, this, m/2 + m%2, n/2 + n%2 - 1, cellWidth, cellHeight));
		mapPlayerToCheckers.get(player2).add(boardCheckers[n/2 + n%2][m/2 + m%2 - 1] = new Checker(player2, this, m/2 + m%2 - 1, n/2 + n%2, cellWidth, cellHeight));
		
		setBoardStuff(n, m, gridWidth, gridHeight, player1, player2);
	}

	/**
	 * Constructor 
	 * @param boardPlayer Board came from the load.
	 * @param gridWidth
	 * @param gridHeight
	 * @param player1
	 * @param player2
	 */
	public Board(Player[][] boardPlayer, float gridWidth, float gridHeight, Player player1, Player player2)
	{
		cellWidth = gridWidth / boardPlayer[0].length;
		cellHeight = gridHeight / boardPlayer.length;
		boardCheckers = new Checker[boardPlayer.length][boardPlayer[0].length];

		mapPlayerToCheckers = new HashMap<Player, List<Checker>>();
		mapPlayerToCheckers.put(player1, new ArrayList<Checker>());
		mapPlayerToCheckers.put(player2, new ArrayList<Checker>());
		
		for (int i = 0; i < boardPlayer.length; i++) {
			for (int j = 0; j < boardPlayer[0].length; j++) {
				if(boardPlayer[i][j] != null)
				{
					boardCheckers[i][j] = new Checker(boardPlayer[i][j], this, j, i, cellWidth, cellHeight);
					mapPlayerToCheckers.get(boardPlayer[i][j]).add(boardCheckers[i][j]);
				}
			}
		}
		
		setBoardStuff(boardPlayer.length, boardPlayer[0].length, gridWidth, gridHeight, player1, player2);
	}
	
	/**
	 * Handles player turn movement.
	 * @param currentPlayer
	 * @return returns true if the move has been made.
	 */
	public boolean handlePlayerTurnMovement(Player currentPlayer)
	{
		// checks if the point have any good effect on any side it goes to
		if((0 <= lastMousePositionMetricX && lastMousePositionMetricX < boardCheckers[0].length && 0 <= lastMousePositionMetricY && lastMousePositionMetricY < boardCheckers.length) && possibleMoves.containsKey(lastMousePositionMetricY * 100 + lastMousePositionMetricX))
		{
			if(mapPlayerToTimer.get(currentPlayer).isRunning())
				mapPlayerToTimer.get(currentPlayer).stop();
			
			for (Checker checker : markedCheckers) {
				mapPlayerToCheckers.get(currentPlayer).add(checker);
				mapPlayerToCheckers.get(currentPlayer.equals(player1) ? player2 : player1).remove(checker);
				checker.setPlayer(currentPlayer);
				checker.initializeImage(currentPlayer.getColor(), currentPlayer.getImageID());
			}
			
			markedCheckers = new ArrayList<Checker>();
			
			// if change has been made, we need to change the destination place
			mapPlayerToCheckers.get(currentPlayer).add(boardCheckers[lastMousePositionMetricY][lastMousePositionMetricX] = new Checker(currentPlayer, this, lastMousePositionMetricX, lastMousePositionMetricY, cellWidth, cellHeight));
			add(boardCheckers[lastMousePositionMetricY][lastMousePositionMetricX], new Integer(2));
			return true;
		}
		return false;
	}
	
	/**
	 * Handles Computer player turn movement.
	 * @param currentPlayer
	 * @param metricXPos
	 * @param metricYPos
	 * @return
	 */
	public boolean computerHandlePlayerTurnMovement(Player currentPlayer, int metricXPos, int metricYPos)
	{
		// checks if the point have any good effect on any side it goes to
		if((0 <= metricXPos && metricXPos < boardCheckers[0].length && 0 <= metricYPos && metricYPos < boardCheckers.length) && possibleMoves.containsKey(metricYPos * 100 + metricXPos))
		{
			if(mapPlayerToTimer.get(currentPlayer).isRunning())
				mapPlayerToTimer.get(currentPlayer).stop();
			
			for (Checker checker : possibleMoves.get(metricYPos * 100 + metricXPos)) {
				mapPlayerToCheckers.get(currentPlayer).add(checker);
				mapPlayerToCheckers.get(currentPlayer.equals(player1) ? player2 : player1).remove(checker);
				checker.setPlayer(currentPlayer);
				checker.initializeImage(currentPlayer.getColor(), currentPlayer.getImageID());
			}
			
			markedCheckers = new ArrayList<Checker>();
			
			// if change has been made, we need to change the destination place
			mapPlayerToCheckers.get(currentPlayer).add(boardCheckers[metricYPos][metricXPos] = new Checker(currentPlayer, this, metricXPos, metricYPos, cellWidth, cellHeight));
			add(boardCheckers[metricYPos][metricXPos], new Integer(2));
			return true;
		}
		return false;
	}
	
	private HashMap<Integer, List<Checker>> possibleMoves;
	
	public void updatePossibleMoves(Player player)
	{
		possibleMoves = getEatableCheckers(player);
	}
	
	private HashMap<Integer, List<Checker>> getEatableCheckers(Player player)
	{
		HashMap<Integer, List<Checker>> mapPlayerCheckerToEatableCheckers = new HashMap<Integer,List<Checker>>();
		
		for (int i = 0; i < boardCheckers.length; i++) {
			for (int j = 0; j < boardCheckers[0].length; j++) {
				if(boardCheckers[i][j] == null)
				{
					List<Checker> checkers = getEatableCheckersPoint(player, j, i);
					if(!checkers.isEmpty())
						mapPlayerCheckerToEatableCheckers.put(i*100 + j, checkers);
				}
			}
		}
		
		return mapPlayerCheckerToEatableCheckers;
	}

	private List<Checker> getEatableCheckersPoint(Player player, int metricXPos, int metricYPos)
	{
		List<Checker> checkers = new ArrayList<Checker>();
		for (int indexXDirection = -1; indexXDirection < 2; indexXDirection++)
			for (int indexYDirection = -1; indexYDirection < 2; indexYDirection++)
				if( indexXDirection != 0 || indexYDirection != 0 )
					checkers.addAll(getEatableCheckersPointDirection(player, metricXPos, metricYPos, indexXDirection, indexYDirection));
		return checkers;
	}

	private List<Checker> getEatableCheckersPointDirection(Player player, int metricXPos, int metricYPos, int indexXDirection, int indexYDirection)
	{
		List<Checker> checkers = new ArrayList<Checker>();
		for (int i = 1; 0 <= metricYPos + i*indexYDirection && metricYPos + i*indexYDirection < boardCheckers.length && 0 <= metricXPos + i*indexXDirection && metricXPos + i*indexXDirection < boardCheckers[0].length && boardCheckers[metricYPos + i*indexYDirection][metricXPos + i*indexXDirection] != null; i++)
			if(boardCheckers[metricYPos + i*indexYDirection][metricXPos + i*indexXDirection].getPlayer().equals(player))
			{
				if(i > 1)
					for (int j = 1; j < i ; j++)
						checkers.add(boardCheckers[metricYPos + j*indexYDirection][metricXPos + j*indexXDirection]);
				break;
			}
		return checkers;
	}

	private int lastMousePositionMetricX;
	private int lastMousePositionMetricY;

	public void setAim(Player currentPlayer)
	{
		mapPlayerToAim.get(currentPlayer).showAim(lastMousePositionMetricX, lastMousePositionMetricY);
	}

	public void removeAim(Player currentPlayer)
	{
		mapPlayerToAim.get(currentPlayer).hideAim();
	}

	public void markEatableCheckers(Player opponent, int metricXPos, int metricYPos)
	{
		for (Checker checker : mapPlayerToCheckers.get(opponent))
			if(possibleMoves.get(metricYPos * 100 + metricXPos).contains(checker))
				markedCheckers.add(checker);
			else
				checker.initializeImage(opponent.getColor(), opponent.getImageID());
	}
    
    
    private Checker checkerWithMouse;
    
    /**
     * called if user clicked on board.
     * @param currentPlayer
     * @param x
     * @param y
     */
	public void MousePressedOnBoard(Player currentPlayer, int x, int y)
	{
		int metricXPos = (int) ((float) x / (float) cellWidth);
		int metricYPos = (int) ((float) y / (float) cellHeight);
		if(boardCheckers[metricYPos][metricXPos] != null && currentPlayer.equals(boardCheckers[metricYPos][metricXPos].getPlayer()))
		{
			checkerWithMouse = boardCheckers[metricYPos][metricXPos];
			checkerWithMouse.mousePressed((int) (x % cellWidth), (int) (y % cellHeight));
		}
	}

	/**
     * called if user dragged on board.
	 * @param player
	 * @param x
	 * @param y
	 */
	public void MouseDraggedOnBoard(Player player, int x, int y)
	{
		int metricXPos = (int) ((float) x / (float) cellWidth);
		int metricYPos = (int) ((float) y / (float) cellHeight);
		
		if(checkerWithMouse != null && (0 <= metricXPos && metricXPos < boardCheckers[0].length && 0 <= metricYPos && metricYPos < boardCheckers.length))
			checkerWithMouse.mouseDragged(x, y);
	}
	
	/**
     * called if user moved on board.
	 * @param player
	 * @param x
	 * @param y
	 */
	public void MouseMovedOnBoard(Player player, int x, int y)
	{
		int metricXPos = (int) ((float) x / (float) cellWidth);
		int metricYPos = (int) ((float) y / (float) cellHeight);
		
		if((metricXPos != lastMousePositionMetricX || metricYPos != lastMousePositionMetricY) && 0 <= metricXPos && metricXPos < boardCheckers[0].length && 0 <= metricYPos && metricYPos < boardCheckers.length &&boardCheckers[metricYPos][metricXPos] == null)
		{
			lastMousePositionMetricX = metricXPos;
			lastMousePositionMetricY = metricYPos;
			if(possibleMoves.containsKey(metricYPos * 100 + metricXPos))
			{
				markedCheckers = new ArrayList<Checker>();
				markEatableCheckers(player == player1 ? player2 : player1, metricXPos, metricYPos);
				setAim(player);
			}
			else
			{
				// hide..
				if(mapPlayerToTimer.get(player == player1 ? player2 : player1).isRunning())
					mapPlayerToTimer.get(player == player1 ? player2 : player1).stop();
				
				for (Checker checker : markedCheckers) {
					checker.initializeImage((player == player1 ? player2 : player1).getColor(), (player == player1 ? player2 : player1).getImageID());
				}
				markedCheckers = new ArrayList<Checker>();
				removeAim(player);
			}
		}
		else if(metricXPos != lastMousePositionMetricX || metricYPos != lastMousePositionMetricY)
		{
			lastMousePositionMetricX = -1;
			lastMousePositionMetricY = -1;
			
			if(mapPlayerToTimer.get(player == player1 ? player2 : player1).isRunning())
				mapPlayerToTimer.get(player == player1 ? player2 : player1).stop();
			
			for (Checker checker : markedCheckers) {
				checker.initializeImage((player == player1 ? player2 : player1).getColor(), (player == player1 ? player2 : player1).getImageID());
			}
			markedCheckers = new ArrayList<Checker>();
			removeAim(player);
		}
	}
	
	/**
     * called if user released on board.
	 * @param player
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean MouseReleasedOnBoard(Player player, int x, int y)
	{
		int metricXPos = (int) ((float) x / (float) cellWidth);
		int metricYPos = (int) ((float) y / (float) cellHeight);
		
		return playMove(player, metricXPos, metricYPos);
	}
	
	/**
	 * Plays a move of player.
	 * @param player
	 * @param metricXPos
	 * @param metricYPos
	 * @return
	 */
	public boolean playMove(Player player, int metricXPos, int metricYPos)
	{
		boolean changed = false;
		if(metricXPos == lastMousePositionMetricX && metricYPos == lastMousePositionMetricY)
		{
			changed = handlePlayerTurnMovement(player); // must be before setBound
			removeAim(player);// must be before handlePlayer..
		}
		return changed;
	}
	
	/**
	 * Playes computer movement
	 * @param player
	 * @param metricXPos
	 * @param metricYPos
	 * @return
	 */
	public boolean computerPlayMove(Player player, int metricXPos, int metricYPos)
	{
		return computerHandlePlayerTurnMovement(player, metricXPos, metricYPos); // must be before setBound
	}
	
	public void releaseChecker()
	{
		if(checkerWithMouse != null)
		{
			checkerWithMouse.mouseReleased();
			checkerWithMouse = null;
		}
	}
	
	/**
	 * switch player color timers.
	 * @param currentPlayer
	 */
	public void switchTimers(Player currentPlayer)
	{
		if(!mapPlayerToTimer.get(currentPlayer).isRunning())
			mapPlayerToTimer.get(currentPlayer).start();
		
		if(mapPlayerToTimer.get(currentPlayer.equals(player1) ? player2 : player1).isRunning())
			mapPlayerToTimer.get(currentPlayer).stop();
	}
	
    public int GetCheckerCount(Player player)
    {
        int result = 0;
        for (int rowIndex = 0; rowIndex < boardCheckers.length; rowIndex++)
            for (int columnIndex = 0; columnIndex < boardCheckers[0].length; columnIndex++)
                if (boardCheckers[rowIndex][columnIndex] != null && boardCheckers[rowIndex][columnIndex].getPlayer().equals(player))
                    result++;
        return result;
    }
    
    public Player[][] getBoardByPlayers()
    {
    	Player[][] playerBoard = new Player[boardCheckers.length][boardCheckers[0].length];
    	for (int i = 0; i < playerBoard.length; i++)
			for (int j = 0; j < playerBoard[0].length; j++)
				if(boardCheckers[i][j] != null)
					playerBoard[i][j] = boardCheckers[i][j].getPlayer();
    	
    	return playerBoard;
    }
	
	public Checker[][] getBoardCheckers() {
		return boardCheckers;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public HashMap<Player, List<Checker>> getMapPlayerToCheckers() {
		return mapPlayerToCheckers;
	}

	public Checker getCheckerWithMouse() {
		return checkerWithMouse;
	}

	public HashMap<Integer, List<Checker>> getPossibleMoves() {
		return possibleMoves;
	}

	public HashMap<Player, Timer> getMapPlayerToTimer() {
		return mapPlayerToTimer;
	}
	
}