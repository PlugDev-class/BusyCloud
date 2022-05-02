package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketSharedInfo extends Packet {

	public PacketSharedInfo(String key, String message) {
		getObjectList().add(message);
	}
	
}
