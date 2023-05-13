package game;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import player.PlayerVehicle;
import sidePanel.RegularImg;

public class PowerUp implements ActionListener
{
	private final static int Y_ADDITION = 1;
	private GameFrame parentFrame;
	private int whichPowerUp;
	private String powerUpName;
	private RegularImg powerUpImage;
	private PlayerVehicle player;
	private Timer powerupMoveTimer;
	private int x, y;
	
	public PowerUp(GameFrame parentFrame, int x, int y, PlayerVehicle player) {
		this.parentFrame = parentFrame;
		this.x = x;
		this.y = y;
		this.whichPowerUp = chooseRandomPowerUp();
		this.powerUpImage = new RegularImg(x, y, "src/game/assets/powerUps/" + this.powerUpName + ".png");
		this.player = player;
		Map.imagesToDraw.add(powerUpImage);
		this.powerupMoveTimer = new Timer(4, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		powerUpImage.moveTo(this.x, this.y + PowerUp.Y_ADDITION);
		this.y += PowerUp.Y_ADDITION;
		if (this.checkIntersection(player.getHitBounds())) {
			this.parentFrame.powerUpGrabbed(PowerUp.this.whichPowerUp);
			this.powerupMoveTimer.stop();
			Map.imagesToDraw.remove(powerUpImage);
		}
		if (this.y >= 1000) {
			this.powerupMoveTimer.stop();
			Map.imagesToDraw.remove(powerUpImage);
		}
	}
	
	public void stop() {
		this.powerupMoveTimer.stop();
	}
	
	public void startMoving() {
		this.powerupMoveTimer.start();
	}
	
	public int chooseRandomPowerUp() {
		int random = new Random().nextInt(100);
		if (isBetween(50, 100, random)) {
			// not a weapon
			this.powerUpName = "heal";
			return 5;
		} else if (isBetween(22, 50, random)) {
			// RPG
			this.powerUpName = "rpg";
			return 1;
		} else if (isBetween(10, 22, random)) {
			// laser gun
			this.powerUpName = "laser";
			return 2;
		} else if (isBetween(2, 10, random)) {
			// grenade launcher
			this.powerUpName = "grenadeLauncher";
			return 3;
		} else {
			// laser beam
			this.powerUpName = "laserBeam";
			return 4;
		}
	}
	
	private static boolean isBetween(int min, int max, int num) {
		return num >= min && num < max;
	}
	
	public boolean checkIntersection(Rectangle player) {
	    Rectangle result = SwingUtilities.computeIntersection(player.x, player.y, player.width, player.height, new Rectangle(this.x, this.y, 50, 50));
	    return result.getWidth() > 0 && result.getHeight() > 0;
	}

	
}
