package me.zinno.admin.commands.bans;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;

public class Sban implements CommandExecutor{
	
	private Main plugin;
	public Sban (Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		} Player player = (Player) sender;
		
		if (!(player.hasPermission("admin.sban"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}
		
		if(args.length<2) {
			player.sendMessage(ChatColor.RED + "You must specify who you're banning and why.");
			return true;
		}
		
		String reason = "";
		for(int i = 1; i < args.length; i++) {
			reason += " ";
			reason += args[i];
		}
		
		Bukkit.getBanList(Type.NAME).addBan(args[0], ChatColor.RED + reason, null, player.getName());
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(args[0])) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.DARK_RED + reason);
			}
		}
		
		if(plugin.getConfig().getStringList(args[0].toLowerCase() + ".Bans")==null) {
			plugin.getConfig().createSection(args[0].toLowerCase() + ".Bans");
		}
		List<String> list = plugin.getConfig().getStringList(args[0].toLowerCase() + ".Bans");
		list.add(reason);
		plugin.getConfig().set(args[0].toLowerCase() + ".Bans", list);
		plugin.saveConfig();
		
		player.sendMessage("§c§l" + args[0] + " has been banned for:" + reason);
		return true;
	}

}
