package com.august.hearthunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Event implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent Event){
        Player player = (Player) Event.getPlayer();
        if(!player.hasPlayedBefore()) {
            player.setResourcePack("https://cdn.discordapp.com/attachments/855714615244357663/929733969249992735/HeartHunt_Pack.zip");
        }
    }
    @EventHandler
    public void onPlayerDie(PlayerDeathEvent Event) {
        Player player = (Player) Event.getEntity();
        Player killer = (Player) Event.getEntity().getKiller();
        Double playerHealth = player.getMaxHealth();

        if(killer == null) return;
        Double killerHealth = killer.getMaxHealth();
        if(playerHealth <= 2f){
            //Player die
            player.setMaxHealth(1.0);
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " died to " + ChatColor.RED + player.getName() + ChatColor.AQUA + " to revive that player use" + ChatColor.GOLD + " Totem of Reviving"+ ChatColor.AQUA +"!");
        }

        if (killerHealth/2 >= 30){
            //The health is limited
            player.setMaxHealth(playerHealth-2);
            ItemStack item = new ItemStack(Material.FIREWORK_STAR, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1004);
            meta.setDisplayName(ChatColor.RED + "Heart");

            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD+ "[Right Click]"+ ChatColor.WHITE+" to add the heart.");
            meta.setLore(lore);
            item.setItemMeta(meta);

            killer.getInventory().addItem(item);
            killer.sendMessage("Your max hearts is limited, you will get the heart as item instead.");
            return;
        }
        killer.setMaxHealth(killerHealth+2);
        player.setMaxHealth(playerHealth-2);
        killer.sendMessage(ChatColor.GOLD + "You have got "+ChatColor.RED+"1"+ChatColor.WHITE+" heart from "+ChatColor.GOLD+player.getName());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent Event) {
        Player player = Event.getPlayer();
        Player killer = Event.getPlayer().getKiller();
        Double healthnumber = player.getMaxHealth();
        if(killer == null) return;
            if (player.getMaxHealth() == 1.0) {
                player.setGameMode(GameMode.SPECTATOR);
                player.getInventory().clear();
                //Send Player A Message
                player.sendTitle("You lost all your hearth!", "You can't respawn now!", 0, 100, 0);
            }
                player.sendMessage(ChatColor.GOLD + "You lost 1 heart by " + killer.getName() + "! so now you have " + ChatColor.RED + String.valueOf(healthnumber / 2) + ChatColor.GOLD + "heart(s) !");
        }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent Event) {
        Player plr = Event.getPlayer();
        if (Event.getHand() == EquipmentSlot.HAND) return;
        if (Event.getAction() == Action.RIGHT_CLICK_AIR || Event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = plr.getInventory().getItemInMainHand();
            if(item == null) return;
            if(item.getItemMeta().hasCustomModelData()){
                if (item.getType() == Material.FIREWORK_STAR && item.getItemMeta().getCustomModelData() == 1004) {
                    if (plr.getMaxHealth() >= 60f) {
                        plr.sendMessage(ChatColor.GOLD + "Your heart reached the limit!");
                        return;
                    }
                    plr.setMaxHealth(plr.getMaxHealth() + 2);
                    plr.sendMessage("Your heart has increased to " + ChatColor.GOLD + String.valueOf((plr.getMaxHealth() / 2)) + ChatColor.WHITE + "!");
                    item.setAmount(item.getAmount() - 1);
                }
            if (item.getItemMeta().getCustomModelData() == 1005 && item.getType() == Material.TOTEM_OF_UNDYING) {
                Inventory ui = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Select player to revive!");
                int index = 0;
                for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
                    Player player = (Player) Bukkit.getOnlinePlayers().toArray()[i];

                    if (!player.getName().equals(plr.getName()) && player.getGameMode() == GameMode.SPECTATOR && player.getMaxHealth() == 1f) {
                        ItemStack playerItem = new ItemStack(Material.PLAYER_HEAD);
                        ItemMeta meta = playerItem.getItemMeta();
                        assert meta != null;
                        meta.setDisplayName(player.getName());
                        List<String> lore = new ArrayList<String>();
                        lore.add("Click to Revive!");
                        meta.setLore(lore);
                        playerItem.setItemMeta(meta);
                        ui.setItem(index, playerItem);
                        index += 1;
                    }
                }
                plr.openInventory(ui);
            }
            }
                }
            }


    @EventHandler
    public void onPlayerInventory(InventoryClickEvent Event) {
        Player plr = (Player) Event.getWhoClicked();
        if (Event.getView().getTitle().equalsIgnoreCase(ChatColor.GOLD + "Select player to revive!")) {
            String playerName = Event.getCurrentItem().getItemMeta().getDisplayName();
            Player targetPlayer = Bukkit.getPlayer(playerName);
            if (targetPlayer == null)
                return;
            targetPlayer.setGameMode(GameMode.SURVIVAL);
            targetPlayer.setMaxHealth(2f);
            targetPlayer.teleport(plr.getLocation());
            Event.getView().close();
            plr.getInventory().getItemInMainHand().setAmount(-1);
        }
    }

    @EventHandler
    public void sadwasfefg(EntityToggleGlideEvent Event) {
        Player plr = (Player) Event.getEntity();
        plr.sendMessage(ChatColor.RED + "Elytra is disabled in this server!");
        Event.setCancelled(true);
    }
    @EventHandler
    public void checkItemCraftEvent(PrepareItemCraftEvent Event){
        int Heart = 0;
        int Totem = 0;
        int NetherStar = 0;

        ItemStack[] items = Event.getInventory().getMatrix();
        for (int i = 0; i < Event.getInventory().getSize(); i++) {
            ItemStack item = Event.getView().getItem(i);

            if (item != null){
                ItemMeta meta = item.getItemMeta();
                if (meta != null){
                    if (meta.getDisplayName().equalsIgnoreCase(ChatColor.RED+"Heart") && item.getType() == Material.FIREWORK_STAR)
                    Heart+=1;
                    if (item.getType() == Material.TOTEM_OF_UNDYING)
                        Totem+=1;
                    if (item.getType() == Material.NETHER_STAR)
                        NetherStar+=1;
                }
            }
        }

        if(Heart != 1 || Totem != 1 || NetherStar != 1)
            return;

        ItemStack rvt = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        ItemMeta metar = rvt.getItemMeta();
        metar.setCustomModelData(1005);
        metar.setDisplayName(ChatColor.GOLD + "Totem of Reviving");
        List<String> sus = new ArrayList<>();
        sus.add(ChatColor.GOLD + "[Right Click]" + ChatColor.WHITE + " to revive the player (The player must be online).");
        metar.setLore(sus);
        rvt.setItemMeta(metar);

        Event.getInventory().setResult(rvt);

    }
}
