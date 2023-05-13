package enemies;

import java.awt.Color;

import javax.swing.JProgressBar;

import game.Map;

public class EnBiker extends EnemyVehicle {
	
	
	public EnBiker(Map parentPanel, int x, int y) 
	{
		super(parentPanel, 3, x, y);
		this.whichVehicle = 3;  // only need to change this between vehicles
		this.parentPanel = parentPanel;
		
		//                   horizontal, min health, max health
		this.healthBar = new JProgressBar(0, 0, 100);
		this.currentHealth = this.healthBar.getMaximum();
		this.healthBar.setValue(this.currentHealth);
		this.healthBar.setForeground(Color.red);
		this.healthBar.setBounds(x + 13, y - 20, this.currentImage.getWidth(null) - 30, 12);
		
		this.moneyValue = 300;
		this.scoreAdditionValue = 50;
	}

	@Override
	public void shoot() 
	{
		HostileBullet bullet = new HostileBullet(this.currX + 40, this.currY + 180, this.parentPanel);
		bullet.start();
	}
}
