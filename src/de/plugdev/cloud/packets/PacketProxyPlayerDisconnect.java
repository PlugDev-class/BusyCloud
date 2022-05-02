package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketProxyPlayerDisconnect extends Packet {

	public PacketProxyPlayerDisconnect(String proxyKey, String uniqueId) {
		getObjectList().add(proxyKey);
		getObjectList().add(uniqueId);
	}
	
}
