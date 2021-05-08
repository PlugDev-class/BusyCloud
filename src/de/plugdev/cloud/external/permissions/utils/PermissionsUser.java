package de.plugdev.cloud.external.permissions.utils;

import java.util.UUID;

public class PermissionsUser {
	
	private int userHeigt;
	private String playerName;
	private UUID uuid;
	private PermissionsGroup currentGroup;
	
	public final int getUserHeigt() {
		return userHeigt;
	}
	public final void setUserHeigt(int userHeigt) {
		this.userHeigt = userHeigt;
	}
	public final String getPlayerName() {
		return playerName;
	}
	public final void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public final UUID getUuid() {
		return uuid;
	}
	public final void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public final PermissionsGroup getCurrentGroup() {
		return currentGroup;
	}
	public final void setCurrentGroup(PermissionsGroup currentGroup) {
		this.currentGroup = currentGroup;
	}
	
}
