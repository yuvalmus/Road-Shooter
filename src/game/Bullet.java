package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.Timer;

public abstract class Bullet implements DrawImageInterface, ActionListener
{
	protected int currX;
	protected int currY;
	protected int muzzleX, muzzleY;
	protected Image bulletImage;
	protected Image muzzleFlashImage;
	protected Timer bulletMoveTimer;
	
	public Bullet(int x, int y, int muzzleXOffset, int muzzleYOffset) 
	{
		this.currX = x;
		this.currY = y;
		this.muzzleX = x + muzzleXOffset;
		this.muzzleY = y + muzzleYOffset;
		this.muzzleFlashImage = new ImageIcon("src/game/assets/muzzleFlash.png").getImage();
	}
	
	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(muzzleFlashImage, muzzleX, muzzleY, null);
		g.drawImage(bulletImage, currX, currY, null);
	}
	
	public void stop() {
		this.bulletMoveTimer.stop();
	}
	
}
