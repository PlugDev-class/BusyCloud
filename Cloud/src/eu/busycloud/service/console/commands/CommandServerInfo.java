package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.SpigotServer;

public class CommandServerInfo extends ConsoleCommand {

	public CommandServerInfo(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				ProxyServer proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);

				CloudInstance.LOGGER.info("[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				CloudInstance.LOGGER.info("[PLUGIN] Serverinformations");
				CloudInstance.LOGGER.info("[PLUGIN] - Servertype: Proxy");
				CloudInstance.LOGGER.info("[PLUGIN] - Servername: " + proxy.getProxyName());
				CloudInstance.LOGGER.info("[PLUGIN] - Serverport: " + proxy.getPort());
				CloudInstance.LOGGER.info("[PLUGIN] - Serveraddress: localhost:" + proxy.getPort());
				CloudInstance.LOGGER.info("[PLUGIN] - Version: " + proxy.getSoftware().getVersionName());
				CloudInstance.LOGGER.info("[PLUGIN] - Spieleranzahl:" + proxy.getOnlinePlayer().size());
				CloudInstance.LOGGER.info("[PLUGIN] - Verbundene Server: ");
				for (SpigotServer server : proxy.getRegisteredServer()) {
					CloudInstance.LOGGER.info("[PLUGIN]   - " + server.getServerName() + " (localhost:"
							+ server.getPort() + ") ID::" + server.getId());
				}
				CloudInstance.LOGGER.info("[PLUGIN] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId).printInfo();
			} else {
				CloudInstance.LOGGER.info("[CORE] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
