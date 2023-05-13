package player;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.Bullet;
import game.Map;

public class PlayerLaserBullet extends Bullet
{
	private static int bulletDamage = 35;
	private int startingYValue;
	private Rectangle result, enemyRect;

	public PlayerLaserBullet(int x, int y) 
	{
		super(x, y, -28, 10);
		this.bulletImage = new ImageIcon("src/game/assets/laser_bullet.png").getImage();
		this.muzzleFlashImage = new ImageIcon("src/game/assets/laserMuzzleFlash.png").getImage();
		this.startingYValue = y;
		this.bulletMoveTimer = new Timer(1, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currY -= 2;
		for (int i = 0; i < Map.enemies.size(); i++)
		{
			enemyRect = Map.enemies.get(i).getVehicleBounds();
		    result = SwingUtilities.computeIntersection(PlayerLaserBullet.this.currX, PlayerLaserBullet.this.currY, PlayerLaserBullet.this.bulletImage.getWidth(null), PlayerLaserBullet.this.bulletImage.getHeight(null), enemyRect);
		    if (result.getWidth() > 0 && result.getHeight() > 0)
		    {
		    	Map.enemies.get(i).damageVehicleHealth(PlayerLaserBullet.bulletDamage + PlayerVehicle.bonusPlayerDamage);
		    	this.bulletMoveTimer.stop();
		    	Map.imagesToDraw.remove(PlayerLaserBullet.this);
		    }
		}
		// if the bullet hit a certain distance from which the muzzle flash should be removed
		if (this.currY == this.startingYValue - 30) {
			this.muzzleFlashImage = null;
		}
		if (this.currY <= -20) {
			this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(PlayerLaserBullet.this);
		}
	}
	
	public void start()
	{
		Map.imagesToDraw.add(PlayerLaserBullet.this);
		this.bulletMoveTimer.start();
	}
	
}
