package levelsPackage;

import java.util.LinkedList;
import java.util.Random;

import enemies.EnemyVehicle;
import game.GameFrame;
import game.Map;
import networking.ClientCommunication;

public abstract class Level {
	protected LinkedList<EnemyVehicle> enemies;
	protected Map parentPanel;
	protected Thread level;
	protected Thread shootRandomly;
	protected char movementDirection;  // r - right, l - left, u - up, d - down
	
	public Level(Map parentPanel, boolean shouldShootRandomly) {
		this.parentPanel = parentPanel;
		this.enemies = new LinkedList<>();
		// inits the runnables
		InitEnemiesMovement(shouldShootRandomly);
		// checks if the level itself should shoot randomly.
		// Value will be false in multiplayer mode where the server should decide which enemy shoots
		if (shouldShootRandomly) {
			InitShootRandomly();
		}
		this.movementDirection = 'r';
	}
	
	public void InitShootRandomly()
	{
		this.shootRandomly = new Thread(new Runnable() {
			@Override
			public void run() 
			{
				Random rnd = new Random();
				int currentIndex;
				while (checkAnybodyAlive())
				{
					currentIndex = rnd.nextInt(enemies.size());
					enemies.get(currentIndex).shoot();
					try {
						Thread.sleep(rnd.nextInt(3000) + 500);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});	
	}
	
	public void InitEnemiesMovement(boolean shouldShootRandomly)
	{
		this.level = new Thread(new Runnable() {
			
			public void run()
			{
				int currentY = enemies.get(0).getVehicleBounds().y, destY, i;
				// get the enemy vehicles from outside the screen
				while (currentY < 85)
				{
					for (i = 0; i < enemies.size(); i++, currentY += 1) 
					{
						enemies.get(i).moveVehicle(enemies.get(i).getVehicleBounds().x, currentY);
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {return;}
				}
				if (shouldShootRandomly) {					
					Level.this.shootRandomly.start();
				} else {
					// should let the server know that all the vehicles moved into the screen successfully
					Level.this.parentPanel.updateServerEnemiesInScreen();
				}
				while (checkAnybodyAlive())
				{
					if (movementDirection == 'r') {						
						// moves the vehicles right
						while (enemies.size() > 0 && enemies.get(enemies.size() - 1).getVehicleBounds().x < GameFrame.ROAD_WIDTH - 240)
						{
							for (i = 0; i < enemies.size(); i++) 
							{
								if (enemies.get(i).isVehicleAlive())
								{
									enemies.get(i).moveVehicle(enemies.get(i).getVehicleBounds().x + 1, currentY);
								}
							}
							try {
								if (!checkAnybodyAlive())
								{
									Level.this.levelEnded();
									return;
								}
								Thread.sleep(20);
							} catch (InterruptedException e) {return;}
						}
						movementDirection = 'd';
					}
					if (movementDirection == 'd') {						
						// brings all the vehicles down
						destY = currentY + 20;
						while (enemies.size() > 0 && enemies.get(enemies.size() - 1).getVehicleBounds().y < destY)
						{
							for (i = 0; i < enemies.size(); i++) 
							{
								if (enemies.get(i).isVehicleAlive())
								{
									enemies.get(i).moveVehicle(enemies.get(i).getVehicleBounds().x, ++currentY);
								}
							}
							try {
								if (!checkAnybodyAlive())
								{
									Level.this.levelEnded();
									return;
								}
								Thread.sleep(30);
							} catch (InterruptedException e) {return;}
						}
						movementDirection = 'l';
					}
					if (movementDirection == 'l') {						
						// moves the vehicles left
						while (enemies.get(0).getVehicleBounds().x > 150)
						{
							for (i = 0; i < enemies.size(); i++) 
							{
								if (enemies.get(i).isVehicleAlive())
								{
									enemies.get(i).moveVehicle(enemies.get(i).getVehicleBounds().x - 1, currentY);
								}
							}
							try {
								if (!checkAnybodyAlive())
								{
									Level.this.levelEnded();
									return;
								}
								Thread.sleep(20);
							} catch (InterruptedException e) {return;}
						}
						movementDirection = 'd';
					}
					if (movementDirection == 'd') {						
						// brings all the vehicles down
						destY = currentY + 20;
						while (enemies.get(0).getVehicleBounds().y < destY)
						{
							for (i = 0; i < enemies.size(); i++) 
							{
								if (enemies.get(i).isVehicleAlive())
								{
									enemies.get(i).moveVehicle(enemies.get(i).getVehicleBounds().x, ++currentY);
								}
							}
							try {
								if (!checkAnybodyAlive())
								{
									Level.this.levelEnded();
									return;
								}
								Thread.sleep(20);
							} catch (InterruptedException e) {return;}
						}
						movementDirection = 'r';
					}
				}
			}
		});
	}
	
	public void startLevel() {
		this.level.start();
	}
	
	public boolean checkAnybodyAlive()
	{
		boolean cont = false;
		for (int i = 0; i < enemies.size(); i++) 
		{
			if (enemies.get(i).isVehicleAlive())
			{
				cont = true;
			}
			else
			{
				enemies.remove(i);
				i--;
			}
		}
		return cont;
	}
	
	public void interruptLevel()
	{
		this.level.interrupt();
		this.shootRandomly.interrupt();
	}
	
	// called from the movement thread
	protected void levelEnded() 
	{
		this.parentPanel.startNextLevel();
	}
	
	public void enemyShoot(int whichEnemyShouldShoot) {
		this.enemies.get(whichEnemyShouldShoot).shoot();
	}

	public LinkedList<EnemyVehicle> getEnemiesList() {
		return this.enemies;
	}
}
