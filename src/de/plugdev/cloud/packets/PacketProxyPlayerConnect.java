package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketProxyPlayerConnect extends Packet {

	public PacketProxyPlayerConnect(String proxyKey, String playerName, String uniqueId, String connectedServer, String address) {
		getObjectList().add(proxyKey);
		getObjectList().add(playerName);
		getObjectList().add(uniqueId);
		getObjectList().add(connectedServer);
		getObjectList().add(address);
	}
	
}
