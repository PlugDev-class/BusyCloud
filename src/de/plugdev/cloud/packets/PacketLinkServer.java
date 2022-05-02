package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketLinkServer extends Packet {

	public PacketLinkServer(String type, String cloudKey) {
		getHeaderList().add(type);
		getObjectList().add(cloudKey);
	}
	
}
