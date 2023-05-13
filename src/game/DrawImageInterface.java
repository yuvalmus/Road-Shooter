package game;

import java.awt.Graphics;

// exists in the drawable classes like Bullet, playerVehicle, EnemyVehicle and more.
// used in order to draw the object on the Map's screen
public interface DrawImageInterface 
{
	public void draw(Graphics g);
}
