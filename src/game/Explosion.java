package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Explosion extends GifStuff
{
	public static BufferedImage[] imagess;
	private final static int milliseconds = 30;
	
	public Explosion(int x, int y)
	{
		super(x, y);
		this.gifThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map.imagesToDraw.add(Explosion.this);
				while (Explosion.this.currFrame < Explosion.imagess.length)
				{
					try {
						Thread.sleep(Explosion.milliseconds);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}
					Explosion.this.currFrame++;
				}
				Map.imagesToDraw.remove(Explosion.this);
			}
		});
	}

	@Override
	public void draw(Graphics g) 
	{
		try
		{
			g.drawImage(imagess[this.currFrame], this.x, this.y, null);
		}
		catch (ArrayIndexOutOfBoundsException e) {}
	}
}
