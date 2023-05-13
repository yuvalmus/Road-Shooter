package player;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.Bullet;
import game.GrenadeExplosion;
import game.Map;

public class PlayerGrenadeLauncher extends Bullet
{
	private static int bulletDamage = 50;
	private int startingYValue;
	private Rectangle result, enemyRect;

	public PlayerGrenadeLauncher(int x, int y) 
	{
		super(x, y, -34, 10);
		this.bulletImage = new ImageIcon("src/game/assets/grenade.png").getImage();
		this.startingYValue = y;
		this.bulletMoveTimer = new Timer(3, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currY -= 2;
		for (int i = 0; i < Map.enemies.size(); i++)
		{
			enemyRect = Map.enemies.get(i).getVehicleBounds();
		    result = SwingUtilities.computeIntersection(PlayerGrenadeLauncher.this.currX, PlayerGrenadeLauncher.this.currY, PlayerGrenadeLauncher.this.bulletImage.getWidth(null), PlayerGrenadeLauncher.this.bulletImage.getHeight(null), enemyRect);
		    if (result.getWidth() > 0 && result.getHeight() > 0)
		    {
		    	Map.enemies.get(i).damageVehicleHealth(PlayerGrenadeLauncher.bulletDamage + PlayerVehicle.bonusPlayerDamage);
		    	Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
		    	this.bulletMoveTimer.stop();
		    	GrenadeExplosion [] explosions = new GrenadeExplosion[3];
		    	for (int j = 0; j < explosions.length; j++) 
		    	{
					explosions[j] = new GrenadeExplosion(currX - 300 + 150 * j, currY - 100);
				}
		    	for (int j = 0; j < explosions.length; j++) 
		    	{
					explosions[j].startThread();
				}
		    }
		}
		// if the bullet hit a certain distance from which the muzzle flash should be removed
		if (this.currY == this.startingYValue - 30) {
			this.muzzleFlashImage = null;
		}
		if (this.currY <= -20) {
			this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
		}
	}
	
//	@Override
//	public void startMoving()
//	{
//		this.bulletMovingThread = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				boolean intersected = false;
//				Map.imagesToDraw.add(PlayerGrenadeLauncher.this);
//				int y = PlayerGrenadeLauncher.this.currY;
//				Rectangle result, enemyRect;
//				while (PlayerGrenadeLauncher.this.currY > y - 30)
//				{
//					currY -= 2;
//					for (int i = 0; i < Map.enemies.size(); i++)
//					{
//						enemyRect = Map.enemies.get(i).getVehicleBounds();
//					    result = SwingUtilities.computeIntersection(PlayerGrenadeLauncher.this.currX - 150, PlayerGrenadeLauncher.this.currY, PlayerGrenadeLauncher.this.bulletImage.getWidth(null) + 300, PlayerGrenadeLauncher.this.bulletImage.getHeight(null), enemyRect);
//					    if (result.getWidth() > 0 && result.getHeight() > 0)
//					    {
//					    	intersected = true;
//					    	Map.enemies.get(i).damageVehicleHealth(PlayerGrenadeLauncher.bulletDamage + PlayerVehicle.bonusPlayerDamage);
//					    	Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
//					    	GrenadeExplosion [] explosions = new GrenadeExplosion[3];
//					    	for (int j = 0; j < explosions.length; j++) 
//					    	{
//								explosions[j] = new GrenadeExplosion(currX - 300 + 150 * j, currY - 100);
//							}
//					    	for (int j = 0; j < explosions.length; j++) 
//					    	{
//								explosions[j].startThread();
//							}
//					    }
//					}
//					if (intersected)
//					{
//						Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
//						return;
//					}
//					try {
//						Thread.sleep(3);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
//				}
//				PlayerGrenadeLauncher.this.muzzleFlashImage = null;
//				while (currY > -20)
//				{
//					currY -= 2;
//					for (int i = 0; i < Map.enemies.size(); i++)
//					{
//						enemyRect = Map.enemies.get(i).getVehicleBounds();
//					    result = SwingUtilities.computeIntersection(PlayerGrenadeLauncher.this.currX - 150, PlayerGrenadeLauncher.this.currY, PlayerGrenadeLauncher.this.bulletImage.getWidth(null) + 300, PlayerGrenadeLauncher.this.bulletImage.getHeight(null), enemyRect);
//					    if (result.getWidth() > 0 && result.getHeight() > 0)
//					    {
//					    	intersected = true;
//					    	if (Map.enemies.get(i).damageVehicleHealth(PlayerGrenadeLauncher.bulletDamage + PlayerVehicle.bonusPlayerDamage))
//					    		i--;
//					    	Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
//					    	GrenadeExplosion [] explosions = new GrenadeExplosion[3];
//					    	for (int j = 0; j < explosions.length; j++) 
//					    	{
//								explosions[j] = new GrenadeExplosion(currX - 300 + 150 * j, currY - 100);
//							}
//					    	for (int j = 0; j < explosions.length; j++) 
//					    	{
//								explosions[j].startThread();
//							}
//					    }
//					}
//					if (intersected)
//					{
//						Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
//						return;
//					}
//					try {
//						Thread.sleep(3);
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					}
//				}
//				Map.imagesToDraw.remove(PlayerGrenadeLauncher.this);
//			}
//		});
//		
//	}
	
	public void start()
	{
		Map.imagesToDraw.add(PlayerGrenadeLauncher.this);
		this.bulletMoveTimer.start();
	}
	
}
