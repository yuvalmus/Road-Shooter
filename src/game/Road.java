package game;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Road implements DrawImageInterface 
{
	private static int velocity = 3;
	private final static int milliseconds = 8;
	private int lowestPoint;
	private int currX;
	private int currY;
	private final static Image roadImage = new ImageIcon("src/game/assets/roadHigher2.png").getImage();
	private Thread roadRunning;
	
	public Road(int x, int y)
	{
		this.currX = x;
		this.currY = y;
		this.lowestPoint = 1265;
		while (this.lowestPoint % velocity != 0)
		{
			this.lowestPoint--;
		}
		this.roadRunning = new Thread(new Runnable() {
			public void run() {
				
				while (true)
				{
					Road.this.currY += Road.velocity;
					try {
						Thread.sleep(Road.milliseconds);
					} catch (InterruptedException e) {return;}
					
					if (Road.this.currY >= Road.this.lowestPoint)
					{
						Road.this.currY = -1000;
					}
				}
			}
		});
	}

	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(roadImage, this.currX, this.currY, null);
	}
	
	public void initRoadMoveThread() {
		this.roadRunning = new Thread(new Runnable() {
			public void run() {
				
				while (true)
				{
					Road.this.currY += Road.velocity;
					try {
						Thread.sleep(Road.milliseconds);
					} catch (InterruptedException e) {return;}
					
					if (Road.this.currY >= Road.this.lowestPoint)
					{
						Road.this.currY = -1000;
					}
				}
			}
		});
	}
	
	public void startThread()
	{
		this.roadRunning.start();
	}
	
	public void interruptThread()
	{
		this.roadRunning.interrupt();
		initRoadMoveThread();
	}
}
