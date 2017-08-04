/* 
 * Things to Evaluate:
 * - Overall Score 
 * - Staff Notes (done)
 * - Ban History
 * - Kick History
 * - Report History (done)
 * - Chat Filter History
 * - Time Spent Playing (done)
 */

package me.zinno.admin.commands.evaluate;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Evaluate implements CommandExecutor {
	
	private Main plugin;
	private Player player;
	private OfflinePlayer offender;
	
	public Evaluate (Main pl) {
		plugin = pl;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can access this command");
			return true;
		} player = (Player) sender;
		if(!(player.hasPermission("admin.evaluate"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if(args == null || args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /evaluate <Player-Name>");
			return true;
		}
		try {
			UUID offenderID = UUID.fromString(plugin.getConfig().getString(args[0].toLowerCase() + ".UUID"));
			try {
				offender = Bukkit.getPlayer(offenderID);
			} catch (Exception e) {
				offender = Bukkit.getOfflinePlayer(offenderID);
			}
		}catch (Exception e) {
			player.sendMessage(ChatColor.RED + "The offender could not be found.");
			return true;
		}
		
		for(int i = 0; i<=25; i+=1)player.sendMessage("");
		String nameProper = offender.getName().substring(0, 1).toUpperCase() + offender.getName().substring(1);
		player.sendMessage(ChatColor.GOLD + nameProper + "'s Full Record:");
		getPlayerRecord();
		return true;
	}
	
	private boolean getPlayerRecord() {
		translateConfig("string", "UUID");
		translateConfig("string", "First Login");
		translateConfig("stringList", "Warns");
		translateConfig("stringList", "Reports");
		translateConfig("stringList", "Violations");
		translateConfig("stringList", "Chat_Infractions");
		translateConfig("int", "Spam Infractions");
		translateConfig("stringList", "Bans");
		translateConfig("stringList", "Kicks");
		player.sendMessage(ChatColor.DARK_AQUA + "Playtime: " + getFormalTime((long) plugin.getConfig().getLong(offender.getName().toLowerCase() + ".Playtime")/20));
		return true;
	}
	
	private boolean translateConfig(String type, String name) {
		if(plugin.getConfig().get(offender.getName().toLowerCase() + "." + name) == null) {
			player.sendMessage(ChatColor.DARK_AQUA + name.replaceAll("_", " ") + ": 0");
			return false;
		}
		switch (type) {
		case "int":
			player.sendMessage(ChatColor.DARK_AQUA + name.replaceAll("_", " ") + ": " + Integer.toString(plugin.getConfig().getInt(offender.getName().toLowerCase() + "." + name)));
			break;
		case "stringList":
			TextComponent title = new TextComponent(name.replaceAll("_", " ") + ": " + plugin.getConfig().getStringList(offender.getName().toLowerCase() + "." + name).size());
			title.setColor(ChatColor.DARK_AQUA);
			ComponentBuilder hover = new ComponentBuilder("Click to view the detailed list").color(ChatColor.DARK_PURPLE);
			title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
			title.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/evallist " + offender.getName().toLowerCase() + "." + name));
			player.spigot().sendMessage(title);
			break;
		case "string":
			player.sendMessage(ChatColor.DARK_AQUA + name.replaceAll("_", " ") + ": " + plugin.getConfig().getString(offender.getName().toLowerCase() + "." + name));
			break;
		default:
			player.performCommand("/report Plugin Error: Unexpected type @ admin/evaluate/translateConfig");
			player.sendMessage(ChatColor.DARK_RED + "### ERROR: ###");
			player.sendMessage(ChatColor.RED + "A report has been filed. Investigations will begin shortly");
		}
		return true;
	}
	
	private String getFormalTime(long seconds) {
		int days = 0;
		while(seconds >= 8640) {
			days += 1;
			seconds-=8640;
		}
		int hours = 0;
		while(seconds >= 3600) {
			hours += 1;
			seconds-=3600;
		}
		int mins = 0;
		while(seconds >= 60) {
			mins +=1;
			seconds -= 60;
		}
		return days + " day(s), " + hours + " hour(s), " + mins + " minute(s), and " + seconds + " second(s).";
	}
}
