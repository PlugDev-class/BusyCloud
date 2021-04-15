package de.plugdev.cloud.console.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class CommandGroup extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Wrong syntax.");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> startgroup");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> startserver");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> stop");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> rcon <command>");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> edit");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> del");
			ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] /group <groupname> template");
			ConsoleColors.write(ConsoleColors.RED,
					"[PLUGIN] /group <groupname> add <Version> <Startport> <MaxRamEachServer> <StartServersByGroupstart> <Percent> <LobbyServer? (true/false)>");
			return;
		}

		String groupName = args[1];
		ServerGroup prefferedGroup = null;
		for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
			if (group.getGroupName().equalsIgnoreCase(groupName)) {
				prefferedGroup = group;
				break;
			}
		}

		if (prefferedGroup == null) {
			if (groupName.equalsIgnoreCase("list")) {
			} else if(args[2].equalsIgnoreCase("add")) {
			} else {
				ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] ServerGroup with specific name not found.");
				return;
			}
		}

		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("list")) {
				ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] The following groups are available:");
				for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
					ConsoleColors.write(ConsoleColors.CYAN,
							"[PLUGIN] " + group.getGroupName() + "(" + group.getGroupID() + ")");
					for (SpigotServer server : group.getGroupList()) {
						ConsoleColors.write(ConsoleColors.CYAN,
								"[PLUGIN] - " + server.getServerName() + "(ID: " + server.getId() + ") | localhost:"
										+ server.getPort() + " | Proxy: " + server.getProxyId());
					}
				}
			}
		} else if (args.length == 3) {
			if (args[2].equalsIgnoreCase("stop")) {
				if (prefferedGroup.getGroupList().size() != 0) {
					for (SpigotServer server : prefferedGroup.getGroupList()) {
						server.stopServer();
					}
				}
			} else if (args[2].equalsIgnoreCase("startgroup")) {
				if (prefferedGroup.getGroupList().size() != 0) {
					ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Some servers are already running.");
					return;
				}

				int startServer = prefferedGroup.getDefaultActiveServers() == 0 ? 1
						: prefferedGroup.getDefaultActiveServers();
				for (int i = 0; i < startServer; i++) {
					prefferedGroup.startServer(true,
							prefferedGroup.getStartPort() + prefferedGroup.getGroupList().size() + 1);
				}
			} else if (args[2].equalsIgnoreCase("startserver")) {
				prefferedGroup.startServer(true,
						prefferedGroup.getStartPort() + prefferedGroup.getGroupList().size() + 1);
			} else if (args[2].equalsIgnoreCase("del")) {
				for (SpigotServer server : prefferedGroup.getGroupList()) {
					server.stopServer();
				}

				try {
					List<String> lines = Files.readAllLines(new File("local/groups.pdv").toPath());
					StringBuilder builder = new StringBuilder();
					for (String string : lines) {
						if (!string.startsWith(prefferedGroup.getGroupName())) {
							builder.append(string + "\n");
						}
					}

					BufferedWriter writer = new BufferedWriter(new FileWriter("local/groups.pdv"));
					writer.write(builder.toString());
					writer.close();

				} catch (Exception exception) {
					ConsoleColors.write(ConsoleColors.RED_BOLD,
							"[CORE] Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().remove(prefferedGroup);
			}
			return;
		} else if (args.length == 9) {
			if (args[2].equalsIgnoreCase("add")) {
				MinecraftVersion version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);
				int mainlystarted = Integer.parseInt(args[6]);
				int percent = Integer.parseInt(args[7]);
				boolean isMain = Boolean.parseBoolean(args[8]);

				ConsoleColors.write(ConsoleColors.CYAN,
						"[PLUGIN] ==========================================================================");
				ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Starting groupcreation.");

				if (ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]) == null) {
					ConsoleColors.write(ConsoleColors.RED,
							"[PLUGIN] Version not downloaded or invalid. Maybe renamed?");
					ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Stopping task...");
					return;
				} else {
					ConsoleColors.write(ConsoleColors.GREEN,
							"[PLUGIN] Version by ID " + version.getVersion() + " found.");
				}

				if (mainlystarted == 0) {
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[PLUGIN] Warning! You set your StartByServer-Value to 0!");
					ConsoleColors.write(ConsoleColors.YELLOW, "[PLUGIN] Continuing task...");
				} else {
					ConsoleColors.write(ConsoleColors.GREEN,
							"[PLUGIN] Set StartServerByStartGroup " + version.getVersion() + " set.");
				}

				if (percent >= 0 && percent <= 10) {
					ConsoleColors.write(ConsoleColors.RED,
							"[PLUGIN] The cloud doesn't support percentrates down 10 percent!");
					ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Stopping task...");
					return;
				}

				if (percent <= 30) {
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[PLUGIN] The cloud doesn't want to see percentrates down 30 percent!");
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[PLUGIN] Reason: It may crash on start without any reasons.");
					ConsoleColors.write(ConsoleColors.YELLOW,
							"[PLUGIN] Result: You won't get any support, if your rate is so low!");
					ConsoleColors.write(ConsoleColors.YELLOW, "[PLUGIN] Continuing task...");
					return;
				}

				if (percent > 100) {
					ConsoleColors.write(ConsoleColors.RED,
							"[PLUGIN] The could doesn't support percentrates above 100 percent.");
					ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Stopping task...");
					return;
				}

				ConsoleColors.write(ConsoleColors.GREEN, "[PLUGIN] Percentrate " + percent + " set.");
				ConsoleColors.write(ConsoleColors.GREEN, "[PLUGIN] RAM in MegaByte " + maxRam + " set.");
				ConsoleColors.write(ConsoleColors.GREEN, "[PLUGIN] Startingport " + startport + " set.");
				ConsoleColors.write(ConsoleColors.GREEN, "[PLUGIN] Lobbyvalue set to " + isMain);

				try {
					List<String> lines = Files.readAllLines(new File("local/groups.pdv").toPath());
					StringBuilder builder = new StringBuilder();
					for (String string : lines) {
						builder.append(string + "\n");
					}
					builder.append(groupName + " | " + new Random().nextInt(34000) + " | " + maxRam + " | " + startport
							+ " | " + mainlystarted + " | " + percent + " | " + version.getVersion() + " | "
							+ (isMain ? "yes" : "no") + "\n");

					BufferedWriter writer = new BufferedWriter(new FileWriter("local/groups.pdv"));
					writer.write(builder.toString());
					writer.close();

				} catch (Exception exception) {
					ConsoleColors.write(ConsoleColors.RED_BOLD,
							"[CORE] Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Starting ServerGroup!");
				ConsoleColors.write(ConsoleColors.CYAN,
						"[PLUGIN] ==========================================================================");

				prefferedGroup = new ServerGroup(version, startport, groupName, startport, null, maxRam, mainlystarted,
						percent, isMain);
			}
		}
	}

}
