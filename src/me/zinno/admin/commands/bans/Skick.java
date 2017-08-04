package me.zinno.admin.commands.bans;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class Skick implements CommandExecutor{
	
	private Main plugin;
	public Skick(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		} Player player = (Player) sender;
		if (!(player.hasPermission("admin.skick"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		if(args.length<2) {
			player.sendMessage(ChatColor.RED + "You must specify who you're kicking and why");
			return true;
		}
		
		Player victim = Bukkit.getPlayer(args[0]);
		if(victim==null) {
			player.sendMessage(ChatColor.RED + "That player is not online");
			return true;
		}
		
		String reason = "";
		for(int i = 1; i < args.length; i++) {
			reason += args[i];
			reason += " ";
		}
		
		if(plugin.getConfig().getStringList(args[0].toLowerCase() + ".Kicks")==null) {
			plugin.getConfig().createSection(args[0].toLowerCase() + ".Kicks");
		}
		List<String> list = plugin.getConfig().getStringList(args[0].toLowerCase() + ".Kicks");
		list.add(reason);
		plugin.getConfig().set(args[0].toLowerCase() + ".Kicks", list);
		plugin.saveConfig();
		
		victim.kickPlayer(ChatColor.DARK_RED + reason);
		
		return true;
	}
}
