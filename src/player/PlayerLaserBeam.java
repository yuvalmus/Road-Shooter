package player;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

import game.GifStuff;
import game.Map;

public class PlayerLaserBeam extends GifStuff
{
	public static BufferedImage[] imagess;
	private static int laserDamage = 90;
	private final static int milliseconds = 30;
	
	public PlayerLaserBeam(int x, int y)
	{
		super(x, y);
		this.gifThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Rectangle enemyRect, result;
				int width = PlayerLaserBeam.imagess[0].getWidth();
				int height = PlayerLaserBeam.imagess[0].getHeight();
				for (int i = 0; i < Map.enemies.size(); i++)
				{
					enemyRect = Map.enemies.get(i).getVehicleBounds();
				    result = SwingUtilities.computeIntersection(x, y, width, height, enemyRect);
				    if (result.getWidth() > 0 && result.getHeight() > 0)
				    {
				    	Map.enemies.get(i).damageVehicleHealth(PlayerLaserBeam.laserDamage + PlayerVehicle.bonusPlayerDamage);
				    }
				}
				Map.imagesToDraw.add(PlayerLaserBeam.this);
				
				while (PlayerLaserBeam.this.currFrame < PlayerLaserBeam.imagess.length)
				{
					try {
						Thread.sleep(PlayerLaserBeam.milliseconds);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}
					PlayerLaserBeam.this.currFrame++;
				}
				Map.imagesToDraw.remove(PlayerLaserBeam.this);
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
