package me.zinno.admin.commands.reports;

import java.util.List;

import org.bukkit.Bukkit;
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

public class Report implements CommandExecutor {
	
	private Main plugin;
	public Report(Main pl) {
		plugin = pl;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command!");
			return true;
		}
		Player player = (Player) sender;
		if(plugin.getConfig().getStringList("Report Bans").contains(player.getName().toLowerCase())) {
			player.sendMessage(ChatColor.RED + "You have been banned from sending reports");
			return true;
		}
		
		String reason = "";
		for(String part : args) {
			reason += part;
			reason += " ";
		}
		
		if(plugin.getConfig().getStringList("Report List") == null)plugin.getConfig().createSection("Report List");
		List<String> rList = plugin.getConfig().getStringList("Report List");
		rList.add(player.getName() + " has issued report: " + reason);
		plugin.getConfig().set("Report List", rList);
		
		TextComponent report = new TextComponent(player.getName());
		report.setColor(ChatColor.GREEN);
		report.setBold(true);
		TextComponent text = new TextComponent(" has issued report: ");
		text.setColor(ChatColor.DARK_GREEN);
		report.addExtra(text);
		report.addExtra(reason);
		ComponentBuilder tp = new ComponentBuilder("Teleport to the player").color(ChatColor.DARK_PURPLE);
		report.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tp.create()));
		report.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
		
		TextComponent reportList = new TextComponent("View Active Reports?");
		reportList.setColor(ChatColor.GOLD);
		reportList.setBold(true);
		ComponentBuilder info = new ComponentBuilder("Click to view Active Reports").color(ChatColor.DARK_PURPLE);
		reportList.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, info.create()));
		reportList.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ractive"));
		
		for (Player staff : Bukkit.getOnlinePlayers()) {
			if (staff.hasPermission("admin.report.view")) {
				staff.spigot().sendMessage(report);
				staff.sendMessage("");
				staff.spigot().sendMessage(reportList);
			}
		}
		
		if(plugin.getConfig().getStringList(player.getName().toLowerCase() + ".Reports") == null) {
			plugin.getConfig().createSection(player.getName().toLowerCase() + ".Reports");
		}
		
		List<String> list = plugin.getConfig().getStringList(player.getName().toLowerCase() + ".Reports");
		list.add(reason);
		plugin.getConfig().set(player.getName().toLowerCase() + ".Reports", list);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			for (String s : args) {
				if(s.equalsIgnoreCase(p.getName())) {
					if(plugin.getConfig().getStringList(s.toLowerCase() + ".Violations") == null) {
						plugin.getConfig().createSection(s.toLowerCase() + ".Violations");
					}
					list = plugin.getConfig().getStringList(s.toLowerCase() + ".Violations");
					list.add(reason);
					plugin.getConfig().set(s.toLowerCase() + ".Violations", list);
				}
			}
		}
		
		plugin.saveConfig();
		
		player.sendMessage(ChatColor.GREEN + "Your report has been issued!");
		return true;
	}
}