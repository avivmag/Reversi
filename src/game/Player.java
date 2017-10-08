package game;

import java.awt.Color;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Class which represents player in the game.
 */
public class Player {
	private Color color;
	private int imageID;
	private float hue;
	private String name;
	
	/**
	 * Constructor.
	 * @param notAllowedHue This hue is not allowed for the color of the player checkers.
	 * @param noImageID This imageID is not allowed for the image destination of the player checkers.
	 * @param name The name of the player.
	 */
	public Player(float notAllowedHue, int noImageID, String name)
	{
		this.name = name;
		Random rand = new Random();
		hue = rand.nextFloat();
		while(Math.abs(hue - notAllowedHue) < 0.2 || ( notAllowedHue != -1 && Math.abs(hue - notAllowedHue) > 0.8))
			hue = rand.nextFloat();
		color = Color.getHSBColor(hue, 1f, 1f);

		do
			imageID = rand.nextInt(17) + 1;
		while(imageID == noImageID);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}
	
	public int getImageID() {
		return imageID;
	}
	
	public float getHue() {
		return hue;
	}

	public String getName() {
		return name;
	}
}
