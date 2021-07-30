package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.utils.SingleServerInstance;
import eu.busycloud.service.utils.TextUtils;

public class CommandTempServer extends ConsoleCommand {

	public CommandTempServer(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("list")) {
				for(SingleServerInstance spigotServer : ApplicationInterface.getAPI().getInfrastructure().getRunningServers())
						CloudInstance.LOGGER.info(spigotServer.getServerName() + " [" + spigotServer.getId() + "]");
				if(ApplicationInterface.getAPI().getInfrastructure().getRunningServers().size() == 0)
					CloudInstance.LOGGER.info("No server is running. Do you need help with cloudsetup? /introduction");
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
				CloudInstance.LOGGER.warning("Server not found!");
				printHelp();
				return;
			}
			if(ApplicationInterface.getAPI().getInfrastructure().getServerByName(args[2]).isStatic()) {
				CloudInstance.LOGGER.warning("Server is static!");
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
		default:
			printHelp();
			break;
		}

	}

	private void printHelp() {
		CloudInstance.LOGGER.info("/tempserver list");
		CloudInstance.LOGGER.info("/tempserver control <servername> rcon <command>");
		CloudInstance.LOGGER.info("/tempserver control <servername> stop");
		CloudInstance.LOGGER.info("/tempserver control <servername> ping");
		CloudInstance.LOGGER.info("/tempserver control <servername> info");
	}
	
}
