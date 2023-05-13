package levelsPackage;

import enemies.EnBiker;
import game.Map;

public class Level4 extends Level
{
	
	public Level4(Map parentPanel, boolean shouldShootRandomly)
	{
		super(parentPanel, shouldShootRandomly);
		
		int x = 200, y = -200;
		for (int i = 0; i < 5; i++) 
		{
			this.enemies.add(new EnBiker(parentPanel, x, y));
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
