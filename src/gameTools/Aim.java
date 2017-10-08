package gameTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Class which represents the aiming object of the player on the board with the mouse.
 */
public class Aim extends JLabel {
	private float width;
	private float height;
	private int xInMetric;
	private int yInMetric;
	private Color color;
	
	/**
	 * Constructor.
	 * @param color The color of the aim.
	 * @param width The width of the aim.
	 * @param height The height of the aim.
	 */
	public Aim(Color color, float width, float height)
	{
		this.color = color;
		this.width = width;
		this.height = height;
		this.xInMetric = -1;
		this.yInMetric = -1;
		initializeImage();		
	}
	
	/**
	 * Shows the aim on the screen in a specific position on the board. 
	 * @param xInMetric
	 * @param yInMetric
	 */
	public void showAim(int xInMetric, int yInMetric)
	{
		this.xInMetric = xInMetric;
		this.yInMetric = yInMetric;
		setBounds((int) (xInMetric*2*width + width/2), (int) (yInMetric*2*height + height/2), (int) width, (int) height);
		setVisible(true);
	}
	
	/**
	 * Hides the aim.
	 */
	public void hideAim()
	{
		setVisible(false);
	}
	
	/**
	 * Sets the image of the aim.
	 */
	private void initializeImage()
	{
		setOpaque(false);
		setIcon(new ImageIcon(getImage()));
	}
	
	/**
	 * Used to change the aim dimensions.
	 * @param width
	 * @param height
	 */
	public void resizeImage(float width, float height)
	{
		this.width = width;
		this.height = height;
		setBounds((int) (xInMetric*2*width + width/2), (int) (yInMetric*2*height + height/2), (int) width, (int) height);
		initializeImage();
	}
	
	/**
	 * Returns the image which will be used for the aim on the board. 
	 */
	private Image getImage()
	{
		// building the circles
		BufferedImage bg = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bg.createGraphics();

		g2d.setColor(color);
		g2d.setRenderingHint (RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.fillOval((int) (0.025 * width), (int) (0.025 * height), (int) (0.95 * width), (int) (0.95 * height));
		
		g2d.setStroke(new BasicStroke((int) (0.05 * Math.min(width, height))));
		g2d.setColor(new Color(Integer.MAX_VALUE - color.getRGB()));
		g2d.drawOval((int) (0.025 * width), (int) (0.025 * height), (int) (0.95 * width), (int) (0.95 * height));

		g2d.dispose();
		
		return bg;
	}
}
