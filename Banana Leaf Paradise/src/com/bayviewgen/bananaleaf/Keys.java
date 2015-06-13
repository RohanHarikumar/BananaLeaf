package com.bayviewgen.bananaleaf;

public class Keys {
	private static String[] collectedKeys = new String[5];

	public static String[] getCollectedKeys() {

		return collectedKeys;

	}

	public static void UpdatecollectedKeys(String roomName) {
		for (int i = 0; i < 5; i++) {

			if (collectedKeys[i] == null || collectedKeys[i].isEmpty()) {
				collectedKeys[i] = roomName;

				break;
			}
		}
	}

	public static boolean CheckcollectedKeys(String roomName) {
		boolean alreadyPicked = false;

		for (int i = 0; i < 5; i++) {
			if (collectedKeys[i] != null
					&& collectedKeys[i].equalsIgnoreCase(roomName))
				alreadyPicked = true;
			break;
		}

		return alreadyPicked;
	}

	public static int CheckKeyCounts() {
		int keyCount = 0;
		for (int i = 0; i < 5; i++) {

			if (collectedKeys[i] != null)
				keyCount = keyCount + 1;
		}

		return keyCount;
	}

	public static void getCollectedKeys(String[] collectedKeys) {
		collectedKeys = collectedKeys;
	}
}
