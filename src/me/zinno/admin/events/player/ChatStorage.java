package me.zinno.admin.events.player;

public final class ChatStorage {
	
	private static String[] lastMessager = new String[2];
	
	public static String[] getLastMessager() {
		return lastMessager;
	}
	
	public static String[] updateLastMessager(String playerName) {
		if(lastMessager[0] != null) {
			lastMessager[1] = lastMessager[0];
		}
		lastMessager[0] = playerName;
		return lastMessager;
	}
}
