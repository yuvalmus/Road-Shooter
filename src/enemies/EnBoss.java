package enemies;

import java.awt.Color;
import javax.swing.JProgressBar;

import game.Map;

public class EnBoss extends EnemyVehicle
{
	
	public EnBoss(Map parentPanel, int x, int y) 
	{
		super(parentPanel, 0, x, y);
		this.whichVehicle = 0;  // change this between vehicles
		this.parentPanel = parentPanel;
		
		//                   horizontal, min health, max health
		this.healthBar = new JProgressBar(0, 0, 1000);
		this.currentHealth = this.healthBar.getMaximum();
		this.healthBar.setValue(this.currentHealth);
		this.healthBar.setForeground(Color.red);
		this.healthBar.setBounds(x + 13, y + 350, this.currentImage.getWidth(null) - 30, 12);
		
		this.scoreAdditionValue = 3000;
	}

	@Override
	public void shoot() 
	{
		
	}
	
}
