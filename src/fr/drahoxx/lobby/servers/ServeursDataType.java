package fr.drahoxx.lobby.servers;

public enum ServeursDataType {
	
	NAME("name"),
	BUNGEE_NAME("bungeeName"),
	MOTD("motd"), 
	ITEM("item");
	private String s;
	private ServeursDataType(String s) {
		this.setXmlName(s);
	}
	public String getXmlName() {
		return s;
	}
	public void setXmlName(String s) {
		this.s = s;
	}

}
