package levelsPackage;

import enemies.EnMissileJeep;
import game.Map;

public class Level3 extends Level
{
	
	public Level3(Map parentPanel, boolean shouldShootRandomly)
	{
		super(parentPanel, shouldShootRandomly);
		
		int x = 200, y = -200;
		for (int i = 0; i < 5; i++) 
		{
			this.enemies.add(new EnMissileJeep(parentPanel, x, y));
			this.enemies.get(i).addToPanel();
			x += 190;
		}
		// adds the new enemies to the total enemies list in the map object
		for (int i = 0; i < this.enemies.size(); i++) 
		{
			Map.enemies.add(this.enemies.get(i));
		}
		
		this.startLevel();
	}
}