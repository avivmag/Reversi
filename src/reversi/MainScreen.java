package reversi;

import game.Game;
import game.Stats;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.imageio.*;

import java.io.*;

public class MainScreen extends JLayeredPane implements MouseListener{
	private JLabel continueGame;
	private JLabel newGame;
	private JLabel loadGame;
	private JLabel saveGame;
	private JLabel settings;
	private JLabel help;
	private JLabel exit;
	private JLabel welcome;
	private JFrame frame;
	public MainScreen()
	{
		int frameHeight = 800;
		int frameWidth = 800;
		
		//Labels
		welcome = new JLabel("Welcome to Reversi Game");
		continueGame = new JLabel("  Continue");
		newGame = new JLabel("New Game");
		loadGame = new JLabel("Load Game");
		saveGame = new JLabel("Save Game");
		settings = new JLabel("   Settings");
		help = new JLabel("      Help");
		exit = new JLabel("Exit Game");
		
		//Panels
		JPanel welcomePanel = new JPanel();
		JPanel menuesPanel = new JPanel();
		JPanel bgPanel = new JPanel();
		
		//Layouts
		bgPanel.setLayout(new GridBagLayout());
		menuesPanel.setLayout(new BoxLayout(menuesPanel,BoxLayout.Y_AXIS));
		welcomePanel.setLayout(new GridBagLayout());

		//Listeners
		continueGame.addMouseListener(this);
		newGame.addMouseListener(this);
		loadGame.addMouseListener(this);
		saveGame.addMouseListener(this);
		settings.addMouseListener(this);
		help.addMouseListener(this);
		exit.addMouseListener(this);

		try{
			JLabel backgroundImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Backgrounds/main_background.png"))));
			bgPanel.add(backgroundImage);
		}
		catch(IOException e){
			System.out.println("file not exist");
		}	
		
		welcome.setFont(new Font("Times New Roman", Font.BOLD, 40));
		continueGame.setFont(new Font("Ariel", Font.PLAIN, 44));
		newGame.setFont(new Font("Ariel", Font.PLAIN, 44));
		loadGame.setFont(new Font("Ariel", Font.PLAIN, 44));
		saveGame.setFont(new Font("Ariel", Font.PLAIN, 44));
		settings.setFont(new Font("Ariel", Font.PLAIN, 44));
		help.setFont(new Font("Ariel", Font.PLAIN, 44));
		exit.setFont(new Font("Ariel", Font.PLAIN, 44));
		
		welcome.setForeground(Color.green);
		continueGame.setForeground(Color.RED);
		newGame.setForeground(Color.RED);
		loadGame.setForeground(Color.RED);
		saveGame.setForeground(Color.RED);
		settings.setForeground(Color.RED);
		help.setForeground(Color.RED);
		exit.setForeground(Color.RED);

		welcomePanel.add(welcome);
		if (Stats.getBoard()!=null)
			menuesPanel.add(continueGame);
		menuesPanel.add(newGame);
		menuesPanel.add(loadGame);
		menuesPanel.add(saveGame);
		menuesPanel.add(settings);
		menuesPanel.add(help);
		menuesPanel.add(exit);

		add(bgPanel,new Integer(0));
		add(menuesPanel, new Integer(1));
		add(welcomePanel, new Integer(1));
		
		welcomePanel.setBounds(150, 10, 500, 100);
		menuesPanel.setBounds(frameWidth/3, frameHeight/3, 500, 500);
		bgPanel.setBounds(1, 1, 1000, 1000);
		welcomePanel.setOpaque(false);
		menuesPanel.setOpaque(false);
		
		frame = new JFrame("Reversi");
		frame.pack();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(400, 150);
		frame.setSize(frameHeight, frameWidth);
		frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == continueGame)
			continueGame.setForeground(Color.WHITE);
		if (e.getSource() == newGame)
			newGame.setForeground(Color.WHITE);
		if (e.getSource() == loadGame)
			loadGame.setForeground(Color.WHITE);
		if (e.getSource() == saveGame)
			saveGame.setForeground(Color.WHITE);
		if (e.getSource() == settings)
			settings.setForeground(Color.WHITE);
		if (e.getSource() == help)
			help.setForeground(Color.WHITE);
		if (e.getSource() == exit)
			exit.setForeground(Color.WHITE);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == continueGame)
			continueGame.setForeground(Color.RED);
		if (e.getSource() == newGame)
			newGame.setForeground(Color.RED);
		if (e.getSource() == loadGame)
			loadGame.setForeground(Color.RED);
		if (e.getSource() == saveGame)
			saveGame.setForeground(Color.RED);
		if (e.getSource() == settings)
			settings.setForeground(Color.RED);
		if (e.getSource() == help)
			help.setForeground(Color.RED);
		if (e.getSource() == exit)
			exit.setForeground(Color.RED);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == continueGame)
		{
			frame.dispose();
			new Game();
		}
		if (e.getSource() == newGame)
		{
			frame.dispose();
			Stats.setBoard(null);
			new Game();
			
		}
		if (e.getSource() == loadGame)
		{
			new LoadGame();
			frame.dispose();
		}
		if (e.getSource() == saveGame)
		{
			new SaveGame();
			frame.dispose();
		}
		if (e.getSource() == help)
		{
			new HelpScreen();
			frame.dispose();
		}
		if (e.getSource() == settings)
		{
			new SettingsScreen();
			frame.dispose();
		}
		if (e.getSource() == exit)
			System.exit(0);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
