package levelsPackage;

import enemies.EnArmoredTruck;
import game.Map;

public class Level5 extends Level
{
	
	public Level5(Map parentPanel, boolean shouldShootRandomly)
	{
		super(parentPanel, shouldShootRandomly);
		
		int x = 200, y = -200;
		for (int i = 0; i < 2; i++) 
		{
			this.enemies.add(new EnArmoredTruck(parentPanel, x, y));
			this.enemies.get(i).addToPanel();
			x += 300;
		}
		// adds the new enemies to the total enemies list in the map object
		for (int i = 0; i < this.enemies.size(); i++) 
		{
			Map.enemies.add(this.enemies.get(i));
		}
		
		this.startLevel();
	}
}
