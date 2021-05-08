package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;

public class CommandStopServer extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			int serverId = Integer.parseInt(args[1]);
			if (ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId) != null) {

				Proxy proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyById(serverId);
				proxy.stopProxy();

			} else if (ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId) != null) {

				SpigotServer server = ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(serverId);
				server.stopServer();

			} else {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[CORE] No Server with ID \"" + serverId + "\" found.");
			}
		}
	}

}
