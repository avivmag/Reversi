package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import reversi.MainScreen;
import gameTools.Board;

/**
 * A class which holds the game frame and all of its sub components.
 */
public class Game extends JFrame implements MouseListener, MouseMotionListener, ActionListener{

	private Board board;
	private Player player1;
	private Player player2;
	private Player currentPlayerTurn;
	private JPanel fatherPanel;

	private JLabel backLabel;
	private JLabel player1CheckersCount;
	private JLabel player2CheckersCount;
	private JLabel arrow;
	private Timer timer;

	/**
	 * Constructor which build the frame and it components and initialize first turn.
	 */
	public Game()
	{
		this.player1 = Stats.getPlayer1();
		this.player2 = Stats.getPlayer2();

		int frameWidth = 700 - 684 % Stats.getM(); 
		int frameHeight = 700 - 616 % Stats.getN();

		currentPlayerTurn = Stats.getCurrentPlayer();

		addMouseListener(this);
		addMouseMotionListener(this);

		if(Stats.getBoard() == null)
			board = new Board(Stats.getN(), Stats.getM(), frameWidth, frameHeight, player1, player2);
		else
			board = new Board(Stats.getBoard(), frameWidth, frameHeight, player1, player2);

		board.updatePossibleMoves(currentPlayerTurn);
		board.switchTimers(currentPlayerTurn);

		fatherPanel = new JPanel(new BorderLayout());
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.X_AXIS));
		backLabel = new JLabel("Back");
		backLabel.setFont(new Font("Times New Roman", 0, 30));
		backLabel.setForeground(Color.BLACK);
		backLabel.addMouseListener(this);

		JLabel player1Name = new JLabel(player1.getName());
		player1Name.setFont(new Font("Times New Roman", 0, 30));
		player1Name.setForeground(player1.getColor());

		int result = 0;
		if(Stats.getBoard() == null)
			result = 2;
		else
		{
			for (int rowIndex = 0; rowIndex < Stats.getBoard().length; rowIndex++)
				for (int columnIndex = 0; columnIndex < Stats.getBoard()[0].length; columnIndex++)
					if (Stats.getBoard()[rowIndex][columnIndex] != null && Stats.getBoard()[rowIndex][columnIndex].equals(player1))
						result++;
		}

		player1CheckersCount = new JLabel(String.valueOf(result));
		player1CheckersCount.setFont(new Font("Times New Roman", 0, 30));
		player1CheckersCount.setForeground(player1.getColor());

		result = 0;
		if(Stats.getBoard() == null)
			result = 2;
		else
		{
			for (int rowIndex = 0; rowIndex < Stats.getBoard().length; rowIndex++)
				for (int columnIndex = 0; columnIndex < Stats.getBoard()[0].length; columnIndex++)
					if (Stats.getBoard()[rowIndex][columnIndex] != null && Stats.getBoard()[rowIndex][columnIndex].equals(player2))
						result++;
		}
		player2CheckersCount = new JLabel(String.valueOf(result));
		player2CheckersCount.setFont(new Font("Times New Roman", 0, 30));
		player2CheckersCount.setForeground(player2.getColor());

		JLabel player2Name = new JLabel(player2.getName());
		player2Name.setFont(new Font("Times New Roman", 0, 30));
		player2Name.setForeground(player2.getColor());

		arrow = new JLabel();
		if(player1 == currentPlayerTurn)
			try { arrow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("../Images/leftArrow.jpg")))); } catch (IOException e) {}
		else
			try { arrow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("../Images/rightArrow.jpg")))); } catch (IOException e) {}

		bottomPanel.setBackground(Color.WHITE);
		
		JPanel names = new JPanel(new FlowLayout());
		bottomPanel.add(backLabel);
		
		if (Stats.getDifficulty() == Stats.DifficultyType.Penny)
			try{
				JLabel opponentCom = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Images/penny.png"))));
				bottomPanel.add(opponentCom);
			}
		catch(IOException e){
			System.out.println("file not exist");
		}
		if (Stats.getDifficulty() == Stats.DifficultyType.Lenard)
			try{
				JLabel opponentCom = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Images/lenny.png"))));
				bottomPanel.add(opponentCom);
			}
		catch(IOException e){
			System.out.println("file not exist");
		}
		if (Stats.getDifficulty() == Stats.DifficultyType.Sheldon)
			try{
				JLabel opponentCom = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Images/sheldon.png"))));
				bottomPanel.add(opponentCom);
			}
		catch(IOException e){
			System.out.println("file not exist");
		}
		
		bottomPanel.add(names);
		names.setBackground(Color.WHITE);
		names.add(Box.createRigidArea(new Dimension(20,9)));
		names.add(player1Name);
		names.add(Box.createRigidArea(new Dimension(20,9)));
		names.add(player1CheckersCount);
		names.add(Box.createRigidArea(new Dimension(20,9)));
		names.add(arrow);
		names.add(Box.createRigidArea(new Dimension(20,9)));
		names.add(player2CheckersCount);
		names.add(Box.createRigidArea(new Dimension(20,9)));
		names.add(player2Name);

		fatherPanel.add(board, BorderLayout.CENTER);
		fatherPanel.add(bottomPanel, BorderLayout.SOUTH);

		bottomPanel.validate();
		bottomPanel.setPreferredSize(bottomPanel.getPreferredSize());

		timer = new Timer(1200, this);
		timer.start();

		add(fatherPanel);

		setMinimumSize(new Dimension(frameWidth, frameHeight));
		setPreferredSize(new Dimension((int) getBoard().getWidth(), (int) getBoard().getHeight()));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Switch current player to the other player and initialize turn. 
	 */
	private void switchPlayer()
	{
		initializeTurn();
		if(currentPlayerTurn.equals(player1))
			currentPlayerTurn = player2;
		else
			currentPlayerTurn = player1;

		if(player1 == currentPlayerTurn)
			try { arrow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("../Images/leftArrow.jpg")))); } catch (IOException e) {}
		else
			try { arrow.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("../Images/rightArrow.jpg")))); } catch (IOException e) {}

		board.updatePossibleMoves(currentPlayerTurn);

		if(board.getPossibleMoves().size() == 0)
		{
			if(currentPlayerTurn.equals(player1))
				currentPlayerTurn = player2;
			else
				currentPlayerTurn = player1;

			board.updatePossibleMoves(currentPlayerTurn);

			if(board.getPossibleMoves().size() == 0)
				gameOver();
			else
				JOptionPane.showMessageDialog(null, "There are no legal moves for " + (currentPlayerTurn == player1 ? player2.getName() : player1.getName()) + ", it is " + currentPlayerTurn.getName() + " turn.");
		}

		board.switchTimers(currentPlayerTurn);
	}

	/**
	 * save the game stats to the static stats class.
	 */
	private void saveGameToStats()
	{
		Stats.setCurrentPlayer(getCurrentPlayer());
		Stats.setBoard(board.getBoardByPlayers());
	}

	/**
	 * initialize a turn.
	 */
	private void initializeTurn()
	{
		player1CheckersCount.setText(String.valueOf(board.GetCheckerCount(player1)));
		player2CheckersCount.setText(String.valueOf(board.GetCheckerCount(player2)));
	}

	private boolean gameIsOver = false;
	
	/**
	 * called if no moves are left, notifies the game is over and who is the winner.
	 */
	private void gameOver()
	{
		gameIsOver = true;
		int player1Number = board.GetCheckerCount(player1);
		int player2Number = board.GetCheckerCount(player2);

		if(player1Number > player2Number)
			JOptionPane.showMessageDialog(null, player1.getName() + " has won the Game!");
		else if(player1Number < player2Number)
			JOptionPane.showMessageDialog(null, player2.getName() + " has won the Game!");
		else
			JOptionPane.showMessageDialog(null, "Well, it is a tie!");
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == backLabel)
			backLabel.setForeground(Color.RED);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == backLabel)
			backLabel.setForeground(Color.BLACK);

		board.removeAim(currentPlayerTurn);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!(currentPlayerTurn instanceof ComputerPlayer) && e.getY() - 30 <= fatherPanel.getComponent(0).getHeight() && !gameIsOver)
			board.MousePressedOnBoard(currentPlayerTurn, e.getX() - 8, e.getY() - 30);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource() == backLabel)
		{
			saveGameToStats();
			timer.stop();
			board.getMapPlayerToTimer().get(player1).stop();
			board.getMapPlayerToTimer().get(player2).stop();
			Game.this.dispose();
			new MainScreen();
		}
		else if(!(currentPlayerTurn instanceof ComputerPlayer) && !gameIsOver)
		{
			board.releaseChecker();
			if(e.getY() - 30 <= fatherPanel.getComponent(0).getHeight())
				if(board.MouseReleasedOnBoard(currentPlayerTurn, e.getX() - 8, e.getY() - 30))
					switchPlayer();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!(currentPlayerTurn instanceof ComputerPlayer) && e.getY() - 30 <= fatherPanel.getComponent(0).getHeight() && !gameIsOver)
		{
			board.MouseDraggedOnBoard(currentPlayerTurn, e.getX() - 8, e.getY() - 30);
			board.MouseMovedOnBoard(currentPlayerTurn, e.getX() - 8, e.getY() - 30);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!(currentPlayerTurn instanceof ComputerPlayer) && e.getY() - 30 <= fatherPanel.getComponent(0).getHeight() && !gameIsOver)
			board.MouseMovedOnBoard(currentPlayerTurn, e.getX() - 8, e.getY() - 30);
	}

	boolean runningMove = false;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!runningMove && currentPlayerTurn instanceof ComputerPlayer && !gameIsOver)
		{
			runningMove = true;
			Integer[] rowColumnIndexes = new Integer[] {-1, -1};
			((ComputerPlayer) currentPlayerTurn).GetNextMove(board.getBoardByPlayers(), rowColumnIndexes, (currentPlayerTurn == player1 ? player2 : player1));
			if(board.computerPlayMove(currentPlayerTurn, rowColumnIndexes[1], rowColumnIndexes[0]))
				switchPlayer();
			runningMove = false;
		}
		if(gameIsOver)
			timer.stop();
	}

	public Board getBoard() {
		return board;
	}

	public Player getCurrentPlayer() {
		return currentPlayerTurn;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayerTurn = currentPlayer;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
}
