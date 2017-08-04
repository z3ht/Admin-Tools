package me.zinno.admin.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ToTeleport implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command!");
			return true;
		} Player player = (Player) sender;
		if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Please enter only the name of the player you would like to visit");
			return true;
		}else if(!(player.hasPermission("admin.tp"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to zoom through space and time");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.RED + "That player is not online");
			return true;
		}
		
		Location tolocation = target.getLocation();
		int x = tolocation.getBlockX();
		int y = tolocation.getBlockY();
		int z = tolocation.getBlockZ();
		Location teleport = new Location(target.getWorld(), x, y, z);
		player.sendMessage(ChatColor.GREEN + "Teleporting...");
		player.teleport(teleport);
		return true;
	}
}
