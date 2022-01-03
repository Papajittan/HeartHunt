package com.august.hearthunt;

import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {
    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String string, String[] args) {
        var player = (Player) commandSender;
        var cmd = command.getName();
        if (!(commandSender instanceof Player)) return false;
        if (cmd.equalsIgnoreCase("withdraw")) {
            if (args.length <= 0) {
                player.sendMessage(ChatColor.AQUA + "Enter the amount you want to withdraw!");
                return true;
            }
            if (!isStringInt(args[0])) {
                player.sendMessage(ChatColor.RED + "Please enter the number only!");
                return true;
            }
            if(player.getMaxHealth()/2 - Integer.parseInt(args[0]) > 0){
                if(player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(ChatColor.AQUA + "Sorry but you can't use this command while inventory is full!");
                    return true;
                }
                    ItemStack item = new ItemStack(Material.FIREWORK_STAR, Integer.parseInt(args[0]));
                    ItemMeta meta = item.getItemMeta();
                    meta.setCustomModelData(1004);
                    meta.setDisplayName(ChatColor.RED + "Heart");
                    List<String> lore = new ArrayList<String>();
                    lore.add(ChatColor.GOLD + "[Right Click]" + ChatColor.WHITE + " to add the heart.");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    player.getInventory().addItem(item);
                    player.setMaxHealth(player.getMaxHealth() - Integer.parseInt(args[0]) * 2);
                    player.sendMessage(ChatColor.GOLD + "You successfully withdraw " +ChatColor.RED + args[0]+ ChatColor.GOLD + "!");
                return true;
            }
            player.sendMessage(ChatColor.RED + "Please enter the amount that you won't die!");
        }
        return true;
    }
}
