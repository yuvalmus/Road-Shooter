package levelsPackage;

import enemies.EnBuggy;
import game.Map;

public class Level1 extends Level
{
	
	public Level1(Map parentPanel, boolean shouldShootRandomly)
	{
		super(parentPanel, shouldShootRandomly);
		
		int y = -170;
		for (int i = 0; i < 5; i++) 
		{
			this.enemies.add(new EnBuggy(parentPanel, i * 150 + 152, y));
			this.enemies.get(i).addToPanel();
		}
		// adds the new enemies to the total enemies list in the map object
		for (int i = 0; i < this.enemies.size(); i++) 
		{
			Map.enemies.add(this.enemies.get(i));
		}
		
		this.startLevel();
	}
}
