package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantStaticCreate;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantStaticDelete;
import eu.busycloud.service.console.screens.assistents.ConsoleAssistantStaticEdit;
import eu.busycloud.service.utils.SingleServerInstance;
import eu.busycloud.service.utils.TextUtils;

public class CommandStaticServer extends ConsoleCommand {

	public CommandStaticServer(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("list")) {
				boolean did = false;
				for(SingleServerInstance spigotServer : ApplicationInterface.getAPI().getInfrastructure().getRunningServers())
					if(spigotServer.isStatic()) {
						did = true;
						CloudInstance.LOGGER.info(spigotServer.getServerName() + " [" + spigotServer.getId() + "]");
					}
				if(!did)
					CloudInstance.LOGGER.info("No staticserver is running. Do you need help with cloudsetup? /introduction");
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

			if (ApplicationInterface.getAPI().getInfrastructure().getServerByName(args[2]) == null) {
				CloudInstance.LOGGER.warning("Staticserver not found!");
				printHelp();
				return;
			}
			if (ApplicationInterface.getAPI().getInfrastructure().getServerByName(args[2]) == null) {
				CloudInstance.LOGGER.warning("Staticserver not found!");
				printHelp();
				return;
			}

			SingleServerInstance spigotServer = ApplicationInterface.getAPI().getInfrastructure().getServerByName(args[2]);
			
			switch (args[3].toLowerCase()) {
			case "rcon":
				if (args.length >= 4) {
					TextUtils.sendFatLine();
					StringBuilder builder = new StringBuilder();
					for (int i = 4; i < args.length; i++)
						builder.append(args[i] + " ");
					CloudInstance.LOGGER.info("Send command '" + builder.substring(0, builder.toString().length() - 1)
					+ "' to '" + spigotServer.getServerName() + "'.");
					spigotServer.rconServer(builder.substring(0, builder.toString().length() - 1));
					TextUtils.sendFatLine();
					return;
				}
				printHelp();
				break;
			case "stop":
				TextUtils.sendFatLine();
				spigotServer.stopServer();
				TextUtils.sendFatLine();
				break;
			case "ping":
				TextUtils.sendFatLine();
				spigotServer.ping();
				TextUtils.sendFatLine();
				break;
			case "info":
				TextUtils.sendFatLine();
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
				new ConsoleAssistantStaticCreate();
				break;
			case "delete":
				new ConsoleAssistantStaticDelete();
				break;
			case "edit":
				new ConsoleAssistantStaticEdit();
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
		CloudInstance.LOGGER.info("/staticserver list");
		CloudInstance.LOGGER.info("/staticserver control <servername> rcon <command>");
		CloudInstance.LOGGER.info("/staticserver control <servername> stop");
		CloudInstance.LOGGER.info("/staticserver control <servername> ping");
		CloudInstance.LOGGER.info("/staticserver control <servername> info");
		CloudInstance.LOGGER.info("/staticserver setup create");
		CloudInstance.LOGGER.info("/staticserver setup delete");
		CloudInstance.LOGGER.info("/staticserver setup edit");
	}
	
}
