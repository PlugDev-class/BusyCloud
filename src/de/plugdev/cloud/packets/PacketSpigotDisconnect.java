package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketSpigotDisconnect extends Packet {

	public PacketSpigotDisconnect(String serverKey, String reason) {
		this.objectList.add(serverKey);
		this.objectList.add(reason);
	}
	
}
