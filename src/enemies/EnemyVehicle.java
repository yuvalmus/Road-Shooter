package enemies;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;

import game.DrawImageInterface;
import game.Explosion;
import game.GameScorePanel;
import game.Map;

public abstract class EnemyVehicle implements DrawImageInterface
{
	protected static Image[] vehiclesImages = null;
	protected static Image[] vehiclesHitImages = null;
	protected int currX, currY;
	protected Image currentImage;
	protected Map parentPanel;
	protected JProgressBar healthBar;
	protected int currentHealth;
	protected Thread hitMarkRedThread;
	protected Runnable hitMarkRed;
	protected int whichVehicle;
	protected boolean isAlive;
	protected int moneyValue;
	protected int scoreAdditionValue;
	
	
	protected EnemyVehicle(Map parentPanel, int whichVehicle, int x, int y)
	{
		this.parentPanel = parentPanel;
		this.currX = x;
		this.currY = y;
		
		if (vehiclesImages == null)
		{
			Image img;
			BufferedImage bfi;
			vehiclesImages = new Image[5];
			vehiclesHitImages = new Image[5];
			int i = 0;
			try {
				// 0 - boss
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/boss.png"));
				img = bfi.getScaledInstance(550, 660, Image.SCALE_SMOOTH);
				vehiclesImages[i] = new ImageIcon(img).getImage();
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/HIT/bossHIT.png"));
				img = bfi.getScaledInstance(550, 660, Image.SCALE_SMOOTH);
				vehiclesHitImages[i++] = new ImageIcon(img).getImage();
				
				// 1 - buggy
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/en_buggy.png"));
				img = bfi.getScaledInstance(104, 190, Image.SCALE_SMOOTH);
				vehiclesImages[i] = new ImageIcon(img).getImage();
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/HIT/en_buggyHIT.png"));
				img = bfi.getScaledInstance(104, 190, Image.SCALE_SMOOTH);
				vehiclesHitImages[i++] = new ImageIcon(img).getImage();
				
				
				// 2 - missile launcher jeep
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/missile_launcher_jeep.png"));
				img = bfi.getScaledInstance(136, 254, Image.SCALE_SMOOTH);
				vehiclesImages[i] = new ImageIcon(img).getImage();
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/HIT/missile_launcher_jeepHIT.png"));
				img = bfi.getScaledInstance(136, 254, Image.SCALE_SMOOTH);
				vehiclesHitImages[i++] = new ImageIcon(img).getImage();
				
				
				// 3 - motorcycle
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/en_biker.png"));
				img = bfi.getScaledInstance(100, 207, Image.SCALE_SMOOTH);
				vehiclesImages[i] = new ImageIcon(img).getImage();
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/HIT/en_bikerHIT.png"));
				img = bfi.getScaledInstance(100, 207, Image.SCALE_SMOOTH);
				vehiclesHitImages[i++] = new ImageIcon(img).getImage();
				
				
				// 4 - armored truck
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/en_armored_truck.png"));
				img = bfi.getScaledInstance(150, 303, Image.SCALE_SMOOTH);
				vehiclesImages[i] = new ImageIcon(img).getImage();
				bfi = ImageIO.read(new File("src/game/assets/enemyVehicles/HIT/en_armored_truckHIT.png"));
				img = bfi.getScaledInstance(150, 303, Image.SCALE_SMOOTH);
				vehiclesHitImages[i++] = new ImageIcon(img).getImage();
			} catch (IOException e) {}
		}	
		
		this.hitMarkRed = new Runnable() {
			public void run() 
			{
				EnemyVehicle.this.currentImage = EnemyVehicle.vehiclesHitImages[whichVehicle];
				try 
				{
					Thread.sleep(300);
				} catch (InterruptedException e) 
				{
					return;
				}
				EnemyVehicle.this.currentImage = EnemyVehicle.vehiclesImages[whichVehicle];
				EnemyVehicle.this.hitMarkRedThread = null;
			}
		};
		
		this.currentImage = EnemyVehicle.vehiclesImages[whichVehicle];
		this.hitMarkRedThread = null;
		this.isAlive = true;
	}

	public void addToPanel() 
	{
		this.parentPanel.add(this.healthBar);
		Map.imagesToDraw.add(this);
	}
	
	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(this.currentImage, this.currX, this.currY, null);
	}
	
	public boolean damageVehicleHealth(int amountOfDamage)
	{
		this.parentPanel.updateServerAboutEnemyVehicleHit(this.whichVehicle, this.currentHealth, amountOfDamage);
		this.currentHealth -= amountOfDamage;
		this.healthBar.setValue(this.currentHealth);
		runTurnCarRed();
		if (this.currentHealth <= 0)
		{
			this.isAlive = false;
			this.hitMarkRedThread.interrupt();
			// adds the money from the vehicle's death to the player
			this.parentPanel.hostileVehicleDied(this.moneyValue, this.currX, this.currY);
			// removes the enemy vehicle from the list of enemies
			Map.enemies.remove(this);
			// removes the enemy vehicle from the drawables
			Map.imagesToDraw.remove(this);
			// removes the healthbar of the enemy vehicle from the screen
			this.parentPanel.remove(this.healthBar);
			Explosion explosion = new Explosion(this.currX - 60, this.currY - 55);
			explosion.startThread();
			GameScorePanel.score += this.scoreAdditionValue;
			return true;
		}
		return false;
	}
	
	public abstract void shoot();
	
	protected void runTurnCarRed()
	{
		if (this.hitMarkRedThread != null)
		{
			this.hitMarkRedThread.interrupt();
		}
		this.hitMarkRedThread = new Thread(this.hitMarkRed);
		this.hitMarkRedThread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void moveVehicle(int newX, int newY)
	{
		this.currX = newX;
		this.currY = newY;
		this.healthBar.move(newX + 13, newY - 20);
	}
	
	public Rectangle getVehicleBounds()
	{
		return new Rectangle(this.currX, this.currY, this.currentImage.getWidth(null), this.currentImage.getHeight(null));
	}
	
	public boolean isVehicleAlive()
	{
		return this.isAlive;
	}
	
	public int getVehicleHealth() {
		return this.currentHealth;
	}
	
	
}
