package fr.drahoxx.lobby;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.drahoxx.lobby.utils.ItemStackCreator;

public class Item {
	
	private String name;
	
	private String displayName;

	private Integer number;
	private Material material;
	private Short data;
	private Boolean isEnchant;
	
	public Item(String name,String displayName, Integer count, Material material, Short data, Boolean isEnchant){
		this.name = name;
		this.displayName = displayName;
		this.number = count;
		this.material = material;
		this.data = data;
		this.isEnchant = isEnchant;

	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public ItemStack getItemStack() {
		ItemStack item;
		if(isEnchant) {
			item = ItemStackCreator.createItem(material,number, Enchantment.PROTECTION_FIRE, 1, true, displayName, data);
		}else {
			item = ItemStackCreator.createItem(material, displayName, data, number);
		}
		return item;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	
	public static void loadAllItems() {
		Main.itemsList.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(Main.main.getDataFolder()+File.separator+"Items.xml");
			Document doc = builder.parse(file);
			NodeList itemsList = doc.getElementsByTagName("item");
			for (int i = 0; i < itemsList.getLength(); i++) {
					Node it = itemsList.item(i);
					Element item = (Element) it;
					String idName = item.getTextContent();
					String displayName = item.getAttribute("displayName");
					Short data = Short.parseShort(item.getAttribute("damage"));
					Integer number = Integer.parseInt(item.getAttribute("count"));
					Material material = Material.matchMaterial(item.getAttribute("material"));
					Boolean isEnchant = Boolean.parseBoolean(item.getAttribute("glow"));
					Item items = new Item(idName, displayName, number, material, data, isEnchant);
					Main.itemsList.add(items);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Item getItemByName(String name) {
		Item item = null;
		for(Item items : Main.itemsList) {
			if(items.getName().equalsIgnoreCase(name)) {
				item = items;
				break;
			}
		}
		return item;
	}
	
	
	
}
