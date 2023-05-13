package sidePanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import game.GifStuff;

public class WeaponTracker extends GifStuff
{
	private static final int PROGRESSBAR_X = 1521;                            // constant x axis value of the progress bar
	private static final int AMMO_X = 1626;                                   // constant x axis value of the ammo label
	private static final Font AMMO_FONT = new Font("Arial", Font.PLAIN, 25);  // constant font of the ammo label
	private static final int milliseconds = 10;                               // constant millisecond sleeping time
	public static BufferedImage[] loadingGifFrames;   // frames of the loading gun gif
	private SidePanel parentPanel;                    // the parent panel (the side panel)
	private int currFrame;                            // the current frame among the frames of the gif
	private SidePanelImg blackRect;          		  // the black rectangle that hides the weapon if not yet discovered
	private JProgressBar overheatBar;        		  // the overheat progress bar
	private int amountOfOverheat;            		  // the amount of overheat that every shot of the weapon adds to the bar
	private JLabel ammoLabel;                		  // the ammo label
	private int magazineSize;                		  // the overall magazine size of the weapon
	private int bulletsAvailable;            		  // the amount of bullets currently available to this weapon
	private int bulletsCurrentlyInMagazine;  		  // the CURRENT amount of bullets in the weapon
	private Thread runOverheatTowardsZero;   		  // the thread the runs the overheat progress bar to zero
	private boolean isFiringAllowed;         		  // boolean indicator to if the weapon is currently able to shoot
	private boolean isSelectionAllowed;      		  // boolean indicator to if the weapon is currently able to be selected by the user (if he discovered it)
	private SidePanelImg noAmmoImage;
	
	
	public WeaponTracker(SidePanel parentPanel, int whichWeapon, int y, int amountToOverheat, int bulletsAvailable)
	{
		// the x and y coordinates of the loading gif
		super(1414, y - 30);
		
		this.parentPanel = parentPanel;
		this.currFrame = 0;
		// creating the overheat bar
		this.overheatBar = new JProgressBar(0, 0, 100);
		this.overheatBar.setValue(0);
		this.overheatBar.setForeground(Color.blue);
		this.overheatBar.setBounds(PROGRESSBAR_X, y, 326, 12);
		this.amountOfOverheat = amountToOverheat;
		// only true for the DEFAULT weapon
		if (whichWeapon == 0) {
			this.isSelectionAllowed = true;
			this.blackRect = null;
		}
		else {
			// make this weapon unselectable, its overheat and ammo not showing and a black rectangle on its image
			this.isSelectionAllowed = false;
			this.overheatBar.setVisible(false);
			this.blackRect = new SidePanelImg(1424, 430 + whichWeapon * 105, 2);
			SidePanel.imagesToDraw.add(this.blackRect);
		}
		
		// creating the ammo label and setting its text
		this.ammoLabel = new JLabel("", SwingConstants.CENTER);
		this.ammoLabel.setFont(AMMO_FONT);
		this.ammoLabel.setBounds(AMMO_X, y - 35, 110, 33);
		// unlimited bullets
		if (bulletsAvailable == -1)
		{
			this.magazineSize = 30;
			this.bulletsAvailable = -1;
			this.bulletsCurrentlyInMagazine = 30;
			updateAmmoLabelText();
		}
		else
		{
			this.magazineSize = bulletsAvailable;
			this.bulletsAvailable = bulletsAvailable;
			this.bulletsCurrentlyInMagazine = bulletsAvailable;
		}
		initGifThread();
		this.noAmmoImage = null;
		this.isFiringAllowed = true;
		initRunOverheatTowardsZero();
	}

	@Override
	public void draw(Graphics g) 
	{
		g.drawImage(loadingGifFrames[this.currFrame], 1414, this.y, null);
	}
	
	public void startOverheatingZeroing()
	{
		try
		{
			this.runOverheatTowardsZero.start();
		}
		catch (IllegalThreadStateException e) {}
	}
	
	public void addOverheating()
	{
		this.overheatBar.setValue(this.overheatBar.getValue() + this.amountOfOverheat);
	}

	public void addToPanel(SidePanel sidePanel) 
	{
		sidePanel.add(this.overheatBar);
		sidePanel.add(this.ammoLabel);
	}

	public boolean isFiringAllowed() 
	{
		return this.isFiringAllowed && this.bulletsCurrentlyInMagazine > 0;
	}

	public void bulletOut() 
	{
		this.bulletsCurrentlyInMagazine--;
		updateAmmoLabelText();
		// if the current magazine is empty
		if (this.bulletsCurrentlyInMagazine == 0)
		{
			reloadWeapon();
		}
		// if the current magazine is NOT empty
		else
		{
			updateAmmoLabelText();
		}
	}
	
	public void reloadWeapon()
	{
		// if the weapon is supposed to have UNLIMITED ammo
		if (this.bulletsAvailable == -1) {
			this.bulletsCurrentlyInMagazine = this.magazineSize;
			startThread();
		}
		// if the weapon still has more bullets to reload
		else if (this.bulletsAvailable != 0)
		{
			// if the current magazine is NOT empty (the player reloaded before it is emptied)
			if (this.bulletsCurrentlyInMagazine != 0)
			{
				// if the available bullets are more than or equal to the magazine size
				if (this.bulletsAvailable >= this.magazineSize)
				{
					this.bulletsAvailable -= this.magazineSize - this.bulletsCurrentlyInMagazine;
					this.bulletsCurrentlyInMagazine = this.magazineSize;
				}
				// if the available bullets are LESS than the magazine size
				else
				{
					this.bulletsCurrentlyInMagazine += this.bulletsAvailable;
					// if the bullets currently in magazine is HIGHER the magazine size after the addition (and the available bullets is surely LESS than the magazine size)
					if (this.bulletsCurrentlyInMagazine > this.magazineSize)
					{
						this.bulletsAvailable = this.bulletsCurrentlyInMagazine - this.magazineSize;
						this.bulletsCurrentlyInMagazine = this.magazineSize;
					}
					// if the bullets currently in magazine is LOWER than the magazine size after the addition
					else
					{
						this.bulletsAvailable = 0;
					}
				}
			}
			// if the current magazine is completely empty
			else
			{
				// if the bullets available are still more than the magazine size
				if (this.bulletsAvailable > this.magazineSize)
				{
					this.bulletsCurrentlyInMagazine = this.magazineSize;
					this.bulletsAvailable -= this.magazineSize;
				}
				// if the bullets available are less than or equal to the magazine size
				else
				{
					this.bulletsCurrentlyInMagazine = this.bulletsAvailable;
					this.bulletsAvailable = 0;
				}
			}
			startThread();
		}
		// if the weapon has no bullets to reload
		else
		{
			if (this.bulletsCurrentlyInMagazine == 0)
			{
				this.noAmmoImage = new SidePanelImg(this.x + 306, this.y - 20, 3);
				SidePanel.imagesToDraw.add(this.noAmmoImage);
				this.parentPanel.repaintSidePanel();
			}
		}
	}
	
	private void updateAmmoLabelText() 
	{
		String result;
		// unlimited bullets
		if (bulletsAvailable == -1)
		{
			result = " / INF";
		}
		else
		{
			result = " / " + this.bulletsAvailable;
		}
		result = this.bulletsCurrentlyInMagazine + result;
		this.ammoLabel.setText(result);
	}
	
	private void initRunOverheatTowardsZero()
	{
		this.runOverheatTowardsZero = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				while (overheatBar.getValue() > 0)
				{
					if (overheatBar.getValue() >= 100)
					{
						overheatBar.setValue(100);
						overheatBar.setBackground(Color.red);
						isFiringAllowed = false;
					}
					overheatBar.setValue(overheatBar.getValue() - 1);
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				overheatBar.setBackground(Color.white);
				if (!WeaponTracker.this.gifThread.isAlive())
					isFiringAllowed = true;
				initRunOverheatTowardsZero();
			}
		});
	}
	
	private void initGifThread() 
	{
		this.gifThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				parentPanel.startPaintTimer(WeaponTracker.this);
				SidePanel.imagesToDraw.add(WeaponTracker.this);
				isFiringAllowed = false;
				while (currFrame < WeaponTracker.loadingGifFrames.length)
				{
					try {
						Thread.sleep(WeaponTracker.milliseconds);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}
					currFrame++;
				}
				currFrame = 0;
				updateAmmoLabelText();
				SidePanel.imagesToDraw.remove(WeaponTracker.this);
				parentPanel.stopPaintTimer(WeaponTracker.this);
				initGifThread();
				if (WeaponTracker.this.overheatBar.getBackground() != Color.red)
					isFiringAllowed = true;
			}
		});
	}

	public void addAmmo() 
	{
		if (this.bulletsAvailable == 0)
		{
			SidePanel.imagesToDraw.remove(this.noAmmoImage);
			this.noAmmoImage = null;
			this.parentPanel.repaintSidePanel();
		}
		this.bulletsAvailable += this.magazineSize;
		updateAmmoLabelText();
	}
	
	public void discoverWeapon() {
		this.isSelectionAllowed = true;
		updateAmmoLabelText();
		this.overheatBar.setVisible(true);
		SidePanel.imagesToDraw.remove(this.blackRect);
		this.parentPanel.repaintSidePanel();
	}
	public boolean isSelectable() {
		return this.isSelectionAllowed;
	}
}
