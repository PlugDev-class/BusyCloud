package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ServerGroup;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandGroup extends ConsoleCommand {

	public CommandGroup(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("list")) {
				for(ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
					CloudInstance.LOGGER.info("-> " + group.getGroupName() + " [" + group.getGroupID() + "]");
					for(SpigotServer server : group.getGroupList())
						CloudInstance.LOGGER.info("->-> " + server.getServerName() + " [" + server.getId() + "]");
				}
				if(ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size() == 0) {
					CloudInstance.LOGGER.info("No group is running. Do you need help with cloudsetup? /introduction");
				}
				return;
			}
		}
		
		
		if (args.length <= 4) {
			printHelp();
			return;
		}

		switch (args[2].toLowerCase()) {
		case "control":

			if (ApplicationInterface.getAPI().getInfrastructure().getGroupbyName(args[4]) == null) {
				CloudInstance.LOGGER.warning("Servergroup not found!");
				printHelp();
				return;
			}
			ServerGroup serverGroup = ApplicationInterface.getAPI().getInfrastructure().getGroupbyName(args[4]);

			switch (args[4].toLowerCase()) {
			case "rcon":
				if (args.length >= 5) {
					StringBuilder builder = new StringBuilder();
					for (int i = 5; i < args.length; i++)
						builder.append(args[i] + " ");
					serverGroup.rconGroup(builder.substring(0, builder.toString().length() - 1));
					CloudInstance.LOGGER.info("Send command '" + builder.substring(0, builder.toString().length() - 1)
							+ "' to '" + serverGroup.getGroupName() + "'.");
					return;
				}
				printHelp();
				return;
			case "startserver":
				serverGroup.startServer(true, serverGroup.getStartPort()+serverGroup.getGroupList().size()+1);
			case "stopgroup":
				for(SpigotServer spigotServer : serverGroup.getGroupList())
					spigotServer.stopServer();
			case "ping":
				for(SpigotServer spigotServer : serverGroup.getGroupList())
					spigotServer.ping();
			case "info":
				for(SpigotServer spigotServer : serverGroup.getGroupList())
					spigotServer.printInfo();
			default:
				break;
			}

			break;
		case "setup":
			
			
			
			
			break;
		default:
			printHelp();
			break;
		}

	}

	private void printHelp() {
		CloudInstance.LOGGER.info("/group list");
		CloudInstance.LOGGER.info("/group control <groupname> rcon <command>");
		CloudInstance.LOGGER.info("/group control <groupname> startserver");
		CloudInstance.LOGGER.info("/group control <groupname> stopgroup");
		CloudInstance.LOGGER.info("/group control <groupname> ping");
		CloudInstance.LOGGER.info("/group control <groupname> info");
		CloudInstance.LOGGER.info("/group setup create <groupname>");
		CloudInstance.LOGGER.info("/group setup delete <groupname>");
		CloudInstance.LOGGER.info("/group setup edit <groupname>");
	}

}