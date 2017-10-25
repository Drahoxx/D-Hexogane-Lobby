package fr.drahoxx.lobby.servers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.drahoxx.lobby.Item;
import fr.drahoxx.lobby.Main;

public class Servers {
	private String id; //done
	
	private String name; //done
	private String bungeeName; //done
	
	private String motd; //done
	
	private Integer numbersOfPlayerConnected; //done
	private String[] playerList; //done
	
	private ItemStack item;
	
	
	
	public ItemStack getItem() {
		return item;
	}


	public String[] getPlayerList() {
		return playerList;
	}


	public void setPlayerList(String[] playerList) {
		this.playerList = playerList;
	}


	public String getId() {
		return id;
	}

	
	public Integer getNumbersOfPlayerConnected() {
		return numbersOfPlayerConnected;
	}



	public void setNumbersOfPlayerConnected(Integer numbersOfPlayerConnected) {
		this.numbersOfPlayerConnected = numbersOfPlayerConnected;
	}



	public String getName() {
		return name;
	}



	public String getBungeeName() {
		return bungeeName;
	}



	public String getMotd() {
		return motd;
	}



	public Servers(String id) {
		this.name = get(ServeursDataType.NAME, id);
		this.bungeeName = get(ServeursDataType.BUNGEE_NAME, id);
		this.motd = get(ServeursDataType.MOTD, id);
		String itemName = get(ServeursDataType.ITEM, id);
		Item item = Item.getItemByName(itemName);
		ItemStack its = item.getItemStack();
		ItemMeta itm = its.getItemMeta();
		itm.setDisplayName(this.getName());
		its.setItemMeta(itm);
		this.item = its;
		this.id = id;
	}
	
	
	
	public void connect(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Connect");
		  out.writeUTF(bungeeName);

		  p.sendPluginMessage(Main.main, "BungeeCord", out.toByteArray());
	}
	public void refreshPlayersCountAndList() {
		try {
			refreshPlayerList();
			refreshPlayerCount();
		} catch (Exception e) {
			System.err.println("Error : server informations are false");;
		}
		
	}
	
	private void refreshPlayerCount() throws Exception{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(bungeeName);
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		player.sendPluginMessage(Main.main, "BungeeCord", out.toByteArray());
	}
	
	private void refreshPlayerList() throws Exception{
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerList");
		out.writeUTF(bungeeName);
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		player.sendPluginMessage(Main.main, "BungeeCord", out.toByteArray());
	}
	
	private String get(ServeursDataType sdt, String id) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(Main.main.getDataFolder()+File.separator+"Servers.xml");
			Document doc = builder.parse(file);
			NodeList serverList = doc.getElementsByTagName("server");
			for (int i = 0; i < serverList.getLength(); i++) {
				Node s = serverList.item(i);
				if(s.getNodeType()==Node.ELEMENT_NODE) {
					Element server = (Element) s;
					String ids = server.getAttribute("id");
					if(ids.equalsIgnoreCase(id)) {
						NodeList dataList = server.getChildNodes();
						for (int j = 0; j < dataList.getLength(); j++) {
							Node d = dataList.item(j);
							if(d.getNodeType()==Node.ELEMENT_NODE) {
								Element x = (Element) d;
								if(x.getTagName().equalsIgnoreCase(sdt.getXmlName())) {
									return x.getTextContent();
								}else {
									continue;
								}
							}
						}
					}else {
						continue;
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Servers getServerByBungeeName(String name) {
		for(Servers servers : Main.serversList) {
			if(servers.getBungeeName().equalsIgnoreCase(name)) {
				return servers;
			}else {
				continue;
			}
		}
		return null;
	}
	
	
}
