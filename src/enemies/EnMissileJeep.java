package enemies;

import java.awt.Color;
import javax.swing.JProgressBar;

import game.Map;

public class EnMissileJeep extends EnemyVehicle
{
	
	public EnMissileJeep(Map parentPanel, int x, int y) 
	{
		super(parentPanel, 2, x, y);
		this.whichVehicle = 2;  // only need to change this between vehicles
		this.parentPanel = parentPanel;
		
		//                   horizontal, min health, max health
		this.healthBar = new JProgressBar(0, 0, 500);
		this.currentHealth = this.healthBar.getMaximum();
		this.healthBar.setValue(this.currentHealth);
		this.healthBar.setForeground(Color.red);
		this.healthBar.setBounds(x + 13, y - 20, this.currentImage.getWidth(null) - 30, 12);
		
		this.moneyValue = 500;
		this.scoreAdditionValue = 250;
	}

	@Override
	public void shoot() 
	{
		HostileBullet bullet = new HostileBullet(this.currX + 33, this.currY + 135, this.parentPanel);
		bullet.start();
	}
	
}
