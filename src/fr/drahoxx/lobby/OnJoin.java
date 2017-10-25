package fr.drahoxx.lobby;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OnJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(Main.main.getDataFolder()+File.separator+"Config.xml");
			Document doc = builder.parse(file);
			NodeList list = doc.getElementsByTagName("giveTeleporterItemOnJoin");
			for(int i = 0; i<list.getLength();i++) {
				Element element = (Element) list.item(i);
				Boolean b = Boolean.valueOf(element.getTextContent());
				if(b) {
					e.getPlayer().getInventory().setItem(Integer.parseInt(element.getAttribute("Slot")), Item.getItemByName("TeleporterItem").getItemStack());
				}
			}
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
