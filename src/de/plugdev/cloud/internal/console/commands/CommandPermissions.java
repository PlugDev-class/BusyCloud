package de.plugdev.cloud.internal.console.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.external.permissions.utils.PermissionsGroup;
import de.plugdev.cloud.external.permissions.utils.PermissionsUser;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.Proxy;

public class CommandPermissions extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length <= 1) {
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Wrong syntax");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms user <username> info");
			ConsoleOutput.write(ConsoleOutput.RED_BOLD_BRIGHT, "[PLUGIN] /perms user <uuid> preregister | Template UUID: 0f30fb39bdef403e991440e25aa4a8b0");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms user <username> setgroup <groupname>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> info");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> create <Prefix> <Suffix> <Groupheight>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> delete");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> addpermission <Permissionsname>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> rempermission <Permissionsname>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> editheight <Preffered Groupheight>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> editprefix <Preffered Prefix>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /perms group <groupname> editsuffix <Preffered Suffix>");
		}
		
		switch (args.length) {
		case 4:
			String presyntax = args[1];
			String objecttitle = args[2];
			String subcommand = args[3];
			
			if(presyntax.equalsIgnoreCase("user")) {
				if(subcommand.equalsIgnoreCase("info")) {
					PermissionsUser user = ApplicationInterface.getAPI().getPermissionsSystem().getUserByName(objecttitle);
					if(user == null) {
						ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Permissionsuser with specific name not found.");
						ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Maybe never connected and not preregistered?");
						ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Manual preregister: /perms user <username> preregister");
						return;
					}
					PlayerInfo playerInfo = null;
					for(Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
						for(PlayerInfo info : proxy.getOnlinePlayer()) {
							if(info.getUniqueID() == user.getUuid()) {
								playerInfo = info;
								break;
							}
						}
					}
					
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Permissionsuser: ");
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] PermissionsUser name: " + user.getPlayerName());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] PermissionsUser uuid: " + user.getUuid().toString());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] PermissionsUser height: " + user.getUserHeigt());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Current group name: " + user.getCurrentGroup().getGroupName());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Current group prefix: " + user.getCurrentGroup().getGroupPrefix());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Current group suffix: " + user.getCurrentGroup().getGroupSuffix());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN-API] Is only?: " + (playerInfo != null ? "Yes on " + playerInfo.getConnectedServer() : "No"));
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN-API] If only -> Address: " + (playerInfo != null ? playerInfo.getAddress() : "Player isn't online."));
				} else if(subcommand.equalsIgnoreCase("preregister")) {
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[PLUGIN] Important! The cloud doesn't check if the uuid is right!");
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[PLUGIN] Little help: https://api.mojang.com/users/profiles/minecraft/<username>");
					
					PermissionsGroup prefferedGroup = null;
					for(PermissionsGroup group : ApplicationInterface.getAPI().getPermissionsSystem().getGroups()) {
						if(prefferedGroup == null) {
							prefferedGroup = group;
						}
						if(group.getGroupHeight() < prefferedGroup.getGroupHeight()) {
							prefferedGroup = group;
						}
					}
					
					try {
						File file = new File("local/permissions/users.pdv");
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
						writer.append(subcommand + " | " + prefferedGroup.getGroupId() + "\n");
						writer.close();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
}
