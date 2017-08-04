package me.zinno.admin.commands.bans;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Unban implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		} Player player = (Player) sender;
		
		if (!(player.hasPermission("admin.unban"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
		if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /unban <Player-Name>");
			return true;
		}
		
		player.sendMessage(ChatColor.GREEN + args[0] + " has been unbanned");
		return true;
	}

}