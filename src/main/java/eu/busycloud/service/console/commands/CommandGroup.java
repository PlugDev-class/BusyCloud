package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantGroupCreate;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantGroupDelete;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantGroupEdit;
import eu.busycloud.service.infrastructure.ServerGroup;
import eu.busycloud.service.utils.SingleServerInstance;
import eu.busycloud.service.utils.TextUtils;

public class CommandGroup extends ConsoleCommand {

	public CommandGroup(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("list")) {
				for(ServerGroup group : ApplicationInterface.getAPI().getInfrastructure().getRunningGroups()) {
					CloudInstance.LOGGER.info(group.getServerGroupContainer().getGroupName() + " [" + group.getServerGroupContainer().getGroupId() + "]");
					for(SingleServerInstance server : group.getGroupList())
						CloudInstance.LOGGER.info("\t" + server.getServerName() + " [" + server.getId() + "]");
				}
				if(ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().size() == 0) {
					CloudInstance.LOGGER.info("No group is running. Do you need help with cloudsetup? /introduction");
				}
				return;
			}
		}

		if(args.length < 3) {
			printHelp();
			return;
		}
		if(args[1].toLowerCase().equals("control")) {
			if(args.length < 4) {
				printHelp();
				return;
			}
		}
		
		
		switch (args[1].toLowerCase()) {
		case "control":

			if (ApplicationInterface.getAPI().getInfrastructure().getGroupByName(args[2]) == null) {
				CloudInstance.LOGGER.warning("Servergroup not found!");
				printHelp();
				return;
			}
			ServerGroup serverGroup = ApplicationInterface.getAPI().getInfrastructure().getGroupByName(args[2]);

			switch (args[3].toLowerCase()) {
			case "rcon":
				if (args.length >= 4) {
					TextUtils.sendFatLine();
					StringBuilder builder = new StringBuilder();
					for (int i = 4; i < args.length; i++)
						builder.append(args[i] + " ");
					CloudInstance.LOGGER.info("Send command '" + builder.substring(0, builder.toString().length() - 1)
					+ "' to '" + serverGroup.getServerGroupContainer().getGroupName() + "'.");
					serverGroup.rconGroup(builder.substring(0, builder.toString().length() - 1));
					TextUtils.sendFatLine();
					return;
				}
				printHelp();
				break;
			case "startserver":
				serverGroup.startServer();
				break;
			case "stopgroup":
				TextUtils.sendFatLine();
				serverGroup.stopServers();
				TextUtils.sendFatLine();
				break;
			case "ping":
				TextUtils.sendFatLine();
				for(SingleServerInstance spigotServer : serverGroup.getGroupList())
					spigotServer.ping();
				TextUtils.sendFatLine();
				break;
			case "info":
				TextUtils.sendFatLine();
				for(SingleServerInstance spigotServer : serverGroup.getGroupList())
					spigotServer.printInfo();
				TextUtils.sendFatLine();
				break;
			default:
				printHelp();
				break;
			}

			break;
		case "setup":
			
			switch (args[2].toLowerCase()) {
			case "create":
				new ConsoleAssistantGroupCreate();
				break;
			case "delete":
				new ConsoleAssistantGroupDelete();
				break;
			case "edit":
				new ConsoleAssistantGroupEdit();
				break;
			default:
				break;
			}
			
			
			break;
		default:
			printHelp();
			break;
		}

	}

	private void printHelp() {
		CloudInstance.LOGGER.info("/servergroup list");
		CloudInstance.LOGGER.info("/servergroup control <groupname> rcon <command>");
		CloudInstance.LOGGER.info("/servergroup control <groupname> startserver");
		CloudInstance.LOGGER.info("/servergroup control <groupname> stopgroup");
		CloudInstance.LOGGER.info("/servergroup control <groupname> ping");
		CloudInstance.LOGGER.info("/servergroup control <groupname> info");
		CloudInstance.LOGGER.info("/servergroup setup create");
		CloudInstance.LOGGER.info("/servergroup setup delete");
		CloudInstance.LOGGER.info("/servergroup setup edit");
	}

}