package sidePanel;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class SidePanelImg implements game.DrawImageInterface 
{	
	//                                                  0                               1                        2                       3                  4                 5              6
	private final static Image[] images = { newImage("sidePanelSmaller"),  newImage("selectionRect"), newImage("blackedRect"), newImage("noAmmo"), newImage("upgrade"), newImage("Z"), newImage("X") };
	private int currX, currY;
	private Image currImage;
	
	public SidePanelImg(int currX, int currY, int whichImg)
	{
		this.currImage = SidePanelImg.images[whichImg];
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
	
	public int getCurrY()
	{
		return this.currY;
	}
	
	private static Image newImage(String nameOfFile)
	{
		return new ImageIcon("src/game/assets/" + nameOfFile + ".png").getImage();
	}
	
	
}
