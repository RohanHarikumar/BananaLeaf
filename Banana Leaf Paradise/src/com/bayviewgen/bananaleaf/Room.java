package com.bayviewgen.bananaleaf;/* * Class Room - a room in an adventure game. * * Author:  Michael Kolling * Version: 1.1 * Date:    August 2000 *  * This class is part of Zork. Zork is a simple, text based adventure game. * * "Room" represents one location in the scenery of the game.  It is  * connected to at most four other rooms via exits.  The exits are labelled * north, east, south, west.  For each direction, the room stores a reference * to the neighbouring room, or null if there is no exit in that direction. */import java.util.ArrayList;import java.util.Set;import java.util.HashMap;import java.util.Iterator;class Room {	private String roomName;	private String description;	private String roomItems;	private String[] collectedKeys = new String[5];	private HashMap<String, Room> exits; // stores exits of this room.	/**	 * Create a room described "description". Initially, it has no exits.	 * "description" is something like "a kitchen" or "an open court yard".	 */	public Room(String description, String roomItems) // Rohan added roomItems	{		this.description = description;		this.roomItems = roomItems;		exits = new HashMap<String, Room>();	}	public Room() {		// default constructor.		roomName = "DEFAULT ROOM";		description = "DEFAULT DESCRIPTION";		roomItems = "DEFAULT ITEMS";		exits = new HashMap<String, Room>();		for (int i =0; i<5; i++){			collectedKeys[i]="";		}	}	public void setExit(char direction, Room r) throws Exception {		String dir = "";		switch (direction) {		case 'E':			dir = "east";			break;		case 'W':			dir = "west";			break;		case 'S':			dir = "south";			break;		case 'N':			dir = "north";			break;		case 'U':			dir = "up";			break;		case 'D':			dir = "down";			break;		default:			throw new Exception("Invalid Direction");		}		exits.put(dir, r);	}	/**	 * Define the exits of this room. Every direction either leads to another	 * room or is null (no exit there).	 */	public void setExits(Room north, Room east, Room south, Room west, Room up,			Room down) {		if (north != null)			exits.put("north", north);		if (east != null)			exits.put("east", east);		if (south != null)			exits.put("south", south);		if (west != null)			exits.put("west", west);		if (up != null)			exits.put("up", up);		if (up != null)			exits.put("down", down);	}	/**	 * Return the description of the room (the one that was defined in the	 * constructor).	 */	public String shortDescription() {		return "Room: " + roomName + "\n\n" + description;	}	/**	 * Return a long description of this room, on the form: You are in the	 * kitchen. Exits: north west	 */	public String longDescription() {		return "Room: " + roomName + "\n\n" + description + "\n" + exitString();	}		public String roomItems() {		return roomItems + "\n" ;	}	public String[] getCollectedKeys() {		return collectedKeys;	}		public String getRoomItems() {		return roomItems;	}	public void UpdatecollectedKeys(String[] collectedKeys, String roomName) {		for (int i = 0; i < 5; i++) {		//	System.out.println("Room no " + i + " "+collectedKeys[i]);			if (collectedKeys[i].isEmpty()) {				collectedKeys[i] = roomName;				//System.out.println("Room no " +i);				break;			}		}	}	public boolean CheckcollectedKeys(String[] collectedKeys, String roomName) {		boolean alreadyPicked = false;		for (int i = 0; i < 5; i++) {			if (collectedKeys[i].contentEquals(roomName))				alreadyPicked = true;			break;		}		return alreadyPicked;	}			public int CheckKeyCounts(String[] collectedKeys) {		int keyCount = 0;		for (int i = 0; i < 5; i++) {			if (!collectedKeys[i].isEmpty())				keyCount = keyCount + 1;			}		return keyCount;	}	/**	 * Return a string describing the room's exits, for example	 * "Exits: north west ".	 */	private String exitString() {		String returnString = "Exits:";		Set keys = exits.keySet();		for (Iterator iter = keys.iterator(); iter.hasNext();)			returnString += " " + iter.next();		return returnString;	}	/**	 * Return the room that is reached if we go from this room in direction	 * "direction". If there is no room in that direction, return null.	 */	public Room nextRoom(String direction) {		return (Room) exits.get(direction);	}	public String getRoomName() {		return roomName;	}	public void getRoomItems(String roomItems) {		this.roomItems = roomItems;	}	public void  getCollectedKeys(String[] collectedKeys ) {		this.collectedKeys = collectedKeys;	}	public void setRoomName(String roomName) {		this.roomName = roomName;	}	public String getDescription() {		return description;	}	public void setDescription(String description) {		this.description = description;	}	public static void setItems(ArrayList<Item> itemList) {		// TODO Auto-generated method stub	}}