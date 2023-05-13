package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import game.DrawImageInterface;
import game.Explosion;
import game.Map;

public class PlayerVehicle implements DrawImageInterface
{
	public static int bonusPlayerDamage = 0;
	protected Image vehicleImage;          // the image of the player vehicle
	//protected Icon vehicleHitImage;      // the image of the player's vehicle getting hit
	protected Map parentPanel;             // the map of the frame
	protected int whichVehicle;            // the int representing which player car is chosen
	protected JProgressBar healthBar;      // the health bar of the player's vehicle
	protected int currX, currY;
	protected int hitXOffset, hitYOffset;  // the x and y offset relative to the vehicle's image
	protected int hitWidth, hitHeight;     // the width and height of the vehicle's image (not including shadows)
	private int offsetBar;
	
	public PlayerVehicle(Map parentPanel, int whichVehicle)
	{
		this.parentPanel = parentPanel;
		this.whichVehicle = whichVehicle;
		this.offsetBar = 0;
		switch (whichVehicle)
		{
			case 0:
				// 0 - gtr
				this.vehicleImage = new ImageIcon("src/game/assets/playerVehicles/gtr.png").getImage();
//				bfi = ImageIO.read(new File("src/game/assets/playerVehicles/HIT/gtrHIT.png"));
//				img = bfi.getScaledInstance(550, 660, Image.SCALE_SMOOTH);
//				vehiclesHitImages[i++] = new ImageIcon(img);
				this.hitXOffset = 97;
				this.hitWidth = 81;
				this.hitYOffset = 20;
				this.hitHeight = 176;
				break;
			case 1:
				// 1 - supra
				this.vehicleImage = new ImageIcon("src/game/assets/playerVehicles/supra.png").getImage();
//				bfi = ImageIO.read(new File("src/game/assets/playerVehicles/HIT/supraHIT.png"));
//				img = bfi.getScaledInstance(104, 190, Image.SCALE_SMOOTH);
//				vehiclesHitImages[i++] = new ImageIcon(img);
				this.hitXOffset = 96;
				this.hitWidth = 83;
				this.hitYOffset = 21;
				this.hitHeight = 176;
				break;
			case 2:
				// 2 - gt3rs
				this.vehicleImage = new ImageIcon("src/game/assets/playerVehicles/gt3rs.png").getImage();
//				bfi = ImageIO.read(new File("src/game/assets/playerVehicles/HIT/gt3rsHIT.png"));
//				img = bfi.getScaledInstance(120, 216, Image.SCALE_SMOOTH);
//				vehiclesHitImages[i++] = new ImageIcon(img);
				this.hitXOffset = 97;
				this.hitWidth = 82;
				this.hitYOffset = 20;
				this.hitHeight = 174;
				break;
			case 3:
				// 3 - mercedes gt
				this.vehicleImage = new ImageIcon("src/game/assets/playerVehicles/mrcdsGt.png").getImage();
//				bfi = ImageIO.read(new File("src/game/assetsplayerVehicles/HIT/mrcdsGtHIT.png"));
//				img = bfi.getScaledInstance(120, 216, Image.SCALE_SMOOTH);
//				vehiclesHitImages[i++] = new ImageIcon(img);
				this.hitXOffset = 97;
				this.hitWidth = 79;
				this.hitYOffset = 20;
				this.hitHeight = 177;
				break;
		}
			
		this.whichVehicle = whichVehicle;
		this.parentPanel = parentPanel;
		
		this.currX = 534;
		this.currY = 685;
		
		//                   horizontal, min health, max health
		this.healthBar = new JProgressBar(0, 0, 100);
		this.healthBar.setValue(this.healthBar.getMaximum());
		this.healthBar.setForeground(Color.green);
		this.healthBar.setBounds(this.currX + 107, this.currY - 15, 60, 12);
	}
	
	public void addToPanel() 
	{
		this.parentPanel.add(this.healthBar);
		Map.imagesToDraw.add(this);
	}
	
	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(this.vehicleImage, this.currX, this.currY, null);
	}
	
	public void damageVehicleHealth(int amountOfDamage)
	{
		this.healthBar.setValue(this.healthBar.getValue() - amountOfDamage);
		//runTurnCarRed();
		if (this.healthBar.getValue() <= 0)
		{
			//this.hitMarkRedThread.interrupt();
			Map.imagesToDraw.remove(this);
			this.parentPanel.remove(this.healthBar);
			Explosion explosion = new Explosion(this.currX - 60, this.currY - 55);
			explosion.startThread();
			this.parentPanel.playerDied();
		}
	}

	public void moveVehicle(int x, int y) 
	{
		this.currX = x;
		this.currY = y;
		this.healthBar.setLocation(x + 107 + this.offsetBar, y - 15);
	}
	
	public void shoot(int whichWeapon, int x, int y)
	{
		switch(whichWeapon)
		{
		case 0:
			PlayerBullet bullet = new PlayerBullet(x + 170, y - 10);
			bullet.start();
			break;
		case 1:
			PlayerRPG rpgRocket = new PlayerRPG(x + 178, y - 40);
			rpgRocket.start();
			break;
		case 2:
			PlayerLaserBullet laserBullet = new PlayerLaserBullet(x + 170, y - 10);
			laserBullet.start();
			break;
		case 3:
			PlayerGrenadeLauncher grenade = new PlayerGrenadeLauncher(x + 178, y - 40);
			grenade.start();
			break;
		case 4:
			PlayerLaserBeam laser = new PlayerLaserBeam(x + 160, y - 950);
			laser.startThread();
			break;
		}
	}
	
	public void bonusHealth(int bonusAmount)
	{
		this.offsetBar -= 5;
		Rectangle bounds = this.healthBar.getBounds();
		this.healthBar.setMaximum(this.healthBar.getMaximum() + bonusAmount);
		this.healthBar.setValue(this.healthBar.getValue() + bonusAmount);
		//this.healthBar.setLocation(bounds.x - this.offsetBar, bounds.y);
		this.healthBar.setSize(bounds.width + 10, bounds.height);
	}
	
	public void refillHealth() {
		this.healthBar.setValue(this.healthBar.getMaximum());
	}
	
	public static void bonusDamage(int bonusAmount)
	{
		PlayerVehicle.bonusPlayerDamage += bonusAmount;
	}

	public Rectangle getBounds() 
	{
		return new Rectangle(this.currX, this.currY, this.vehicleImage.getWidth(null), this.vehicleImage.getHeight(null));
	}
	
	public Rectangle getHitBounds()
	{
		return new Rectangle(this.currX + hitXOffset, this.currY + hitYOffset, hitWidth, hitHeight);
	}

	public int getX() 
	{
		return this.currX;
	}
	
	public int getY() 
	{
		return this.currY;
	}

	public int getWidth() 
	{
		return this.vehicleImage.getWidth(null);
	}
	
	public int getHeight() 
	{
		return this.vehicleImage.getHeight(null);
	}

	public String getPosition() {
		return String.format("%d,%d", this.currX, this.currY);
	}
}
