package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class HelpScreen extends JLayeredPane implements MouseListener {
	private JLabel back;
	private JFrame frame;
	
	public HelpScreen()
	{
		//frame
		frame = new JFrame("Help");
		int frameHeight = 800;
		int frameWidth = 800;
		
		//Panels
		JPanel backPanel = new JPanel();
		JPanel helpPanel = new JPanel();
		JPanel bgPanel = new JPanel();
		
		bgPanel.setLayout(new GridBagLayout());
		backPanel.setLayout(new BorderLayout());
		
		//Labels
		JLabel instructions = new JLabel("<html>This is Reversi game. In this game you"
				+ "<br> need to eat all your opponents checkers."
				+ "<br>Controls: you can drag a checker to a valid "
				+ "<br>spot or you click on the spot itself."
				+ "<br>To read more about the rules you can"
				+ "<br> visit http://en.wikipedia.org/wiki/Reversi</html>");
		back = new JLabel("Back");
		back.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		back.setForeground(Color.DARK_GRAY);
		
		
		
		try{
			JLabel backgroundImage = new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("../Backgrounds/help_background.jpg"))));
			bgPanel.add(backgroundImage);
			}
			catch(IOException e){
				System.out.println("file not exist");
			}
		
		instructions.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		instructions.setForeground(Color.BLUE);

		backPanel.add(back, BorderLayout.NORTH);
		add(bgPanel, new Integer(0));
		add(backPanel, new Integer(1));
		add(helpPanel, new Integer(1));
		helpPanel.add(instructions);

		backPanel.setBounds(1, 1, 500, 500);
		bgPanel.setBounds(1, 1, 1000, 1000);
		helpPanel.setBounds(1, frameHeight/5, 1000, 1000);
		
		helpPanel.setOpaque(false);
		backPanel.setOpaque(false);
		
		//Listeners
		back.addMouseListener(this);
		
		frame.pack();
		frame.setResizable(false);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setLocation(400, 150);
		frame.setVisible(true);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == back)
			back.setForeground(Color.WHITE);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == back)
			back.setForeground(Color.BLACK);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == back)
		{
			new MainScreen();
			frame.dispose();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
