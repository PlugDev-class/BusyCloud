package de.plugdev.cloud.external.permissions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.permissions.netlistener.DecodePermissionsListener;
import de.plugdev.cloud.external.permissions.utils.PermissionsGroup;
import de.plugdev.cloud.external.permissions.utils.PermissionsUser;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.utils.FileUtils;

public class PermissionsSystem {

	private List<PermissionsGroup> groups = new LinkedList<>();
	private List<PermissionsUser> users = new LinkedList<>();

	public void init(String string) {
		ConsoleOutput.write(ConsoleOutput.CYAN, "[PERMISSIONS] Initiating PermissionsSystem...");
		ApplicationInterface.getAPI().getNetworking().getServer().registerListener(new DecodePermissionsListener());
		interpretFile(string);
	}

	public void interpretFile(String groupsPath) {
		try {
			if(!new File(groupsPath).exists()) {
				File localPermissionsGroupsFile = new File("local/permissions/groups.pdv");
				File localPermissionsUsersFile = new File("local/permissions/users.pdv");
				if(!localPermissionsGroupsFile.exists()) {
					localPermissionsGroupsFile.createNewFile();
				}
				if(!localPermissionsUsersFile.exists()) {
					localPermissionsUsersFile.createNewFile();
				}
				
				StringBuilder builder = new StringBuilder();
				builder.append("#################################################\n");
				builder.append("# T h a n k  y o u  f o r  u s i n g  BusyCloud #\n");
				builder.append("#################################################\n");
				builder.append("#        Please use the command \"/perms\"        #\n");
				builder.append("#         (Instead of editing this file)        #\n");
				builder.append("#################################################\n");
				builder.append("01 | admin | §cAdmin - | §cTeam | *,debug.* | 100 | rffu | rffu | rffu | rffu\n");
				builder.append("02 | default | §7Player - | §7Player | lobby.use,lobby.interact | 0 | rffu | rffu | rffu | rffu\n");
				FileUtils.writeFile(localPermissionsGroupsFile, builder.toString());
				
				builder = new StringBuilder();
				builder.append("#################################################\n");
				builder.append("# T h a n k  y o u  f o r  u s i n g  BusyCloud #\n");
				builder.append("#################################################\n");
				builder.append("#        Please use the command \"/perms\"        #\n");
				builder.append("#         (Instead of editing this file)        #\n");
				builder.append("#################################################\n");
				builder.append("0f30fb39bdef403e991440e25aa4a8b0 | 01 | rffu\n");
				FileUtils.writeFile(localPermissionsUsersFile, builder.toString());
			}
			List<String> lines = Files.readAllLines(new File(groupsPath).toPath(), Charset.defaultCharset());
			for (String line : lines) {
				if (!line.startsWith("#")) {
					String[] arrays = line.split(" \\| ");
					PermissionsGroup group = new PermissionsGroup();
					group.setGroupId(Integer.parseInt(arrays[0]));
					group.setGroupName(arrays[1]);
					group.setGroupPrefix(arrays[2]);
					group.setGroupSuffix(arrays[3]);
					group.setPermissions(Arrays.asList(arrays[4].split(",")));
					group.setGroupHeight(0);
					groups.add(group);
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PERMISSIONS] Registered group (" + group.getGroupId() + ", " + group.getGroupName() + ", " + group.getGroupHeight() + ")");
				}
			}
			lines.clear();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public List<PermissionsGroup> getGroups() {
		return groups;
	}

	public List<PermissionsUser> getUsers() {
		return users;
	}

	public PermissionsGroup getGroupByName(String groupName) {
		for (PermissionsGroup group : groups) {
			if (group.getGroupName().equalsIgnoreCase(groupName)) {
				return group;
			}
		}
		return null;
	}

	public PermissionsGroup getGroupByID(int groupID) {
		for (PermissionsGroup group : groups) {
			if (group.getGroupId() == (groupID)) {
				return group;
			}
		}
		return null;
	}

	public PermissionsUser getUserByName(String userName) {
		for (PermissionsUser user : users) {
			if (user.getPlayerName().equalsIgnoreCase(userName)) {
				return user;
			}
		}
		return null;
	}

	public PermissionsUser getUserByUUID(UUID userUUID) {
		for (PermissionsUser user : users) {
			if (user.getUuid() == (userUUID)) {
				return user;
			}
		}
		return null;
	}

}
