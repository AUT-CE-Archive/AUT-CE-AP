package com.company;

/**
 * Imports
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Date;

/**
 * Packages
 */
import com.company.CarePackage.CarePackage;
import com.company.EasterEgg.AirSupport;
import com.company.Obstacles.Fence;
import com.company.Obstacles.Obstacle;
import com.company.Explosives.*;

/**
 * Missile class
 * <p>This class does a triple-buffering as a modern BufferStrategy which operates as the main game engine which inherits from JFrame</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.1.0
 */
public class GameFrame extends JFrame {

	/**
	 * Objects, Variables, Components, ...
	 */
	public static int GAME_HEIGHT;     // 720p game resolution
	public static int GAME_WIDTH;      // wide aspect ratio (16/9)

	private BufferStrategy bufferStrategy;

	public long startTime = new Date().getTime();
	public long loading = 3000;
	private float opacity = 0;

	/**
	 * Object Constructor
	 *
	 * @param title JFrame title
	 */
	public GameFrame(String title) {

		// Initialize default properties
		super(title);

		setResizable(false);                                     // Set resizable
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);            // Go full screen
		this.setUndecorated(true);                               // Remove title bar
		this.setBackground(Color.WHITE);                         // Set background color
	}

	/**
	 * This method creates a BufferStrategy for the rendering
	 */
	public void initBufferStrategy() {
		createBufferStrategy(5);
		bufferStrategy = getBufferStrategy();
	}

	/**
	 * Game rendering with triple-buffering using BufferStrategy.
	 *
	 * @param state GameState object
	 */
	public void render(GameManager state) {
		// Render single frame
		do {
			do {
				// Get new Graphics2D to make sure the strategy is validated
				Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
				try {
					doRendering(graphics, state);
				} finally {
					graphics.dispose();    // Dispose the graphics
				}
				// Repeat the rendering if the drawing buffer contents were restored
			} while (bufferStrategy.contentsRestored());

			// Display the buffer
			bufferStrategy.show();

			// Force system to draw
			Toolkit.getDefaultToolkit().sync();

		} while (bufferStrategy.contentsLost());
	}

	/**
	 * Rendering all game elements based on the game state.
	 *
	 * @param g2d     Graphics2D context
	 * @param manager GameState object
	 */
	private void doRendering(Graphics2D g2d, GameManager manager) {

		// The await
		if (GameManager.PLAYER_COUNT != manager.getPlayers().size()) {
			drawAwait(g2d, manager);
			startTime = new Date().getTime();
			return;
		}

		// The count down
		if (timeDiff() <= loading) {
			drawLoading(g2d);
			return;
		}
		manager.start = true;

		if (!GameManager.gameOver) {

			// Draw background
			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, getScreenWidth(), getScreenHeight());

			// Draw background
			g2d.drawImage(manager.getBackground(), (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2, GAME_WIDTH, GAME_HEIGHT, null);

			// Draw game title (Back ground)
			g2d.setColor(Color.BLACK);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(24f));
			g2d.drawString(this.getTitle(), (getScreenWidth() - getStringWidth(g2d, "Trouble Tank")) / 2, 40);

			// Draw separator
			drawSeparator(g2d, (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2 - 30, GAME_WIDTH);

			// Draw obstacles (Mid ground)
			for (Obstacle obstacle : manager.getObstacles()) {
				if (obstacle != null) {

					if (obstacle instanceof Fence)
						((Fence) obstacle).draw(g2d);
					else {
						g2d.setColor(obstacle.getColor());
						g2d.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
					}
				}
			}

			// Draw CarePackages
			for (CarePackage carePackage : manager.getCarePackages()) {
				if (carePackage.getOpacity() > 0)
					carePackage.draw(g2d);
			}

			// Draw player (Tank & tracks) (Fore ground)
			for (Player player : manager.getPlayers()) {
				player.draw(g2d);

				// Draw AI's debug
//			if (player.getTank() instanceof TankAI)
//				((TankAI)player.getTank()).debug(g2d);
			}

			// Draw explosives (Bomb & missile) (Fore ground)
			for (Explosive explosive : manager.getExplosives()) {
				if (explosive instanceof Bomb) ((Bomb) explosive).draw(g2d);    // Draw Explosive (bomb)
				else if (explosive instanceof Missile) ((Missile) explosive).draw(g2d);    // Draw Explosive (Missile)
				else if (explosive instanceof BombingRun)
					((BombingRun) explosive).draw(g2d);    // Draw Explosive (Bombing Run)
			}

			// Draw explosions (Fore ground)
			for (Explosion explosion : manager.getExplosions()) {
				explosion.draw(g2d);
			}

			// Draw air-support
			for (AirSupport airSupport : manager.getAirSupports()) {
				airSupport.draw(g2d);
			}

			// Draw games status - Bottom panel
			manager.drawGameStates(g2d, (this.getWidth() - GAME_WIDTH) / 2, (this.getHeight() - GAME_HEIGHT) / 2, GAME_WIDTH, GAME_HEIGHT);

			// Draw game time
			drawTime(g2d);

		} else {
			drawGameOver(g2d);
			drawWinnerStatus(g2d, manager.getWinner());
		}
	}

	/**
	 * Draws a thin separator line
	 */
	private void drawSeparator(Graphics2D g2d, int x, int y, int width) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, width, 4);
	}

	/**
	 * Draws the game over message
	 *
	 * @param g2d Graphics2D
	 */
	private void drawGameOver(Graphics2D g2d) {
		String massage = "WINNER WINNER CHICKEN DINNER!";
		if (opacity <= 1) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			opacity += 0.002;
		}

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		g2d.setColor(new Color(255, 204, 0));
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(70f));
		g2d.drawString(massage, (getScreenWidth() - getStringWidth(g2d, massage)) / 2, getScreenHeight() / 2 - 50);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
	}

	/**
	 * Draws winner status
	 *
	 * @param g2d    Graphics2D
	 * @param winner Winner obj
	 */
	private void drawWinnerStatus(Graphics2D g2d, Player winner) {
		String massage = winner.getName() + " WINS!";
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.7));
		g2d.setColor(new Color(140, 140, 140));
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(80f));
		g2d.drawString(massage, (getScreenWidth() - getStringWidth(g2d, massage)) / 2, getScreenHeight() / 2 + 20);

		massage = "FATALITY";
		g2d.setColor(new Color(255, 77, 77));
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(80f));
		g2d.drawString(massage, (getScreenWidth() - getStringWidth(g2d, massage)) / 2, getScreenHeight() / 2 + 90);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1));
	}

	/**
	 * Draws the await screen
	 * @param g2d			Graphics2D
	 * @param manager		Manager obj
	 */
	private void drawAwait(Graphics2D g2d, GameManager manager) {

		// Draw background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getScreenWidth(), getScreenHeight());

		String massage1 = "WAITING FOR PLAYERS...";
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.7));
		g2d.setColor(Color.BLACK);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(40f));
		g2d.drawString(massage1, (getScreenWidth() - getStringWidth(g2d, massage1)) / 2, getScreenHeight() / 2 - 20);

		String massage2 = "";
		for (Player player: manager.getPlayers())
			massage2 += player.getName() + ", ";

		massage2 = massage2.substring(0, massage2.length() - 2);

		if (manager.getPlayers().size() == 1)
			massage2 += " has joined so far.";
		else
			massage2 += " have joined so far.";

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.7));
		g2d.setColor(Color.BLACK);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(20f));
		g2d.drawString(massage2, (getScreenWidth() - getStringWidth(g2d, massage2)) / 2, getScreenHeight() / 2 + 20);
	}

	/**
	 * Draws the time
	 *
	 * @param g2d Graphics2D
	 */
	private void drawTime(Graphics2D g2d) {
		long diff = timeDiff() - loading;
		long diffSeconds = diff / 1000 % 60;
		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000);
		String time = String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
		g2d.setColor(new Color(255, 80, 80));
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(35f));
		g2d.drawString(time, 25, getHeight() - 25);
	}

	/**
	 * Loading visual effect
	 *
	 * @param g2d Graphics2D
	 */
	private void drawLoading(Graphics2D g2d) {
		long diff = timeDiff();
		long diffSeconds = diff / 1000 % 60;

		// Draw background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getScreenWidth(), getScreenHeight());

		g2d.setColor(new Color(255, 204, 0));
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(40f));

		String dot = "";
		for (int i = 0; i < diffSeconds % 4; i++) dot = dot.concat(". ");

		g2d.drawString("loading " + dot, 25, getHeight() - 25);
	}

	/**
	 * Time difference
	 *
	 * @return Long
	 */
	private long timeDiff() {
		return Math.abs(startTime - new Date().getTime());
	}

	/**
	 * Calculates the string width in pixels
	 *
	 * @param g2d    Graphics2D
	 * @param string String
	 * @return Width
	 */
	private int getStringWidth(Graphics2D g2d, String string) {
		return g2d.getFontMetrics().stringWidth(string);
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
}