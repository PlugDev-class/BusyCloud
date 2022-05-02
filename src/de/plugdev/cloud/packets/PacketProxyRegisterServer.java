package de.plugdev.cloud.packets;

@SuppressWarnings("serial")
public class PacketProxyRegisterServer extends Packet{
	
	public PacketProxyRegisterServer(String serverName, int port, boolean isLobby) {
		getObjectList().add(serverName);
		getObjectList().add(port);
		getObjectList().add(isLobby);
	}
	
}
