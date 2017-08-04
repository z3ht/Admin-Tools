package me.zinno.admin.commands.evaluate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class EvalList implements CommandExecutor {
	
	private Main plugin;
	
	public EvalList (Main pl) {
		plugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can access this command");
			return true;
		} Player player = (Player) sender;
		if(!(player.hasPermission("admin.evaluate"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if (args == null || args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /evalList <Player-Name>");
			return true;
		}
		
		player.sendMessage("");
		String title = args[0].replaceAll("\\.", " ").replaceAll("_", " ");
		player.sendMessage(ChatColor.LIGHT_PURPLE + title.substring(0, 1).toUpperCase() + title.substring(1) + ":");
		for(String s : plugin.getConfig().getStringList(args[0])) {
			player.sendMessage(ChatColor.AQUA + "  - " + s);
		}
		return true;
	}

}
