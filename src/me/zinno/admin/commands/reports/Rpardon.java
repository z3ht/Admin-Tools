package me.zinno.admin.commands.reports;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class Rpardon implements CommandExecutor {
	
	private Main plugin;
	public Rpardon(Main pl) {
		plugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender.hasPermission("admin.rpardon"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
		}
		if(args.length!=1) {
			sender.sendMessage(ChatColor.RED + "Only enter the player you are banning");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target==null) {
			sender.sendMessage(ChatColor.RED + "The player could not be found.");
			return true;
		}
		if(plugin.getConfig().getStringList("Report Bans").equals(null))plugin.getConfig().createSection("Report Bans");
		List<String> rblock = plugin.getConfig().getStringList("Report Bans");
		rblock.remove(args[0].toLowerCase());
		plugin.getConfig().set("Report Bans", rblock);
		plugin.saveConfig();
		sender.sendMessage(ChatColor.GREEN + "The player can now send reports");
		return true;
	}
}
