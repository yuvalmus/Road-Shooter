package player;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.Bullet;
import game.Map;

public class PlayerBullet extends Bullet
{
	private static int bulletDamage = 20;
	private int startingYValue;
	private Rectangle result, enemyRect;

	public PlayerBullet(int x, int y) 
	{
		super(x, y, -34, 10);
		this.bulletImage = new ImageIcon("src/game/assets/AR_bullet.png").getImage();
		this.startingYValue = y;
		this.bulletMoveTimer = new Timer(1, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currY -= 2;
		for (int i = 0; i < Map.enemies.size(); i++)
		{
			enemyRect = Map.enemies.get(i).getVehicleBounds();
		    result = SwingUtilities.computeIntersection(PlayerBullet.this.currX, PlayerBullet.this.currY, PlayerBullet.this.bulletImage.getWidth(null), PlayerBullet.this.bulletImage.getHeight(null), enemyRect);
		    if (result.getWidth() > 0 && result.getHeight() > 0)
		    {
		    	Map.enemies.get(i).damageVehicleHealth(PlayerBullet.bulletDamage + PlayerVehicle.bonusPlayerDamage);
		    	this.bulletMoveTimer.stop();
		    	Map.imagesToDraw.remove(PlayerBullet.this);
		    }
		}
		// if the bullet hit a certain distance from which the muzzle flash should be removed
		if (this.currY == this.startingYValue - 30) {
			this.muzzleFlashImage = null;
		}
		if (this.currY <= -20) {
			this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(PlayerBullet.this);
		}
	}
	
	public void start()
	{
		Map.imagesToDraw.add(PlayerBullet.this);
		this.bulletMoveTimer.start();
	}

	
}
