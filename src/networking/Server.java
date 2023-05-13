package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

import levelsPackage.Level;

public class Server {
	private ServerSocket server;
	private LinkedList<ClientCommunication> clients;
	private LinkedList<ClientCommunication> inMultiplayerGameList;
	private LinkedList<Integer> enemiesHP;
	private Thread enemyRandomShotsThread;
	private boolean finishedShootingThread;
	private int currentLevel;
	private boolean isServerOn;
	
	
	public Server() {
		try {
			this.server = new ServerSocket(NetworkingConstants.PORT);
			this.isServerOn = true;
			this.clients = new LinkedList<>();
			this.inMultiplayerGameList = new LinkedList<>();
			this.enemiesHP = new LinkedList<Integer>();
			this.currentLevel = 0;
			this.enemyRandomShotsThread = null;
			this.finishedShootingThread = false;
			updateEnemiesHPs();
			
			Thread connectionsThread = new Thread(new Runnable() {
				@Override
				public void run() {
					getNewConnections();
				}
			});
			connectionsThread.start();
			System.out.println("Server is listening on port " + NetworkingConstants.PORT + "...");
		} catch (IOException e) {}
	}
	
	private void getNewConnections() {
		while (this.isServerOn) {
			try {
				Socket newSocket = this.server.accept();
				ClientCommunication newClient = new ClientCommunication(newSocket);
				this.clients.add(newClient);
				System.out.println("A new connection to the server: " + newSocket);
				Thread messagesThread = new Thread(new Runnable() {
					@Override
					public void run() {
						getMessagesFromClient(newClient);
					}
				});
				messagesThread.start();
			} catch (IOException e) {
				System.out.println("Socket acceptance failed!");
			}
		}
	}
	
	private void getMessagesFromClient(ClientCommunication client) {
		while (this.isServerOn) {
			String msg = client.recvMsgFromClient();
			if (msg.indexOf(',') != -1) {
				if (this.inMultiplayerGameList.get(0) == client) {
					this.inMultiplayerGameList.get(1).sendMsgToClient(msg);
				} else {
					this.inMultiplayerGameList.get(0).sendMsgToClient(msg);
				}
			}
			else if (msg.startsWith("U")) {
				//an update from the user about when the enemies have entered the screen and can start shooting at the players
				this.enemiesRandomShots();
			}
			// enemy vehicle hit
			else if (msg.startsWith("H")) {
				String[] values = msg.substring(1).split("-");
				this.enemyHit(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
			}
			else if (msg.equals("EXIT")) {
				client.close();
				this.clients.remove(client);
				this.inMultiplayerGameList.remove(client);
				System.out.println("The client " + client + " has left the game!");
				break;
			} else {
				System.out.println(msg);
				if (msg.equals("MULTIPLAYER")) {
					System.out.println("received a multiplayer request");
					client.sendMsgToClient("SEARCHING");
					if (this.clients.size() == 2) {
						this.clients.get(0).sendMsgToClient("FOUND");
						client.sendMsgToClient("FOUND");
					}
				}
				else if (msg.equals("READY")) {
					this.inMultiplayerGameList.add(client);
					if (this.inMultiplayerGameList.size() == 2) {
						this.inMultiplayerGameList.get(0).sendMsgToClient("START");
						client.sendMsgToClient("START");
					}
				}
			}
		}
	}
	
	public synchronized void enemyHit(int whichEnemyVehicle, int previousVehicleHealth, int amountOfDamage) {
		// if the enemy actually exists
		if (this.enemiesHP.get(whichEnemyVehicle) > 0) {
			int currentHP = this.enemiesHP.get(whichEnemyVehicle);
			// if the HP that was sent is actually the enemy vehicle's HP.
			// Checking this because both of the players will send the server a message when an enemy was hit
			// and I only want to handle a hit once.
			if (currentHP == previousVehicleHealth) {
				this.enemiesHP.set(whichEnemyVehicle, currentHP - amountOfDamage);
				if (this.enemiesHP.get(whichEnemyVehicle) <= 0) {
					System.out.println("Enemy " + whichEnemyVehicle + " has died!");
				}
			}
		}
	}
	
	public synchronized void enemiesRandomShots() {
		// the first player that lets the server know to start shooting starts the thread
		if (this.enemyRandomShotsThread == null || this.finishedShootingThread) {			
			this.enemyRandomShotsThread = new Thread(new Runnable() {	
				@Override
				public void run() 
				{
					Random rnd = new Random();
					int currentIndex;
					while (checkAnybodyAlive())
					{
						currentIndex = rnd.nextInt(howManyAlive());
						while (enemiesHP.get(currentIndex) <= 0) {
							currentIndex = rnd.nextInt(howManyAlive());
						}
						inMultiplayerGameList.get(0).sendMsgToClient("ES" + currentIndex);
						inMultiplayerGameList.get(1).sendMsgToClient("ES" + currentIndex);
						try {
							Thread.sleep(rnd.nextInt(3000) + 500);
						} catch (InterruptedException e) {
							return;
						}
					}
					currentLevel++;
					updateEnemiesHPs();
					finishedShootingThread = true;
				}
			});
			this.finishedShootingThread = false;
			this.enemyRandomShotsThread.start();
		}
	}
	
	public boolean checkAnybodyAlive() {
		for (int i = 0; i < this.enemiesHP.size(); i++) {
			if (this.enemiesHP.get(i) > 0) {
				return true;
			}
		}
		return false;
	}
	
	public int howManyAlive() {
		int counterAlive = 0;
		for (int i = 0; i < this.enemiesHP.size(); i++) {
			if (this.enemiesHP.get(i) > 0) {
				counterAlive++;
			}
		}
		return counterAlive;
	}
	
	public void updateEnemiesHPs() {
		this.enemiesHP.clear();
		for (int j = 0; j < NetworkingConstants.HPs[this.currentLevel].length; j++) {
			this.enemiesHP.add(NetworkingConstants.HPs[this.currentLevel][j]);
		}
	}

	public void turnOffServer() {
		this.isServerOn = false;
		for (ClientCommunication clientSocket: this.clients) {
			clientSocket.close();
		}
		try {
			this.server.close();
			System.out.println("Server closed!");
		} catch (IOException e) {}
	}
	
	
	
	
	public static void main(String[] args) {
		Server server = new Server();
	}
}
