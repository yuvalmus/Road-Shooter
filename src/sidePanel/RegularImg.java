package sidePanel;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class RegularImg implements game.DrawImageInterface 
{
	private int currX, currY;
	private String imageUrl;  // relative URL to src directory, f.e: "src/game/assets/image.png"
	private Image currImage;
	
	public RegularImg(int currX, int currY, String imageUrl)
	{
		this.imageUrl = imageUrl;
		this.currImage = new ImageIcon(imageUrl).getImage();
		this.currX = currX;
		this.currY = currY;
	}
	
	@Override
	public void draw(Graphics g)
	{
		g.drawImage(this.currImage, this.currX, this.currY, null);
	}
	
	// moves the image to the requested coordinates.
	// if any of the coordinates is -1 it means that this specific coordinate's value should NOT be changed
	public void moveTo(int x, int y)
	{
		if (x != -1)
			this.currX = x;
		if (y != -1)
			this.currY = y;
	}
	
	public int getX() {
		return this.currX;
	}
	
	public int getY() {
		return this.currY;
	}
	
	public void setX(int newX) {
		this.currX = newX;
	}
	
	public void setY(int newY) {
		this.currY = newY;
	}
}
