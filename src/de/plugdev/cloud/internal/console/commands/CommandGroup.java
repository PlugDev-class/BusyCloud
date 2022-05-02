package de.plugdev.cloud.internal.console.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;
import de.plugdev.cloud.internal.models.IVersion;

public class CommandGroup implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> startgroup");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> startserver");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> stop");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> template <ServerID>");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> rcon <command>");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> del");
			ConsoleOutput.write(ConsoleOutput.RED, "/group <groupname> add <Version> <Startport> <MaxRamEachServer> <StartServersByGroupstart> <Percent> <LobbyServer? (true/false)>");
			return;
		}

		String groupName = args[1];
		Optional<ServerGroup> prefferedGroup = ApplicationInterface.getAPI().getInfrastructure().getGroupByName(groupName);

		if (!prefferedGroup.isPresent()) {
			if (!(groupName.equalsIgnoreCase("list") || args[2].equalsIgnoreCase("add"))) {
				ConsoleOutput.write(ConsoleOutput.RED, "ServerGroup with specific name not found.");
				return;
			}
		}

		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("list")) {
				ConsoleOutput.write(ConsoleOutput.CYAN, "The following groups are available:");
				for (ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
					ConsoleOutput.write(ConsoleOutput.CYAN, "" + group.getName() + "(" + group.getUniqueId() + ")");
					for (IService server : group.getGroupList()) {
						ConsoleOutput.write(ConsoleOutput.CYAN,
								"- " + server.getName() + "(ID: " + server.getUniqueId() + ") | localhost:"
										+ server.getPort() + " | Proxy: " + ((SpigotServer) server).getProxyId());
					}
				}
			}
		} else if (args.length == 3) {
			if (args[2].equalsIgnoreCase("stop")) {
				if (prefferedGroup.get().getGroupList().size() != 0) {
					for (IService server : prefferedGroup.get().getGroupList()) {
						server.stop();
					}
				}
			} else if (args[2].equalsIgnoreCase("startgroup")) {
				if (prefferedGroup.get().getGroupList().size() != 0) {
					ConsoleOutput.write(ConsoleOutput.RED, "Some servers are already running.");
					return;
				}
				prefferedGroup.get().init();
			} else if (args[2].equalsIgnoreCase("startserver")) {
				prefferedGroup.get().start();
			} else if (args[2].equalsIgnoreCase("del")) {
				for (IService server : prefferedGroup.get().getGroupList()) {
					server.stop();
				}

				try {
					List<String> lines = Files.readAllLines(new File("local/groups.pdv").toPath());
					StringBuilder builder = new StringBuilder();
					for (String string : lines) {
						if (!string.startsWith(prefferedGroup.get().getName())) {
							builder.append(string + "\n");
						}
					}

					BufferedWriter writer = new BufferedWriter(new FileWriter("local/groups.pdv"));
					writer.write(builder.toString());
					writer.close();

				} catch (Exception exception) {
					ConsoleOutput.write(ConsoleOutput.RED_BOLD,
							"Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().remove(prefferedGroup.get());
			}
			return;
		} else if (args.length > 3) {
			if (args[2].equalsIgnoreCase("add")) {
				Optional<IVersion> version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				UUID uuid = UUID.randomUUID();
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);
				int mainlystarted = Integer.parseInt(args[6]);
				int percent = Integer.parseInt(args[7]);
				boolean isMain = Boolean.parseBoolean(args[8]);

				ConsoleOutput.write(ConsoleOutput.CYAN, "==========================================================================");
				ConsoleOutput.write(ConsoleOutput.CYAN, "Starting groupcreation.");

				if (!version.isPresent()) {
					ConsoleOutput.write(ConsoleOutput.RED, "Version not downloaded or invalid. Maybe renamed?");
					ConsoleOutput.write(ConsoleOutput.RED, "Stopping task...");
					return;
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN, "Version by ID " + version.get().getVersion() + " found.");
				}

				if (mainlystarted == 0) {
					ConsoleOutput.write(ConsoleOutput.YELLOW, "Warning! You set your StartByServer-Value to 0!");
					ConsoleOutput.write(ConsoleOutput.YELLOW, "Continuing task...");
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN, "Set StartServerByStartGroup " + version.get().getVersion() + " set.");
				}

				if (percent >= 0 && percent <= 10) {
					ConsoleOutput.write(ConsoleOutput.RED, "The cloud doesn't support percentrates down 10 percent!");
					ConsoleOutput.write(ConsoleOutput.RED, "Stopping task...");
					return;
				}

				if (percent <= 30) {
					ConsoleOutput.write(ConsoleOutput.YELLOW, "The cloud may crash, if <30% is set wrongly.");
					ConsoleOutput.write(ConsoleOutput.RED, "Stopping task...");
					return;
				}

				if (percent > 100) {
					ConsoleOutput.write(ConsoleOutput.RED, "The could doesn't support percentrates above 100 percent.");
					ConsoleOutput.write(ConsoleOutput.RED, "Stopping task...");
					return;
				}

				ConsoleOutput.write(ConsoleOutput.GREEN, "Percentrate " + percent + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "RAM in MegaByte " + maxRam + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "Startingport " + startport + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "Lobbyvalue set to " + isMain);

				try {
					List<String> lines = Files.readAllLines(new File("local/groups.pdv").toPath());
					StringBuilder builder = new StringBuilder();
					for (String string : lines) {
						builder.append(string + "\n");
					}
					builder.append(groupName + " | " + uuid + " | " + maxRam + " | " + startport
							+ " | " + mainlystarted + " | " + percent + " | " + version.get().getVersion() + " | "
							+ (isMain ? "yes" : "no") + "\n");

					BufferedWriter writer = new BufferedWriter(new FileWriter("local/groups.pdv"));
					writer.write(builder.toString());
					writer.close();

				} catch (Exception exception) {
					ConsoleOutput.write(ConsoleOutput.RED_BOLD,
							"Couldn't find file \"local/groups.pdv\". Please reinstall the cloud!");
					ApplicationInterface.getAPI().getInfrastructure().shutdownTask();
				}

				ConsoleOutput.write(ConsoleOutput.CYAN, "Starting ServerGroup!");
				ConsoleOutput.write(ConsoleOutput.CYAN,
						"==========================================================================");

				prefferedGroup = Optional.of(new ServerGroup(uuid, groupName, maxRam, version.get(), startport, isMain, null, mainlystarted, percent));
				
				if (!ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().contains(prefferedGroup.get())) {
					ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(prefferedGroup.get());
				}
			} else if (args[2].equalsIgnoreCase("rcon")) {
				StringBuilder stringBuilder = null;
				for (IService spigotServer : prefferedGroup.get().getGroupList()) {
					if (spigotServer.getConnection().isConnected()) {
						stringBuilder = new StringBuilder();
						for (int i = 3; i < args.length; i++)
							stringBuilder.append(args[i] + " ");
						spigotServer.rcon(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
					}
				}
				ConsoleOutput.write(ConsoleOutput.CYAN, "Send rcon \"" 
						+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1) 
						+ "\" to ServerGroup " 
						+ prefferedGroup.get().getName());
			} else if(args[2].equalsIgnoreCase("template")) {
				UUID uuid = UUID.fromString(args[3]);
				if(uuid == null) {
					ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found! UniqueId invalid.");
					return;
				}
				Optional<IService> iService = ApplicationInterface.getAPI().getInfrastructure().getServiceById(uuid);
				if(!iService.isPresent()) {
					ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found!");
					return;
				}
				((SpigotServer) iService.get()).doTemplate(prefferedGroup.get().getName());
			}
		}
	}

	@Override
	public String getHelp() {
		return "Manages a servergroup or create new.";
	}

}
