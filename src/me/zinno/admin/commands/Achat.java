package me.zinno.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Achat implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		}
		Player player = (Player) sender;

		if (!(player.hasPermission("admin.achat.send"))) {
			player.sendMessage(ChatColor.RED + "Use /report to send a message to staff");
			return true;
		}

		String msg = "";
		for (String part : args) {
			msg += part;
			msg += " ";
		}

		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.hasPermission("admin.achat.view")) {
				all.sendMessage("§5§l[AChat] " + "§r§5" + player.getName() + ": §d" + msg);
			}
		}
		return true;
	}
}