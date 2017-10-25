package fr.drahoxx.lobby;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import fr.drahoxx.lobby.servers.Servers;
import fr.drahoxx.lobby.utils.ResourceManager;

public class Main extends JavaPlugin implements PluginMessageListener{
	
	public static Main main;
	public static ArrayList<Servers> serversList = new ArrayList<>();
	public static ArrayList<Item> itemsList = new ArrayList<>();
	@Override
	public void onEnable() {
		main = this;
		createXmlFileServer();
		createXmlFileInventory();
		createXmlItem();
		createXmlConfigFile();
		registerEvents();
		registerChannels();
		Item.loadAllItems();
		registerServers();
		InventoryLoader.generateInventory();
		launchRunnable();
		getCommand("refreshFiles").setExecutor(new CmdRefreshFiles());
		super.onEnable();
	}
	
	public void launchRunnable() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Servers s : serversList) {
					s.refreshPlayersCountAndList();
				}
			}
		}, 0, 2400);
	}

	@Override
	  public void onPluginMessageReceived(String channel, Player player, byte[] message){
	    if (!channel.equalsIgnoreCase("BungeeCord")) {
	      return;
	    }
	    try {
		    ByteArrayDataInput in = ByteStreams.newDataInput(message);
		    String subchannel = in.readUTF();
		    if (subchannel.equalsIgnoreCase("PlayerCount")) {
		    	String server = in.readUTF(); 
		    	int playercount = in.readInt();
		    	for (int i = 0; i < serversList.size(); i++) {
					if(serversList.get(i).getBungeeName().equalsIgnoreCase(server)) {
						serversList.get(i).setNumbersOfPlayerConnected(playercount);
					}
				}
		    	
		    }else if (subchannel.equalsIgnoreCase("PlayerList")) {
		    	String server2 = in.readUTF();
		    	String[] playerList = in.readUTF().split(", ");
		    	for (int i = 0; i < serversList.size(); i++) {
					if(serversList.get(i).getBungeeName().equalsIgnoreCase(server2)) {
						serversList.get(i).setPlayerList(playerList);
					}
				}
		    	
		    }
	    } catch (Exception e) {
			System.err.println("Server informations are falses.");
		}
	  }
	
	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new MenuOpenEvents(), this);
		pm.registerEvents(new OnJoin(), this);
	}
	
	
	public void createXmlFileInventory() {
		File file = new File(getDataFolder()+File.separator+"CompassInventory.xml");
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if(!file.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
			    final DocumentBuilder builder = factory.newDocumentBuilder();
			    final Document document= builder.newDocument();
			    final Element racine = document.createElement("compassInventory");
			    document.appendChild(racine);			

			    final Comment commentaire = document.createComment("You can config your inventory here");
			    racine.appendChild(commentaire);
					
			    final Element inventory = document.createElement("inventory");
			    racine.appendChild(inventory);
			    ArrayList<Servers> array = serversList;
			    //Slots
			    for (int j = 0; j < 54; j++) {
			    	final Element slot = document.createElement("slot");
				    slot.setAttribute("n", ""+j);
				    inventory.appendChild(slot);
				    final Element nameAndType = document.createElement("name");
				    if(array.size()>0) {
				    	Servers s = array.get(0);
				    	nameAndType.appendChild(document.createTextNode(s.getBungeeName()));
				    	nameAndType.setAttribute("type", "server");
				    	array.remove(0);
				    }else {
				    	nameAndType.appendChild(document.createTextNode("ItemDefault"));
				    	nameAndType.setAttribute("type", "item");
				    }
				    
				    slot.appendChild(nameAndType);
				}
			    
					
			   	
					
			    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    final Transformer transformer = transformerFactory.newTransformer();
			    final DOMSource source = new DOMSource(document);
			    final StreamResult sortie = new StreamResult(file);
					
			    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
			    		
			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					
			    transformer.transform(source, sortie);	
			}
			catch (final ParserConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerException e) {
			    e.printStackTrace();
			}			
		    }

		
	}
	
	public void createXmlConfigFile() {
		File file = new File(getDataFolder()+File.separator+"Config.xml");
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if(!file.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
			    final DocumentBuilder builder = factory.newDocumentBuilder();
			    final Document document= builder.newDocument();
			    final Element racine = document.createElement("configuration");
			    document.appendChild(racine);			

			    final Comment commentaire = document.createComment("You can config Hexogane-Lobby here.");
			    racine.appendChild(commentaire);
					
			    final Element teleporter = document.createElement("giveTeleporterItemOnJoin");
			    teleporter.setTextContent("true");
			    teleporter.setAttribute("Slot", "4");
			    racine.appendChild(teleporter);
			    
			    
					
			    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    final Transformer transformer = transformerFactory.newTransformer();
			    final DOMSource source = new DOMSource(document);
			    final StreamResult sortie = new StreamResult(file);
					
			    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
			    		
			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					
			    transformer.transform(source, sortie);	
			}
			catch (final ParserConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerException e) {
			    e.printStackTrace();
			}			
		    }

		
	}
	
	public void createXmlItem() {
		File file = new File(getDataFolder()+File.separator+"Items.xml");
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if(!file.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
			    final DocumentBuilder builder = factory.newDocumentBuilder();
			    final Document document= builder.newDocument();
			    final Element items = document.createElement("items");
			    document.appendChild(items);			

			    final Comment commentaire = document.createComment("Creates your items here.");
			    items.appendChild(commentaire);
					
			    //Slots
			    final Element slot = document.createElement("item");
				slot.setAttribute("displayName", "§6Item");
				slot.setAttribute("material", "STAINED_GLASS_PANE");
				slot.setAttribute("count", "1");
				slot.setAttribute("damage", "5");
				slot.setAttribute("glow", "true");
				slot.setTextContent("ItemDefault");
				items.appendChild(slot);
				
				
				final Element tItem = document.createElement("item");
				tItem.setAttribute("displayName", "§6Teleporter");
				tItem.setAttribute("material", "COMPASS");
				tItem.setAttribute("count", "1");
				tItem.setAttribute("damage", "0");
				tItem.setAttribute("glow", "false");
				tItem.setTextContent("TeleporterItem");
				items.appendChild(tItem);
				items.appendChild(document.createComment("Please do not remove \"TeleporterItem\" item -> teleporter item (just edit it)"));
					
			    final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			    final Transformer transformer = transformerFactory.newTransformer();
			    final DOMSource source = new DOMSource(document);
			    final StreamResult sortie = new StreamResult(file);
					
			    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
			    		
			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					
			    transformer.transform(source, sortie);	
			}
			catch (final ParserConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerConfigurationException e) {
			    e.printStackTrace();
			}
			catch (TransformerException e) {
			    e.printStackTrace();
			}			
		    }

		
	}
	
	public void createXmlFileServer() {
		File file = new File(getDataFolder()+File.separator+"Servers.xml");
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		if(!file.exists()) {
			try{
				File f = new File(ResourceManager.extract("/res/Servers.xml"));
				Files.copy(f, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
			
	}
	
	public void registerChannels() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	}
	
	public void registerServers() {
		Main.serversList.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(getDataFolder()+File.separator+"Servers.xml");
			Document doc = builder.parse(file);
			NodeList serverList = doc.getElementsByTagName("server");
			for (int i = 0; i < serverList.getLength(); i++) {
				Node s = serverList.item(i);
				if(s.getNodeType()==Node.ELEMENT_NODE) {
					Element server = (Element) s;
					String ids = server.getAttribute("id");
					Servers serv = new Servers(ids);
					Boolean alreadyExist = false;
					for (int j = 0; j < serversList.size(); j++) {
						if(serversList.get(j).getId()==ids) {
							alreadyExist = true;
							break;
						}else {
						}
					}
					if(!alreadyExist)
						serversList.add(serv);
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
