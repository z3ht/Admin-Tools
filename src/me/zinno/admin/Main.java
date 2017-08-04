package me.zinno.admin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.zinno.admin.commands.Achat;
import me.zinno.admin.commands.Strip;
import me.zinno.admin.commands.bans.Gban;
import me.zinno.admin.commands.bans.Gkick;
import me.zinno.admin.commands.bans.Sban;
import me.zinno.admin.commands.bans.Skick;
import me.zinno.admin.commands.bans.Unban;
import me.zinno.admin.commands.evaluate.EvalList;
import me.zinno.admin.commands.evaluate.Evaluate;
import me.zinno.admin.commands.reports.Ractive;
import me.zinno.admin.commands.reports.Rblock;
import me.zinno.admin.commands.reports.Rdelete;
import me.zinno.admin.commands.reports.Report;
import me.zinno.admin.commands.reports.Rpardon;
import me.zinno.admin.commands.reports.Rupdate;
import me.zinno.admin.commands.teleport.FromTeleport;
import me.zinno.admin.commands.teleport.ToTeleport;
import me.zinno.admin.commands.warn.Admin;
import me.zinno.admin.commands.warn.Mod;
import me.zinno.admin.commands.warn.Warn;
import me.zinno.admin.events.player.PlayerChat;
import me.zinno.admin.events.player.PlayerJoin;
import me.zinno.admin.events.player.PlayerLeave;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	public void onEnable() {
		registerConfig();
		registerCommands();
		registerEvents();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {

	}
	
	public void registerCommands() {
		getCommand("admin").setExecutor(new Admin());
		getCommand("mod").setExecutor(new Mod());
		getCommand("warn").setExecutor(new Warn(this));
		getCommand("gban").setExecutor(new Gban(this));
		getCommand("sban").setExecutor(new Sban(this));
		getCommand("skick").setExecutor(new Skick(this));
		getCommand("gkick").setExecutor(new Gkick(this));
		getCommand("unban").setExecutor(new Unban());
		getCommand("tp").setExecutor(new ToTeleport());
		getCommand("tphere").setExecutor(new FromTeleport());
		getCommand("report").setExecutor(new Report(this));
		getCommand("rblock").setExecutor(new Rblock(this));
		getCommand("rpardon").setExecutor(new Rpardon(this));
		getCommand("ractive").setExecutor(new Ractive(this));
		getCommand("rdelete").setExecutor(new Rdelete(this));
		getCommand("rupdate").setExecutor(new Rupdate(this));
		getCommand("strip").setExecutor(new Strip());
		getCommand("achat").setExecutor(new Achat());
		getCommand("evaluate").setExecutor(new Evaluate(this));
		getCommand("evallist").setExecutor(new EvalList(this));
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerJoin(this), this);
		pm.registerEvents(new PlayerLeave(this), this);
		pm.registerEvents(new PlayerChat(this), this);
	}
	
	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	private ArrayList<Player> vanished = new ArrayList<Player>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vanish")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Only players can use this command!");
				return true;
			}
			Player player = (Player) sender;
			if (!(player.hasPermission("admin.vanish"))) {
				player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return true;
			} else if (args.length != 0) {
				player.sendMessage(ChatColor.RED + "This command takes no arguments");
				return true;
			}
			if (!(vanished.contains(player))) {
				player.sendMessage(ChatColor.GREEN + "You are now invisible");
				player.setPlayerListName(null);
				for (Player players : Bukkit.getOnlinePlayers()) {
					if(!(players.hasPermission("vanish.see"))) {
						players.hidePlayer(player);
					}
				}
				vanished.add(player);
				return true;
			} else {
				player.sendMessage(ChatColor.GREEN + "You are no longer invisible!");
				player.setPlayerListName(player.getName());
				for (Player players : Bukkit.getOnlinePlayers()) {
					players.showPlayer(player);
				}
				vanished.remove(player);
				return true;
			}
		}
		return true;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (Player invisPlayer : vanished) {
			if (!(event.getPlayer().hasPermission("vanish.see"))) {
				event.getPlayer().hidePlayer(invisPlayer);
			}
		}
	}
}
