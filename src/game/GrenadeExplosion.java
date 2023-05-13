package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GrenadeExplosion extends GifStuff
{
	public static BufferedImage[] imagess;
	private final static int milliseconds = 20;
	
	public GrenadeExplosion(int x, int y)
	{
		super(x, y);
		this.gifThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map.imagesToDraw.add(GrenadeExplosion.this);
				while (GrenadeExplosion.this.currFrame < GrenadeExplosion.imagess.length)
				{
					try {
						Thread.sleep(GrenadeExplosion.milliseconds);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}
					GrenadeExplosion.this.currFrame++;
				}
				Map.imagesToDraw.remove(GrenadeExplosion.this);
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
