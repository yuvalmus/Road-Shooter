package game;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import levelsPackage.Level;
import levelsPackage.Level1;
import levelsPackage.Level2;
import levelsPackage.Level3;
import levelsPackage.Level4;
import levelsPackage.Level5;
import networking.Client;
import player.PlayerVehicle;
import sidePanel.RegularImg;
import sidePanel.SidePanel;
import startScreen.StartingPanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame
{
	public final static int panelWidth = 973, panelHeight = 900, ROAD_WIDTH = 1340, ROAD_HEIGHT = 1000;
	private static int powerUpProbability = 0;
	private Map map;
	private SidePanel sidePanel;
	private JPanel p;
	private StartingPanel startingPanel;
	private GameScorePanel scorePanel;
	private GameOver gameOverIcons;
	private int currentLevelNum;
	private Level currentLevel;
	private String cheatCode = "";
	private Client socket;
	
	public GameFrame()
	{
		super("Road Shooter");
		ImageIcon img = new ImageIcon("src/game/assets/logo.png");
		setIconImage(img.getImage());
		
		this.p = new JPanel();
		add(this.p);
		this.startingPanel = new StartingPanel(this);
		add(this.startingPanel);
		this.map = new Map(this);
		add(this.map);
		this.sidePanel = new SidePanel(this.map.getPlayerVehicle());
		add(this.sidePanel);
		this.scorePanel = new GameScorePanel();
		this.gameOverIcons = new GameOver();
		this.map.add(scorePanel);
		this.socket = null;
		
		p.setBounds(176, 0, GameFrame.panelWidth, GameFrame.panelHeight);
		p.setSize(GameFrame.panelWidth, GameFrame.panelHeight);
		p.setOpaque(false);
		
		this.currentLevelNum = 1;
		
		
		
		// smaller road width- 840
		// medium road width- 1090
		// large road width- 1340
		//setSize(1920, 1000);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        GameFrame.this.interruptAllThreads();
		        if (socket != null) {
		        	socket.sendMsgToServer("EXIT");
		        	socket.close();
		        }
		        System.exit(1);
		    }
		});
	}
	
	public void startGame() {
		setCursorTransparent();
		try {
			new Robot().mouseMove(665, 700);
		} catch (AWTException e) {}
		this.startingPanel.setVisible(false);
		this.map.startPaintingTimer();
		addListeners();
		startNextLevel();
		this.scorePanel.start();
		System.out.println("Starting...");
	}
	
	private void addListeners() {
		this.p.addMouseListener(new MouseAdapter() {
			
			// when the mouse is pressed in order to shoot
			public void mousePressed(MouseEvent e)
			{
				if (GameFrame.this.sidePanel.mousePressed(e.getX(), e.getY()) && socket != null) {
					socket.sendMsgToServer("S" + GameFrame.this.sidePanel.getCurrentWeapon() + e.getX() + ',' + e.getY());
				}
			}
		});
		this.p.addMouseMotionListener(new MouseMotionAdapter() {
			int x, y, mouseX, mouseY;
			Rectangle enemyRect, result;
			PlayerVehicle playerVehicle = GameFrame.this.map.getPlayerVehicle();
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = MouseInfo.getPointerInfo().getLocation().x;
				mouseY = MouseInfo.getPointerInfo().getLocation().y;
				x = (mouseX - 132) - playerVehicle.getX();
				y = (mouseY - 22) - playerVehicle.getY();
				playerVehicle.moveVehicle(playerVehicle.getX() + x,playerVehicle.getY() + y);
				if (socket != null) {							
					socket.sendMsgToServer("M" + map.getPlayerVehicle().getPosition());
				}
				// checks if the player intersected with one of the enemy vehicles
//				for (int i = 0; i < Map.enemies.size(); i++)
//				{
//					enemyRect = Map.enemies.get(i).getVehicleBounds();
//					result = SwingUtilities.computeIntersection(playerVehicle.getX() + 180, playerVehicle.getY(), playerVehicle.getWidth(), playerVehicle.getHeight(), enemyRect);
//					if (result.getWidth() > 0 && result.getHeight() > 0)
//					{
//						GameFrame.this.interruptAllThreads();
//					    return;
//					}
//				}
				
			}
		});
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if (Character.isAlphabetic(e.getKeyChar()))
				{
					GameFrame.this.sidePanel.keyPressed(e.getKeyChar());
					cheatCode += e.getKeyChar();
					if (cheatCode.length() == 5)
					{
						if (cheatCode.equals("mario"))
						{
							System.out.println("GODMODE ACTIVATED");
						}
						else if (cheatCode.equals("nammo"))
						{
							GameFrame.this.sidePanel.addAmmoToAll();
						}
						else if (cheatCode.equals("nheat"))
						{
							GameFrame.this.sidePanel.turnOverheatOff();
						}
						else if (cheatCode.equals("money")) {
							GameFrame.this.sidePanel.addMoney(10000);
						}
						cheatCode = "";
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					cheatCode = "";
					pauseGame();
				}
				else
				{
					GameFrame.this.sidePanel.keyPressed(e.getKeyChar());
				}
			}
		});
	}
	
	public void pauseGame() {
		this.scorePanel.stop();
		LinkedList<MouseListener> mouseListeners = new LinkedList<>();
		LinkedList<MouseMotionListener> mouseMotionListeners = new LinkedList<>();
		for (MouseListener listener: p.getMouseListeners())
		{
			mouseListeners.add(listener);
			p.removeMouseListener(listener);
		}
		for (MouseMotionListener listener: p.getMouseMotionListeners())
		{
			mouseMotionListeners.add(listener);
			p.removeMouseMotionListener(listener);
		}
		this.map.stopPaintingTimer();
		this.currentLevel.interruptLevel();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//________________________________________________________
		
		for (MouseListener listener: mouseListeners)
		{
			p.addMouseListener(listener);
		}
		for (MouseMotionListener listener: mouseMotionListeners)
		{
			p.addMouseMotionListener(listener);
		}
		this.map.startPaintingTimer();
		this.currentLevel.InitShootRandomly();
		this.currentLevel.InitEnemiesMovement(true);
		this.currentLevel.startLevel();
		this.scorePanel.start();
	}

	public void startNextLevel()
	{
		switch (this.currentLevelNum) {
		case 1:
			this.currentLevel = new Level1(this.map, this.socket == null);
			break;
		case 2:
			this.currentLevel = new Level2(this.map, this.socket == null);
			break;
		case 3: 
			this.currentLevel = new Level3(this.map, this.socket == null);
			break;
		case 4: 
			this.currentLevel = new Level4(this.map, this.socket == null);
			break;
		case 5: 
			this.currentLevel = new Level5(this.map, this.socket == null);
			break;
		case 6:
			this.currentLevel = new Level1(this.map, this.socket == null);
			this.currentLevelNum = 1;
			break;
		}
		this.currentLevelNum++;
	}
	
	private void setCursorTransparent() 
	{
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		this.map.getRootPane().setCursor(blankCursor);
	}
	
	private void setCursorVisible() 
	{
		this.map.getRootPane().setCursor(Cursor.getDefaultCursor());
	}
	
	public void playerDied() {
		interruptAllThreads();
		Map.imagesToDraw.add(this.gameOverIcons);
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) 
			{
				setCursorVisible();
				GameFrame.this.map.stopPaintingTimer();
				GameFrame.this.startingPanel.setVisible(true);
			}
		});
	}

	public void interruptAllThreads()
	{
		this.scorePanel.stop();
		for (MouseListener listener: p.getMouseListeners())
		{
			p.removeMouseListener(listener);
		}
		for (KeyListener listener: this.getKeyListeners())
		{
			this.removeKeyListener(listener);
		}
		if (this.currentLevel != null)
			this.currentLevel.interruptLevel();
		// this.map.interruptThread();
		LinkedList<DrawImageInterface> toRemove = new LinkedList<>();
		for (int i = 0; i < Map.imagesToDraw.size(); i++)
		{
			if (Map.imagesToDraw.get(i) instanceof Road)
				((Road)Map.imagesToDraw.get(i)).interruptThread();
			else if (Map.imagesToDraw.get(i) instanceof Bullet || Map.imagesToDraw.get(i) instanceof RegularImg)
				toRemove.add(Map.imagesToDraw.get(i));
		}
		while (!toRemove.isEmpty())
		{
			Map.imagesToDraw.remove(toRemove.remove(0));
		}
	}

	public void hostileVehicleDied(int moneyValue, int x, int y) 
	{
		this.sidePanel.addMoney(moneyValue);
		if (new Random().nextInt(100) < GameFrame.powerUpProbability) {
			new PowerUp(this, x, y, this.map.getPlayerVehicle()).startMoving();
			GameFrame.powerUpProbability = 0;
		} else {
			GameFrame.powerUpProbability += 80;
		}
	}
	
	public void powerUpGrabbed(int whichPowerUp) {
		// if the power up that was grabbed is a weapon
		if (whichPowerUp >= 1 && whichPowerUp <= 4) {
			this.sidePanel.discoverWeapon(whichPowerUp);
		}
		else {
			// heal
			if (whichPowerUp == 5) {
				this.map.getPlayerVehicle().refillHealth();
			}
		}
	}
	
	public void connectToServer() {
		if (this.socket == null)
			this.socket = new Client();
	}

	public void waitForMultiplayerConnection() {
		System.out.println("Waiting for another player to connect with me for a multiplayer game!");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				socket.sendMsgToServer("MULTIPLAYER");
				String msgBack = socket.recvMsgFromServer();
				System.out.println(msgBack);
				String connectionFoundMsg = socket.recvMsgFromServer();
				System.out.println("A teammate was found!");
				startingPanel.teammateFound();
			}
		}).start();
		
	}
	
	public void startReceiveingPositions() {
		PlayerVehicle friend = new PlayerVehicle(this.map, 3);
		friend.addToPanel();
		new Thread(new Runnable() {
			
			public void run() {
				String data;
				String[] XYarr = new String[2];
				while (true) {
					data = socket.recvMsgFromServer();
					// System.out.println("received => " + data);
					if (data.startsWith("M")) {
						XYarr = data.substring(1).split(",");
						friend.moveVehicle(Integer.valueOf(XYarr[0]), Integer.valueOf(XYarr[1]));
					} else if (data.startsWith("S")) {
						XYarr = data.substring(2).split(",");
						friend.shoot(data.charAt(1) - '0', Integer.valueOf(XYarr[0]), Integer.valueOf(XYarr[1]));
					} else if (data.startsWith("ES")) {
						int whichEnemy = Integer.valueOf(data.substring(2));
						Map.enemies.get(whichEnemy).shoot();
					}
				}
			}
		}).start();
	}
	
	public void tellServerImReady() {
		this.socket.sendMsgToServer("READY");
		this.socket.recvMsgFromServer();
	}
	
	public void updateServerAboutEnemyVehicleHit(int whichEnemyVehicle, int previousVehicleHealth, int amountOfDamage) {
		if (this.socket != null) {
			this.socket.sendMsgToServer("H" + whichEnemyVehicle + '-' + previousVehicleHealth + '-' + amountOfDamage);
		}
	}
	
	public void updateServerEnemiesInScreen() {
		this.socket.sendMsgToServer("U");
	}

}
