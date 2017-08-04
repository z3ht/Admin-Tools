package me.zinno.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Strip implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can issue this command!");
			return true;
		} Player player = (Player) sender;
		if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Please enter the player name");
			return true;
		}
		if(!(player.hasPermission("admin.strip"))) {
			player.sendMessage(ChatColor.RED + "You wont be seeing any naked players today...");
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if(target==null) {
			player.sendMessage(ChatColor.RED + "That player is not online");
			return true;
		}
		ItemStack helm = target.getInventory().getHelmet();
		ItemStack chest = target.getInventory().getChestplate();
		ItemStack legs = target.getInventory().getLeggings();
		ItemStack boots = target.getInventory().getBoots();
		
		int available = 4;
		if(helm == null) {
			helm = new ItemStack(Material.AIR);
			available -= 1;
		}if (chest == null) {
			chest = new ItemStack(Material.AIR);
			available -= 1;
		}if (legs == null) {
			legs = new ItemStack(Material.AIR);
			available -= 1;
		}if (boots == null) {
			boots = new ItemStack(Material.AIR);
			available -= 1;
		}
		
		for(ItemStack item : player.getInventory().getContents()){
            if(item == null && item != player.getInventory().getItemInOffHand()){
                available-=1;
            }
        }
		if(available>0) {
			player.sendMessage(ChatColor.RED + "The targets inventory is full");
			return true;
		}
		
		target.getInventory().addItem(helm);
		target.getInventory().addItem(chest);
		target.getInventory().addItem(legs);
		target.getInventory().addItem(boots);
		
		target.getInventory().setHelmet(null);
		target.getInventory().setChestplate(null);
		target.getInventory().setLeggings(null);
		target.getInventory().setBoots(null);
		player.sendMessage(ChatColor.GREEN + args[0] + " was stripped");
		return true;
	}
}