package gameTools;
import game.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class which represents Checker on the board.
 */
public class Checker extends JLabel {
//	private Timer timer;
	private float width;
	private float height;
	private int xInMetric;
	private int yInMetric;
	private Board parentBoard;
	private Player player;
	private BufferedImage image;
	private int lastImageID;

	/**
	 * Constructor - gets all the info it needed.
	 * @param player
	 * @param parentBoard
	 * @param xInMetric
	 * @param yInMetric
	 * @param width
	 * @param height
	 */
	public Checker(Player player, Board parentBoard, int xInMetric, int yInMetric, float width, float height) 
	{
		this.player = player;
		this.parentBoard = parentBoard;
		this.width = width;
		this.height = height;
		this.xInMetric = xInMetric;
		this.yInMetric = yInMetric;
		lastImageID = -1;
		
		initializeImage(player.getColor(), player.getImageID());
		setBounds((int) (xInMetric*width), (int) (yInMetric*height), (int) width, (int) height);
	}
	
	/**
	 * Creates Jlabel with the following backgroud and foreground ID of images in icons directory
	 * @param bgID The background image ID 
	 * @param fgID The foreground image ID
	 */
	public void initializeImage(Color circleColor, int fgID)
	{
		setOpaque(false);
		setIcon(new ImageIcon(getImage(circleColor, fgID)));
	}
	
	/**
	 * resized image of Checker.
	 * @param width
	 * @param height
	 */
	public void resizeImage(float width, float height)
	{
		this.width = width;
		this.height = height;
		setBounds((int) (xInMetric*width), (int) (yInMetric*height), (int) width, (int) height);
		initializeImage(player.getColor(), player.getImageID());
	}
	
	/**
	 * gets  the image played by Checker.
	 * @param circleColor
	 * @param fgID
	 * @return
	 */
	private Image getImage(Color circleColor, int fgID)
	{
		// building the circles
		BufferedImage bg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bg.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillOval((int) (0.025 * width), (int) (0.025 * height), (int) (0.95 * width), (int) (0.95 * height));
		
		g2d.setStroke(new BasicStroke((int) (0.1 * Math.min(width, height))));
		g2d.setColor(circleColor);
		g2d.drawOval((int) (0.025 * width), (int) (0.025 * height), (int) (0.95 * width), (int) (0.95 * height));

		g2d.dispose();

		BufferedImage scaled;
		Graphics g;
		
		if(fgID != lastImageID)
		{
			BufferedImage fg = null;
			try { fg = ImageIO.read(getClass().getResource("../Icons/" + fgID + ".png")); }
			catch (IOException e) { }
	
			scaled = new BufferedImage((int) (width * 0.6), (int) (height * 0.6), BufferedImage.TYPE_INT_ARGB);
			g = scaled.getGraphics();
			g.drawImage(fg,0, 0, scaled.getWidth(), scaled.getHeight(), null);
			lastImageID = fgID;
			image = fg;
		}	
		else
		{
			if(image == null)
			{
				try { image = ImageIO.read(getClass().getResource("../Icons/" + fgID + ".png")); } 
				catch (IOException e) { }
			}
			
			scaled = new BufferedImage((int) (width * 0.6), (int) (height * 0.6), BufferedImage.TYPE_INT_ARGB);
			g = scaled.getGraphics();
			g.drawImage(image,0, 0, scaled.getWidth(), scaled.getHeight(), null);
		}
		
		g.dispose();
		bg.getGraphics().drawImage(scaled, bg.getWidth()/2 - scaled.getWidth()/2, bg.getHeight()/2 - scaled.getHeight()/2, null );
		
		return bg;
	}

	public void mousePressed(int x, int y) {
		parentBoard.setComponentZOrder(this, 0);
		axisXPressed = x;
		axisYPressed = y;
	}

	public void mouseReleased() {
			setBounds((int) (xInMetric*width + 1), (int) (yInMetric*height + 1), (int) width, (int) height);
	}

	private int axisXPressed;
	private int axisYPressed;
	
	public void mouseDragged(int globalX, int globalY) {
		setBounds((int) (globalX - axisXPressed), (int) (globalY - axisYPressed), (int) width, (int) height);
	}

	public int getXInMetric() {
		return xInMetric;
	}

	public void setXInMetric(int xInMetric) {
		this.xInMetric = xInMetric;
	}

	public int getYInMetric() {
		return yInMetric;
	}

	public void setYInMetric(int yInMetric) {
		this.yInMetric = yInMetric;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}