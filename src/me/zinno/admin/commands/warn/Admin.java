package me.zinno.admin.commands.warn;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Admin implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		} Player player = (Player) sender;
		
		if (!(player.hasPermission("admin.admin"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
		String msg = "";
		for(int i = 0; i < args.length; i++) {
			msg += args[i];
			msg += " ";
		}
		
		Bukkit.broadcastMessage("§4§l### ADMIN ### " + "§c§k" + msg.toUpperCase());
		Bukkit.broadcastMessage("§4§l### ADMIN ### " + "§c§l" + msg.toUpperCase());
		Bukkit.broadcastMessage("§4§l### ADMIN ### " + "§c§k" + msg.toUpperCase());
		return true;
	}

}
