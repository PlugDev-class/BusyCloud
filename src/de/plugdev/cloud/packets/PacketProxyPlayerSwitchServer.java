package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketProxyPlayerSwitchServer extends Packet {

	public PacketProxyPlayerSwitchServer(String proxyKey, String uniqueId, String from, String to) {
		getObjectList().add(proxyKey);
		getObjectList().add(uniqueId);
		getObjectList().add(from);
		getObjectList().add(to);
	}
	
}
