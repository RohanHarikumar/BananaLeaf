package com.bayviewgen.bananaleaf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */

class Game {
	private Parser parser;
	private Room currentRoom;
	private Room prevRoom;
	private Scanner readaFile;
	// public Item roomKeys;
	// This is a MASTER object that contains all of the rooms and is easily
	// accessible.
	// The key will be the name of the room -> no spaces (Use all caps and
	// underscore -> Great Room would have a key of GREAT_ROOM
	// In a hashmap keys are case sensitive.
	// masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the
	// Great Room (assuming you have one).
	private HashMap<String, Room> masterRoomMap;

	public void initRooms(String fileName) throws Exception {

		masterRoomMap = new HashMap<String, Room>();
		Scanner roomScanner;
		try {
			HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();
			roomScanner = new Scanner(new File(fileName));
			while (roomScanner.hasNext()) {
				Room room = new Room();
				// Read the Name
				String roomName = roomScanner.nextLine();
				room.setRoomName(roomName.split(":")[1].trim());
				// Read the Description
				String roomDescription = roomScanner.nextLine();
				room.setDescription(roomDescription.split(":")[1].replaceAll(
						"<br>", "\n").trim());
				// Read Items
				String roomItems = roomScanner.nextLine();
				room.getRoomItems(roomItems);
				// Read the Exits
				String roomExits = roomScanner.nextLine();
				// An array of strings in the format E-RoomName
				String[] rooms = roomExits.split(":")[1].split(",");

				HashMap<String, String> temp = new HashMap<String, String>();
				for (String s : rooms) {
					temp.put(s.split("-")[0].trim(), s.split("-")[1]);
				}

				exits.put(roomName.substring(10).trim().toUpperCase()
						.replaceAll(" ", "_"), temp);

				// This puts the room we created (Without the exits in the
				// masterMap)
				masterRoomMap.put(roomName.toUpperCase().substring(10).trim()
						.replaceAll(" ", "_"), room);

				// Now we better set the exits.
			}

			for (String key : masterRoomMap.keySet()) {
				Room roomTemp = masterRoomMap.get(key);
				HashMap<String, String> tempExits = exits.get(key);
				for (String s : tempExits.keySet()) {
					// s = direction
					// value is the room.

					String roomName2 = tempExits.get(s.trim());
					Room exitRoom = masterRoomMap.get(roomName2.toUpperCase()
							.replaceAll(" ", "_"));
					roomTemp.setExit(s.trim().charAt(0), exitRoom);

				}

			}

			roomScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the game and initialise its internal map.
	 */
	public Game() {
		
		try {
			initRooms("data/rooms.dat");
			currentRoom = masterRoomMap.get("ROOM_1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parser = new Parser();
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		 printWelcome();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Thank you for playing.  Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		Scanner usrInput = new Scanner(System.in);
		System.out.println();
		System.out.println("Welcome to Zork!");
		System.out.println("Zork is a new, incredibly boring adventure game.");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		System.out.println(currentRoom.longDescription());
	

	}

	/**
	 * Given a command, process (that is: execute) the command. If this command
	 * ends the game, true is returned, otherwise false is returned.
	 */
	private boolean processCommand(Command command) {
		Scanner usrInput = new Scanner(System.in);
		if (command.isUnknown()) {
			System.out.println("I don't know what you mean...");
			return false;
		}

		String commandWord = command.getCommandWord();
		if (commandWord.equals("help"))
			printHelp();
		else if (commandWord.equals("go"))
			goRoom(command);
		else if (commandWord.equals("take"))
			takeItem(command);
		else if (commandWord.equals("quit")) {
			if (command.hasSecondWord())
				System.out.println("Quit what?");
			else
				System.out.println("Would you like to save your progress? Y/N");
			// -------
			String Answer = usrInput.nextLine();
			if (Answer.equals("Y") || Answer.equals("y")) {
				WriteFile();

			} else if (Answer.equals("N") || Answer.equals("n"))
				EmptyplayerFile();

			else
				System.out.println("Sorry, that is not a valid selection");
			// -------
			return true; // signal that we want to quit

		} else if (commandWord.equals("eat")) {
			System.out
					.println("Do you really think you should be eating at a time like this?");
		}
		return false;
	}

	// implementations of user commands:

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp() {
		System.out.println("You are lost. You are alone. You wander");
		System.out.println("around Banana Leaf Paradise.");
		System.out.println();
		System.out.println("Your command words are:");
		parser.showCommands();
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 */
	private void takeItem(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Take What?");
			return;
		}

		String itemType = command.getSecondWord().toUpperCase();
		String text = currentRoom.getRoomItems();
		String lRoom = currentRoom.getRoomName();
		String tmpColl[] = new String[5];
		tmpColl = Keys.getCollectedKeys();

		boolean alreadyPicked = false;

		alreadyPicked = Keys.CheckcollectedKeys(lRoom);

		if (itemType.equals("KEY")) {
			if (alreadyPicked) {
				System.out
						.println("You already picked the key from this room ...");
			} else {
				if (text.toLowerCase().contains("key")) {
					System.out.println("Congrads, you got the key from "
							+ lRoom + "....");
					Keys.UpdatecollectedKeys(lRoom);

				} else
					System.out.println("There is no " + itemType + " in "
							+ lRoom);
			}
		} else if (itemType.equals("BOAT")) {
			lRoom = currentRoom.getRoomName();
			int keyCount = 0;
			if (lRoom.contentEquals("Room 35")) {
				keyCount = Keys.CheckKeyCounts();
				if (keyCount == 5)
					System.out
							.println("Great, you have escaped the Banana Leaf Paradise");
				System.exit(0);
			} else
				System.out.println("There is no boat here...");

		}

	}

	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return;
		}

		String direction = command.getSecondWord();

		// Try to leave current room.
		Room nextRoom = currentRoom.nextRoom(direction);

		if (nextRoom == null)
			System.out.println("There is no door!");
		else {
			prevRoom = currentRoom;
			currentRoom = nextRoom;
			System.out.println(currentRoom.longDescription());

			String lRoom = currentRoom.getRoomName();
			int keyCount = 0;

			if (lRoom.contentEquals("Room 35")) {
				// System.out.println("Room Name=>" + lRoom);
				keyCount = Keys.CheckKeyCounts();

				if (keyCount == 5) {

					System.out
							.println("You have five keys. You can drive the boat!");
				}
			}
			String tmpColl[] = new String[5];

			boolean alreadyPicked;

			System.out.println(currentRoom.getRoomItems());

		}

	}

	public void openFile(String fileName) { // Method to open file
		try {
			readaFile = new Scanner(new File(fileName));
		} catch (Exception e) {
			System.out.println("Could not find file... " + fileName);
		}
	}

	public void closeFile() {
		readaFile.close();
	}

	public void updateGameInfo() {
		openFile("data/players.dat");

	}

	public void WriteFile() {
		PrintWriter write2File = null;
		String[] tempKey = new String[5];
		tempKey = Keys.getCollectedKeys();
		try {
			write2File = new PrintWriter("data/players.dat");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		write2File.println(currentRoom.getRoomName());
		System.out.println();

		for (int j = 0; j < 5; j++) {
			if (tempKey[j] != null) {
				write2File.println(tempKey[j]);
				System.out.println();
			}
		}
		write2File.close();
	}

	public void EmptyplayerFile() {
		PrintWriter write2File = null;
		try {
			write2File = new PrintWriter("data/players.dat");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		write2File.close();
	}
}
