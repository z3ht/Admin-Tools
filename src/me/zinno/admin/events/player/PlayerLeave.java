package me.zinno.admin.events.player;

import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.zinno.admin.Main;

public class PlayerLeave implements Listener {
	
	private Main plugin;
	private PlayerQuitEvent event;
	
	public PlayerLeave(Main pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void PlayerLeaveEvent(PlayerQuitEvent e) {
		event = e;
		timePlayed();
		
	}

	private void timePlayed() {
		String playerName = event.getPlayer().getName().toLowerCase();
		long timePlayed = event.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK);
		plugin.getConfig().set(playerName + ".Playtime", timePlayed);
		plugin.saveConfig();
	}

}
