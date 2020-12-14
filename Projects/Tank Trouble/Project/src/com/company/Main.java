package com.company;

/**
 * Imports
 */
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.EventQueue;
import java.io.File;
import java.util.Scanner;
import javax.swing.JFrame;

/**
 * Driver class
 */
public class Main {

	/**
	 * Objects, Variables, Components, ...
	 */
	private static GameFrame frame;
	private static GameManager manager;
	private static Scanner scanner;

	/**
	 * Driver method
	 *
	 * @param args Args
	 */
	public static void main(String[] args) {

		// Initialize the global thread-pool
		ThreadPool.init();

		frame = new GameFrame("Tank Trouble");
		try {
			JSONObject jsonObject = new JSONObject();
			JSONParser parser = new JSONParser();
			File file = new File("Assets//Data//Data.txt");
			scanner = new Scanner(file);
			while (scanner.hasNextLine())
				jsonObject = (JSONObject) parser.parse(scanner.nextLine());

			manager = new GameManager(1, 0, "map.txt", (String) jsonObject.get("Map Type"), "239.1.2.3", 1234, Integer.parseInt(jsonObject.get("Tank Health").toString()), Integer.parseInt(jsonObject.get("Bomb Damage").toString()), Integer.parseInt(jsonObject.get("Missile Damage").toString()), Integer.parseInt(jsonObject.get("Obstacle Health").toString()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// After the player clicks 'PLAY' ...
		EventQueue.invokeLater(() -> {

			frame.setLocationRelativeTo(null);                            // put frame at center of screen
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.initBufferStrategy();

			// Create and execute the game-loop
			GameLoop game = new GameLoop(frame, manager);
			game.init();
			ThreadPool.execute(game);
		});
	}
}