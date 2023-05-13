package enemies;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.Bullet;
import game.Map;
import player.PlayerBullet;
import player.PlayerVehicle;

public class HostileBullet extends Bullet
{
	private Map parentPanel;
	private static int bulletDamage = 20;
	private int startingYValue;
	private Rectangle result, playerRect;
	private PlayerVehicle player;
	
	public HostileBullet(int x, int y, Map parentPanel) 
	{
		super(x, y, -22, 0);
		this.parentPanel = parentPanel;
		this.bulletImage = new ImageIcon("src/game/assets/hostile_bullet.png").getImage();
		this.player = this.parentPanel.getPlayerVehicle();
		this.startingYValue = y;
		this.bulletMoveTimer = new Timer(5, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currY += 1;
		playerRect = player.getHitBounds();
	    result = SwingUtilities.computeIntersection(currX, currY, HostileBullet.this.bulletImage.getWidth(null), HostileBullet.this.bulletImage.getHeight(null), playerRect);
	    if (result.getWidth() > 0 && result.getHeight() > 0)
	    {
	    	player.damageVehicleHealth(HostileBullet.bulletDamage);
	    	this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(this);
	    	return;
	    }
		// if the bullet hit a certain distance from which the muzzle flash should be removed
		if (this.currY == this.startingYValue + 20) {
			this.muzzleFlashImage = null;
		}
		if (this.currY >= 1000) {
			this.bulletMoveTimer.stop();
	    	Map.imagesToDraw.remove(HostileBullet.this);
		}
	}
	
	public void start()
	{
		Map.imagesToDraw.add(HostileBullet.this);
		this.bulletMoveTimer.start();
	}
	
}
