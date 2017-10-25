package fr.drahoxx.lobby;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.drahoxx.lobby.servers.Servers;
import fr.drahoxx.lobby.utils.ItemStackCreator;

public class InventoryLoader{
	public static Inventory inv = Bukkit.createInventory(null, 6*9, "§6Teleport");
	public static Inventory getInventory() {
		return inv;
	}
	/*
	 * Get the server's bungeeName attached to a slot, if it's not a server, it will return " item[SECURITYNAMETAG98547845445877%%%*.,] "
	 */
	public static String getServer(int slotId) {
		String result = "item[SECURITYNAMETAG98547845445877%%%*.,]";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(Main.main.getDataFolder()+File.separator+"CompassInventory.xml");
			Document doc = builder.parse(file);
			NodeList slotsList = doc.getElementsByTagName("slot");
			for (int i = 0; i < slotsList.getLength(); i++) {
				Node s = slotsList.item(i);
				if(s.getNodeType()==Node.ELEMENT_NODE) {
					Element slot = (Element) s;
					Integer slotNumber = Integer.parseInt(slot.getAttribute("n"));
					if(slotId == slotNumber) {
						NodeList nameList = doc.getElementsByTagName("name");
						Node nName = nameList.item(i);
						Element name = (Element) nName;
						String type = name.getAttribute("type");
						if(type.equalsIgnoreCase("server")) {
							result = name.getTextContent();
						}
						break;
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
		return result;
	}
	public static void generateInventory() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(Main.main.getDataFolder()+File.separator+"CompassInventory.xml");
			Document doc = builder.parse(file);
			NodeList slotsList = doc.getElementsByTagName("slot");
			for (int i = 0; i < slotsList.getLength(); i++) {
				Node s = slotsList.item(i);
				if(s.getNodeType()==Node.ELEMENT_NODE) {
					Element slot = (Element) s;
					Integer slotNumber = Integer.parseInt(slot.getAttribute("n"));

					NodeList nameList = doc.getElementsByTagName("name");
					Node nName = nameList.item(i);
					Element name = (Element) nName;
					String type = name.getAttribute("type");
					if(type.equalsIgnoreCase("server")) {
						inv.setItem(slotNumber, Servers.getServerByBungeeName(name.getTextContent()).getItem());
					}else if(type.equalsIgnoreCase("item")){
						inv.setItem(slotNumber, Item.getItemByName(name.getTextContent()).getItemStack());
					}else {
						inv.setItem(slotNumber, ItemStackCreator.createItem(Material.STAINED_GLASS_PANE, "§4ERROR item n°"+slotNumber, (short)14, 10));
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
	}

}
