package me.zinno.admin.events.player;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class PlayerJoin implements Listener {
	
	private Main plugin;
	private PlayerLoginEvent event;
	public PlayerJoin(Main pl) {
		plugin = pl;
	}
	
	@EventHandler
	public void PlayerJoinEvent(PlayerLoginEvent e) {
		event = e;
		getPlayerData();
	}

	private void getPlayerData() {
		getPlayerUUID();
		getPlayerJoinDate();
		
	}

	private void getPlayerJoinDate() {
		String playerName = event.getPlayer().getName().toLowerCase();
		if(!(plugin.getConfig().getString(playerName + ".First Login") == null))return;
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
		plugin.getConfig().set(playerName + ".First Login", format.format(now));
		plugin.saveConfig();
	}

	private void getPlayerUUID() {
		String playerName = event.getPlayer().getName().toLowerCase();
		if(!(plugin.getConfig().getString(playerName + ".UUID")==null))return;
		String playerID = event.getPlayer().getUniqueId().toString();
		for(OfflinePlayer players : Bukkit.getOfflinePlayers()) {
			try {
				if(playerID == plugin.getConfig().getString(players.getName().toLowerCase() + ".UUID")) {
					event.setKickMessage(ChatColor.RED + "Some plugins on this server do not support name swapping");
				}
			} catch (Exception e) {}
		}
		plugin.getConfig().set(playerName + ".UUID", playerID);
		plugin.saveConfig();
	}
}
