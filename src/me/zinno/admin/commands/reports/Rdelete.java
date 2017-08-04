package me.zinno.admin.commands.reports;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class Rdelete implements CommandExecutor {
	
	private Main plugin;
	public Rdelete(Main pl) {
		plugin = pl;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender.hasPermission("admin.report.view"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if (args.equals(null) || args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /rdelete <Report Number>");
		}else if (plugin.getConfig().getStringList("Report List") == null || plugin.getConfig().getStringList("Report List").isEmpty()) {
			sender.sendMessage(ChatColor.RED + "Their are no active reports");
			return true;
		}
		
		if(args[0] == "all") {
			plugin.getConfig().set("Report List", null);
			plugin.saveConfig();
			sender.sendMessage(ChatColor.GREEN + "All reports have been deleted");
			return true;
		}
		
		int id;
		try {
			id = Integer.parseInt(args[0]) - 1;
		}catch(NumberFormatException ex) {
			sender.sendMessage(ChatColor.RED + "Usage: /rdelete <Report Number>");
			return true;
		}
		
		if (plugin.getConfig().getStringList("Report List").get(id) == null) {
			sender.sendMessage(ChatColor.RED + "No report found with the given ID");
			return true;
		}
		
		List<String> rList = plugin.getConfig().getStringList("Report List");
		rList.remove(id);
		plugin.getConfig().set("Report List", rList);
		plugin.saveConfig();
		
		sender.sendMessage(ChatColor.GREEN + "The report has been deleted");
		
		return true;
	}
}
