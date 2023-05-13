package startScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import game.GameFrame;
import game.GifStuff;
import game.WaitingForConnectionGif;

@SuppressWarnings("serial")
public class StartingPanel extends JPanel {
	
	private final static int ROAD_WIDTH = 1920, ROAD_HEIGHT = 1080, OPACITY_PERCENTAGE = 80;
	private final static Rectangle SINGLE_PLAYER_RECT = new Rectangle((int)(ROAD_WIDTH * 0.5 - 335), 400, 612, 106);
	private final static Rectangle MULTIPLAYER_RECT = new Rectangle((int)(ROAD_WIDTH * 0.5 - 335), 550, 612, 106);
	private JPanel backgroundPanel;
	private JLabel selection;
	private JLabel singlePlayer, multiplayer;
	private Timer drawingTimer;
	private WaitingForConnectionGif waitingScreen;
	private GameFrame mainFrame;
	
	public StartingPanel(GameFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		Icon selectionIcon = new ImageIcon("src/game/assets/startingScreen/selectionBorder.png");
		this.selection = new JLabel(selectionIcon);
		this.selection.setBounds(SINGLE_PLAYER_RECT);
		this.selection.setVisible(false);
		selection.setOpaque(false);
		add(this.selection);
		
		// ---------------------------------------------------------------------------------------------
		Icon singlePlayerIcon = new ImageIcon("src/game/assets/startingScreen/btnSinglePlayer.png");
		this.singlePlayer = new JLabel(singlePlayerIcon);
		this.singlePlayer.setBounds(SINGLE_PLAYER_RECT);
		this.singlePlayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				StartingPanel.this.selection.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				StartingPanel.this.selection.setVisible(true);
				StartingPanel.this.selection.setBounds(SINGLE_PLAYER_RECT);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.startGame();
			}
		});
		add(this.singlePlayer);
		// ---------------------------------------------------------------------------------------------
		Icon multiplayerIcon = new ImageIcon("src/game/assets/startingScreen/btnMultiplayer.png");
		this.multiplayer = new JLabel(multiplayerIcon);
		this.multiplayer.setBounds(MULTIPLAYER_RECT);
		this.multiplayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				StartingPanel.this.selection.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				StartingPanel.this.selection.setVisible(true);
				StartingPanel.this.selection.setBounds(MULTIPLAYER_RECT);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.connectToServer();
				singlePlayer.setVisible(false);
				multiplayer.setVisible(false);
				selection.setVisible(false);
				backgroundPanel.setVisible(false);
				waitingScreen = new WaitingForConnectionGif(StartingPanel.this);
				waitingScreen.startThread();
				drawingTimer.start();
				mainFrame.waitForMultiplayerConnection();
			}
		});
		add(this.multiplayer);
		//----------------------------------------------------------------------------------------------
		
		
		this.backgroundPanel = new JPanel();
		this.backgroundPanel.setSize(ROAD_WIDTH, ROAD_HEIGHT);
		this.backgroundPanel.setBackground(new Color(0, 0, 0, 255 * OPACITY_PERCENTAGE / 100));
		add(this.backgroundPanel);
		
		this.waitingScreen = null;
		this.drawingTimer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		this.drawingTimer.start();
		
		setLayout(null);
		setBounds(0, 0, ROAD_WIDTH, ROAD_HEIGHT);
		setSize(ROAD_WIDTH, ROAD_HEIGHT);
		setVisible(true);
	}
	
	public void showStartScreenAgain() {
		this.setVisible(true);
		this.drawingTimer.start();
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		//--
		if (this.waitingScreen != null) {
			this.waitingScreen.draw(g);
		}
		//--
	}
	
	public void startMultiplayerGame() {
		this.mainFrame.tellServerImReady();
		this.drawingTimer.stop();
		this.mainFrame.startReceiveingPositions();
		this.mainFrame.startGame();
	}

	public void teammateFound() {
		this.waitingScreen.changeImageToFound();
	}
}
