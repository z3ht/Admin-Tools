package me.zinno.admin.events.player;

import org.bukkit.Bukkit;

import java.util.List;

import org.bukkit.BanList.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class PlayerChat implements Listener {
	
	private Main plugin;
	private AsyncPlayerChatEvent event;
	
	public PlayerChat(Main pl) {
		plugin = pl;
	}
	
	
	
	@EventHandler
	public void AsyncPlayerChat(AsyncPlayerChatEvent e) {
		event = e;
		spamFilter();
		chatFilter();
		mute();
		ChatStorage.updateLastMessager(event.getPlayer().getName());
	}
	
	private void mute() {
		if(!(plugin.getConfig().getStringList("Muted").contains(event.getPlayer())))return;
		event.getPlayer().sendMessage(ChatColor.RED + "You have been muted for: " + plugin.getConfig().getString("Mute." + event.getPlayer().getName() + ".reason"));
		event.getPlayer().sendMessage(ChatColor.RED + "Mute Expiration: " + plugin.getConfig().getString("Mute." + event.getPlayer().getName() + ".time"));
		event.setCancelled(true);
	}
	
	private void chatFilter() {
		if(plugin.getConfig().getStringList("Filter.Severe") == null)plugin.getConfig().createSection("Filter.Severe");
		for(String severeWord : plugin.getConfig().getStringList("Filter.Severe")) {
			if(!(event.getMessage().replaceAll(" ", "").replaceAll("/d", "").contains(severeWord)))continue;
			
			if(plugin.getConfig().get(event.getPlayer().getName().toLowerCase() + ".Severe Infraction Number") == null) {
					plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Severe Infraction Number", 0);
			}
			int infractions = plugin.getConfig().getInt(event.getPlayer().getName().toLowerCase() + ".Severe Infraction Number") + 1;
			plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Severe Infraction Number", infractions);
			
			if(plugin.getConfig().get("Filter.Maximum Severe Infractions") == null)plugin.getConfig().set("Filter.Maximum Severe Infractions", -1);
			if(infractions == plugin.getConfig().getInt("Filter.Maximum Severe Infractions")){
				Bukkit.getBanList(Type.NAME).addBan(event.getPlayer().getName(), ChatColor.RED + "Exceeded Maximum Severe Chat Infractions" + ChatColor.DARK_RED +  "(Automatic)", null, "Admin Tools -> Chat Filter");
			}
			event.getPlayer().sendMessage(ChatColor.RED + "Your message has been blocked by the chat filter");
			event.getPlayer().sendMessage(ChatColor.RED + "The attempt has been logged and will have a negative impact on your reputation");
			if(plugin.getConfig().getInt("Filter.Maximum Severe Infractions") - infractions > 0) {
				event.getPlayer().sendMessage(ChatColor.RED + "If you send " + ChatColor.DARK_RED + Integer.toString(plugin.getConfig().getInt("Filter.Maximum Severe Infractions") - infractions) + ChatColor.RED + " more blocked chat messages, you will be muted permanently");
			}
			if(plugin.getConfig().getStringList(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions") == null) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/mute " + event.getPlayer().getName() + " Multiple severe chat infractions");
			}
			List<String> list = plugin.getConfig().getStringList(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions");
			list.add(event.getMessage());
			plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions", list);
			plugin.saveConfig();
			
			event.setCancelled(true);
		}
		if(plugin.getConfig().getStringList("Filter.Mild") == null)plugin.getConfig().createSection("Filter.Mild");
		for(String mildWord : plugin.getConfig().getStringList("Filter.Mild")) {
			if(!(event.getMessage().replaceAll(" ", "").replaceAll("/d", "").contains(mildWord)))continue;
			
			if(plugin.getConfig().getStringList(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions") == null) {
				plugin.getConfig().createSection(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions");
			}
			List<String> list = plugin.getConfig().getStringList(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions");
			list.add(event.getMessage());
			plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Chat_Infractions", list);
			plugin.saveConfig();
			
			event.setCancelled(true);
		}
		
	}

	private void spamFilter() {
		String[] storage = ChatStorage.getLastMessager();
		if(storage == null || storage[0] == null || storage[1] == null)return;
		if(!(event.getPlayer().getName().equals(storage[0]) && event.getPlayer().getName().equals(storage[1])))return;
		
		if(plugin.getConfig().get(event.getPlayer().getName().toLowerCase() + ".Spam Infractions") == null) {
			plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Spam Infractions", 0);
		}
		int infractions = plugin.getConfig().getInt(event.getPlayer().getName().toLowerCase() + ".Spam Infractions") + 1;
		
		if(plugin.getConfig().get("Filter.Maximum Spam Infractions") == null) {
			plugin.getConfig().set("Filter.Maximum Spam Infractions", -1);
		}
		int maxInfractions = plugin.getConfig().getInt("Filter.Maximum Spam Infractions");
		
		if(infractions == maxInfractions) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/mute " + event.getPlayer().getName() + "Spam (Automatic)");
		}
		
		if(plugin.getConfig().getInt("Filter.Maximum Spam Infractions") - infractions > 0) {
			event.getPlayer().sendMessage(ChatColor.RED + "If you send " + ChatColor.DARK_RED + Integer.toString(plugin.getConfig().getInt("Filter.Maximum Spam Infractions") - infractions) + ChatColor.RED + " more blocked chat messages, you will be muted permanently");
		}
		
		plugin.getConfig().set(event.getPlayer().getName().toLowerCase() + ".Spam Infractions", infractions);
		plugin.saveConfig();
		
		event.getPlayer().sendMessage(ChatColor.RED + "You can not send more than two messages in a row. Please be patient.");
		
		event.setCancelled(true);
	}
}
