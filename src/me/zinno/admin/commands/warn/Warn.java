package me.zinno.admin.commands.warn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class Warn implements CommandExecutor {
	
	private Main plugin;
	public Warn(Main pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender.hasPermission("admin.warn"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if (args.length <= 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /warn <Player-Name> <Warning>");
			return true;
		}
		
		String reason = "";
		for(int c = 1; c != args.length; c+=1) {
			reason += args[c] + " ";
		}
		
		if(plugin.getConfig().getStringList(args[0].toLowerCase() + ".Warns") == null) {
			plugin.getConfig().createSection(args[0].toLowerCase() + ".Warns");
		}
		List<String> list = plugin.getConfig().getStringList(args[0].toLowerCase() + ".Warns");
		list.add(reason);
		plugin.getConfig().set(args[0].toLowerCase() + ".Warns", list);
		
		try {
			for(int i=0; i<=3; i+=1)Bukkit.getPlayer(args[0]).sendMessage("");
			Bukkit.getPlayer(args[0]).sendMessage(ChatColor.DARK_RED + "### WARNING: ###");
			Bukkit.getPlayer(args[0]).sendMessage(ChatColor.RED + sender.getName() + " has issued YOU an official warning:");
			Bukkit.getPlayer(args[0]).sendMessage(ChatColor.DARK_PURPLE + reason);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "The player is not online, but the report was logged.");
			return true;
		}
		
		plugin.saveConfig();
		
		sender.sendMessage(ChatColor.GREEN + "The offender has been warned");
		return true;
	}
}
