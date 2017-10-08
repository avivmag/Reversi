package reversi;
import game.Player;
import game.Stats;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SaveGame extends JLayeredPane implements MouseListener{
	private JLabel back;
	private JLabel[] save;
	private JFrame frame;

	public SaveGame()
	{
		int frameHeight = 800;
		int frameWidth = 800;

		//Panels
		JPanel savePanel = new JPanel();
		JPanel bgPanel = new JPanel();
		JPanel welcomePanel = new JPanel();

		//Layouts
		bgPanel.setLayout(new GridBagLayout());
		savePanel.setLayout(new BoxLayout(savePanel,BoxLayout.Y_AXIS));
		welcomePanel.setLayout(new FlowLayout());

		savePanel.setOpaque(false);

		try{
			JLabel backgroundImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Backgrounds/save_background.png"))));
			bgPanel.add(backgroundImage);
		}
		catch(IOException e){
			System.out.println("file not exist");
		}
		add(bgPanel, new Integer(0));

		//Labels
		save = new JLabel[7];
		JLabel[] savedGame = new JLabel[7];
		JLabel headline = new JLabel("                    Save Game");
		back = new JLabel("Back");

		headline.setFont(new Font("Times New Roman", Font.BOLD, 40));
		headline.setForeground(Color.RED);
		back.setFont(new Font("Times New Roman", 0, 30));
		back.setForeground(Color.WHITE);
		welcomePanel.add(back);
		welcomePanel.add(headline);
		welcomePanel.setOpaque(false);
		add(welcomePanel, new  Integer(1));

		//Check if file is exist and act accordingly
		for (int i = 0; i < save.length; i++)
		{
			File f = new File("Saves/Game "+i+".txt"); //where to save
			if (!f.exists())
			{
				save[i] = new JLabel("Save Game  " + (i+1));
				save[i].setFont(new Font("Ariel", Font.PLAIN, 35));
				save[i].setForeground(Color.BLUE);
				savePanel.add(save[i]);
				savePanel.add(Box.createRigidArea(new Dimension(0, 7)));
				save[i].addMouseListener(this);
			}
			else
			{
				savedGame[i] = new JLabel("Game is Saved " + (i+1));
				savedGame[i].setFont(new Font("Ariel", Font.PLAIN, 35));
				savedGame[i].setForeground(Color.GRAY);
				savePanel.add(savedGame[i]);
				savePanel.add(Box.createRigidArea(new Dimension(0, 7)));
			}
		}
		add(savePanel, new Integer(1));

		back.addMouseListener(this);

		bgPanel.setBounds(-100,-101, 1000, 1000);
		savePanel.setBounds(frameWidth/3+40, frameHeight/3+80, 500, 500);
		welcomePanel.setBounds(-150, 50, 800, 5000);

		frame = new JFrame("Save Game");
		frame.pack();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setResizable(false);
		frame.setLocation(400, 150);
		frame.setVisible(true);
	}


	public void saveGame(int whatGame){
			PrintWriter writer;
			try {
				if(Files.notExists(Paths.get("Saves")))
					Files.createDirectory(Paths.get("Saves"));
				writer = new PrintWriter("Saves/Game "+whatGame+".txt", "UTF-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			//writes the main stats: nXm, player1, player2, current player, computer difficulty
			String text = Stats.getN() + "X" + Stats.getM() + "-" + Stats.getPlayer1().getName();
			text += "-" + Stats.getPlayer2().getName() + "-" + Stats.getCurrentPlayer().getName() + "-" + Stats.getDifficulty().toString();
			writer.println(text);
		
			text = "";
			Player[][] checkers = Stats.getBoard();
			int count = 0;
			//save default stats if the board is empty
			if (checkers == null)
			{
				for (int i = Stats.getN() / 2 + Stats.getN() % 2 - 1; i < Stats.getN() / 2 + Stats.getN() % 2 + 1; i++) 
					for (int j = Stats.getM() / 2 + Stats.getM() % 2 - 1; j < Stats.getM() / 2 + Stats.getM() % 2 + 1; j++)
					{
						text += "(" + i + "," + j + "," + (j - Stats.getM() / 2 + Stats.getM() % 2 + 1) + ")-";
					}
				text = text.substring(0, text.length()-1);
				writer.write(text);
			}
			else
			{
				//save the board by the checkers positions 
				for (int i=0; i < Stats.getN(); i++)
					for(int j=0; j < Stats.getM(); j++)
					{
						if (checkers[i][j] != null){
							if (checkers[i][j].getName() == Stats.getPlayer1().getName())
								text += "(" + i + "," + j + "," + "0" + ")-";
							else
								text += "(" + i + "," + j + "," + "1" + ")-";
							count++;
						}
						//new line
						if (count == 7)
						{
							writer.println(text);
							text="";
						}
					}
				if (!text.equals(""))
					text = text.substring(0, text.length()-1);
				writer.write(text);
			}
			writer.close();

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		for(int i = 0; i < save.length; i++)
		{
			if (e.getSource() == save[i])
				save[i].setForeground(Color.RED);
		}

		if (e.getSource() == back)
		{
			back.setForeground(Color.YELLOW);
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {
		for(int i = 0; i < save.length; i++)
		{
			if (e.getSource() == save[i])
				save[i].setForeground(Color.BLUE);
		}
		if (e.getSource() == back)
		{
			back.setForeground(Color.WHITE);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(int i = 0; i < save.length; i++)
		{
			if (e.getSource() == save[i] && Stats.getBoard() !=  null){
				saveGame(i);
				frame.dispose();
				new MainScreen();
			}
		}

		if (e.getSource() == back)
		{
			frame.dispose();
			new MainScreen();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
