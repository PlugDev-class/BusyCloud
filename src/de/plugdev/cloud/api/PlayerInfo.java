package de.plugdev.cloud.api;

import java.util.UUID;

public class PlayerInfo {
	
	private String playername, connectedServer, address;
	private UUID uuid;
	
	public PlayerInfo(String playername, UUID uuid, String connectedServer, String address) {
		this.playername = playername;
		this.uuid = uuid;
		this.connectedServer = connectedServer;
		this.address = address;
	}
	
	public String getConnectedServer() {
		return connectedServer;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getPlayername() {
		return playername;
	}
	
	public UUID getUniqueID() {
		return uuid;
	}
	
	public void setConnectedServer(String connectedServer) {
		this.connectedServer = connectedServer;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPlayername(String playername) {
		this.playername = playername;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
}
