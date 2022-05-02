package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketProxyUnRegisterServer extends Packet {
	
	public PacketProxyUnRegisterServer(String serverName) {
		getObjectList().add(serverName);
	}
	
}
