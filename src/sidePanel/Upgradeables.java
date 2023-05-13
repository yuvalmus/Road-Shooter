package sidePanel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class Upgradeables 
{
	private static final int PROGRESSBAR_X = 1430;    // constant x axis value of the progress bar
	private static final int UPGRADE_IMAGE_X = 1775;
	private static final int KEYBOARD_KEY_IMAGE_X = 1657;
	private static final Font FONT = new Font("Arial", Font.PLAIN, 20);
	private SidePanel parentPanel;
	private int upgradeImageY;
	private int currentUpgradeAmount;
	private JLabel currentUpgradeAmountLabel;
	private JProgressBar upgradeBar;
	private String labelAddition;
	private int upgradePrice;
	private boolean isUpgradeAvailable;
	
	public Upgradeables(SidePanel parentPanel, int upgradeImageY, int whichUpgradeable)
	{
		this.parentPanel = parentPanel;
		this.currentUpgradeAmount = 0;
		this.upgradeImageY = upgradeImageY;
		this.upgradePrice = 1000;
		this.isUpgradeAvailable = false;
		
		this.currentUpgradeAmountLabel = new JLabel("Current: +0 ", SwingConstants.CENTER);
		this.currentUpgradeAmountLabel.setFont(FONT);
		this.currentUpgradeAmountLabel.setBounds(PROGRESSBAR_X + 20, upgradeImageY + 34, 269, 27);
		
		this.upgradeBar = new JProgressBar(0, 0, 100);
		this.upgradeBar.setValue(0);
		this.upgradeBar.setBounds(PROGRESSBAR_X, upgradeImageY + 12, 326, 12);
		if (whichUpgradeable == 0)
		{
			this.upgradeBar.setForeground(Color.red);
			this.currentUpgradeAmountLabel.setForeground(Color.red);
			this.labelAddition = "MAX HP";
		}
		else
		{
			this.upgradeBar.setForeground(Color.yellow);
			this.currentUpgradeAmountLabel.setForeground(Color.yellow);
			this.labelAddition = "DPS";
		}
		this.currentUpgradeAmountLabel.setText("(" + this.currentUpgradeAmountLabel.getText() + this.labelAddition + ")");
		
		
	}
	
	// function that tells the upgradeable that it is now available
	public void upgradeIsAvailable()
	{
		if (!this.isUpgradeAvailable && !isMaxUpgraded())
		{
			this.isUpgradeAvailable = true;
			// adds the upgrade image
			SidePanel.imagesToDraw.add(new SidePanelImg(UPGRADE_IMAGE_X, this.upgradeImageY, 4));
			// adds the 'Z' keyboard image
			if (this.labelAddition.equals("MAX HP"))
			{
				SidePanel.imagesToDraw.add(new SidePanelImg(KEYBOARD_KEY_IMAGE_X, this.upgradeImageY - 37, 5));
			}
			// adds the 'X' keyboard image
			else
			{
				SidePanel.imagesToDraw.add(new SidePanelImg(KEYBOARD_KEY_IMAGE_X, this.upgradeImageY - 37, 6));	
			}
			this.parentPanel.repaintSidePanel();
		}
	}
	
	public boolean upgrade() 
	{
		if (this.isUpgradeAvailable && this.upgradeBar.getValue() < 100)
		{
			//////////////
			this.upgradeBar.setValue(this.upgradeBar.getValue() + 20);
			if (this.labelAddition.equals("MAX HP"))
				this.currentUpgradeAmount += 100;
			else
				this.currentUpgradeAmount += 30;
			updateUpgradeLabel();
			//////////////
			removeUpgradeImages();
			this.parentPanel.subtractMoney(this.upgradePrice);
			this.parentPanel.repaintSidePanel();
			this.upgradePrice *= 1.2;
			return true;
		}
		return false;
	}
	
	public void removeUpgradeImages()
	{
		for (int i = SidePanel.imagesToDraw.size(); i >= 1; i--)
		{
			try
			{
				if (SidePanel.imagesToDraw.get(i) instanceof SidePanelImg)
				{
					SidePanelImg current = (SidePanelImg) SidePanel.imagesToDraw.get(i);
					// checks if the current image is this Upgradeable's upgrade image
					if (current.getCurrY() == this.upgradeImageY)
					{
						SidePanel.imagesToDraw.remove(i);
						SidePanel.imagesToDraw.remove(i);
						this.isUpgradeAvailable = false;
						return;
					}
				}
			}
			catch (IndexOutOfBoundsException e) {}
		}
	}
	
	public void addToPanel(SidePanel sidePanel) 
	{
		sidePanel.add(this.upgradeBar);
		sidePanel.add(this.currentUpgradeAmountLabel);
	}
	
	public int getUpgradePrice()
	{
		return this.upgradePrice;
	}
	
	public boolean isMaxUpgraded()
	{
		return this.upgradeBar.getValue() == 100;
	}
	
	private void updateUpgradeLabel()
	{
		this.currentUpgradeAmountLabel.setText("(Current: +" + this.currentUpgradeAmount + " " + this.labelAddition + ")");
	}

	
}
