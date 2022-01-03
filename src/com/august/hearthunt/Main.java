package com.august.hearthunt;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new Event(), this);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "HeartHunt is ready!");
        ItemStack item = new ItemStack(Material.FIREWORK_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1004);
        meta.setDisplayName(ChatColor.RED + "Heart");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "[Right Click]" + ChatColor.WHITE + " to add the heart.");
        meta.setLore(lore);

        item.setItemMeta(meta);
        NamespacedKey e = new NamespacedKey(this, this.getDescription().getName() + "HeartElytra");
        ShapedRecipe f = new ShapedRecipe(e, item);
        f.shape("xsx", "sds", "xsx");
        f.setIngredient('x', Material.NETHERITE_INGOT);
        f.setIngredient('s', Material.DIAMOND_BLOCK);
        f.setIngredient('d', Material.ELYTRA);
        getServer().addRecipe(f);
        getCommand("withdraw").setExecutor(new Command());
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Oh no! HeartHunt is disabling!");
    }
}