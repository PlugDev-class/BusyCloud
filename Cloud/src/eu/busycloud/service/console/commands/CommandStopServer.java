package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandStopServer extends ConsoleCommand {

	public CommandStopServer(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				ProxyServer proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);
				proxy.stopProxy();

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {

				SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);
				server.stopServer();

			} else {
				CloudInstance.LOGGER.info("No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
