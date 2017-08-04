package me.zinno.admin.commands.reports;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zinno.admin.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Ractive implements CommandExecutor {
	
	private Main plugin;
	public Ractive(Main pl) {
		plugin = pl;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can access this command");
			return true;
		} Player player = (Player) sender;
		if(!(player.hasPermission("admin.report.view"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if (args.equals(null) || args.length != 0) {
			player.sendMessage(ChatColor.RED + "Usage: /ractive");
		}else if (plugin.getConfig().getStringList("Report List") == null || plugin.getConfig().getStringList("Report List").isEmpty()) {
			player.sendMessage(ChatColor.RED + "Their are no active reports");
			return true;
		}
		
		sender.sendMessage(ChatColor.BLUE + "Active Reports:");
		int numReports = plugin.getConfig().getStringList("Report List").size();
		List<String> rList = plugin.getConfig().getStringList("Report List");
		ComponentBuilder info = new ComponentBuilder("Click to delete report").color(ChatColor.DARK_PURPLE);
		for(int counter = 1; counter != numReports + 1; counter+=1) {
			TextComponent item = new TextComponent(counter + ": " + rList.get(counter-1));
			item.setColor(ChatColor.AQUA);
			item.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, info.create()));
			if(numReports <= 1) {
				item.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rdelete " + counter));
			} else {
				item.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rupdate " + counter));
			}
			player.spigot().sendMessage(item);
		}
		return true;
	}
}
