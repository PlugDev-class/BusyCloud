package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandRconServer extends ConsoleCommand {

	public CommandRconServer(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length > 1) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				ProxyServer proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);

				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					stringBuilder.append(args[i] + " ");
				}
				proxy.sendRCON(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
				CloudInstance.LOGGER.info("Send rcon \""
								+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
								+ "\" to Proxy " + proxy.getProxyName());

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				SpigotServer spigotServer = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);

				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					stringBuilder.append(args[i] + " ");
				}
				spigotServer.sendRCON(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
				CloudInstance.LOGGER.info("Send rcon \""
								+ stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1)
								+ "\" to Spigot " + spigotServer.getServerName());
			} else {
				CloudInstance.LOGGER.info("No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
