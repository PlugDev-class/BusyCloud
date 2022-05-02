package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketSharedRconServer extends Packet {

	public PacketSharedRconServer(String command) {
		getObjectList().add(command);
	}
	
}
