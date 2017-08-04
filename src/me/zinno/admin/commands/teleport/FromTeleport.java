package me.zinno.admin.commands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class FromTeleport implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command!");
			return true;
		} Player player = (Player) sender;
		if(!(player.hasPermission("admin.tphere"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to send others through space and time");
			return true;
		} else if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Please enter only the name of the player you would like to teleport");
			return true;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.RED + "That player is not online");
			return true;
		}
		Location tolocation = player.getLocation();
		int x = tolocation.getBlockX();
		int y = tolocation.getBlockY();
		int z = tolocation.getBlockZ();
		Location teleport = new Location(player.getWorld(), x, y, z);
		player.sendMessage(ChatColor.GREEN + "Teleporting player...");
		target.teleport(teleport);
		return true;
	}
}
