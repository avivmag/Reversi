package reversi;

import game.ComputerPlayer;
import game.Player;
import game.Stats;
import game.Stats.DifficultyType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.PlainDocument;

public class SettingsScreen extends JLayeredPane implements MouseListener{

	private JFrame frame;
	private  JSpinner nDim, mDim;
	private JButton okBtn;
	private JLabel back;
	private JTextField player1;
	private JTextField player2;
	private JPanel playersNamePanel;
	private JLabel notEq;
	private JLabel notCom;
	private JComboBox<Integer> numOfPlayersCombo;
	private JComboBox<String> difficultyCombo;

	public SettingsScreen() {
		
		class JTextFieldLimit extends PlainDocument {
		  private int limit;
		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  JTextFieldLimit(int limit, boolean upper) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}

		frame = new JFrame("Settings");
		int frameHeight = 800;
		int frameWidth = 800;

		//Panels
		JPanel headline = new JPanel(); //Headline panel
		JPanel bgPanel = new JPanel(); //background panel
		JPanel boardSizePanel = new JPanel(); //Board size panel
		JPanel btnPanel = new JPanel(); //Button panel
		JPanel playersPanel = new JPanel(); //Numbers  of players panel
		playersNamePanel = new JPanel(); //Players name panel
		JPanel difficultyPanel = new JPanel();//Difficulty Panel

		//Layouts
		headline.setLayout(new FlowLayout());
		bgPanel.setLayout(new GridBagLayout());
		playersPanel.setLayout(new FlowLayout());
		playersNamePanel.setLayout(new FlowLayout());
		boardSizePanel.setLayout(new FlowLayout());
		btnPanel.setLayout(new GridLayout());
		difficultyPanel.setLayout(new FlowLayout());

		try{
			JLabel backgroundImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Backgrounds/settings_background.jpg"))));
			bgPanel.add(backgroundImage);
		}
		catch(IOException e){
			System.out.println("file not exist");
		}
		bgPanel.setBounds(0, 0, 1000, 1000);
		add(bgPanel, new Integer(0),0); //Background image

		//Headline panel
		back = new JLabel("Back");
		back.setFont(new Font("Times New Roman",0,30));
		back.setForeground(Color.lightGray);
		JLabel settings = new JLabel("                 Settings");
		settings.setForeground(Color.cyan);
		settings.setFont(new Font("Times New Roman",0,50));
		headline.add(back);
		headline.add(settings);
		headline.setBounds(-100, 0, 700, 100);
		headline.setOpaque(false);
		add(headline, new Integer(1),1);

		//Number of players panel
		JLabel numbersOfPlayers = new JLabel("Numbers of human Players: ");
		numbersOfPlayers.setFont(new Font("Times New Roman",0,40));
		numbersOfPlayers.setForeground(Color.orange);
		playersPanel.add(numbersOfPlayers);
		numOfPlayersCombo = new JComboBox<Integer>();
		numOfPlayersCombo.addItem(new Integer(1));
		numOfPlayersCombo.addItem(new Integer(2));
		numOfPlayersCombo.addItem(new Integer(0));
		numOfPlayersCombo.setPreferredSize(new Dimension(50, 35));
		numOfPlayersCombo.setFont(new Font("", 0, 30));
		playersPanel.add(numOfPlayersCombo);
		playersPanel.setBounds(-9, 200, 550, 500);
		playersPanel.setOpaque(false);
		add(playersPanel, new Integer(1),2);

		//Players name panel
		JLabel names = new JLabel("Players Name:");
		names.setFont(new Font("Times New Roman",0,50));
		names.setForeground(Color.orange);
		player1 = new JTextField(15);
		player1.setDocument(new JTextFieldLimit(10));
		player1.setText(Stats.getPlayer1().getName());
		player1.setPreferredSize(new Dimension(10, 35));
		player1.setFont(new Font("", Font.BOLD, 15));
		player2 = new JTextField(15);
		player2.setDocument(new JTextFieldLimit(10));  
		player2.setText(Stats.getPlayer2().getName());
		player2.setPreferredSize(new Dimension(10, 35));
		player2.setFont(new Font("", Font.BOLD, 15));
		notEq = new JLabel("•The names must be different");
		notEq.setFont(new Font("Times New Roman",0,30));
		notEq.setForeground(Color.RED);
		notCom = new JLabel("•The names can't be Computer");
		notCom.setFont(new Font("Times New Roman",0,30));
		notCom.setForeground(Color.RED);
		playersNamePanel.add(names);
		playersNamePanel.add(player1);
		playersNamePanel.add(player2);
		playersNamePanel.setBounds(-2, 300, 700, 700);
		playersNamePanel.setOpaque(false);
		add(playersNamePanel, new Integer(1),3);

		//Board size panel
		JLabel boardSize = new JLabel("Size of Board: ");
		boardSize.setForeground(Color.orange);
		boardSize.setFont(new Font("Times New Roman",0,50));
		nDim = new JSpinner(new SpinnerNumberModel(8,4,20,1)); //Spinner to the number of squares in X axis
		mDim = new JSpinner(new SpinnerNumberModel(8,4,20,1)); //Spinner to the number of squares in Y axis
		nDim.setPreferredSize(new Dimension(60, 40));
		mDim.setPreferredSize(new Dimension(60, 40));
		nDim.setFont(new Font("Times New Roman", Font.BOLD, 20));
		mDim.setFont(new Font("Times New Roman", Font.BOLD, 20)); 
		JFormattedTextField notEditN = ((JSpinner.DefaultEditor) nDim.getEditor()).getTextField();
		notEditN.setEditable(false);
		JFormattedTextField notEditM = ((JSpinner.DefaultEditor) mDim.getEditor()).getTextField();
		notEditM.setEditable(false);
		boardSizePanel.add(boardSize);
		boardSizePanel.add(nDim);
		boardSizePanel.add(mDim);
		boardSizePanel.setBounds(-20, 400, 500, 500);
		boardSizePanel.setOpaque(false);
		add(boardSizePanel, new Integer(1),4);

		//DifficultyPanel
		JLabel diff = new JLabel("Difficulty: ");
		diff.setForeground(Color.orange);
		diff.setFont(new Font("Times New Roman",0,50));
		difficultyCombo = new JComboBox<String>();
		difficultyCombo.addItem("Penny");
		difficultyCombo.addItem("Lenard");
		difficultyCombo.addItem("Sheldon");
		difficultyCombo.setPreferredSize(new Dimension(100, 40));
		difficultyCombo.setFont(new Font("", 0, 20));
		difficultyPanel.add(diff);
		difficultyPanel.add(difficultyCombo);
		difficultyPanel.setBounds(-75, 500, 500, 100);
		difficultyPanel.setOpaque(false);
		add(difficultyPanel, new Integer(1),5);

		//Button Panel
		okBtn = new JButton("OK",null);
		btnPanel.setBounds(frameWidth/2-50, frameHeight-200, 100, 100);
		btnPanel.add(okBtn);
		add(btnPanel,new Integer(1),6);

		//Listeners
		back.addMouseListener(this);
		okBtn.addMouseListener(this);

		frame.pack();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setResizable(false);
		frame.setLocation(400, 150);
		frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == back)
			back.setForeground(Color.ORANGE);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == back)
			back.setForeground(Color.LIGHT_GRAY);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == back)
		{
			frame.dispose();
			new MainScreen();
		}
		if (e.getSource() == okBtn)
		{	
			boolean isCom = false;
			if (Stats.getN() != (Integer)nDim.getValue() || Stats.getM() != (Integer)mDim.getValue())
				Stats.setBoard(null);
			Stats.setN((Integer)nDim.getValue()); 
			Stats.setM((Integer)mDim.getValue());
			if (!player1.getText().equals(player2.getText()))
			{
				switch(difficultyCombo.getSelectedIndex())
				{
				case(0): 
				{
					Stats.setDifficulty(DifficultyType.Penny);
					break;
				}
				case(1): 
				{
					Stats.setDifficulty(DifficultyType.Lenard);
					break;
				}
				case(2):
				{
					Stats.setDifficulty(DifficultyType.Sheldon);
					break;
				}
				}
				switch(numOfPlayersCombo.getSelectedIndex())
				{
				case(0): 
				{
					if (player1.getText().equals("Computer") || player1.getText().equals("Computer 1") || player1.getText().equals("Computer 2"))
					{
						isCom = true;
					}

					else
					{
						Stats.setPlayer1(new Player(-1,-1,player1.getText()));
						Stats.setPlayer2(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer"));
						Stats.setBoard(null);
						Random rand = new Random();
						int r = rand.nextInt(2);
						if (r == 0)
							Stats.setCurrentPlayer(Stats.getPlayer1());
						else
							Stats.setCurrentPlayer(Stats.getPlayer2());
					}
					break;
				}
				case(1): 
				{
					if (player1.getText().equals("Computer") || player1.getText().equals("Computer 1") || player1.getText().equals("Computer 2"))
					{
						isCom = true;
					}
					if (player2.getText().equals("Computer") || player2.getText().equals("Computer 1") || player2.getText().equals("Computer 2"))
					{
						isCom = true;
					}
					if (!isCom)
					{
						Stats.setPlayer1(new Player(-1,-1,player1.getText()));
						Stats.setPlayer2(new Player(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),player2.getText()));
						Stats.setBoard(null);
						Random rand = new Random();
						int r = rand.nextInt(2);
						if (r == 0)
							Stats.setCurrentPlayer(Stats.getPlayer1());
						else
							Stats.setCurrentPlayer(Stats.getPlayer2());
					}
					break;
				}
				case(2):
				{
					Stats.setPlayer1(new ComputerPlayer(-1, -1,"Computer 1"));
					Stats.setPlayer2(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer 2"));
					Stats.setBoard(null);
					Random rand = new Random();
					int r = rand.nextInt(2);
					if (r == 0)
						Stats.setCurrentPlayer(Stats.getPlayer1());
					else
						Stats.setCurrentPlayer(Stats.getPlayer2());
					break;
				}
				}
				if (!isCom){
					frame.dispose();
					new MainScreen();
				}
				else
					JOptionPane.showMessageDialog(null, "The names can't be Computer");
			}
			else
			{
				playersNamePanel.add(notCom);
				add(playersNamePanel,3);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
