package player;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.Bullet;
import game.Map;

public class PlayerRPG extends Bullet
{
	private static int bulletDamage = 60;
	private int startingYValue;
	private Rectangle result, enemyRect;

	public PlayerRPG(int x, int y) 
	{
		super(x, y, -33, 35);
		this.bulletImage = new ImageIcon("src/game/assets/rpg_rocket_small.png").getImage();
		this.startingYValue = y;
		this.bulletMoveTimer = new Timer(3, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currY -= 2;
		for (int i = 0; i < Map.enemies.size(); i++)
		{
			enemyRect = Map.enemies.get(i).getVehicleBounds();
		    result = SwingUtilities.computeIntersection(PlayerRPG.this.currX, PlayerRPG.this.currY, PlayerRPG.this.bulletImage.getWidth(null), PlayerRPG.this.bulletImage.getHeight(null), enemyRect);
		    if (result.getWidth() > 0 && result.getHeight() > 0)
		    {
		    	Map.enemies.get(i).damageVehicleHealth(PlayerRPG.bulletDamage + PlayerVehicle.bonusPlayerDamage);
		    	this.bulletMoveTimer.stop();
		    	Map.imagesToDraw.remove(PlayerRPG.this);
		    }
		}
		// if the bullet hit a certain distance from which the muzzle flash should be removed
		if (this.currY == this.startingYValue - 30) {
			this.muzzleFlashImage = null;
		}
		if (this.currY <= -20) {
			this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(PlayerRPG.this);
		}
	}
	
	public void start()
	{
		Map.imagesToDraw.add(PlayerRPG.this);
		this.bulletMoveTimer.start();
	}
	
}
