package com.company;

/**
 * Imports
 */
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Packages
 */
import com.company.CarePackage.*;
import com.company.EasterEgg.AirSupport;
import com.company.MapGenerator.BackgroundRenderer;
import com.company.Obstacles.*;
import com.company.Explosives.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static com.company.Obstacles.ObstacleGenerator.generateObstacles;

/**
 * Missile class
 * <p>This class holds the state of game and all of the elements. Also, handles user inputs, and manages most of the calculations for the game.</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class GameManager {

	/**
	 * Objects, Variables, Components, ...
	 */
	public static boolean gameOver;                                         // Game over indicator
	private final List<Player> players;                                     // List of players
	private final List<Obstacle> obstacles;                                 // List of obstacles
	private final List<Explosive> explosives;                               // List of active explosives (ie. bombs, missiles, rayguns, ...)
	private final List<Explosion> explosions;                               // List of active explosions
	private final List<String> availableTanks;                              // List of un-used tanks
	private final List<CarePackage> carePackages;                           // List of active care packages
	private final List<AirSupport> airSupports;                             // List of all active air supports
	private final List<Tile> tiles;                                         // list of all tiles (Used for spawning players randomly on these tiles center)
	private final BufferedImage background;                                 // Game's pre-rendered background image
	private final CarePackageGenerator carePackageGenerator;                // The care package generator obj
	private final int spawnDistance = 64 * 3;                               // Tanks will spawn from each other with the given distance
	private final Random random;                                            // Random obj
	public static boolean isSinglePlayer = false;							// Game mode
	private Player winner;													// Winner player obj
	public boolean start = false;											// Start indicator
	public static int PLAYER_COUNT, AI_COUNT;								// Number of players

	// Input stuff
	private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keySPACE, keyCTRL;   // Accepted key bindings
	private final KeyHandler keyHandler;                                    // Key handler

	// Socket stuff
	private final int port;                                                 // Socket port
	private final String IP;                                                // Socket IP
	InetAddress group;                                                      // Socket group address
	private final MulticastSocket socket;                                   // Socket obj

	// Settings
	private int tankHealth;
	private int bombDamage;
	private int missileDamage;
	private int obstacleHealth;

	/**
	 * Object Constructor
	 *
	 * @param playersCount Players count
	 * @param AICount      AI players count
	 * @param mapFilePath  map of the game path
	 * @param mapType      Map type
	 * @param IP           Socket IP
	 * @param port         Socket Port
	 * @throws IOException handled Err
	 */
	public GameManager(int playersCount, int AICount, String mapFilePath, String mapType, String IP, int port, int tankHealth, int bombDamage, int missileDamage, int obstacleHealth) throws IOException {
		this.availableTanks = new ArrayList<>() {
			{
				add("tank_blue");
				add("tank_black");
				add("tank_red");
				add("tank_green");
				add("tank_sand");
			}
		};

		gameOver = false;
		this.carePackages = new ArrayList<>();
		this.tiles = new ArrayList<>();
		this.explosions = new ArrayList<>();
		this.explosives = Collections.synchronizedList(new ArrayList<>());
		this.players = new ArrayList<>();
		this.airSupports = new ArrayList<>();
		this.random = new Random();
		this.IP = IP;
		this.port = port;
		this.tankHealth = tankHealth;
		this.bombDamage = bombDamage;
		this.missileDamage = missileDamage;
		this.obstacleHealth = obstacleHealth;
		PLAYER_COUNT = playersCount;
		AI_COUNT = AICount;

		keyUP = false;
		keyDOWN = false;
		keyRIGHT = false;
		keyLEFT = false;
		keySPACE = false;
		keyCTRL = false;

		this.keyHandler = new KeyHandler();

		group = InetAddress.getByName(IP);
		socket = new MulticastSocket(port);

		socket.setTimeToLive(0);
		socket.joinGroup(group);

		// Add all the obstacles (ie. walls) (Note: this function must be called before spawning the players!)
		this.obstacles = generateObstacles(this.tiles, mapFilePath, obstacleHealth);

		// Initialize self
		spawnPlayer();

		// Initialize & start a reader thread (Note: thread must be started after player spawn!)
		Thread readerThread = new Thread(new ReadThread(socket, group, port));
		readerThread.start();

		// Renders the background image of the game (Note: this function must be called after generating the obstacles)
		this.background = BackgroundRenderer.render(mapType, ObstacleGenerator.getMap(), GameFrame.GAME_WIDTH, GameFrame.GAME_HEIGHT, 64, 64);

		// generate some CarePackages
		carePackageGenerator = new CarePackageGenerator(tiles);
	}

	/**
	 * The method updates the game state.
	 * <p>Note that order of the drawing is important for both rendering!!! DO NOT CHANGE THE ORDER!</p>
	 */
	public void update() {

		// ------------- Start Sending Player Packages ------------
		for (Player player : players) {
			// Send self data to the socket - There is no server, so the first to join is the leader (admin)
			if (player.isME) {
				try {
					// Player & tank's frame info
					String bufferString = player.getPlayerID() + "," + player.getDeathCount() + "," + player.getKillCount() + "," + player.isDead + "," + player.exploded + ",";
					bufferString += player.getTank().x + "," + player.getTank().y + "," + player.getTank().getRotation() + "," + player.getTank().getThrust() + "," + player.getTank().getHealth() + ",";
					bufferString += player.getTank().explosiveName + ",";
					bufferString += player.getTank().firePowerCoefficient + "," + player.getTank().explosiveSpeedCoefficient + "," + player.getTank().activateBuff + ',' + player.getTank().shield + "," + player.getTank().ghost + ",";
					bufferString += player.getName() + "," + player.isAI() + "," + player.getTeamID() + "," + player.getTank().getTankName();

					byte[] buffer = (bufferString).getBytes();                                             // Get info bytes
					DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);    // Create transfer protocol
					socket.send(datagram);                    // Send!
				} catch (Exception ex) {
					System.out.println("\033[1;31mPackage transfer failed for '" + player.getPlayerID() + "' (GameManager Connection Error).\033[0m");
					ex.printStackTrace();
				}
			}
		}
		// ------------- End Sending Player Packages ------------

		if (!start) {
			return;
		}

		// Save
		if (gameOver) {
//			try {
//				JSONObject js = new JSONObject();
//				js.put("Tank Health", tankHealth);
//				js.put("Bomb Damage", bombDamage);
//				js.put("Missile Damage", missileDamage);
//				js.put("Obstacle Health", obstacleHealth);
//
//				File myObj = new File("Assets//Data//Data.txt");
//				myObj.createNewFile();
//				FileWriter writer = new FileWriter("Assets//Data//Data.txt");
//				writer.write(js.toJSONString());
//				writer.close();
//			} catch (Exception ex) { ex.printStackTrace(); }
		}

		int deadCount = 0;
		for (Player player: players) {
			if (player.isDead)
				deadCount++;
		}

		// Check game over
		if ((players.size() == 1 && deadCount == 1) || (players.size() > 1 && deadCount == players.size() - 1)) {
			gameOver = true;
			for (Player player : players)
				if (!player.isDead) winner = player;

			if (winner == null)
				winner = players.get(0);
		}

		// Check explosives collision, fire, rotate, movement, obstacle collision, care package collision with players
		for (Player player : players) {

			// Explosive collision stuff
			for (Explosive explosive : explosives) {
				// Check collision only if player is alive & is not ghost
				if (!player.isDead && !player.getTank().getGhost()) {
					explosive.checkHit(player.getTank());

					// If ran out of health, set to disposal
					if (player.getTank().getHealth() <= 0)
						player.isDead = true;
				}

				if (explosive instanceof BombingRun && ((BombingRun) explosive).isHit()) explosive.setDead(true);
			}

			// Movement, rotate & fire stuff
			if (player.isME) {
				switch (player.getTank().getMarched()) {
					case 0:
						player.move(keyLEFT ? -1 : (keyRIGHT ? +1 : 0), keyUP ? +1 : (keyDOWN ? -1 : 0));
						break;
					case 1:
						player.move(keyLEFT ? -1 : (keyRIGHT ? +1 : 0), keyUP ? 0 : (keyDOWN ? -1 : 0));
						break;
					case -1:
						player.move(keyLEFT ? -1 : (keyRIGHT ? +1 : 0), keyUP ? +1 : (keyDOWN ? 0 : 0));
						break;
				}

				// Switch weapons
				if (keyCTRL && !player.isDead && player.equals(players.get(0))) {
					switch (player.getTank().getExplosiveName()) {
						case "bomb":
							player.getTank().setExplosiveName("missile");
							break;
						case "missile":
							player.getTank().setExplosiveName("bomb");
							break;
					}

					keyCTRL = false;
				}

				// Fire explosives!
				if (keySPACE && player.getTank().canFire() && !player.isDead) {
					this.explosives.add(player.getTank().fire());
					sendExplosiveToSocket(explosives.get(explosives.size() - 1));    // Send the created explosive to the socket

					// Disable auto-cannon
					keySPACE = false;
				}
			}

			// Obstacle collision Stuff
			int collisionCount = 0;
			for (Obstacle obstacle : obstacles) {
				// Check collision with Tanks
				if (!player.getTank().getGhost() && obstacle != null && player.getTank().hasCollision(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight())) {
					player.getTank().setMarched(march(player, obstacle));
//
//					// Avoid moving into the obstacles
//					switch (player.getTank().getMarched()) {
//						case 0:
//							player.move(0, keyUP ? +1 : (keyDOWN ? -1 : 0));
//							break;
//						case 1:
//							player.move(0, keyUP ? 0 : (keyDOWN ? -1 : 0));
//							break;
//						case -1:
//							player.move(0, keyUP ? +1 : (keyDOWN ? 0 : 0));
//							break;
//					}
					collisionCount++;
				}

				// Move mode, normal
				if (collisionCount == 0) player.getTank().setMarched(0);
			}

			// Care packages collision stuff
			for (CarePackage carePackage : carePackages) {
				if (player.getTank().hasCollision(carePackage.getX(), carePackage.getY(), carePackage.getWidth(), carePackage.getHeight()) && !carePackage.isUsed() && !player.getTank().isActivateBuff()) {
					carePackage.setTank(player.getTank());
					carePackage.doAction(player.getTank());
				} else if (carePackage.getTank() != null && carePackage.getTank().equals(player.getTank()))
					carePackage.turnOff();
			}
		}

		// Check collision between obstacles & explosives
		for (Obstacle obstacle : obstacles) {
			for (Explosive explosive : explosives) {

				// Collision between obstacles & explosives
				if (obstacle != null) {
					explosive.checkCollision(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight(), obstacle);

					// Kill missiles on collision to any game object. Kill anything that hits the corners
					if ((explosive instanceof Missile || obstacle instanceof Corner) && !(explosive instanceof BombingRun) && explosive.hasCollision(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight()))
						explosive.setDead(true);
				}
			}
		}

		// Check collision between the tanks
		for (Player player1 : players) {
			for (Player player2 : players)
				if (player1.getTank().getGhost() && player2.getTank().getGhost() && player1.getTank().checkTankCollision(player2) && !player1.equals(player2) && !player1.isDead)
					player1.getTank().pushTank(player2.getTank());
		}

		// Spawn new care-packages
		if (carePackageGenerator.timeDif() >= 10000) {

			int rand;
			do {
				rand = random.nextInt(tiles.size());
			} while (tiles.get(rand).getDir() <= 0);
			Tile tile = tiles.get(rand);

			// Set coordinates of the package
			int x = (int) (tile.getX() + (tile.getWidth() - 14 * 0.3d) / 2) + 2;
			int y = (int) (tile.getY() + (tile.getHeight() - 14 * 0.3d) / 2) + 2;

			switch (random.nextInt(6)) {
				case 0:
					carePackageGenerator.spawnPerk(x, y, "ghost", carePackages);
					break;
				case 1:
					carePackageGenerator.spawnPerk(x, y, "health", carePackages);
					break;
				case 2:
					carePackageGenerator.spawnPerk(x, y, "power", carePackages);
					break;
				case 3:
					carePackageGenerator.spawnPerk(x, y, "speed", carePackages);
					break;
				case 4:
					carePackageGenerator.spawnPerk(x, y, "laser", carePackages);
					break;
				case 5:
					carePackageGenerator.spawnPerk(x, y, "shield", carePackages);
					break;
			}

			// Send to server for transfer
			if (carePackages.size() != 0)
				sendCarePackageToSocket(carePackages.get(carePackages.size() - 1));
		}

		// Spawn new air support
		if (random.nextInt(1000) == 1) {
			int rand = random.nextInt(players.size());
			if (!players.get(rand).isDead && players.get(rand) != null) {
				airSupports.add(spawnRandomAirSupport(players.get(rand)));
				sendAirSupportToSocket(airSupports.get(airSupports.size() - 1));	// Send to server for async
			}
		}

		// Air supports
		for (AirSupport airSupport : airSupports) {
			if (airSupport.canDrop()) {
				airSupport.drop();
				explosives.add(new BombingRun(airSupport.getX(), airSupport.getY(), 10, 1.0d));
				if (airSupport.getBombCount() >= 0) {
					airSupport.setBombCount(airSupport.getBombCount() - 1);
					airSupport.setHaveDropped(false);
				}
			}
			airSupport.move();
		}

		// Remove the disposed game objects
		disposalHandler();
	}

	/**
	 * Sends to the socket steam
	 * @param airSupport	Air support obj
	 */
	private void sendAirSupportToSocket(AirSupport airSupport) {

		// If the object has not been sent yet
		if (!airSupport.isTransferred) {
			try {
				String bufferString = "airSupport," + airSupport.getAirSupportID();
				bufferString += "," + airSupport.getPlayer().getPlayerID() + "," + airSupport.getX() + "," + airSupport.getY() + "," + airSupport.getxStart() + "," + airSupport.getyStart() + ",";
				bufferString += airSupport.getxEnd() + "," + airSupport.getyEnd() + "," + airSupport.getRotation();

				byte[] buffer = (bufferString).getBytes();                                             // Get info bytes
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);      // Create transfer protocol
				socket.send(datagram);                    // Send!

				airSupport.isTransferred = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Sends to the socket steam
	 *
	 * @param explosive Explosive obj
	 */
	private void sendExplosiveToSocket(Explosive explosive) {
		// If obj has not been send before
		if (!explosive.isTransferred) {
			try {
				String bufferString = explosive.getExplosiveID() + "," + explosive.getX() + "," + explosive.getY() + "," + explosive.getRotation() + "," + explosive.getWidth() + "," + explosive.getHeight() + ",";
				bufferString += explosive.getScale() + "," + explosive.getDamage() + "," + explosive.getThrust() + ",";
				if (explosive.getColor() == Color.RED)
					bufferString += "red";
				else if (explosive.getColor() == Color.BLUE)
					bufferString += "blue";
				else if (explosive.getColor() == Color.BLACK)
					bufferString += "black";
				else if (explosive.getColor() == Color.GREEN)
					bufferString += "green";
				else
					bufferString += "sand";

				byte[] buffer = (bufferString).getBytes();                                             // Get info bytes
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);      // Create transfer protocol
				socket.send(datagram);                    // Send!

				explosive.isTransferred = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Sends to the socket steam
	 *
	 * @param carePackage Care package obj
	 */
	private void sendCarePackageToSocket(CarePackage carePackage) {

		// If obj has not been send before
		if (!carePackage.isTransferred) {
			try {
				String bufferString = "carePackage,";

				if (carePackage instanceof Ghost)
					bufferString += "ghost";
				else if (carePackage instanceof Health)
					bufferString += "health";
				else if (carePackage instanceof Laser)
					bufferString += "laser";
				else if (carePackage instanceof Power)
					bufferString += "power";
				else if (carePackage instanceof Shield)
					bufferString += "shield";
				else
					bufferString += "speed";

				bufferString += "," + carePackage.getX() + "," + carePackage.getY() + "," + carePackage.getCarePackageID();

				byte[] buffer = (bufferString).getBytes();                                             // Get info bytes
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);      // Create transfer protocol
				socket.send(datagram);                    // Send!

				carePackage.isTransferred = true;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Creates a new instance of air support
	 * @return	Air support instance
	 */
	private AirSupport spawnRandomAirSupport(Player player) {

		int x = 0, y = 0, xStart, yStart, xEnd, yEnd, def = 0;
		double rotation = 0;

		int startPoint;
		do {
			startPoint = random.nextInt(5);
		} while (startPoint == 0);
		switch (startPoint) {
			case 1:
				x = 25;
				y = 25;
				break;
			case 2:
				x = getScreenWidth() - 25;
				y = 25;
				break;
			case 3:
				x = 25;
				y = getScreenHeight() - 25;
				break;
			case 4:
				x = - 25;
				y = - 25;
				break;
		}

		xStart = (int) (player.getTank().getX() - player.getTank().getIcon().getWidth() * player.getTank().getScale()) - 15;
		yStart = (int) (player.getTank().getY() - player.getTank().getIcon().getHeight() * player.getTank().getScale()) - 15;
		xEnd = xStart + 70;
		yEnd = yStart + 70;

		double yDif = Math.abs(player.getTank().getY() - y), xDif = Math.abs(player.getTank().getX() - x), theta = Math.atan(yDif / xDif);

		if (startPoint == 1) rotation += 3 * Math.PI / 2 + theta;
		else if (startPoint == 2) rotation += Math.PI / 2 - theta;
		else if (startPoint == 3) rotation += 3 * Math.PI / 2 - theta;
		else if (startPoint == 4) rotation += Math.PI / 2 + theta;
		rotation += Math.PI / 2;

		return new AirSupport(player, x, y, xStart, yStart, xEnd, yEnd, rotation);
	}

	/**
	 * Get screen width
	 *
	 * @return width
	 */
	private int getScreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	/**
	 * Get screen height
	 *
	 * @return height
	 */
	private int getScreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	/**
	 * Determines march mode
	 *
	 * @param player   Player
	 * @param obstacle Obstacle
	 * @return March mode
	 */
	private int march(Player player, Obstacle obstacle) {
		return player.getTank().isForward(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight()) ? 1 : -1;
	}

	/**
	 * Draw game status (ie. tank's properties)
	 */
	public void drawGameStates(Graphics2D g2d, int left, int top, int MAP_WIDTH, int MAP_HEIGHT) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenSizeWidth = screenSize.getWidth();

		// Draw player's status (ie. health, ...)
		for (int i = 0; i < players.size(); i++) {

			int offset = 100;    // The distance between each label

			// X & Y
			int x = (i % 2 == 0) ? left + players.get(i).getTank().getIcon().getWidth() / 2 + (i * offset) : (int) (screenSizeWidth - (offset * i) - (screenSizeWidth - MAP_WIDTH) / 2);
			int y = top + MAP_HEIGHT + players.get(i).getTank().getIcon().getHeight() / 2;

			players.get(i).drawStatus(g2d, x, y);
		}
	}

	/**
	 * Checks if any of the game objects are to be disposed, and disposes them
	 */
	private void disposalHandler() {

		// Removes the vanished tracks & destroyed tanks
		for (int i = players.size() - 1; i >= 0; i--) {

			// Remove vanished tracks
			players.get(i).getTank().tracks.removeIf(Track::isVanished);

			// Vanish destroyed tanks
			if (players.get(i).isDead && !players.get(i).exploded) {
				explosions.add(new Explosion(players.get(i).getTank().getX(), players.get(i).getTank().getY(), 1.0, 0.05, Math.PI / 100, 0.01, "explosion_fire"));

				// This is just that the explosion is shown only once
				players.get(i).exploded = true;
				players.get(i).getTank().setRotation(-Math.PI / 2);

				try {
					players.get(i).getTank().setIcon(ImageIO.read(new File(TextureReference.getPath("rip"))));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				//players.remove(i);
			}
		}

		// Removes the exploded explosives
		Iterator<Explosive> iteratorExplosive = explosives.iterator();
		while (iteratorExplosive.hasNext()) {
			Explosive explosive = iteratorExplosive.next();

			// When lifespan has been reached
			if (explosive.isDead()) {

				// Remove the explosive
				iteratorExplosive.remove();

				// Add an explosion
				if (explosive instanceof Bomb)
					explosions.add(new Explosion(explosive.getX(), explosive.getY(), 0.5, 0.07, Math.PI / 100, 0.005, "explosion_smoke"));
				else if (explosive instanceof Missile || explosive instanceof BombingRun)
					explosions.add(new Explosion(explosive.getX(), explosive.getY(), 0.7, 0.05, Math.PI / 200, 0.005, "explosion_fire"));
			}
		}

		// Removed the destroyed obstacles (ie. fences)
		obstacles.removeIf(Obstacle::isDestroyed);

		// Removes the vanished explosions
		explosions.removeIf(Explosion::isVanished);

		// removes care-packages
		carePackages.removeIf(CarePackage::isRemove);

		// Remove air supports
		airSupports.removeIf(AirSupport::exit);
	}

	/**
	 * Spawns the players around the map in a way their paths are linked
	 */
	private void spawnPlayer() {
		try {
			// Get a random tank name
			int randomTankIndex = random.nextInt(availableTanks.size()), rand;

			do {
				rand = random.nextInt(tiles.size());
			} while (tiles.get(rand).getDir() <= 0);

			Tile tile = tiles.get(rand);
			int x = tile.getX() + 33;
			int y = tile.getY() + 33;

			while (true) {
				boolean isOK = true;
				for (Player player : players)
					if (getDistance(x, y, player.getTank().getX(), player.getTank().getY()) < spawnDistance)
						isOK = false;

				// If tank has a casual distance from all other tanks
				if (isOK) {
					Player player = new Player("Bardia", availableTanks.get(randomTankIndex), x, y, "01", "bomb", false, "@player" + System.currentTimeMillis(), true, tankHealth, bombDamage, missileDamage);

					players.add(player);                // Add the player to the list of players
					setTankDirection(tile, player);        // Sets the direction of the tank on startup

					// Remove the added tank
					availableTanks.remove(randomTankIndex);

					// Remove the spawn point tile
					tiles.remove(tile);
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Calculates the distance between the two points
	 *
	 * @param x1 X1-Axis
	 * @param y1 Y1-Axis
	 * @param x2 X2-Axis
	 * @param y2 Y2-Axis
	 * @return Distance
	 */
	private int getDistance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	/**
	 * Set's the tank's starting direction according to it's surrounding obstacles
	 *
	 * @param tile   The tile on which the tank is placed on
	 * @param player Player obj
	 */
	private void setTankDirection(Tile tile, Player player) {
		switch (tile.getDir()) {
			case 1:
				player.getTank().setRotation(-1 * Math.PI / 2);
				break;
			case 2:
				player.getTank().setRotation(0);
				break;
			case 3:
				player.getTank().setRotation(Math.PI / 2);
				break;
			default:
				player.getTank().setRotation(Math.PI);
				break;
		}
	}

	/**
	 * Key Listener
	 *
	 * @return KeyListener
	 */
	public KeyListener getKeyListener() {
		return keyHandler;
	}

	/**
	 * Getter
	 *
	 * @return Player's list
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Getter
	 *
	 * @return Obstacle's list
	 */
	public List<Obstacle> getObstacles() {
		return obstacles;
	}

	/**
	 * Getter
	 *
	 * @return Explosive's list
	 */
	public List<Explosive> getExplosives() {
		return explosives;
	}

	/**
	 * Getter
	 *
	 * @return Care packager's list
	 */
	public List<CarePackage> getCarePackages() {
		return carePackages;
	}

	/**
	 * Getter
	 *
	 * @return Game's pre-rendered game icon
	 */
	public BufferedImage getBackground() {
		return background;
	}

	/**
	 * Getter
	 * @return	Winner player
	 */
	public Player getWinner() {
		return winner;
	}

	/**
	 * Getter
	 *
	 * @return List of active air supports
	 */
	public List<AirSupport> getAirSupports() {
		return airSupports;
	}

	/**
	 * Getter
	 *
	 * @return	Obstacle's health
	 */
	public int getObstacleHealth() { return obstacleHealth; }

	/**
	 * Getter
	 *
	 * @return Explosions's list
	 */
	public List<Explosion> getExplosions() {
		return explosions;
	}

	/**
	 * The keyboard handler.
	 */
	class KeyHandler extends KeyAdapter implements Serializable {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					keyUP = true;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = true;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = true;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = true;
					break;
				case KeyEvent.VK_SPACE:
					keySPACE = true;
					break;
				case KeyEvent.VK_CONTROL:
					keyCTRL = true;
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					keyUP = false;
					break;
				case KeyEvent.VK_DOWN:
					keyDOWN = false;
					break;
				case KeyEvent.VK_LEFT:
					keyLEFT = false;
					break;
				case KeyEvent.VK_RIGHT:
					keyRIGHT = false;
					break;
			}
		}

	}

	/**
	 * This inner-class listens for new shared info and updates 'em
	 */
	class ReadThread implements Runnable {
		private final MulticastSocket socket;                  // Socket
		private final InetAddress group;                       // Group InetAddress
		private final int port;                                // port number
		private static final int MAX_LEN = 1000;         	   // Max transfer length

		/**
		 * Object Constructor
		 *
		 * @param socket Socket obj
		 * @param group  Group InetAddress
		 * @param port   Port number
		 */
		public ReadThread(MulticastSocket socket, InetAddress group, int port) {
			this.socket = socket;
			this.group = group;
			this.port = port;
		}

		/**
		 * Runs over and over again
		 */
		@Override
		public void run() {
			while (!gameOver) {
				byte[] buffer = new byte[ReadThread.MAX_LEN];
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
				try {
					socket.receive(datagram);
					try {
						String[] info = new String(datagram.getData()).split(",");

						// Clean up the received string (Remove the redundant bytes)
						for (int i = 0; i < info.length; i++)
							info[i] = info[i].replaceAll("\u0000.*", "");

						if (info[0].contains("@player"))    // Player info
							handlePlayer(info);
						else if (info[0].contains("@bomb") || info[1].contains("@missile") || info[1].contains("@bombRun"))        // Explosive info
							handleExplosive(info);
						else if (info[0].equals("carePackage"))		// Care package info
							handleCarePackage(info);
						else if (info[0].equals("airSupport"))		// Air support info
							handleAirSupport(info);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} catch (IOException e) {
					System.out.println("Socket closed!");
				}
			}
		}

		/**
		 * Handles an air support update
		 *
		 * @param info	Air support info
		 */
		private void handleAirSupport(String[] info) {

			// Not the duplicates idiot
			for (AirSupport airSupport : airSupports)
				if (airSupport.getAirSupportID().equals(info[1]))
					return;

			Player supportPlayer = null;
			for (Player player : players)
				if (player.getPlayerID().equals(info[2]))
					supportPlayer = player;

			AirSupport airSupport = new AirSupport(supportPlayer, Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), Integer.parseInt(info[6]), Integer.parseInt(info[7]), Integer.parseInt(info[8]), Double.parseDouble(info[9]));
			airSupport.setAirSupportID(info[1]);    // Set ID
			airSupports.add(airSupport);
		}

		/**
		 * Handles a care package update
		 *
		 * @param info Care package info
		 */
		private void handleCarePackage(String[] info) {

			// Not the duplicates idiot
			for (CarePackage carePackage : carePackages)
				if (carePackage.getCarePackageID().equals(info[4]))
					return;

			int x = Integer.parseInt(info[2]);
			int y = Integer.parseInt(info[3]);

			switch (info[1]) {
				case "ghost":
					carePackageGenerator.spawnPerk(x, y, "ghost", carePackages);
					break;
				case "health":
					carePackageGenerator.spawnPerk(x, y, "health", carePackages);
					break;
				case "power":
					carePackageGenerator.spawnPerk(x, y, "power", carePackages);
					break;
				case "speed":
					carePackageGenerator.spawnPerk(x, y, "speed", carePackages);
					break;
				case "laser":
					carePackageGenerator.spawnPerk(x, y, "laser", carePackages);
					break;
				case "shield":
					carePackageGenerator.spawnPerk(x, y, "shield", carePackages);
					break;
			}
		}

		/**
		 * Handles an explosive update
		 *
		 * @param info explosive info
		 */
		private void handleExplosive(String[] info) {

			// Not the duplicates idiot
			for (Explosive tmp : explosives)
				if (tmp.getExplosiveID().equals(info[0]))
					return;

			Color color = null;
			switch (info[9]) {
				case "sand":
					color = new Color(255, 253, 208);
					break;
				case "black":
					color = Color.BLACK;
					break;
				case "blue":
					color = Color.BLUE;
					break;
				case "red":
					color = Color.RED;
					break;
				case "green":
					color = Color.GREEN;
					break;
			}

			Explosive explosive;
			if (info[0].contains("@bomb"))
				explosive = new Bomb(Integer.parseInt(info[1]), Integer.parseInt(info[2]), Double.parseDouble(info[3]), Integer.parseInt(info[8]), Integer.parseInt(info[7]), Double.parseDouble(info[6]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), color);
			else if (info[0].contains("@missile"))
				explosive = new Missile(Integer.parseInt(info[1]), Integer.parseInt(info[2]), Double.parseDouble(info[3]), Integer.parseInt(info[8]), Integer.parseInt(info[7]), Double.parseDouble(info[6]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), color);
			else
				explosive = new BombingRun(Integer.parseInt(info[1]), Integer.parseInt(info[2]), 10, 1.0d);

			explosives.add(explosive);
		}

		/**
		 * Handles a player update
		 *
		 * @param info Player info
		 */
		private void handlePlayer(String[] info) {
			// Don't receive itself
			if (!players.get(0).getPlayerID().equals(info[0])) {
				boolean exist = false;
				for (Player player : players)
					if (!player.isME && player.getPlayerID().equals(info[0])) {
						updatePlayer(player, info);
						exist = true;
						break;
					}

				// Create the fucking player that has joined the fucking server
				if (!exist) {
					spawnPlayer();
					players.get(players.size() - 1).setPlayerID(info[0]);    // Set the player ID
					players.get(players.size() - 1).isME = false;
					updatePlayer(players.get(players.size() - 1), info);
				}
			}
		}

		/**
		 * Updates the received information about the player obj
		 *
		 * @param info Player data
		 */
		private void updatePlayer(Player player, String[] info) {
			player.setName(info[16]);
			player.getTank().setTankName(info[19]);
			player.getTank().setX(Integer.parseInt(info[5]));
			player.getTank().setY(Integer.parseInt(info[6]));
			player.setTeamID(info[18]);
			player.getTank().setExplosiveName(info[10]);
			player.isAI = Boolean.parseBoolean(info[17]);
			player.setPlayerID(info[0]);
			player.setDeathCount(Integer.parseInt(info[1]));                            // Set death count
			player.setKillCount(Integer.parseInt(info[2]));                                // Set kill count
			player.isDead = Boolean.parseBoolean(info[3]);                                // Set isDead status
			player.exploded = Boolean.parseBoolean(info[4]);                            // Set exploded status
			player.getTank().setRotation(Double.parseDouble(info[7]));                    // Set tank's rotation
			player.getTank().setThrust(Integer.parseInt(info[8]));                        // Set tank's thrust
			player.getTank().setHealth(Integer.parseInt(info[9]));                        // Set tank's health
			player.getTank().setFirePowerCoefficient(Integer.parseInt(info[11]));        // Set tank's fire power coefficient
			player.getTank().setExplosiveSpeedCoefficient(Integer.parseInt(info[12]));    // Set tank's explosive speed coefficient
			player.getTank().setActivateBuff(Boolean.parseBoolean(info[13]));            // Set tank's active buff status
			player.getTank().setShield(Boolean.parseBoolean(info[14]));                    // Set tank's shield status
			player.getTank().setGhost(Boolean.parseBoolean(info[15]));                    // Set tank's ghost status
		}
	}
}