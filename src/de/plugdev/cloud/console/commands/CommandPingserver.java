package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.infrastructure.SpigotServer;
import de.terrarier.netlistening.api.DataContainer;

public class CommandPingserver extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection() != null) {
					DataContainer container = new DataContainer();
					container.add("ping");
					container.add(System.currentTimeMillis());
					ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection()
							.sendData(container);
					ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Send pingrequest to Proxyserver \""
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getProxyName());
				} else {
					ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Proxyserver isn't linked.");
				}

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				SpigotServer spigotServer = ApplicationInterface.getAPI().getInfrastructure()
						.getSpigotServerById(serverId);

				if (spigotServer.getConnection() != null) {
					DataContainer container = new DataContainer();
					container.add("ping");
					container.add(System.currentTimeMillis());
					spigotServer.getConnection().sendData(container);
					ConsoleColors.write(ConsoleColors.CYAN,
							"[PLUGIN] Send pingrequest to Spigotserver \"" + spigotServer.getServerName());
				} else {
					ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] Spigotserver isn't linked.");
				}
			} else {
				ConsoleColors.write(ConsoleColors.CYAN, "[PLUGIN] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
