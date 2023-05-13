package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameOver implements DrawImageInterface {
	
	private Image gameOverImage;
	private Image pressAnyKepImage;
	
	public GameOver() {
		this.gameOverImage = new ImageIcon("src/game/assets/gameOver.png").getImage();
		BufferedImage bfi = null;
		try {
			bfi = ImageIO.read(new File("src/game/assets/pressAnyKey.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Image img = bfi.getScaledInstance(904, 70, Image.SCALE_SMOOTH);
		this.pressAnyKepImage = new ImageIcon(img).getImage();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(this.gameOverImage, 354, 350, null);
		g.drawImage(this.pressAnyKepImage, 218, 500, null);
	}
}
