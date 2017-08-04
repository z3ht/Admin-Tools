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

public class Rupdate implements CommandExecutor {
	
	private Main plugin;
	public Rupdate(Main pl) {
		plugin = pl;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command");
		} Player player = (Player) sender;
		if(!(player.hasPermission("admin.report.view"))) {
			player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
			return true;
		}else if (args.equals(null) || args.length != 1) {
			player.sendMessage(ChatColor.RED + "Usage: /rdelete <Report Number>");
		}else if (plugin.getConfig().getStringList("Report List") == null || plugin.getConfig().getStringList("Report List").isEmpty()) {
			player.sendMessage(ChatColor.RED + "Their are no active reports");
			return true;
		}
		
		int id;
		try {
			id = Integer.parseInt(args[0]) - 1;
		}catch(NumberFormatException ex) {
			player.sendMessage(ChatColor.RED + "Usage: /rdelete <Report Number>");
			return true;
		}
		
		if (plugin.getConfig().getStringList("Report List").get(id) == null) {
			player.sendMessage(ChatColor.RED + "No report found with the given ID");
			return true;
		}
		
		List<String> rList = plugin.getConfig().getStringList("Report List");
		rList.remove(id);
		plugin.getConfig().set("Report List", rList);
		plugin.saveConfig();
		
		for(int counter = 0; counter <=25; counter+=1) {
			player.sendMessage("");
		}
		
		sender.sendMessage(ChatColor.BLUE + "Active Reports:");
		int numReports = plugin.getConfig().getStringList("Report List").size();
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
		player.sendMessage(ChatColor.GREEN + "Active Reports updated");
		return true;
	}
}
