package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import startScreen.StartingPanel;

public class WaitingForConnectionGif extends GifStuff {
	
	public static BufferedImage[] searchingFrames;
	public static BufferedImage[] foundTeammateFrames;
	private BufferedImage[] imagess;  // which frames to show currently
	private final static int milliseconds = 30;
	private boolean teammateFound;
	private StartingPanel parentPanel;
	
	public WaitingForConnectionGif(StartingPanel parentPanel) {
		super(360, 150);
		this.parentPanel = parentPanel;
		this.teammateFound = false;
		this.imagess = WaitingForConnectionGif.foundTeammateFrames;
		this.gifThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true)
				{
					try {
						Thread.sleep(WaitingForConnectionGif.milliseconds);
					} catch (InterruptedException e) {
						return;
					}
					currFrame++;
					// if reached the last frame
					if (currFrame == WaitingForConnectionGif.searchingFrames.length) {
						currFrame = 0;
						if (teammateFound) {
							imagess = foundTeammateFrames;
							break;
						}
					}
				}
				while (currFrame < WaitingForConnectionGif.foundTeammateFrames.length) {
					try {
						Thread.sleep(WaitingForConnectionGif.milliseconds);
					} catch (InterruptedException e) {
						return;
					}
					currFrame++;
				}
				currFrame--;
				parentPanel.startMultiplayerGame();
			}
		});
	}
	
	public void draw(Graphics g) {	
		g.drawImage(imagess[this.currFrame], this.x, this.y, null);
	}
	
	public void changeImageToFound() {
		this.teammateFound = true;
	}
}
