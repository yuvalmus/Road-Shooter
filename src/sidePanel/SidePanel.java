package sidePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import game.DrawImageInterface;
import player.PlayerVehicle;

@SuppressWarnings("serial")
public class SidePanel extends JPanel
{
	public static LinkedList<DrawImageInterface> imagesToDraw;
	private PlayerVehicle player;                   // the player's vehicle
	private SidePanelImg selectionImage;              // the green rectangle to show the player which weapon is currently in use
	private int balance;                            // the balance value
	private JLabel balanceLabel;                    // the label that shows the balance of the player  
	private LinkedList<WeaponTracker> trackers;     // all the weapon trackers (for each weapon)
	private LinkedList<Upgradeables> upgradeables;  // all the upgradeables trackers
	private int currentWeapon;                      // used to keep track of which weapon is currently in use
	private Timer runPaintingTimer;                 // the timer that starts painting the side panel every 1 millisecond
	private WeaponTracker masterTimerCaller;        // saves the last object that started the timer because only the last one is able to stop it
	private boolean isOverheatOn;
	
	
	public SidePanel(PlayerVehicle player)
	{
		super();
		this.player = player;
		this.currentWeapon = 0;
		imagesToDraw = new LinkedList<>();
		// adds the side panel image to the screen
		imagesToDraw.add(new SidePanelImg(1340, 0, 0));
		
		// the balance label that shows the money of the player
		this.balance = 0;
		this.balanceLabel = new JLabel("0$", SwingConstants.CENTER);
		this.balanceLabel.setFont(new Font("Arial", Font.PLAIN, 60));
		this.balanceLabel.setBounds(1405, 79, 372, 78);
		this.balanceLabel.setForeground(new Color(139, 215, 104));
		add(this.balanceLabel);
		
		// the green selection rectangle
		this.selectionImage = new SidePanelImg(1424, 430, 1);
		imagesToDraw.add(this.selectionImage);
		
		// the overheat progress trackers of the weapons
		this.trackers = new LinkedList<>();
		// the overheat amount to add (for each of the weapons)
		int [] amountsOfOverheat = { 20, 50, 30, 40, 80 };
		int [] amountsOfBullets = { -1, 10, 25, 15, 10 }; // -1 means unlimited bullets
		WeaponTracker newBar;
		for (int i = 0; i < 5; i++)
		{
			newBar = new WeaponTracker(this, i, 460 + i * 104, amountsOfOverheat[i], amountsOfBullets[i]);
			this.trackers.add(newBar);
			newBar.addToPanel(this);
		}
		this.isOverheatOn = true;
		
		this.upgradeables = new LinkedList<>();
		for (int i = 0; i < 2; i++) 
		{
			this.upgradeables.add(new Upgradeables(this, 206 + i * 136, i));
			this.upgradeables.get(i).addToPanel(this);
		}
		
		initTimer();
		this.masterTimerCaller = null;
		setBackground(new Color(102, 102, 102));
		setLayout(null);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//--
		for (int i = 0; i < imagesToDraw.size(); i++) 
		{
			imagesToDraw.get(i).draw(g);
		}
		//--
	}
	
	public boolean isFiringAllowed()
	{
		return this.trackers.get(this.currentWeapon).isFiringAllowed();
	}
	
	// returns a true or false to the GameFrame, standing for whether the shot was allowed or not
	public boolean mousePressed(int x, int y)
	{
		if (this.trackers.get(this.currentWeapon).isFiringAllowed())
		{
			this.trackers.get(this.currentWeapon).bulletOut();
			if (this.isOverheatOn)
			{
				this.trackers.get(this.currentWeapon).addOverheating();
				this.trackers.get(this.currentWeapon).startOverheatingZeroing();
			}
			this.player.shoot(this.currentWeapon, x, y);
			return true;
		}
		return false;
	}
	
	public int getCurrentWeapon() {
		return this.currentWeapon;
	}
	
	public void keyPressed(char keyChar) 
	{
		if (Character.isDigit(keyChar) && keyChar - '0' <= 5)
		{
			int index = keyChar - '0' - 1;
			if (this.trackers.get(index).isSelectable()) {				
				this.currentWeapon = index;
				this.selectionImage.moveTo(-1, 430 + index * 105);
				repaintSidePanel();
			}
		}
		else if (Character.isAlphabetic(keyChar))
		{
			// player pressed 'r' in order to reload
			if (keyChar == 'r')
				reloadWeapon();
			// player pressed 'z' in order to upgrade health
			else if (keyChar == 'z')
			{
				if (this.upgradeables.get(0).upgrade())
				{
					if (this.upgradeables.get(0).getUpgradePrice() < this.balance)
						this.upgradeables.get(0).upgradeIsAvailable();
					if (this.upgradeables.get(1).getUpgradePrice() > this.balance)
					{
						this.upgradeables.get(1).removeUpgradeImages();
					}
					this.player.bonusHealth(100);
				}
			}
			// player pressed 'x' in order to upgrade damage
			else if (keyChar == 'x')
			{
				if (this.upgradeables.get(1).upgrade())
				{
					if (this.upgradeables.get(1).getUpgradePrice() < this.balance)
						this.upgradeables.get(1).upgradeIsAvailable();
					if (this.upgradeables.get(0).getUpgradePrice() > this.balance)
					{
						this.upgradeables.get(0).removeUpgradeImages();
					}
					PlayerVehicle.bonusDamage(30);
				}
			}
		}
	}
	
	public void startPaintTimer(WeaponTracker caller)
	{
		this.masterTimerCaller = caller;
		this.runPaintingTimer.start();
	}
	
	public void stopPaintTimer(WeaponTracker caller)
	{
		if (caller == this.masterTimerCaller)
		{
			this.runPaintingTimer.stop();
			this.masterTimerCaller = null;
		}
	}
	
	public void repaintSidePanel()
	{
		repaint(1340, 0, 580, 1080);
	}
	
	public void reloadWeapon()
	{
		this.trackers.get(this.currentWeapon).reloadWeapon();
	}
	
	public void addMoney(int amountToAdd)
	{
		this.balance += amountToAdd;
		updateBalanceLabel();
		for (int i = 0; i < this.upgradeables.size(); i++) 
		{
			if (this.balance >= this.upgradeables.get(i).getUpgradePrice())
			{
				this.upgradeables.get(i).upgradeIsAvailable();
			}
		}
	}
	
	public void subtractMoney(int amountToSubtract)
	{
		this.balance -= amountToSubtract;
		updateBalanceLabel();
	}
	
	private void updateBalanceLabel()
	{
		this.balanceLabel.setText(this.balance + "$");
	}
	
	public void addAmmoToAll()
	{
		for (int i = 0; i < this.trackers.size(); i++) 
		{
			this.trackers.get(i).addAmmo();
		}
	}
	
	// activate by cheat code 'nheat'
	public void turnOverheatOff()
	{
		this.isOverheatOn = false;
	}
	
	private void initTimer()
	{
		this.runPaintingTimer = new Timer(1, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				repaintSidePanel();
			}
		});
	}
	
	public void discoverWeapon(int whichWeapon) {
		if (this.trackers.get(whichWeapon).isSelectable()) {
			this.trackers.get(whichWeapon).addAmmo();
		} else {			
			this.trackers.get(whichWeapon).discoverWeapon();
		}
	}
}
