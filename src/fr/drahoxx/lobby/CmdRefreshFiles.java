package fr.drahoxx.lobby;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.drahoxx.lobby.servers.Servers;

public class CmdRefreshFiles implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Item.loadAllItems();
		Main.main.registerServers();
		InventoryLoader.generateInventory();
		for(Servers s : Main.serversList) {
			s.refreshPlayersCountAndList();
		}
		sender.sendMessage("It's working !");
		return true;
	}

}
