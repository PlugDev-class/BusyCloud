package de.plugdev.cloud.console.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class CommandGroup extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> startgroup");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> startserver");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> stop");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> template <ServerID>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> rcon <command>");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> del");
			ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] /group <groupname> add <Version> <Startport> <MaxRamEachServer> <StartServersByGroupstart> <Percent> <LobbyServer? (true/false)>");
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
			} else if (args[2].equalsIgnoreCase("add")) {
			} else {
				ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] ServerGroup with specific name not found.");
				return;
			}
		}

		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("list")) {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] The following groups are available:");
				for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
					ConsoleOutput.write(ConsoleOutput.CYAN,
							"[PLUGIN] " + group.getGroupName() + "(" + group.getGroupID() + ")");
					for (SpigotServer server : group.getGroupList()) {
						ConsoleOutput.write(ConsoleOutput.CYAN,
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
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Some servers are already running.");
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
					ConsoleOutput.write(ConsoleOutput.RED_BOLD,
							"[CORE] Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().remove(prefferedGroup);
			}
			return;
		} else if (args.length > 3) {
			if (args[2].equalsIgnoreCase("add")) {
				MinecraftVersion version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);
				int mainlystarted = Integer.parseInt(args[6]);
				int percent = Integer.parseInt(args[7]);
				boolean isMain = Boolean.parseBoolean(args[8]);

				ConsoleOutput.write(ConsoleOutput.CYAN,
						"[PLUGIN] ==========================================================================");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Starting groupcreation.");

				if (ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]) == null) {
					ConsoleOutput.write(ConsoleOutput.RED,
							"[PLUGIN] Version not downloaded or invalid. Maybe renamed?");
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Stopping task...");
					return;
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN,
							"[PLUGIN] Version by ID " + version.getVersion() + " found.");
				}

				if (mainlystarted == 0) {
					ConsoleOutput.write(ConsoleOutput.YELLOW,
							"[PLUGIN] Warning! You set your StartByServer-Value to 0!");
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[PLUGIN] Continuing task...");
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN,
							"[PLUGIN] Set StartServerByStartGroup " + version.getVersion() + " set.");
				}

				if (percent >= 0 && percent <= 10) {
					ConsoleOutput.write(ConsoleOutput.RED,
							"[PLUGIN] The cloud doesn't support percentrates down 10 percent!");
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Stopping task...");
					return;
				}

				if (percent <= 30) {
					ConsoleOutput.write(ConsoleOutput.YELLOW,
							"[PLUGIN] The cloud doesn't want to see percentrates down 30 percent!");
					ConsoleOutput.write(ConsoleOutput.YELLOW,
							"[PLUGIN] Reason: It may crash on start without any reasons.");
					ConsoleOutput.write(ConsoleOutput.YELLOW,
							"[PLUGIN] Result: You won't get any support, if your rate is so low!");
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[PLUGIN] Continuing task...");
					return;
				}

				if (percent > 100) {
					ConsoleOutput.write(ConsoleOutput.RED,
							"[PLUGIN] The could doesn't support percentrates above 100 percent.");
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Stopping task...");
					return;
				}

				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Percentrate " + percent + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] RAM in MegaByte " + maxRam + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Startingport " + startport + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "[PLUGIN] Lobbyvalue set to " + isMain);

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
					ConsoleOutput.write(ConsoleOutput.RED_BOLD,
							"[CORE] Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Starting ServerGroup!");
				ConsoleOutput.write(ConsoleOutput.CYAN,
						"[PLUGIN] ==========================================================================");

				prefferedGroup = new ServerGroup(version, startport, groupName, startport, null, maxRam, mainlystarted,
						percent, isMain);
				
				if (!ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().contains(prefferedGroup)) {
					ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(prefferedGroup);
				}
			} else if (args[2].equalsIgnoreCase("rcon")) {
				StringBuilder stringBuilder = null;
				for (SpigotServer spigotServer : prefferedGroup.getGroupList()) {
					if (spigotServer.getConnection().isConnected()) {
						stringBuilder = new StringBuilder();
						for (int i = 3; i < args.length; i++) {
							stringBuilder.append(args[i] + " ");
						}
						spigotServer
								.sendRCON(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
					}
				}
				ConsoleOutput.write(ConsoleOutput.CYAN, "[CORE] Send rcon \"" 
						+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1) 
						+ "\" to ServerGroup " 
						+ prefferedGroup.getGroupName());
			} else if(args[2].equalsIgnoreCase("template")) {
				int serverId = Integer.parseInt(args[3]);
				if(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) == null) {
					ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] Server with specific ID not found.");
					return;
				}
				
				ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId).doTemplate(prefferedGroup.getGroupName());
			}
		}
	}

}
