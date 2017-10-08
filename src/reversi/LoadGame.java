package reversi;

import game.ComputerPlayer;
import game.Game;
import game.Player;
import game.Stats;
import game.Stats.DifficultyType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class LoadGame extends JLayeredPane implements MouseListener {
	private JLabel[] load;
	private JLabel back;
	private JFrame frame;
	private Player[][] boardToLoad;

	public LoadGame()
	{
		int frameHeight = 800;
		int frameWidth = 800;

		//Panels
		JPanel loadPanel = new JPanel();
		JPanel bgPanel = new JPanel();
		JPanel welcomePanel = new JPanel();

		//Layouts
		bgPanel.setLayout(new GridBagLayout());
		loadPanel.setLayout(new BoxLayout(loadPanel,BoxLayout.Y_AXIS));
		welcomePanel.setLayout(new FlowLayout());

		loadPanel.setOpaque(false);
		
		//Background image
		try{
			JLabel backgroundImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Backgrounds/load_background.jpg"))));
			bgPanel.add(backgroundImage);
		}
		catch(IOException e){
			System.out.println("file not exist");
		}
		add(bgPanel, new Integer(0));


		//Labels
		load = new JLabel[7];
		JLabel[] emptyGame = new JLabel[7];
		JLabel headline = new JLabel("                    Load Game");
		back = new JLabel("Back");

		headline.setFont(new Font("Times New Roman", Font.BOLD, 40));
		headline.setForeground(Color.CYAN);
		back.setFont(new Font("Times New Roman", 0, 30));
		back.setForeground(Color.WHITE);
		welcomePanel.add(back);
		welcomePanel.add(headline);
		welcomePanel.setOpaque(false);
		add(welcomePanel, new  Integer(1));


		//Check if file is exist and act accordingly
		for (int i = 0; i < load.length; i++)
		{
			File f = new File("Saves/Game "+i+".txt");
			if (f.exists())
			{
				load[i] = new JLabel("Load Game  " + (i+1));
				load[i].setFont(new Font("Ariel", Font.PLAIN, 35));
				load[i].setForeground(Color.RED);
				loadPanel.add(load[i]);
				loadPanel.add(Box.createRigidArea(new Dimension(0, 7)));
				load[i].addMouseListener(this);
			}
			else
			{
				emptyGame[i] = new JLabel("Game is empty " + (i+1));
				emptyGame[i].setFont(new Font("Ariel", Font.PLAIN, 35));
				emptyGame[i].setForeground(Color.DARK_GRAY);
				loadPanel.add(emptyGame[i]);
				loadPanel.add(Box.createRigidArea(new Dimension(0, 7)));
			}
		}
		add(loadPanel, new Integer(1));

		back.addMouseListener(this);

		bgPanel.setBounds(-100,-101, 1000, 1000);
		loadPanel.setBounds(frameWidth/3+40, frameHeight/3+80, 500, 500);
		welcomePanel.setBounds(-150, 50, 800, 5000);

		frame = new JFrame("Load Game");
		frame.pack();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setResizable(false);
		frame.setLocation(400, 150);
		frame.setVisible(true);
	}

	private void loadGame(int whatToLoad) throws IOException
	{
		String[] data;
		List<String> game = new ArrayList<String>();
		BufferedReader output = null;
		try {
			File file = new File("Saves/Game " + whatToLoad + ".txt");  //what to open
			output = new BufferedReader(new FileReader(file));
			int i=0;
			String tmp = "";
			while(tmp != null)
			{
				tmp = output.readLine();
				if (tmp != null && !tmp.equals(""))
				{
					game.add(i, tmp);
					data = game.get(i).split("-");
					putStats(i,data);
					i++;
				}
			}


		}

		catch(IOException e){}

		finally { if ( output != null ) output.close(); }
	}

	private void putStats(int whatRow, String[] splitRow)
	{
		
		if (whatRow == 0)
		{
			String[] lengthWidth;
			lengthWidth = splitRow[0].split("X");
			Stats.setN(Integer.parseInt(lengthWidth[0])); //set the n dimension
			Stats.setM(Integer.parseInt(lengthWidth[1])); //set the m dimension
			//the difficulty of the computer
			if (splitRow[4].equals("Penny")) 
				Stats.setDifficulty(DifficultyType.Penny);
			if (splitRow[4].equals("Lenard"))
				Stats.setDifficulty(DifficultyType.Lenard);
			if (splitRow[4].equals("Sheldon"))
				Stats.setDifficulty(DifficultyType.Sheldon);
			//if player 1 is computer
			if(splitRow[1].equals("Computer") || splitRow[1].equals("Computer 2") || splitRow[1].equals("Computer 1"))
				Stats.setPlayer1(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer"));
			Stats.getPlayer1().setName(splitRow[1]);
			//if player 2 is computer
			if(splitRow[2].equals("Computer") || splitRow[2].equals("Computer 2") || splitRow[2].equals("Computer 2"))
				Stats.setPlayer2(new ComputerPlayer(Stats.getPlayer1().getHue(), Stats.getPlayer1().getImageID(),"Computer 2"));
			Stats.getPlayer2().setName(splitRow[2]);
			Stats.setCurrentPlayer((splitRow[3].equals("player1") ? Stats.getPlayer1() : Stats.getPlayer2())); //set who is the current player
			boardToLoad = new Player[Stats.getN()][Stats.getM()];
		}
		else
		{
			String[] posAndPlayer;
			//build the board 
			for (int i = 0; i < splitRow.length; i++) {
				posAndPlayer = splitRow[i].split(",");
				posAndPlayer[0] = posAndPlayer[0].substring(1, posAndPlayer[0].length());
				posAndPlayer[2] = posAndPlayer[2].substring(0, 1);
				if (Integer.parseInt(posAndPlayer[2]) == 0)
					boardToLoad[Integer.parseInt(posAndPlayer[0])][Integer.parseInt(posAndPlayer[1])] = Stats.getPlayer1();
				else
					boardToLoad[Integer.parseInt(posAndPlayer[0])][Integer.parseInt(posAndPlayer[1])] = Stats.getPlayer2();
			}
			Stats.setBoard(boardToLoad);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {


	}

	@Override
	public void mouseEntered(MouseEvent e) {
		for(int i = 0; i < load.length; i++)
		{
			if (e.getSource() == load[i])
				load[i].setForeground(Color.WHITE);
		}

		if (e.getSource() == back)
		{
			back.setForeground(Color.YELLOW);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		for(int i = 0; i < load.length; i++)
		{
			if (e.getSource() == load[i])
				load[i].setForeground(Color.RED);
		}

		if (e.getSource() == back)
		{
			back.setForeground(Color.WHITE);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == back)
		{
			frame.dispose();
			new MainScreen();
		}

		for(int i = 0; i < load.length; i++)
		{
			if (e.getSource() == load[i])
				try {
					loadGame(i);
					frame.dispose();
					new Game();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
