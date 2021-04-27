package de.plugdev.cloud.api.permissions.utils;

import java.util.LinkedList;
import java.util.List;

public class PermissionsGroup {
	
	private int groupId;
	private int groupHeight;
	private String groupName;
	private String groupPrefix;
	private String groupSuffix;
	private List<String> permissions = new LinkedList<>();
	private List<PermissionsUser> players = new LinkedList<>();
	
	public final int getGroupId() {
		return groupId;
	}
	public final void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public final int getGroupHeight() {
		return groupHeight;
	}
	public final void setGroupHeight(int groupHeight) {
		this.groupHeight = groupHeight;
	}
	public final String getGroupName() {
		return groupName;
	}
	public final void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public final String getGroupPrefix() {
		return groupPrefix;
	}
	public final void setGroupPrefix(String groupPrefix) {
		this.groupPrefix = groupPrefix;
	}
	public final String getGroupSuffix() {
		return groupSuffix;
	}
	public final void setGroupSuffix(String groupSuffix) {
		this.groupSuffix = groupSuffix;
	}
	public final List<String> getPermissions() {
		return permissions;
	}
	public final void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public final List<PermissionsUser> getPlayers() {
		return players;
	}
	public final void setPlayers(List<PermissionsUser> players) {
		this.players = players;
	}
}
