package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandPingserver extends ConsoleCommand {

	public CommandPingserver(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = 0;
			try {
				serverId = Integer.parseInt(args[1]);
			} catch (Exception exception) {
				CloudInstance.LOGGER.warning("IDs are only integers. Please try again with numbers instead.");
				return;
			}
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {
				if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection() != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection()
							.sendData("ping", System.currentTimeMillis());
					CloudInstance.LOGGER.info("Sent pingrequest to Proxyserver \""
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getProxyName());
				} else {
					CloudInstance.LOGGER.warning("Proxyserver isn't linked.");
				}
			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				SpigotServer spigotServer = ApplicationInterface.getAPI().getInfrastructure()
						.getSpigotServerById(serverId);

				if (spigotServer.getConnection() != null) {
					spigotServer.getConnection().sendData("ping", System.currentTimeMillis());
					CloudInstance.LOGGER.info("Sent pingrequest to Spigotserver \"" + spigotServer.getServerName());
				} else {
					CloudInstance.LOGGER.info("Spigotserver isn't linked.");
				}
			} else {
				CloudInstance.LOGGER.warning("No Server with ID \"" + serverId + "\" found.");
			}
			return;
		}
		CloudInstance.LOGGER.info("/ping <Proxy>");
		CloudInstance.LOGGER.info("/ping <Spigotinstance>");
	}

}
