package com.bayviewgen.bananaleaf;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

class Item {
	private String[] collectedKeys = new String[5];

	public String[] getCollectedKeys() {

		for (int i = 0; i < 5; i++) {
			collectedKeys[i] = "Room " + i;
		}
		return collectedKeys;

	}

	public void UpdatecollectedKeys(String[] collectedKeys, String roomName) {
		for (int i = 0; i < 5; i++) {

			if (collectedKeys[i].isEmpty()) {
				collectedKeys[i] = roomName;

				break;
			}
		}
	}

	public boolean CheckcollectedKeys(String roomName) {
		boolean alreadyPicked = false;

		return alreadyPicked;
	}

	public int CheckKeyCounts(String[] collectedKeys) {
		int keyCount = 0;
		for (int i = 0; i < 5; i++) {
			if (!collectedKeys[i].isEmpty())
				keyCount = keyCount + 1;
		}

		return keyCount;
	}

	public void getCollectedKeys(String[] collectedKeys) {
		this.collectedKeys = collectedKeys;
	}
}
