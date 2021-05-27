package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;

public class CommandPingserver extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection() != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getConnection()
							.sendData("ping", System.currentTimeMillis());
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Sent pingrequest to Proxyserver \""
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId).getProxyName());
				} else {
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Proxyserver isn't linked.");
				}
			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {
				SpigotServer spigotServer = ApplicationInterface.getAPI().getInfrastructure()
						.getSpigotServerById(serverId);

				if (spigotServer.getConnection() != null) {
					spigotServer.getConnection().sendData("ping", System.currentTimeMillis());
					ConsoleOutput.write(ConsoleOutput.CYAN,
							"[PLUGIN] Sent pingrequest to Spigotserver \"" + spigotServer.getServerName());
				} else {
					ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] Spigotserver isn't linked.");
				}
			} else {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[PLUGIN] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
