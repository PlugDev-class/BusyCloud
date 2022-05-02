package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketSharedPing extends Packet {

	public PacketSharedPing(long systemCurrentTimeMillis) {
		getObjectList().add(systemCurrentTimeMillis);
	}
	
}
