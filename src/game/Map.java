package game;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;
import enemies.EnemyVehicle;
import player.PlayerLaserBeam;
import player.PlayerVehicle;
import sidePanel.WeaponTracker;

/*
 *                       for scaled
 *                       
 	BufferedImage chainsPicture = ImageIO.read(new File(this.imageFolderPath + "chains.png"));
	Image tmp2 = chainsPicture.getScaledInstance(220, 220, Image.SCALE_SMOOTH);
	this.winningChains.setIcon(new ImageIcon(tmp2));
	
	---------------------------------------------------------------------
	
	Icon trophyPicture = new ImageIcon(this.imageFolderPath + "winnerTrophy.png");
	this.winningTrophy = new JLabel(trophyPicture);
 */

@SuppressWarnings("serial")
public class Map extends JPanel
{
	public static LinkedList<EnemyVehicle> enemies;
	public static LinkedList<DrawImageInterface> imagesToDraw;
	private final int amountOfRoads = 3;
	private GameFrame parentFrame;
	private PlayerVehicle car;
	private Timer paintTimer;
	
	public Map(GameFrame parentFrame)
	{
		super();
		WaitingForConnectionGif.searchingFrames = GifStuff.loadAllFrames("src/game/assets/startingScreen/SearchingLoadingScreen.gif");
		WaitingForConnectionGif.foundTeammateFrames = GifStuff.loadAllImages("src/game/assets/startingScreen/foundTeammateFramesLower");
		// initializes explosion frames
		Explosion.imagess = GifStuff.loadAllFrames("src/game/assets/explosion.gif");
		// initializes laser beam frames
		PlayerLaserBeam.imagess = GifStuff.loadAllFrames("src/game/assets/laserBeam.gif");
		// initializes weapon load frames
		WeaponTracker.loadingGifFrames = GifStuff.loadAllFrames("src/game/assets/loadingWeapon.gif");
		// initializes grenade launcher explosion frames
		GrenadeExplosion.imagess = GifStuff.loadAllFrames("src/game/assets/explosion3.gif");
		
		
		this.parentFrame = parentFrame;
		
		// initializes the list of images to draw
		Map.imagesToDraw = new LinkedList<>();
		
		// adding the roads' images to the list
		for (int i = 0; i < amountOfRoads; i++) 
		{
			imagesToDraw.add(new Road(0, -755 + 755 * i));
		}
		
		// adding the player's vehicle (integer represents the type of the vehicle)
		this.car = new PlayerVehicle(this, 3);
		this.car.addToPanel();
		
		// initializes the list of the enemy vehicles
		enemies = new LinkedList<EnemyVehicle>();
		
		// the timer of the paint action
		this.paintTimer = new Timer(1, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		
		setLayout(null);
		setSize(GameFrame.ROAD_WIDTH, GameFrame.ROAD_HEIGHT);
		setVisible(true);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//--
		for (int i = 0; i < imagesToDraw.size(); i++) 
		{
			try
			{
				imagesToDraw.get(i).draw(g);
			}
			catch (NullPointerException e)
			{}
		}
		//--
	}

	public PlayerVehicle getPlayerVehicle() 
	{
		return this.car;
	}
	
	// called from the player himself to say that it has no health
	public void playerDied()
	{
		this.parentFrame.playerDied();
	}

	public void hostileVehicleDied(int moneyValue, int x, int y) 
	{
		this.parentFrame.hostileVehicleDied(moneyValue, x, y);
	}

	public void startNextLevel() {
		this.parentFrame.startNextLevel();
	}
	
	public void startPaintingTimer() {
		this.paintTimer.start();
		for (int i = 0; i < this.amountOfRoads; i++) {
			((Road)(Map.imagesToDraw.get(i))).startThread();
		}
	}
	
	public void stopPaintingTimer() {
		this.paintTimer.stop();
		for (int i = 0; i < this.amountOfRoads; i++) {
			((Road)(Map.imagesToDraw.get(i))).interruptThread();
		}
	}
	
	public void updateServerAboutEnemyVehicleHit(int whichEnemyVehicle, int previousVehicleHealth, int amountOfDamage) {
		this.parentFrame.updateServerAboutEnemyVehicleHit(whichEnemyVehicle, previousVehicleHealth, amountOfDamage);
	}
	
	public void updateServerEnemiesInScreen() {
		this.parentFrame.updateServerEnemiesInScreen();
	}

}
