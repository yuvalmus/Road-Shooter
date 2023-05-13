package levelsPackage;

import enemies.EnBuggy;
import enemies.EnMissileJeep;
import game.Map;

public class Level2 extends Level
{
	
	public Level2(Map parentPanel, boolean shouldShootRandomly)
	{
		super(parentPanel, shouldShootRandomly);
		
		int x = 65, y;
		for (int i = 0; i < 5; i++) 
		{
			if (i != 0 && i != 4) {
				x += 214;
				y = -238;
			} else {
				x += 161;
				y = -256;
			}
			if (i % 2 == 0) {
				this.enemies.add(new EnBuggy(parentPanel, x + 20, y));
			} else {
				this.enemies.add(new EnMissileJeep(parentPanel, x, y));
			}
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
