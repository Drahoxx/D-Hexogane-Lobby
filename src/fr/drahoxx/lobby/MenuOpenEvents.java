package fr.drahoxx.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.drahoxx.lobby.servers.Servers;

public class MenuOpenEvents implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getPlayer().getInventory().getItemInMainHand().isSimilar(Item.getItemByName("TeleporterItem").getItemStack())) {
			if(e.getAction() == Action.LEFT_CLICK_AIR || 
				e.getAction() == Action.LEFT_CLICK_BLOCK ||
				e.getAction() == Action.RIGHT_CLICK_AIR ||
				e.getAction() == Action.LEFT_CLICK_BLOCK) {
				e.getPlayer().openInventory(InventoryLoader.getInventory());
			}
		}
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getInventory().getName().equalsIgnoreCase("§6Teleport")) {
			if(e.isLeftClick()) {
				if(InventoryLoader.getServer(e.getSlot()).equalsIgnoreCase("item[SECURITYNAMETAG98547845445877%%%*.,]")) {
					e.setCancelled(true);
				}else {
					e.setCancelled(true);
					Servers s = Servers.getServerByBungeeName(InventoryLoader.getServer(e.getSlot()));
					s.connect((Player) e.getWhoClicked());
				}
			}else if(e.isRightClick()) {
				if(InventoryLoader.getServer(e.getSlot()).equalsIgnoreCase("item[SECURITYNAMETAG98547845445877%%%*.,]")) {
					e.setCancelled(true);
				}else {
					e.setCancelled(true);
					Servers s = Servers.getServerByBungeeName(InventoryLoader.getServer(e.getSlot()));
					s.refreshPlayersCountAndList();
					e.getWhoClicked().sendMessage(s.getName());
					e.getWhoClicked().sendMessage(s.getMotd());
					e.getWhoClicked().sendMessage(s.getNumbersOfPlayerConnected()+" players connected.");
					String str = "";
					for(String ss : s.getPlayerList()) {
						str = str+ss+" ; ";
					}
					e.getWhoClicked().sendMessage("Player list : "+str);
				}
			}
			e.setCancelled(true);
		}
	}
}
